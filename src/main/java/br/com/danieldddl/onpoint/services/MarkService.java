package br.com.danieldddl.onpoint.services;

import br.com.danieldddl.onpoint.dao.api.IMarkDao;
import br.com.danieldddl.onpoint.model.Mark;

import java.time.LocalDateTime;
import java.util.List;

public class MarkService {

    private IMarkDao markDao;

    public MarkService (IMarkDao markDao) {
        this.markDao = markDao;
    }

    public void markArrival () {

    }

    public void markArrival (LocalDateTime whenArrived) {

    }

    public void markLeaving () {

    }

    public void markLeaving (LocalDateTime whenLeft) {

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
