package Commands;
import Models.Image;
import Models.Profile;
import Repos.IRepository;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class HelpCommand extends AbstractCommand {
    public HelpCommand(IRepository<Image> imageRepository) {
        super(imageRepository);
    }

    @Override
    public ICommand execute(MessageReceivedEvent event, Profile profile) {
        event.getChannel().sendMessage("Команды: ls - показать загруженные картинки\n" +
                "cb - обработать картинку\n" +
                "help - вывести это сообщение еще раз");
        return null;
    }
}
