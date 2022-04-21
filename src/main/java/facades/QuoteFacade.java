package facades;

import entities.Quote;
import errorhandling.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class QuoteFacade implements IFacade<Quote>{
    private static QuoteFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private QuoteFacade() {}

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static IFacade<Quote> getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new QuoteFacade();
        }
        return instance;
    }

    @Override
    public Quote create(Quote quote) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(quote);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return quote;
    }

    @Override
    public Quote getById(int id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Quote quote = em.find(Quote.class, id);
        if (quote == null)
            throw new EntityNotFoundException("The Quote entity with ID: "+id+" Was not found");
        return quote;
    }

    @Override
    public List<Quote> getAll() {
        EntityManager em = getEntityManager();
        TypedQuery<Quote> query = em.createQuery("SELECT q FROM Quote q", Quote.class);
        return query.getResultList();
    }

    @Override
    public Quote update(Quote quote) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Quote q = em.find(Quote.class, quote.getId());
        if(q == null){
            throw new EntityNotFoundException("Quote with ID: " + quote.getId() + " not found");
        }
        q.setQuote(quote.getQuote());
        q.setAuthor(quote.getAuthor());

        em.getTransaction().begin();
        Quote updated = em.merge(quote);
        em.getTransaction().commit();
        return updated;
    }

    @Override
    public Quote delete(int id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Quote q = em.find(Quote.class, id);
        if (q == null)
            throw new EntityNotFoundException("Could not remove Quote with id: "+id);
        em.getTransaction().begin();
        em.remove(q);
        em.getTransaction().commit();
        return q;
    }

    @Override
    public Quote addRelation(int id1, int id2) throws EntityNotFoundException {
        return null;
    }

    @Override
    public Quote removeRelation(int id1, int id2) throws EntityNotFoundException {
        return null;
    }

    @Override
    public long getCount() {
        EntityManager em = getEntityManager();
        try{
            return (long)em.createQuery("SELECT COUNT(q) FROM Quote q").getSingleResult();
        }finally{
            em.close();
        }
    }
}
