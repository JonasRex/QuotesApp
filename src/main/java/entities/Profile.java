package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profile")
@NamedQuery(name = "Profile.deleteAllRows", query = "DELETE from Profile")
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @OneToOne(mappedBy = "profile")
    private User user;

    @JoinTable(
            name = "profile_quote",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "quote_id"))
    @ManyToMany
    private List<Quote> quotesList;

    public Profile(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.quotesList = new ArrayList<>();
    }

    public Profile() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        user.setProfile(this);
    }

    public List<Quote> getQuotesList() {
        return quotesList;
    }

    public void addQuote(Quote quote) {
        this.quotesList.add(quote);
        if(!quote.getProfileList().contains(this)){
            quote.addProfile(this);
        }
    }

    public void setQuotesList(List<Quote> quotesList) {
        this.quotesList = quotesList;
    }

    public void removeQuote(Quote quote) {
        this.quotesList.remove(quote);
        if(!quote.getProfileList().contains(this))
            quote.getProfileList().remove(this);
    }


    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", user=" + user +
                ", quotesList=" + quotesList +
                '}';
    }
}