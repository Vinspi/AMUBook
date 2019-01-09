package dao;

import models.Activite;

import java.util.List;


public interface ActiviteDAO extends GenericDAO<Activite>{

    List<Activite> findByTitleStrict(String title);
    List<Activite> findByTitle(String title);


}
