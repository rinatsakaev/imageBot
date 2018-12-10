package Commands;

import Models.Image;
import Models.Profile;
import Repos.IRepository;
import org.apache.logging.log4j.Logger;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.List;

public class SaveContrastCommand extends AbstractCommand {
    public SaveContrastCommand(IRepository<Image> imageRepository) {
        super(imageRepository);
    }

    @Override
    public ICommand execute(MessageReceivedEvent event, Profile profile) throws IOException {
        List<Image> images = profile.getImages();
        Image image = images.get(images.size()-1);
        double contrast = Double.parseDouble(event.getMessage().getContent());
        image.setContrast(contrast);
        super.getImageRepository().update(image);
        //TODO Надо возвращать ReturnImageCommand, а не исполнять ее по месту
        new ReturnImageCommand(super.getImageRepository()).execute(event, profile);
        return null;
    }
}
