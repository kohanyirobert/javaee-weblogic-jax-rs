package com.codecool.bank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Startup;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@Startup
@ApplicationPath("")
public class BankApplication extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(BankApplication.class);

    @PostConstruct
    public void init() {
        LOG.info("init");
    }

    @PreDestroy
    public void destroy() {
        LOG.info("destroy");
    }
}
