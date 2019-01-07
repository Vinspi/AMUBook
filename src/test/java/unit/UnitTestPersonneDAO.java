package unit;

import dao.PersonneDAO;
import models.Personne;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Spy;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import javax.transaction.Transactional;
import javax.ws.rs.core.Context;
import java.util.Date;
import java.util.List;


public class UnitTestPersonneDAO {

    static EJBContainer ejbContainer;

    static PersonneDAO personneDAO;
    static int numberOfEntity;


    /*
    *   Ici on utilise une configuration de base de données "in memory" grâce à HSQL,
    *   donc nous n'utiliserons pas de mock.
    */

    @BeforeClass
    public static void init() throws NamingException{
        ejbContainer = EJBContainer.createEJBContainer();
        personneDAO = (PersonneDAO) ejbContainer.getContext().lookup("java:global/AMUBook/PersonneDAO");
        numberOfEntity = personneDAO.findAll().size();

    }

    @AfterClass
    public static void end(){
        ejbContainer.close();
    }


    @Test
    @Transactional
    public void testDAOPersonne() {


        /* sélection vide */

        Personne personne = personneDAO.findByEmail("VINCENT");
        Assert.assertNull(personne);

        /* insertions */
        for (int i=0;i<100;i++){
            Personne p = new Personne();
            p.setNom("Edward");
            p.setPrenom("Norton");
            p.setBirthdate(new Date());
            p.setPassword("pantoufle".getBytes());
            p.setSalt("salt".getBytes());
            p.setWebsite("www.google.com");
            p.setEmail("norton"+i+"@gmail.com");
            personneDAO.addPersonne(p);

        }

        /* récupération */

        List<Personne> personnes = personneDAO.findAll();
        System.out.println(personnes.size());
        Assert.assertTrue(personnes.size() == 100 + numberOfEntity);

        /* sélection */

        List<Personne> personnes2 = personneDAO.findByName("Edward");
        Assert.assertTrue(personnes2.size() == 100);

        List<Personne> personnes3 = personneDAO.findBySurname("Norton");
        Assert.assertTrue(personnes3.size() == 100);

        /* ciblage par id */

        Personne p2 = personneDAO.findById(numberOfEntity + 5);
        Assert.assertTrue(p2.getId() == numberOfEntity + 5);

        /* ciblage par email */

        Personne personne2 = personneDAO.findByEmail("norton12@gmail.com");
        Assert.assertNotNull(personne2);

        /* update */

        p2.setPrenom("michel");
        Personne p3 = personneDAO.update(p2);
        Assert.assertTrue(p3.getPrenom() == "michel");

        /* suppression */

        long idToSuppress = p3.getId();
        personneDAO.removePersonne(idToSuppress);
        Assert.assertNull(personneDAO.findById(idToSuppress));

    }
}
