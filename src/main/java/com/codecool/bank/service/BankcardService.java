package com.codecool.bank.service;

import com.codecool.bank.dao.BankcardDao;
import com.codecool.bank.dao.CardTypeDao;
import com.codecool.bank.entity.Bankcard;
import com.codecool.bank.entity.CardType;
import com.codecool.bank.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

@RequestScoped
public class BankcardService {

    private static class ValidMonthAndYear {

        private int validMonth;
        private int validYear;

        private ValidMonthAndYear(int validMonth, int validYear) {
            this.validMonth = validMonth;
            this.validYear = validYear;
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(BankcardService.class);

    @Inject
    private BankcardDao bankcardDao;

    @Inject
    private CardTypeDao cardTypeDao;

    public List<Bankcard> getAll() {
        LOG.debug("getAll()");
        return bankcardDao.findAll();
    }

    public Bankcard get(String cardNumber) {
        LOG.debug("get({})", cardNumber);
        return bankcardDao.findByCardNumber(cardNumber);
    }

    public Bankcard create(String type, String cardNumber, String cvv, String owner, String validThru) {
        LOG.debug("create({}, {}, {}, {}, {})", type, cardNumber, cvv, owner, validThru);
        if (!Util.isValidCardNumber(cardNumber)
            || !Util.isValidCvv(cvv)
            || Util.isNulLOrEmpty(owner)
            || !Util.isValidValidThru(validThru)) {
            throw new IllegalArgumentException();
        }

        CardType cardType = cardTypeDao.findByTypeName(type);

        Bankcard bankcard = new Bankcard();
        bankcard.setCardType(cardType);
        bankcard.setCardNumber(cardNumber);
        bankcard.setOwnerName(owner);

        ValidMonthAndYear validMonthAndYear = calcValidMonthAndYear(validThru);
        bankcard.setValidMonth(validMonthAndYear.validMonth);
        bankcard.setValidYear(validMonthAndYear.validYear);

        bankcard.setCardHash(calcCardHash(bankcard, cvv));
        return bankcardDao.save(bankcard);
    }

    public void disable(String cardNumber) {
        LOG.debug("disable({})", cardNumber);
        Bankcard bankcard = bankcardDao.findByCardNumber(cardNumber);
        bankcard.setDisabled(true);
    }

    public boolean validate(String type, String cardNumber, String cvv, String validThru) {
        LOG.debug("validate({}, {}, {}, {})", type, cardNumber, cvv, validThru);
        Bankcard bankcard;
        try {
            bankcard = bankcardDao.findByCardNumber(cardNumber);
        } catch (NoResultException ex) {
            return false;
        }

        if (bankcard.isDisabled()
            || !bankcard.getCardType().getTypeName().equals(type)) {
            return false;
        }
        ValidMonthAndYear validMonthAndYear = calcValidMonthAndYear(validThru);
        if (validMonthAndYear.validMonth != bankcard.getValidMonth()
            || validMonthAndYear.validYear != bankcard.getValidYear()) {
            return false;
        }
        byte[] cardHash = calcCardHash(bankcard, cvv);

        return Arrays.equals(bankcard.getCardHash(), cardHash);
    }

    private ValidMonthAndYear calcValidMonthAndYear(String validThru) {
        int monthStartIndex = 0;
        int monthEndIndex = validThru.indexOf('/');
        int yearStartIndex = monthEndIndex + 1;
        int yearEndIndex = yearStartIndex + 2;
        int validMonth = Integer.valueOf(validThru.substring(monthStartIndex, monthEndIndex));
        int validYear = Integer.valueOf(validThru.substring(yearStartIndex, yearEndIndex));
        return new ValidMonthAndYear(validMonth, validYear);
    }

    private byte[] calcCardHash(Bankcard bankcard, String cvv) {
        try {
            String input = bankcard.getCardNumber() + bankcard.getValidMonth() + bankcard.getValidYear() + cvv;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
