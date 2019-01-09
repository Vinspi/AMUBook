package services;

import dao.ActiviteDAO;
import dao.PersonneDAO;
import dao.impl.ActiviteDAOImpl;
import dao.impl.PersonneDAOImpl;
import models.Activite;
import models.Personne;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.*;

@Transactional
@Stateless
public class SearchService {

    private PersonneDAO personneDAOImpl;
    private ActiviteDAO activiteDAOImpl;


    @Inject
    public SearchService(PersonneDAO personneDAOImpl, ActiviteDAO activiteDAOImpl) {
        this.personneDAOImpl = personneDAOImpl;
        this.activiteDAOImpl = activiteDAOImpl;
    }

    private String[] parsebySpace(String toBeParsed){return toBeParsed.split(" ");}

    /* tested */
    public HashMap<Personne, Activite> findByLastname(String query, boolean exact){
        HashMap<Personne, Activite> results = new HashMap<>();
        ArrayList<Personne> resultsP = new ArrayList<>();
        String[] words = parsebySpace(query);
        for(String n : words) {
            if (exact)
                resultsP.addAll(new ArrayList<>(personneDAOImpl.findByNameStrict(n)));
            else
                resultsP.addAll(new ArrayList<>(personneDAOImpl.findByName(n)));
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

    /* tested */
    public HashMap<Personne, Activite> findByFirstname(String query, boolean exact){
        HashMap<Personne, Activite> results = new HashMap<>();
        ArrayList<Personne> resultsP = new ArrayList<>();
        String[] words = parsebySpace(query);
        for(String n : words) {
            if (exact)
                resultsP.addAll(new ArrayList<>(personneDAOImpl.findBySurnameStrict(n)));
            else
                resultsP.addAll(new ArrayList<>(personneDAOImpl.findBySurname(n)));
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

    /* tested */
    public HashMap<Personne, Activite> findByActivity(String activite, boolean exact){
        HashMap<Personne, Activite> results = new HashMap<>();
        ArrayList<Activite> results_tmp = new ArrayList<>();
        String[] words = parsebySpace(activite);
        for(String n : words) {
            if (exact)
                results_tmp.addAll(new ArrayList<>(activiteDAOImpl.findByTitleStrict(n)));
            else
                results_tmp.addAll(new ArrayList<>(activiteDAOImpl.findByTitle(n)));
        }

        if(exact)
            results_tmp.addAll(new ArrayList<>(activiteDAOImpl.findByTitleStrict(activite)));

        for(Activite a : results_tmp)
            results.put(a.getCv().getPersonne(), a);

        return results;
    }

}
