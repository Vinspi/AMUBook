package dao.impl;

import dao.ActiviteDAO;
import models.Activite;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

//@Transactional
@Stateless
public class ActiviteDAOImpl implements ActiviteDAO {

//    @PersistenceContext(unitName = "AMUBookdbUnit")
    @PersistenceContext(unitName = "mysqlDB")
    private EntityManager em;

    /**
     *
     * @param id
     * @return Activite with matching id.
     */
    public Activite findById(long id) {
        try {
            return em.find(Activite.class, id);
        }
        catch (NoResultException e){
            return null;
        }
    }

    /**
     *
     * @param title
     * @return Activite with matching title.
     */
    public List<Activite> findByTitleStrict(String title){try {
        Query q = em.createQuery("SELECT a FROM Activite a WHERE lower(a.titre) LIKE lower(:title)").setParameter("title", title);
            return q.getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    /**
     *
     * @param title
     * @return Activite with matching title in the title.
     */
    public List<Activite> findByTitle(String title){
        try {
            Query q = em.createQuery("SELECT a FROM Activite a WHERE lower(a.titre) LIKE lower(:title)").setParameter("title", "%"+title+"%");
            return q.getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    /**
     *
     * @param activite
     * perform an update of the provided activite.
     * @return Activite updated.
     */
    public Activite update(Activite activite){

        return em.merge(activite);
    }

    /**
     *
     * @param id
     * remove the activite identified by the id.
     */
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

    /**
     * add the activite to the database.
     * @param activite
     */
    public void add(Activite activite){
        /* no need for the moment */

        em.persist(activite);

    }

    /**
     * return all activity in the database .
     * @return List<Activite> list of all activity in database.
     */
    public List<Activite> findAll() {
        /* no need for the moment */
        return null;
    }

}

