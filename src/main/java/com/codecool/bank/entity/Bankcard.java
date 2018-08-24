package com.codecool.bank.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
    @NamedQuery(name = "Bankcard.findAll", query = "SELECT b FROM Bankcard b"),
    @NamedQuery(name = "Bankcard.findByCardNumber", query = "SELECT b FROM Bankcard b WHERE b.cardNumber = :cardNumber")
})
public class Bankcard implements Serializable {

    @Id
    @GeneratedValue(generator = "BankcardSequence")
    @SequenceGenerator(name = "BankcardSequence", sequenceName = "bankcard_sequence", allocationSize = 1)
    private int id;

    @ManyToOne
    @JoinColumn(name = "card_type_id", referencedColumnName = "id")
    private CardType cardType;

    @Column(name = "card_number", length = 19, nullable = false, unique = true)
    private String cardNumber;

    @Column(name = "valid_year", length = 4, nullable = false)
    private Integer validYear;

    @Column(name = "valid_month", length = 2, nullable = false)
    private Integer validMonth;

    @Column(name = "card_hash", length = 32, nullable = false, unique = true)
    private byte[] cardHash;

    @Column(nullable = false)
    private boolean disabled;

    @Column(name = "owner_name", length = 255, nullable = false)
    private String ownerName;

    @OneToMany(mappedBy = "bankcard")
    @JoinColumn(name = "bankcard_id")
    private List<ContactPerson> contactPersons = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getValidYear() {
        return validYear;
    }

    public void setValidYear(Integer validYear) {
        this.validYear = validYear;
    }

    public Integer getValidMonth() {
        return validMonth;
    }

    public void setValidMonth(Integer validMonth) {
        this.validMonth = validMonth;
    }

    public byte[] getCardHash() {
        return cardHash;
    }

    public void setCardHash(byte[] cardHash) {
        this.cardHash = cardHash;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public List<ContactPerson> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(List<ContactPerson> contactPersons) {
        this.contactPersons = contactPersons;
    }
}
