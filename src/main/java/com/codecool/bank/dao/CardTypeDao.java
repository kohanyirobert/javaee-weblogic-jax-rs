package com.codecool.bank.dao;

import com.codecool.bank.entity.CardType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequestScoped
public class CardTypeDao {

    private static final Logger LOG = LoggerFactory.getLogger(CardTypeDao.class);

    @PersistenceContext
    private EntityManager em;

    public CardType findByTypeName(String typeName) {
        LOG.debug("findByTypeName({})", typeName);
        return em.createNamedQuery("CardType.findByTypeName", CardType.class)
            .setParameter("typeName", typeName)
            .getSingleResult();
    }
}
