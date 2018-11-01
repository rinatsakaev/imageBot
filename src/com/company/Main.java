package com.company;
import java.util.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


public class Main extends ListenerAdapter {

    public static void main(String[] args){
        JDABuilder jda = new JDABuilder(AccountType.BOT);
        jda.setToken("NTA2NTE3NDQ5NzUyMTgyODE1.DrjSrA.bl7kiFhTl4546XLhLrE2UUvAYMo");
        jda.setAudioEnabled(false);
        Bot bot = new Bot();
        jda.addEventListener(bot);
        Thread threadBot = new Thread(bot);
        threadBot.start();
        try {
            jda.buildAsync();
        }catch (Exception e){
            System.out.println("Bot exc " + e.getLocalizedMessage());
        }
    }
}
