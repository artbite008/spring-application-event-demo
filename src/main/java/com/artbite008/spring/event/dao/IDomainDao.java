package com.artbite008.spring.event.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface IDomainDao<T, ID extends Serializable> {

	/**
	 * Find by primary key
	 * @param id primary key
	 * @return return null if object not found
	 */
	T get(ID id);
	
	/**
	 * refresh object
	 * @param obj
	 */
	void refresh(T obj);
	
	/**
	 * trigger entity manager flush
	 */
	void flush();

	/**
	 * add exclusive lock on target object
	 * @param id primary key
	 * @return return null if object not found
	 */
	@Transactional()
	T lock(ID id);

	/**
	 * find by a list of primary key
	 * @param ids list of primary key
	 * @return return null if object not found
	 */
	List<T> get(List<ID> ids);

	/**
	 * need to enhance to support operator like, and, or, like, lessThan
	 * TODO
	 * 
	 * @param queryOptions
	 * @return
	 */
	List<T> findByProperties(Map<String, Object> queryOptions);
	
	/**
	 * @param pageRequest
	 * @param queryOptions
	 * @return
	 */
	Page<T> findByProperties(Pageable pageRequest, Map<String, Object> queryOptions);

	@Deprecated
	List<T> findByExample(T t);
	
	/**
	 * @param queryName
	 * @param criterias
	 * @return
	 */
	List<T> findByNamedQuery(String queryName, Object... criterias);

	/**
	 * @param queryStr
	 * @param criterias
	 * @return
	 */
	List<T> findByQuery(String queryStr, Object... criterias);
	
	/**
	 * not able to provide count number so far
	 * 
	 * @param pageRequest
	 * @param queryStr native SQL to query
	 * @param criterias
	 * @return
	 */
	Page<T> findByQuery(Pageable pageRequest, String queryStr, Object... criterias);

	/**
	 * @param obj
	 */
	@Transactional()
	T save(T obj);
	
	@Transactional()
	T saveAndFlush(T obj);
	
	@Transactional()
	T merge(T obj);

	/**
	 * @param obj
	 * @return
	 */
	@Transactional()
	T insert(T obj);

	/**
	 * @param id
	 */
	@Transactional()
	void remove(ID id);

	/**
	 * partial update, exception will be thrown if object not found,
	 * optimistic lock can be supported if updateMap contains the key value pair which annotated as Version
	 * 
	 * @param id primary key
	 * @param updateMap map contains values to update
	 */
	@Transactional()
	void update(ID id, Map<String, Object> updateMap);

	/**
	 * @param queryOptions
	 * @return
	 */
	long count(Map<String, Object> queryOptions);
	
	/**
	 * count a query
	 * @param queryStr has to be a count query, like "select count(t) from Table t"
	 * @param criterias
	 * @return
	 */
	long count(String queryStr, Object... criterias);

	/**
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List nativeQuery(String sql);

}
