import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;

public class Main {

    public static void main(String[] args){
        IDiscordClient client = DiscordClient.createClient("NTA2NTE3NDQ5NzUyMTgyODE1.DtRlCw.I2puMyJH5hIcKQ2rdDSyBaGiO9g", true); // Gets the client object (from the first example)
        EventDispatcher dispatcher = client.getDispatcher(); // Gets the EventDispatcher instance for this client instance
        Bot bot = new Bot();
        Thread botThread = new Thread(bot);
        botThread.start();
        dispatcher.registerListener(bot); // Registers the IListener example class from above
    }
}
