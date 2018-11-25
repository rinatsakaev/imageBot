package Commands;

import Models.Profile;
import Repos.IRepository;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class RequestContrastCommand implements ICommand {
    @Override
    public void execute(MessageReceivedEvent event, Profile profile, IRepository repository) {
        event.getChannel().sendMessage("И еще одно от 0 до 100");
    }
}
