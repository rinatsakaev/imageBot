package com.company.Helpers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WebUtil {
    public WebUtil(){

    }

    public InputStream getStreamFromURL(String inputUrl){
        InputStream in = null;
        try {
            URL url = new URL(inputUrl);
            URLConnection conn = url.openConnection();
            in = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

}
