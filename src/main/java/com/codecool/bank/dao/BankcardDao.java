package com.codecool.bank.dao;

import com.codecool.bank.dto.BankcardDto;
import com.codecool.bank.entity.Bankcard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequestScoped
public class BankcardDao {

    private static final Logger LOG = LoggerFactory.getLogger(BankcardDto.class);

    @PersistenceContext
    private EntityManager em;

    public Bankcard save(Bankcard bankcard) {
        LOG.debug("save({})", bankcard);
        em.persist(bankcard);
        return bankcard;
    }

    public List<Bankcard> findAll() {
        LOG.debug("findAll");
        return em.createNamedQuery("Bankcard.findAll", Bankcard.class).getResultList();
    }

    public Bankcard findByCardNumber(String cardNumber) {
        LOG.debug("findByCardNumber({})", cardNumber);
        return em.createNamedQuery("Bankcard.findByCardNumber", Bankcard.class)
            .setParameter("cardNumber", cardNumber)
            .getSingleResult();
    }
}
