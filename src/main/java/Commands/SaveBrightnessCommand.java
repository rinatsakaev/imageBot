package Commands;

import Models.Image;
import Models.Profile;
import Repos.IRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class SaveBrightnessCommand extends AbstractCommand {

    public SaveBrightnessCommand(IRepository<Image> imageRepository) {
        super(imageRepository);
    }

    @Override
    public ICommand execute(MessageReceivedEvent event, Profile profile) {
        List<Image> images = profile.getImages();
        Image image = images.get(images.size()-1);
        double brightness = Double.parseDouble(event.getMessage().getContent());
        image.setBrightness(brightness);
        super.getImageRepository().update(image);
        event.getChannel().sendMessage("И еще одно от 0 до 100");
        return new SaveContrastCommand(super.getImageRepository());
    }
}
