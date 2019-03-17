package br.com.danieldddl.onpoint.dao.api;

import br.com.danieldddl.onpoint.model.MarkType;
import org.jetbrains.annotations.NotNull;

public interface IMarkTypeDao {

    MarkType getAndSaveMarkTypeByName (@NotNull String name);
    MarkType findMarkTypeById (@NotNull Integer id);
    MarkType findMarkTypeByName (@NotNull String name);
    MarkType insert (@NotNull MarkType markType);
    boolean existsMarkTypeWithName(@NotNull String name);

}
