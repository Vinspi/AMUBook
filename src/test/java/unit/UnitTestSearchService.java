package unit;

import dao.impl.ActiviteDAOImpl;
import dao.impl.PersonneDAOImpl;
import models.Activite;
import models.CV;
import models.Personne;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import services.SearchService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class UnitTestSearchService {

    @Mock
    private PersonneDAOImpl personneDAOImpl;

    @Mock
    private ActiviteDAOImpl activiteDAOImpl;

    @InjectMocks
    private SearchService searchService;

    @Before
    public void initMock(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByLastName() {

        Personne fake;

        ArrayList<Personne> fakes = new ArrayList<>();
        ArrayList<Personne> fakes2 = new ArrayList<>();

        ArrayList<Activite> fakeActivites = new ArrayList<>();
        Activite a = new Activite();
        a.setTitre("fake activity");
        fakeActivites.add(a);

        CV fakecv = new CV();
        fakecv.setActivites(fakeActivites);

        for (int i=0;i<10;i++){
            fake = fakePerson();
            fake.setCv(fakecv);
            fakes.add(fake);
        }

        /* people without activity */
        fake = fakePerson();
        fakecv = new CV();
        fakecv.setActivites(new ArrayList<>());
        fake.setCv(fakecv);
        fakes2.add(fake);

        Mockito.when(personneDAOImpl.findByNameStrict("Bernard")).thenReturn(fakes);
        Mockito.when(personneDAOImpl.findByName("Bernard")).thenReturn(fakes);
        Mockito.when(personneDAOImpl.findByName("Arthur")).thenReturn(fakes2);


        HashMap<Personne, Activite> result1 = searchService.findByLastname("Bernard Bernard", false);
        HashMap<Personne, Activite> result2 = searchService.findByLastname("Bernard Bernard", true);
        HashMap<Personne, Activite> result3 = searchService.findByLastname("Michel", true);
        HashMap<Personne, Activite> result4 = searchService.findByLastname("Arthur", false);


        Assert.assertNotNull(result1);
        Assert.assertTrue(result1.size() == 10);

        Assert.assertNotNull(result2);
        Assert.assertTrue(result2.size() == 10);

        Assert.assertNotNull(result3);
        Assert.assertTrue(result3.isEmpty());

        Assert.assertNotNull(result4);
        Assert.assertFalse(result4.isEmpty());


    }

    @Test
    public void findByFirstname() {

        Personne fake;

        ArrayList<Personne> fakes = new ArrayList<>();
        ArrayList<Personne> fakes2 = new ArrayList<>();

        ArrayList<Activite> fakeActivites = new ArrayList<>();
        Activite a = new Activite();
        a.setTitre("fake activity");
        fakeActivites.add(a);

        CV fakecv = new CV();
        fakecv.setActivites(fakeActivites);

        for (int i=0;i<10;i++){
            fake = fakePerson();
            fake.setCv(fakecv);
            fakes.add(fake);
        }

        /* people without activity */
        fake = fakePerson();
        fakecv = new CV();
        fakecv.setActivites(new ArrayList<>());
        fake.setCv(fakecv);
        fakes2.add(fake);

        Mockito.when(personneDAOImpl.findBySurnameStrict("Bernard")).thenReturn(fakes);
        Mockito.when(personneDAOImpl.findBySurname("Bernard")).thenReturn(fakes);
        Mockito.when(personneDAOImpl.findBySurname("Arthur")).thenReturn(fakes2);


        HashMap<Personne, Activite> result1 = searchService.findByFirstname("Bernard Bernard", false);
        HashMap<Personne, Activite> result2 = searchService.findByFirstname("Bernard Bernard", true);
        HashMap<Personne, Activite> result3 = searchService.findByFirstname("Michel", true);
        HashMap<Personne, Activite> result4 = searchService.findByFirstname("Arthur", false);


        Assert.assertNotNull(result1);
        Assert.assertTrue(result1.size() == 10);

        Assert.assertNotNull(result2);
        Assert.assertTrue(result2.size() == 10);

        Assert.assertNotNull(result3);
        Assert.assertTrue(result3.isEmpty());

        Assert.assertNotNull(result4);
        Assert.assertFalse(result4.isEmpty());

    }

    @Test
    public void findByActivity() {

        Personne fake;

        ArrayList<Personne> fakes = new ArrayList<>();

        ArrayList<Activite> fakeActivites = new ArrayList<>();

        Activite a = new Activite();
        CV fakecv = new CV();

        a.setTitre("fake activity");
        a.setCv(fakecv);

        fakecv.setPersonne(fakePerson());

        /* pour les doublons */
        fakeActivites.add(a);
        fakeActivites.add(a);
        fakeActivites.add(a);



        Mockito.when(activiteDAOImpl.findByTitleStrict("truc")).thenReturn(fakeActivites);
        Mockito.when(activiteDAOImpl.findByTitle("truc")).thenReturn(fakeActivites);

        HashMap<Personne, Activite> result1 = searchService.findByActivity("truc", true);
        HashMap<Personne, Activite> result2 = searchService.findByActivity("truc", false);

        Assert.assertNotNull(result1);
        Assert.assertTrue(result1.size() == 1);

        Assert.assertNotNull(result2);
        Assert.assertTrue(result2.size() == 1);


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
