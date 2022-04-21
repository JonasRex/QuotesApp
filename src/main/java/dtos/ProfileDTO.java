package dtos;

import entities.Profile;
import entities.Quote;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private List<QuoteDTO> quoteDTOS = new ArrayList<>(); // List of IDs.


    public ProfileDTO(Profile profile) {
        if (profile.getId() != 0)
            this.id = profile.getId();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.email = profile.getEmail();
        for (Quote quote : profile.getQuotesList()) {
            this.quoteDTOS.add(new QuoteDTO(quote));
        }
    }


    public Profile getEntity() {
        Profile p = new Profile(this.firstName, this.lastName, this.email);
        this.quoteDTOS.forEach(quoteDTO -> p.addQuote(quoteDTO.getEntity()));
        return p;
    }

    public static List<ProfileDTO> toList(List<Profile> profiles) {
        return profiles.stream().map(ProfileDTO::new).collect(Collectors.toList());
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

    public List<QuoteDTO> getQuoteDTOS() {
        return quoteDTOS;
    }

    @Override
    public String toString() {
        return "ProfileDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", quoteDTOS=" + quoteDTOS +
                '}';
    }
}
