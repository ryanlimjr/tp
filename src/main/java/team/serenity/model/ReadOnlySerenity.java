package team.serenity.model;

import java.util.List;

import team.serenity.model.group.Group;

/**
 * Unmodifiable view of a Serenity object.
 */
public interface ReadOnlySerenity {

    /**
     * Returns an unmodifiable view of the group list. This list will not contain any duplicate groups.
     */
    List<Group> getGroupList();

}