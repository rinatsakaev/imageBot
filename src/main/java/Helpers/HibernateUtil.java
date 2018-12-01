package Helpers;

import Models.Image;
import Models.Profile;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.*;

public final class HibernateUtil {
    private Configuration configuration;
    private static volatile HibernateUtil instance;
    private SessionFactory factory;

    private HibernateUtil() throws FileNotFoundException {
        this.configuration = new Configuration();
        File f = new File("src\\main\\resources\\hibernate.cfg.xml");
        if (!f.exists())
            throw new FileNotFoundException("File not found");


            this.configuration.configure(f);
            this.configuration.addAnnotatedClass(Profile.class);
            this.configuration.addAnnotatedClass(Image.class);
            this.factory = configuration.buildSessionFactory();

    }

    public static HibernateUtil getInstance() {
        if (instance == null) {
            synchronized (HibernateUtil.class) {
                try{
                    instance = new HibernateUtil();
                }
                catch (FileNotFoundException e){
                    //TODO WTF?!
                    System.out.println("n found");
                }
            }
        }
        return instance;
    }

    public Session getSession() {
        return factory.openSession();
    }
}
