package controllers;

import beans.SearchResults;
import beans.SessionUser;
import models.Activite;
import models.Personne;
import services.PersonneManager;
import services.SearchService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import java.util.*;

@ManagedBean(name = "controller")
@RequestScoped
public class FaceletsController {

    private String query;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String month;
    private String day;
    private String year;
    private String temporaryPass;
    private String passwordConfirm;
    private Personne personne;
    private String newCVTitle;
    private String newDescription;
    private String activityType;
    private String activityId;
    private String activityDescription;
    private String activityTitle;
    private int activityDate;

    private boolean sr_perfectmatching;



    @ManagedProperty(value = "#{sessionUser}")
    private SessionUser sessionUser;

    @ManagedProperty(value ="#{searchResults}")
    private SearchResults searchResults;

    @Inject
    private PersonneManager personneManager;

    @Inject
    private SearchService searchService;

    @PostConstruct
    public void init(){
        this.month = "01";
        this.day = "1";
        this.year = "1990";


    }


    public String changePassword() {

        if(password.equals(passwordConfirm)){

            personneManager.changePassword(sessionUser.getEmail(), this.password);
            personneManager.activateAccount(sessionUser.getEmail());

            return "index";
        }

        this.password = null;
        this.passwordConfirm = null;

        return "accountValidationPage";
    }

    public String toSearch(){
        return "searchPage";
    }


    public String search(){


        searchResults.cleanFoundByAll();
        searchResults.setFoundByLastname(searchService.findByLastname(query, sr_perfectmatching));
        searchResults.setFoundByFirstname(searchService.findByFirstname(query, sr_perfectmatching));
        searchResults.setFoundByActivity(searchService.findByActivity(query, sr_perfectmatching));


        return "searchResultsPage";
    }

    public String logout() {

        sessionUser.setName(null);
        sessionUser.setEmail(null);
        sessionUser.setSurname(null);

        return "index";
    }

    public String login() {

        Personne p = personneManager.login(email, password);

        if(p != null){

            sessionUser.setEmail(email);
            sessionUser.setName(p.getNom());
            sessionUser.setSurname(p.getPrenom());
            sessionUser.setValidAccount(p.getValide());
            sessionUser.setId(p.getId());


            if (!p.getValide()){
                /* si le compte n'est pas valide on redirige vers la page de validation */

                this.password = null;
                return "accountValidationPage";
            }
            

            return "index";
        }

        else return  "loginPage";

    }

    public String register() {
        /* the caller must be logged in */

        if (sessionUser.getEmail() == null){
            return "loginPage";
        }

        if(Integer.parseInt(this.day) < 10) this.day = "0"+this.day;

        Map<String, String> map = new HashMap<>();

        map.put("email", this.email);
        map.put("nom", this.name);
        map.put("prenom", this.surname);
        map.put("birthdate", this.day+"/"+this.month+"/"+this.year);

        this.temporaryPass = personneManager.registerWithTemporaryLog(map).getTemporaryPassword();

        System.out.println("temporary password is : "+this.temporaryPass);

        return "registerPage";
    }

    public String myCV() {

        personne = personneManager.findByEmail(this.sessionUser.getEmail());

        return "userEditPage";

    }

    public void changeCVInfo() {

        personne = personneManager.changeCVInfo(newCVTitle, newDescription,this.sessionUser.getEmail());

    }

    public void updateActivityList() {

        System.out.println(activityId);



        if(activityId.equals("new")) {

            Activite activite = new Activite();
            activite.setNature(activityType);
            activite.setAnnee(activityDate);
            activite.setDescritption(activityDescription);
            activite.setTitre(activityTitle);


            personneManager.addActivity(activite, sessionUser.getId());

        }
        else {
            System.out.println("update");
            String[] parts = activityId.split("-");
            long id = Long.parseLong(parts[parts.length-1]);
            personneManager.updateActivity(activityTitle, activityDescription, activityDate, activityType, id, sessionUser.getId());

        }

        personne = personneManager.findById(sessionUser.getId());


    }

    public void removeActivity(){


        String[] parts = activityId.split("-");
        long id = Long.parseLong(parts[parts.length-1]);

        personne = personneManager.removeActivity(id, sessionUser.getId());

        System.out.println("size : "+personne.getCv().getActivites().size());
    }

    /* only for testing purpose */
    public String testUserPage() {

        personne = personneManager.findById(1);


        System.out.println("Activity : "+personne.getCv().getActivites());

        return "userPage";
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public SessionUser getSessionUser() {
        return sessionUser;
    }

    public void setSessionUser(SessionUser sessionUser) {
        this.sessionUser = sessionUser;
    }

    public SearchResults getSearchResults() { return searchResults; }

    public void setSearchResults(SearchResults searchResults) {
        this.searchResults = searchResults;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTemporaryPass() {
        return temporaryPass;
    }

    public void setTemporaryPass(String temporaryPass) {
        this.temporaryPass = temporaryPass;
    }


    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public String getNewCVTitle() {
        return newCVTitle;
    }

    public void setNewCVTitle(String newCVTitle) {
        this.newCVTitle = newCVTitle;
    }

    public String getNewDescription() {
        return newDescription;
    }

    public void setNewDescription(String newDescription) {
        this.newDescription = newDescription;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public int getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(int activityDate) {
        this.activityDate = activityDate;
    }


    public boolean isSr_perfectmatching() {
        return sr_perfectmatching;
    }

    public void setSr_perfectmatching(boolean sr_perfectmatching) {
        this.sr_perfectmatching = sr_perfectmatching;
    }
}
