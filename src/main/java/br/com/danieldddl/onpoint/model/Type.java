package br.com.danieldddl.onpoint.model;

public class Type {

    private Integer id;
    private String name;

    public Type(Integer id, String name) {
        this.id = id;
        this.name = name.toLowerCase();
    }

    public Type(String name) {
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
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
