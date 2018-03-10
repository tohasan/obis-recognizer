package org.tohasan.dlms.obisrecognizer;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.sun.media.sound.InvalidFormatException;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohasan.dlms.obisrecognizer.cli.CliHelper;
import org.tohasan.dlms.obisrecognizer.entities.FinderOptions;
import org.tohasan.dlms.obisrecognizer.entities.Obis;
import org.tohasan.dlms.obisrecognizer.entities.ObisDefinition;
import org.tohasan.dlms.obisrecognizer.finder.ObisDefinitionFinder;
import org.tohasan.dlms.obisrecognizer.reader.ObisDefinitionReader;
import org.tohasan.dlms.obisrecognizer.reader.impl.XlsObisDefinitionReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class DefinitionFinderRunner {
    private final static Logger LOGGER = LoggerFactory.getLogger(DefinitionFinderRunner.class);

    private final static String LOGGER_APPENDER_STDOUT = "STDOUT";
    private final static String FORMAT_OUTPUT_OBIS_DESCRIPTION = "%1$-24s %2$s";

    private long startTime;
    private CliHelper cliHelper = new CliHelper();

    public static void main(String[] args) {
        DefinitionFinderRunner appRunner = new DefinitionFinderRunner();
        appRunner.run(args);
    }

    private void run(String[] args) {
        LOGGER.debug("App started at {}", new Date());
        startTime = System.currentTimeMillis();

        // Try to parse command line arguments to app options
        FinderOptions options;
        try {
            options = cliHelper.parseCommandLineArgs(args);
        } catch (ParseException e) {
            printError(e);
            cliHelper.printHelp();
            printGoodbyeLines();
            return;
        }

        if (options.isShouldPrintVersion()) {
            cliHelper.printVersion();
        }

        if (options.isShouldPrintHelp()) {
            cliHelper.printHelp();
        }

        if (options.isVerbose()) {
            changeLogLevelToVerbose();
        }

        // Get OBISes to recognize

        List<String> obisList = options.getObisList();
        if (obisList.isEmpty()) {
            try (Stream<String> stream = Files.lines(Paths.get(options.getObisFile()))) {
                stream.forEach(obisList::add);
            } catch (IOException e) {
                printError(e);
                printGoodbyeLines();
                return;
            }
        }

        if (obisList.isEmpty()) {
            printInfo("There are no OBISes to recognize");
            printGoodbyeLines();
            return;
        }

        // Get OBIS definitions

        ObisDefinitionFinder finder;

        try (InputStream inputStream = new FileInputStream(options.getDefinitionFile())) {
            ObisDefinitionReader reader = new XlsObisDefinitionReader(inputStream);
            List<ObisDefinition> definitions = reader.read();
            finder = new ObisDefinitionFinder(definitions);
        } catch (IOException e) {
            printError(e);
            printGoodbyeLines();
            return;
        }

        // Recognize OBISes

        printInfo(String.format(FORMAT_OUTPUT_OBIS_DESCRIPTION, "OBIS", "Description"));
        for (String obisStr : obisList) {
            Obis obis;
            try {
                obis = new Obis(obisStr);
            } catch (InvalidFormatException e) {
                printInfo(String.format(FORMAT_OUTPUT_OBIS_DESCRIPTION, obisStr, "ERROR: has incorrect format"));
                continue;
            }

            List<String> descriptions = finder.findDescriptionsByObis(obis);

            for (String description : descriptions) {
                printInfo(String.format(FORMAT_OUTPUT_OBIS_DESCRIPTION, obisStr, description));
            }

            if (descriptions.isEmpty()) {
                printInfo(String.format(FORMAT_OUTPUT_OBIS_DESCRIPTION, obisStr, "WARNING: there is no description"));
            }
        }

        printGoodbyeLines();
    }

    private void changeLogLevelToVerbose() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<ch.qos.logback.classic.Logger> loggerList = loggerContext.getLoggerList();
        loggerList.forEach(logger -> {
            Appender<ILoggingEvent> appender = logger.getAppender(LOGGER_APPENDER_STDOUT);
            if (appender != null) {
                appender.clearAllFilters();
            }
            logger.setLevel(Level.DEBUG);
        });
    }

    private void printGoodbyeLines() {
        long endTime = System.currentTimeMillis();
        LOGGER.debug(String.format("Total time: %d ms", endTime - startTime));
        LOGGER.debug("App finished at {}", new Date());
    }

    private void printError(Exception e) {
        LOGGER.error("Error:", e);
    }

    private void printInfo(String message) {
        LOGGER.info(message);
    }
}
