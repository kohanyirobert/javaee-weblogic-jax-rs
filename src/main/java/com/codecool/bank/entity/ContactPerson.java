package com.codecool.bank.entity;

import javax.persistence.*;

@Entity
@Table(name = "contact_person")
public class ContactPerson {

    @Id
    @GeneratedValue(generator = "ContactPersonSequence")
    @SequenceGenerator(name = "ContactPersonSequence", sequenceName = "contact_person_sequence", allocationSize = 1)
    private int id;

    @Column(length = 255, nullable = false)
    private String contact;

    @ManyToOne
    @JoinColumn(name = "contact_type_id", referencedColumnName = "id")
    private ContactType contactType;

    @ManyToOne
    private Bankcard bankcard;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public Bankcard getBankcard() {
        return bankcard;
    }

    public void setBankcard(Bankcard bankcard) {
        this.bankcard = bankcard;
    }
}
