package com.company.Repos;

import com.company.Helpers.GridFSUtil;
import com.company.Helpers.HibernateUtil;
import com.company.IRepository;
import com.company.Models.Image;
import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageRepo implements IRepository<Image> {
    //TODO Куда-то потерялись модификаторы доступа
    String clsName = "Image";
    //TODO Куда-то потерялись модификаторы доступа
    GridFSUtil gridFSUtil;

    public ImageRepo() {
        gridFSUtil = new GridFSUtil();
    }

    public List<Image> getAll() {
        //TODO Сессию, наверное, тоже нужно закрывать?
        Session session = HibernateUtil.getInstance().getSession();
        List<Image> images = (List<Image>) session.createQuery("FROM " + clsName).list();
        for (Image img : images)
            //TODO А кто закрывает InputStream, который получился из getFileInputStream?
            img.setInputStream(gridFSUtil.getFileInputStream(img.getId()));

        return images;
    }

    public long getCount() {
        //TODO Сессию, наверное, тоже нужно закрывать?
        Session session = HibernateUtil.getInstance().getSession();
        return (long) session.createQuery("SELECT COUNT(*) FROM " + clsName).list().get(0);
    }

    public Image getRandom() {
        //TODO Сессию, наверное, тоже нужно закрывать?
        Session session = HibernateUtil.getInstance().getSession();
        Image img = (Image) session.createQuery("FROM " + clsName + " ORDER BY RANDOM()").list().get(0);
        //TODO А кто закрывает InputStream, который получился из getFileInputStream?
        img.setInputStream(gridFSUtil.getFileInputStream(img.getId()));
        return img;
    }


    public Image getById(long id) {
        //TODO Сессию, наверное, тоже нужно закрывать?
        Session session = HibernateUtil.getInstance().getSession();
        Image img = session.get(Image.class, id);
        //TODO А кто закрывает InputStream, который получился из getFileInputStream?
        img.setInputStream(gridFSUtil.getFileInputStream(img.getId()));
        return img;
    }


    public void add(Image item) {
        //TODO Сессию, наверное, тоже нужно закрывать?
        Session session = HibernateUtil.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        //TODO А кто закрывает InputStream, который получился из item.getInputStream()
        String id = gridFSUtil.uploadFile(item.getInputStream());
        item.setId(id);
        session.persist(item);
        tx.commit();
    }


    public void remove(Image item) {
        //TODO Сессию, наверное, тоже нужно закрывать?
        Session session = HibernateUtil.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.remove(item);
        gridFSUtil.deleteFile(item.getId());
        tx.commit();
    }


    public void update(Image item) {
        //TODO Сессию, наверное, тоже нужно закрывать?
        Session session = HibernateUtil.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.update(item);
        tx.commit();
    }
}