package org.tohasan.dlms.obisrecognizer.entities;

import com.sun.media.sound.InvalidFormatException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * author LehaSan
 * date 22.02.2018
 */
public class ObisDefinition {
    private final static String GROUP_INDENT_ON_PRINT = "    ";
    private final static String DESCRIPTION_SEPARATOR = ", ";
    private final static int VALID_GROUP_COUNT = 6;

    private List<ObisRangeGroup> groups;

    public ObisDefinition(List<ObisRangeGroup> groups) throws InvalidFormatException {
        if (groups.size() != VALID_GROUP_COUNT) {
            throw new InvalidFormatException();
        }
        this.groups = groups;
    }

    public List<ObisRangeGroup> getGroups() {
        return groups;
    }

    public boolean contains(Obis obis) {
        for (int i = 0; i < groups.size(); i++) {
            ObisRangeGroup group = groups.get(i);
            if (!group.contains(obis.getGroupIds().get(i))) {
                return false;
            }
        }
        return true;
    }

    public String getDescription(Obis obis) {
        List<String> descriptions = new ArrayList<>();
        for (int i = 0; i < groups.size(); i++) {
            ObisRangeGroup group = groups.get(i);
            String description = group.getDescription(obis.getGroupIds().get(i));
            if (!description.isEmpty()) {
                descriptions.add(description);
            }
        }
        return String.join(DESCRIPTION_SEPARATOR, descriptions);
    }

    @Override
    public String toString() {
        String strGroups = groups.stream()
                .map(obisRangeGroup -> GROUP_INDENT_ON_PRINT + obisRangeGroup.toString())
                .collect(Collectors.joining("\n"));

        return "ObisDefinition{\n" +
                strGroups + "\n" +
                "}";
    }
}
