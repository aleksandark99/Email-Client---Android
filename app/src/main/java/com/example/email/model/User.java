package com.example.email.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.HashSet;
import java.util.Set;

public class User {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("firstName")
    @Expose
    private String firstName;

    @SerializedName("lastName")
    @Expose
    private String lastName;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("roles")
    @Expose
    private String roles; //ROLE_USER

    @SerializedName("contacts")
    @Expose
    private Set<Contact> contacts;

    @SerializedName("accounts")
    @Expose
    private Set<Account> accounts;

    public User(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.roles = "ROLE_USER";
        this.contacts = new HashSet<Contact>() {};
        this.accounts = new HashSet<Account>() {};
    }
    public User(int id, String firstName, String lastName, String username, String password, String roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.roles = roles;

    }

    public User(int id, String firstName, String lastName, String username, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.roles = "ROLE_USER";
    }

    public User(int id, String firstName, String lastName, String username, String password, String roles, Set<Contact> contacts) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.contacts = contacts;
    }

    /*    public void add(Contact contact){
        if (contact.getUser() != null){
            contact.getUser().getContacts().remove(contact);
        }
        getContacts().add(contact);
        contact.setUser(this);
    }*/

    public User() {
        this.roles = "ROLE_USER";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles='" + roles + '\'' +
                ", contacts=" + contacts +
                ", accounts=" + accounts +
                '}';
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }
}

