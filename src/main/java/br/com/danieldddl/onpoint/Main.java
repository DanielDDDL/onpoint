package br.com.danieldddl.onpoint;

import br.com.danieldddl.onpoint.config.MarkServiceModule;
import br.com.danieldddl.onpoint.model.Mark;
import br.com.danieldddl.onpoint.services.MarkService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import jdk.nashorn.internal.runtime.ParserException;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    private static final Scanner ler = new Scanner(System.in);

    public static void main(String[] args) {

        for (int i = 0; i < 5; i++) {

            args = ler.nextLine().split(" ");

            Injector injector = Guice.createInjector(new MarkServiceModule());
            MarkService markService = injector.getInstance(MarkService.class);

            Option points = Option.builder("p")
                    .longOpt("point")
                    .desc("Get the last n points")
                    .required(false)
                    .numberOfArgs(1)
                    .optionalArg(false)
                    .build();

            Option since = Option.builder("s")
                    .longOpt("since")
                    .desc("Get all points since informed date")
                    .required(false)
                    .numberOfArgs(1)
                    .optionalArg(false)
                    .build();

            Option between = Option.builder("b")
                    .longOpt("between")
                    .desc("Get all points between two informed dates")
                    .required(false)
                    .numberOfArgs(2)
                    .optionalArg(false)
                    .build();

            Option help = Option.builder("h")
                    .longOpt("help")
                    .desc("Print all information about commands you can use")
                    .required(false)
                    .numberOfArgs(0)
                    .build();

            Option mark = Option.builder("m")
                    .longOpt("mark")
                    .desc("Create a simple mark without a MarkType")
                    .required(false)
                    .numberOfArgs(1)
                    .optionalArg(true)
                    .build();

            Option arrival = Option.builder("a")
                    .longOpt("arrival")
                    .desc("Create a arrival mark with the specified date, " +
                            "if existing. If not, use current date")
                    .required(false)
                    .numberOfArgs(1)
                    .optionalArg(true)
                    .build();

            Option leaving = Option.builder("l")
                    .longOpt("leaving")
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

                if (commandLine.hasOption("point")) {

                    int numberOfArgs = Integer.parseInt(commandLine.getOptionValues("point")[0]);

                    List<Mark> fetchedMarks = markService.list(numberOfArgs);
                    printAllMarks(fetchedMarks);

                } else if (commandLine.hasOption("since")) {

                    String rawDate = commandLine.getOptionValues("since")[0];

                    LocalDateTime sinceDate = extractDateFromString(rawDate);
                    List<Mark> sinceMarks = markService.listSince(sinceDate);
                    printAllMarks(sinceMarks);

                } else if (commandLine.hasOption("between")) {

                    String firstRawDate = commandLine.getOptionValues("between")[0];
                    String secondRawDate = commandLine.getOptionValues("between")[1];

                    LocalDateTime firstDate = extractDateFromString(firstRawDate);
                    LocalDateTime secondDate = extractDateFromString(secondRawDate);

                    List<Mark> betweenMarks = markService.listBetween(firstDate, secondDate);
                    printAllMarks(betweenMarks);

                } else if (commandLine.hasOption("mark")) {

                    if (commandLine.getArgList().isEmpty()) {
                        markService.simpleMark();
                    } else {
                        String rawDate = commandLine.getOptionValues("mark")[0];

                        LocalDateTime markDate = extractDateFromString(rawDate);
                        markService.simpleMark(markDate);
                    }

                } else if (commandLine.hasOption("arrival")) {

                    if (commandLine.getArgList().isEmpty()) {
                        markService.markArrival();
                    } else {
                        String rawDate = commandLine.getOptionValues("arrival")[0];

                        LocalDateTime markDate = extractDateFromString(rawDate);
                        markService.markArrival(markDate);
                    }
                } else if (commandLine.hasOption("leaving")) {

                    if (commandLine.getArgList().isEmpty()) {
                        markService.markLeaving();
                    } else {
                        String rawDate = commandLine.getOptionValues("leaving")[0];

                        LocalDateTime markDate = extractDateFromString(rawDate);
                        markService.markLeaving(markDate);
                    }

                } else if (commandLine.hasOption("help")) {
                    HelpFormatter formatter = new HelpFormatter();
                    formatter.printHelp("Onpoint", options);
                }



            } catch (ParseException e) {
                System.err.println(e.getMessage());
            }
        }

    }

    private static LocalDateTime extractDateFromString (String raw) {

        try {

            Date date = new SimpleDateFormat("yyyyMMdd").parse(raw);
            return date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

        } catch (java.text.ParseException e) {
            throw new ParserException("Error while converting informed date. Please, use format yyyyMMdd");
        }

    }

    private static String extractStringFromDate (LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = date.format(formatter);

        return formattedDateTime;
    }

    private static void printAllMarks (List<Mark> marks) {

        for (Mark mark : marks)  {

            System.out.printf("mark %d %n", mark.getId());
            System.out.printf("mark of type %s %n",
                                (mark.getMarkType() == null ? "undefined" : mark.getMarkType().getName()));
            System.out.printf("when marked %s%n", extractStringFromDate(mark.getWhen()));
            System.out.printf("marked date %s%n%n", extractStringFromDate(mark.getMarkedDate()));

        }
    }

}
