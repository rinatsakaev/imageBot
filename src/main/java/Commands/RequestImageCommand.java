package Commands;
import Models.Image;
import Models.Profile;
import Repos.IRepository;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class RequestImageCommand extends AbstractCommand{
    public RequestImageCommand(IRepository<Image> imageRepository) {
        super(imageRepository);
    }

    @Override
    public ICommand execute(MessageReceivedEvent event, Profile profile) {
        event.getChannel().sendMessage("Дай ссылку на картинку");
        return new SaveImageCommand(super.getImageRepository());
    }
}
