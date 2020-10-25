package team.serenity.logic.commands.question;

import static java.util.Objects.requireNonNull;
import static team.serenity.model.Model.PREDICATE_SHOW_ALL_QUESTIONS;

import team.serenity.logic.commands.Command;
import team.serenity.logic.commands.CommandResult;
import team.serenity.logic.commands.exceptions.CommandException;
import team.serenity.model.Model;

/**
 * Lists all questions to the user.
 */
public class ViewQnCommand extends Command {

    public static final String COMMAND_WORD = "viewqn";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all questions.";
    public static final String MESSAGE_SUCCESS = "Listed all questions.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        model.updateFilteredQuestionList(PREDICATE_SHOW_ALL_QUESTIONS);
        // TODO: Ryan/Ruien to add the viewQn boolean to CommandResult constructor to switch to view question tab
        return new CommandResult(MESSAGE_SUCCESS);
    }

}