/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtoFacades.QuoteDTOFacade;
import dtos.QuoteDTO;
import entities.Quote;

/**
 *
 * @author tha
 */
public class Populator {
    public static void populate(){
        IFacade<QuoteDTO> FACADE =  QuoteDTOFacade.getFacade();
        FACADE.create(new QuoteDTO(new Quote("Life is beautiful", "Unknown")));
        FACADE.create(new QuoteDTO(new Quote("It's not over until the..", "Unknown")));
        FACADE.create(new QuoteDTO(new Quote("Dude where's my car?", "Unknown")));
    }

    public static void main(String[] args) {
        populate();
    }
}
