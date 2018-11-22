import Helpers.GridFSUtil;
import Helpers.OpenCVUtil;
import Helpers.WebUtil;
import Repos.IRepository;
import Models.Image;
import Models.Profile;
import Repos.ImageRepo;
import Repos.ProfileRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import java.io.InputStream;
import java.util.List;
import java.io.IOException;


import java.io.File;
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

    public Bot() throws IOException {
        gridFSUtil = new GridFSUtil();
        openCVUtil = new OpenCVUtil();
        profileRepo = new ProfileRepo();
        imageRepo = new ImageRepo();
        profileRequests = new SynchronousQueue<>();
        logger = LogManager.getRootLogger();
    }

    @Override
    public void run() {
        while (true) {
            try {
                MessageReceivedEvent cmd = profileRequests.take();
                handleCommand(cmd);
            } catch (Exception e) {
                break;
            }
        }
    }

    @EventSubscriber
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
                listImages(e);
                break;
            case "cb":
                changeBrightness(e);
                break;
            case "help":
                e.getChannel().sendMessage("Команды: ls - показать загруженные картинки\n" +
                        "cb - обработать картинку\n" +
                        "help - вывести это сообщение еще раз");
                break;
            case "exit":
                throw new Exception("Exit");
            default:
                break;
        }
    }
    private void listImages(MessageReceivedEvent e) {
        if (profile.getImages().size() == 0) {
            e.getChannel().sendMessage("Нет картинок");
            return;
        }
        for (Image img : profile.getImages())
            e.getChannel().sendMessage(img.getId());
    }

    private void changeBrightness(MessageReceivedEvent e) throws InterruptedException {
        e.getChannel().sendMessage("Дай ссылку на картинку");
        String url = profileRequests.take().getMessage().getContent().substring(1);
        try (InputStream inputStream = WebUtil.getStreamFromURL(url)) {
            Image img = new Image(inputStream, profile);
            imageRepo.add(img);
            try {
                e.getChannel().sendMessage("Круто. Теперь число от 1.0 до 3.0");
                String bright = profileRequests.take().getMessage().getContent().substring(1);
                double brightness = Double.parseDouble(bright);
                img.setBrightness(brightness);
                e.getChannel().sendMessage("И еще одно от 0 до 100");
                String contr = profileRequests.take().getMessage().getContent().substring(1);
                double contrast = Double.parseDouble(contr);
                img.setContrast(contrast);
            } catch (Exception exc) {
                e.getChannel().sendMessage("Это не то число >:C");
            }
            imageRepo.update(img);
            gridFSUtil.getFileById(img.getId());
            openCVUtil.changeBrightness(img.getId(), img.getBrightness(), img.getContrast());
            File file = new File("output.jpg");
            e.getChannel().sendFile("Картинка готова", file);
        } catch (IOException exception){
            e.getChannel().sendMessage("Не могу прочитать картинку");
            logger.info("Cant read image", exception);
        }
    }
}
