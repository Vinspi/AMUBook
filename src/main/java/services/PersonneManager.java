package services;

import beans.SessionUser;
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
public class PersonneManager {

    private PersonneDAO personneDAO;

    private ActiviteDAO activiteDAO;


    @Inject
    public PersonneManager(PersonneDAO personneDAO, ActiviteDAO activiteDAO) {
        this.personneDAO = personneDAO;
        this.activiteDAO = activiteDAO;
    }

    public Personne login(String email, String password){


        try {
            /* get the Personne if it exist */

            Personne p = personneDAO.findByEmail(email);

            if (p == null){
                return null;
            }

            /* hash the password */

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] salt = p.getSalt();

            md.update(salt);
            byte[] hash = md.digest(password.getBytes());

            if (Arrays.equals(hash,p.getPassword())){

                return p;
            }
            else return null;


        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }

    }

    public String registerWithTemporaryLog(Map<String, String> personData){

        /* generate random password */

        String randomPass = SecureRandom.getSeed(8).toString();

        personData.put("password", randomPass);
        personData.put("valid", "false");

        Personne p = register(personData);

        System.out.println("register : "+p);

        return randomPass;

    }

    public Personne register(Map<String, String> personData){

        try {

            /* verify if a personne already exists in db */


            if(personneDAO.findByEmail(personData.get("email")) != null){
                return null;
            }

            Personne p = new Personne();

            /* password and salt */
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] salt = SecureRandom.getSeed(128);

            p.setSalt(salt);

            md.update(salt);

            byte[] passHash = md.digest((personData.get("password")).getBytes());


            p.setPassword(passHash);

            /* other settings for the Personne bean */

            CV cv = new CV();
            cv.setDescription("This the default description");
            cv.setTitre("My CV");
            cv.setPersonne(p);

            p.setNom(personData.get("nom"));
            p.setPrenom(personData.get("prenom"));
            p.setBirthdate(new Date());
            p.setWebsite(personData.get("website"));
            p.setEmail(personData.get("email"));
            p.setCv(cv);
            p.setValide(Boolean.parseBoolean(personData.get("valid")));

            personneDAO.addPersonne(p);



            return p;

        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }

    }

    public void addActivity(Activite activite, long personId) {

        Personne p = personneDAO.findById(personId);


        if(p == null){
            return;
        }

        activite.setCv(p.getCv());
        p.getCv().getActivites().add(activite);

        personneDAO.update(p);

    }

    public Personne removeActivity(long id, long userId) {

        Personne p = personneDAO.findById(userId);

        List<Activite> activites = p.getCv().getActivites();

        int idToRemove = -1;

        for(int i=0;i<activites.size();i++){
            if(activites.get(i).getId() == id){
                idToRemove = i;
                break;
            }
        }

        if(idToRemove > -1)
            p.getCv().getActivites().remove(idToRemove);

        personneDAO.update(p);


        return p;
    }

    public Map<String, List<Object>> search(String query){




        return null;
    }

    public void activateAccount(String email){

        System.out.println("activate account for : "+email);

        Personne p = personneDAO.findByEmail(email);

        System.out.println(p);

        if(p == null){
            return;
        }

        p.setValide(true);

        personneDAO.update(p);

    }

    public void changePassword(String email, String newOne) {

        try {

            Personne p = personneDAO.findByEmail(email);

            if (p == null) {
                return;
            }

            /* password and salt */
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] salt = SecureRandom.getSeed(128);



            md.update(salt);
            byte[] passHash = md.digest(newOne.getBytes());

            p.setSalt(salt);
            p.setPassword(passHash);

            personneDAO.update(p);

        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    public Personne findById(long id){
        return personneDAO.findById(id);
    }

    public Personne findByEmail(String email){
        return personneDAO.findByEmail(email);
    }

    public Personne changeCVInfo(String newTitle, String newDescription, String email) {

        Personne p = personneDAO.findByEmail(email);
        p.getCv().setTitre(newTitle);
        p.getCv().setDescription(newDescription);

        personneDAO.update(p);

        return p;

    }

    public Personne updateActivity(String title, String description, int year, String type, long id, long personId) {

        Personne p = personneDAO.findById(personId);

        for(Activite activite: p.getCv().getActivites()){
            if (activite.getId() == id){
                activite.setTitre(title);
                activite.setDescritption(description);
                activite.setAnnee(year);
                activite.setNature(type);
            }
        }

        personneDAO.update(p);

        return p;

    }


}
