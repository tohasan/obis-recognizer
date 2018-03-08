package org.tohasan.dlms.obisrecognizer.reader;

import com.sun.media.sound.InvalidFormatException;
import org.tohasan.dlms.obisrecognizer.entities.ObisDefinition;

import java.util.List;

/**
 * author LehaSan
 * date 25.02.2018
 */
public interface ObisDefinitionReader {
    List<ObisDefinition> read() throws InvalidFormatException;
}
