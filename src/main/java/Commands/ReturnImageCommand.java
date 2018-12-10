package Commands;

import Helpers.OpenCVUtil;
import Models.Image;
import Models.Profile;
import Repos.IRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ReturnImageCommand extends AbstractCommand {
    private static final Logger logger  = LogManager.getLogger("ReturnImageCommand");

    public ReturnImageCommand(IRepository<Image> imageRepository) {
        super(imageRepository);
    }

    @Override
    public ICommand execute(MessageReceivedEvent event, Profile profile) throws IOException {
        List<Image> images = profile.getImages();
        Image image = images.get(images.size()-1);
        OpenCVUtil openCVUtil = new OpenCVUtil();
        Mat outputImage = openCVUtil.changeBrightness(image.getId(), image.getBrightness(), image.getContrast());
        Imgcodecs.imwrite("tmp.jpg", outputImage);
        File file = new File("tmp.jpg");
        try {
            event.getChannel().sendFile("Картинка готова", file);
        } finally {
            file.delete();
        }
       return null;
    }
}
