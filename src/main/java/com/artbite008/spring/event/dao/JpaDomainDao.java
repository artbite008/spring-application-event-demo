package com.artbite008.spring.event.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import org.springframework.data.domain.Sort;
import javax.persistence.criteria.Order;


public class JpaDomainDao<T, ID extends Serializable> implements IDomainDao<T, ID> {
	
	private Class<T> entityClass;
	
	@PersistenceContext(type=PersistenceContextType.TRANSACTION)
	private EntityManager em;
	


	public EntityManager getEm() {
		return em;
	}
	
	public JpaDomainDao(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	@SuppressWarnings("unchecked")
	public JpaDomainDao() {
		this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public T get(ID id) {
		T t = em.find(entityClass, id);
		return t;
	}

	public void refresh(T obj) {
		em.refresh(obj);
	}

	public T lock(ID id) {
		T t = em.find(entityClass, id, LockModeType.PESSIMISTIC_WRITE);
		return t;
	}

	public List<T> get(List<ID> ids) {
		if(null == ids || ids.isEmpty()) {
			return new ArrayList<T>();
		}
		else {
			String table = entityClass.getSimpleName();
			TypedQuery<T> q = em.createQuery("select t from " + table + " t where t.id in ?1", entityClass);
			q.setParameter(1, ids);
			return q.getResultList();
		}
		
	}

	public List<T> findByProperties(Map<String, Object> queryOptions) {
		return findByProperties(null, queryOptions).getContent();
	}

	public Page<T> findByProperties(Pageable pageRequest, Map<String, Object> queryOptions) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(entityClass);
		Root<T> t = cq.from(entityClass);
		//EntityType<T> t_ = t.getModel();
		Predicate condition = null;
		
		if(queryOptions != null) {
			for(String key : queryOptions.keySet()) {
				Object value = queryOptions.get(key);
				Predicate cri = null;
				
				//special search operator
				//TODO, need to change to support more operators
				if(value instanceof String && ((String)value).startsWith("?")) {
					String strValue = (String)value;
					String searchKey = strValue.substring(1, strValue.length());
					Expression<String> ex = t.get(key);
					cri = cb.like(ex, searchKey);
				} else {
					cri = cb.equal(t.get(key), value);
				}

				if(condition == null) {
					condition = cri;
				} else {
					condition = cb.and(condition, cri);
				}
			}
			
			cq.where(condition);
		}
		
		TypedQuery<T> q = em.createQuery(cq);
		
		// paging&sorting support
		long total = 0;
		if (pageRequest == null) {
			q = em.createQuery(cq);
		} else {
			Sort s = pageRequest.getSort();
			if (s != null) {
				Iterator<Sort.Order> it = s.iterator();
				List<Order> orderList = new ArrayList();
				while (it.hasNext()) {
					Sort.Order so = it.next();
					Path<Object> p = t.get(so.getProperty());
					switch (so.getDirection()) {
					case ASC:
						orderList.add(cb.asc(p));
						break;
					case DESC:
						orderList.add(cb.desc(p));
						break;
					default:
						continue;
					}
				}
				if (!orderList.isEmpty()) {
					cq.orderBy(orderList);
				}
			}
			q = em.createQuery(cq);

			q.setFirstResult(pageRequest.getOffset());
			q.setMaxResults(pageRequest.getPageSize());
			total = this.count(queryOptions);
		}
		
		List<T> content = q.getResultList();
		return new PageImpl<T>(content, pageRequest, total);
	}

	public List<T> findByExample(T t) {
		return null;
	}


	public List<T> findByQuery(String queryStr, Object ... criterias) {
		TypedQuery<T> q = em.createQuery(queryStr, entityClass);
		if(criterias != null) {
			for(int i = 1; i <= criterias.length; i++) {
				q.setParameter(i, criterias[i-1]);
			}
		}
		return q.getResultList();
	}

	public Page<T> findByQuery(Pageable pageRequest, String queryStr, Object ... criterias) {		
		TypedQuery<T> q = em.createQuery(queryStr, entityClass);
		if(criterias != null) {
			for(int i = 1; i <= criterias.length; i++) {
				q.setParameter(i, criterias[i-1]);
			}
		}
		
		//TODO, how to get total count? may need another named query, just use page size so far
		//long total = this.count(querySql, null);
		long total = pageRequest.getPageSize();
		
		//paging support
		if(pageRequest != null) {
			q.setFirstResult(pageRequest.getOffset());
			q.setMaxResults(pageRequest.getPageSize());
		}
		
		List<T> content = q.getResultList();
		
		return new PageImpl<T>(content, pageRequest, total);
	}

	public List<T> findByNamedQuery(String queryName, Object... criterias) {
		TypedQuery<T> q = em.createNamedQuery(queryName, entityClass);
		if(criterias != null) {
			for(int i = 1; i <= criterias.length; i++) {
				q.setParameter(i, criterias[i-1]);
			}
		}
		return q.getResultList();
	}

	public T save(T obj) {
		 return em.merge(obj);
	}

	public T saveAndFlush(T obj) {
		 em.persist(obj);
		 em.flush();
		 return obj;
	}

	public T merge(T obj) {
		 return em.merge(obj);		
	}

	public T insert(T obj) {
		try {
			em.persist(obj);
		} catch (EntityExistsException e) {
			throw e;
		}
		//in case id strategy relies on database table or sequence, so flush here to get id for return
		em.flush();
		return obj;
	}

	public void remove(ID id) {
		T target = get(id);
		if(target != null)
			em.remove(target);
		em.flush();
	}

	public void update(ID id, Map<String, Object> updateMap) {
		T target = em.find(entityClass, id);
		
		//merge attributes
		
		//update
		em.merge(target);
	}
	
	/**
	 * The element in List could be an Object[] or an Object, depends on the SQL
	 * 
	 * @param sql, the native SQL to run in database
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List nativeQuery(String sql) {
		Query q = em.createNativeQuery(sql);
		return q.getResultList();
	}

	public long count(Map<String, Object> queryOptions) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<T> t = countQuery.from(entityClass);
		Predicate condition = null;
		countQuery.select(cb.count(t));
		if (queryOptions != null) {
			for (String key : queryOptions.keySet()) {
				Object value = queryOptions.get(key);
				Predicate cri = null;

				// some special search operator
				//TODO, need to change to support more operators
				if (value instanceof String && ((String) value).startsWith("?")) {
					String strValue = (String) value;
					String searchKey = strValue.substring(1, strValue.length());
					Expression<String> ex = t.get(key);
					cri = cb.like(ex, searchKey);
				} else {
					cri = cb.equal(t.get(key), value);
				}

				if (condition == null) {
					condition = cri;
				} else {
					condition = cb.and(condition, cri);
				}
			}

			countQuery.where(condition);
		}
		return em.createQuery(countQuery).getSingleResult();
	}

	public long count(String queryStr, Object... criterias) {
		Query q = em.createQuery(queryStr);
		if(criterias != null) {
			for(int i = 1; i <= criterias.length; i++) {
				q.setParameter(i, criterias[i-1]);
			}
		}
		
		Object obj = q.getSingleResult();
		long result_count = 0;
		try {
			result_count = (Long)obj;
		} catch(Exception ex) {
			//throw new InvalidParameterException("result is not a number, please check query string", ex);
		}
		return result_count;
	}

	public void flush() {
		em.flush();
	}


}
