package team.serenity.logic.parser.studentinfo;

import static team.serenity.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static team.serenity.logic.commands.CommandTestUtil.INVALID_STUDENT_WITHOUT_NAME;
import static team.serenity.logic.commands.CommandTestUtil.INVALID_STUDENT_WITHOUT_NUMBER;
import static team.serenity.logic.commands.CommandTestUtil.NEG_NUMBER_SET_SCORE;
import static team.serenity.logic.commands.CommandTestUtil.NON_INTEGER;
import static team.serenity.logic.commands.CommandTestUtil.NON_INTEGER_SET_SCORE;
import static team.serenity.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static team.serenity.logic.commands.CommandTestUtil.SET_SCORE_DESC;
import static team.serenity.logic.commands.CommandTestUtil.STUDENT_DESC_AARON;
import static team.serenity.logic.commands.CommandTestUtil.STUDENT_NAME_DESC;
import static team.serenity.logic.commands.CommandTestUtil.STUDENT_NUMBER_DESC;
import static team.serenity.logic.commands.CommandTestUtil.VALID_INDEX;
import static team.serenity.logic.commands.CommandTestUtil.VALID_SCORE;
import static team.serenity.logic.parser.CommandParserTestUtil.assertParseFailure;
import static team.serenity.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import team.serenity.commons.core.index.Index;
import team.serenity.logic.commands.studentinfo.EditScoreCommand;
import team.serenity.model.group.student.Student;
import team.serenity.model.group.studentinfo.Participation;
import team.serenity.testutil.StudentBuilder;

class EditScoreCommandParserTest {

    private EditScoreCommandParser parser = new EditScoreCommandParser();

    @Test
    public void parse_missingStudentName_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditScoreCommand.MESSAGE_USAGE);
        String userInput = PREAMBLE_WHITESPACE + INVALID_STUDENT_WITHOUT_NAME + " " + SET_SCORE_DESC;

        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_missingStudentId_throwsCommandException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditScoreCommand.MESSAGE_USAGE);
        String userInput = PREAMBLE_WHITESPACE + INVALID_STUDENT_WITHOUT_NUMBER + " " + SET_SCORE_DESC;

        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_missingScore_throwsCommandException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditScoreCommand.MESSAGE_USAGE);

        assertParseFailure(parser, PREAMBLE_WHITESPACE + STUDENT_DESC_AARON, expectedMessage);
    }

    @Test
    public void parse_invalidIndex_throwsCommandException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditScoreCommand.MESSAGE_USAGE);
        String empty = "";

        assertParseFailure(parser, NON_INTEGER + " " + SET_SCORE_DESC, expectedMessage);
        assertParseFailure(parser, empty + SET_SCORE_DESC, expectedMessage);
    }

    @Test
    public void parse_invalidScore_throwsCommandException() {
        String expectedMessage = String.format(Participation.MESSAGE_CONSTRAINTS);

        String userInputOne = PREAMBLE_WHITESPACE + VALID_INDEX + " " + NON_INTEGER_SET_SCORE;
        String userInputTwo = PREAMBLE_WHITESPACE + STUDENT_DESC_AARON + " " + NON_INTEGER_SET_SCORE;
        String userInputThree = PREAMBLE_WHITESPACE + VALID_INDEX + " " + NEG_NUMBER_SET_SCORE;
        String userInputFour = PREAMBLE_WHITESPACE + STUDENT_DESC_AARON + " " + NEG_NUMBER_SET_SCORE;

        assertParseFailure(parser, userInputOne, expectedMessage);
        assertParseFailure(parser, userInputTwo, expectedMessage);
        assertParseFailure(parser, userInputThree, expectedMessage);
        assertParseFailure(parser, userInputFour, expectedMessage);
    }

    @Test
    public void parse_studentAndIndex_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditScoreCommand.MESSAGE_USAGE);

        assertParseFailure(parser, VALID_INDEX + STUDENT_DESC_AARON + SET_SCORE_DESC , expectedMessage);
    }

    @Test
    public void parse_studentNameAndIndex_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditScoreCommand.MESSAGE_USAGE);

        assertParseFailure(parser, VALID_INDEX + STUDENT_NAME_DESC + SET_SCORE_DESC, expectedMessage);
    }

    @Test
    public void parse_studentNumberAndIndex_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditScoreCommand.MESSAGE_USAGE);

        assertParseFailure(parser, VALID_INDEX + STUDENT_NUMBER_DESC + SET_SCORE_DESC, expectedMessage);
    }

    @Test
    public void parse_validStudentAndNumberParameter_returnsSetScoreCommand() {
        Student student = new StudentBuilder().build();
        int score = Integer.parseInt(VALID_SCORE);
        String userInput = PREAMBLE_WHITESPACE + STUDENT_DESC_AARON + " " + SET_SCORE_DESC;

        assertParseSuccess(parser, userInput, new EditScoreCommand(student, score));
    }

    @Test
    public void parse_validIndexParameter_returnsSetScoreCommand() {
        Index index = Index.fromOneBased(Integer.parseInt(VALID_INDEX));
        int score = Integer.parseInt(VALID_SCORE);

        assertParseSuccess(parser, VALID_INDEX + " " + SET_SCORE_DESC, new EditScoreCommand(index, score));
    }
}
