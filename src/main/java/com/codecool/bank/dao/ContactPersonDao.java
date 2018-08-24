package com.codecool.bank.dao;

import com.codecool.bank.entity.ContactPerson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequestScoped
public class ContactPersonDao {

    private static final Logger LOG = LoggerFactory.getLogger(ContactPersonDao.class);

    @PersistenceContext
    private EntityManager em;

    public ContactPerson save(ContactPerson contactPerson) {
        LOG.debug("save({})", contactPerson);
        em.persist(contactPerson);
        return contactPerson;
    }
}
