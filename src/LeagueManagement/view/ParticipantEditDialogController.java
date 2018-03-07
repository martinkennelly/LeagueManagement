package LeagueManagement.view;

import LeagueManagement.model.Participant;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ParticipantEditDialogController {
    @FXML
    private TextField participantNameField;
    private Stage dialogStage;
    private Participant participant;
    private boolean okClicked = false;

    @FXML
    private void initialize() { }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setParticipantName(Participant participant) {
        this.participant = participant;
        this.participantNameField.setText(participant.getParticipantName());
    }

    public boolean isOkClicked() {
        return this.okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            participant.setParticipantName(participantNameField.getText());
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMsg = "";
        if (participantNameField.getText() == null || participantNameField.getText().length() == 0) {
            errorMsg += "Incorrect participant name\n";
        }
        if (errorMsg.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid input(s)");
            alert.setHeaderText("Correct input box");
            alert.setContentText(errorMsg);
            alert.showAndWait();
            return false;
        }
    }
}
