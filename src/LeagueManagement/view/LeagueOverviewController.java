package LeagueManagement.view;

import LeagueManagement.model.League;
import LeagueManagement.model.Participant;
import LeagueManagement.utilities.FileUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import LeagueManagement.MainApp;
import java.io.File;

public class LeagueOverviewController {
    @FXML
    private TableView<League> leagueTable;
    @FXML
    private TableColumn<League,String> leagueName;
    @FXML
    private TableColumn<League,Integer> numberOfParticipants;
    @FXML
    private TableView<Participant> teamTable;
    @FXML
    private TableColumn<Participant,String> teamName;
    @FXML
    private TableColumn<Participant, Integer> W;
    @FXML
    private TableColumn<Participant, Integer> D;
    @FXML
    private TableColumn<Participant, Integer> L;
    @FXML
    private TableColumn<Participant, Integer> Pts;
    @FXML
    private TableColumn<Participant,Integer> Pld;
    private MainApp mainApp;
    private ObservableList<League> administratorOwnedLeagues;

    public LeagueOverviewController() { }

    @FXML
    private void initialize() {
        leagueName.setCellValueFactory(cellData -> cellData.getValue().getLeagueNameProperty());
        numberOfParticipants.setCellValueFactory(cellData -> cellData.getValue().getNumberOfParticipantsProperty().asObject());
        showLeagueDetails(null);
        teamName.setCellValueFactory(cellDate -> cellDate.getValue().getTeamNameStringProperty());
        W.setCellValueFactory(cellDate -> cellDate.getValue().getWIntegerProperty().asObject());
        D.setCellValueFactory(cellDate -> cellDate.getValue().getDIntegerProperty().asObject());
        L.setCellValueFactory(cellDate -> cellDate.getValue().getLIntegerProperty().asObject());
        Pts.setCellValueFactory(cellDate -> cellDate.getValue().getPtsIntegerProperty().asObject());
        Pld.setCellValueFactory(cellDate -> cellDate.getValue().getPldIntegerProperty().asObject());
        leagueTable.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue) -> showLeagueDetails(newValue));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        setAdministratorOwnedLeagues();
    }

    private void setAdministratorOwnedLeagues() {
        this.administratorOwnedLeagues = FXCollections.observableArrayList();
        if (this.mainApp.getLeagueData().size() != 0) {
            for (League league : this.mainApp.getLeagueData()) {
                if (league.getAdminId().get() == this.mainApp.getCurrentLoggedInAdministrator().getAdminId()) {
                    administratorOwnedLeagues.add(league);
                }
            }
            this.leagueTable.setItems(administratorOwnedLeagues);
        }
    }

    private void showLeagueDetails(League league){
        if (league != null){
            teamTable.setItems(league.participantData);
        }
    }

    @FXML
    private void handleNewLeague() {
        League tempLeague = new League("",0,mainApp.getLeagueData().size() + 1,mainApp.getCurrentLoggedInAdministrator().getAdminId());   //TODO
        boolean okClicked = mainApp.showLeagueEditDialog(tempLeague);
        if (okClicked) {
            mainApp.getLeagueData().add(tempLeague);
            administratorOwnedLeagues.add(tempLeague);
            leagueTable.setItems(administratorOwnedLeagues);
        }
    }

    @FXML
    public void handleEditLeague() {
        League selectedLeague = leagueTable.getSelectionModel().getSelectedItem();
        if (selectedLeague != null) {
            boolean okClicked = mainApp.showLeagueEditDialog(selectedLeague);
            if (okClicked) {
                showLeagueDetails(selectedLeague);
            }
        }
    }

    @FXML
    public void handleEditParticipant() {
        Participant selectedParticipant = teamTable.getSelectionModel().getSelectedItem();
        if (selectedParticipant != null) {
            boolean okClicked = mainApp.showParticipantEditDialog(selectedParticipant);
            if (okClicked) {
                leagueTable.refresh();
                teamTable.refresh();
            }
        }
    }

    @FXML
    public void handleNewParticipant() {
        League selectedLeague = leagueTable.getSelectionModel().getSelectedItem();
        if (selectedLeague != null) {
            boolean okClicked = mainApp.showParticipantNewDialog(selectedLeague);
            //not handling things but seems to work
            if (okClicked) {
                selectedLeague.clearPartisipantsResults();
                selectedLeague.generateFixtures();
                selectedLeague.setHasResults(false);
            }
        }
    }

    @FXML
    public void handleFixtureAdd() {
        League selectedLeague = leagueTable.getSelectionModel().getSelectedItem();
        if (selectedLeague != null) {
            boolean okClicked = mainApp.showFixtureAddDialog(selectedLeague);
            if (okClicked) {
                selectedLeague.setHasResults(true);
            }
        }
    }

    public void saveChanges() {
        File file = new File("Leagues.csv");
        String leagueFileData = "";
        for (League league : mainApp.getLeagueData()) {
            league.takeADump();
            leagueFileData += league.getLeagueId().get() + "," + league.getLeagueName() + "," + league.getAdminId().get() + "\r\n";
        }
        if (!leagueFileData.isEmpty()) {
            FileUtils.writeFile(file,leagueFileData.substring(0,leagueFileData.length() - 1));
        } else {
            FileUtils.writeFile(file,leagueFileData);
        }
    }
}
