package dtoFacades;

import dtos.QuoteDTO;
import entities.Quote;
import errorhandling.EntityNotFoundException;
import facades.IFacade;
import facades.QuoteFacade;
import utils.EMF_Creator;
import utils.Utility;
import java.io.IOException;
import java.util.List;

public class QuoteDTOFacade implements IFacade<QuoteDTO> {
    private static IFacade<QuoteDTO> instance;
    private static IFacade<Quote> quoteFacade;


    public QuoteDTOFacade() {
    }

    public static IFacade<QuoteDTO> getFacade() {
        if (instance == null) {
            quoteFacade = QuoteFacade.getFacade(EMF_Creator.createEntityManagerFactory());
            instance = new QuoteDTOFacade();
        }
        return instance;
    }

    @Override
    public QuoteDTO create(QuoteDTO quoteDTO) {
        Quote q = quoteDTO.getEntity();
        q = quoteFacade.create(q);
        return new QuoteDTO(q);
    }

    @Override
    public QuoteDTO getById(int id) throws EntityNotFoundException {
        return new QuoteDTO(quoteFacade.getById(id));
    }

    @Override
    public List<QuoteDTO> getAll() {
        return QuoteDTO.toList(quoteFacade.getAll());
    }

    @Override
    public QuoteDTO update(QuoteDTO quoteDTO) throws EntityNotFoundException {
        Quote quote = new Quote(quoteDTO.getQ(), quoteDTO.getA());
        quote.setId(quoteDTO.getId());
        Quote q = quoteFacade.update(quote);
        return new QuoteDTO(q);
    }

    @Override
    public QuoteDTO delete(int id) throws EntityNotFoundException {
        return new QuoteDTO(quoteFacade.delete(id));
    }

    @Override
    public QuoteDTO addRelation(int id1, int id2) throws EntityNotFoundException {
        return null;
    }

    @Override
    public QuoteDTO removeRelation(int id1, int id2) throws EntityNotFoundException {
        return null;
    }

    @Override
    public long getCount() {
        return quoteFacade.getCount();
    }
}