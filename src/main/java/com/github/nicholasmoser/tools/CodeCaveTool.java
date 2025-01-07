package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.ppc.Branch;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.github.nicholasmoser.utils.GUIUtils;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class CodeCaveTool {
  private static final Logger LOGGER = Logger.getLogger(CodeCaveTool.class.getName());
  public static void run() {
    // Get the offset to where the new code cave will be written to, this will overwrite the
    // existing code here, so make sure it is unused code.
    Optional<String> codeCaveResult = getCodeCaveOffset();
    if (codeCaveResult.isEmpty()) {
      return;
    }
    int codeCaveStart;
    try {
      codeCaveStart = Long.decode(codeCaveResult.get()).intValue();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Invalid Offset", e);
      Message.error("Invalid Offset", "Please enter a valid offset in decimal or hex: " + e.getMessage());
      return;
    }
    // Get the C2 Gecko Code that writes new assembly instructions to a code location. This code
    // will be parsed to get the branch location and new lines of code.
    Optional<String> c2code = getC2Code();
    if (c2code.isEmpty()) {
      return;
    }
    byte[] bytes;
    try {
      bytes = ByteUtils.hexTextToBytes(c2code.get());
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Invalid Bytes", e);
      Message.error("Invalid Bytes", "Please enter a valid C2 Gecko Code in hex: " + e.getMessage());
      return;
    }
    // Parse the C2 code
    if (bytes[0] != (byte) 0xC2) {
      Message.error("Invalid Gecko Code", "Please provide a gecko code that starts with C2");
    }
    int branchOffset = getBranchOffset(bytes);
    byte[] codeBytes = getCodeBytes(bytes);
    if (codeBytes == null) {
      return;
    }
    // We have all the info we need now, so calculate the actual bytes to insert
    byte[] branchBytes = Branch.getBranchInstruction(branchOffset, codeCaveStart);
    byte[] leaveCodeCaveBranch = Branch.getBranchInstruction(codeCaveStart + codeBytes.length, branchOffset + 4);
    byte[] codeCaveBytes = Bytes.concat(codeBytes, leaveCodeCaveBranch);
    initDisplay(branchOffset, branchBytes, codeCaveStart, codeCaveBytes);
  }

  private static void initDisplay(int branchOffset, byte[] branchBytes, int codeCaveStart, byte[] codeCaveBytes) {
    FXMLLoader loader = new FXMLLoader(CodeCaveTool.class.getResource("codecave.fxml"));
    try {
      Scene scene = new Scene(loader.load());
      GUIUtils.initDarkMode(scene);
      CodeCaveController controller = loader.getController();
      controller.init(branchOffset, branchBytes, codeCaveStart, codeCaveBytes);
      Stage stage = new Stage();
      GUIUtils.setIcons(stage);
      stage.setScene(scene);
      stage.setTitle("Code Cave");
      stage.centerOnScreen();
      stage.show();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "GUI Failure", e);
      Message.error("GUI Failure", "Failed to open code cave window: " + e.getMessage());
    }
  }

  private static byte[] getCodeBytes(byte[] bytes) {
    try {
      ByteStream bs = new ByteStream(bytes);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      bs.seek(0x8);
      while(bs.peekWord() != 0 && bs.peekWord() != 0x60000000) {
        baos.write(bs.readNBytes(4));
      }
      return baos.toByteArray();
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Invalid Bytes", e);
      Message.error("Invalid Bytes", "Please enter a valid C2 Gecko Code in hex");
      return null;
    }
  }

  private static int getBranchOffset(byte[] bytes) {
    byte[] offset = new byte[4];
    offset[0] = (byte) 0x80;
    offset[1] = bytes[1];
    offset[2] = bytes[2];
    offset[3] = bytes[3];
    return ByteUtils.toInt32(offset);
  }

  private static Optional<String> getCodeCaveOffset() {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Code Cave Offset");
    dialog.setHeaderText("Please enter the memory address of where this code cave will go:");
    GUIUtils.setIcons(dialog);
    GUIUtils.initDarkMode(dialog.getDialogPane().getScene());
    return dialog.showAndWait();
  }

  private static Optional<String> getC2Code() {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("C2 Gecko Code");
    dialog.setHeaderText("Please enter the C2 Gecko Code to insert:");
    GUIUtils.setIcons(dialog);
    GUIUtils.initDarkMode(dialog.getDialogPane().getScene());
    return dialog.showAndWait();
  }
}
