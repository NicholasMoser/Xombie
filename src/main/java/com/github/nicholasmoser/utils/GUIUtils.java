package com.github.nicholasmoser.utils;

import com.github.nicholasmoser.Message;
import java.awt.Desktop;
import java.net.URI;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * GUI utilities.
 */
public class GUIUtils {
  private static final Logger LOGGER = Logger.getLogger(GUIUtils.class.getName());

  public static final String FONT_SIZE_CSS = "-fx-font-size: 26px;";

  public static final String BORDER = "-fx-effect: innershadow(gaussian, #039ed3, 2, 1.0, 0, 0);";

  private static final Image WORM_16 = new Image(GUIUtils.class.getResourceAsStream("worm16.png"));

  private static final Image WORM_32 = new Image(GUIUtils.class.getResourceAsStream("worm32.png"));

  private static final Image WORM_64 = new Image(GUIUtils.class.getResourceAsStream("worm64.png"));

  private static final Image WORM_128 =
      new Image(GUIUtils.class.getResourceAsStream("worm128.png"));

  /**
   * Creates a new loading window for a specified task with the default window size.
   * 
   * @param title The title of the window.
   * @param task The task to perform.
   * @return The loading window.
   */
  public static Stage createLoadingWindow(String title, Task<?> task) {
    return createLoadingWindow(title, task, 450, 200);
  }

  /**
   * Browse the given UI in a separate task. This is done to provide compatibility with both
   * Windows and Linux, due to weirdness with mixing java.awt.Desktop with JavaFX.
   *
   * @param uri The URI to browse to.
   */
  public static void browse(String uri) {
    Task<Void> task = new Task<>() {
      @Override
      public Void call() throws Exception {
        Desktop.getDesktop().browse(new URI(uri));
        return null;
      }
    };
    task.exceptionProperty().addListener((observable,oldValue, e) -> {
      if (e!=null){
        LOGGER.log(Level.SEVERE, "Error Opening URI", e);
        Message.error("Error Opening URI", e.getMessage());
      }
    });
    new Thread(task).start();
  }

  /**
   * Open the given file path in a separate task. This is done to provide compatibility with both
   * Windows and Linux, due to weirdness with mixing java.awt.Desktop with JavaFX.
   *
   * @param filePath The file path to open.
   */
  public static void open(Path filePath) {
    Task<Void> task = new Task<>() {
      @Override
      public Void call() throws Exception {
        Desktop.getDesktop().open(filePath.toFile());
        return null;
      }
    };
    task.exceptionProperty().addListener((observable, oldValue, e) -> {
      if (e != null) {
        LOGGER.log(Level.SEVERE, "Error Opening File", e);
        Message.error("Error Opening File", e.getMessage());
      }
    });
    new Thread(task).start();
  }

  /**
   * Creates a new loading window for a specified task with the given size.
   *
   * @param title The title of the window.
   * @param task The task to perform.
   * @param width The width of the window.
   * @param height The height of the window.
   * @return The loading window.
   */
  public static Stage createLoadingWindow(String title, Task<?> task, double width, double height) {
    Stage loadingWindow = new Stage();
    loadingWindow.initModality(Modality.APPLICATION_MODAL);
    loadingWindow.initStyle(StageStyle.UNDECORATED);
    loadingWindow.setTitle(title);
    GUIUtils.setIcons(loadingWindow);

    GridPane flow = new GridPane();
    flow.setAlignment(Pos.CENTER);
    flow.setVgap(20);

    Text text = new Text();
    text.setStyle(FONT_SIZE_CSS);

    ProgressIndicator progressIndicator = new ProgressIndicator(-1.0f);

    GridPane.setHalignment(text, HPos.CENTER);
    GridPane.setHalignment(progressIndicator, HPos.CENTER);
    flow.add(text, 0, 0);
    flow.add(progressIndicator, 0, 1);
    flow.setStyle(BORDER);

    Scene dialogScene = new Scene(flow, width, height);
    loadingWindow.setScene(dialogScene);
    loadingWindow.show();

    progressIndicator.progressProperty().bind(task.progressProperty());
    text.textProperty().bind(task.messageProperty());

    return loadingWindow;
  }

  /**
   * Sets the application icons on the stage.
   * 
   * @param primaryStage The primary stage to set the icons for.
   */
  public static void setIcons(Stage primaryStage) {
    ObservableList<Image> icons = primaryStage.getIcons();
    icons.add(WORM_16);
    icons.add(WORM_32);
    icons.add(WORM_64);
    icons.add(WORM_128);
  }

  /**
   * Sets the application icons on the alert.
   *
   * @param alert The alert to set the icons for.
   */
  public static void setIcons(Alert alert) {
    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    ObservableList<Image> icons = stage.getIcons();
    icons.add(WORM_16);
    icons.add(WORM_32);
    icons.add(WORM_64);
    icons.add(WORM_128);
  }

  /**
   * Sets the application icons on the dialog.
   *
   * @param dialog The dialog to set the icons for.
   */
  public static void setIcons(Dialog dialog) {
    Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
    ObservableList<Image> icons = stage.getIcons();
    icons.add(WORM_16);
    icons.add(WORM_32);
    icons.add(WORM_64);
    icons.add(WORM_128);
  }

  /**
   * Gets a node at a specified row and column in a GridPane.
   * https://stackoverflow.com/questions/20655024/javafx-gridpane-retrieve-specific-cell-content/20656861#20656861
   *
   * @param gridPane The GridPane to search.
   * @param col The column of the node.
   * @param row The row of the node.
   * @return The Optional node requested.
   */
  public static Optional<Node> getNodeFromGridPane(GridPane gridPane, int col, int row) {
    for (Node node : gridPane.getChildren()) {
      if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
        return Optional.of(node);
      }
    }
    return Optional.empty();
  }

  /**
   * @return If the platform this is running on is Windows.
   */
  public static boolean isWindows() {
    return System.getProperty("os.name").startsWith("Windows");
  }

  public static void initDarkMode(Scene scene) {
    scene.getStylesheets().add(GUIUtils.class.getResource("stylesheet.css").toExternalForm());
  }
}
