package com.example.email.model;

import java.util.ArrayList;

public class Folder {

    private int id;
    private String name;
    private ArrayList<Folder> childFolders;
    //private ArrayList<Message> messages;
    private Rule destination;


    public Folder(){}

    public Folder(int id, String name, ArrayList<Folder> childFolders, Rule destination) {
        this.id = id;
        this.name = name;
        this.childFolders = childFolders;
        this.destination = destination;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Folder> getChildFolders() {
        return childFolders;
    }

    public void setChildFolders(ArrayList<Folder> childFolders) {
        this.childFolders = childFolders;
    }

    public Rule getDestination() {
        return destination;
    }

    public void setDestination(Rule destination) {
        this.destination = destination;
    }
}
