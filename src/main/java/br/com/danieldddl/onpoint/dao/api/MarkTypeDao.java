package br.com.danieldddl.onpoint.dao.api;

import br.com.danieldddl.onpoint.model.MarkType;

import javax.validation.constraints.NotNull;

public interface MarkTypeDao {

    MarkType getOrElsePersistByName(@NotNull String name);
    MarkType find(@NotNull Integer id);
    MarkType find(@NotNull String name);
    MarkType insert (@NotNull MarkType markType);
    boolean existsWithName(@NotNull String name);

}
