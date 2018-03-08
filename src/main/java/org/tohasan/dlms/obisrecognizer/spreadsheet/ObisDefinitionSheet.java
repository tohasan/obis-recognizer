package org.tohasan.dlms.obisrecognizer.spreadsheet;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ObisDefinitionSheet implements Iterable<ObisDefinitionRow> {
    public final static int TYPE_ABSTRACT = 4;
    public final static int TYPE_ELECTRICITY = 5;

    private final static Map<Integer, String> MAP_SHEET_INDEX_TO_GROUP_ID;

    static {
        MAP_SHEET_INDEX_TO_GROUP_ID = new HashMap<>();
        MAP_SHEET_INDEX_TO_GROUP_ID.put(TYPE_ABSTRACT, "0");
        MAP_SHEET_INDEX_TO_GROUP_ID.put(TYPE_ELECTRICITY, "1");
    }

    private Sheet sheet;

    public ObisDefinitionSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public String getName() {
        return sheet.getSheetName();
    }

    public String getGroupId() {
        int sheetIndex = sheet.getWorkbook().getSheetIndex(sheet);
        return MAP_SHEET_INDEX_TO_GROUP_ID.get(sheetIndex);
    }

    @Override
    public Iterator<ObisDefinitionRow> iterator() {
        return new ObisDefinitionRowIterator(sheet.rowIterator());
    }
}
