package com.easybank.enums;

public enum MovementType {

    TRANSFER("transfer"),
    WITHDRAW("withdraw"),
    DEPOSIT("deposit");

    private String value;

    MovementType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static MovementType getMovementByValue(String value) {
        MovementType status;
        if ("transfer".equals(value)) {
            status = MovementType.TRANSFER;
        } else if ("withdraw".equals(value)) {
            status = MovementType.WITHDRAW;
        } else if ("deposit".equals(value)) {
            status = MovementType.DEPOSIT;
        } else {
            status = null;
        }
        return status;
    }

}
