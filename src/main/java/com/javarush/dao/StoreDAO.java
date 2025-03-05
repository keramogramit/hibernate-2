package com.javarush.dao;

import com.javarush.entity.Store;
import org.hibernate.SessionFactory;

public class StoreDAO extends GenericDao<Store> {
    public StoreDAO(SessionFactory sessionFactory) {
        super(Store.class, sessionFactory);
    }
}
