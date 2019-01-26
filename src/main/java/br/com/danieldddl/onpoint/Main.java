package br.com.danieldddl.onpoint;

import br.com.danieldddl.onpoint.config.MarkServiceModule;
import br.com.danieldddl.onpoint.dao.api.IMarkDao;
import br.com.danieldddl.onpoint.services.MarkService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new MarkServiceModule());
        MarkService markService = injector.getInstance(MarkService.class);

        markService.markArrival();
        markService.markLeaving();
        LOGGER.debug(markService.list(2));

        markService.markArrival();
        markService.markLeaving();
        LOGGER.debug(markService.list(2));
    }

}
