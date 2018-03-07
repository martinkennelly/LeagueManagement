package LeagueManagement.view;

import LeagueManagement.model.Fixture;
import LeagueManagement.model.League;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class FixtureAddController  {
    @FXML
    private TableView<Fixture> fixtureTable;
    @FXML
    private TextField homeScoreField;
    @FXML
    private TextField awayScoreField;
    @FXML
    private Button submitButton;
    @FXML
    private TableColumn<Fixture, String> homeParticipantColumn;
    @FXML
    private TableColumn<Fixture, String> awayParticipantColumn;
    @FXML
    private TableColumn<Fixture, Integer> homeResultColumn;
    @FXML
    private TableColumn<Fixture, Integer> awayResultColumn;
    private League league;
    private Stage dialogStage;
    private boolean isCloseClicked = false;

    @FXML
    public void initialize() {
    }

    public boolean isCloseClicked(){
        return this.isCloseClicked;
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setLeague(League league) {
        this.league = league;
        if (league.getFixtureData().isEmpty()) {
            league.generateFixtures();
            fixtureTable.setItems(league.getFixtureData());
        } else {
            fixtureTable.setItems(league.getFixtureData());
        }
    }

    @FXML
    private void close(){
        this.isCloseClicked = true;
        this.dialogStage.close();
    }

    @FXML
    private void commit() {
        Fixture selectedFixture = fixtureTable.getSelectionModel().getSelectedItem();
        String errorMsg = "";
        if (checkFieldsValid()) {
            if (selectedFixture != null) {
                int homeResult = Integer.parseInt(homeScoreField.getText());
                int awayResult = Integer.parseInt(awayScoreField.getText());
                selectedFixture.setHomeResult(homeResult);
                selectedFixture.setAwayResult(awayResult);
                homeScoreField.clear();
                awayScoreField.clear();
                fixtureTable.refresh();
                fixtureTable.getSelectionModel().selectNext();
                league.generateTable();
                fixtureTable.sort();
            } else {
                errorMsg = "Select a row to change!!";
            }
        } else {
            errorMsg = "Incorrect Inputs, please enter Integers only!!";
        }
        if (!errorMsg.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid input(s)");
            alert.setContentText(errorMsg);
            alert.showAndWait();
        }
    }

    private boolean checkFieldsValid() {
        boolean result =  !awayScoreField.getText().isEmpty() && !homeScoreField.getText().isEmpty();
        try {
            Integer.parseInt(awayScoreField.getText());
            Integer.parseInt(homeScoreField.getText());
        } catch (NumberFormatException e) {
            result = false;
        }
        return result;
    }
}


