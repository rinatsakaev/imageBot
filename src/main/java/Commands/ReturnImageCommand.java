package Commands;

import Helpers.OpenCVUtil;
import Models.Image;
import Models.Profile;
import Repos.IRepository;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class ReturnImageCommand implements ICommand {
    @Override
    public ICommand execute(MessageReceivedEvent event, Profile profile, IRepository repository, Logger logger) {
        List<Image> images = profile.getImages();
        Image image = images.get(images.size()-1);
        OpenCVUtil openCVUtil = new OpenCVUtil();
        Mat outputImage = openCVUtil.changeBrightness(image.getId(), image.getBrightness(), image.getContrast());
        Imgcodecs.imwrite("tmp.jpg", outputImage);
        try {
            File file = new File("tmp.jpg");
            event.getChannel().sendFile("Картинка готова", file);
            //TODO А что если sendFile выкинул исключение?
            file.delete();
        } catch (FileNotFoundException e) {
            logger.warn(e);
        }
       return null;
    }
}
