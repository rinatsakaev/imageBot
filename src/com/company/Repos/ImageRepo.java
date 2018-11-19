package Repos;
import Helpers.GridFSUtil;
import Helpers.HibernateUtil;
import Models.Image;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageRepo implements IRepository<Image> {
    private String clsName = "Image";
    private GridFSUtil gridFSUtil;

    public ImageRepo() {
        gridFSUtil = new GridFSUtil();
    }

    public List<Image> getAll() {
        try (Session session = HibernateUtil.getInstance().getSession()) {
            List<Image> images = (List<Image>) session.createQuery("FROM " + clsName).list();
            for (Image img : images)
                try(InputStream inputStream = gridFSUtil.getFileInputStream(img.getId())){
                    img.setInputStream(inputStream);
                } catch (IOException e) {
                    //TODO Наверное, проглатывать ошибки в данном случае не самая хорошая стратегия
                    Logger.getAnonymousLogger().log(Level.ALL, "Cant open img inputstream", e);
                }
            return images;
        }
    }

    public long getCount() {
        try(Session session = HibernateUtil.getInstance().getSession()){
            return (long) session.createQuery("SELECT COUNT(*) FROM " + clsName).list().get(0);
        }
    }

    public Image getRandom() {
        try(Session session = HibernateUtil.getInstance().getSession()){
            Image img = (Image) session.createQuery("FROM " + clsName + " ORDER BY RANDOM()").list().get(0);
            try(InputStream inputStream = gridFSUtil.getFileInputStream(img.getId())){
                img.setInputStream(inputStream);
            } catch (IOException e) {
                //TODO Наверное, проглатывать ошибки в данном случае не самая хорошая стратегия
                Logger.getAnonymousLogger().log(Level.ALL, "Cant open img inputstream", e);
            }
            return img;
        }
    }


    public Image getById(long id) {

        try(Session session = HibernateUtil.getInstance().getSession()) {
            Image img = session.get(Image.class, id);
            img.setInputStream(gridFSUtil.getFileInputStream(img.getId()));
            return img;
        }

    }


    public void add(Image item) {
        try(Session session = HibernateUtil.getInstance().getSession()) {
            Transaction tx = session.beginTransaction();
            try(InputStream inputStream = item.getInputStream()){
                String id = gridFSUtil.uploadFile(inputStream);
                item.setId(id);
            } catch (IOException e) {
                //TODO Наверное, проглатывать ошибки в данном случае не самая хорошая стратегия
                Logger.getAnonymousLogger().log(Level.ALL, "Cant open img inputstream", e);
            }
            session.persist(item);
            tx.commit();
        }
    }


    public void remove(Image item) {
        try(Session session = HibernateUtil.getInstance().getSession()) {
            Transaction tx = session.beginTransaction();
            session.remove(item);
            gridFSUtil.deleteFile(item.getId());
            tx.commit();
        }
    }


    public void update(Image item) {
        try(Session session = HibernateUtil.getInstance().getSession()){
            Transaction tx = session.beginTransaction();
            session.update(item);
            tx.commit();
        }
    }
}