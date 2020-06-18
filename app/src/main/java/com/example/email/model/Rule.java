package com.example.email.model;

import com.example.email.model.enums.ECondition;
import com.example.email.model.enums.EOperation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Rule implements Serializable {

    @SerializedName("rule_id")
    @Expose
    private int id;

    @SerializedName("operation")
    @Expose
    private EOperation operation;

    @SerializedName("condition")
    @Expose
    private ECondition condition;

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("active")
    @Expose
    private boolean isActive;


    public Rule() {}

    public Rule(int id, EOperation operation, ECondition condition, String value, boolean active) {
        this.id = id;
        this.operation = operation;
        this.condition = condition;
        this.value = value;
        this.isActive = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EOperation getOperation() {
        return operation;
    }

    public void setOperation(EOperation operation) {
        this.operation = operation;
    }

    public ECondition getCondition() {
        return condition;
    }

    public void setCondition(ECondition condition) {
        this.condition = condition;
    }

    public String getValue() { return value; }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isActive() { return isActive; }

    public void setActive(boolean active) { isActive = active; }
}
