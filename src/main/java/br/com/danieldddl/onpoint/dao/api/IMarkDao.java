package br.com.danieldddl.onpoint.dao.api;

import br.com.danieldddl.onpoint.model.Mark;

import java.time.LocalDateTime;
import java.util.List;

public interface IMarkDao {

    Mark persist(Mark mark);
    List<Mark> listLastMarks(int amount);
    List<Mark> listSince (LocalDateTime sinceWhen);
}
