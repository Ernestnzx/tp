package seedu.address.logic.commands.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.EVENT_COMMAND_WORD;
import static seedu.address.logic.parser.event.EventCliSyntax.PREFIX_LINKED_PERSON_INDEX;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.abstractcommand.EditCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Event;
import seedu.address.model.person.Person;

/**
 * Remove some associated people from an event class
 */
public class RemovePersonFromEventCommand extends EditCommand<Event> {
    public static final String COMMAND_WORD = "unlink";

    public static final String MESSAGE_USAGE = EVENT_COMMAND_WORD + " " + COMMAND_WORD
            + ": Remove the association between a event and some contacts.\n"
            + "Parameters: INDEX "
            + PREFIX_LINKED_PERSON_INDEX + " [PERSON_INDEX]...\n"
            + "Example: " + EVENT_COMMAND_WORD + " " + COMMAND_WORD + " 1 "
            + PREFIX_LINKED_PERSON_INDEX + " 1 3 4";

    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX =
            "The person index provided is invalid: %1$s";
    public static final String MESSAGE_INVALID_EVENT_DISPLAYED_INDEX =
            "The event index provided is invalid";
    public static final String MESSAGE_REMOVE_PERSON_SUCCESS = "Removed persons from event: %1$s";
    public static final String MESSAGE_NOT_REMOVED = "At least one person must be provided.";
    public static final String MESSAGE_DUPLICATE_EVENT = "This event already exists";

    private final List<Index> personIndices;

    /**
     * Creates an RemovePersonFromEventCommand to remove the persons at the specified {@code
     * personIndices} from the list of persons in the event at the specified {@code index}.
     */
    public RemovePersonFromEventCommand(Index index, List<Index> personIndices) {
        super(index, Model::getEventManagerAndList);
        requireNonNull(personIndices);
        this.personIndices = personIndices;
    }

    @Override
    public Event createEditedItem(Model model, Event eventToEdit) throws CommandException {
        for (Index index : personIndices) {
            if (index.getZeroBased() >= eventToEdit.getPersons().size()) {
                // System.out.println();
                throw new CommandException(String.format(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX,
                        index.getOneBased()));
            }
        }
        List<Person> newPersons = new ArrayList<>(eventToEdit.getPersons());
        personIndices.stream()
                .map(Index::getZeroBased)
                .sorted(Comparator.reverseOrder())
                .forEach(index -> newPersons.remove((int) index));
        return new Event(
            eventToEdit.getName(),
            eventToEdit.getStartTime(),
            eventToEdit.getEndTime(),
            eventToEdit.getLocation(),
            List.copyOf(newPersons)
        );
    }

    @Override
    public String getInvalidIndexMessage() {
        return MESSAGE_INVALID_EVENT_DISPLAYED_INDEX;
    }

    @Override
    public String getDuplicateMessage() {
        return MESSAGE_DUPLICATE_EVENT;
    }

    @Override
    public String getSuccessMessage(Event editedItem) {
        return String.format(MESSAGE_REMOVE_PERSON_SUCCESS, Messages.format(editedItem));
    }
}
