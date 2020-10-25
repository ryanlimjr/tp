package team.serenity.logic.commands.partipation;

import static java.util.Objects.requireNonNull;
import static team.serenity.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static team.serenity.commons.core.Messages.MESSAGE_NOT_VIEWING_A_GROUP;
import static team.serenity.commons.core.Messages.MESSAGE_NOT_VIEWING_A_LESSON;
import static team.serenity.commons.core.Messages.MESSAGE_STUDENT_NOT_FOUND;
import static team.serenity.logic.parser.CliSyntax.PREFIX_ADD_SCORE;
import static team.serenity.logic.parser.CliSyntax.PREFIX_ID;
import static team.serenity.logic.parser.CliSyntax.PREFIX_NAME;
import static team.serenity.logic.parser.CliSyntax.PREFIX_SET_SCORE;

import javafx.collections.ObservableList;
import team.serenity.commons.core.index.Index;
import team.serenity.logic.commands.Command;
import team.serenity.logic.commands.CommandResult;
import team.serenity.logic.commands.exceptions.CommandException;
import team.serenity.model.Model;
import team.serenity.model.group.Attendance;
import team.serenity.model.group.Lesson;
import team.serenity.model.group.Participation;
import team.serenity.model.group.Student;
import team.serenity.model.group.StudentInfo;
import team.serenity.model.util.UniqueList;

public class AddScoreCommand extends Command {
    public static final String COMMAND_WORD = "addscore";
    public static final String MESSAGE_SUCCESS = "%s: \nUpdated Participation Score: %d";
    public static final String MESSAGE_STUDENT_NOT_PRESENT =
            "%s is not present. \nPlease ensure student is present before adding score!";
    public static final String MESSAGE_SCORE_NOT_WITHIN_RANGE = "Updated score should be within range of 0 to 5";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Increase the participation score of a specific student for a lesson.\n"
            + "Parameters: "
            + PREFIX_NAME + " STUDENT_NAME "
            + PREFIX_ID + " STUDENT_NUMBER "
            + PREFIX_ADD_SCORE + " SCORE_TO_ADD "
            + "or INDEX " + PREFIX_ADD_SCORE + " SCORE_TO_ADD\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + " Aaron Tan "
            + PREFIX_ID + " e0123456 "
            + PREFIX_ADD_SCORE + " 2\n"
            + "or " + COMMAND_WORD + " 2"
            + PREFIX_ADD_SCORE + " 2\n";

    private Student toAddScore;
    private Index index;
    private boolean isByIndex;
    private int score;
    private int scoreToAdd;
    private boolean isCorrectStudent;

    /**
     * Creates an AddScoreCommand to increase the specified {@code Student}'s participation score.
     */
    public AddScoreCommand(Student student, int scoreToAdd) {
        requireNonNull(student);
        requireNonNull(scoreToAdd);
        // Specified student to add participation score
        this.toAddScore = student;
        this.scoreToAdd = scoreToAdd;
        this.isByIndex = false;
    }

    /**
     * Creates an AddScoreCommand to increase the specified {@code Student}'s participation score by index.
     */
    public AddScoreCommand(Index index, int scoreToAdd) {
        requireNonNull(index);
        requireNonNull(scoreToAdd);
        // Specified index of student to add participation score
        this.index = index;
        this.scoreToAdd = scoreToAdd;
        this.isByIndex = true;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.getFilteredGroupList().size() != 1) {
            throw new CommandException(MESSAGE_NOT_VIEWING_A_GROUP);
        }

        if (model.getFilteredLessonList().size() != 1) {
            throw new CommandException(MESSAGE_NOT_VIEWING_A_LESSON);
        }

        Lesson uniqueLesson = model.getFilteredLessonList().get(0);
        UniqueList<StudentInfo> uniqueStudentInfoList = uniqueLesson.getStudentsInfo();
        ObservableList<StudentInfo> studentsInfo = uniqueStudentInfoList.asUnmodifiableObservableList();
        int newScore = 0;

        if (! isByIndex) {
            // Update single student participation score
            for (int i = 0; i < studentsInfo.size(); i++) {
                StudentInfo studentInfo = studentsInfo.get(i);
                this.score = studentInfo.getParticipation().getScore();
                this.isCorrectStudent = studentInfo.containsStudent(this.toAddScore);
                if (this.isCorrectStudent) {
                    Attendance currentAttendance = studentInfo.getAttendance();
                    if (! currentAttendance.getAttendance()) {
                        throw new CommandException(String.format(MESSAGE_STUDENT_NOT_PRESENT, this.toAddScore));
                    }
                    newScore = score + scoreToAdd;
                    if (newScore > 5 || newScore < 0) {
                        throw new CommandException(MESSAGE_SCORE_NOT_WITHIN_RANGE);
                    }
                    Participation update = studentInfo.getParticipation().setNewScore(newScore);
                    StudentInfo updatedStudentInfo = studentInfo.updateParticipation(update);
                    uniqueStudentInfoList.setElement(studentInfo, updatedStudentInfo);
                    model.updateLessonList();
                    model.updateStudentsInfoList();
                    break;
                }
        }
            if (! this.isCorrectStudent) {
                throw new CommandException(String.format(MESSAGE_STUDENT_NOT_FOUND, this.toAddScore));
            }
        } else {
            if (index.getZeroBased() > studentsInfo.size()) {
                throw new CommandException(String.format(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX,
                        index.getOneBased()));
            }

            StudentInfo studentInfo = studentsInfo.get(index.getZeroBased());
            toAddScore = studentInfo.getStudent();
            Attendance currentAttendance = studentInfo.getAttendance();
            if (! currentAttendance.getAttendance()) {
                throw new CommandException(String.format(MESSAGE_STUDENT_NOT_PRESENT, this.toAddScore));
            }
            newScore = score + scoreToAdd;
            if (newScore > 5 || newScore < 0) {
                throw new CommandException(MESSAGE_SCORE_NOT_WITHIN_RANGE);
            }
            Participation update = studentInfo.getParticipation().setNewScore(newScore);
            StudentInfo updatedStudentInfo = studentInfo.updateParticipation(update);
            uniqueStudentInfoList.setElement(studentInfo, updatedStudentInfo);
            model.updateLessonList();
            model.updateStudentsInfoList();
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, this.toAddScore, newScore));
    }

    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddScoreCommand // instanceof handles nulls
                && this.toAddScore.equals(((AddScoreCommand) other).toAddScore)
                && this.scoreToAdd == ((AddScoreCommand) other).scoreToAdd);
    }
}
