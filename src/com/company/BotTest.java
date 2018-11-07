import junit.framework.TestCase;
import java.util.HashMap;
import java.util.Map;

public class BotTest extends TestCase {

    public void testOneUser() {
        try {
            Bot bot = new Bot("login");
            Thread botThread = new Thread(bot);
            botThread.start();
            bot.addToQueue("help");
            assert (true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert (false);
        }
    }

    public void testTwoUser() {
        try {
            Bot bot1 = new Bot("1");
            Bot bot2 = new Bot("2");
            Thread botThread1 = new Thread(bot1);
            botThread1.start();
            Thread botThread2 = new Thread(bot2);
            botThread2.start();
            bot1.addToQueue("cb");
            bot2.addToQueue("help");
            assert (true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert (false);
        }
    }

    public void testTenUser() {
        try {
            Map<String, Bot> profileBot = new HashMap<>();
            for (int i = 0; i < 10; i++) {
                Bot bot = new Bot(Integer.toString(i));
                System.out.println(i);
                Thread botThread = new Thread(bot);
                botThread.start();
                profileBot.put(Integer.toString(i), bot);
            }
            for (int i = 0; i < profileBot.size(); i++) {
                profileBot.get(Integer.toString(i)).addToQueue("cb");
            }
            assert (true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert (false);
        }
    }

}