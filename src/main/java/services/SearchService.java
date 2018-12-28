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

    public HashMap<Personne, Activite> findByLastname(String query, boolean exact){
        HashMap<Personne, Activite> results = new HashMap<>();
        ArrayList<Personne> resultsP = new ArrayList<>();
        String[] words = parsebySpace(query);
        for(String n : words) {
            if (exact)
                resultsP.addAll(new ArrayList<>(personneDAO.findByNameStrict(n)));
            else
                resultsP.addAll(new ArrayList<>(personneDAO.findByName(n)));
        }

        Activite a = new Activite();
        a.setTitre("-Non renseigné-");

        for(Personne p : resultsP){
            if(p.getCv().getActivites().size() == 0)
                results.put(p, a);
            else
                results.put(p, p.getCv().getActivites().get(0));
        }

        return results;
    }

    public HashMap<Personne, Activite> findByFirstname(String query, boolean exact){
        HashMap<Personne, Activite> results = new HashMap<>();
        ArrayList<Personne> resultsP = new ArrayList<>();
        String[] words = parsebySpace(query);
        for(String n : words) {
            if (exact)
                resultsP.addAll(new ArrayList<>(personneDAO.findBySurnameStrict(n)));
            else
                resultsP.addAll(new ArrayList<>(personneDAO.findBySurname(n)));
        }

        Activite a = new Activite();
        a.setTitre("-Non renseigné-");

        for(Personne p : resultsP){
            if(p.getCv().getActivites().size() == 0)
                results.put(p, a);
            else
                results.put(p, p.getCv().getActivites().get(0));
        }

        return results;
    }

    public HashMap<Personne, Activite> findByActivity(String activite, boolean exact){
        HashMap<Personne, Activite> results = new HashMap<>();
        ArrayList<Activite> results_tmp = new ArrayList<>();
        String[] words = parsebySpace(activite);
        for(String n : words) {
            if (exact)
                results_tmp.addAll(new ArrayList<>(activiteDAO.findByTitleStrict(n)));
            else
                results_tmp.addAll(new ArrayList<>(activiteDAO.findByTitle(n)));
        }

        if(exact)
            results_tmp.addAll(new ArrayList<>(activiteDAO.findByTitleStrict(activite)));

        for(Activite a : results_tmp)
            results.put(a.getCv().getPersonne(), a);

        return results;
    }

}
