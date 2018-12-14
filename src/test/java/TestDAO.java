import dao.PersonneDAO;
import models.Personne;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import java.util.Date;

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

            Personne p = new Personne();
            p.setNom("VINCENT");
            p.setPrenom("Pierre");
            p.setEmail("vinspi13@gmail.com");
            p.setBirthdate("test");
            p.setPassword("pantoufle");
            p.setWebsite("www.google.com");

            personneDAO.addPersonne(p);

    }
}
