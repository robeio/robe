package io.robe.admin.hibernate.dao;

import io.robe.admin.RobeAdminTest;
import io.robe.hibernate.RobeHibernateBundle;
import io.robe.hibernate.dao.BaseDao;
import io.robe.hibernate.entity.BaseEntity;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by hasanmumin on 07/10/2016.
 */
public abstract class BaseDaoTest<T extends BaseEntity, D extends BaseDao<T>> extends RobeAdminTest {

    protected SessionFactory sessionFactory;

    protected D dao;

    protected Class<D> daoClazz;

    public abstract T instance();

    public abstract T update(T model);

    @Before
    public void before() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (sessionFactory == null) {
            sessionFactory = RobeHibernateBundle.getInstance().getSessionFactory();
            ManagedSessionContext.bind(sessionFactory.openSession());
            this.daoClazz = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
            dao = daoClazz.getDeclaredConstructor(SessionFactory.class).newInstance(sessionFactory);
        }
    }

    @Test
    public void create() {
        T instance = this.createFrom();
        T entity = this.findByIdFrom(instance.getOid());
        assertEquals(instance, entity);
        this.deleteFrom(entity);
    }

    @Test
    public void findById() {
        T instance = this.createFrom();
        T entity = this.findByIdFrom(instance.getOid());
        assertEquals(instance, entity);
        this.deleteFrom(entity);
    }

    @Test
    public void findAll() {
        T instance = this.createFrom();
        List<T> entities = this.findByAll();
        assertTrue(entities.size() > 0); //TODO change
        this.deleteFrom(instance);
    }

    @Test
    public void update() {
        T instance = this.createFrom();
        assertEquals(instance, instance);
        T update = this.update(instance);
        update = this.updateFrom(update);
        T entity = this.findByIdFrom(update.getOid());
        assertEquals(update, entity);
        this.deleteFrom(update);
    }

    @Test
    public void delete() {
        T instance = this.createFrom();
        T entity = this.findByIdFrom(instance.getOid());
        assertEquals(instance, entity);
        this.deleteFrom(entity);
        entity = this.findByIdFrom(entity.getOid());
        Assert.assertTrue("Entity deleted from database", entity == null);
    }

    protected T deleteFrom(T instance) {
        T response = dao.delete(instance);
        dao.flush();
        return response;
    }

    protected T createFrom() {
        return createFrom(instance());
    }

    protected T createFrom(T instance) {
        T response = dao.create(instance);
        dao.flush();
        return response;
    }

    protected T updateFrom(T instance) {
        T response = dao.update(instance);
        dao.flush();
        return response;
    }

    protected T findByIdFrom(String oid) {
        return dao.findById(oid);
    }

    protected List<T> findByAll() {
        return dao.findAllStrict();
    }
}
