import beans.SessionUser;
import dao.PersonneDAO;
import models.Activite;
import models.CV;
import models.Personne;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.*;
import services.PersonneManager;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TestPersonneManager {

    static EJBContainer ejbContainer;

    @Mock
    PersonneDAO personneDAO;

    @InjectMocks
    PersonneManager pm;

    @BeforeClass
    public static void init(){
        ejbContainer = EJBContainer.createEJBContainer();
    }

    @AfterClass
    public static void end(){
        ejbContainer.close();
    }

    @Test
    public void register() {

        MockitoAnnotations.initMocks(this);

        Mockito.when(personneDAO.findByEmail("vinspi13@gmail.com")).thenReturn(new Personne());

        /* register */

        Map<String, String> personne = new HashMap<>();

        personne.put("nom", "VINCENT");
        personne.put("prenom", "Pierre");
        personne.put("website", "VINCENT");
        personne.put("email", "vinspi13@gmail.com");
        personne.put("password", "pantoufle");


        Personne p = pm.register(personne);
        Assert.assertNull(p);

        personne = new HashMap<>();

        personne.put("nom", "VINCENT");
        personne.put("prenom", "Pierre");
        personne.put("website", "VINCENT");
        personne.put("password", "pantoufle");
        personne.put("email", "newemail@valid.email");


        Personne p2 = pm.register(personne);

        Assert.assertNotNull(p2);
        Assert.assertTrue(Arrays.equals(p2.getPassword(), hashPassword(p2.getSalt(), "pantoufle".getBytes())));


    }

    @Test
    public void login() throws NamingException {

        MockitoAnnotations.initMocks(this);

        /* login */

        Personne p = new Personne();
        byte[] salt = "ImAsEcUrEsAlT".getBytes();
        p.setPassword(hashPassword(salt, "pantoufle".getBytes()));
        p.setSalt(salt);
        p.setNom("VINCENT");
        p.setEmail("vinspi13@gmail.com");
        p.setPrenom("Pierre");

        Mockito.when(personneDAO.findByEmail("vinspi13@gmail.com")).thenReturn(p);

        Personne p2 = pm.login("vinspi13@gmail.com", "pantoufle");

        Assert.assertNotNull(p2);

        Personne p3 = pm.login("vinspi@notanemail.com", "pantoufle");

        Assert.assertNull(p3);

        Personne p4 = pm.login("vinspi13@gmail.com", "p@ssword");

        Assert.assertNull(p4);
    }

    @Test
    public void addActivity(){

        MockitoAnnotations.initMocks(this);

        Personne p = new Personne();
        CV cv = new CV();
        cv.setActivites(new LinkedList<>());
        p.setCv(cv);
        Mockito.when(personneDAO.findById(5)).thenReturn(p);

        pm.addActivity(new Activite(), 5);


        Mockito.verify(personneDAO).update(p);

    }

    @Test
    public void changePassword(){

        MockitoAnnotations.initMocks(this);

        Personne p = new Personne();
        p.setEmail("test");
        p.setSalt("salt".getBytes());
        p.setPassword("password".getBytes());

        Mockito.when(personneDAO.findByEmail("test")).thenReturn(p);

        pm.changePassword("test", "newOne");

        byte[] hashPass = hashPassword(p.getSalt(), "newOne".getBytes());

        Assert.assertTrue(Arrays.equals(p.getPassword(), hashPass));

    }


    private byte[] hashPassword(byte[] salt, byte[] password){

        try {

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] hash = md.digest(password);
            return hash;

        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
    }
}
