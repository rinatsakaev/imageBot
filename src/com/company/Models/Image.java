package com.company.Models;

import javax.persistence.*;
import java.io.File;

@Entity
public class Image {
    @Id
    private String Id;
    private int Brightness;
    private int Contrast;

    @Transient
    private File File;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private Profile Profile;

    public Image() {

    }

    public Image(File file){
        File = file;
    }

    public int getBrightness() {
        return Brightness;
    }

    public void setBrightness(int brightness) {
        Brightness = brightness;
    }

    public int getContrast() {
        return Contrast;
    }

    public void setContrast(int contrast) {
        Contrast = contrast;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }


    public Profile getProfile() {
        return Profile;
    }

    public void setProfile(Profile profile) {
        Profile = profile;
    }

    public File getFile() {
        return File;
    }

    public void setFile(File file) {
        File = file;
    }

}
