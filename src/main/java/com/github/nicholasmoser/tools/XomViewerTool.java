package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.Xombie;
import com.github.nicholasmoser.utils.GUIUtils;
import com.github.nicholasmoser.xom.Xom;
import com.github.nicholasmoser.xom.XomParser;
import com.github.nicholasmoser.xom.XomViewerController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XomViewerTool {

    private static final Logger LOGGER = Logger.getLogger(XomViewerTool.class.getName());

    public static void run() {
        Optional<Path> inputPath = Choosers.getInputXOM(Xombie.USER_HOME);
        if (inputPath.isEmpty()) {
            return;
        }
        try {
            Xom xom = XomParser.parse(inputPath.get());
            FXMLLoader loader = new FXMLLoader(XomViewerController.class.getResource("xomviewer.fxml"));
            Scene scene = new Scene(loader.load());
            GUIUtils.initDarkMode(scene);
            XomViewerController controller = loader.getController();
            Stage stage = new Stage();
            GUIUtils.setIcons(stage);
            controller.init(xom);
            stage.setScene(scene);
            stage.setTitle("XOM Viewer");
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to parse XOM", e);
            Message.error("Failed to parse XOM", "See log file for more details: " + e.getMessage());
        }
    }
}
