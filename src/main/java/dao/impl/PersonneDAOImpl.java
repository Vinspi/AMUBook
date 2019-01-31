package dao.impl;

import dao.PersonneDAO;
import models.Personne;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

//@Transactional
@Stateless
public class PersonneDAOImpl implements PersonneDAO {


//    @PersistenceContext(unitName = "AMUBookdbUnit")
    @PersistenceContext(unitName = "mysqlDB")
    private EntityManager em;

    public Personne update(Personne p){
        return em.merge(p);
    }

    /* tested */

    /**
     * add p to database.
     * @param p
     */
    public void add(Personne p){
        em.persist(p);
        em.flush();
    }

    /* tested */

    /**
     * find user who match the email.
     * @param email
     * @return Personne who match the id.
     */
    public Personne findByEmail(String email){
        try {

            System.out.println("em : "+em);
            Query q = em.createQuery("SELECT p FROM Personne p WHERE p.email LIKE :email").setParameter("email", email);
            return (Personne) q.getSingleResult();

        }catch (NoResultException e){
            System.out.println("exeption rised");
            return null;
        }
    }

    /**
     * remove Personne who match the id from the database.
     * @param id
     */
    public void remove(long id){
        Personne p1;
        try {
            p1 = em.find(Personne.class, id);
            if(p1 != null)
                em.remove(p1);
        } catch (NoResultException e){
            /* id is not in database, we don't remove anything */
        }

    }

    /* tested */

    /**
     * find the user who match the id.
     * @param id
     * @return Personne who match the id.
     */
    public Personne findById(long id){
        try {
            return em.find(Personne.class, id);
        }catch (NoResultException e){
            return null;
        }
    }

    /* tested */

    /**
     * find all user who match the name.
     * @param nom
     * @return List<Personne> a list of Personne who match the name.
     */
    public List<Personne> findByName(String nom){
        try {
            Query q = em.createQuery("SELECT p FROM Personne p WHERE lower(p.nom) LIKE lower(:name)").setParameter("name", "%"+nom+"%");

            return q.getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    /**
     * find all user who match the name strictly.
     * @param nom
     * @return List<Personne> a list of Personne who match the name strictly.
     */
    public List<Personne> findByNameStrict(String nom){
        try {
            Query q = em.createQuery("SELECT p FROM Personne p WHERE lower(p.nom) LIKE lower(:name)").setParameter("name", nom);
            return q.getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    /* tested */
    /**
     * find all user who match the surname.
     * @param surName
     * @return List<Personne> a list of Personne who match the surname.
     */
    public List<Personne> findBySurname(String surName){
        try {
            Query q = em.createQuery("SELECT p FROM Personne p WHERE lower(p.prenom) LIKE lower(:surname)").setParameter("surname", "%"+surName+"%");
            return q.getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    /**
     * find all user who match the surname strictly.
     * @param surName
     * @return List<Personne> a list of Personne who match the surname strictly.
     */
    public List<Personne> findBySurnameStrict(String surName){
        try {
            Query q = em.createQuery("SELECT p FROM Personne p WHERE lower(p.prenom) LIKE lower(:surName)").setParameter("surName", surName);
            return q.getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    /* tested */

    /**
     * find all user int he database.
     * @return List<Personne> liast of all Personne in the database.
     */
    public List<Personne> findAll(){
        Query q = em.createQuery("SELECT p FROM Personne p");
        return q.getResultList();
    }

}
