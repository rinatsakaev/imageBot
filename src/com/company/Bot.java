package com.company;

import com.company.Helpers.GridFSUtil;
import com.company.Helpers.OpenCVUtil;
import com.company.Helpers.WebUtil;
import com.company.Models.Image;
import com.company.Models.Profile;
import com.company.Repos.ImageRepo;
import com.company.Repos.ProfileRepo;

import java.io.InputStream;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

//TODO Достаточно странный дизайн получился: ни действия просто не добавить, ни взаимодействие с пользователем не изолировано. При прикручивании телеграмма скорее всего придется полностью переделывать все.
public class Bot implements Runnable {
    private IRepository<Profile> profileRepo;
    private IRepository<Image> imageRepo;
    private Profile profile;
    private WebUtil webUtil;
    private GridFSUtil gridFSUtil;
    private OpenCVUtil openCVUtil;
    //TODO С очередью есть некоторая проблема. Вы добавляете из одного потока, который работает в Main, а удаляете из потока, в котором запущен бот. Вместо этого стоит воспользоваться какой-нибудь потокобезопасной очередью
    private Queue<String> profileRequests;

    public Bot(String login) {
        webUtil = new WebUtil();
        gridFSUtil = new GridFSUtil();
        openCVUtil = new OpenCVUtil();
        profileRepo = new ProfileRepo();
        imageRepo = new ImageRepo();
        profileRequests = new PriorityQueue<>();
        authorize(login);
    }

    @Override
    public void run() {
        System.out.println("Команды: ls - показать загруженные картинки\n" +
                "cb - обработать картинку\n" +
                "help - вывести это сообщение еще раз");
        while (true) {
            String cmd = waitForInput();
            try {
                handleCommand(cmd);
            } catch (Exception e) {
                e.printStackTrace(); //TODO Прямо видно, что вы используете лучшие практики логирования, прямо как я вам на паре показывал :)
            }
        }
    }


    public void addToQueue(String message) {
        profileRequests.add(message);
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

    private void handleCommand(String command) throws Exception {
        switch (command) {
            case "ls":
                if (profile.getImages() == null) {
                    System.out.println("Нет картинок");
                    break;
                }
                for (Image img : profile.getImages())
                    System.out.println(img.getId());
                break;
            case "cb":
                System.out.println("Дай ссылку на картинку");
                String url = waitForInput();
                //TODO А кто закрывает InputStream, который получился из webUtil.getStreamFromURL(url)?
                InputStream picture = webUtil.getStreamFromURL(url);
                Image img = new Image(picture, profile);
                imageRepo.add(img);
                try {
                    System.out.println("Круто. Теперь число от 1.0 до 3.0");

                    double brightness = Double.parseDouble(waitForInput());
                    img.setBrightness(brightness);

                    System.out.println("И еще одно от 0 до 100");

                    double contrast = Double.parseDouble(waitForInput());
                    img.setContrast(contrast);

                } catch (Exception e) {
                    System.out.println("Это не то число >:C");
                }

                imageRepo.update(img);
                gridFSUtil.getFileById(img.getId());
                openCVUtil.changeBrightness(img.getId(), img.getBrightness(), img.getContrast());
                System.out.println("Картинка готова");
                break;
            case "help":
                System.out.println("Команды: ls - показать загруженные картинки\n" +
                        "cb - обработать картинку\n" +
                        "help - вывести это сообщение еще раз");
                break;
            case "exit":
                throw new Exception("Exit");
            default:
                break;
        }
    }

    private String waitForInput() {
        while (true)
            if (profileRequests.size() != 0)
                return profileRequests.poll();
        //TODO Наверное, не стоит молотить в цикле. Обычно у потокобезопасных очередей есть методы, которые позволяют заблокироваться и ждать, пока не придут новые элементы
    }
}
