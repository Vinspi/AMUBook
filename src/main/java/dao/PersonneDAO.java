package dao;

import models.Personne;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class PersonneDAO {

    @PersistenceContext(unitName = "AMUBookdbUnit")
    private EntityManager em;

    public Personne update(Personne p){
        return em.merge(p);
    }

    public double addPersonne(Personne p){
        em.persist(p);
        return p.getId();
    }

    public Personne findByEmail(String email){
        try {

            Query q = em.createQuery("SELECT p FROM Personne p WHERE p.email LIKE :email").setParameter("email", email);
            return (Personne) q.getSingleResult();

        }catch (NoResultException e){
            System.out.println("exeption rised");
            return null;
        }
    }

    public void removePersonne(Personne p){
        Personne p1 = em.find(Personne.class, p);
        em.remove(p1);

    }

    public Personne findById(long id){
        try {
            return em.find(Personne.class, id);
        }catch (NoResultException e){
            return null;
        }
    }

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
            Query q = em.createQuery("SELECT p FROM Personne p WHERE p.nom LIKE :name").setParameter("name", nom);
            return q.getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

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
            Query q = em.createQuery("SELECT p FROM Personne p WHERE p.prenom LIKE :surName").setParameter("surName", surName);
            return q.getResultList();
        }catch (NoResultException e){
            return null;
        }
    }


    public List<Personne> findAll(){
        Query q = em.createQuery("SELECT p FROM Personne p");
        return q.getResultList();
    }

}
