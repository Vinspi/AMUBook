package unit;

import dao.impl.ActiviteDAOImpl;
import dao.impl.PersonneDAOImpl;
import models.Personne;
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

        Personne fake = fakePerson();

        ArrayList<Personne> fakes = new ArrayList<>();

        for (int i=0;i<10;i++){
            fakes.add(fakePerson());
        }

        Mockito.when(personneDAOImpl.findByNameStrict("Bernard")).thenReturn(fakes);
        Mockito.when(personneDAOImpl.findByName("Bernard")).thenReturn(fakes);




    }

    @Test
    public void findByFirstname() {

    }

    @Test
    public void findByActivity() {

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
