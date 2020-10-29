package team.serenity.ui.lessondata;

import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import team.serenity.commons.core.LogsCenter;
import team.serenity.commons.core.Messages;
import team.serenity.model.group.GroupName;
import team.serenity.model.group.lesson.LessonName;
import team.serenity.model.group.question.Question;
import team.serenity.model.group.studentinfo.StudentInfo;
import team.serenity.ui.DataPanel;
import team.serenity.ui.groupdata.GroupDataPanel;
import team.serenity.ui.serenitydata.QuestionListViewCell;

public class LessonDataPanel extends DataPanel {
    private static final String FXML = "LessonDataPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(GroupDataPanel.class);

    @FXML
    private ListView<StudentInfo> studentInfoListView;

    @FXML
    private ListView<Question> questionListView;

    @FXML
    private TabPane tabPane;

    /**
     * Constructor for panel to display tutorial lesson data.
     */
    public LessonDataPanel(ObservableList<StudentInfo> studentInfoList, ObservableList<Question> questionList,
        String groupName, String lessonName) {
        super(FXML);
        questionList = questionList.filtered(qn -> qn.getGroupName().toString().equals(groupName)
            && qn.getLessonName().toString().equals(lessonName));
        this.studentInfoListView.setItems(studentInfoList);
        this.studentInfoListView.setCellFactory(listView -> new StudentInfoListViewCell());
        this.studentInfoListView.setPlaceholder(new Label(Messages.MESSAGE_NO_STUDENTS));
        this.questionListView.setItems(questionList);
        this.questionListView.setCellFactory(listView -> new QuestionListViewCell());
        this.tabPane.getSelectionModel().select(0);
    }

    public static class StudentInfoListViewCell extends ListCell<StudentInfo> {

        @Override
        protected void updateItem(StudentInfo studentInfo, boolean empty) {
            super.updateItem(studentInfo, empty);

            if (empty || studentInfo == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new StudentInfoCard(studentInfo, getIndex() + 1).getRoot());
            }
        }
    }
}

