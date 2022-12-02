package com.uqac.calculator.model;

public class Calculation {

    private String id;
    private String operation;
    private String result;

    public Calculation() {

    }

    public Calculation(String operation, String result) {
        this.id = generateId();
        this.operation = operation;
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private String generateId() {
        return java.util.UUID.randomUUID().toString();
    }
}

