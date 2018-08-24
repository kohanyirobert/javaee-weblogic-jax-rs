package com.codecool.bank.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BankcardValidationResultDto {

    private String result;

    public BankcardValidationResultDto() {
    }

    public BankcardValidationResultDto(boolean result) {
        this.result = result ? "VALID" : "INVALID";
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
