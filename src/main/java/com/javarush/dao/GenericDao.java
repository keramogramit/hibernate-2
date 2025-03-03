package com.javarush.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public abstract class GenericDao<T> {
    private final Class<T> clazz;
    private final SessionFactory sessionFactory;

    public GenericDao(Class<T> clazz, SessionFactory sessionFactory) {
        this.clazz = clazz;
        this.sessionFactory = sessionFactory;
    }

    public T getById(final int id){
        return (T) sessionFactory.getCurrentSession().get(clazz, id);
    }

    public List<T> getItems(int offset, int count){
        Query<T> query = sessionFactory.getCurrentSession().createQuery("from "+clazz.getSimpleName(), clazz);
        query.setFirstResult(offset);
        query.setMaxResults(count);
        return query.getResultList();
    }

    public List<T> findAll(){
        return sessionFactory.getCurrentSession().createCriteria(clazz).list();
    }

    public T save(final T entity){
        sessionFactory.getCurrentSession().save(entity);
        return entity;
    }

    public T update(final T entity){
        return (T) sessionFactory.getCurrentSession().merge(entity);
    }

    public void delete(final T entity){
        sessionFactory.getCurrentSession().delete(entity);
    }

    public void deleteById(final int id){
        sessionFactory.getCurrentSession().delete(getById(id));
    }

//    protected Session getCurrentSession() {
//        return sessionFactory.getCurrentSession();
//    }
}
