package beans;

import services.PersonneManager;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Startup
public class LifeCycleBean {

    @Inject
//    private PersonneManager personneManager;

    @PostConstruct
    public void init(){

//        Map<String, String> personne = new HashMap<>();
//
//        personne.put("nom", "VINCENT");
//        personne.put("prenom", "Pierre");
//        personne.put("website", "www.google.com");
//        personne.put("password", "pantoufle");
//        personne.put("email", "vinspi13@gmail.com");
//
//
//        personneManager.register(personne);

        System.out.println("app initialized");


    }
}
