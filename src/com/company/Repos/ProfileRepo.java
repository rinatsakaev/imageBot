package com.company.Repos;

import com.company.Helpers.HibernateUtil;
import com.company.IRepository;
import com.company.Models.Profile;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProfileRepo implements IRepository<Profile> {
    String clsName = "Profile";

    public ProfileRepo(){

    }

    public List<Profile> getAll() {
        Session session = HibernateUtil.getInstance().getSession();
        return (List<Profile>) session.createQuery("FROM "+clsName).list();
    }

    public long getCount() {
        Session session = HibernateUtil.getInstance().getSession();
        return (long) session.createQuery("SELECT COUNT(*) FROM "+clsName).list().get(0);
    }

    public Profile getRandom() {
        Session session = HibernateUtil.getInstance().getSession();
        return (Profile) session.createQuery("FROM " + clsName + " ORDER BY RANDOM()").list().get(0);
    }


    public Profile getById(long id) {
        Session session = HibernateUtil.getInstance().getSession();
        return session.get(Profile.class, id);
    }


    public void add(Profile item) {
        Session session = HibernateUtil.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.persist(item);
        tx.commit();
    }


    public void remove(Profile item) {
        Session session = HibernateUtil.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.remove(item);
        tx.commit();
    }


    public void update(Profile item) {
        Session session = HibernateUtil.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.update(item);
        tx.commit();
    }
}
