package dao;

import models.Activite;
import models.Personne;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ActiviteDAO {

    @PersistenceContext(unitName = "AMUBookdbUnit")
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

    public List<Activite> findByTitle(String title){  try {
        Query q = em.createQuery("SELECT a FROM Activite a WHERE lower(a.titre) LIKE lower(:title)").setParameter("title", "%"+title+"%");
            return q.getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    public void update(Activite activite){
        em.merge(activite);
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
}