package br.com.danieldddl.onpoint.services;

import br.com.danieldddl.onpoint.dao.api.MarkDao;
import br.com.danieldddl.onpoint.dao.api.TypeDao;
import br.com.danieldddl.onpoint.model.Mark;
import br.com.danieldddl.onpoint.model.Type;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.*;

public class MarkService {

    private static final String ARRIVAL_MARK_NAME = "Arrival";
    private static final String LEAVING_MARK_NAME = "Leaving";

    private MarkDao markDao;
    private TypeDao typeDao;

    @Inject
    public MarkService(MarkDao markDao, TypeDao typeDao) {
        this.markDao = markDao;
        this.typeDao = typeDao;
    }

    public Mark markArrival () {
        Type arrivalType = typeDao.getOrElsePersistWithName(ARRIVAL_MARK_NAME);
        Mark arrivalMark = new Mark(arrivalType);

        return markDao.persist(arrivalMark);
    }

    public Mark markArrival (@NotNull LocalDateTime whenArrived) {

        requireNonNull(whenArrived);

        Type arrivalType = typeDao.getOrElsePersistWithName(ARRIVAL_MARK_NAME);
        Mark arrivalMark = new Mark(whenArrived, arrivalType);

        return markDao.persist(arrivalMark);
    }

    public Mark markLeaving () {
        Type leavingType = typeDao.getOrElsePersistWithName(LEAVING_MARK_NAME);
        Mark leavingMark = new Mark(leavingType);

        return markDao.persist(leavingMark);
    }

    public Mark markLeaving (@NotNull LocalDateTime whenLeft) {

        requireNonNull(whenLeft);

        Type leavingType = typeDao.getOrElsePersistWithName(LEAVING_MARK_NAME);
        Mark leavingMark = new Mark(whenLeft, leavingType);

        return markDao.persist(leavingMark);
    }

    public Mark simpleMark () {
        Mark simpleMark = new Mark();
        return markDao.persist(simpleMark);
    }

    public Mark simpleMark (@NotNull LocalDateTime when) {

        requireNonNull(when);

        Mark simpleMark = new Mark(when);
        return markDao.persist(simpleMark);
    }

    public List<Mark> listBetween (@NotNull LocalDateTime firstDate,
                                   @NotNull LocalDateTime secondDate) {

        requireNonNull(firstDate);
        requireNonNull(secondDate);

        //guaranteed for the first argument to be the lower date
        return firstDate.isBefore(secondDate) ?
                markDao.listBetween(firstDate, secondDate) :
                markDao.listBetween(secondDate, firstDate);
    }

    public List<Mark> listSince (@NotNull LocalDateTime startingDate) {
        requireNonNull(startingDate);
        return markDao.listSince(startingDate);
    }

    public List<Mark> list (@NotNull Integer numberOfMarks) {
        requireNonNull(numberOfMarks);
        return markDao.listLast(numberOfMarks);
    }

}
