package com.codecool.bank.dto;

import com.codecool.bank.entity.Bankcard;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement
public class BankcardDto {

    private String cardType;
    private String cardNumber;
    private String validThru;
    private boolean disabled;
    private String owner;
    private List<ContactInfoDto> contactInfo = new ArrayList<>();

    public BankcardDto() {
    }

    public BankcardDto(Bankcard bankcard) {
        this.cardType = bankcard.getCardType().getTypeName();
        this.cardNumber = bankcard.getCardNumber();
        this.validThru = String.format("%02d/%02d", bankcard.getValidMonth(), bankcard.getValidYear());
        this.disabled = bankcard.isDisabled();
        this.owner = bankcard.getOwnerName();
        this.contactInfo = bankcard.getContactPersons()
            .stream()
            .map(ContactInfoDto::new)
            .collect(Collectors.toList());
    }

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

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
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
