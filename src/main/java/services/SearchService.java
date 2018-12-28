package services;

import dao.ActiviteDAO;
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
    private ActiviteDAO activiteDAO;


    @Inject
    public SearchService(PersonneDAO personneDAO, ActiviteDAO activiteDAO) {
        this.personneDAO = personneDAO;
        this.activiteDAO = activiteDAO;
    }

    private String[] parsebySpace(String toBeParsed){return toBeParsed.split(" ");}

    public ArrayList<Personne> findByLastname(String query, boolean exact){
        ArrayList<Personne> results = new ArrayList<>();
        String[] words = parsebySpace(query);
        for(String n : words)
            if(exact)
                results.addAll(new ArrayList<>(personneDAO.findByNameStrict(n)));
            else
                results.addAll(new ArrayList<>(personneDAO.findByName(n)));

        return results;
    }

    public ArrayList<Personne> findByFirstname(String query, boolean exact){
        ArrayList<Personne> results = new ArrayList<>();
        String[] words = parsebySpace(query);
        for(String n : words)
            if(exact)
                results.addAll(new ArrayList<>(personneDAO.findBySurnameStrict(n)));
            else
                results.addAll(new ArrayList<>(personneDAO.findBySurname(n)));

        return results;
    }

    public ArrayList<Personne> findByActivity(String activite, boolean exact){
        ArrayList<Activite> results_tmp = new ArrayList<>();
        String[] words = parsebySpace(activite);
        for(String n : words)
            if(exact)
                results_tmp.addAll(new ArrayList<>(activiteDAO.findByTitleStrict(n)));
            else
                results_tmp.addAll(new ArrayList<>(activiteDAO.findByTitle(n)));

        ArrayList<Personne> results = new ArrayList<>();
        for(Activite a : results_tmp)
            results.add(a.getCv().getPersonne());
        return results;
    }

}
