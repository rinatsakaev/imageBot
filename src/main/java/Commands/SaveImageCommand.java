package Commands;

import Helpers.WebUtil;
import Models.Image;
import Models.Profile;
import Repos.IRepository;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.io.IOException;
import java.io.InputStream;

public class SaveImageCommand implements ICommand {
    @Override
    public ICommand execute(MessageReceivedEvent event, Profile profile, IRepository repository) {
        String url = event.getMessage().getContent().substring(1);
        try (InputStream inputStream = WebUtil.getStreamFromURL(url)) {
            Image img = new Image(inputStream, profile);
            repository.add(img);
            repository.update(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        event.getChannel().sendMessage("Круто. Теперь число от 1.0 до 3.0");
        return new SaveBrightnessCommand();
    }
}
