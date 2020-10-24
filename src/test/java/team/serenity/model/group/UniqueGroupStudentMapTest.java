package team.serenity.model.group;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static team.serenity.testutil.TypicalGroups.GROUP_C;
import static team.serenity.testutil.TypicalGroups.GROUP_D;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import team.serenity.model.group.exceptions.DuplicateQuestionException;
import team.serenity.model.group.exceptions.QuestionNotFoundException;
import team.serenity.model.util.UniqueList;
import team.serenity.model.util.UniqueMap;

class UniqueGroupStudentMapTest {
    private final UniqueMap<Group, UniqueList<Student>> uniqueGroupStudentMap = new UniqueGroupStudentMap();

    @Test
    public void containsKey_NullGroup_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueGroupStudentMap.containsKey(null));
    }



}
