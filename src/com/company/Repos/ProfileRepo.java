package com.company.Repos;

import com.company.Helpers.HibernateUtil;
import com.company.IRepository;
import com.company.Models.Profile;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProfileRepo implements IRepository<Profile> {
    //TODO Куда-то потерялись модификаторы доступа
    String clsName = "Profile";

    //TODO А точно пустой конструктор нужен?
    public ProfileRepo() {

    }

    public List<Profile> getAll() {
        //TODO Сессию, наверное, тоже нужно закрывать?
        Session session = HibernateUtil.getInstance().getSession();
        return (List<Profile>) session.createQuery("FROM " + clsName).list();
    }

    public long getCount() {
        //TODO Сессию, наверное, тоже нужно закрывать?
        Session session = HibernateUtil.getInstance().getSession();
        return (long) session.createQuery("SELECT COUNT(*) FROM " + clsName).list().get(0);
    }

    public Profile getRandom() {
        //TODO Сессию, наверное, тоже нужно закрывать?
        Session session = HibernateUtil.getInstance().getSession();
        return (Profile) session.createQuery("FROM " + clsName + " ORDER BY RANDOM()").list().get(0);
    }


    public Profile getById(long id) {
        //TODO Сессию, наверное, тоже нужно закрывать?
        Session session = HibernateUtil.getInstance().getSession();
        return session.get(Profile.class, id);
    }


    public void add(Profile item) {
        //TODO Сессию, наверное, тоже нужно закрывать?
        Session session = HibernateUtil.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.persist(item);
        tx.commit();
    }


    public void remove(Profile item) {
        //TODO Сессию, наверное, тоже нужно закрывать?
        Session session = HibernateUtil.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.remove(item);
        tx.commit();
    }


    public void update(Profile item) {
        //TODO Сессию, наверное, тоже нужно закрывать?
        Session session = HibernateUtil.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.update(item);
        tx.commit();
    }
}
