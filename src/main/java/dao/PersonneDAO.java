package dao;

import models.Personne;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PersonneDAO {

    @PersistenceContext(unitName = "AMUBookdbUnit")
    private EntityManager em;

    public double addPersonne(Personne p){
        em.persist(p);
        return p.getId();
    }

}
