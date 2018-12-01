package Helpers;

import java.io.*;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private Logger logger = LogManager.getRootLogger();
    private Properties properties;

    //TODO Настройки лучше принимать в конструкторе, а вычитывать их в месте создания GridFSUtil.
    public GridFSUtil() throws IOException  {
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
        try(InputStream inputStream = getFileInputStream(id)) {
            FileUtils.copyInputStreamToFile(inputStream, file);
        } catch (IOException e) {
            //TODO Нужно использовать именнованный логер вместо конкатенации getClass в сообщение
            //TODO Нужно передавать exception напрямую в подсистему логирования, а не конкатенировать мессадж в сообщение, так как этим вы теряете стэктрейс
            //TODO Не очень понятно, почему уровень логирования info
            //TODO Конкретно в этом месте лучше пробрасывать исключение наверх

            String msg = String.format("Exception in getFileById, id=%s", id);
            logger.info(this.getClass() + "\n" + msg + "\n" + e.getMessage());
        }
        return file;
    }

    private Properties getProperties() throws IOException {
        Properties prop = new Properties();
        File file = new File("src/main/resources/settings.cfg");
        try (InputStream output = new FileInputStream(file)) {
            prop.load(output);
            return prop;
        }
    }


}