package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.event.Event;
import seedu.address.model.person.Person;
import seedu.address.model.todo.Todo;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_DISPLAY_SPECIFIC_PERSON_INFO = "Displaying full information of student: %s!";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
               .append("; ID: ")
               .append(person.getId())
               .append("; Phone: ")
               .append(person.getPhone())
               .append("; Email: ")
               .append(person.getEmail())
               .append("; Course: ")
               .append(person.getCourse())
               .append("; Group: ")
               .append(person.getGroup())
               .append("; Tags: ");
        person.getTags()
              .forEach(builder::append);
        return builder.toString();
    }

    /**
     * Formats the {@code todo} for display to the user.
     */
    public static String format(Todo todo) {
        String personsFormatted;

        if (todo.getPersons().isEmpty()) {
            personsFormatted = "None";
        } else {
            personsFormatted = IntStream.range(0, todo.getPersons().size())
                    .mapToObj(i -> (i + 1) + ". " + getSimplifiedFormat(todo.getPersons().get(i)))
                    .collect(Collectors.joining("\n"));
        }

        return todo.getName()
                + "; Deadline: " + todo.getDeadline()
                + "; Location: " + todo.getLocation()
                + "; Persons:" + (todo.getPersons().isEmpty() ? " " : "\n") + personsFormatted;
    }

    /**
     * Formats the {@code todo} for display to the user.
     */
    public static String format(Event event) {
        String personListFormatted;
        if (event.getPersons().isEmpty()) {
            personListFormatted = "None";
        } else {
            personListFormatted = "\n" + IntStream.range(0, event.getPersons().size())
                    .mapToObj(i -> String.format("%d. %s", i + 1, getSimplifiedFormat(event.getPersons().get(i))))
                    .collect(Collectors.joining("\n"));
        }
        return event.getName()
                + "; Start Time: " + event.getStartTime()
                + "; End Time: " + event.getEndTime()
                + "; Location: " + event.getLocation()
                + "; Persons: " + personListFormatted;
    }

    /**
     * Returns a simplified format of a {@code person} for display to the user.
     */
    public static String getSimplifiedFormat(Person person) {
        return person.getName()
                + "; ID: " + person.getId()
                + "; Course: " + person.getCourse()
                + "; Group: " + person.getGroup();
    }
}
