package com.javarush.dao;

import com.javarush.entity.Customer;
import org.hibernate.SessionFactory;

public class CustomerDAO extends GenericDao<Customer> {
    public CustomerDAO(SessionFactory sessionFactory) {
        super(Customer.class, sessionFactory);
    }
}
