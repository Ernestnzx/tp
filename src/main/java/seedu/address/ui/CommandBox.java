package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;

    private Consumer<ListPanelViewType> viewSwitchHandler;
    private final List<String> previousCommands;
    private int currentCommandIndex;

    @FXML
    private TextField commandTextField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(CommandExecutor commandExecutor) {
        super(FXML);
        this.commandExecutor = commandExecutor;

        previousCommands = new ArrayList<>();
        currentCommandIndex = -1;

        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
        commandTextField.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
    }

    /**
     * Handles key press events for command history navigation.
     */
    private void handleKeyPress(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.UP) {
            displayPreviousCommand();
            keyEvent.consume();
        } else if (keyEvent.getCode() == KeyCode.DOWN) {
            displayNextCommand();
            keyEvent.consume();
        }
    }

    /**
     * Displays the previous command if it exists.
     */
    private void displayPreviousCommand() {
        // Check if there is a previous command
        if (currentCommandIndex - 1 < 0) {
            return;
        }
        // Shift back and display
        currentCommandIndex--;
        String previousCommand = previousCommands.get(currentCommandIndex);
        commandTextField.setText(previousCommand);
        commandTextField.positionCaret(previousCommand.length());
    }

    /**
     * Displays the next command if it exists.
     */
    private void displayNextCommand() {
        // Check if there is a next command
        if (currentCommandIndex + 1 >= previousCommands.size()) {
            return;
        }
        // Shift forward and display
        currentCommandIndex++;
        String nextCommand = previousCommands.get(currentCommandIndex);
        commandTextField.setText(nextCommand);
        commandTextField.positionCaret(nextCommand.length());
    }

    /**
     * Sets a handler that will be called when a view switch command is detected.
     *
     * @param handler callback to handle view switching
     */
    public void setViewSwitchHandler(Consumer<ListPanelViewType> handler) {
        this.viewSwitchHandler = handler;
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        String commandText = commandTextField.getText().trim();
        if (commandText.isEmpty()) {
            return;
        }
        try {
            // Add the current command and set index to end
            previousCommands.add(commandText);
            currentCommandIndex = previousCommands.size() - 1;

            // Execute the command and clear the command box
            commandExecutor.execute(commandText);
            commandTextField.setText("");
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }
}
