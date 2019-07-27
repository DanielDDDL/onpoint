package br.com.danieldddl.onpoint.dao.api;

import br.com.danieldddl.onpoint.dao.impl.MarkDaoImpl;
import br.com.danieldddl.onpoint.dao.impl.MarkTypeDaoImpl;
import br.com.danieldddl.onpoint.model.Mark;
import br.com.danieldddl.onpoint.model.MarkType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class MarkDaoTest {

    private MarkDao markDao;
    private MarkTypeDao markTypeDao;

    public MarkDaoTest (MarkDao markDao, MarkTypeDao markTypeDao) {
        this.markDao = markDao;
        this.markTypeDao = markTypeDao;
    }

    /**
     * All instances that will be tested will be passed as
     * parameter to the function which receive the objects defined below
     * */
    @Parameterized.Parameters
    public static Collection<Object[]> instancesToTest() {

        MarkTypeDao markTypeDao = new MarkTypeDaoImpl();
        MarkDao markDao = new MarkDaoImpl(markTypeDao);

        return Arrays.asList(new Object[][] {
                { markDao, markTypeDao }
        });
    }

    @Test
    public void shouldPersistMarkWithoutType () {

        Mark simpleMark = new Mark();
        Mark createdMarkWithoutType = markDao.persist(simpleMark);

        assertNotNull(createdMarkWithoutType.getId());
        assertNull(createdMarkWithoutType.getMarkType());
        assertEquals(createdMarkWithoutType.getMarkedDate(), createdMarkWithoutType.getWhen()); //should be equal, none informed
    }

    @Test
    public void shouldPersistMarkWithSpecificDate () {

        int year = 2015;
        Month month = Month.JANUARY;
        int day = 20;
        int hours = 15;
        int minutes = 30;

        LocalDateTime markDate = LocalDateTime.of(year, month, day, hours, minutes);
        Mark mark = new Mark(markDate);
        Mark persistedMark = markDao.persist(mark);

        assertNotNull(persistedMark);
        assertNotNull(persistedMark.getId());
        assertEquals(persistedMark.getWhen(), LocalDateTime.of(year, month, day, hours, minutes));
    }

    @Test
    public void shouldPersistMarkWithType() {

        String markTypeDescription = "Arrival";
        MarkType markType = markTypeDao.getOrElsePersistByName(markTypeDescription);

        Mark markWithType = new Mark(markType);
        Mark createdMarkWithType = markDao.persist(markWithType);

        assertNotNull(createdMarkWithType.getId());
        assertNotNull(createdMarkWithType.getMarkType().getId());
        assertEquals(createdMarkWithType.getMarkType().getName(), markTypeDescription);
    }

}