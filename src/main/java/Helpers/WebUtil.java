package Helpers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class WebUtil {
    public WebUtil() {

    }

    public static InputStream getStreamFromURL(String inputUrl) {
        try {
            URL url = new URL(inputUrl);
            URLConnection conn = url.openConnection();
            return conn.getInputStream();
        } catch (IOException e) {
            //TODO Наверное, проглатывать ошибки в данном случае не самая хорошая стратегия
            Logger.getAnonymousLogger().log(Level.ALL, "Cant read stream from url", e);
        }
        return null;
    }

}