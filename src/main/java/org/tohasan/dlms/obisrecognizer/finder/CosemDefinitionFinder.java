package org.tohasan.dlms.obisrecognizer.finder;

import org.tohasan.dlms.obisrecognizer.entities.CosemDefinition;
import org.tohasan.dlms.obisrecognizer.entities.Obis;

import java.util.ArrayList;
import java.util.List;

public class CosemDefinitionFinder {
    private List<CosemDefinition> definitions;

    public CosemDefinitionFinder(List<CosemDefinition> definitions) {
        this.definitions = definitions;
    }

    public List<String> findDescriptionsByObis(Obis obis) {
        List<String> foundDescriptions = new ArrayList<>();

        for (CosemDefinition definition : this.definitions) {
            if (definition.contains(obis)) {
                foundDescriptions.add(definition.getDescription(obis));
            }
        }

        return foundDescriptions;
    }
}
