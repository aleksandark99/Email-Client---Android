package com.example.email.model;

import com.example.email.model.items.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Message {

    private long id;
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String content;
    private LocalDateTime dateReceived;
    private boolean unread;
    private ArrayList<Tag> tags;




    public Message(){}


    public Message(long id, String from, String to, String cc, String bcc, String subject, String content, LocalDateTime dateReceived, boolean unread, ArrayList<Tag> tags) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.subject = subject;
        this.content = content;
        this.dateReceived = dateReceived;
        this.unread = unread;
        this.tags = tags;
    }
// GETTERS AND SETTERS ///////////////////////////////////////////////////////////////////////
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(LocalDateTime dateReceived) {
        this.dateReceived = dateReceived;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }
    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }
}
