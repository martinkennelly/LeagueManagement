package LeagueManagement.view;

import LeagueManagement.MainApp;
import LeagueManagement.model.Administrator;
import LeagueManagement.utilities.Password;
import LeagueManagement.utilities.StringUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Optional;


public class AdminLoginDialogController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    private Stage dialogStage;
    private boolean loginClicked = false;
    private int numberOfLoginAttempts = 0;
    private MainApp mainApp;
    private ObservableList<Administrator> administratorData;

    @FXML
    private void initialize(){}

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setAdministratorData(ObservableList<Administrator> administratorData) {
        this.administratorData = administratorData;
    }

    public boolean isOkClicked() {
        return this.loginClicked;
    }

    @FXML
    public void handleLoginAttempt() {
        boolean okToContinue = false;
        if (isInputValid()) {
            if (this.administratorData.size() != 0) {
                for (Administrator administrator : this.administratorData) {
                    try {
                        if (administrator.getUsername().equalsIgnoreCase(usernameField.getText())) {
                            if (Password.checkPassword(passwordField.getText(), administrator.getPasswordHash())) {
                                okToContinue = true;
                                mainApp.setCurrentLoggedInAdministrator(administrator);
                            }
                        }
                    } catch (Exception e) {e.printStackTrace();}
                }

            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.initOwner(dialogStage);
                alert.setTitle("Please contact Software Administrator");
                alert.setHeaderText("Ask software Administor to create your account");
                alert.setContentText("Contact administrator to create an account for you!");
                alert.showAndWait();
            }

        }
        if (okToContinue) {
            loginClicked = true;
            dialogStage.close();
        } else {
            this.numberOfLoginAttempts++;
            if (this.numberOfLoginAttempts >= 3) {
                dialogStage.close();
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.initOwner(dialogStage);
                alert.setTitle("Wrong Credentials");
                alert.setHeaderText("Incorrect login information");
                alert.setContentText("Try again!...but you only have " + (3 - this.numberOfLoginAttempts) + "  attempts left!!");
                alert.showAndWait();
                passwordField.clear();
            }
        }
    }

    @FXML
    public void handleExit() {
        if (areYouSure()) {
            dialogStage.close();
        }
    }

    private boolean isInputValid() {
        String errorMsg = "";
        boolean result = false;
        if (usernameField.getText() == null || usernameField.getText().length() == 0) {
            errorMsg += "Enter a username name\n";
        }

        if (passwordField.getText() == null || passwordField.getText().length() == 0) {
            errorMsg += "Enter a password\n";
        }

        if (!StringUtils.validateUsernamePassword(usernameField.getText(),5,20)) {
            errorMsg += "Enter a username between 5 and 20 characters only using alphabetic characters, numbers, _ or -\n";
        }

        if (!StringUtils.validateUsernamePassword(passwordField.getText(),7,20)) {
            errorMsg += "Enter a password between 7 and 20 characters only using alphabetic characters,numbers, _ or -\n";
        }

        if (errorMsg.isEmpty()) {
            result = true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid input(s)");
            alert.setHeaderText("Correct input box(s)");
            alert.setContentText(errorMsg);
            alert.showAndWait();
            result = false;
        }
        return result;
    }

    private boolean areYouSure() {
        boolean proceed = false;
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("If you proceed, you will exit the application");
        alert.setContentText("Are you ok with this?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            proceed = true;
        }
        return proceed;
    }
}
