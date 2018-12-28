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

    private HashMap<Personne, Activite> foundAll;
    private HashMap<Personne, Activite> foundByLastname;
    private HashMap<Personne, Activite> foundByFirstname;
    private HashMap<Personne, Activite> foundByActivity;

    // No getters & setters, useful only for bean purposes
    private HashSet<Long> idkeeper;

    // ******************************************** //

    public void cleanFoundByAll(){ this.foundAll = new HashMap<>(); this.idkeeper = new HashSet<>();}

    public HashMap<Personne, Activite> getFoundByLastname() {
        return foundByLastname;
    }

    public void setFoundByLastname(HashMap<Personne, Activite> foundByLastname) {
        this.foundByLastname = foundByLastname;
        updateFoundByAllUnique(sr_updatedSource.fbyLastname);
    }

    public HashMap<Personne, Activite> getFoundByFirstname() {
        return foundByFirstname;
    }

    public void setFoundByFirstname(HashMap<Personne, Activite>  foundByFirstname) {
        this.foundByFirstname = foundByFirstname;
        updateFoundByAllUnique(sr_updatedSource.fbyFirstname);
    }

    public HashMap<Personne, Activite> getFoundByActivity() {
        return foundByActivity;
    }

    public void setFoundByActivity(HashMap<Personne, Activite> foundByActivity) {
        this.foundByActivity = foundByActivity;
        updateFoundByAllUnique(sr_updatedSource.fbyActivity);
    }

    public HashMap<Personne, Activite> getFoundAll() {
        return foundAll;
    }

    public void setFoundAll(HashMap<Personne, Activite> foundAll) {
        this.foundAll = foundAll;
    }

    private void updateFoundByAllUnique(sr_updatedSource source){
        if(this.foundAll == null) this.foundAll = new HashMap<>();
        if(this.idkeeper == null) this.idkeeper = new HashSet<>();

        switch (source){
            case fbyLastname:
                this.foundByLastname.forEach((Personne p, Activite a) -> {
                    if(!this.idkeeper.contains(p.getId())){
                        this.foundAll.put(p, a);
                        this.idkeeper.add(p.getId());
                    }
                });
                break;

            case fbyFirstname:
                this.foundByFirstname.forEach((Personne p, Activite a) -> {
                    if(!this.idkeeper.contains(p.getId())){
                        this.foundAll.put(p, a);
                        this.idkeeper.add(p.getId());
                    }
                });
                break;

            case fbyActivity:
                this.foundByActivity.forEach((Personne p, Activite a) -> {
                    if(!this.idkeeper.contains(p.getId())){
                        this.foundAll.put(p, a);
                        this.idkeeper.add(p.getId());
                    }
                });
                break;
        }

        if(this.foundByActivity == null || this.foundByFirstname == null || this.foundByLastname == null)
            return;

//        this.foundAll.sort((Personne o1, Personne o2) -> {
//                int scoreA = 0, scoreB = 0;
//                if(foundByActivity.contains(o1)) scoreA++;
//                if(foundByFirstname.contains(o1)) scoreA++;
//                //if(foundByLastname.contains(o1)) scoreA++;
//                if(foundByActivity.contains(o2)) scoreB++;
//                if(foundByFirstname.contains(o2)) scoreB++;
//                //if(foundByLastname.contains(o2)) scoreB++;
//                return scoreB - scoreA;
//            });
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
