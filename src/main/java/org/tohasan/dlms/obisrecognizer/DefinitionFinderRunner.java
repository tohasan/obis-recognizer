package org.tohasan.dlms.obisrecognizer;

import com.sun.media.sound.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohasan.dlms.obisrecognizer.entities.CosemDefinition;
import org.tohasan.dlms.obisrecognizer.entities.Obis;
import org.tohasan.dlms.obisrecognizer.finder.CosemDefinitionFinder;
import org.tohasan.dlms.obisrecognizer.reader.CosemReader;
import org.tohasan.dlms.obisrecognizer.reader.impl.XlsCosemReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DefinitionFinderRunner {
    private final static Logger LOGGER = LoggerFactory.getLogger(DefinitionFinderRunner.class);
    private final static String DEFAULT_OBIS_FILE = "obis-list.txt";
    private final static String DEFAULT_OUTPUT_FILE = "obis-descriptions.txt";
    private final static String DEFAULT_COSEM_OBJECT_DEFINITION_FILENAME = "object-definitions.xls";

    public static void main(String[] args) throws InvalidFormatException {
        Obis obis = new Obis("0.6.96.20.30.255");
        LOGGER.info("obis: {}", obis);

        try (InputStream inputStream = new FileInputStream(DEFAULT_COSEM_OBJECT_DEFINITION_FILENAME)) {
            CosemReader reader = new XlsCosemReader(inputStream);
            List<CosemDefinition> definitions = reader.read();
            CosemDefinitionFinder finder = new CosemDefinitionFinder(definitions);
            List<String> descriptions = finder.findDescriptionsByObis(obis);

            for (String description : descriptions) {
                LOGGER.debug("obis {} description: {}", obis.getFullName(), description);
            }

            if (descriptions.isEmpty()) {
                LOGGER.info("obis {} description: there are no definitions for specified obis", obis.getFullName());
            }
        } catch (IOException e) {
            LOGGER.error("Error: {}\\nStack trace: {}", e.getMessage(), e.getStackTrace());
        }
    }
}
