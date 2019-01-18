package br.com.danieldddl.onpoint.model;

public class MarkType {

    private Long id;
    private String name;

    public MarkType(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    //region getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //endregion


    @Override
    public String toString() {
        return "MarkType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
