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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * PersonneManager
 * Purpose : methods to create, find, connect and update/modify a person and his CV
 */
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
    /**
    * @param email
     * @param password
     *
     * log in the user if right email and password is provided.
     *
     * @return Personne logged in or null if wrong login / password.
     *
    */
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

    /**
     *
     * @param personData
     *
     * register the user with a temporary password, this password is going to be changed when the first login attempt will occur.
     *
     * @return TemporaryLogs created.
     */
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

    /**
     *
     * @param personData
     *
     * register an account to the database based on the informations in the Map param.
     *
     * @return Personne registered.
     */
    public Personne register(Map<String, String> personData) {

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
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date birthDate = new Date();
            try {
                birthDate = sdf.parse(personData.get("birthdate"));
            }catch(ParseException ignore){}
            p.setBirthdate(birthDate);
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

    /**
     *
     * @param activite
     * @param personId
     *
     * add the activity to the CV of the user identified by the personId parameter.
     */
    public void addActivity(Activite activite, long personId) {


        Personne p = personneDAOImpl.findById(personId);

        if(!verifyActivity(activite))
            return;

        if(p == null){
            return;
        }

        activite.setCv(p.getCv());
        p.getCv().getActivites().add(activite);


        personneDAOImpl.update(p);

    }

    /* tested */

    /**
     *
     * @param id
     * @param userId
     *
     * remove the activity identified by id in the CV of the user identified by userId.
     *
     * @return Personne object on which the action is performed.
     */
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

    /**
     *
     * @param email
     *
     * validate the account identified by the email provided by the email parameter.
     */
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

    /**
     *
     * @param email
     * @param newOne
     *
     * Replace the current password of the user identified by the param email by the newOne.
     */
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

    /**
     *
     * @param id
     * Find user identified by id.
     * @return Personne matching the id.
     */
    public Personne findById(long id){
        return personneDAOImpl.findById(id);
    }

    /* will not be tested */
    /**
     *
     * @param email
     * Find user identified by email.
     * @return Personne matching th email.
     */
    public Personne findByEmail(String email){
        return personneDAOImpl.findByEmail(email);
    }

    /* tested */

    /**
     *
     * @param newTitle
     * @param newDescription
     * @param email
     *
     * Replace all the info provided (title and description) in the CV identified by email.
     *
     * @return Personne on which the action is performed.
     */
    public Personne changeCVInfo(String newTitle, String newDescription, String email) {

        Personne p = personneDAOImpl.findByEmail(email);

        if(newTitle == null)
            return p;

        p.getCv().setTitre(newTitle);
        p.getCv().setDescription(newDescription);

        personneDAOImpl.update(p);

        return p;

    }

    /**
     *
     * @param newTitle
     * @param newDescription
     * @param email
     *
     * Replace all the info provided (title, description and website) in the CV identified by email.
     *
     * @return Personne on which the action is performed.
     */
    public Personne changeCVInfo(String newTitle, String newDescription,String newWebsite, String email) {

        Personne p = personneDAOImpl.findByEmail(email);

        if(newTitle == null)
            return p;

        p.getCv().setTitre(newTitle);
        p.getCv().setDescription(newDescription);
        p.setWebsite(newWebsite);

        personneDAOImpl.update(p);

        return p;

    }

    /* tested */

    /**
     *
     * @param title
     * @param description
     * @param year
     * @param type
     * @param id
     * @param personId
     *
     * replace all the info provided (title, description, year and type) in the activity n°id for the user identified by personId.
     *
     * @return Personne on which the action is performed.
     */
    public Personne updateActivity(String title, String description, int year, String type, long id, long personId) {


        Personne p = personneDAOImpl.findById(personId);

        System.out.println("nouveau titre : "+title);

        if (title.length() < 0 || year < 1900)
            return p;


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
    /**
     *
     * @param title
     * @param description
     * @param year
     * @param type
     * @param id
     * @param personId
     *
     * replace all the info provided (title, description, year, type and website) in the activity n°id for the user identified by personId.
     *
     * @return on which the action is performed.
     */
    public Personne updateActivity(String title, String description, int year, String type, String website, long id, long personId) {

        Personne p = personneDAOImpl.findById(personId);

        System.out.println("nouveau titre : "+title);

        if (title.length() < 1 || year < 1900)
            return p;

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

    /**
     * remove the account identified by id
     * @param id
     */
    public void removeAccount(long id) {
        personneDAOImpl.remove(id);
    }

    private boolean verifyActivity(Activite a){
        if(a.getAnnee() < 1900 || a.getTitre() == null) return false;
        return true;
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


