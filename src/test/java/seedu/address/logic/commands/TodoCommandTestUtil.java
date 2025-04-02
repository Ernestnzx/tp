package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.todo.TodoCliSyntax.PREFIX_TODO_DEADLINE_LONG;
import static seedu.address.logic.parser.todo.TodoCliSyntax.PREFIX_TODO_LOCATION_LONG;
import static seedu.address.logic.parser.todo.TodoCliSyntax.PREFIX_TODO_NAME_LONG;
import static seedu.address.logic.parser.todo.TodoCliSyntax.PREFIX_TODO_TAG_LONG;

import java.util.List;

import seedu.address.commons.core.Operator;
import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.item.predicate.NamePredicate;
import seedu.address.model.todo.Todo;

/**
 * Contains helper methods for testing todo commands.
 */
public class TodoCommandTestUtil {

    public static final String VALID_NAME_REPORT = "Report";
    public static final String VALID_NAME_GRADING = "Grading submissions";
    public static final String VALID_DEADLINE_REPORT = "25-12-31 23:59";
    public static final String VALID_DEADLINE_GRADING = "25-11-30 23:59";
    public static final String VALID_LOCATION_REPORT = "Canvas";
    public static final String VALID_LOCATION_GRADING = "Prof office";
    public static final String VALID_TAG_REPORT = "important";

    public static final String NAME_DESC_REPORT = " " + PREFIX_TODO_NAME_LONG + VALID_NAME_REPORT;
    public static final String NAME_DESC_GRADING = " " + PREFIX_TODO_NAME_LONG + VALID_NAME_GRADING;
    public static final String DEADLINE_DESC_REPORT =
            " " + PREFIX_TODO_DEADLINE_LONG + VALID_DEADLINE_REPORT;
    public static final String DEADLINE_DESC_GRADING =
            " " + PREFIX_TODO_DEADLINE_LONG + VALID_DEADLINE_GRADING;
    public static final String LOCATION_DESC_REPORT =
            " " + PREFIX_TODO_LOCATION_LONG + VALID_LOCATION_REPORT;
    public static final String LOCATION_DESC_GRADING =
            " " + PREFIX_TODO_LOCATION_LONG + VALID_LOCATION_GRADING;
    public static final String TAG_DESC_REPORT = " " + PREFIX_TODO_TAG_LONG + VALID_TAG_REPORT;

    public static final String INVALID_TODO_NAME_DESC = " " + PREFIX_TODO_NAME_LONG + "-stuff";
    public static final String INVALID_TODO_DEADLINE_DESC_NOT_DATETIME =
            " " + PREFIX_TODO_DEADLINE_LONG + "aaaa";
    public static final String INVALID_TODO_DEADLINE_DESC_INCORRECT_FORMAT =
            " " + PREFIX_TODO_DEADLINE_LONG + "23:59 25-12-31";
    // empty
    public static final String INVALID_TODO_LOCATION_DESC = " " + PREFIX_TODO_LOCATION_LONG + "";
    // * not allowed
    public static final String INVALID_TAG_TODO = " " + PREFIX_TODO_TAG_LONG + "too*";



    /**
     * Updates {@code model}'s filtered list to show only the todo at the given {@code targetIndex} in the
     * {@code model}'s todo list.
     */
    public static void showTodoAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased()
                < model.getTodoManagerAndList().getFilteredItemsList().size());

        Todo todo = model.getTodoManagerAndList().getFilteredItemsList()
                .get(targetIndex.getZeroBased());
        final String[] splitName = todo.getName().value.split("\\s+");
        model.getTodoManagerAndList()
                .updateFilteredItemsList(new NamePredicate(Operator.AND, List.of(splitName[0])));

        assertEquals(1, model.getTodoManagerAndList().getFilteredItemsList().size());
    }
}
