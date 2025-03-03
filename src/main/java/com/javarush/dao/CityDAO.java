package com.javarush.dao;

import com.javarush.domain.City;
import org.hibernate.SessionFactory;

public class CityDAO extends GenericDao<City> {
    public CityDAO(SessionFactory sessionFactory) {
        super(City.class, sessionFactory);
    }
}
