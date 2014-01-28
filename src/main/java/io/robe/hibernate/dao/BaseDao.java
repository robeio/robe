package io.robe.hibernate.dao;

import com.google.inject.Inject;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import io.robe.hibernate.HibernateBundle;
import io.robe.hibernate.entity.BaseEntity;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class BaseDao<T extends BaseEntity> extends AbstractDAO<T> {

	@Inject
	public BaseDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public List<T> findAll(Class<T> t) {
		Criteria criteria = currentSession().createCriteria(t);
		return list(criteria);
	}

	public T findById(String oid) {
		return get(oid);
	}

	public T create(T entity) {
		return persist(entity);
	}

	public T update(T entity) {

//		T entity2 = findById(entity.getOid());
//		BeanUtils.copyProperties(entity2, entity);
//		entity2 = persist(entity2);
//		flush();
		entity = persist(entity);
		return entity;

	}

	public T delete(T entity) {
		currentSession().delete(entity);
		return entity;
	}

	public void flush() {
		currentSession().flush();
	}

	public T attach(T entity) {
		return (T) currentSession().merge(entity);
	}

	public T detach(T entity) {
		currentSession().evict(entity);
		return entity;
	}

}
