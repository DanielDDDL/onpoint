package br.com.danieldddl.onpoint.dao.api;

import br.com.danieldddl.onpoint.model.Mark;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public interface MarkDao {

    Mark persist(@NotNull Mark mark);
    List<Mark> listLast(int amount);
    List<Mark> listSince (@NotNull LocalDateTime sinceWhen);
    List<Mark> listBetween(@NotNull LocalDateTime lowerDate, @NotNull LocalDateTime upperDate);

}
