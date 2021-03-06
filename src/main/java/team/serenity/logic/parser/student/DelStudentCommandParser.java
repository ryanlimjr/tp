package team.serenity.logic.parser.student;

import static team.serenity.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static team.serenity.logic.parser.CliSyntax.PREFIX_GRP;
import static team.serenity.logic.parser.CliSyntax.PREFIX_MATRIC;
import static team.serenity.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.stream.Stream;

import team.serenity.commons.core.index.Index;
import team.serenity.logic.commands.student.DelStudentCommand;
import team.serenity.logic.parser.ArgumentMultimap;
import team.serenity.logic.parser.ArgumentTokenizer;
import team.serenity.logic.parser.Parser;
import team.serenity.logic.parser.Prefix;
import team.serenity.logic.parser.SerenityParserUtil;
import team.serenity.logic.parser.exceptions.ParseException;
import team.serenity.model.group.GroupContainsKeywordPredicate;
import team.serenity.model.group.student.StudentName;
import team.serenity.model.group.student.StudentNumber;

/**
 * Parses input arguments and creates a new DelStudentCommand object.
 */
public class DelStudentCommandParser implements Parser<DelStudentCommand> {

    private final ParseException deleteStudentCommandParserException = new ParseException(
        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DelStudentCommand.MESSAGE_USAGE));

    @Override
    public DelStudentCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_GRP, PREFIX_NAME, PREFIX_MATRIC);

        if (!arePrefixesPresent(argMultimap, PREFIX_GRP)) {
            throw this.deleteStudentCommandParserException;
        }

        if (argMultimap.getValue(PREFIX_NAME).isPresent() && !argMultimap.getValue(PREFIX_MATRIC).isPresent()) {
            throw this.deleteStudentCommandParserException;
        }

        if (!argMultimap.getValue(PREFIX_NAME).isPresent() && argMultimap.getValue(PREFIX_MATRIC).isPresent()) {
            throw this.deleteStudentCommandParserException;
        }
        if (argMultimap.getValue(PREFIX_NAME).isPresent() && argMultimap.getPreamble().length() != 0) {
            throw this.deleteStudentCommandParserException;
        }

        if (argMultimap.getValue(PREFIX_MATRIC).isPresent() && argMultimap.getPreamble().length() != 0) {
            throw this.deleteStudentCommandParserException;
        }

        Index index;
        String[] grpKeywordArray = argMultimap.getValue(PREFIX_GRP).get().split("\\s+");

        if (grpKeywordArray.length != 1) {
            throw this.deleteStudentCommandParserException;
        }

        if (argMultimap.getValue(PREFIX_NAME).isPresent() && argMultimap.getValue(PREFIX_MATRIC).isPresent()) {
            String[] studentNameArray = argMultimap.getValue(PREFIX_NAME).get().split("\\s+");
            String[] studentIdArray = argMultimap.getValue(PREFIX_MATRIC).get().split("\\s+");

            if (studentIdArray.length != 1) {
                throw this.deleteStudentCommandParserException;
            }

            String name = String.join(" ", studentNameArray);
            String matric = studentIdArray[0];
            String grpName = grpKeywordArray[0];

            StudentName studentName = SerenityParserUtil.parseStudentName(name);
            StudentNumber studentId = SerenityParserUtil.parseStudentNumber(matric);
            grpName = SerenityParserUtil.parseGroupName(grpName).toString();
            return new DelStudentCommand(studentName, studentId, new GroupContainsKeywordPredicate(grpName));

        } else {
            try {
                index = SerenityParserUtil.parseIndex(argMultimap.getPreamble());
            } catch (NumberFormatException e) {
                throw this.deleteStudentCommandParserException;
            }
            String grpName = grpKeywordArray[0];
            grpName = SerenityParserUtil.parseGroupName(grpName).toString();
            return new DelStudentCommand(index, new GroupContainsKeywordPredicate(grpName));
        }
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
