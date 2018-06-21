package edu.cnm.deepdive.rps;

import edu.cnm.deepdive.rps.controller.Controller;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

  public static final String RESOURCES_DIR = "resources";
  public static final String UI_STRINGS = "/ui-strings";
  public static final String MAIN_FXML = "/main.fxml";
  public static final String WINDOW_TITLE = "window_title";
  public static final String RPS_PNG = "/rps.png";

  private Controller controller;

  @Override
  public void start(Stage stage) throws Exception {
    ClassLoader classLoader = getClass().getClassLoader();
    ResourceBundle bundle = ResourceBundle.getBundle(RESOURCES_DIR + UI_STRINGS);
    FXMLLoader fxmlLoader = new FXMLLoader(classLoader.getResource(RESOURCES_DIR + MAIN_FXML), bundle);
    Parent root = fxmlLoader.load();
    controller = fxmlLoader.getController();
    Scene scene = new Scene(root);
    stage.setTitle(bundle.getString(WINDOW_TITLE));
    stage.getIcons().add(new Image(classLoader.getResourceAsStream(RESOURCES_DIR + RPS_PNG)));
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void stop() throws Exception {
    controller.stop(null);
    super.stop();
  }

  public static void main(String[] args) {
    launch(args);
  }

}
