package magazineDAO;

import java.util.List;

public interface MagazineDAO<T> {
    T find(long id);

    boolean create(T p);

    boolean delete(T p);

    boolean update(T p1, T p2);

    List<T> findAll();

    List<T> findAll(String key);

}
