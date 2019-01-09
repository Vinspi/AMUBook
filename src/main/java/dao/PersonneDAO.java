package dao;

import models.Personne;

import java.util.List;

public interface PersonneDAO extends GenericDAO<Personne>{

    Personne findByEmail(String email);
    List<Personne> findByName(String name);
    List<Personne> findByNameStrict(String name);
    List<Personne> findBySurnameStrict(String name);
    List<Personne> findBySurname(String name);


}
