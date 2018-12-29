package beans;

import models.Activite;

import services.PersonneManager;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Singleton
@Startup
public class LifeCycleBean {

    @Inject
    private PersonneManager personneManager;

    @PostConstruct
    public void init() throws InterruptedException{

        Map<String, String> personne = new HashMap<>();

        personne.put("nom", "VINCENT");
        personne.put("prenom", "Pierre");
        personne.put("website", "www.google.com");
        personne.put("password", "pantoufle");
        personne.put("email", "vinspi13@gmail.com");
        personne.put("valid", "true");

        personneManager.register(personne);

        personne = new HashMap<>();

        personne.put("nom", "LOIGNON");
        personne.put("prenom", "Lucas");
        personne.put("website", "www.google.com");
        personne.put("password", "pantoufle");
        personne.put("email", "lucasl@gmail.com");
        personne.put("valid", "true");

        personneManager.register(personne);

        personne = new HashMap<>();

        personne.put("nom", "LOIGNON");
        personne.put("prenom", "Lucas");
        personne.put("website", "www.google.com");
        personne.put("password", "pantoufle");
        personne.put("email", "lucasl@gmail.com");
        personne.put("valid", "true");

        personneManager.register(personne);

        personne = new HashMap<>();


        personne.put("nom", "NONAME");
        personne.put("prenom", "Jean-pierre");
        personne.put("website", "www.google.com");
        personne.put("password", "pantoufle");
        personne.put("email", "jpnoname@gmail.com");
        personne.put("valid", "true");

        personneManager.register(personne);


        Activite activite;



        Random rand = new Random();
        int n;

        for (int i=0;i<8;i++){

            activite = new Activite();
            activite.setAnnee(2012+2*i);
            activite.setDescritption("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
            n = rand.nextInt(3);
            System.out.println("n "+n);
            switch (n) {
                case 0:
                    activite.setNature("Experience pro");
                    break;
                case 1:
                    activite.setNature("Formation");
                    break;
                case 2:
                    activite.setNature("Projet");
                    break;
            }

            activite.setTitre("Developpeur web ("+i+") chez Sopra Steria");
            if(i==6)
                activite.setTitre("Graphiste chez Sopra Steria");

            activite.setWebsite("website");

            personneManager.addActivity(activite, 1);

            Activite actb = new Activite();
            actb.setTitre("Graphiste chez Freelance");
            personneManager.addActivity(actb, 2);

        }


        //System.out.println(personneManager.findById(1).getCv().getActivites().size());

        System.out.println("app initialized");








    }
}
