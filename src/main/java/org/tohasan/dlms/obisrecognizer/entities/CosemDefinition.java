package org.tohasan.dlms.obisrecognizer.entities;

import com.sun.media.sound.InvalidFormatException;

import java.util.ArrayList;
import java.util.List;

/**
 * author LehaSan
 * date 22.02.2018
 */
public class CosemDefinition {
    private final static String DESCRIPTION_SEPARATOR = ", ";
    private final static int VALID_GROUP_COUNT = 6;

    private List<CosemRangeGroup> groups;

    public CosemDefinition(List<CosemRangeGroup> groups) throws InvalidFormatException {
        if (groups.size() != VALID_GROUP_COUNT) {
            throw new InvalidFormatException();
        }
        this.groups = groups;
    }

    public List<CosemRangeGroup> getGroups() {
        return groups;
    }

    public boolean contains(Obis obis) {
        for (int i = 0; i < groups.size(); i++) {
            CosemRangeGroup group = groups.get(i);
            if (!group.contains(obis.getGroupIds().get(i))) {
                return false;
            }
        }
        return true;
    }

    public String getDescription(Obis obis) {
        List<String> descriptions = new ArrayList<>();
        for (int i = 0; i < groups.size(); i++) {
            CosemRangeGroup group = groups.get(i);
            String description = group.getDescription(obis.getGroupIds().get(i));
            if (!description.isEmpty()) {
                descriptions.add(description);
            }
        }
        return String.join(DESCRIPTION_SEPARATOR, descriptions);
    }

    @Override
    public String toString() {
        return "CosemDefinition{" +
            "groups=" + groups +
            '}';
    }
}
