package Models;

import javax.persistence.*;
import java.io.InputStream;

@Entity
public class Image {
    @Id
    private String id;
    private double brightness;
    private double contrast;

    @Transient
    private InputStream inputStream;

    @ManyToOne
    @JoinColumn(name = "User_Id", nullable = false)
    private Profile profile;

    public Image() {

    }

    public Image(InputStream is, Profile profile) {
        inputStream = is;
        this.profile = profile;
    }

    public double getBrightness() {
        return brightness;
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }

    public double getContrast() { return contrast; }

    public void setContrast(double contrast) {
        this.contrast = contrast;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public java.io.InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(java.io.InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
