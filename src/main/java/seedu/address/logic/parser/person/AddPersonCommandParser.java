package seedu.address.logic.parser.person;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.person.PersonCliSyntax.PREFIX_PERSON_COURSE;
import static seedu.address.logic.parser.person.PersonCliSyntax.PREFIX_PERSON_EMAIL;
import static seedu.address.logic.parser.person.PersonCliSyntax.PREFIX_PERSON_GROUP;
import static seedu.address.logic.parser.person.PersonCliSyntax.PREFIX_PERSON_ID;
import static seedu.address.logic.parser.person.PersonCliSyntax.PREFIX_PERSON_NAME;
import static seedu.address.logic.parser.person.PersonCliSyntax.PREFIX_PERSON_PHONE;
import static seedu.address.logic.parser.person.PersonCliSyntax.PREFIX_PERSON_TAG;

import java.util.Set;

import seedu.address.logic.commands.person.AddPersonCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Course;
import seedu.address.model.person.Email;
import seedu.address.model.person.Group;
import seedu.address.model.person.Id;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddPersonCommandParser implements Parser<AddPersonCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddPersonCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PERSON_ID, PREFIX_PERSON_NAME,
                PREFIX_PERSON_PHONE, PREFIX_PERSON_EMAIL, PREFIX_PERSON_TAG, PREFIX_PERSON_COURSE, PREFIX_PERSON_GROUP);

        if (!argMultimap.arePrefixesPresent(PREFIX_PERSON_ID, PREFIX_PERSON_NAME, PREFIX_PERSON_PHONE,
                PREFIX_PERSON_EMAIL, PREFIX_PERSON_COURSE, PREFIX_PERSON_GROUP)
            || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPersonCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PERSON_ID, PREFIX_PERSON_NAME, PREFIX_PERSON_PHONE,
                PREFIX_PERSON_EMAIL, PREFIX_PERSON_COURSE, PREFIX_PERSON_GROUP);
        Id id = PersonParserUtil.parseId(argMultimap.getValue(PREFIX_PERSON_ID).get());
        Name name = PersonParserUtil.parseName(argMultimap.getValue(PREFIX_PERSON_NAME).get());
        Phone phone = PersonParserUtil.parsePhone(argMultimap.getValue(PREFIX_PERSON_PHONE).get());
        Email email = PersonParserUtil.parseEmail(argMultimap.getValue(PREFIX_PERSON_EMAIL).get());
        Course course = PersonParserUtil.parseModule(argMultimap.getValue(PREFIX_PERSON_COURSE).get());
        Group group = PersonParserUtil.parseGroup(argMultimap.getValue(PREFIX_PERSON_GROUP).get());
        Set<Tag> tagList = PersonParserUtil.parseTags(argMultimap.getAllValues(PREFIX_PERSON_TAG));

        Person person = new Person(id, name, phone, email, course, group, tagList);
        return new AddPersonCommand(person);
    }
}
