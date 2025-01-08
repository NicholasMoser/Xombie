package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.Xombie;
import com.github.nicholasmoser.iso.DirectoryParser;
import com.github.nicholasmoser.iso.FileSystemTable;
import com.github.nicholasmoser.iso.ISOCreator;
import com.github.nicholasmoser.iso.ISOFile;
import com.github.nicholasmoser.iso.ISOHeader;
import com.github.nicholasmoser.iso.ISOItem;
import com.github.nicholasmoser.utils.GUIUtils;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FSTCompareTool {

  private static final Logger LOGGER = Logger.getLogger(FSTCompareTool.class.getName());
  public static void run() {
    Optional<Path> first = Choosers.getInputDirectory(Xombie.USER_HOME);
    if (first.isEmpty()) {
      return;
    }
    Optional<Path> second = Choosers.getInputDirectory(Xombie.USER_HOME);
    if (second.isEmpty()) {
      return;
    }


    Task<String> task = new Task<>() {
      @Override
      public String call() {
        try {
          updateMessage("Parsing first...");
          List<ISOItem> firstItems = FileSystemTable.read(first.get().resolve("sys/fst.bin"));
          updateMessage("Parsing second...");
          List<ISOItem> secondItems = FileSystemTable.read(second.get().resolve("sys/fst.bin"));
          updateMessage("Comparing files...");

          // Manipulate list to other lists
          List<String> firstNames = firstItems.stream().map(ISOItem::getGamePath).toList();
          List<String> secondNames = secondItems.stream().map(ISOItem::getGamePath).toList();
          List<ISOFile> firstFiles = firstItems.stream()
              .filter(item -> item instanceof ISOFile)
              .map(item -> (ISOFile) item)
              .toList();
          List<ISOFile> secondFiles = secondItems.stream()
              .filter(item -> item instanceof ISOFile)
              .map(item -> (ISOFile) item)
              .toList();
          List<ISOFile> firstSorted = new ArrayList<>(firstFiles);
          List<ISOFile> secondSorted = new ArrayList<>(secondFiles);
          firstSorted.sort(Comparator.comparing(ISOFile::getPos));
          secondSorted.sort(Comparator.comparing(ISOFile::getPos));

          // Get a few random useful items
          ISOFile firstStarting = firstFiles.stream().min(Comparator.comparing(ISOFile::getPos)).get();
          ISOFile secondStarting = secondFiles.stream().min(Comparator.comparing(ISOFile::getPos)).get();
          ISOFile firstEnding = firstFiles.stream().max(Comparator.comparing(ISOFile::getPos)).get();
          ISOFile secondEnding = secondFiles.stream().max(Comparator.comparing(ISOFile::getPos)).get();

          // Create text comparison
          StringBuilder result = new StringBuilder("File position order:\n");
          result.append(firstSorted).append('\n');
          result.append(secondSorted).append('\n');
          result.append("First file count: ").append(firstItems.size()).append('\n');
          result.append("Second file count: ").append(secondItems.size()).append('\n');
          if (firstItems.size() == secondItems.size()) {

            for (int i = 0; i < secondItems.size(); i++) {
              ISOItem firstItem = firstItems.get(i);
              ISOItem secondItem = secondItems.get(i);
              if (firstItem instanceof ISOFile firstFile && secondItem instanceof ISOFile secondFile) {
                result.append(firstFile.getGamePath()).append('\n');
                if (firstFile.getLen() != secondFile.getLen()) {
                  result.append(String.format("  Different lengths: 0x%X vs 0x%X\n", firstFile.getLen(), secondFile.getLen()));
                }
                if (firstFile.getPos() != secondFile.getPos()) {
                  result.append(String.format("  Different position: 0x%X vs 0x%X\n", firstFile.getPos(), secondFile.getPos()));
                }
              }
            }
            result.append(String.format("First dir starting file at offset 0x%X: %s\n", firstStarting.getPos(), firstStarting.getGamePath()));
            result.append(String.format("First dir ending file at offset 0x%X: %s\n", firstEnding.getPos(), firstEnding.getGamePath()));
            result.append(String.format("Second dir starting file at offset 0x%X: %s\n", secondStarting.getPos(), secondStarting.getGamePath()));
            result.append(String.format("Second dir ending file at offset 0x%X: %s\n", secondEnding.getPos(), secondEnding.getGamePath()));
          }
          updateMessage("Complete");
          updateProgress(1, 1);
          return result.toString();
        } catch (Exception e) {
          LOGGER.log(Level.SEVERE, "Error", e);
          throw new RuntimeException(e);
        }
      }
    };

    Stage loadingWindow = GUIUtils.createLoadingWindow("Parsing Directories", task, 450, 200);
      task.setOnSucceeded(event -> {
        Message.info("FST parsing complete", "FST parsing complete");
        loadingWindow.close();
        showResult((String) event.getSource().getValue());
      });
    task.setOnFailed(event -> {
      Message.error("Failed to parse FST", "See log for more information.");
      loadingWindow.close();
    });
    new Thread(task).start();
  }

  private static void showResult(String result) {
    // Create a new Stage (window)
    Stage newWindow = new Stage();
    newWindow.setTitle("File System Table Diff");

    // Create a TextArea
    TextArea textArea = new TextArea();
    textArea.setText(result);

    // Set up the layout
    StackPane layout = new StackPane(textArea);

    // Create a scene and set it to the Stage
    Scene scene = new Scene(layout, 800, 600);
    newWindow.setScene(scene);

    // Show the new window
    newWindow.show();
  }
}
