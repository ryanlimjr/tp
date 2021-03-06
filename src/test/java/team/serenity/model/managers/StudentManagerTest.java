package team.serenity.model.managers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static team.serenity.testutil.Assert.assertThrows;
import static team.serenity.testutil.TypicalGroups.GROUP_G01;
import static team.serenity.testutil.TypicalGroups.GROUP_G02;
import static team.serenity.testutil.TypicalStudent.AARON;
import static team.serenity.testutil.TypicalStudent.DAVID;
import static team.serenity.testutil.TypicalStudent.HELENE;
import static team.serenity.testutil.TypicalStudent.JEFFERY;
import static team.serenity.testutil.TypicalStudent.getTypicalStudentManager;
import static team.serenity.testutil.TypicalStudent.getTypicalStudents;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import team.serenity.model.group.exceptions.DuplicateStudentException;
import team.serenity.model.group.exceptions.GroupNotFoundException;
import team.serenity.model.group.exceptions.StudentNotFoundException;

class StudentManagerTest {

    private final StudentManager studentManager = new StudentManager();

    @Test
    public void constructor_noParams() {
        assertEquals(Collections.emptyMap(), this.studentManager.getStudentMap());
    }

    @Test
    public void constructor_withParams() {
        StudentManager actual = new StudentManager(this.studentManager);
        assertEquals(actual.getStudentMap(), this.studentManager.getStudentMap());
    }

    @Test
    public void resetData_withNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> this.studentManager.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyGroupManager() {
        StudentManager newData = getTypicalStudentManager();
        this.studentManager.resetData(newData);
        assertEquals(newData, this.studentManager);
    }

    @Test
    public void addStudentToGroup_nullInput_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> this.studentManager
            .addStudentToGroup(null, null));
        assertThrows(NullPointerException.class, () -> this.studentManager
            .addStudentToGroup(GROUP_G01.getGroupName(), null));
        assertThrows(NullPointerException.class, () -> this.studentManager
            .addStudentToGroup(null, HELENE));
    }

    @Test
    public void addStudentToGroup_targetGroupDoesNotExist_throwGroupNotFoundException() {
        assertThrows(GroupNotFoundException.class, () -> this.studentManager
            .addStudentToGroup(GROUP_G01.getGroupName(), HELENE));
    }

    @Test
    public void addStudentToGroup_duplicateStudent_throwDuplicateLessonException() {
        this.studentManager.resetData(getTypicalStudentManager());
        assertThrows(DuplicateStudentException.class, () ->
            this.studentManager.addStudentToGroup(GROUP_G01.getGroupName(), AARON));
    }

    @Test
    public void addStudentToGroup_validStudent() {
        this.studentManager.resetData(getTypicalStudentManager());
        assertFalse(this.studentManager.checkIfStudentExistsInGroup(GROUP_G01.getGroupName(), HELENE));
        this.studentManager.addStudentToGroup(GROUP_G01.getGroupName(), HELENE);
        assertTrue(this.studentManager.checkIfStudentExistsInGroup(GROUP_G01.getGroupName(), HELENE));
    }

    @Test
    public void addListOfStudentsToGroup_nullInput_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                this.studentManager.addListOfStudentsToGroup(null, null));
        assertThrows(NullPointerException.class, () ->
                this.studentManager.addListOfStudentsToGroup(GROUP_G01.getGroupName(), null));
        assertThrows(NullPointerException.class, () ->
                this.studentManager.addListOfStudentsToGroup(null, getTypicalStudents()));
    }

    @Test
    public void checkIfStudentExistsInGroup_nullInput_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                this.studentManager.checkIfStudentExistsInGroup(null, null));
        assertThrows(NullPointerException.class, () ->
                this.studentManager.checkIfStudentExistsInGroup(GROUP_G01.getGroupName(), null));
        assertThrows(NullPointerException.class, () ->
                this.studentManager.checkIfStudentExistsInGroup(null, HELENE));
    }

    @Test
    public void checkIfStudentExistsInGroup_targetGroupDoesNotExist_throwGroupNotFoundException() {
        assertThrows(GroupNotFoundException.class, () ->
                this.studentManager.checkIfStudentExistsInGroup(GROUP_G01.getGroupName(), HELENE));
    }

    @Test
    public void checkIfStudentExistsInGroup_targetStudentExist_returnTrue() {
        StudentManager studentManager = getTypicalStudentManager();
        assertTrue(studentManager.checkIfStudentExistsInGroup(GROUP_G01.getGroupName(), AARON));
    }

    @Test
    public void checkIfStudentExistsInGroup_targetStudentDoesNotExist_returnFalse() {
        StudentManager studentManager = getTypicalStudentManager();
        assertFalse(studentManager.checkIfStudentExistsInGroup(GROUP_G02.getGroupName(), JEFFERY));
    }

    @Test
    public void getListOfStudentsFromGroup_nullInput_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> this.studentManager.getListOfStudentsFromGroup(null));
    }

    @Test
    public void getListOfStudentsFromGroup_targetGroupDoesNotExist_throwsGroupNotFoundException() {
        assertThrows(GroupNotFoundException.class, () ->
            this.studentManager.getListOfStudentsFromGroup(GROUP_G01.getGroupName()));
    }

    @Test
    public void getListOfStudentsFromGroup_validGroup() {
        this.studentManager.resetData(getTypicalStudentManager());
        assertDoesNotThrow(() -> this.studentManager.getListOfStudentsFromGroup(GROUP_G01.getGroupName()));
    }

    @Test
    public void setListOfStudentsToGroup_nullInput_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                this.studentManager.setListOfStudentsToGroup(null, null));
        assertThrows(NullPointerException.class, () ->
                this.studentManager.setListOfStudentsToGroup(GROUP_G01.getGroupName(), null));
        assertThrows(NullPointerException.class, () ->
                this.studentManager.setListOfStudentsToGroup(null, getTypicalStudents()));
    }

    @Test
    public void deleteStudentsFromGroup_nullInput_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
            this.studentManager.deleteStudentFromGroup(null, null));
        assertThrows(NullPointerException.class, () ->
            this.studentManager.deleteStudentFromGroup(GROUP_G02.getGroupName(), null));
        assertThrows(NullPointerException.class, () ->
            this.studentManager.deleteStudentFromGroup(null, HELENE));
    }

    @Test
    public void deleteStudentFromGroup_targetGroupDoesNotExist_throwsGroupNotFoundException() {
        assertThrows(GroupNotFoundException.class, () ->
            this.studentManager.deleteStudentFromGroup(GROUP_G02.getGroupName(), HELENE));
    }

    @Test
    public void deleteStudentFromGroup_studentDoesNotExist_throwsStudentNotFoundException() {
        this.studentManager.resetData(getTypicalStudentManager());
        assertThrows(StudentNotFoundException.class, () -> this.studentManager
                .deleteStudentFromGroup(GROUP_G02.getGroupName(), HELENE));
    }

    @Test
    public void deleteStudentFromGroup_validStudent() {
        this.studentManager.resetData(getTypicalStudentManager());
        assertTrue(this.studentManager.checkIfStudentExistsInGroup(GROUP_G02.getGroupName(), DAVID));
        this.studentManager.deleteStudentFromGroup(GROUP_G02.getGroupName(), DAVID);
        assertFalse(this.studentManager.checkIfStudentExistsInGroup(GROUP_G02.getGroupName(), DAVID));
    }
}
