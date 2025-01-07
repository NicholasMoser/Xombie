package com.github.nicholasmoser;

import com.github.nicholasmoser.utils.GUIUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * A tool that allows you to modify files in Worms 3D ISO file.
 * 
 * @author Nicholas Moser
 */
public class Xombie extends Application {

  public static final File USER_HOME = new File(System.getProperty("user.home"));

  private static final Logger LOGGER = Logger.getLogger(Xombie.class.getName());

  @Override
  public void start(Stage primaryStage) {
    LOGGER.info("Application has started.");
    setLoggingProperties();
    createGUI(primaryStage);
  }

  /**
   * Creates the GUI for the application.
   * 
   * @param primaryStage The stage to use.
   */
  private void createGUI(Stage primaryStage) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("tools.fxml"));
      Scene scene = new Scene(loader.load());
      GUIUtils.initDarkMode(scene);
      ToolController controller = loader.getController();
      controller.init();
      GUIUtils.setIcons(primaryStage);
      primaryStage.setScene(scene);
      primaryStage.setTitle("Xombie");
      primaryStage.centerOnScreen();
      primaryStage.show();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Launch", e);
      Message.error("Failed to Launch", e.getMessage());
    }
  }

  /**
   * Sets the custom logging properties from the logging.properties included resource file.
   */
  private void setLoggingProperties() {
    try (InputStream properties = getClass().getResourceAsStream("logging.properties")) {
      LogManager.getLogManager().readConfiguration(properties);
    } catch (SecurityException | IOException e) {
      LOGGER.log(Level.SEVERE, "Unable to load logging.properties", e);
      Message.error("Logging Error", "Unable to load logging.properties");
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
