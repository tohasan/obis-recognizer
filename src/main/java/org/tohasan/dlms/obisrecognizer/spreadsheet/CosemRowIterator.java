package org.tohasan.dlms.obisrecognizer.spreadsheet;

import org.apache.poi.ss.usermodel.Row;

import java.util.Iterator;

public class CosemRowIterator implements Iterator<CosemRow> {
    private Iterator<Row> iterator;

    CosemRowIterator(Iterator<Row> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public CosemRow next() {
        return new CosemRow(iterator.next());
    }
}
