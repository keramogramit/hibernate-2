package com.javarush.dao;

import com.javarush.entity.Payment;
import org.hibernate.SessionFactory;

public class PaymentDAO extends GenericDao<Payment> {
    public PaymentDAO(SessionFactory sessionFactory) {
        super(Payment.class, sessionFactory);
    }
}
