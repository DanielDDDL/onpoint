package br.com.danieldddl.onpoint.services.api;

import br.com.danieldddl.onpoint.model.Mark;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public interface MarkService {

    Mark markArrival();

    Mark markArrival (@NotNull LocalDateTime whenArrived);

    Mark markLeaving();

    Mark markLeaving(@NotNull LocalDateTime whenLeft);

    Mark simpleMark();

    Mark simpleMark(@NotNull LocalDateTime when);

    List<Mark> listBetween(@NotNull LocalDateTime firstDate,
                           @NotNull LocalDateTime secondDate);

    List<Mark> listSince(@NotNull LocalDateTime startingDate);

    List<Mark> list(@NotNull Integer numberOfMarks);
}
