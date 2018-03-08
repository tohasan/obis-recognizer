package org.tohasan.dlms.obisrecognizer.reader.impl;

import com.sun.media.sound.InvalidFormatException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohasan.dlms.obisrecognizer.entities.CosemRangeGroup;
import org.tohasan.dlms.obisrecognizer.spreadsheet.CosemSheet;
import org.tohasan.dlms.obisrecognizer.entities.CosemDefinition;
import org.tohasan.dlms.obisrecognizer.entities.CosemGroupDescription;
import org.tohasan.dlms.obisrecognizer.entities.CosemVariable;
import org.tohasan.dlms.obisrecognizer.reader.CosemReader;
import org.tohasan.dlms.obisrecognizer.spreadsheet.CosemRow;
import org.tohasan.dlms.obisrecognizer.spreadsheet.CosemRowType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XlsCosemReader implements CosemReader {
    private final static Logger LOGGER = LoggerFactory.getLogger(XlsCosemReader.class);

    private final static int[] SHEET_INDEXES = {CosemSheet.TYPE_ABSTRACT, CosemSheet.TYPE_ELECTRICITY};

    private HSSFWorkbook workBook;

    public XlsCosemReader(InputStream inputStream) throws IOException {
        this.workBook = new HSSFWorkbook(inputStream);
    }

    @Override
    public List<CosemDefinition> read() throws InvalidFormatException {
        List<CosemDefinition> definitions = new ArrayList<>();

        for (int sheetIndex : SHEET_INDEXES) {
            Map<String, List<CosemGroupDescription>> descriptionDictionary = new HashMap<>();
            Map<String, CosemVariable> variables = new HashMap<>();

            CosemSheet sheet = new CosemSheet(this.workBook.getSheetAt(sheetIndex));
            CosemRangeGroup groupA = new CosemRangeGroup(sheet.getGroupId(), sheet.getName());

            LOGGER.debug("start to read sheet '{}'", sheet.getName());

            for (CosemRow row : sheet) {
                LOGGER.debug("row number: {}", row.getRowNum());

                if (row.isEmpty()) {
                    LOGGER.debug("sheet '{}' is read completely", sheet.getName());
                    break;
                }

                CosemRowType rowType = row.getType();

                switch (rowType) {
                    case DESCRIPTION:
                        List<CosemGroupDescription> descriptions = descriptionDictionary.computeIfAbsent(
                            row.getDescriptionNumber(),
                            k -> new ArrayList<>()
                        );
                        descriptions.add(row.getDescription());
                        break;
                    case DEFINITION:
                        CosemVariable variable = row.getDefinitionVariable();
                        variables.put(variable.getName(), variable);
                        break;
                    case DEFAULT:
                        List<CosemRangeGroup> groups = new ArrayList<>();
                        groups.add(groupA);

                        for (int colIndex = CosemRow.COL_INDEX_GROUP_ID_GROUP_B; colIndex <= CosemRow.COL_INDEX_GROUP_ID_GROUP_F; colIndex++) {
                            String idRange = row.getCellValue(colIndex);
                            String description = row.getCellValue(colIndex + CosemRow.GROUP_COUNT_TO_READ);

                            if (variables.containsKey(idRange)) {
                                idRange = variables.get(idRange).getValue();
                            }

                            groups.add(new CosemRangeGroup(idRange, description, descriptionDictionary));
                        }

                        CosemDefinition definition = new CosemDefinition(groups);
                        definitions.add(definition);
                        LOGGER.debug("definition: {}", definition);
                }
            }
        }

        return definitions;
    }
}
