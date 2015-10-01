package com.codepath.simpletodo.data;

import nl.qbusict.cupboard.annotation.Column;

public class TodoItem {
    private Long _id;
    @Column("value")
    private String value;

    public TodoItem() {
        this(null);
    }

    public TodoItem(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
