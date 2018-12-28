package controllers;

import beans.SearchResults;
import beans.SessionUser;
import models.Personne;
import org.jboss.logging.annotations.Pos;
import services.PersonneManager;
import services.SearchService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
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

            if (!p.getValide()){
                /* si le compte n'est pas valide on redirige vers la page de validation */

                return "accountValidationPage";
            }

            sessionUser.setEmail(email);
            sessionUser.setName(p.getNom());
            sessionUser.setSurname(p.getPrenom());

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

        this.temporaryPass = personneManager.registerWithTemporaryLog(map);

        System.out.println("temporary password is : "+this.temporaryPass);

        return "registerPage";
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

    public boolean isSr_perfectmatching() {
        return sr_perfectmatching;
    }

    public void setSr_perfectmatching(boolean sr_perfectmatching) {
        this.sr_perfectmatching = sr_perfectmatching;
    }
}
