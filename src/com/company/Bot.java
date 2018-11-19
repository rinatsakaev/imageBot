import Helpers.GridFSUtil;
import Helpers.OpenCVUtil;
import Helpers.WebUtil;
import Repos.IRepository;
import Models.Image;
import Models.Profile;
import Repos.ImageRepo;
import Repos.ProfileRepo;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.impl.MessageImpl;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.File;

public class Bot extends ListenerAdapter implements Runnable{
    private IRepository<Profile> profileRepo;
    private IRepository<Image> imageRepo;
    private Profile profile;
    private WebUtil webUtil;
    private GridFSUtil gridFSUtil;
    private OpenCVUtil openCVUtil;
    private BlockingQueue<MessageReceivedEvent> profileRequests;

    public Bot() {
        gridFSUtil = new GridFSUtil();
        openCVUtil = new OpenCVUtil();
        profileRepo = new ProfileRepo();
        imageRepo = new ImageRepo();
        profileRequests = new SynchronousQueue<>();
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

    public void addCommand(MessageReceivedEvent message) {
        profileRequests.offer(message);
    }

    public void onMessageReceived(MessageReceivedEvent e){
        if (e.getMessage().getContent().startsWith("!")){
            authorize(e.getAuthor().getName());
            profileRequests.add(e);
        }

    }

    public void authorize(String login) {
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
                String url = profileRequests.take().getMessage().getContent().substring(1);
                try (InputStream inputStream = WebUtil.getStreamFromURL(url)) {
                    Image img = new Image(inputStream, profile);
                    imageRepo.add(img);
                    try {
                        e.getChannel().sendMessage("Круто. Теперь число от 1.0 до 3.0").queue();

                        String bright = profileRequests.take().getMessage().getContent().substring(1);
                        double brightness = Double.parseDouble(bright);
                        img.setBrightness(brightness);

                        e.getChannel().sendMessage("И еще одно от 0 до 100").queue();;

                        String contr = profileRequests.take().getMessage().getContent().substring(1);
                        double contrast = Double.parseDouble(contr);
                        img.setContrast(contrast);

                    } catch (Exception exc) {
                        e.getChannel().sendMessage("Это не то число >:C").queue();
                    }
                    imageRepo.update(img);
                    gridFSUtil.getFileById(img.getId());
                    openCVUtil.changeBrightness(img.getId(), img.getBrightness(), img.getContrast());
                    File file = new File("output.jpg");
                    e.getChannel().sendFile(file, e.getChannel().sendMessage("Картинка готова").complete()).queue();
                }
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
}
