package Helpers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class WebUtil {
    public WebUtil() {

    }

    public static InputStream getStreamFromURL(String inputUrl) throws IOException {
        URL url = new URL(inputUrl);
        URLConnection conn = url.openConnection();
        return conn.getInputStream();
    }

}