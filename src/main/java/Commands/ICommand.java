package Commands;

import Models.Image;
import Models.Profile;
import Repos.IRepository;
import org.apache.logging.log4j.Logger;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public interface ICommand {
    ICommand execute(MessageReceivedEvent event, Profile profile, IRepository<Image> repository, Logger logger);
}
