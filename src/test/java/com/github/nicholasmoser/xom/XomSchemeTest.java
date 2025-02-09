package com.github.nicholasmoser.xom;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class XomSchemeTest {

  @Test
  public void test() throws Exception {
    List<XContainer> containers = XomScheme.get();
    Map<String, XContainer> names = new HashMap<>();
    XomScheme.fillNameMap(containers, names);
    Set<String> allAttributes = XomScheme.getAllAttributes();
  }

  @Test
  public void findPatterns() throws Exception {
    try (Stream<Path> walk = Files.walk(Paths.get("E:\\GNTLargeFiles\\Extracted\\Worms3D\\files"))) {
      walk.filter(Files::isRegularFile)
              .forEach(this::check);
    }
  }

  private void check(Path f) {
    if (f.getFileName().toString().endsWith(".xom")) {
      try {
        XomParser.parse(f);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
