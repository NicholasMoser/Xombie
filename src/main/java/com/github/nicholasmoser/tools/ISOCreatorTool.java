package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.Xombie;
import com.github.nicholasmoser.iso.DirectoryParser;
import com.github.nicholasmoser.iso.ISOCreator;
import com.github.nicholasmoser.iso.ISOHeader;
import com.github.nicholasmoser.utils.GUIUtils;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.stage.Stage;

public class ISOCreatorTool {

  private static final Logger LOGGER = Logger.getLogger(ISOExtractorTool.class.getName());

  /**
   * Extracts a GameCube ISO.
   */
  public static void run() {
    Optional<Path> inputPath = Choosers.getInputDirectory(Xombie.USER_HOME);
    if (inputPath.isEmpty()) {
      return;
    }
    Path iso = inputPath.get();
    Optional<Path> outputPath = Choosers.getOutputISO(iso.getParent().toFile());
    if (outputPath.isEmpty()) {
      return;
    }

    Task<Void> task = new Task<>() {
      @Override
      public Void call() {
        try {
          updateMessage("Parsing directory...");
          DirectoryParser dirParser = new DirectoryParser(inputPath.get(), true);
          updateMessage("Parsing ISO header...");
          ISOHeader isoHeader = dirParser.getISOHeader();
          updateMessage("Creating ISO creator...");
          ISOCreator creator = new ISOCreator(inputPath.get(), outputPath.get());
          updateMessage("Creating ISO...");
          creator.create(true, isoHeader);
          updateMessage("Complete");
          updateProgress(1, 1);
          return null;
        } catch (Exception e) {
          LOGGER.log(Level.SEVERE, "Error", e);
          throw new RuntimeException(e);
        }
      }
    };

    Stage loadingWindow = GUIUtils.createLoadingWindow("Creating ISO", task, 450, 200);
    task.setOnSucceeded(event -> {
      Message.info("FPK Repacked", "FPK repacking complete.");
      loadingWindow.close();
    });
    task.setOnFailed(event -> {
      Message.error("Failed to Repack FPK", "See log for more information.");
      loadingWindow.close();
    });
    new Thread(task).start();
  }
}
