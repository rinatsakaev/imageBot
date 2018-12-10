import Commands.*;
import Repos.IRepository;
import Models.Image;
import Models.Profile;
import Repos.ImageRepo;
import Repos.ProfileRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.List;
import java.io.IOException;


import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class Bot implements Runnable {
    private static final Logger logger = LogManager.getLogger("Bot");

    private IRepository<Profile> profileRepo;
    private IRepository<Image> imageRepo;
    private Profile profile;
    private BlockingQueue<MessageReceivedEvent> profileRequests;
    private ICommand currentState;
    private Map<String, ICommand> commandMap;

    public Bot() throws IOException {
        profileRepo = new ProfileRepo();
        imageRepo = new ImageRepo();
        profileRequests = new SynchronousQueue<>();
        commandMap = new HashMap<String, ICommand>() {{
            put("ls", new ListCommand());
            put("cb", new RequestImageCommand());
            put("help", new HelpCommand());
        }};
    }

    @Override
    public void run() {
        while (true) {
            try {
                MessageReceivedEvent event = profileRequests.take();
                String message = event.getMessage().getContent();
                if (currentState == null || commandMap.containsKey(message))
                    currentState = commandMap.get(message);
                //TODO Не просовывать imageRepo аргументом
                //TODO Не просовывать logger аргументами
                currentState = currentState.execute(event, profile, imageRepo, logger);
            } catch (Exception e) {
                //TODO А пользователю то не надо сказать, что произошла ошибка?
                logger.warn(e);
            }
        }
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent e) {
        try {
            authorize(e.getAuthor().getStringID());
        } catch (Exception exc) {
            logger.fatal(exc);
        }
        //TODO Наверное, если прозошла ошибка при авторизации, то не надо это сообщение обрабатывать дальше
        profileRequests.add(e);
    }

    private void authorize(String login) throws Exception{
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
}
