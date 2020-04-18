package com.example.email.model;

import com.example.email.model.enums.ECondition;
import com.example.email.model.enums.EOperation;

public class Rule {

    private int id;
    private EOperation operation;
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
