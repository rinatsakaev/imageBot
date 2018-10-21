package com.company;
import com.company.Helpers.GridFSUtil;
import com.company.Helpers.HibernateUtil;
import com.company.Models.Image;
import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageRepo {
    String clsName = "Image";
    GridFSUtil gridFSUtil;
    public ImageRepo(){
        gridFSUtil = new GridFSUtil();
    }

    public List<Image> getAll() {
        Session session = HibernateUtil.getInstance().getSession();
        List<Image> images = (List<Image>) session.createQuery("FROM "+clsName).list();
        for (Image img: images)
            img.setFile(getFileById(img.getId()));

        return images;
    }

    public long getCount() {
        Session session = HibernateUtil.getInstance().getSession();
        return (long) session.createQuery("SELECT COUNT(*) FROM "+clsName).list().get(0);
    }

    public Image getRandom() {
        Session session = HibernateUtil.getInstance().getSession();
        Image img = (Image) session.createQuery("FROM " + clsName + " ORDER BY RANDOM()").list().get(0);
        img.setFile(getFileById(img.getId()));
        return img;
    }


    public Image getById(long id) {
        Session session = HibernateUtil.getInstance().getSession();
        Image img = session.get(Image.class, id);
        img.setFile(getFileById(img.getId()));
        return img;
    }


    public void add(Image item) {
        Session session = HibernateUtil.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        String id = gridFSUtil.UploadFile(item.getFile());
        item.setId(id);
        session.persist(item);
        tx.commit();
    }


    public void remove(Image item) {
        Session session = HibernateUtil.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.remove(item);
        gridFSUtil.DeleteFile(item.getId());
        tx.commit();
    }


    public void update(Image item) {
        Session session = HibernateUtil.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.update(item);
        tx.commit();
    }

    private File getFileById(String id){
        File file = new File(id);
        try {
            FileUtils.copyInputStreamToFile(gridFSUtil.GetFileInputStream(id), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}