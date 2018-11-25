package Commands;

import Models.Image;
import Models.Profile;
import Repos.IRepository;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class SaveBrightnessCommand implements ICommand {
    @Override
    public ICommand execute(MessageReceivedEvent event, Profile profile, IRepository repository) {
        List<Image> images = profile.getImages();
        Image image = images.get(images.size()-1);
        double brightness = Double.parseDouble(event.getMessage().getContent());
        image.setBrightness(brightness);
        repository.update(image);
        event.getChannel().sendMessage("И еще одно от 0 до 100");
        return new SaveContrastCommand();
    }
}
