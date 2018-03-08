package org.tohasan.dlms.obisrecognizer.reader;

import com.sun.media.sound.InvalidFormatException;
import org.tohasan.dlms.obisrecognizer.entities.CosemDefinition;

import java.util.List;

/**
 * author LehaSan
 * date 25.02.2018
 */
public interface CosemReader {
    List<CosemDefinition> read() throws InvalidFormatException;
}
