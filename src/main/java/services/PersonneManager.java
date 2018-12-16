package services;

import dao.PersonneDAO;
import models.Personne;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@Transactional
@Stateless
public class PersonneManager {

    private PersonneDAO personneDAO;

    @Inject
    public PersonneManager(PersonneDAO personneDAO) {
        this.personneDAO = personneDAO;
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

            p.setNom(personData.get("nom"));
            p.setPrenom(personData.get("prenom"));
            p.setBirthdate(new Date());
            p.setWebsite(personData.get("website"));
            p.setEmail(personData.get("email"));

            personneDAO.addPersonne(p);

            return p;

        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }

    }

}
