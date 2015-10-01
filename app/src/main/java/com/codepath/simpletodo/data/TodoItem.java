package com.codepath.simpletodo.data;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import nl.qbusict.cupboard.annotation.Column;

public class TodoItem {
    private Long _id;
    @Column("value")
    private String value;
    @Column("dueDateMillis")
    private Long dueDateMillis;

    public TodoItem() {
        this(null, null);
    }

    public TodoItem(String value) {
        this(value, null);
    }

    public TodoItem(String value, DateTime dueDate) {
        this.value = value;
        this.dueDateMillis = dueDate == null ? null : dueDate.getMillis();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Optional<DateTime> getDueDate() {
        if (dueDateMillis == null) {
            return Optional.absent();
        } else {
            return Optional.of(new DateTime(dueDateMillis));
        }
    }

    public void setDueDate(DateTime dueDate) {
        this.dueDateMillis = dueDate == null ? null : dueDate.getMillis();
    }
}
