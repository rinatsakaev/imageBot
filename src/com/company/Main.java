package com.company;
import java.util.*;

public class Main {

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Bot bot = null;
        Map<String, Bot> profileBot = new HashMap<>();
        List<Thread> threads = new ArrayList<>();

        while (true){
            System.out.println("Введи логин");
            String login = sc.nextLine();
            if (!profileBot.containsKey(login)){
                bot = new Bot(login);
                Thread botThread = new Thread(bot);
                botThread.start();
                profileBot.put(login, bot);
                threads.add(botThread);
            }

            System.out.println("Введи сообщение");
            String message = sc.nextLine();
            if (message == "stop"){
                break;
            }
            profileBot.get(login).addToQueue(message);

        }

        for (Thread thread : threads){
            thread.stop();
        }

    }
}
