package controllers;

import beans.*;
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

/*
    Main controller of the app
 */

@ManagedBean(name = "controller")
@RequestScoped
public class FaceletsController {

    private String query;
    private String email;
    private String password;
    private String name;

    private Personne personne;
    private String newCVTitle;
    private String newDescription;
    private String newWebsite;
    private String activityType;
    private String activityId;
    private String activityDescription;
    private String activityTitle;
    private String activityWebsite;
    private int activityDate;
    private String userId;

    private boolean error;


    private boolean sr_perfectmatching;

    /* backing beans for web component */
    @ManagedProperty(value = "#{loginPage}")
    private LoginPageBean loginPage;

    @ManagedProperty(value = "#{registerPage}")
    private RegisterPageBean registerPage;

    @ManagedProperty(value = "#{accountPage}")
    private AccountPageBean accountPage;

    @ManagedProperty(value = "#{validationPage}")
    private AccountValidationPage validationPage;

    /***********************************/

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

        registerPage.setDay("1");
        registerPage.setMonth("01");
        registerPage.setYear("1990");
        accountPage.setError(false);

    }

    public void changePasswordAjax() {

        if(accountPage.getPassword().length() != 0 && accountPage.getPassword().equals(accountPage.getPasswordConfirm())){

            personneManager.changePassword(sessionUser.getEmail(), accountPage.getPassword());
            personneManager.activateAccount(sessionUser.getEmail());

            accountPage.setError(false);
        }
        else {
            accountPage.setError(true);
        }
        personne = personneManager.findByEmail(sessionUser.getEmail());
        accountPage.setPersonne(personneManager.findByEmail(sessionUser.getEmail()));

        accountPage.setPassword(null);
        accountPage.setPasswordConfirm(null);

    }


    public String changePassword() {

        if(validationPage.getPassword().length() != 0 && validationPage.getPassword().equals(validationPage.getPasswordConfirm())){

            personneManager.changePassword(sessionUser.getEmail(), validationPage.getPassword());
            personneManager.activateAccount(sessionUser.getEmail());

            return "index";
        }

        validationPage.setPassword(null);
        validationPage.setPasswordConfirm(null);

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

        System.out.println("email : "+loginPage.getEmail());
        Personne p = personneManager.login(loginPage.getEmail(), loginPage.getPassword());

        if(p != null){

            sessionUser.setEmail(loginPage.getEmail());
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
        else {
            loginPage.setError(true);
            return  "loginPage";
        }

    }

    public String register() {
        
        /* the caller must be logged in */
        if (sessionUser.getEmail() == null){
            return "loginPage";
        }

        /* if user try to bypass ... */
        if(registerPage.getEmail().length() < 1 || registerPage.getName().length() < 1 || registerPage.getSurname().length() < 1){
            return "registerPage";
        }

        if(Integer.parseInt(registerPage.getDay()) < 10) registerPage.setDay("0"+registerPage.getDay());

        Map<String, String> map = new HashMap<>();

        map.put("email", registerPage.getEmail());
        map.put("nom", registerPage.getName());
        map.put("prenom", registerPage.getSurname());
        map.put("birthdate", registerPage.getDay()+"/"+registerPage.getMonth()+"/"+registerPage.getYear());

        registerPage.setTemporaryPass(personneManager.registerWithTemporaryLog(map).getTemporaryPassword());

        System.out.println("temporary password is : "+registerPage.getTemporaryPass());

        return "registerPage";
    }

    public String myCV() {

        personne = personneManager.findByEmail(this.sessionUser.getEmail());

        return "userEditPage";

    }

    public void changeCVInfo() {

        System.out.println("change cv info");

        personne = personneManager.changeCVInfo(newCVTitle, newDescription,newWebsite,this.sessionUser.getEmail());

    }

    public void foo() {
        System.out.println("i'm foo");
    }

    public void updateActivityList() {


        System.out.println("coucou "+activityId);



        if(activityId.equals("new")) {

            Activite activite = new Activite();
            activite.setNature(activityType);
            activite.setAnnee(activityDate);
            activite.setDescritption(activityDescription);
            activite.setTitre(activityTitle);
            activite.setWebsite(activityWebsite);


            personneManager.addActivity(activite, sessionUser.getId());

        }
        else {
            System.out.println("update : "+activityWebsite);
            String[] parts = activityId.split("-");
            long id = Long.parseLong(parts[parts.length-1]);
            personneManager.updateActivity(activityTitle, activityDescription, activityDate, activityType, activityWebsite, id, sessionUser.getId());

        }

        personne = personneManager.findById(sessionUser.getId());


    }

    public String deleteAccount() {

        /* remove the account */
        personneManager.removeAccount(sessionUser.getId());

        /* log out the user */
        sessionUser.setName(null);
        sessionUser.setEmail(null);
        sessionUser.setSurname(null);

        /* then redirect to front page */

        return "index";
    }

    public void removeActivity(){


        String[] parts = activityId.split("-");
        long id = Long.parseLong(parts[parts.length-1]);

        personne = personneManager.removeActivity(id, sessionUser.getId());

        System.out.println("size : "+personne.getCv().getActivites().size());
    }

    public String seeProfile(){

        personne = personneManager.findById(Long.parseLong(userId));

        return "userPage";

    }

    public String accountInfo(){

        accountPage.setPersonne(personneManager.findByEmail(sessionUser.getEmail()));

        return "accountPage";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getNewWebsite() {
        return newWebsite;
    }

    public void setNewWebsite(String newWebsite) {
        this.newWebsite = newWebsite;
    }

    public String getActivityWebsite() {
        return activityWebsite;
    }

    public void setActivityWebsite(String activityWebsite) {
        this.activityWebsite = activityWebsite;
    }

    public LoginPageBean getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(LoginPageBean loginPage) {
        this.loginPage = loginPage;
    }

    public RegisterPageBean getRegisterPage() {
        return registerPage;
    }

    public void setRegisterPage(RegisterPageBean registerPage) {
        this.registerPage = registerPage;
    }

    public AccountPageBean getAccountPage() {
        return accountPage;
    }

    public void setAccountPage(AccountPageBean accountPage) {
        this.accountPage = accountPage;
    }

    public AccountValidationPage getValidationPage() {
        return validationPage;
    }

    public void setValidationPage(AccountValidationPage validationPage) {
        this.validationPage = validationPage;
    }

    public SessionUser getSessionUser() {
        return sessionUser;
    }

    public void setSessionUser(SessionUser sessionUser) {
        this.sessionUser = sessionUser;
    }

    public SearchResults getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(SearchResults searchResults) {
        this.searchResults = searchResults;
    }
}
