package org.tohasan.dlms.obisrecognizer.entities;

import org.tohasan.dlms.obisrecognizer.utils.CalculationUtils;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author LehaSan
 * date 22.02.2018
 */
public class ObisRangeGroup {
    private final static String RANGES_SEPARATOR = ",\\s*";
    private final static String RANGE_SEPARATOR = "-";
    private final static int RANGE_START_INDEX = 0;
    private final static int RANGE_FINISH_INDEX = 1;

    private final static String DESCRIPTION_EXPRESSION_REGEXP = "\\$\\(?([A-F][*+\\-/]?\\d*)\\)?";
    private final static String DESCRIPTION_VARIABLE_PREFIX_REGEXP = "\\$";
    private final static String DESCRIPTION_VARIABLE_REGEXP = "[A-F]";

    private String idRange;
    private String description = "";
    private List<Integer> ids = null;
    private Map<String, List<ObisGroupDescription>> descriptionDictionary = new HashMap<>();

    public ObisRangeGroup(String idRange, String description) {
        this.idRange = idRange;
        this.description = description;
    }

    public ObisRangeGroup(String idRange) {
        this.idRange = idRange;
    }

    public ObisRangeGroup(
        String idRange,
        String description,
        Map<String, List<ObisGroupDescription>> descriptionDictionary
    ) {
        this.idRange = idRange;
        this.description = description;
        this.descriptionDictionary = descriptionDictionary;
    }

    private List<Integer> getIds() {
        if (ids == null) {
            ids = getRangeValues(idRange);
        }
        return ids;
    }

    public String getDescription(int groupId) {
        if (!this.contains(groupId)) {
            return "";
        }

        if (this.isDescriptionFromDictionary()) {
            return this.getDescriptionFromDictionary(groupId);
        }

        Pattern pattern = Pattern.compile(DESCRIPTION_EXPRESSION_REGEXP);
        Matcher matcher = pattern.matcher(this.description);

        StringBuilder description = new StringBuilder(this.description);
        while (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                String expression = matcher.group(i)
                    .replaceAll(DESCRIPTION_VARIABLE_PREFIX_REGEXP, "")
                    .replaceAll(DESCRIPTION_VARIABLE_REGEXP, ((Integer) groupId).toString());
                String expressionResult = "";
                try {
                    expressionResult = CalculationUtils.evaluate(expression);
                } catch (ScriptException e) {
                    e.printStackTrace();
                }
                description = description.replace(matcher.start(i), matcher.end(i), expressionResult);
            }
            matcher = pattern.matcher(description.toString());
        }

        return description.toString();
    }

    public boolean contains(int groupId) {
        return getIds().contains(groupId);
    }

    private boolean isDescriptionFromDictionary() {
        return this.descriptionDictionary.containsKey(this.description);
    }

    private String getDescriptionFromDictionary(int groupId) {
        List<ObisGroupDescription> descriptions = this.descriptionDictionary.get(this.description);
        for (ObisGroupDescription groupDescription : descriptions) {
            if (groupDescription.getGroupId() == groupId) {
                return groupDescription.getDescription();
            }
        }
        return "";
    }

    private List<Integer> getRangeValues(String ranges) {
        List<Integer> values = new ArrayList<>();
        String[] valueRanges = ranges.split(RANGES_SEPARATOR);

        for (String range : valueRanges) {
            String[] rangeParts = range.split(RANGE_SEPARATOR);

            int rangeStart = Integer.parseInt(rangeParts[RANGE_START_INDEX]);
            int rangeEnd = rangeParts.length == 1 ? rangeStart : Integer.parseInt(rangeParts[RANGE_FINISH_INDEX]);

            for (int value = rangeStart; value <= rangeEnd; value++) {
                values.add(value);
            }
        }

        return values;
    }

    @Override
    public String toString() {
        return "ObisRangeGroup{" +
            "idRange={" + idRange + "}" +
            ", description='" + description + '\'' +
            '}';
    }
}
