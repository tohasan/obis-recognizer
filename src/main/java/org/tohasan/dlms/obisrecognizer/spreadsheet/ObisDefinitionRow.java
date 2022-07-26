package org.tohasan.dlms.obisrecognizer.spreadsheet;

import org.apache.poi.ss.usermodel.Row;
import org.tohasan.dlms.obisrecognizer.entities.ObisGroupDescription;
import org.tohasan.dlms.obisrecognizer.entities.ObisVariable;
import org.tohasan.dlms.obisrecognizer.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ObisDefinitionRow {
    private final static String ROW_TYPE_COMMON = "com";
    private final static String ROW_TYPE_DEFINITION = "def";
    private final static String ROW_TYPE_DESCRIPTION = "N\\d+[A-F]";

    private final static int COL_INDEX_ROW_TYPE = 0;
    private final static int COL_INDEX_DEFINITION = 1;

    private final static String DESCRIPTION_PREFIX = "$";
    private final static String DESCRIPTION_NUMBER_REGEXP = "(\\d+)";
    private final static String DESCRIPTION_GROUP_NAME_REGEXP = "([B-F])";

    public final static int GROUP_COUNT_TO_READ = 5;

    public final static int COL_INDEX_GROUP_ID_GROUP_B = 2;
    public final static int COL_INDEX_GROUP_ID_GROUP_F = COL_INDEX_GROUP_ID_GROUP_B + GROUP_COUNT_TO_READ - 1;

    private final static Map<String, Integer> GROUP_NAME_TO_COL_INDEX;

    static {
        GROUP_NAME_TO_COL_INDEX = new HashMap<>();
        GROUP_NAME_TO_COL_INDEX.put("B", COL_INDEX_GROUP_ID_GROUP_B);
        GROUP_NAME_TO_COL_INDEX.put("C", COL_INDEX_GROUP_ID_GROUP_B + 1);
        GROUP_NAME_TO_COL_INDEX.put("D", COL_INDEX_GROUP_ID_GROUP_B + 2);
        GROUP_NAME_TO_COL_INDEX.put("E", COL_INDEX_GROUP_ID_GROUP_B + 3);
        GROUP_NAME_TO_COL_INDEX.put("F", COL_INDEX_GROUP_ID_GROUP_B + 4);
    }

    private Row row;

    ObisDefinitionRow(Row row) {
        this.row = row;
    }

    /**
     * Determine whether a row is effectively completely empty
     * i.e. all cells either contain an empty string or nothing.
     *
     * @return True if row does not have any non-empty cell otherwise false.
     */
    public boolean isEmpty() {
        if (row == null) {
            return true;
        }

        for (int i = 0; i <= row.getLastCellNum(); i++) {
            if (!this.getCellValue(i).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private String getRawType() {
        return getCellValue(COL_INDEX_ROW_TYPE);
    }

    public ObisDefinitionRowType getType() {
        String rowType = getRawType();
        if (rowType.equals(ROW_TYPE_COMMON)) {
            return ObisDefinitionRowType.COMMON;
        } else if (rowType.matches(ROW_TYPE_DESCRIPTION)) {
            return ObisDefinitionRowType.DESCRIPTION;
        } else if (rowType.equals(ROW_TYPE_DEFINITION)) {
            return ObisDefinitionRowType.DEFINITION;
        } else {
            return ObisDefinitionRowType.DEFAULT;
        }
    }

    private ObisDefinitionCell getCell(int cellIndex) {
        return new ObisDefinitionCell(row.getCell(cellIndex));
    }

    public String getCellValue(int cellIndex) {
        return getCell(cellIndex).getValue();
    }

    public String getDescriptionNumber() {
        if (getType() != ObisDefinitionRowType.DESCRIPTION) {
            return "";
        }
        return DESCRIPTION_PREFIX + StringUtils.getSubstringByRegexp(getRawType(), DESCRIPTION_NUMBER_REGEXP);
    }

    private String getDescriptionGroup() {
        if (getType() != ObisDefinitionRowType.DESCRIPTION) {
            return "";
        }
        return StringUtils.getSubstringByRegexp(getRawType(), DESCRIPTION_GROUP_NAME_REGEXP);
    }

    public ObisVariable getDefinitionVariable() {
        if (getType() != ObisDefinitionRowType.DEFINITION) {
            return null;
        }

        return getCell(COL_INDEX_DEFINITION).getVariable();
    }

    public int getRowNum() {
        return row.getRowNum();
    }

    public ObisGroupDescription getDescription() {
        if (getType() != ObisDefinitionRowType.DESCRIPTION) {
            return null;
        }

        Integer colIndexDescriptionId = GROUP_NAME_TO_COL_INDEX.get(getDescriptionGroup());
        Integer colIndexDescriptionValue = colIndexDescriptionId + GROUP_COUNT_TO_READ;

        int descriptionId = Integer.parseInt(getCellValue(colIndexDescriptionId));
        String descriptionValue = getCellValue(colIndexDescriptionValue);
        return new ObisGroupDescription(descriptionId, descriptionValue);
    }
}
