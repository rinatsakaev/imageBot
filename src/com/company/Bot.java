package com.company;

import com.company.Helpers.GridFSUtil;
import com.company.Helpers.OpenCVUtil;
import com.company.Helpers.WebUtil;
import com.company.Models.Image;
import com.company.Models.Profile;
import com.company.Repos.ImageRepo;
import com.company.Repos.ProfileRepo;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.impl.MessageImpl;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Bot extends ListenerAdapter implements Runnable{
    private IRepository<Profile> profileRepo;
    private IRepository<Image> imageRepo;
    private Profile profile;
    private WebUtil webUtil;
    private GridFSUtil gridFSUtil;
    private OpenCVUtil openCVUtil;
    private Queue<MessageReceivedEvent> profileRequests;

    public Bot() {
        webUtil = new WebUtil();
        gridFSUtil = new GridFSUtil();
        openCVUtil = new OpenCVUtil();
        profileRepo = new ProfileRepo();
        imageRepo = new ImageRepo();
        profileRequests = new PriorityQueue<>();
    }

    public void onMessageReceived(MessageReceivedEvent e){
        if (e.getMessage().getContent().startsWith("!")){
            authorize(e.getAuthor().getName());
            profileRequests.add(e);
        }

    }

    private void authorize(String login) {
        List<Profile> profiles = profileRepo.getAll();
        for (Profile user : profiles) {
            if (user.getLogin().equals(login))
                profile = user;
        }
        if (profile == null) {
            profile = new Profile(login);
            profileRepo.add(profile);
        }
    }

    private void handleCommand(MessageReceivedEvent e) throws Exception {
        String command = e.getMessage().getContent().substring(1);
        switch (command) {
            case "ls":
                if (profile.getImages() == null) {
                    e.getChannel().sendMessage("Нет картинок").queue();
                    break;
                }
                for (Image img : profile.getImages())
                    e.getChannel().sendMessage(img.getId()).queue();
                break;
            case "cb":
                e.getChannel().sendMessage("Дай ссылку на картинку").queue();
                String url = waitForInput().getMessage().getContent().substring(1);
                InputStream picture = webUtil.getStreamFromURL(url);
                Image img = new Image(picture, profile);
                imageRepo.add(img);
                try {
                    e.getChannel().sendMessage("Круто. Теперь число от 1.0 до 3.0").queue();

                    String bright = waitForInput().getMessage().getContent().substring(1);
                    double brightness = Double.parseDouble(bright);
                    img.setBrightness(brightness);

                    e.getChannel().sendMessage("И еще одно от 0 до 100").queue();

                    String contr = waitForInput().getMessage().getContent().substring(1);
                    double contrast = Double.parseDouble(contr);
                    img.setContrast(contrast);

                } catch (Exception e1) {
                    e.getChannel().sendMessage("Это не то число >:C").queue();
                }

                imageRepo.update(img);
                gridFSUtil.getFileById(img.getId());
                openCVUtil.ChangeBrightness(img.getId(), img.getBrightness(), img.getContrast());
                File file = new File("output.jpg");
                e.getChannel().sendFile(file, e.getChannel().sendMessage("Картинка готова").complete()).queue();
                break;
            case "help":
                e.getChannel().sendMessage("Команды: ls - показать загруженные картинки\n" +
                        "cb - обработать картинку\n" +
                        "help - вывести это сообщение еще раз").queue();
                break;
            case "exit":
                throw new Exception("Exit");
            default:
                break;
        }
    }

    private MessageReceivedEvent waitForInput() {
        while (true)
            if (profileRequests.size() != 0)
                return profileRequests.poll();

    }

    @Override
    public void run() {
        while(true) {
            MessageReceivedEvent cmd = waitForInput();
            try {
                handleCommand(cmd);
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }
    }
}
