package dtos;

import entities.Quote;

import java.util.List;
import java.util.stream.Collectors;


public class QuoteDTO {
    private int id;
    private String q;
    private String a;



    public QuoteDTO(Quote quote) {
        if(quote.getId() != 0)
            this.id = quote.getId();
        this.q = quote.getQuote();
        this.a = quote.getAuthor();
    }

    public Quote getEntity() {
        Quote quote = new Quote(this.q, this.a);
        quote.setId(this.id);
        return quote;
    }
    public static List<QuoteDTO> toList(List<Quote> quotes) {
        return quotes.stream().map(QuoteDTO::new).collect(Collectors.toList());
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "QuoteDTO{" +
                "id=" + id +
                ", q='" + q + '\'' +
                ", a='" + a + '\'' +
                '}';
    }
}
