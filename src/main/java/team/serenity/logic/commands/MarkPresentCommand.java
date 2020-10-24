package team.serenity.logic.commands;

import static java.util.Objects.requireNonNull;
import static team.serenity.logic.parser.CliSyntax.PREFIX_ID;
import static team.serenity.logic.parser.CliSyntax.PREFIX_NAME;

import javafx.collections.ObservableList;
import team.serenity.commons.core.index.Index;
import team.serenity.logic.commands.exceptions.CommandException;
import team.serenity.logic.parser.exceptions.ParseException;
import team.serenity.model.Model;
import team.serenity.model.group.Attendance;
import team.serenity.model.group.Lesson;
import team.serenity.model.group.Student;
import team.serenity.model.group.StudentInfo;
import team.serenity.model.util.UniqueList;

/**
 * Marks the attendance of a class or a student in the class.
 */
public class MarkPresentCommand extends Command {

    public static final String COMMAND_WORD = "markpresent";
    public static final String MESSAGE_SUCCESS = "%s: \nAttendance: present";
    public static final String MESSAGE_ALL_SUCCESS = "Attendance of all students marked present!";
    public static final String MESSAGE_STUDENT_NOT_FOUND =
            "%s is not found, please ensure the name & student id is correct";
    public static final String MESSAGE_NOT_IN_LESSON = "Currently not in any lesson. Please enter a lesson.";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX =
            "Index %d is not found, please ensure that it exists";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks a specific student or all students present from a lesson. \n"
            + "Parameters: "
            + "all or "
            + PREFIX_NAME + " STUDENT_NAME "
            + PREFIX_ID + " STUDENT_NUMBER " + "or INDEX\n"
            + "Example: " + COMMAND_WORD + " " + "all\n"
            + "or " + COMMAND_WORD + " "
            + PREFIX_NAME + " Aaron Tan "
            + PREFIX_ID + " e0123456\n"
            + "or " + COMMAND_WORD + " 2";

    private Student toMarkPresent;
    private Index index;
    private boolean isByIndex;
    private boolean isWholeClass;
    private boolean isCorrectStudent;

    /**
     * Creates an MarkPresentCommand to mark all {@code Student} present.
     */
    public MarkPresentCommand() {
        // Mark all students present
        this.isWholeClass = true;
    }

    /**
     * Creates an MarkPresentCommand to mark the specified {@code Student} present.
     */
    public MarkPresentCommand(Student student) {
        requireNonNull(student);
        this.isWholeClass = false;
        // Specified student to mark present
        this.toMarkPresent = student;
        this.isByIndex = false;
    }

    public MarkPresentCommand(Index index) {
        requireNonNull(index);
        this.isWholeClass = false;
        // Specified index of student to mark present
        this.index = index;
        this.isByIndex = true;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        try {
            Lesson uniqueLesson = model.getFilteredLessonList().get(0);
            UniqueList<StudentInfo> uniqueStudentInfoList = uniqueLesson.getStudentsInfo();
            ObservableList<StudentInfo> studentsInfo = uniqueStudentInfoList.asUnmodifiableObservableList();

            if (!this.isWholeClass) {

                if (!isByIndex) {

                    // Mark single student present
                    for (int i = 0; i < studentsInfo.size(); i++) {
                        StudentInfo studentInfo = studentsInfo.get(i);
                        this.isCorrectStudent = studentInfo.containsStudent(this.toMarkPresent);
                        if (this.isCorrectStudent) {
                            Attendance update = studentInfo.getAttendance().setNewAttendance(true);
                            StudentInfo updatedStudentInfo = studentInfo.updateAttendance(update);
                            uniqueStudentInfoList.setElement(studentInfo, updatedStudentInfo);
                            model.updateLessonList();
                            model.updateStudentsInfoList();
                            break;
                        }
                    }

                    if (! this.isCorrectStudent) {
                        throw new CommandException(String.format(MESSAGE_STUDENT_NOT_FOUND, this.toMarkPresent));
                    }
                } else {
                    if (index.getZeroBased() > studentsInfo.size()) {
                        throw new CommandException(String.format(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, index.getOneBased()));
                    }

                    StudentInfo studentInfo = studentsInfo.get(index.getZeroBased());
                    toMarkPresent = studentInfo.getStudent();
                    Attendance update = studentInfo.getAttendance().setNewAttendance(true);
                    StudentInfo updatedStudentInfo = studentInfo.updateAttendance(update);
                    uniqueStudentInfoList.setElement(studentInfo, updatedStudentInfo);
                    model.updateLessonList();
                    model.updateStudentsInfoList();
                }
                return new CommandResult(String.format(MESSAGE_SUCCESS, this.toMarkPresent));
            } else {

                // Mark whole class present
                for (StudentInfo each : studentsInfo) {
                    Attendance update = each.getAttendance().setNewAttendance(true);
                    StudentInfo updatedStudentInfo = each.updateAttendance(update);
                    uniqueStudentInfoList.setElement(each, updatedStudentInfo);
                    model.updateLessonList();
                    model.updateStudentsInfoList();
                }

                return new CommandResult(String.format(MESSAGE_ALL_SUCCESS));
            }

        } catch (Exception e) {
            if (e instanceof CommandException) {
                throw e;
            } else {
                throw new CommandException(MESSAGE_NOT_IN_LESSON);
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MarkPresentCommand // instanceof handles nulls
                && this.toMarkPresent.equals(((MarkPresentCommand) other).toMarkPresent)
                && this.isCorrectStudent == ((MarkPresentCommand) other).isCorrectStudent
                && this.isWholeClass == ((MarkPresentCommand) other).isWholeClass);
    }

}
