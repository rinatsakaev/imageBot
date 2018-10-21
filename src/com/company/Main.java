package com.company;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.println("Введи логин");
            String login = sc.next();
            Bot bot = new Bot(login);
            Thread botThread = new Thread(bot);
            botThread.start();
            try {
                botThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
