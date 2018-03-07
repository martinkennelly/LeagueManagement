package LeagueManagement.view;

import LeagueManagement.model.League;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LeagueEditDialogController {
    @FXML
    private TextField leagueNameField;
    private Stage dialogStage;
    private League league;
    private boolean okClicked = false;

    @FXML
    private void initialize() { }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setLeague(League league) {
        this.league = league;
        leagueNameField.setText(league.getLeagueName());
    }

    public boolean isOkClicked() {
        return this.okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            league.setLeagueName(leagueNameField.getText());
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        boolean result = false;
        String errorMsg = "";
        if (leagueNameField.getText() == null || leagueNameField.getText().length() == 0) {
            errorMsg += "Incorrect league name\n";
        }
        if (errorMsg.length() == 0) {
            result = true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid input(s)");
            alert.setHeaderText("Correct input boxs");
            alert.setContentText(errorMsg);
            alert.showAndWait();
        }
        return result;
    }
}
