package com.github.nicholasmoser;

import com.github.nicholasmoser.tools.CodeCaveTool;
import com.github.nicholasmoser.tools.FSTCompareTool;
import com.github.nicholasmoser.tools.ISOCreatorTool;
import com.github.nicholasmoser.tools.ISOExtractorTool;
import com.github.nicholasmoser.tools.Ram2DolTool;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * UI controller for the list of tools.
 */
public class ToolController {

  private static final Logger LOGGER = Logger.getLogger(ToolController.class.getName());
  private static final String ISO_EXTRACTOR_GC = "ISO Extractor (GameCube)";
  private static final String ISO_CREATOR_GC = "ISO Creator (GameCube)";
  private static final String CODE_CAVE_TOOL = "Code Cave Tool";
  private static final String FILE_SYSTEM_TABLE_COMPARE = "File System Table Compare";
  private static final String RAM2DOL = "Ram Address to Dol Offset";

  @FXML
  private ListView<String> tools;

  /**
   * Initialize the list of tools.
   */
  public void init() {
    List<String> items = tools.getItems();
    items.add(ISO_EXTRACTOR_GC);
    items.add(ISO_CREATOR_GC);
    items.add(CODE_CAVE_TOOL);
    items.add(FILE_SYSTEM_TABLE_COMPARE);
    items.add(RAM2DOL);
  }

  @FXML
  protected void toolSelected(MouseEvent mouseEvent) {
    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
      if(mouseEvent.getClickCount() == 2) {
        EventTarget target =  mouseEvent.getTarget();
        if (target instanceof Labeled label) {
          runTool(label.getText());
        }
        else if (target instanceof Text text) {
          runTool(text.getText());
        }
      }
    }
  }

  /**
   * Runs the specified tool by name.
   *
   * @param tool The tool name.
   */
  private void runTool(String tool) {
    try {
      switch (tool) {
        case ISO_EXTRACTOR_GC -> ISOExtractorTool.extractGameCubeISO();
        case ISO_CREATOR_GC -> ISOCreatorTool.run();
        case CODE_CAVE_TOOL -> CodeCaveTool.run();
        case FILE_SYSTEM_TABLE_COMPARE -> FSTCompareTool.run();
        case RAM2DOL -> Ram2DolTool.run();
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "An error was encountered when running the tool.", e);
    }
  }
}
