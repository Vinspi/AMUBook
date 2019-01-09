package dao;

import java.util.List;

public interface GenericDAO<T> {

    T update(T daoObject);
    T findById(long id);
    void add(T daoObject);
    void remove(long id);
    List<T> findAll();

}
