---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# TutorConnect Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* This project is based on the AddressBook-Level3 (AB3) project created by the [SE-EDU initiative](https://se-education.org).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* Tutors managing student information and course-related details
  * Needs to track student attendance and student issues
  * Needs to manage course related tasks
* Has a need to manage a significant number of contacts
* Prefers desktop apps over other types
* Can type fast
* Prefers typing to mouse interactions
* Is reasonably comfortable using CLI apps

**Value proposition**: manage contacts faster than a typical mouse/GUI driven app


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a ...                 | I want to ...                                  | so that I can ...                                   |
|----------|--------------------------|------------------------------------------------|-----------------------------------------------------|
| `* * *`  | first time user          | see a functional help page                     | understand the app's functionalities                |
| `*`      | first-time user          | get a guide to creating my profile             | know what to do and not feel overwhelmed            |
| `* * *`  | tutor                    | automatically load in data                     | easily reuse the application                        |
| `* * *`  | tutor                    | automatically save and export data             | easily reuse the application                        |
| `* * *`  | tutor                    | safely exit the program                        | avoid corrupting my files                           |
| `* * *`  | tutor                    | create an event                                | represent a tutorial                                |
| `* * *`  | tutor                    | delete an event                                | remove events I no longer need                      |
| `*`      | impatient tutor          | create recurring events                        | save time and ensure consistency                    |
| `*`      | impatient tutor          | mass import class timings (events)             | save time                                           |
| `* * *`  | tutor                    | add students to an event                       | assign students to class                            |
| `* * *`  | tutor                    | remove students from an event                  | unassign students to class                          |
| `* * *`  | tutor                    | log a student student as having attended event | check attendance                                    |
| `* * *`  | tutor                    | add a contact                                  | store the information of my student                 |
| `* *`    | tutor                    | add multiple contacts with same names          | handle students with duplicate names                |
| `* * *`  | tutor                    | delete a contact                               | remove students I no longer need to handle          |
| `* *`    | tutor with many classes  | label a contact with a class                   | remember student is in which class                  |
| `* *`    | tutor with many course   | label a contact with a course                  | remember student is in which course                 |
| `*`      | impatient tutor          | mass import students data                      | save time                                           |
| `* *`    | tutor                    | search for a specific contact by feature       | retrieve full information for a particular contact  |
| `* *`    | tutor                    | list all contacts                              |                                                     |
| `* *`    | tutor with many classes  | filter all students by class/course            | find students easily for various purposes           |
| `* *`    | tutor with many classes  | sort all students by class/course              | find students easily for various purposes           |
| `* *`    | tutor                    | label progress of students                     | identify struggling students                        |
| `* *`    | caring tutor             | create a todo                                  | represent a task for some contact                   |
| `* *`    | caring tutor             | add contact to todo                            | handle a situation for some contact(s)              |
| `* *`    | caring tutor             | remove contact from todo                       |                                                     |
| `* *`    | caring tutor             | mark todo as done                              | remember that I have handled the situation          |
| `* *`    | caring tutor             | mark todo as not done                          |                                                     |
| `*`      | head tutor               | distinguish students and tutors                | add tutors as contacts                              |
| `*`      | head tutor               | know when my tutors are unavailable            | schedule make-up classes and track tutor attendance |
| `*`      | head tutor               | apply labels to tutors                         | track tutor performance                             |
| `*`      | morally upright tutor    | tag students suspected of plagiarism           | later report them for further investigation         |
| `*`      | tutor                    | send messages to individuals or groups         | remind them of tasks                                |
| `*`      | tutor teaching many sems | archive old classes                            | retain useful data while focusing on improvements   |
| `*`      | tutor teaching many sems | archive or purge old contacts                  | avoid confusion between current and former students |
| `*`      | experienced user         | create custom commands/macros                  | optimize workflow                                   |
| `*`      | forgetful tutor          | view upcoming tasks in some priority           | prioritize on tasks with nearer deadlines           |
| `*`      | impatient tutor          | synchronize contact labels with events         | avoid manually tagging students                     |
| `*`      | careless tutor           | undo (multiple times)                          | revert to previous state in case of wrong command   |

### Use cases

(For all use cases below, the **System** is the `TutorConnect (TC)` and the **Actor** is the `user`, unless specified otherwise)

**Use case 1: Create a New Todo**

**MSS**

1. User provides input to create a new todo task.
2. TC validates the input.
3. TC creates the task and confirms the creation.

   Use case ends.

**Extensions**

* 2a. User provides an empty task name.

    * 2a1. TC displays an error message: "Task name cannot be empty."

      Use case ends.

* 2b. A task with the same name already exists.

    * 2b1. TC displays an error message: "Task <name> already exists."

      Use case ends.

**Use case 2: Delete a Todo**

**MSS**

1. User provides input to delete a todo task.
2. TC validates the input.
3. TC deletes the task and confirms the deletion.

   Use case ends.

**Extensions**

* 2a. The specified task does not exist.

    * 2a1. TC displays an error message: "Task <name> not found."

      Use case ends.

**Use case 3: Add Contact to Existing Task**

**MSS**

1. User searches for the contact's ID (UC:TBD)
2. User provides input to associate a contact with a task.
3. TC validates the input.
4. TC associates the task with the contact and confirms the update.

   Use case ends.

**Extensions**

* 3a. User provides an empty task name.

    * 3a1. TC displays an error message: "Task name cannot be empty."

      Use case ends.

* 3b. The specified task does not exist.

    * 3b1. TC displays an error message: "Task <name> not found."

      Use case ends.

* 3c. The specified contact does not exist.

    * 3c1. TC displays an error message: "Contact <contact_id> not found."

      Use case ends.

* 3d. The task is already assigned to the contact.

    * 3d1. TC displays an error message: "Task <name> already exists for <contact_id>."

      Use case ends.

**Use case 4: Remove Contact from Existing Task**

**MSS**

1. User finds all contacts associated with a task (UC:TBD)
2. User provides input to remove a contact from a task.
3. TC validates the input.
4. TC removes the association and confirms the update.

   Use case ends.

**Extensions**

* 3a. User provides an empty task name.

    * 3a1. TC displays an error message: "Task name cannot be empty."

      Use case ends.

* 3b. The specified task does not exist.

    * 3b1. TC displays an error message: "Task <name> not found."

      Use case ends.

* 3c. The specified contact does not exist.

    * 3c1. TC displays an error message: "Contact <contact_id> not found."

      Use case ends.

* 3d. The task is not assigned to the contact.

    * 3d1. TC displays an error message: "Task <name> is not assigned to <contact_id>."

      Use case ends.

**Use case 5: Mark a Todo as Done**

**MSS**

1. User provides input to mark a todo as done.
2. TC validates the input.
3. TC marks the task as done and confirms the update.

   Use case ends.

**Extensions**

* 2a. The specified task does not exist.

    * 2a1. TC displays an error message: "Task <name> not found."

      Use case ends.

**Use case 6: Unmark a Todo as Done**

**MSS**

1. User provides input to unmark a todo as done.
2. TC validates the input.
3. TC unmarks the task as done and confirms the update.

   Use case ends.

**Extensions**

* 2a. The specified task does not exist.

    * 2a1. TC displays an error message: "Task <name> not found."

      Use case ends.

**Use case 7: List Full Details of a Specific Todo**

**MSS**

1. User provides input to list the full details of a specific todo.
2. TC validates the input.
3. TC retrieves the full details of the todo and displays it to the user.

   Use case ends.

**Extensions**

* 2a. The specified task does not exist.

    * 2a1. TC displays an error message: "Task <name> not found."

      Use case ends.

**Use case 8: Search for Contacts by Some Identifiable Feature**

**MSS**
1. User enters a valid `filter` command with at least one column and value.
2. TC filters contacts based on the specified criteria.
3. TC displays the matching results along with the message: `"n results found."`

   Use case ends.

**Extensions**
* 1a. User enters a `filter` command with a duplicate column.
    * 1a1. TC displays the error message: `"Duplicate column: <col>."`

      Use case resumes at step 1.

* 1b. User enters a `filter` command with an unrecognized column.
    * 1b1. TC displays the error message: `"Unrecognized column: <unknown>."`

      Use case resumes at step 1.

* 1c. User enters a `filter` command with an invalid operator.
    * 1c1. TC displays the error message: `"Unrecognized operator: <unknown>."`

      Use case resumes at step 1.

* 1d. User enters a `filter` command where a column has no values specified.
    * 1d1. TC displays the error message: `"No values specified for column <col>."`

      Use case resumes at step 1.

* 1e. User enters a `filter` command without specifying any column.
    * 1e1. TC displays the error message: `"Specify at least one column and value."`

      Use case resumes at step 1.

* 1f. User enters a `filter` command with multiple operators in the same column.
    * 1f1. TC applies only the first operator and treats the next ones as values.

      Use case resumes at step 2.

**Use case 9: List all Contacts**

**MSS**
1. User enters the `list` command.
2. TC displays a list of all contacts.

   Use case ends.

**Extensions**
* 2a. No contacts exist in the system.
    * 2a1. TC displays: `"No contacts found. Add a contact to get started."`

      Use case ends.

**Use case 10: Help**

**MSS**
1. User enters the `help` command.
2. TC displays a general help page listing all features.

   Use case ends.

**Extensions**
* 1a. User enters `help <feature>` (e.g., `help todo`)
    * 1a1. TC displays detailed information about the requested feature.

      Use case ends.

* 1b. User enters `help <invalid feature>`
    * 1b1. TC displays the error message: `"Feature <invalid feature> not recognized."`

      Use case resumes at step 2.

**Use case 11: Exit the Program**

**MSS**
1. User enters the `exit` command.
2. TC terminates.

   Use case ends.

**Extensions**
* 1a. User enters an alias for `exit` (e.g., `bye`, `quit`, `kill`).
    * 1a1. TC recognizes the alias and terminates.

      Use case ends.
  
**Use case 12: List all events**
  
**MSS**
1. User requests to see all events in the list.
2. TC displays the message `“Here are the events in the event list: 
                                           <Event 1>
                                           <Event 2>
                                           …… “`.
   Use case ends.

**Use case 13: Retrieve full information of an event**

**MSS**
1. User requests to see full information of an event using a valid name.
2. TC displays full information of the event.
   
   Use case ends.

**Extensions**
* 1a. User inputs an empty event name.
    * 1a1. TC displays the error message: `"event name cannot be empty !!!"`.

      Use case resumes at step 1.

* 1b. User inputs a non-existent event name.
    * 1b1. TC displays the error message: `“Sorry!!! we could not find <event_name> in the event list”`.

      Use case resumes at step 1.

**Use case 14: Create an event**

**MSS**
1. User requests to create a new event using a valid name.
2. TC adds the event to the list and displays the message `"successfully created event  <event name>"`.

   Use case ends.

**Extensions**
* 1a. User inputs an empty event name.
    * 1a1. TC displays the error message: `"event name cannot be empty !!!"`.

      Use case resumes at step 1.

**Use case 15: Delete an event**

**MSS**
1. User retrieves full information of an event (UC13).
2. User requests to remove the event from the list using a valid name.
3. TC removes the event from the list and displays the message `"successfully removed event  <event name>"`.

   Use case ends.

**Extensions**
* 2a. User inputs an empty event name.
    * 2a1. TC displays the error message: `"event name cannot be empty !!!"`.

      Use case resumes at step 2.

* 2b. User inputs a non-existent event name.
    * 2b1. TC displays the error message: `“Sorry!!! we could not find <event_name> in the event list”`.

      Use case resumes at step 2.

**Use case 16: Log contact as having attended an event**

**MSS**
1. User retrieves full information of an event (UC13).
2. User searches for contacts' ids (UC:TBD).
3. User requests to mark those contacts as having attended the event.
4. TC marked contacts with as attending the found event and displays the message `“successfully marked <id_1>, <id_2>,... as having attended event <event name>”`.

   Use case ends.

**Extensions**
* 3a. User inputs an empty event name.
    * 3a1. TC displays the error message: `"event name cannot be empty !!!"`.

      Use case resumes at step 3.

* 3b. User inputs a non-existent event name.
    * 3b1. TC displays the error message: `“Sorry!!! we could not find <event_name> in the event list”`.

      Use case resumes at step 3.

* 3c. User inputs all empty contact ids.
    * 3c1. TC displays the error message: `“There must be at least 1 contact !!!”`.

      Use case resumes at step 3.

* 3d. User enters a non-existent contact id.
    * 3d1. TC displays the error message: `“Sorry, we could not find <id> in the contact list”`.
      
      Use case resumes at step 3.

**Use case 17: Add a contact (Supports duplicate names)**

**MSS**
1. User requests to add a contact
2. TC adds the contact to the contact list.
3. TC displays a confirmation message.

   Use case ends.

**Extensions**
* 1a. User inputs a duplicate ID.
    * 1a1. TC displays an error message.

      Use case resumes at step 1.

* 1b. User inputs an empty contact ID:
    * 1b1. TC displays an error message.

      Use case resumes at step 1.

* 1c. User inputs an empty contact name.
    * 1c1. TC displays an error message.
    
      Use case resumes at step 1.

* 1d. User inputs an empty contact number.
    * 1d1. TC displays an error message.
    
      Use case resumes at step 1.

* 1e. User inputs a contact name that does not contain alphabets:
    * 1e1. TC displays an error message.
  
      Use case resumes at step 1.

**Use case 18: Remove contact from contact list**

**MSS**
1. User requests to remove a contact.
2. TC removes the contact from the contact list.
3. TC displays a message for succesful operation.

   Use case ends.

**Extensions**
* 1a. User enters a ID that is not present in the contact list.
    * 1a1. TC displays an error message.

      Use case resumes at step 1.

**Use case 19: Tagging each student for grouping**

**MSS**
1. User requests to tag a contact.
2. TC updates the contact ID with the provided tags.
3. TC displays a message for successful operation.
    
   Use case ends.

**Extensions**
* 1a. User inputs an ID that is not found in the contact list.
    * 1a1. TC displays an error message.

      Use case resumes at step 1.

* 1b. User inputs an empty contact ID.
    * 1b1. TC displays an error message.

      Use case resumes at step 1.

* 1c. User inputs an empty tag.
    * 1c1. TC displays an error message.
    
      Use case resumes at step 1.

**Use case 20: Removing tags for a specific contact**

**MSS**
1. User requests to remove tags for a contact.
2. TC removes the given tags on the given contact ID.
3. TC displays a message upon a successful operation.

   Use case ends.
  
**Extensions**
* 1a. User inputs an ID that is not found in the contact list.
    * 1a1. TC displays an error message.

      Use case resumes at step 1.

* 1b. User inputs a tag that does not exist for the contact.
    * 1b1. TC displays an error message.

      Use case resumes at step 1.

* 1c. User inputs a empty tag:
    * 1c1. TC displays an error message.

      Use case resumes at step 1.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  Data should not be fully lost in the event of a crash or unexpected shutdown.
5.  Should be easily testable to ensure that new updates or features do not negatively impact existing functionality.
6.  Should function without requiring an installer.
7.  Should only use third-party libraries or services that are free, open-source, have permissive license terms, and do not require installation by the user.
8.  Should display optimally on screen resolutions 1920x1080 and higher with screen scales 100% and 125%, and remain functional on 1280x720 and higher with up to 150% screen scaling.
9.  The main application (JAR/ZIP file) should not exceed 100MB.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
