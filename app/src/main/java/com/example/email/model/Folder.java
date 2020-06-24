package com.example.email.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Set;

public class Folder implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("parent_folder")
    @Expose
    private Folder parent_folder;


    @SerializedName("childFolders")
    @Expose
    private Set<Folder> childFolders;


    @SerializedName("messages")
    @Expose
    private Set<Message> messages;


    @SerializedName("destination")
    @Expose
    private Set<Rule> destination;

    @SerializedName("active")
    @Expose
    private boolean isActive;



    public Folder(){}

    public Folder(int id, String name, Folder parent_folder, Set<Folder> childFolders, Set<Message> messages, Set<Rule> destination, boolean isActive) {
        this.id = id;
        this.name = name;
        this.parent_folder = parent_folder;
        this.childFolders = childFolders;
        this.messages = messages;
        this.destination = destination;
        this.isActive = isActive;
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

    public Folder getParent_folder() {
        return parent_folder;
    }

    public void setParent_folder(Folder parent_folder) {
        this.parent_folder = parent_folder;
    }

    public Set<Folder> getChildFolders() {
        return childFolders;
    }

    public void setChildFolders(Set<Folder> childFolders) {
        this.childFolders = childFolders;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Set<Rule> getDestination() {
        return destination;
    }

    public void setDestination(Set<Rule> destination) {
        this.destination = destination;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @NonNull
    @Override
    public String toString() {

        return name;
    }
}