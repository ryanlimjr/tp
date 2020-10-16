package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.ModelStub;

class AddLsnCommandTest {

    @Test
    public void constructor_nullLesson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddLsnCommand(null,null));
    }

    @Test
    public void execute_lsnAcceptedByModel_addLsnSuccessful() {
        // TODO: assertEquals upon successful adding of Lesson
    }

    @Test
    public void execute_duplicateStudent_throwsCommandException() {
        // TODO: assertThrows when adding a duplicate Leson
    }

    private class ModelStubWithStudent extends ModelStub {

    }
}
