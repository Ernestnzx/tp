package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.event.Event;
import seedu.address.model.item.ItemManagerWithFilteredList;
import seedu.address.model.person.Person;
import seedu.address.model.todo.Todo;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;
    Predicate<Event> PREDICATE_SHOW_ALL_EVENTS = unused -> true;
    Predicate<Todo> PREDICATE_SHOW_ALL_TODOS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Returns the user prefs' todo list file path.
     */
    Path getTodoListFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setTodoListFilePath(Path addressBookFilePath);

    /**
     * Returns the user prefs' event list file path.
     */
    Path getEventListFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setEventListFilePath(Path addressBookFilePath);

    /**
     * Returns the person manager and display list.
     */
    ItemManagerWithFilteredList<Person> getPersonManagerAndList();

    /**
     * Returns the todo manager and display list.
     */
    ItemManagerWithFilteredList<Todo> getTodoManagerAndList();

    /**
     * Returns the event manager and display list
     */
    ItemManagerWithFilteredList<Event> getEventManagerAndList();
}
