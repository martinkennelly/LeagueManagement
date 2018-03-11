package LeagueManagement;

import LeagueManagement.model.Administrator;
import LeagueManagement.utilities.FileUtils;
import LeagueManagement.view.CreateUserDialogController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.util.ArrayList;

public class CreateUserApp extends Application {
    private ArrayList<Administrator> administratorsData = new ArrayList<>();
    private final String createUserDialogLoc = "view/CreateUserDialog.fxml";

    public static void main(String[] args) {
        Application.launch(CreateUserApp.class,args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(CreateUserApp.class.getResource(this.createUserDialogLoc));
        AnchorPane anchorPane = (AnchorPane)fxmlLoader.load();
        CreateUserDialogController controller = fxmlLoader.getController();
        controller.setAdministratorDataAndLoad(administratorsData);
        Scene scene = new Scene(anchorPane);
        primaryStage.setScene(scene);
        primaryStage.setMaxWidth(600);
        primaryStage.setMaxHeight(450);
        primaryStage.setMinHeight(450);
        primaryStage.setMinWidth(600);
        primaryStage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                controller.outputAdministratorsToFile();
            }
        });
        primaryStage.show();

    }
}
