package com.futurelin.r2aot.api.item;

public enum KeyType {
    SHIFT("Shift"),
    ALT("Alt"),
    CONTROL("Control");

    private final String value;
    private KeyType(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
