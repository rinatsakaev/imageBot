package Repos;

import java.util.List;

public interface IRepository<T> {
    List<T> getAll();

    T getById(long id);

    void add(T item);

    void remove(T item);

    void update(T item);
}