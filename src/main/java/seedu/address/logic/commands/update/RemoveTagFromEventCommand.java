package seedu.address.logic.commands.update;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.EVENT_COMMAND_WORD;
import static seedu.address.logic.parser.event.EventCliSyntax.PREFIX_EVENT_TAG_LONG;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Event;
import seedu.address.model.item.commons.Tag;

/**
 * Remove a tag from a specified event
 */
public class RemoveTagFromEventCommand extends EditEventCommand {
    public static final String COMMAND_WORD = "untag";
    public static final String MESSAGE_USAGE = EVENT_COMMAND_WORD + " " + COMMAND_WORD
            + ": Removes a tag from a specified event.\n"
            + "Parameters: INDEX "
            + PREFIX_EVENT_TAG_LONG + "TAG(S)\n"
            + "INDEX must be a positive integer.\n"
            + "Example: " + EVENT_COMMAND_WORD + " " + COMMAND_WORD + " 1 "
            + PREFIX_EVENT_TAG_LONG + "important weekly";
    public static final String MESSAGE_REMOVE_TAG_SUCCESS = "Removed tag from event: %1$s";
    public static final String MESSAGE_TAG_NOT_FOUND = "The tag is not assigned to this event";

    private final Set<Tag> newTags;

    /**
     * Creates an RemoveTagFromEventCommand to remove a tag from the specified {@code Event}
     */
    public RemoveTagFromEventCommand(Index index, Set<Tag> tags) {
        super(index, new EditEventDescriptor());
        requireNonNull(tags);
        this.newTags = tags;
    }

    @Override
    public Event createEditedItem(Model model, Event eventToEdit) throws CommandException {
        requireNonNull(eventToEdit);
        // Every tag that is intended to be removed must already be in the event
        Set<Tag> existingTags = eventToEdit.getTags();
        for (Tag tag : newTags) {
            if (!existingTags.contains(tag)) {
                throw new CommandException(String.format(MESSAGE_TAG_NOT_FOUND));
            }
        }

        // Merge tags and update
        Set<Tag> combinedTags = new HashSet<>(existingTags);
        combinedTags.removeAll(newTags);
        editEventDescriptor.setTags(combinedTags);

        return super.createEditedItem(model, eventToEdit);
    }

    @Override
    public String getSuccessMessage(Event editedEvent) {
        return String.format(MESSAGE_REMOVE_TAG_SUCCESS, Messages.format(editedEvent));
    }
}
