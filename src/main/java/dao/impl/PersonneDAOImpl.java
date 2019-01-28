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
    public void add(Personne p){
        em.persist(p);
        em.flush();
    }

    /* tested */
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


    public void remove(long id){
        Personne p1 = em.find(Personne.class, id);
        em.remove(p1);

    }

    /* tested */
    public Personne findById(long id){
        try {
            return em.find(Personne.class, id);
        }catch (NoResultException e){
            return null;
        }
    }

    /* tested */
    public List<Personne> findByName(String nom){
        try {
            Query q = em.createQuery("SELECT p FROM Personne p WHERE lower(p.nom) LIKE lower(:name)").setParameter("name", "%"+nom+"%");

            return q.getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    public List<Personne> findByNameStrict(String nom){
        try {
            Query q = em.createQuery("SELECT p FROM Personne p WHERE lower(p.nom) LIKE lower(:name)").setParameter("name", nom);
            return q.getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    /* tested */
    public List<Personne> findBySurname(String surName){
        try {
            Query q = em.createQuery("SELECT p FROM Personne p WHERE lower(p.prenom) LIKE lower(:surname)").setParameter("surname", "%"+surName+"%");
            return q.getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    public List<Personne> findBySurnameStrict(String surName){
        try {
            Query q = em.createQuery("SELECT p FROM Personne p WHERE lower(p.prenom) LIKE lower(:surName)").setParameter("surName", surName);
            return q.getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    /* tested */
    public List<Personne> findAll(){
        Query q = em.createQuery("SELECT p FROM Personne p");
        return q.getResultList();
    }

}
