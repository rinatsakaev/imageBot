package com.company;

import com.company.Models.Profile;

public class Main {

    public static void main(String[] args){
        Profile profile = new Profile();
        ProfileRepo repo = new ProfileRepo();
        repo.add(profile);
//        File file = new File("C:\\img.jpg");
//        Image img = new Image(file);
//        img.setProfile(profile);
//        ImageRepo repo1 = new ImageRepo();
//        repo1.add(img);
//        List<Image> imgs = repo1.getAll();
    }
}
