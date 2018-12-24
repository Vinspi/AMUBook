package beans;

import models.Personne;

import javax.ejb.Stateful;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;

@Stateful
@ManagedBean(name = "searchResults")
@SessionScoped
public class SearchResults implements Serializable {

    public SearchResults(){}

    private ArrayList<Personne> foundByLastname;
    private ArrayList<Personne> foundByFirstname;
    private ArrayList<Personne> foundByActivity;

    // ******************************************** //

    public ArrayList<Personne> getFoundByLastname() {
        return foundByLastname;
    }

    public void setFoundByLastname(ArrayList<Personne> foundByLastname) {
        this.foundByLastname = foundByLastname;
    }

    public ArrayList<Personne> getFoundByFirstname() {
        return foundByFirstname;
    }

    public void setFoundByFirstname(ArrayList<Personne> foundByFirstname) {
        this.foundByFirstname = foundByFirstname;
    }

    public ArrayList<Personne> getFoundByActivity() {
        return foundByActivity;
    }

    public void setFoundByActivity(ArrayList<Personne> foundByActivity) {
        this.foundByActivity = foundByActivity;
    }

    @Override
    public String toString() {
        return "SearchResults{" +
                "foundByLastname=" + foundByLastname +
                ", foundByFirstname=" + foundByFirstname +
                ", foundByActivity=" + foundByActivity +
                '}';
    }
}
