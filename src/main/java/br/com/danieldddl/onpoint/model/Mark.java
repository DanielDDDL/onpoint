package br.com.danieldddl.onpoint.model;

import java.time.LocalDateTime;

public class Mark {

    private Integer id;
    private LocalDateTime when;
    private LocalDateTime markedDate;
    private Type type;

    public Mark () {

        //to ensure the same date when saving
        LocalDateTime now = LocalDateTime.now();

        this.when = now;
        this.markedDate = now;
    }

    public Mark (LocalDateTime when) {
        this.when = when;
        this.markedDate = LocalDateTime.now();
    }

    public Mark(Integer id, LocalDateTime when, LocalDateTime markedDate, Type type) {
        this.id = id;
        this.when = when;
        this.markedDate = markedDate;
        this.type = type;
    }

    public Mark (LocalDateTime when, Type type) {
        this.when = when;
        this.markedDate = LocalDateTime.now();
        this.type = type;
    }

    public Mark (Type type) {

        //to ensure the same date when saving
        LocalDateTime now = LocalDateTime.now();

        this.when = now;
        this.markedDate = now;
        this.type = type;
    }

    //region getters and setters
    public Integer getId () {
        return id;
    }

    public void setId (Integer id) {
        this.id = id;
    }

    public LocalDateTime getWhen() {
        return when;
    }

    public void setWhen(LocalDateTime when) {
        this.when = when;
    }

    public LocalDateTime getMarkedDate() {
        return markedDate;
    }

    public void setMarkedDate(LocalDateTime markedDate) {
        this.markedDate = markedDate;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    //endregion

    @Override
    public String toString() {
        return "Mark{" +
                "id=" + id +
                ", when=" + when +
                ", markedDate=" + markedDate +
                ", type=" + type +
                '}';
    }
}
