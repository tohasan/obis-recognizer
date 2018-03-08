package org.tohasan.dlms.obisrecognizer.spreadsheet;

import org.apache.poi.ss.usermodel.Row;

import java.util.Iterator;

public class ObisDefinitionRowIterator implements Iterator<ObisDefinitionRow> {
    private Iterator<Row> iterator;

    ObisDefinitionRowIterator(Iterator<Row> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public ObisDefinitionRow next() {
        return new ObisDefinitionRow(iterator.next());
    }
}
