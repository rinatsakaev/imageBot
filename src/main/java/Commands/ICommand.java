package Commands;

import Models.Profile;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.io.IOException;

public interface ICommand {
    ICommand execute(MessageReceivedEvent event, Profile profile) throws IOException;
}
