package Commands;

import Models.Profile;
import Repos.IRepository;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class RequestImageCommand implements ICommand{
    @Override
    public ICommand execute(MessageReceivedEvent event, Profile profile, IRepository repository) {
        event.getChannel().sendMessage("Дай ссылку на картинку");
        return new SaveImageCommand();
    }
}
