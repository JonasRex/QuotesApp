package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.QuoteDTO;
import entities.Quote;
import io.restassured.http.ContentType;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
//@Disabled

public class QuoteResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Quote q1, q2, q3;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        q1 = new Quote("First", "First");
        q2 = new Quote("Second", "Second");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Quote.deleteAllRows").executeUpdate();
            em.persist(q1);
            em.persist(q2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        given().when().get("/quote").then().statusCode(200);
    }


    @Test
    public void testCount() throws Exception {
        given()
                .contentType("application/json")
                .get("/quote/count").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(2));
    }

    @Test
    public void testGetAll() {
        List<QuoteDTO> rmeDTOs;

        rmeDTOs = given()
                .contentType("application/json")
                .when()
                .get("/quote")
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .extract().body().jsonPath().getList("", QuoteDTO.class);


        assertEquals(rmeDTOs.size(), 2);
    }


    @Test
    public void testGetById() {
        given()
                .contentType("application/json")
                .get("/quote/{id}", q1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("q", equalTo("First"))
                .body("a", equalTo("First"));
    }



    @Test
    public void testFailByID() {
        System.out.println("================================================================");
        System.out.println("OBS: Test failing on purpose, when finding entity by invalid ID: ");
        System.out.println("================================================================");

        given()
                .contentType("application/json")
                .get("/quote/99999")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("message", equalTo("The Quote entity with ID: 99999 Was not found"));
    }


    @Test
    public void testCreate() {
        q3 = new Quote("Third", "Third");
        String requestBody = GSON.toJson(new QuoteDTO(q3));

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/quote")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("q", equalTo("Third"));
    }

    @Test
    public void updateTest() {
        QuoteDTO quoteDTO = new QuoteDTO(q1);
        quoteDTO.setQ("Last");
        String requestBody = GSON.toJson(quoteDTO);

        given()
                .header("Content-type", ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/quote/"+q1.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(q1.getId()))
                .body("q", equalTo("Last"));
    }

    @Test
    public void testDelete() {
        given()
                .header("Content-type", ContentType.JSON)
                .pathParam("id", q1.getId())
                .delete("/quote/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(q1.getId()));
    }



}
