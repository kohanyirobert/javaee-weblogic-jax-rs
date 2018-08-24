package com.codecool.bank.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class NewBankcardDto {

    private String cardType;
    private String cardNumber;
    private String validThru;
    @XmlElement(name = "CVV")
    private String cvv;
    private String owner;
    private List<ContactInfoDto> contactInfo = new ArrayList<>();

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getValidThru() {
        return validThru;
    }

    public void setValidThru(String validThru) {
        this.validThru = validThru;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<ContactInfoDto> getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(List<ContactInfoDto> contactInfo) {
        this.contactInfo = contactInfo;
    }
}
