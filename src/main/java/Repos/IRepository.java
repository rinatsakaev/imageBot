package Repos;

import java.io.IOException;
import java.util.List;

public interface IRepository<T> {
    List<T> getAll() throws IOException;

    T getById(long id);

    void add(T item) throws IOException;

    void remove(T item);

    void update(T item);
}