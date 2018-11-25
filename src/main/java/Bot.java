import Commands.*;
import Helpers.GridFSUtil;
import Helpers.OpenCVUtil;
import Repos.IRepository;
import Models.Image;
import Models.Profile;
import Repos.ImageRepo;
import Repos.ProfileRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;
import java.io.IOException;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class Bot implements Runnable{
    private IRepository<Profile> profileRepo;
    private IRepository<Image> imageRepo;
    private Profile profile;
    private GridFSUtil gridFSUtil;
    private OpenCVUtil openCVUtil;
    private BlockingQueue<MessageReceivedEvent> profileRequests;
    private Logger logger;
    private ICommand currentState;

    public Bot() throws IOException {
        gridFSUtil = new GridFSUtil();
        openCVUtil = new OpenCVUtil();
        profileRepo = new ProfileRepo();
        imageRepo = new ImageRepo();
        profileRequests = new SynchronousQueue<>();
        //TODO Мне нужно, чтобы вы объяснили мне разницу между LogManager.getLogger("Bot") и  LogManager.getRootLogger()
        logger = LogManager.getRootLogger();
    }

    @Override
    public void run() {
        while (true) {
            try {
                MessageReceivedEvent event = profileRequests.take();
                handleCommand(event);
                currentState = currentState.execute(event, profile, imageRepo);
            } catch (Exception e) {
                logger.info(String.format("Пользователь %s ввел exit", profile.getLogin()));
                break;
            }
        }
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent e){
        if (e.getMessage().getContent().startsWith("!")){
            authorize(e.getAuthor().getStringID());
            //TODO Не очень понятно, почему MessageReceivedEvent можно передавать в другой поток :)
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
                currentState = new ListCommand();
                break;
            case "cb":
                currentState = new RequestImageCommand();
                break;
            case "help":
                currentState = new HelpCommand();
                break;
            case "exit":
                currentState = new ExitCommand();
                default:
                break;
        }
    }
//    private void listImages(MessageReceivedEvent e) {
//        if (profile.getImages().size() == 0) {
//            e.getChannel().sendMessage("Нет картинок");
//            return;
//        }
//        for (Image img : profile.getImages())
//            e.getChannel().sendMessage(img.getId());
//    }
//
//    private void changeBrightness(MessageReceivedEvent e) throws InterruptedException {
//        //TODO Хотелось бы, чтобы диалог с пользователем шел через несколько вызовов onMessageReceived и состояние между ними где-от хранилось
//        //TODO Сейчас у вас все написано уж очень неявно, а почему вы вообще можете через изначальный e.getChannel что-то посылать клиенту - большой вопрос :)
//        //Указано в документации ^
//        e.getChannel().sendMessage("Дай ссылку на картинку");
//        e = profileRequests.take();
//        String url = e.getMessage().getContent().substring(1);
//        try (InputStream inputStream = WebUtil.getStreamFromURL(url)) {
//            Image img = new Image(inputStream, profile);
//            imageRepo.add(img);
//            try {
//                e.getChannel().sendMessage("Круто. Теперь число от 1.0 до 3.0");
//                e = profileRequests.take();
//                double brightness = Double.parseDouble(e.getMessage().getContent().substring(1));
//                img.setBrightness(brightness);
//                e.getChannel().sendMessage("И еще одно от 0 до 100");
//                e = profileRequests.take();
//                double contrast = Double.parseDouble(e.getMessage().getContent().substring(1));
//                img.setContrast(contrast);
//            } catch (Exception exc) {
//                logger.warn(exc);
//                e.getChannel().sendMessage("Это не то число >:C");
//            }
//            imageRepo.update(img);
//            openCVUtil.changeBrightness(img.getId(), img.getBrightness(), img.getContrast());
//            File file = new File("output.jpg");
//            e.getChannel().sendFile("Картинка готова", file);
//        } catch (IOException exception){
//            e.getChannel().sendMessage("Не могу прочитать картинку");
//            logger.info("Cant read image", exception);
//        }
//    }
}
