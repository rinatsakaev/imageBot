package Commands;

import Helpers.GridFSUtil;
import Helpers.OpenCVUtil;
import Models.Image;
import Models.Profile;
import Repos.IRepository;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class ReturnImageCommand implements ICommand {
    @Override
    public ICommand execute(MessageReceivedEvent event, Profile profile, IRepository repository) {
        List<Image> images = profile.getImages();
        Image image = images.get(images.size()-1);
         OpenCVUtil openCVUtil = new OpenCVUtil();
        openCVUtil.changeBrightness(image.getId(), image.getBrightness(), image.getContrast());
        File file = new File("output.jpg");
        try {
            event.getChannel().sendFile("Картинка готова", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
       return null;
    }
}
