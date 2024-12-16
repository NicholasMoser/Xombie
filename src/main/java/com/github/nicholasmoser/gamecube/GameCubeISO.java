package com.github.nicholasmoser.gamecube;

import com.github.nicholasmoser.iso.ISOCreator;
import com.github.nicholasmoser.iso.ISOExtractor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * Utility to access GCRebuilder.exe through the command line.
 */
public class GameCubeISO {

  private static final Logger LOGGER = Logger.getLogger(GameCubeISO.class.getName());

  /**
   * Export the files contained in the given ISO to the given output directory.
   *
   * @param inputFile The given input ISO.
   * @param outputDirectory The given output directory to export to.
   * @throws IOException If an I/O error occurs
   */
  public static void exportFiles(Path inputFile, Path outputDirectory) throws IOException {
    if (inputFile == null || !Files.isRegularFile(inputFile)) {
      throw new IllegalArgumentException(inputFile + " is null or not a file.");
    }
    if (outputDirectory == null || !Files.isDirectory(outputDirectory)) {
      throw new IllegalArgumentException(outputDirectory + " is null or not a directory.");
    }
    LOGGER.info("Exporting files...");
    ISOExtractor extractor = new ISOExtractor(inputFile, outputDirectory);
    extractor.extract();
    LOGGER.info("Finished exporting files.");
  }

  /**
   * Import the files contained in the given directory to the given output ISO file.
   *
   * @param inputDirectory The directory to import files from.
   * @param outputISO The output ISO to import files into.
   * @param pushFilesToEnd If the files should be pushed to the end of the ISO.
   * @throws IOException If an I/O error occurs
   */
  public static void importFiles(Path inputDirectory, Path outputISO, boolean pushFilesToEnd) throws IOException {
    LOGGER.info("Importing files...");
    ISOCreator creator = new ISOCreator(inputDirectory, outputISO);
    creator.create(pushFilesToEnd);
    LOGGER.info("Finished importing files.");
  }
}
