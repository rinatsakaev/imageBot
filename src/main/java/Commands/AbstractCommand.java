package Commands;
import Models.Image;
import Repos.IRepository;

public abstract class AbstractCommand implements ICommand {
    private IRepository<Image> imageRepository;
    public AbstractCommand(IRepository<Image> imageRepository){
        this.imageRepository = imageRepository;
    }

    public IRepository<Image> getImageRepository() {
        return imageRepository;
    }
}
