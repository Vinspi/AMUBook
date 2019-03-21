package unit;

import dao.ActiviteDAO;
import dao.impl.ActiviteDAOImpl;
import dao.impl.PersonneDAOImpl;
import models.Activite;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import javax.transaction.Transaction;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class UnitTestActiviteDAO {

    static EJBContainer ejbContainer;

    static ActiviteDAO activiteDAO;
    static long id;


    /*
     *   Ici on utilise une configuration de base de données "in memory" grâce à HSQL,
     *   donc nous n'utiliserons pas de mock.
     */

    @BeforeClass
    public static void init() throws NamingException {
        ejbContainer = EJBContainer.createEJBContainer();
        activiteDAO = (ActiviteDAO) ejbContainer.getContext().lookup("java:global/AMUBook/ActiviteDAOImpl");

        Activite activite = new Activite();
        activite.setTitre("activite");
        activite.setWebsite("website");
        activite.setNature("Experience pro");
        activite.setDescritption("Une description");

        activiteDAO.add(activite);

        id = activite.getId();

    }



    @Before
    public void insertMockDB(){

    }

    @Test
    public void findById(){


        Activite activite = activiteDAO.findById(id);

        Assert.assertNotNull(activite);

    }

    @Test
    public void findByTitleStrict(){

        List<Activite> activites = activiteDAO.findByTitleStrict("activite");

        Assert.assertNotNull(activites);
        Assert.assertTrue(activites.size() == 1);

    }

    @Test
    public void findByTitle(){

        List<Activite> activites = activiteDAO.findByTitleStrict("activite");

        Assert.assertNotNull(activites);
        Assert.assertTrue(activites.size() == 1);

    }

    @Test
    public void update(){

        Activite activite = activiteDAO.findById(id);
        activite.setDescritption("newDesc");
        activiteDAO.update(activite);

        activite = activiteDAO.findById(id);

        Assert.assertTrue(activite.getDescritption().equals("newDesc"));

    }

    @Test
    public void remove(){

        Activite activite = new Activite();
        activiteDAO.add(activite);
        long id_suppr = activite.getId();
        activiteDAO.remove(id_suppr);
        activite = activiteDAO.findById(id_suppr);
        Assert.assertNull(activite);

    }

}
