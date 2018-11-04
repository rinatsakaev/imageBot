package com.company.Helpers;
import java.io.*;
import java.util.Properties;
import java.util.logging.*;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

public class GridFSUtil {
    private DB database;
    private GridFS imageBucket;
    private Logger logger = Logger.getAnonymousLogger();
    private Properties properties;
    public GridFSUtil() {
        properties = getProperties();
        MongoClientURI connectionString = new MongoClientURI(properties.getProperty("MONGO_CONNECTION_STRING"));
        MongoClient mongoClient = new MongoClient(connectionString);
        database = mongoClient.getDB(properties.getProperty("MONGO_DB_NAME"));
        imageBucket = new GridFS(database, properties.getProperty("MONGO_BUCKET_NAME"));
    }

    public String uploadFile(InputStream is) {
        GridFSInputFile gfsFile = imageBucket.createFile(is);
        gfsFile.save();
        return gfsFile.get("_id").toString();
    }

    public void deleteFile(String id) {
        imageBucket.remove(new ObjectId(id));
    }

    public InputStream getFileInputStream(String id) {
        GridFSDBFile imageForOutput = imageBucket.findOne(new ObjectId(id));
        return imageForOutput.getInputStream();
    }

    public File getFileById(String id) {
        File file = new File(id);
        try {
            InputStream inputStream = getFileInputStream(id);
            FileUtils.copyInputStreamToFile(inputStream, file);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            String msg = String.format("Exception in getFileById, id=%s", id);
            logger.log(Level.ALL, msg, e);
        }
        return file;
    }

    private Properties getProperties(){
        Properties prop = new Properties();
        try (InputStream output = new FileInputStream("META-INF/settings.cfg")) {
            prop.load(output);
        } catch (IOException e) {
            logger.log(Level.ALL, "Can't read property file", e);
        }
        return prop;
    }



}
