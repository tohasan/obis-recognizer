package org.tohasan.dlms.obisrecognizer.reader.impl;

import com.sun.media.sound.InvalidFormatException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohasan.dlms.obisrecognizer.entities.ObisRangeGroup;
import org.tohasan.dlms.obisrecognizer.spreadsheet.ObisDefinitionSheet;
import org.tohasan.dlms.obisrecognizer.entities.ObisDefinition;
import org.tohasan.dlms.obisrecognizer.entities.ObisGroupDescription;
import org.tohasan.dlms.obisrecognizer.entities.ObisVariable;
import org.tohasan.dlms.obisrecognizer.reader.ObisDefinitionReader;
import org.tohasan.dlms.obisrecognizer.spreadsheet.ObisDefinitionRow;
import org.tohasan.dlms.obisrecognizer.spreadsheet.ObisDefinitionRowType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XlsObisDefinitionReader implements ObisDefinitionReader {
    private final static Logger LOGGER = LoggerFactory.getLogger(XlsObisDefinitionReader.class);

    private final static int[] SHEET_INDEXES = {ObisDefinitionSheet.TYPE_ABSTRACT, ObisDefinitionSheet.TYPE_ELECTRICITY};

    private HSSFWorkbook workBook;

    public XlsObisDefinitionReader(InputStream inputStream) throws IOException {
        this.workBook = new HSSFWorkbook(inputStream);
    }

    @Override
    public List<ObisDefinition> read() throws InvalidFormatException {
        List<ObisDefinition> definitions = new ArrayList<>();

        for (int sheetIndex : SHEET_INDEXES) {
            Map<String, List<ObisGroupDescription>> descriptionDictionary = new HashMap<>();
            Map<String, ObisVariable> variables = new HashMap<>();

            ObisDefinitionSheet sheet = new ObisDefinitionSheet(this.workBook.getSheetAt(sheetIndex));
            ObisRangeGroup groupA = new ObisRangeGroup(sheet.getGroupId(), sheet.getName());

            LOGGER.debug("start to read sheet '{}'", sheet.getName());

            for (ObisDefinitionRow row : sheet) {
                LOGGER.debug("row number: {}", row.getRowNum());

                if (row.isEmpty()) {
                    LOGGER.debug("sheet '{}' is read completely", sheet.getName());
                    break;
                }

                ObisDefinitionRowType rowType = row.getType();

                switch (rowType) {
                    case DESCRIPTION:
                        List<ObisGroupDescription> descriptions = descriptionDictionary.computeIfAbsent(
                            row.getDescriptionNumber(),
                            k -> new ArrayList<>()
                        );
                        descriptions.add(row.getDescription());
                        break;
                    case DEFINITION:
                        ObisVariable variable = row.getDefinitionVariable();
                        variables.put(variable.getName(), variable);
                        break;
                    case DEFAULT:
                        List<ObisRangeGroup> groups = new ArrayList<>();
                        groups.add(groupA);

                        for (int colIndex = ObisDefinitionRow.COL_INDEX_GROUP_ID_GROUP_B; colIndex <= ObisDefinitionRow.COL_INDEX_GROUP_ID_GROUP_F; colIndex++) {
                            String idRange = row.getCellValue(colIndex);
                            String description = row.getCellValue(colIndex + ObisDefinitionRow.GROUP_COUNT_TO_READ);

                            if (variables.containsKey(idRange)) {
                                idRange = variables.get(idRange).getValue();
                            }

                            groups.add(new ObisRangeGroup(idRange, description, descriptionDictionary));
                        }

                        ObisDefinition definition = new ObisDefinition(groups);
                        definitions.add(definition);
                        LOGGER.debug("definition: {}", definition);
                }
            }
        }

        return definitions;
    }
}
