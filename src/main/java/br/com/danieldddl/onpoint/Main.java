package br.com.danieldddl.onpoint;

import br.com.danieldddl.onpoint.config.MarkServiceModule;
import br.com.danieldddl.onpoint.model.Mark;
import br.com.danieldddl.onpoint.services.api.MarkService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.HelpFormatter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {

    private static final String OPTION_POINT = "point";
    private static final String OPTION_SINCE = "since";
    private static final String OPTION_BETWEEN = "between";
    private static final String OPTION_HELP = "help";
    private static final String OPTION_MARK = "mark";
    private static final String OPTION_ARRIVAL = "arrival";
    private static final String OPTION_LEAVING = "leaving";

    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new MarkServiceModule());
        MarkService markService = injector.getInstance(MarkService.class);

        Option points = Option.builder("p")
                .longOpt(OPTION_POINT)
                .desc("Get the last n points")
                .required(false)
                .numberOfArgs(1)
                .optionalArg(false)
                .build();

        Option since = Option.builder("s")
                .longOpt(OPTION_SINCE)
                .desc("Get all points since informed date")
                .required(false)
                .numberOfArgs(1)
                .optionalArg(false)
                .build();

        Option between = Option.builder("b")
                .longOpt(OPTION_BETWEEN)
                .desc("Get all points between two informed dates")
                .required(false)
                .numberOfArgs(2)
                .optionalArg(false)
                .build();

        Option help = Option.builder("h")
                .longOpt(OPTION_HELP)
                .desc("Print all information about commands you can use")
                .required(false)
                .numberOfArgs(0)
                .build();

        Option mark = Option.builder("m")
                .longOpt(OPTION_MARK)
                .desc("Create a simple mark without a Type")
                .required(false)
                .numberOfArgs(1)
                .optionalArg(true)
                .build();

        Option arrival = Option.builder("a")
                .longOpt(OPTION_ARRIVAL)
                .desc("Create a arrival mark with the specified date, " +
                        "if existing. If not, use current date")
                .required(false)
                .numberOfArgs(1)
                .optionalArg(true)
                .build();

        Option leaving = Option.builder("l")
                .longOpt(OPTION_LEAVING)
                .desc("Create a leaving mark with the specified date, " +
                        "if existing. If not, use current date")
                .required(false)
                .numberOfArgs(1)
                .optionalArg(true)
                .build();

        Options options = new Options();
        options.addOption(points);
        options.addOption(since);
        options.addOption(between);
        options.addOption(mark);
        options.addOption(arrival);
        options.addOption(leaving);
        options.addOption(help);

        try {

            DefaultParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, args);

            //we just want one of the options, and nothing else
            if (commandLine.getOptions().length != 1) {
                throw new ParseException("Problem trying to parse given commands.");
            }

            if (commandLine.hasOption(OPTION_POINT)) {

                int numberOfArgs = Integer.parseInt(commandLine.getOptionValues(OPTION_POINT)[0]);

                List<Mark> fetchedMarks = markService.list(numberOfArgs);
                printAllMarks(fetchedMarks);

            } else if (commandLine.hasOption(OPTION_SINCE)) {

                String rawDate = commandLine.getOptionValues(OPTION_SINCE)[0];

                LocalDateTime sinceDate = extractDateFromString(rawDate);
                List<Mark> sinceMarks = markService.listSince(sinceDate);
                printAllMarks(sinceMarks);

            } else if (commandLine.hasOption(OPTION_BETWEEN)) {

                String firstRawDate = commandLine.getOptionValues(OPTION_BETWEEN)[0];
                String secondRawDate = commandLine.getOptionValues(OPTION_BETWEEN)[1];

                LocalDateTime firstDate = extractDateFromString(firstRawDate);
                LocalDateTime secondDate = extractDateFromString(secondRawDate);

                List<Mark> betweenMarks = markService.listBetween(firstDate, secondDate);
                printAllMarks(betweenMarks);

            } else if (commandLine.hasOption(OPTION_MARK)) {

                if (commandLine.getArgList().isEmpty()) {
                    markService.simpleMark();
                } else {
                    String rawDate = commandLine.getOptionValues(OPTION_MARK)[0];

                    LocalDateTime markDate = extractDateFromString(rawDate);
                    markService.simpleMark(markDate);
                }

            } else if (commandLine.hasOption(OPTION_ARRIVAL)) {

                if (commandLine.getArgList().isEmpty()) {
                    markService.markArrival();
                } else {
                    String rawDate = commandLine.getOptionValues(OPTION_ARRIVAL)[0];

                    LocalDateTime markDate = extractDateFromString(rawDate);
                    markService.markArrival(markDate);
                }
            } else if (commandLine.hasOption(OPTION_LEAVING)) {

                if (commandLine.getArgList().isEmpty()) {
                    markService.markLeaving();
                } else {
                    String rawDate = commandLine.getOptionValues(OPTION_LEAVING)[0];

                    LocalDateTime markDate = extractDateFromString(rawDate);
                    markService.markLeaving(markDate);
                }

            } else if (commandLine.hasOption(OPTION_HELP)) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("Onpoint", options);
            }



        } catch (ParseException e) {
            printerr(e.getMessage());
        }
    }


    private static LocalDateTime extractDateFromString (String raw) throws ParseException {

        try {

            Date date = new SimpleDateFormat("yyyyMMdd").parse(raw);
            return date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

        } catch (java.text.ParseException e) {
            throw new ParseException("Error while converting informed date. Please, use format yyyyMMdd");
        }

    }

    private static String extractStringFromDate (LocalDateTime date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return formatter.format(date);
    }

    private static void printAllMarks (List<Mark> marks) {

        for (Mark mark : marks)  {

            printf("mark %d %n", mark.getId());
            printf("mark of type %s %n",
                    (mark.getType() == null ? "undefined" : mark.getType().getName()));
            printf("when marked %s%n", extractStringFromDate(mark.getWhen()));
            printf("marked date %s%n%n", extractStringFromDate(mark.getMarkedDate()));

        }
    }

    /**
     * Utils for printing formatted text
     * */
    private static void printf(String format, Object... objects) {
        System.out.printf(format, objects);
    }

    /**
     * Utils for printing error
     * */
    private static void printerr (String message) {
        System.err.print(message);
    }

}
