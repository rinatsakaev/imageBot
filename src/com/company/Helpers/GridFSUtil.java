package com.company.Helpers;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GridFSUtil {
    private DB database;
    private GridFS imageBucket;

    public GridFSUtil(){
        MongoClientURI connectionString = new MongoClientURI("mongodb://root:root@localhost:27017/");
        MongoClient mongoClient = new MongoClient(connectionString);
        database = mongoClient.getDB("imagesdb");
        imageBucket = new GridFS(database, "images");
    }

    public String UploadFile(File file){
        GridFSInputFile gfsFile = null;
        try {
            gfsFile  = imageBucket.createFile(file);
            gfsFile.save();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return gfsFile.get("_id").toString();
    }

    public void DeleteFile(String id){
        imageBucket.remove(new ObjectId(id));
    }

    public InputStream GetFileInputStream(String id){
        GridFSDBFile imageForOutput = imageBucket.findOne(new ObjectId(id));
        return imageForOutput.getInputStream();
    }


}
