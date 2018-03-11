package LeagueManagement.view;

import LeagueManagement.model.League;
import LeagueManagement.model.Participant;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Optional;

public class ParticipantAddDialogController {
    @FXML
    private TextField participantNameField;
    private Stage dialogStage;
    private League league;
    private boolean okClicked = false;

    @FXML
    private void initialize(){}

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setLeague(League league){
        this.league = league;
    }

    public void addParticipant(Participant participant) {
        this.league.getParticipantData().add(participant);
    }

    public boolean isOkClicked() {
        return this.okClicked;
    }

    @FXML
    public void handleOk(){
        boolean okToContinue = true;
        if (this.league.getHasResults()) {
            okToContinue = areYouSure();
        }
        if (isInputValid() && okToContinue){
            league.getParticipantData().add(new Participant(participantNameField.getText(),getMaxParticipantId() + 1));
            league.setNumberOfParticipants(league.getNumberOfParticipants() + 1);
            if (league.getNumberOfParticipants() % 2 == 1) {
                league.getParticipantData().add(new Participant("RENAME - EVEN NUMBER OF TEAMS ONLY",getMaxParticipantId() + 1));
                league.setNumberOfParticipants(league.getNumberOfParticipants() + 1);
            }
            okClicked = true;
            league.fixtureData.clear();
            dialogStage.close();
        }
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }

    private int getMaxParticipantId() {
        int max = 0;
        for (Participant each : league.getParticipantData()) {
            if (each.getParticipantId() > max) {
                max = each.getParticipantId();
            }
        }
        return max;
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

    private boolean areYouSure() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("If you proceed, all results will be lost");
        alert.setContentText("Are you ok with this?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }
}
