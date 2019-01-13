package models;

import javax.persistence.*;

import java.io.Serializable;
import java.util.*;



@Entity()
public class CV implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String titre;
    private String description;

    @OneToOne(mappedBy = "cv")
    private Personne personne;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "cv", orphanRemoval = true)
    private List<Activite> activites = new ArrayList<>();

    public CV() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public List<Activite> getActivites() {

        Collections.sort(activites, (Activite activite1, Activite activite2) -> {
            return activite2.getAnnee()-activite1.getAnnee();
        });

        return activites;
    }

    public void setActivites(List<Activite> activites) {
        this.activites = activites;
    }

}
