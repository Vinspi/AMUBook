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

    // TODO: 21/12/2018 exception rise if result not found, fix that.
    public Personne findById(long id){
        return em.find(Personne.class, id);
    }

    // TODO: 21/12/2018 exception rise if result not found, fix that.
    public List<Personne> findByName(String nom){
        Query q = em.createQuery("SELECT p FROM Personne p WHERE p.nom LIKE :name").setParameter("name", nom);
        return q.getResultList();
    }
    // TODO: 21/12/2018 exception rise if result not found, fix that.
    public List<Personne> findBySurname(String surName){
        Query q = em.createQuery("SELECT p FROM Personne p WHERE p.prenom LIKE :surName").setParameter("surName", surName);
        return q.getResultList();
    }

    public List<Personne> findAll(){
        Query q = em.createQuery("SELECT p FROM Personne p");
        return q.getResultList();
    }

}
