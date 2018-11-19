package Helpers;

import Models.Image;
import Models.Profile;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateUtil {
    private Configuration configuration;
    private static volatile HibernateUtil instance;
    private SessionFactory factory;

    private HibernateUtil() {
        this.configuration = new Configuration();
        this.configuration.configure();
        this.configuration.addAnnotatedClass(Profile.class);
        this.configuration.addAnnotatedClass(Image.class);
        this.factory = configuration.buildSessionFactory();
    }

    public static HibernateUtil getInstance() {
        if (instance == null) {
            synchronized (HibernateUtil.class) {
                instance = new HibernateUtil();
            }
        }
        return instance;
    }

    public Session getSession() {
        return factory.openSession();
    }
}
