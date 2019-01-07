package integration;

import beans.SessionUser;
import models.Activite;
import models.Personne;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.PersonneManager;

import javax.ejb.embeddable.EJBContainer;
import javax.inject.Inject;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TestEndToEnd {

    static EJBContainer ejbContainer;


    @BeforeClass
    public static void init(){
        ejbContainer = EJBContainer.createEJBContainer();
    }

    @AfterClass
    public static void end(){
        ejbContainer.close();
    }

    @Test
    public void testPersonneManager() throws NamingException {

        PersonneManager personneManager = (PersonneManager) ejbContainer.getContext().lookup("java:global/AMUBook/PersonneManager");

        Map<String, String> personne = new HashMap<>();

        personne.put("nom", "VINCENT");
        personne.put("prenom", "Pierre");
        personne.put("website", "www.google.com");
        personne.put("password", "pantoufle");
        personne.put("email", "vinspi13@gmail.com");


        personneManager.register(personne);
    }


    @Test
    public void testLogin() throws NamingException {

        PersonneManager personneManager = (PersonneManager) ejbContainer.getContext().lookup("java:global/AMUBook/PersonneManager");


        Personne p = personneManager.login("vinspi13@gmail.com","pantoufle");

        Assert.assertNotNull(p);


    }

    @Test
    public void addActivite() throws NamingException {
        PersonneManager personneManager = (PersonneManager) ejbContainer.getContext().lookup("java:global/AMUBook/PersonneManager");

        Activite a = new Activite();
        a.setTitre("titre");
        a.setDescritption("description");
        a.setWebsite("website");
        a.setNature("Formation");
        a.setAnnee(2018);

        personneManager.addActivity(a, 1);

        Personne p = personneManager.findById(1);


        Assert.assertTrue(p.getCv().getActivites().size() == 9);

    }

}
