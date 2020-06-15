package com.example.email.model;

import java.io.Serializable;

public class Attachment implements Serializable {
    private String name;
    private byte[]  data;
    private String mime_type;


    public Attachment(String name, byte[] data, String mime_type) {
        this.name = name;
        this.data = data;
        this.mime_type = mime_type;
    }
    public Attachment(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }
}
