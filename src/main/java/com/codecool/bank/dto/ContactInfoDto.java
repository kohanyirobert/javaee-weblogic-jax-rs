package com.codecool.bank.dto;

import com.codecool.bank.entity.ContactPerson;

public class ContactInfoDto {

    private String type;
    private String contact;

    public ContactInfoDto() {
    }

    public ContactInfoDto(ContactPerson contactPerson) {
        this.type = contactPerson.getContactType().getTypeName();
        this.contact = contactPerson.getContact();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ContactInfoDto) {
            ContactInfoDto other = (ContactInfoDto) o;
            return type.equals(other.type) && contact.equals(other.contact);
        }
        return false;
    }
}
