package com.company.Models;

import javax.persistence.*;
import java.io.File;
import java.io.InputStream;
import java.nio.file.attribute.UserDefinedFileAttributeView;

@Entity
public class Image {
    //TODO Как же так, приватные поля, а называются с большой буквы...
    @Id
    private String Id;
    private double Brightness;
    private double Contrast;

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
        return Brightness;
    }

    public void setBrightness(double brightness) {
        Brightness = brightness;
    }

    public double getContrast() {
        return Contrast;
    }

    public void setContrast(double contrast) {
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

    public java.io.InputStream getInputStream() {
        return InputStream;
    }

    public void setInputStream(java.io.InputStream inputStream) {
        InputStream = inputStream;
    }
}
