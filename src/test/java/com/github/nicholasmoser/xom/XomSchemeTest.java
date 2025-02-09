package com.github.nicholasmoser.xom;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertSame;

public class XomSchemeTest {

  @Test
  public void testContainerRetrieval() throws Exception {
    List<XContainerDef> containers = XomScheme.getContainerDefinitions();
    Map<String, XContainerDef> names = XomScheme.getContainerNameMap();

    // Assert that the retrievals are cached
    assertSame(containers, XomScheme.getContainerDefinitions());
    assertSame(names, XomScheme.getContainerNameMap());
  }
}
