package org.tohasan.dlms.obisrecognizer.spreadsheet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.tohasan.dlms.obisrecognizer.entities.CosemVariable;

class CosemCell {
    private final static String VARIABLE_KEY_VALUE_SEPARATOR = "\\s*=\\s*";
    private final static int VARIABLE_KEY_INDEX = 0;
    private final static int VARIABLE_VALUE_INDEX = 1;

    private Cell cell;

    CosemCell(Cell cell) {
        this.cell = cell;
    }

    private boolean isEmpty(Cell cell) {
        return cell == null || cell.getCellTypeEnum() == CellType.BLANK;
    }

    String getValue() {
        if (this.isEmpty(cell)) {
            return "";
        }

        switch (cell.getCellTypeEnum()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return Integer.toString((int) cell.getNumericCellValue());
            default:
                return "";
        }
    }

    CosemVariable getVariable() {
        String definition = this.getValue();
        String[] variableKeyValue = definition.split(VARIABLE_KEY_VALUE_SEPARATOR);
        String variableKey = variableKeyValue[VARIABLE_KEY_INDEX];
        String variableValue = variableKeyValue[VARIABLE_VALUE_INDEX];
        return new CosemVariable(variableKey, variableValue);
    }
}
