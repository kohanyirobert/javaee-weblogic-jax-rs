package com.codecool.bank.dao;

import com.codecool.bank.entity.ContactType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequestScoped
public class ContactTypeDao {

    private static final Logger LOG = LoggerFactory.getLogger(ContactTypeDao.class);

    @PersistenceContext
    private EntityManager em;

    public ContactType findByTypeName(String typeName) {
        LOG.debug("findByTypeName({})", typeName);
        return em.createNamedQuery("ContactType.findByTypeName", ContactType.class)
            .setParameter("typeName", typeName)
            .getSingleResult();
    }
}
