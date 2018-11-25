package Commands;

import Models.Profile;
import Repos.IRepository;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class HelpCommand implements ICommand {
    @Override
    public ICommand execute(MessageReceivedEvent event, Profile profile, IRepository repository) {
        event.getChannel().sendMessage("Команды: ls - показать загруженные картинки\n" +
                "cb - обработать картинку\n" +
                "help - вывести это сообщение еще раз");
        return null;
    }
}
