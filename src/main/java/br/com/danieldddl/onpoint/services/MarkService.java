package br.com.danieldddl.onpoint.services;

import br.com.danieldddl.onpoint.dao.api.IMarkDao;
import br.com.danieldddl.onpoint.dao.api.IMarkTypeDao;
import br.com.danieldddl.onpoint.model.Mark;
import br.com.danieldddl.onpoint.model.MarkType;

import java.time.LocalDateTime;
import java.util.List;

public class MarkService {

    private static final String ARRIVAL_MARK_NAME = "Arrival";
    private static final String LEAVING_MARK_NAME = "Leaving";

    private IMarkDao markDao;
    private IMarkTypeDao markTypeDao;

    public MarkService (IMarkDao markDao, IMarkTypeDao markTypeDao) {
        this.markDao = markDao;
        this.markTypeDao = markTypeDao;
    }

    public Mark markArrival () {
        MarkType arrivalType = markTypeDao.getAndSaveMarkTypeByName(ARRIVAL_MARK_NAME);
        Mark arrivalMark = new Mark(arrivalType);

        return markDao.persist(arrivalMark);
    }

    public Mark markArrival (LocalDateTime whenArrived) {
        MarkType arrivalType = markTypeDao.getAndSaveMarkTypeByName(ARRIVAL_MARK_NAME);
        Mark arrivalMark = new Mark(whenArrived, arrivalType);

        return markDao.persist(arrivalMark);
    }

    public Mark markLeaving () {
        MarkType leavingType = markTypeDao.getAndSaveMarkTypeByName(LEAVING_MARK_NAME);
        Mark leavingMark = new Mark(leavingType);

        return markDao.persist(leavingMark);
    }

    public Mark markLeaving (LocalDateTime whenLeft) {
        MarkType leavingType = markTypeDao.getAndSaveMarkTypeByName(LEAVING_MARK_NAME);
        Mark leavingMark = new Mark(whenLeft, leavingType);

        return markDao.persist(leavingMark);
    }

    public Mark simpleMark () {
        Mark simpleMark = new Mark();
        return markDao.persist(simpleMark);
    }

    public Mark simpleMark (LocalDateTime when) {
        Mark simpleMark = new Mark(when);
        return markDao.persist(simpleMark);
    }

    public List<Mark> listBetween (LocalDateTime startingDate, LocalDateTime endingDate) {
        return null;
    }

    public List<Mark> listSince (LocalDateTime startingDate) {
        return null;
    }

    public List<Mark> list (Integer numberOfMarks) {
        return null;
    }

    public void generateTable (LocalDateTime startingDate, LocalDateTime endingDate) {

    }

}
