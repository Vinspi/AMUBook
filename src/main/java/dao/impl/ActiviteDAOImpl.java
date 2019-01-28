package dao.impl;

import dao.ActiviteDAO;
import models.Activite;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

//@Transactional
@Stateless
public class ActiviteDAOImpl implements ActiviteDAO {

//    @PersistenceContext(unitName = "AMUBookdbUnit")
    @PersistenceContext(unitName = "mysqlDB")
    private EntityManager em;

    public Activite findById(long id) {
        try {
            return em.find(Activite.class, id);
        }
        catch (NoResultException e){
            return null;
        }
    }

    public List<Activite> findByTitleStrict(String title){try {
        Query q = em.createQuery("SELECT a FROM Activite a WHERE lower(a.titre) LIKE lower(:title)").setParameter("title", title);
            return q.getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    public List<Activite> findByTitle(String title){
        try {
            Query q = em.createQuery("SELECT a FROM Activite a WHERE lower(a.titre) LIKE lower(:title)").setParameter("title", "%"+title+"%");
            return q.getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    public Activite update(Activite activite){

        return em.merge(activite);
    }

    public void remove(long id) {
        try {
            Activite a = em.find(Activite.class, id);
            em.remove(a);
        }
        catch (NoResultException e){
            System.out.println("no result found");
            return;
        }
    }

    public void add(Activite activite){
        /* no need for the moment */

        em.persist(activite);

    }

    public List<Activite> findAll() {
        /* no need for the moment */
        return null;
    }

}

