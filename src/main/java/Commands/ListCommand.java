package Commands;

import Models.Image;
import Models.Profile;
import Repos.IRepository;
import org.apache.logging.log4j.Logger;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class ListCommand implements ICommand {
    @Override
    public ICommand execute(MessageReceivedEvent event, Profile profile, IRepository repository, Logger logger) {
        if (profile.getImages().size() == 0) {
            event.getChannel().sendMessage("Нет картинок");
            return null;
        }
        for (Image img : profile.getImages())
            event.getChannel().sendMessage(img.getId());
       return null;

    }
}
