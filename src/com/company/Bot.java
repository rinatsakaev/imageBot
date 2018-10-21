package com.company;

import com.company.Helpers.GridFSUtil;
import com.company.Helpers.OpenCVUtil;
import com.company.Helpers.WebUtil;
import com.company.Models.Image;
import com.company.Models.Profile;
import com.company.Repos.ImageRepo;
import com.company.Repos.ProfileRepo;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Bot implements Runnable {
    private IRepository<Profile> profileRepo;
    private IRepository<Image> imageRepo;
    private Profile profile;
    private WebUtil webUtil;
    private GridFSUtil gridFSUtil;
    private OpenCVUtil openCVUtil;
    public Bot(String login){
        webUtil = new WebUtil();
        gridFSUtil = new GridFSUtil();
        openCVUtil =  new OpenCVUtil();
        profileRepo = new ProfileRepo();
        imageRepo = new ImageRepo();
        authorize(login);
    }

    @Override
    public void run() {
        System.out.println("Команды: ls - показать загруженные картинки\n" +
                "cb - обработать картинку");
        String line = "";
        Scanner sc = new Scanner(System.in);
        while (true) {
            line = sc.nextLine();
            try {
                handleCommand(line);
            } catch (Exception e){
                break;
            }

        }
    }

    private void authorize(String login){
        List<Profile> profiles = profileRepo.getAll();
        for (Profile user:profiles) {
            if (user.getLogin().equals(login))
                profile = user;
        }
        if (profile == null){
            profile = new Profile(login);
            profileRepo.add(profile);
        }
    }

    private void handleCommand(String command) throws Exception {
        switch (command){
            case "ls":
                for (Image img:profile.getImages())
                    System.out.println(img.getId());
                break;
            case "cb":
                System.out.println("Дай ссылку на картинку");
                Scanner sc = new Scanner(System.in);
                String url = "https://pp.userapi.com/c849520/v849520259/68826/g7DgzbnYI7Q.jpg";
                InputStream picture = webUtil.getStreamFromURL(url);
                Image img = new Image(picture, profile);
                imageRepo.add(img);
                System.out.println("Круто. Теперь число от 1.0 до 3.0");
                int alpha = sc.nextInt();
                img.setBrightness(alpha);
                System.out.println("И еще одно от 0 до 100");
                int beta = sc.nextInt();
                img.setContrast(beta);
                imageRepo.update(img);
                gridFSUtil.getFileById(img.getId());
                openCVUtil.ChangeBrightness(img.getId(), img.getBrightness(), img.getContrast());
                System.out.println("Картинка готова");
                break;
            case "exit":
                    throw new Exception("Exit");
        }
    }
}
