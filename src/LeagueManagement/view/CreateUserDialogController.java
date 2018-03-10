package LeagueManagement.view;

import LeagueManagement.CreateUserApp;
import LeagueManagement.model.Administrator;
import LeagueManagement.utilities.CSVUtils;
import LeagueManagement.utilities.FileUtils;
import LeagueManagement.utilities.Password;
import LeagueManagement.utilities.StringUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CreateUserDialogController implements Initializable{
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button submitButton;
    private ArrayList<Administrator> administratorData;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setAdministratorData(ArrayList<Administrator> administratorData) {
        this.administratorData = administratorData;
        loadAdministratorFile();
    }

    @FXML
    private void handleCreateUser(final ActionEvent event) {
        if (isInputValid()) {
            final String username = usernameField.getText();
            final String password = passwordField.getText();
            if (!isUsernameAlreadyCreated(username)) {
                Administrator administrator;
                try {
                    administrator = new Administrator(getNextAdministratorId(), username, Password.getSaltedHash(password));
                } catch (Exception e) {
                    administrator = new Administrator(getNextAdministratorId(), username, password);
                }
                this.administratorData.add(administrator);
                JOptionPane.showMessageDialog(null, "User Created!");
                usernameField.clear();
                passwordField.clear();
            } else {
                JOptionPane.showMessageDialog(null, "Already user like this created, try a different username!");
            }
        }
    }

    private boolean isUsernameAlreadyCreated(String username) {
        for (Administrator administrator : this.administratorData) {
            if (administrator.getUsername().equalsIgnoreCase(username)){
                return true;
            }
        }
        return false;
    }

    private int getNextAdministratorId() {
        int max = 0;
        for (Administrator administrator : this.administratorData) {
            if (administrator.getAdminId() > max) {
                max = administrator.getAdminId();
            }
        }
        return max+1;
    }

    private boolean isInputValid() {
        boolean isValid = false;
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
            isValid = true;
        } else {
            JOptionPane.showMessageDialog(null, errorMsg);
            isValid = false;
        }
        return isValid;
    }

    private boolean loadAdministratorFile() {
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

    public void takeADump() {
        File administratorFile = new File("Administrators.csv");
        StringBuilder sb = new StringBuilder();
        if (this.administratorData.size() != 0) {
            for (Administrator administrator : this.administratorData) {
                sb.append(administrator.getAdminId() + "," + administrator.getUsername() + ',' + administrator.getPasswordHash() + "\r\n");
            }
            FileUtils.writeFile(administratorFile,sb.substring(0,sb.length() - 2));
        }
    }
}
