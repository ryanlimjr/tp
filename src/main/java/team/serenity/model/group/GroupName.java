package team.serenity.model.group;

import static team.serenity.commons.util.AppUtil.checkArgument;

public class GroupName {

    public static final String MESSAGE_CONSTRAINTS =
        "Group name must follow the format 'XYY' where X is an alphabetical character and Y is a digit from 0 to 9.";

    public static final String MESSAGE_GROUP_NAME_EMPTY =
        "Group name is empty.";

    public static final String MESSAGE_GROUP_NAME_MULTIPLE =
        "Only 1 group name can be entered.";

    /*
     * Must contain one upper case letter followed by 2 digits.
     */
    public static final String VALIDATION_REGEX = "[A-Z][0-9]{2}";

    public final String groupName;

    /**
     * Constructs a {@code GroupName}.
     *
     * @param name A valid name.
     */
    public GroupName(String name) {
        assert name != null;
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        this.groupName = name.toUpperCase();
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        assert test != null;
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return groupName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof GroupName // instanceof handles nulls
            && groupName.equals(((GroupName) other).groupName)); // state check
    }

    @Override
    public int hashCode() {
        return groupName.hashCode();
    }

}
