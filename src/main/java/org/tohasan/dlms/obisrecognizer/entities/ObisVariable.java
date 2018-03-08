package org.tohasan.dlms.obisrecognizer.entities;

public class ObisVariable {
    private String name;
    private String value;

    public ObisVariable(String name, String value) {
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
