package com.example.email.model;

import com.example.email.model.items.Tag;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.function.Predicate;

public class Message implements Serializable {

    private long id;
    private String from;
//    private String to;
//    private String cc;
//    private String bcc;
    ArrayList<String> to;
    ArrayList<String> cc;
    ArrayList<String> bcc;

    private String subject;
    private String content;
    private LocalDateTime dateReceived;
    private boolean unread;
    private ArrayList<Tag> tags;
    private ArrayList<Attachment> attachments;




    public Message(){
        this.from = "";
        this.to = new ArrayList<String> ();
        this.cc = new ArrayList<String> ();
        this.bcc = new ArrayList<String> ();
        this.subject = "";
        this.content = "";
        this.tags = new ArrayList<Tag>();
        this.attachments=new ArrayList<Attachment>();


    }


    public Message(long id, String from, ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc, String subject, String content, LocalDateTime dateReceived, boolean unread, ArrayList<Tag> tags) {
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

    public ArrayList<String> getTo() {
        return to;
    }

    public void setTo(ArrayList<String> to) {
        this.to = to;
    }

    public ArrayList<String> getCc() {
        return cc;
    }

    public void setCc(ArrayList<String> cc) {
        this.cc = cc;
    }

    public ArrayList<String> getBcc() {
        return bcc;
    }

    public void setBcc(ArrayList<String> bcc) {
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

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<Attachment> attachments) {
        this.attachments = attachments;
    }


}
