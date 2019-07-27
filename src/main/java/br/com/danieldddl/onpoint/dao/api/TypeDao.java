package br.com.danieldddl.onpoint.dao.api;

import br.com.danieldddl.onpoint.model.Type;

import javax.validation.constraints.NotNull;

public interface TypeDao {

    Type getOrElsePersistWithName(@NotNull String name);
    Type find(@NotNull Integer id);
    Type find(@NotNull String name);
    Type insert (@NotNull Type type);
    boolean exists(@NotNull String name);

}
