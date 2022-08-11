package com.example.hbd.Model;

public class NewsModel {

    String Title;
    String Des;
    String Image;
    String Id;


    public NewsModel() {
    }

    public NewsModel(String title, String des, String image, String Id) {
        Title = title;
        Des = des;
        Image = image;
        Id = Id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String des) {
        this.Des = des;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
