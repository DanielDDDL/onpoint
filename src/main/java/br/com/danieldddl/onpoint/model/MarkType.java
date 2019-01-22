package br.com.danieldddl.onpoint.model;

public class MarkType {

    private Integer id;
    private String name;

    public MarkType(Integer id, String name) {
        this.id = id;
        this.name = name.toLowerCase();
    }

    public MarkType(String name) {
        this.name = name;
    }

    //region getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
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
