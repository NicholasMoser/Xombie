package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.dol.DolUtil;
import com.github.nicholasmoser.utils.GUIUtils;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextInputDialog;

public class Ram2DolTool {
  private static final Logger LOGGER = Logger.getLogger(Ram2DolTool.class.getName());

  public static void run() {
    Optional<String> ramResult = getRamAddress();
    if (ramResult.isEmpty()) {
      return;
    }
    long ramAddress;
    try {
      ramAddress = Long.decode(ramResult.get());
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Invalid Offset", e);
      Message.error("Invalid Offset", "Please enter a valid offset in decimal or hex: " + e.getMessage());
      return;
    }
    long offset = DolUtil.ram2dol(ramAddress);
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Dol Offset");
    dialog.setHeaderText("Dol Offset:");
    dialog.getEditor().setText(String.format("0x%X", offset));
    GUIUtils.setIcons(dialog);
    GUIUtils.initDarkMode(dialog.getDialogPane().getScene());
    dialog.showAndWait();
  }

  private static Optional<String> getRamAddress() {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Insert Ram Address");
    dialog.setHeaderText("Please enter the ram address to convert to a dol address:");
    GUIUtils.setIcons(dialog);
    GUIUtils.initDarkMode(dialog.getDialogPane().getScene());
    return dialog.showAndWait();
  }
}
