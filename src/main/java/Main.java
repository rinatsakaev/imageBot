import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.IOException;

public class Main {
    public static void main(String[] args){
        Logger logger = LogManager.getRootLogger();
        IDiscordClient client = DiscordClient.createClient("NTA2NTE3NDQ5NzUyMTgyODE1.DtRlCw.I2puMyJH5hIcKQ2rdDSyBaGiO9g", true); // Gets the client object (from the first example)
        EventDispatcher dispatcher = client.getDispatcher();
        try {
            Bot bot = new Bot();
            Thread botThread = new Thread(bot);
            botThread.start();
            dispatcher.registerListener(bot);
        } catch (IOException e) {
            logger.debug("Something is wrong with bot", e);
        }
    }
}