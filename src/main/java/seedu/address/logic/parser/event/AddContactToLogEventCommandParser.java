package seedu.address.logic.parser.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_MISSING_CONTACT_INDEX;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.update.AddContactToLogEventCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.PrefixAlias;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddContactToLogEventCommandParser object.
 */
public class AddContactToLogEventCommandParser implements Parser<AddContactToLogEventCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the
     * AddContactToLogEventCommandParser and returns a AddContactToLogEventCommand object for
     * execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public AddContactToLogEventCommand parse(String args) throws ParseException {
        requireNonNull(args);
        PrefixAlias contactPrefix = EventCliSyntax.PREFIX_ALIAS_EVENT_LINKED_CONTACT;
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, contactPrefix.getAll());

        if (argMultimap.getValue(contactPrefix).isEmpty() || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddContactToLogEventCommand.MESSAGE_USAGE));
        }
        // Parse index of event to edit
        Index index = ParserUtil.parseIndex(argMultimap.getPreamble());

        // Parse contact indices, duplicates are handled in parseIndices
        String contactRaw = argMultimap.getValue(contactPrefix)
                .orElseThrow(() -> new ParseException(MESSAGE_MISSING_CONTACT_INDEX));
        List<Index> contactIndices = ParserUtil.parseIndices(contactRaw);
        if (contactIndices.isEmpty()) {
            throw new ParseException(MESSAGE_MISSING_CONTACT_INDEX);
        }
        return new AddContactToLogEventCommand(index, contactIndices);
    }
}
