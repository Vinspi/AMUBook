package services;

import dao.PersonneDAO;
import models.Activite;
import models.CV;
import models.Personne;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@Transactional
@Stateless
public class SearchService {

    private PersonneDAO personneDAO;


    @Inject
    public SearchService(PersonneDAO personneDAO) {
        this.personneDAO = personneDAO;
    }

    public ArrayList<Personne> findByLastname(String lname, boolean exact){
        if(exact)
            return new ArrayList<>(personneDAO.findByNameStrict(lname));
        else
            return new ArrayList<>(personneDAO.findByName(lname));
    }

    public ArrayList<Personne> findByFirstname(String fname, boolean exact){
        if(exact)
            return new ArrayList<>(personneDAO.findBySurnameStrict(fname));
        else
            return new ArrayList<>(personneDAO.findBySurname(fname));
    }

    public ArrayList<Personne> findByActivity(String activite, boolean exact){return new ArrayList<>();}


    public ArrayList<Personne> findByAllUnique(String query, boolean exact){

        ArrayList<Personne> result = new ArrayList<>();
        for(Personne p : findByLastname(query, exact)){
            if(!result.contains(p)) {
                result.add(p);
            }
        }
        for(Personne p : findByFirstname(query, exact)){
            if(!result.contains(p)) {
                result.add(p);
            }
        }
        for(Personne p : findByActivity(query, exact)){
            if(!result.contains(p)) {
                result.add(p);
            }
        }
        return result;
    }
}
