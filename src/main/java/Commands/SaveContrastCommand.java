package Commands;

import Models.Image;
import Models.Profile;
import Repos.IRepository;
import org.apache.logging.log4j.Logger;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class SaveContrastCommand implements ICommand {
    @Override
    public ICommand execute(MessageReceivedEvent event, Profile profile, IRepository repository, Logger logger) {
        List<Image> images = profile.getImages();
        Image image = images.get(images.size()-1);
        double contrast = Double.parseDouble(event.getMessage().getContent());
        image.setContrast(contrast);
        repository.update(image);
        //TODO Надо возвращать ReturnImageCommand, а не исполнять ее по месту
        new ReturnImageCommand().execute(event, profile, repository, logger);
        return null;
    }
}
