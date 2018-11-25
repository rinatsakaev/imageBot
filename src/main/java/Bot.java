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
import org.bytedeco.javacpp.presets.opencv_core;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;


import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class Bot implements Runnable {
    private IRepository<Profile> profileRepo;
    private IRepository<Image> imageRepo;
    private Profile profile;
    private BlockingQueue<MessageReceivedEvent> profileRequests;
    private Logger logger;
    private ICommand currentState;
    private Map<String, ICommand> commandMap;

    public Bot() throws IOException {
        profileRepo = new ProfileRepo();
        imageRepo = new ImageRepo();
        profileRequests = new SynchronousQueue<>();
        //TODO Мне нужно, чтобы вы объяснили мне разницу между LogManager.getLogger("Bot") и  LogManager.getRootLogger()
        logger = LogManager.getRootLogger();
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
                if (currentState == null && commandMap.containsKey(message))
                    currentState = commandMap.get(message);
                currentState = currentState.execute(event, profile, imageRepo);
            } catch (Exception e) {
                logger.info(e);
            }
        }
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent e) {
        if (profile == null)
            authorize(e.getAuthor().getStringID());
        //TODO Не очень понятно, почему MessageReceivedEvent можно передавать в другой поток :)
        profileRequests.add(e);
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
}
