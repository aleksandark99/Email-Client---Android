package com.example.email.model.items;

public class Tag {

    private int id;
    private String tagName;

    public Tag(int id, String tagName) {
        this.id = id;
        this.tagName = tagName;
    }

    public Tag() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
