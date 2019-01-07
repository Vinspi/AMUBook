package unit;

import dao.PersonneDAO;
import models.Activite;
import models.CV;
import models.Personne;
import org.junit.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import services.PersonneManager;

import javax.ejb.embeddable.EJBContainer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class UnitTestPersonneManager {

    /*
    *       Afin de garantir l'unicité de ce test, nous utilisons une librairie de mock : Mockito afin de
    *       simuler le comportement de la couche inférieure.
    */


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

    @Before
    public void initMock(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void register() {


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
    public void login() {


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


        Personne p = new Personne();
        p.setEmail("test");
        p.setSalt("salt".getBytes());
        p.setPassword("password".getBytes());

        Mockito.when(personneDAO.findByEmail("test")).thenReturn(p);

        pm.changePassword("test", "newOne");

        byte[] hashPass = hashPassword(p.getSalt(), "newOne".getBytes());

        Assert.assertTrue(Arrays.equals(p.getPassword(), hashPass));

    }


    @Test
    public void activateAccount() {


        Personne fake = this.fakePerson();

        Mockito.when(personneDAO.findByEmail("mark.johnson@yahoo.fr")).thenReturn(fake);

        pm.changePassword("mark.johnson@yahoo.fr", "newOne");

        Assert.assertFalse(Arrays.equals(fake.getSalt(), "fake".getBytes()));
        Assert.assertTrue(Arrays.equals(fake.getPassword(), hashPassword(fake.getSalt(), "newOne".getBytes())));
        Mockito.verify(personneDAO).update(fake);


    }

    @Test
    public void removeActivity(){

        Personne fake = this.fakePerson();
        List<Activite> activities = new ArrayList<>();
        Activite act;
        long idToRemove = 3;

        for (int i=1;i<=10;i++){
            act = new Activite();
            act.setTitre("fake");
            act.setDescritption("lorem ipsum");
            act.setAnnee(2010);
            act.setNature("Experience pro");
            act.setWebsite("www.google.com");
            act.setId(i);
            activities.add(act);
        }

        CV fakeCV = new CV();
        fakeCV.setActivites(activities);

        fake.setCv(fakeCV);

        Mockito.when(personneDAO.findById(1)).thenReturn(fake);

        pm.removeActivity(idToRemove, 1);

        Assert.assertTrue(fake.getCv().getActivites().size() == 9);

        for (int i=0;i<fake.getCv().getActivites().size();i++){
            Assert.assertFalse(fake.getCv().getActivites().get(i).getId() == idToRemove);
        }

        Mockito.verify(personneDAO).update(fake);

    }

    @Test
    public void changeCVInfo(){

        Personne fake = fakePerson();

        CV fakeCV = new CV();
        fakeCV.setTitre("Titre");
        fakeCV.setDescription("Description");
        fake.setCv(fakeCV);

        Mockito.when(personneDAO.findByEmail("fake")).thenReturn(fake);

        pm.changeCVInfo("Lorem", "Lorem Ipsum", "fake");

        Assert.assertTrue(fake.getCv().getDescription().equals("Lorem Ipsum"));
        Assert.assertTrue(fake.getCv().getTitre().equals("Lorem"));

        Mockito.verify(personneDAO).update(fake);



    }

    @Test
    public void updateActivity(){

        Personne fake = fakePerson();

        long activityId = 3;

        Mockito.when(personneDAO.findById(1)).thenReturn(fake);
        Activite act;
        List<Activite> activities = new ArrayList<>();

        for (int i=1;i<=10;i++){
            act = new Activite();
            act.setTitre("fake");
            act.setDescritption("lorem ipsum");
            act.setAnnee(2010);
            act.setNature("Experience pro");
            act.setWebsite("www.google.com");
            act.setId(i);
            activities.add(act);
        }

        CV fakeCV = new CV();
        fakeCV.setActivites(activities);

        fake.setCv(fakeCV);

        pm.updateActivity("Lorem", "Description", 2014, "Projet", activityId, 1);

        for (int i=0;i<fake.getCv().getActivites().size();i++){
            if(fake.getCv().getActivites().get(i).getId() == activityId){
                Assert.assertTrue(fake.getCv().getActivites().get(i).getTitre().equals("Lorem"));
                Assert.assertTrue(fake.getCv().getActivites().get(i).getDescritption().equals("Description"));
                Assert.assertTrue(fake.getCv().getActivites().get(i).getAnnee() == 2014);
                Assert.assertTrue(fake.getCv().getActivites().get(i).getNature().equals("Projet"));
            }
        }

        Mockito.verify(personneDAO).update(fake);

    }

    @Test
    public void registerWithTemporaryLog(){

        Map<String, String> personne = new HashMap<>();

        personne.put("nom", "VINCENT");
        personne.put("prenom", "Pierre");
        personne.put("website", "VINCENT");
        personne.put("email", "vinspi13@gmail.com");
        personne.put("password", "pantoufle");

        PersonneManager.TemporaryLogs tempoLogs = pm.registerWithTemporaryLog(personne);
        Personne account = tempoLogs.getAccount();
        String temporaryPass = tempoLogs.getTemporaryPassword();

        byte[] hashedPassword = hashPassword(account.getSalt(), temporaryPass.getBytes());

        Assert.assertNotNull(temporaryPass);
        Assert.assertTrue(temporaryPass.length() == 10);
        Assert.assertTrue(Arrays.equals(account.getPassword(), hashedPassword));
        Assert.assertFalse(account.getValide());

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

    private Personne fakePerson(){
        Personne p = new Personne();
        p.setValide(false);
        p.setSalt("salt".getBytes());
        p.setEmail("fake");
        p.setPrenom("fake");
        p.setNom("fake");
        p.setWebsite("fake");
        p.setBirthdate(new Date());
        p.setPassword(hashPassword("salt".getBytes(),"fake".getBytes()));

        return p;
    }

}
