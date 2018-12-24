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

    public ArrayList<Personne> findByLastname(String lname){
        return new ArrayList<Personne>(personneDAO.findByName(lname.toUpperCase()));
    }

    public ArrayList<Personne> findByFirstname(String fname){
        return new ArrayList<Personne>(personneDAO.findBySurname(fname));
    }

    public ArrayList<Personne> findByActivite(String activite){return null;}
}
