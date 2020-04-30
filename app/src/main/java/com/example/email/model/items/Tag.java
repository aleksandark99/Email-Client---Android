package com.example.email.model.items;

import java.io.Serializable;

public class Tag implements Serializable {

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
