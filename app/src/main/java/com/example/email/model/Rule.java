package com.example.email.model;

import com.example.email.model.enums.ECondition;
import com.example.email.model.enums.EOperation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Rule implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("operation")
    @Expose
    private EOperation operation;

    @SerializedName("condition")
    @Expose
    private ECondition condition;


    public Rule() {}

    public Rule(int id, EOperation operation, ECondition condition) {
        this.id = id;
        this.operation = operation;
        this.condition = condition;
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
}
