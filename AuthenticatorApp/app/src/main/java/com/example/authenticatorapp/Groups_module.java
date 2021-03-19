package com.example.authenticatorapp;

public class Groups_module {
    public String group_name, group_description,image;

    public Groups_module(String group_name, String group_description, String image) {
        this.group_name = group_name;
        this.group_description = group_description;
        this.image = image;
    }

    public String getGroup_name() {
        return group_name;
    }

    public Groups_module() {
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_description() {
        return group_description;
    }

    public void setGroup_description(String group_description) {
        this.group_description = group_description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
