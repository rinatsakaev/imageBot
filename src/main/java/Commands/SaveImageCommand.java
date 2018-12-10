package Commands;

import Helpers.WebUtil;
import Models.Image;
import Models.Profile;
import Repos.IRepository;
import org.apache.logging.log4j.Logger;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.io.IOException;
import java.io.InputStream;

public class SaveImageCommand extends AbstractCommand {
    public SaveImageCommand(IRepository<Image> imageRepository) {
        super(imageRepository);
    }

    @Override
    public ICommand execute(MessageReceivedEvent event, Profile profile) throws IOException {
        String url = event.getMessage().getContent();
        try (InputStream inputStream = WebUtil.getStreamFromURL(url)) {
            Image img = new Image(inputStream, profile);
            super.getImageRepository().add(img);
            super.getImageRepository().update(img);
        }
        event.getChannel().sendMessage("Круто. Теперь число от 1.0 до 3.0");
        return new SaveBrightnessCommand(super.getImageRepository());
    }
}
