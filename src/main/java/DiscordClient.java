import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

public class DiscordClient {
    public static IDiscordClient createClient(String token) {
        ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken(token);
        return clientBuilder.login();
    }
}