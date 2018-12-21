package controllers;

import beans.SessionUser;
import services.PersonneManager;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

@RequestScoped
@ManagedBean(name = "controller")
public class FaceletsController {

    private String query;
    private String email;
    private String password;

    @Inject
    private PersonneManager personneManager;


    public String toSearch(){
        return "searchPage";
    }

    public String search(){

        System.out.println("hi : "+query);

        return "searchPage";
    }

    public String login() {

        System.out.println("login : "+email+" "+password);

        if(personneManager.login(email, password) != null){
            return "index";
        }
        else return  "loginPage";

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
}
