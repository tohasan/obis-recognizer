package org.tohasan.dlms.obisrecognizer.entities;

/**
 * author LehaSan
 * date 07.03.2018
 */
public class CosemGroupDescription {
    private int groupId;
    private String description;

    public CosemGroupDescription(int groupId, String description) {
        this.groupId = groupId;
        this.description = description;
    }

    int getGroupId() {
        return groupId;
    }

    public String getDescription() {
        return description;
    }
}
