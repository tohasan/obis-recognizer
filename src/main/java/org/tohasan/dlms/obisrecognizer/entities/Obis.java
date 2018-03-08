package org.tohasan.dlms.obisrecognizer.entities;

import com.sun.media.sound.InvalidFormatException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Obis {
    private final static String OBIS_SEPARATOR_REGEXP = "\\.";
    private final static int VALID_GROUP_COUNT = 6;

    private String obis;

    private List<Integer> groupIds;

    public Obis(String obis) throws InvalidFormatException {
        this.obis = obis;

        String[] obisParts = obis.split(OBIS_SEPARATOR_REGEXP);

        if (obisParts.length != VALID_GROUP_COUNT) {
            throw new InvalidFormatException();
        }

        this.groupIds = Arrays.stream(obisParts)
            .map(Integer::parseInt)
            .collect(Collectors.toList());
    }

    public List<Integer> getGroupIds() {
        return groupIds;
    }

    public String getFullName() {
        return obis;
    }

    @Override
    public String toString() {
        return "Obis{" +
            "obis='" + obis + '\'' +
            '}';
    }
}
