package br.com.danieldddl.onpoint.model;

import java.time.LocalDateTime;

public class Mark {

    private Integer id;
    private LocalDateTime when;
    private LocalDateTime markedDate;
    private MarkType markType;

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

    public Mark(Integer id, LocalDateTime when, LocalDateTime markedDate, MarkType markType) {
        this.id = id;
        this.when = when;
        this.markedDate = markedDate;
        this.markType = markType;
    }

    public Mark(LocalDateTime when, LocalDateTime markedDate, MarkType markType) {
        this.when = when;
        this.markedDate = markedDate;
        this.markType = markType;
    }

    public Mark (LocalDateTime when, MarkType markType) {
        this.when = when;
        this.markedDate = LocalDateTime.now();
        this.markType = markType;
    }

    public Mark (MarkType markType) {

        //to ensure the same date when saving
        LocalDateTime now = LocalDateTime.now();

        this.when = now;
        this.markedDate = now;
        this.markType = markType;
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

    public MarkType getMarkType() {
        return markType;
    }

    public void setMarkType(MarkType markType) {
        this.markType = markType;
    }
    //endregion

    @Override
    public String toString() {
        return "Mark{" +
                "id=" + id +
                ", when=" + when +
                ", markedDate=" + markedDate +
                ", markType=" + markType +
                '}';
    }
}
