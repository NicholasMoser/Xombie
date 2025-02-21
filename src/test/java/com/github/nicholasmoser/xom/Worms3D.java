package com.github.nicholasmoser.xom;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Worms3D {
    public static Path dir() {
        Path path = Paths.get("E:\\GNTLargeFiles\\Extracted\\Worms3D");
        if (!Files.isDirectory(path)) {
            throw new IllegalStateException("Replace the above path with your own Worms3D GameCube path");
        }
        return path;
    }
}
