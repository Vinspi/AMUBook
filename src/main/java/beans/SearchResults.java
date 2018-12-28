package beans;

import models.Activite;
import models.Personne;

import javax.ejb.Stateful;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Stateful
@ManagedBean(name = "searchResults")
@SessionScoped
public class SearchResults implements Serializable {

    public enum sr_updatedSource {
        fbyLastname, fbyFirstname, fbyActivity
    }

    public SearchResults(){}

    private ArrayList<Personne> foundAll;
    private ArrayList<Personne> foundByLastname;
    private ArrayList<Personne> foundByFirstname;
    private ArrayList<Personne> foundByActivity;

    // No getters & setters, useful only for bean purposes
    private HashSet<Long> idkeeper;

    // ******************************************** //

    public void cleanFoundByAll(){ this.foundAll = new ArrayList<>();}

    public ArrayList<Personne> getFoundByLastname() {
        return foundByLastname;
    }

    public void setFoundByLastname(ArrayList<Personne> foundByLastname) {
        this.foundByLastname = foundByLastname;
        updateFoundByAllUnique(sr_updatedSource.fbyLastname);
    }

    public ArrayList<Personne> getFoundByFirstname() {
        return foundByFirstname;
    }

    public void setFoundByFirstname(ArrayList<Personne> foundByFirstname) {
        this.foundByFirstname = foundByFirstname;
        updateFoundByAllUnique(sr_updatedSource.fbyFirstname);
    }

    public ArrayList<Personne> getFoundByActivity() {
        return foundByActivity;
    }

    public void setFoundByActivity(ArrayList<Personne> foundByActivity) {
        this.foundByActivity = foundByActivity;
        updateFoundByAllUnique(sr_updatedSource.fbyActivity);
    }

    public ArrayList<Personne> getFoundAll() {
        return foundAll;
    }

    public void setFoundAll(ArrayList<Personne> foundAll) {
        this.foundAll = foundAll;
    }

    private void updateFoundByAllUnique(sr_updatedSource source){
        if(this.foundAll == null) this.foundAll = new ArrayList<>();
        if(this.idkeeper == null) this.idkeeper = new HashSet<>();

        switch (source){
            case fbyLastname:
                for(Personne p : this.foundByLastname)
                    if(!this.idkeeper.contains(p.getId())) {
                        this.foundAll.add(p);
                        this.idkeeper.add(p.getId());
                    }
                break;

            case fbyFirstname:
                for(Personne p : this.foundByFirstname)
                    if(!this.idkeeper.contains(p.getId())) {
                        this.foundAll.add(p);
                        this.idkeeper.add(p.getId());
                    }
                break;

            case fbyActivity:
                for(Personne p : this.foundByActivity)
                    if(!this.idkeeper.contains(p.getId())) {
                        this.foundAll.add(p);
                        this.idkeeper.add(p.getId());
                    }
                break;
        }

        if(this.foundByActivity == null || this.foundByFirstname == null || this.foundByLastname == null)
            return;

        this.foundAll.sort((Personne o1, Personne o2) -> {
                int scoreA = 0, scoreB = 0;
                if(foundByActivity.contains(o1)) scoreA++;
                if(foundByFirstname.contains(o1)) scoreA++;
                if(foundByLastname.contains(o1)) scoreA++;
                if(foundByActivity.contains(o2)) scoreB++;
                if(foundByFirstname.contains(o2)) scoreB++;
                if(foundByLastname.contains(o2)) scoreB++;
                return scoreB - scoreA;
            });
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
