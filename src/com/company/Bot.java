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

public class Bot implements Runnable {
    private IRepository<Profile> profileRepo;
    private IRepository<Image> imageRepo;
    private Profile profile;
    private WebUtil webUtil;
    private GridFSUtil gridFSUtil;
    private OpenCVUtil openCVUtil;
    private BlockingQueue<String> profileRequests;

    public Bot(String login) {
        webUtil = new WebUtil();
        gridFSUtil = new GridFSUtil();
        openCVUtil = new OpenCVUtil();
        profileRepo = new ProfileRepo();
        imageRepo = new ImageRepo();
        profileRequests = new SynchronousQueue<>();
        authorize(login);
    }

    @Override
    public void run() {
        System.out.println("Команды: ls - показать загруженные картинки\n" +
                "cb - обработать картинку\n" +
                "help - вывести это сообщение еще раз");
        while (true) {
            String cmd = null;
            try {
                cmd = profileRequests.take();
                handleCommand(cmd);
            } catch (Exception e) {
                break;
            }
        }
    }

    public void addToQueue(String message) {
        profileRequests.offer(message);
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
                String url = profileRequests.take();
                try (InputStream inputStream = webUtil.getStreamFromURL(url)) {
                    Image img = new Image(inputStream, profile);
                    imageRepo.add(img);
                    try {
                        System.out.println("Круто. Теперь число от 1.0 до 3.0");

                        double brightness = Double.parseDouble(profileRequests.take());
                        img.setBrightness(brightness);

                        System.out.println("И еще одно от 0 до 100");

                        double contrast = Double.parseDouble(profileRequests.take());
                        img.setContrast(contrast);

                    } catch (Exception e) {
                        System.out.println("Это не то число >:C");
                    }

                    imageRepo.update(img);
                    gridFSUtil.getFileById(img.getId());
                    openCVUtil.changeBrightness(img.getId(), img.getBrightness(), img.getContrast());
                }
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
}
