package com.company.Helpers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebUtil {
    public WebUtil() {

    }

    public InputStream getStreamFromURL(String inputUrl) {
        InputStream in = null;
        try {
            URL url = new URL(inputUrl);
            URLConnection conn = url.openConnection();
            in = conn.getInputStream();
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.ALL, "Cant read stream from url", e);
        }
        return in;
    }

}
