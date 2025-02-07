package com.github.nicholasmoser.xom;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class XomSchemeTest {
  @Test
  public void test() throws Exception {
    List<XContainer> containers = XomScheme.get();
    Map<String, XContainer> names = new HashMap<>();
    XomScheme.fillNameMap(containers, names);
    Set<String> allAttributes = XomScheme.getAllAttributes();
    System.out.println();
  }
}
