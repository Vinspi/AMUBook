package models;

import javax.persistence.*;

@Entity
public class Activite {

    public enum Nature {
        EXPERIENCE_PRO, FORMATION, PROJETS, AUTRE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int annee;
    private String nature;
    private String titre;

    @Lob
    @Column(length = 1000)
    private String descritption;
    private String website;

    @ManyToOne
    private CV cv;

    public Activite() {}

    public CV getCv() {
        return cv;
    }

    public void setCv(CV cv) {
        this.cv = cv;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescritption() {
        return descritption;
    }

    public void setDescritption(String descritption) {
        this.descritption = descritption;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return "Activite{" +
                "id=" + id +
                ", annee=" + annee +
                ", nature='" + nature + '\'' +
                ", titre='" + titre + '\'' +
                ", descritption='" + descritption + '\'' +
                ", website='" + website + '\'' +
                "}\n\n";
    }


}

