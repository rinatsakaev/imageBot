package Commands;

import Models.Profile;
import Repos.IRepository;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public interface ICommand {
    void execute(MessageReceivedEvent event, Profile profile, IRepository repository);
}
