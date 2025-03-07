package com.javarush;

import com.javarush.dao.*;
import com.javarush.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class AppMain {
    private final SessionFactory sessionFactory;

    private final ActorDAO actorDAO;
    private final AddressDAO addressDAO;
    private final CategoryDAO categoryDAO;
    private final CityDAO cityDAO;
    private final CountryDAO countryDAO;
    private final CustomerDAO customerDAO;
    private final FilmDAO filmDAO;
    private final FilmTextDAO filmTextDAO;
    private final InventoryDAO inventoryDAO;
    private final LanguageDAO languageDAO;
    private final PaymentDAO paymentDAO;
    private final RentalDAO rentalDAO;
    private final StaffDAO staffDAO;
    private final StoreDAO storeDAO;



    public AppMain() {
        Properties properties = getProperties();

        sessionFactory = new Configuration()
                .addAnnotatedClass(Actor.class)
                .addAnnotatedClass(Address.class)
                .addAnnotatedClass(Category.class)
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Film.class)
                .addAnnotatedClass(FilmText.class)
                .addAnnotatedClass(Inventory.class)
                .addAnnotatedClass(Language.class)
                .addAnnotatedClass(Payment.class)
                .addAnnotatedClass(Rental.class)
                .addAnnotatedClass(Staff.class)
                .addAnnotatedClass(Store.class)
                .addProperties(properties)
                .buildSessionFactory();

        actorDAO = new ActorDAO(sessionFactory);
        addressDAO = new AddressDAO(sessionFactory);
        categoryDAO = new CategoryDAO(sessionFactory);
        cityDAO = new CityDAO(sessionFactory);
        countryDAO = new CountryDAO(sessionFactory);
        customerDAO = new CustomerDAO(sessionFactory);
        filmDAO = new FilmDAO(sessionFactory);
        filmTextDAO = new FilmTextDAO(sessionFactory);
        inventoryDAO = new InventoryDAO(sessionFactory);
        languageDAO = new LanguageDAO(sessionFactory);
        paymentDAO = new PaymentDAO(sessionFactory);
        rentalDAO = new RentalDAO(sessionFactory);
        staffDAO = new StaffDAO(sessionFactory);
        storeDAO = new StoreDAO(sessionFactory);

    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
        properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/movie");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "mysql");
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.put(Environment.HBM2DDL_AUTO, "validate");
        return properties;
    }

    public static void main(String[] args) {
        AppMain main = new AppMain();
        Customer customer = main.createNewCustomer();
        main.customerRentInventory(customer);
        main.customerReturnInventoryToStore();
        main.newFilmWasMade();
    }

    private void newFilmWasMade() {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            try {
                Language language = languageDAO.getItems(0, 20).stream().unordered().findAny().orElse(null);
                List<Category> categories = categoryDAO.getItems(0, 5);
                List<Actor> actors = actorDAO.getItems(0,20);

                Film film = new Film();
                film.setLanguage(language);
                film.setActors(new HashSet<>(actors));
                film.setRating(Rating.NC17);
                film.setSpecialFeatures(Set.of(Features.TRAILERS, Features.COMMENTARIES));
                film.setLength((short) 5);
                film.setReplacementCost(BigDecimal.ONE);
                film.setRentalRate(BigDecimal.ZERO);
                film.setDescription("new film");
                film.setTitle("new film");
                film.setRentalDuration((byte) 5);
                film.setOriginalLanguage(language);
                film.setCategories(new HashSet<>(categories));
                film.setReleaseYear(Year.now());
                filmDAO.save(film);

                FilmText filmText = new FilmText();
                filmText.setFilm(film);
                filmText.setId(film.getId());
                filmText.setDescription("new film");
                filmText.setTitle("new film");
                filmTextDAO.save(filmText);


                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                System.out.println(e.getMessage() + " rollback");
            }
        }
    }

    private void customerRentInventory(Customer customer) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            Film film = filmDAO.getFirstAvailableFilmForRent();
            Store store = storeDAO.getItems(0, 1).getFirst();
            Inventory inventory = new Inventory();
            inventory.setFilm(film);
            inventory.setStore(store);
            inventoryDAO.save(inventory);

            Staff staff = store.getStaff();

            Rental rental = new Rental();
            rental.setCustomer(customer);
            rental.setRentalDate(LocalDateTime.now());
            rental.setInventory(inventory);
            rental.setStaff(staff);
            rentalDAO.save(rental);

            Payment payment = new Payment();
            payment.setCustomer(customer);
            payment.setRental(rental);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setAmount(BigDecimal.valueOf(55.77));
            payment.setStaff(staff);
            paymentDAO.save(payment);

            try {
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                System.out.println(e.getMessage() + "rollback");
            }
        }
    }

    private void customerReturnInventoryToStore() {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try (session) {

            Rental rental = rentalDAO.getAnyUnreturnedRental();
            rental.setReturnDate(LocalDateTime.now());
            rentalDAO.save(rental);

            try {
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                System.out.println(e.getMessage() + "rollback");
            }
        }
    }

    private Customer createNewCustomer() {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            Store store = storeDAO.getItems(0, 1).getFirst();
            City city = cityDAO.getByName("Kragujevac");
            Address address = new Address();
            address.setAddress("White str, 4");
            address.setPhone("999-444-333");
            address.setCity(city);
            address.setDistrict("Wawa");
            addressDAO.save(address);
            Customer customer = new Customer();
            customer.setActive(true);
            customer.setEmail("re@gmail.com");
            customer.setAddress(address);
            customer.setStore(store);
            customer.setFirstName("Igor");
            customer.setLastName("Rei");
            customerDAO.save(customer);
            try {
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                System.out.println(e.getMessage() + "rollback");
            }
            return customer;

        }
    }
}