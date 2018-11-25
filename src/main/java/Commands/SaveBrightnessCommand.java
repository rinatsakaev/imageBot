package Commands;

import Models.Image;
import Models.Profile;
import Repos.IRepository;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class SaveBrightnessCommand implements ICommand {
    @Override
    public void execute(MessageReceivedEvent event, Profile profile, IRepository repository) {
        List<Image> images = profile.getImages();
        Image image = images.get(images.size()-1);
        double brightness = Double.parseDouble(event.getMessage().getContent().substring(1));
        image.setBrightness(brightness);
        repository.update(image);
    }
}
