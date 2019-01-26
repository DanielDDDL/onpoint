package br.com.danieldddl.onpoint.config;

import br.com.danieldddl.onpoint.dao.api.IMarkDao;
import br.com.danieldddl.onpoint.dao.api.IMarkTypeDao;
import br.com.danieldddl.onpoint.dao.impl.MarkDaoImpl;
import br.com.danieldddl.onpoint.dao.impl.MarkTypeDaoImpl;
import com.google.inject.AbstractModule;

public class MarkServiceModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(IMarkDao.class).to(MarkDaoImpl.class);
        bind(IMarkTypeDao.class).to(MarkTypeDaoImpl.class);

    }
}
