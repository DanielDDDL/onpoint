package br.com.danieldddl.onpoint.dao.api;

import br.com.danieldddl.onpoint.model.MarkType;

public interface IMarkTypeDao {

    MarkType getAndSaveMarkTypeByName (String name);
    MarkType findMarkTypeByName (String name);
    MarkType insert (MarkType markType);
    boolean existsMarkTypeWithName(String name);

}
