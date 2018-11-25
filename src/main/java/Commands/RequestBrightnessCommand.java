package Commands;

import Models.Profile;
import Repos.IRepository;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class RequestBrightnessCommand implements ICommand {
    @Override
    public void execute(MessageReceivedEvent event, Profile profile, IRepository repository) {
        event.getChannel().sendMessage("Круто. Теперь число от 1.0 до 3.0");
    }
}
