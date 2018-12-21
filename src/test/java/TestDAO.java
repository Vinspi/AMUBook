import dao.PersonneDAO;
import models.Personne;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Spy;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import java.util.Date;
import java.util.List;

public class TestDAO {

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
    public void testDAOPersonne() throws NamingException{

        PersonneDAO personneDAO = (PersonneDAO) ejbContainer.getContext().lookup("java:global/AMUBook/PersonneDAO");

        /* sélection vide */

        Personne personne = personneDAO.findByEmail("VINCENT");
        Assert.assertNull(personne);

        /* insertions */
        for (int i=0;i<100;i++){
            Personne p = new Personne();
            p.setNom("VINCENT");
            p.setPrenom("Pierre");
            p.setBirthdate(new Date());
            p.setPassword("pantoufle".getBytes());
            p.setSalt("salt".getBytes());
            p.setWebsite("www.google.com");
            p.setEmail("vinspi"+i+"@gmail.com");
            personneDAO.addPersonne(p);

        }

        /* récupération */

        List<Personne> personnes = personneDAO.findAll();
        Assert.assertTrue(personnes.size() == 100);

        /* sélection */

        List<Personne> personnes2 = personneDAO.findByName("VINCENT");
        Assert.assertTrue(personnes2.size() == 100);

        List<Personne> personnes3 = personneDAO.findBySurname("Pierre");
        Assert.assertTrue(personnes3.size() == 100);

        /* ciblage par id */

        Personne p2 = personneDAO.findById(5);
        Assert.assertTrue(p2.getId() == 5);

        /* ciblage par email */

        Personne personne2 = personneDAO.findByEmail("vinspi13@gmail.com");
        Assert.assertNotNull(personne2);

        /* update */

        p2.setPrenom("michel");
        Personne p3 = personneDAO.update(p2);
        Assert.assertTrue(p3.getPrenom() == "michel");

    }
}
