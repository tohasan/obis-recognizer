package org.tohasan.dlms.obisrecognizer.entities;

public class CosemVariable {
    private String name;
    private String value;

    public CosemVariable(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
