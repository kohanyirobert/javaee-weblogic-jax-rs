package com.codecool.bank.service;

import com.codecool.bank.dao.ContactPersonDao;
import com.codecool.bank.dao.ContactTypeDao;
import com.codecool.bank.entity.Bankcard;
import com.codecool.bank.entity.ContactPerson;
import com.codecool.bank.entity.ContactType;
import com.codecool.bank.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class ContactPersonService {

    private static final Logger LOG = LoggerFactory.getLogger(ContactPersonService.class);

    @Inject
    private ContactTypeDao contactTypeDao;

    @Inject
    private ContactPersonDao contactPersonDao;

    public ContactPerson create(Bankcard bankcard, String type, String contact) {
        LOG.debug("create({}, {}, {})", bankcard, type, contact);
        if (Util.isNulLOrEmpty(contact)) {
            throw new IllegalArgumentException();
        }

        ContactType contactType = contactTypeDao.findByTypeName(type);
        ContactPerson contactPerson = new ContactPerson();
        contactPerson.setBankcard(bankcard);
        contactPerson.setContact(contact);
        contactPerson.setContactType(contactType);
        return contactPersonDao.save(contactPerson);
    }
}
