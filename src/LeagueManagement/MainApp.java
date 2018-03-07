package LeagueManagement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import LeagueManagement.model.Administrator;
import LeagueManagement.model.Fixture;
import LeagueManagement.model.Participant;
import LeagueManagement.utilities.CSVUtils;
import LeagueManagement.utilities.FileUtils;
import LeagueManagement.view.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import LeagueManagement.model.League;
import javafx.stage.WindowEvent;

public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<League> leagueData = FXCollections.observableArrayList();
    private ObservableList<Administrator> administratorData = FXCollections.observableArrayList();
    private Administrator currentLoggedInAdministrator = null;

    public MainApp() { }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("League Management");
        this.primaryStage.setMinWidth(1000);
        this.primaryStage.setMinHeight(700);
        this.primaryStage.setMaxHeight(1000);
        this.primaryStage.setMaxWidth(700);
        initRootLayout();
        if (loadAdministratorFile()) {
            if (showAdminLoginDialog(this.administratorData)) {
                if (currentLoggedInAdministrator != null) {
                    loadData();
                    showLeagueOverview();
                }
            } else {
                primaryStage.close();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(primaryStage);
            alert.setTitle("Missing Administrator File");
            alert.setHeaderText("Contact admin");
            alert.setContentText("Please contact admin and tell him to do his job");
            alert.showAndWait();
            primaryStage.close();
        }
    }

    public void setCurrentLoggedInAdministrator(Administrator administrator){
        this.currentLoggedInAdministrator = administrator;
    }

    public Administrator getCurrentLoggedInAdministrator() {
        return this.currentLoggedInAdministrator;
    }

    public void loadData() {
        File leagueOverview = new File("Leagues.csv");
        if (FileUtils.checkExists(leagueOverview)) {
            ArrayList<ArrayList<String>> leaguesFile = CSVUtils.readInCSV(leagueOverview,true);
            for (ArrayList<String> row : leaguesFile) {
                League league = new League(row.get(1),0,Integer.parseInt(row.get(0)),Integer.parseInt(row.get(2)));
                ArrayList<ArrayList<String>> participantsFile = CSVUtils.readInCSV(new File(row.get(0) + "_Partisipants.csv"),true);
                ArrayList<ArrayList<String>> fixturesFile = CSVUtils.readInCSV(new File(row.get(0) + "_Fixtures.csv"),true);
                ArrayList<ArrayList<String>> resultsFile = CSVUtils.readInCSV(new File(row.get(0) + "_Results.csv"),true);
                for (ArrayList<String> participantRow : participantsFile) {
                    league.addParticipant(new Participant(participantRow.get(1),Integer.parseInt(participantRow.get(0))));
                }
                league.setNumberOfParticipants(league.getNumberOfParticipants());
                for (ArrayList<String> fixtureRow : fixturesFile) {
                    league.addFixture(new Fixture(Integer.parseInt(fixtureRow.get(0)),Integer.parseInt(fixtureRow.get(1)),Integer.parseInt(fixtureRow.get(2)),league.getParticipantName(Integer.parseInt(fixtureRow.get(1))),league.getParticipantName(Integer.parseInt(fixtureRow.get(2)))));
                }
                for (ArrayList<String> resultsRow : resultsFile) {
                    Fixture fixture = league.findFixture(Integer.parseInt(resultsRow.get(0)));
                    if (!(resultsRow.get(1).equalsIgnoreCase("null") || resultsRow.get(2).equalsIgnoreCase("null"))) {
                        fixture.setHomeResult(Integer.parseInt(resultsRow.get(1)));
                        fixture.setAwayResult(Integer.parseInt(resultsRow.get(2)));
                    }
                }
                league.generateTable();
                getLeagueData().add(league);
            }
        }
    }

    public boolean loadAdministratorFile() {
        boolean isSuccessful = false;
        File file = new File("Administrators.csv");
        if (FileUtils.checkExists(file)) {
            ArrayList<ArrayList<String>> adminFile =  CSVUtils.readInCSV(file,true);
            Administrator newAdmin;
            for (ArrayList<String> row : adminFile) {
                if (row.size() == 3) {
                    try {
                        newAdmin = new Administrator(Integer.parseInt(row.get(0)),row.get(1),row.get(2));
                        administratorData.add(newAdmin);
                        isSuccessful = true;
                    } catch (NumberFormatException e) {throw new RuntimeException("Corrupted admin file: " + e.getLocalizedMessage());}
                } else {
                    throw new RuntimeException("Corrupted admin file");
                }
            }
        }
        return isSuccessful;
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException iox) {
            iox.printStackTrace();
        }
    }

    public void showLeagueOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/LeaguesOverview.fxml"));
            AnchorPane leagueOverview = (AnchorPane) loader.load();
            rootLayout.setCenter(leagueOverview);
            LeagueOverviewController controller = loader.getController();
            controller.setMainApp(this);
            primaryStage.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    controller.saveChanges();
                }
            });
        } catch (IOException iox) {
            iox.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    public ObservableList<League> getLeagueData() {
        return this.leagueData;
    }

    public ObservableList<Participant> getTeamData(League league) {
        return league.getParticipantData();
    }

    public boolean showLeagueEditDialog(League league) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/LeagueEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("League Management");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            LeagueEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setLeague(league);
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException iox) {
            return false;
        }
    }

    public boolean showParticipantEditDialog(Participant participant) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ParticipantEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage diaglogStage = new Stage();
            diaglogStage.setTitle("Edit Participant");
            diaglogStage.initModality(Modality.WINDOW_MODAL);
            diaglogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            diaglogStage.setScene(scene);
            ParticipantEditDialogController controller = loader.getController();
            controller.setDialogStage(diaglogStage);
            controller.setParticipantName(participant);
            diaglogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException iox) {
            return false;
        }
    }

    public boolean showParticipantNewDialog(League league) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ParticipantAddDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create New Participant");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            ParticipantAddDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setLeague(league);
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException iox) {
            return false;
        }
    }

    public boolean showFixtureAddDialog(League league) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/FixtureAdd.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Fixture Add/Edit");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            FixtureAddController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setLeague(league);
            dialogStage.showAndWait();
            return controller.isCloseClicked();
        } catch (IOException iox) {
            iox.printStackTrace();
            return false;
        }
    }

    private boolean showAdminLoginDialog(ObservableList<Administrator> administratorData) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AdminLoginDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Log in");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            AdminLoginDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setAdministratorData(administratorData);
            controller.setMainApp(this);
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException iox) {
            iox.printStackTrace();
            return false;
        }
    }
}
