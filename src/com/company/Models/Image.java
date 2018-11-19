package Models;

import javax.persistence.*;
import java.io.File;
import java.io.InputStream;
import java.nio.file.attribute.UserDefinedFileAttributeView;

@Entity
public class Image {
    @Id
    private String id;
    private double brightness;
    private double contrast;

    @Transient
    private InputStream InputStream;

    @ManyToOne
    @JoinColumn(name = "User_Id", nullable = false)
    private Profile Profile;

    public Image() {

    }

    public Image(InputStream is, Profile profile) {
        InputStream = is;
        Profile = profile;
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
        return Profile;
    }

    public void setProfile(Profile profile) {
        Profile = profile;
    }

    public java.io.InputStream getInputStream() {
        return InputStream;
    }

    public void setInputStream(java.io.InputStream inputStream) {
        InputStream = inputStream;
    }
}