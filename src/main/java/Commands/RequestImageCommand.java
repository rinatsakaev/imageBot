package Commands;

import Models.Profile;
import Repos.IRepository;
import org.apache.logging.log4j.Logger;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class RequestImageCommand implements ICommand{
    @Override
    public ICommand execute(MessageReceivedEvent event, Profile profile, IRepository repository, Logger logger) {
        event.getChannel().sendMessage("Дай ссылку на картинку");
        return new SaveImageCommand();
    }
}
