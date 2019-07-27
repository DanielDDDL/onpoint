package br.com.danieldddl.onpoint.config;

import br.com.danieldddl.onpoint.dao.api.MarkDao;
import br.com.danieldddl.onpoint.dao.api.TypeDao;
import br.com.danieldddl.onpoint.dao.impl.MarkDaoImpl;
import br.com.danieldddl.onpoint.dao.impl.TypeDaoImpl;
import com.google.inject.AbstractModule;

public class MarkServiceModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(MarkDao.class).to(MarkDaoImpl.class);
        bind(TypeDao.class).to(TypeDaoImpl.class);

    }
}
