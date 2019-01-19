package services;

import dao.ActiviteDAO;
import dao.PersonneDAO;
import dao.impl.ActiviteDAOImpl;
import dao.impl.PersonneDAOImpl;
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

    private PersonneDAO personneDAOImpl;
    private ActiviteDAO activiteDAOImpl;



    @Inject
    public PersonneManager(PersonneDAO personneDAOImpl, ActiviteDAO activiteDAOImpl) {
        this.personneDAOImpl = personneDAOImpl;
        this.activiteDAOImpl = activiteDAOImpl;
    }


    /* tested */
    public Personne login(String email, String password){


        try {
            /* get the Personne if it exist */

            Personne p = personneDAOImpl.findByEmail(email);

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

    /* tested */
    public TemporaryLogs registerWithTemporaryLog(Map<String, String> personData){

        /* generate random password */

        int lowerBound = 48;
        int higherBound = 122;

        Random rand = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        int randChar;

        for (int i=0;i<10;i++){
            randChar = rand.nextInt(higherBound-lowerBound) + lowerBound;
            sb.append(Character.toChars(randChar));
        }




         /***************************************/

        String randomPass = sb.toString();


        personData.put("password", randomPass);
        personData.put("valid", "false");

        Personne p = register(personData);

        System.out.println("register : "+p);

        return new TemporaryLogs(randomPass, p);

    }

    /* tested */
    public Personne register(Map<String, String> personData){

        try {

            /* verify if a personne already exists in db */


            if(personneDAOImpl.findByEmail(personData.get("email")) != null){
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

            personneDAOImpl.add(p);



            return p;

        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }

    }

    /* tested */
    public void addActivity(Activite activite, long personId) {

        Personne p = personneDAOImpl.findById(personId);


        if(p == null){
            return;
        }

        activite.setCv(p.getCv());
        p.getCv().getActivites().add(activite);


        personneDAOImpl.update(p);

    }

    /* tested */
    public Personne removeActivity(long id, long userId) {

        Personne p = personneDAOImpl.findById(userId);

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

        personneDAOImpl.update(p);


        return p;
    }


    /* tested */
    public void activateAccount(String email){

        Personne p = personneDAOImpl.findByEmail(email);

        System.out.println(p);

        if(p == null){
            return;
        }

        p.setValide(true);

        personneDAOImpl.update(p);

    }

    /* tested */
    public void changePassword(String email, String newOne) {

        try {

            Personne p = personneDAOImpl.findByEmail(email);

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

            personneDAOImpl.update(p);

        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    /* will not be tested */
    public Personne findById(long id){
        return personneDAOImpl.findById(id);
    }

    /* will not be tested */
    public Personne findByEmail(String email){
        return personneDAOImpl.findByEmail(email);
    }

    /* tested */
    public Personne changeCVInfo(String newTitle, String newDescription, String email) {

        Personne p = personneDAOImpl.findByEmail(email);
        p.getCv().setTitre(newTitle);
        p.getCv().setDescription(newDescription);

        personneDAOImpl.update(p);

        return p;

    }


    public Personne changeCVInfo(String newTitle, String newDescription,String newWebsite, String email) {

        Personne p = personneDAOImpl.findByEmail(email);
        p.getCv().setTitre(newTitle);
        p.getCv().setDescription(newDescription);
        p.setWebsite(newWebsite);

        personneDAOImpl.update(p);

        return p;

    }

    /* tested */
    public Personne updateActivity(String title, String description, int year, String type, long id, long personId) {

        Personne p = personneDAOImpl.findById(personId);

        for(Activite activite: p.getCv().getActivites()){
            if (activite.getId() == id){
                activite.setTitre(title);
                activite.setDescritption(description);
                activite.setAnnee(year);
                activite.setNature(type);
            }
        }

        personneDAOImpl.update(p);

        return p;

    }

    public Personne updateActivity(String title, String description, int year, String type, String website, long id, long personId) {

        Personne p = personneDAOImpl.findById(personId);

        for(Activite activite: p.getCv().getActivites()){
            if (activite.getId() == id){
                activite.setTitre(title);
                activite.setDescritption(description);
                activite.setAnnee(year);
                activite.setNature(type);
                activite.setWebsite(website);
            }
        }

        personneDAOImpl.update(p);

        return p;

    }

    /* wrapper class for temporary logs */
     public class TemporaryLogs {

         private String temporaryPassword;
         private Personne account;

         public TemporaryLogs(String temporaryPassword, Personne account) {
             this.temporaryPassword = temporaryPassword;
             this.account = account;
         }

         public String getTemporaryPassword() {
             return temporaryPassword;
         }

         public Personne getAccount() {
             return account;
         }
     }

}


