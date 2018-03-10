package org.tohasan.dlms.obisrecognizer.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * author LehaSan
 * date 09.03.2018
 */
public class FinderOptions {
    private List<String> obisList = new ArrayList<>();
    private boolean shouldPrintVersion;
    private boolean shouldPrintHelp;
    private boolean isVerbose;
    private String obisFile;
    private String definitionFile;
    private String outputFile;

    public boolean isShouldPrintVersion() {
        return shouldPrintVersion;
    }

    public void setShouldPrintVersion(boolean shouldPrintVersion) {
        this.shouldPrintVersion = shouldPrintVersion;
    }

    public boolean isShouldPrintHelp() {
        return shouldPrintHelp;
    }

    public void setShouldPrintHelp(boolean shouldPrintHelp) {
        this.shouldPrintHelp = shouldPrintHelp;
    }

    public boolean isVerbose() {
        return isVerbose;
    }

    public void setVerbose(boolean verbose) {
        isVerbose = verbose;
    }

    public List<String> getObisList() {
        return obisList;
    }

    public void setObisList(List<String> obisList) {
        this.obisList = obisList;
    }

    public String getObisFile() {
        return obisFile;
    }

    public void setObisFile(String obisFile) {
        this.obisFile = obisFile;
    }

    public String getDefinitionFile() {
        return definitionFile;
    }

    public void setDefinitionFile(String definitionFile) {
        this.definitionFile = definitionFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }
}
