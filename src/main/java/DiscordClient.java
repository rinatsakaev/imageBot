import org.apache.logging.log4j.Logger;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

public class DiscordClient {
    //TODO Не нашел использования DispordClient
    //TODO Logger не нужно тащить как параметр
    public static IDiscordClient createClient(String token, boolean login, Logger logger) {
        ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken(token);
        try {
            if (login) {
                return clientBuilder.login();
            } else {
                return clientBuilder.build();
            }
        } catch (DiscordException e) {
            //TODO Не очень понятно, почему уровень логирования info, а не возвращать null, так как скорее всего вызывающий код не будет ожидать этого
            //TODO Конкретно в этом месте лучше пробрасывать исключение наверх
            logger.info("Cant start client", e);
            return null;
        }
    }
}