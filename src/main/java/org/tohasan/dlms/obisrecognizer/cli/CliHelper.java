package org.tohasan.dlms.obisrecognizer.cli;

import org.apache.commons.cli.*;
import org.tohasan.dlms.obisrecognizer.entities.FinderOptions;

/**
 * Works with command line arguments and prepares app options.
 * <p>
 * author tohasan
 * date 09.03.2018
 */
public class CliHelper {
    private static final String UTILITY_NAME = "obis-recognizer";

    private static final String OPT_VERSION = "version";
    private static final String OPT_VERBOSE = "verbose";
    private static final String OPT_HELP = "help";
    private static final String OPT_OBIS_LIST = "obis-list";
    private static final String OPT_OBIS_FILE = "obis-file";
    private static final String OPT_DEFINITIONS_FILE = "definitions-file";
    private static final String OPT_OUTPUT_FILE = "output-file";

    private Options options = null;

    public void print(String message) {
        System.out.println(message);
    }

    /**
     * Method prints help for usage of utility.
     */
    public void printHelp() {
        // Automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(UTILITY_NAME, getOptions());
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

        // TODO: Добавить вывод версии: https://stackoverflow.com/questions/3697449/retrieve-version-from-maven-pom-xml-in-code
        // TODO: Изменять уровень логирования (verbose): https://gist.github.com/nkcoder/cd74919fd80594c56e09b448a2d1ba31
        // TODO: Прочитать properties: https://www.mkyong.com/java/java-properties-file-examples/

        FinderOptions finderOptions = new FinderOptions();
        finderOptions.setDefinitionFile(commandLine.getOptionValue(OPT_DEFINITIONS_FILE));
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
                    .desc(
                            "sets obis file as (i)nput obis list to recognize.\n" +
                                    "Obis file is txt-file contains one obis per line:\n" +
                                    "   0.6.96.20.30.255\n" +
                                    "   1.2.18.0.7.151\n" +
                                    "It has lower priority than obis list (see, option -ol).\n" +
                                    "If obis list is specified, this options is ignored.\n" +
                                    "Default: ./obis-list.txt (set in app.properties).\n" +
                                    "For example, -i /path/to/obis-file.txt"
                    )
                    .optionalArg(false)
                    .numberOfArgs(1)
                    .build();

            Option optDefinitionFile = Option.builder("d")
                    .longOpt(OPT_DEFINITIONS_FILE)
                    .desc(
                            "specifies xls file contains (d)efinitions of objects.\n" +
                                    "This file is used to get obis descriptions.\n" +
                                    "Default: ./object-definitions.xls (set in app.properties).\n" +
                                    "For example, -d /path/to/object-definitions.xls"
                    )
                    .optionalArg(false)
                    .numberOfArgs(1)
                    .build();

            Option optOutputFile = Option.builder("o")
                    .longOpt(OPT_OUTPUT_FILE)
                    .desc(
                            "sets (o)utput file with recognized obis descriptions.\n" +
                                    "Default: ./obis-descriptions.txt (set in app.properties).\n" +
                                    "For example, -o /path/to/obis-descriptions.txt"
                    )
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
