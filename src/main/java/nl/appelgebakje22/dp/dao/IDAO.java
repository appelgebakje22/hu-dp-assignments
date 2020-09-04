package nl.appelgebakje22.dp.dao;

import java.util.List;

public interface IDAO<T> {

	boolean save(T entity);

	boolean update(T entity);

	boolean delete(T entity);

	T findById(int id);

	List<T> findAll();
}