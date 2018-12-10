import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

public class DiscordClient {
    private static final Logger logger = LogManager.getLogger("DiscordClient");

    public static IDiscordClient createClient(String token) {
        ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken(token);
        try {
            return clientBuilder.login();
        } catch (DiscordException e) {
            //TODO Кажется, что логирование тут не нужно
            logger.fatal(e);
            throw e;
        }
    }
}