package org.tohasan.dlms.obisrecognizer.cli;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohasan.dlms.obisrecognizer.entities.FinderOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Works with command line arguments and prepares app options.
 * <p>
 * author tohasan
 * date 09.03.2018
 */
public class CliHelper {
    private final static Logger LOGGER = LoggerFactory.getLogger(CliHelper.class);

    private final static String APP_PROPERTIES_FILENAME = "app.properties";

    private final static String OBIS_LIST_SEPARATOR_REGEXP = ",\\s*";

    private final static String PROPERTY_APP_NAME = "app.name";
    private final static String PROPERTY_APP_VERSION = "app.version";
    private final static String PROPERTY_DEFINITIONS_FILE = "io.input.object-definitions";
    private final static String PROPERTY_OBIS_FILE = "io.input.obis-file";
    private final static String PROPERTY_OUTPUT_FILE = "io.output.obis-descriptions-file";

    private final static String OPT_VERSION = "version";
    private final static String OPT_VERBOSE = "verbose";
    private final static String OPT_HELP = "help";
    private final static String OPT_OBIS_LIST = "obis-list";
    private final static String OPT_OBIS_FILE = "obis-file";
    private final static String OPT_DEFINITIONS_FILE = "definitions-file";
    private final static String OPT_OUTPUT_FILE = "output-file";

    private Options options = null;
    private Properties properties = null;

    /**
     * Method prints specified message to console.
     *
     * @param message Message to print.
     */
    private void print(String message) {
        System.out.println(message);
    }

    /**
     * Method prints help for usage of the utility.
     */
    public void printHelp() {
        // Automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(getProperties().getProperty(PROPERTY_APP_NAME), getOptions());
    }

    /**
     * Method prints version of the utility.
     */
    public void printVersion() {
        print(String.format(
            "%s %s\n",
            getProperties().getProperty(PROPERTY_APP_NAME),
            getProperties().getProperty(PROPERTY_APP_VERSION)
        ));
    }

    /**
     * Method parses command line args using command line options.
     *
     * @param args Arguments user specifies.
     * @return Prepared command line that contains information about used options.
     * @throws ParseException Throws if arguments can not be parsed.
     */
    public FinderOptions parseCommandLineArgs(String[] args) throws ParseException {
        // Create the command line parser
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(getOptions(), args);

        // TODO: Изменять уровень логирования (verbose): https://gist.github.com/nkcoder/cd74919fd80594c56e09b448a2d1ba31

        FinderOptions finderOptions = new FinderOptions();

        finderOptions.setShouldPrintVersion(commandLine.hasOption(OPT_VERSION));
        finderOptions.setShouldPrintHelp(commandLine.hasOption(OPT_HELP));
        finderOptions.setVerbose(commandLine.hasOption(OPT_VERBOSE));

        finderOptions.setDefinitionFile(commandLine.getOptionValue(OPT_DEFINITIONS_FILE, getDefaultDefinitionsFile()));
        finderOptions.setObisFile(commandLine.getOptionValue(OPT_OBIS_FILE, getDefaultObisFile()));
        finderOptions.setOutputFile(commandLine.getOptionValue(OPT_OUTPUT_FILE, getDefaultOutputFile()));

        String obisListAsString = commandLine.getOptionValue(OPT_OBIS_LIST, "").trim();
        List<String> obisList = new ArrayList<>();
        if (!obisListAsString.isEmpty()) {
            obisList = Arrays.asList(obisListAsString.split(OBIS_LIST_SEPARATOR_REGEXP));
        }
        finderOptions.setObisList(obisList);

        return finderOptions;
    }

    /**
     * Method loads a properties file from class path.
     *
     * @return Loaded properties if everything is success, otherwise empty properties.
     */
    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();

            try (InputStream inputStream = CliHelper.class.getClassLoader().getResourceAsStream(APP_PROPERTIES_FILENAME)) {
                properties.load(inputStream);
            } catch (IOException ex) {
                LOGGER.error("Error was occurred during reading properties: {}", ex);
            }
        }
        return properties;
    }

    /**
     * @return Default path to definitions file (file that contains information about obis descriptions).
     */
    private String getDefaultDefinitionsFile() {
        return getProperties().getProperty(PROPERTY_DEFINITIONS_FILE);
    }

    /**
     * @return Default path to file contains OBISes to recognize.
     */
    private String getDefaultObisFile() {
        return getProperties().getProperty(PROPERTY_OBIS_FILE);
    }

    /**
     * @return Default path to output file contains recognized OBIS descriptions.
     */
    private String getDefaultOutputFile() {
        return getProperties().getProperty(PROPERTY_OUTPUT_FILE);
    }

    /**
     * Method creates command line options which utility uses to specify launching parameters.
     *
     * @return Command line options.
     */
    private Options getOptions() {
        if (options == null) {
            Option optVersion = Option.builder("v")
                .longOpt(OPT_VERSION)
                .desc("prints (v)ersion of the utility. Has no arguments.\nFor example, -v")
                .numberOfArgs(0)
                .build();

            Option optVerbose = Option.builder("ve")
                .longOpt(OPT_VERBOSE)
                .desc("prints (ve)rbose logs during utility execution.\nHas no arguments.\nFor example, -ve")
                .numberOfArgs(0)
                .build();

            Option optHelp = Option.builder("h")
                .longOpt(OPT_HELP)
                .desc(
                    "prints current help contains description of all available options of utility.\n" +
                        "Has no arguments.\n" +
                        "For example, -h"
                )
                .build();

            Option optObisList = Option.builder("l")
                .longOpt(OPT_OBIS_LIST)
                .desc(
                    "uses comma-separated obis (l)ist to recognize.\n" +
                        "It has higher priority than obis file (see, option -of).\n" +
                        "If specified, this list used instead of obis file.\n" +
                        "Default: empty list.\n" +
                        "For example, -l 0.6.96.20.30.255,1.2.18.0.7.151"
                )
                .optionalArg(false)
                .numberOfArgs(1)
                .build();

            // TODO: Read default filename from app.properties
            Option optObisFile = Option.builder("i")
                .longOpt(OPT_OBIS_FILE)
                .desc(String.format(
                    "sets obis file as (i)nput obis list to recognize.\n" +
                        "Obis file is txt-file contains one obis per line:\n" +
                        "   0.6.96.20.30.255\n" +
                        "   1.2.18.0.7.151\n" +
                        "It has lower priority than obis list (see, option -ol).\n" +
                        "If obis list is specified, this options is ignored.\n" +
                        "Default: %s (set in app.properties).\n" +
                        "For example, -i /path/to/obis-file.txt",
                    getDefaultObisFile()
                ))
                .optionalArg(false)
                .numberOfArgs(1)
                .build();

            Option optDefinitionFile = Option.builder("d")
                .longOpt(OPT_DEFINITIONS_FILE)
                .desc(String.format(
                    "specifies xls file contains (d)efinitions of objects.\n" +
                        "This file is used to get obis descriptions.\n" +
                        "Default: %s (set in app.properties).\n" +
                        "For example, -d /path/to/object-definitions.xls",
                    getDefaultDefinitionsFile()
                ))
                .optionalArg(false)
                .numberOfArgs(1)
                .build();

            Option optOutputFile = Option.builder("o")
                .longOpt(OPT_OUTPUT_FILE)
                .desc(String.format(
                    "sets (o)utput file with recognized obis descriptions.\n" +
                        "Default: %s (set in app.properties).\n" +
                        "For example, -o /path/to/obis-descriptions.txt",
                    getDefaultOutputFile()
                ))
                .optionalArg(false)
                .numberOfArgs(1)
                .build();

            options = new Options();
            options.addOption(optVersion);
            options.addOption(optVerbose);
            options.addOption(optHelp);
            options.addOption(optObisList);
            options.addOption(optObisFile);
            options.addOption(optDefinitionFile);
            options.addOption(optOutputFile);
        }
        return options;
    }
}
