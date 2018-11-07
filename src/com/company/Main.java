import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, Bot> profileBot = new HashMap<>();
        List<Thread> threads = new ArrayList<>();

        while (true) {
            System.out.println("Введи логин");
            String login = sc.nextLine();

            if (login.equals("stop")) {
                break;
            }

            if (!profileBot.containsKey(login)) {
                Bot bot = new Bot(login);
                Thread botThread = new Thread(bot);
                botThread.start();
                profileBot.put(login, bot);
                threads.add(botThread);
            }

            System.out.println("Введи сообщение");
            String message = sc.nextLine();
            profileBot.get(login).addToQueue(message);
        }

        for (Thread thread : threads) {
            thread.stop();
        }

    }
}
