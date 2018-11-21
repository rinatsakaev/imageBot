package Repos;

import Helpers.HibernateUtil;
import Models.Profile;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProfileRepo implements IRepository<Profile> {
    private String clsName = "Profile";

    public List<Profile> getAll() {

        try (Session session = HibernateUtil.getInstance().getSession()) {
            return (List<Profile>) session.createQuery("FROM " + clsName).list();
        }
    }

    public long getCount() {
        try (Session session = HibernateUtil.getInstance().getSession()) {
            return (long) session.createQuery("SELECT COUNT(*) FROM " + clsName).list().get(0);
        }
    }

    public Profile getRandom() {
        try (Session session = HibernateUtil.getInstance().getSession()) {
            return (Profile) session.createQuery("FROM " + clsName + " ORDER BY RANDOM()").list().get(0);
        }
    }


    public Profile getById(long id) {
        try (Session session = HibernateUtil.getInstance().getSession()) {
            return session.get(Profile.class, id);
        }
    }


    public void add(Profile item) {
        try (Session session = HibernateUtil.getInstance().getSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(item);
            tx.commit();
        }
    }


    public void remove(Profile item) {
        try (Session session = HibernateUtil.getInstance().getSession()) {
            Transaction tx = session.beginTransaction();
            session.remove(item);
            tx.commit();
        }
    }


    public void update(Profile item) {
        try (Session session = HibernateUtil.getInstance().getSession()) {
            Transaction tx = session.beginTransaction();
            session.update(item);
            tx.commit();
        }
    }
}