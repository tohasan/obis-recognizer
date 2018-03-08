package org.tohasan.dlms.obisrecognizer.finder;

import org.tohasan.dlms.obisrecognizer.entities.ObisDefinition;
import org.tohasan.dlms.obisrecognizer.entities.Obis;

import java.util.ArrayList;
import java.util.List;

public class ObisDefinitionFinder {
    private List<ObisDefinition> definitions;

    public ObisDefinitionFinder(List<ObisDefinition> definitions) {
        this.definitions = definitions;
    }

    public List<String> findDescriptionsByObis(Obis obis) {
        List<String> foundDescriptions = new ArrayList<>();

        for (ObisDefinition definition : this.definitions) {
            if (definition.contains(obis)) {
                foundDescriptions.add(definition.getDescription(obis));
            }
        }

        return foundDescriptions;
    }
}
