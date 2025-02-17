package com.github.nicholasmoser.xom;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
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

  @Test
  public void testGetParentChildren() throws Exception {
    // XContainer with children defined in parent before
    List<XContainerDef> before = XomScheme.getParentClassChildrenBefore("XBitmapDescriptor", "XBaseResourceDescriptor");
    List<XContainerDef> after = XomScheme.getParentClassChildrenAfter("XBitmapDescriptor", "XBaseResourceDescriptor");
    assertThat(before.size()).isEqualTo(2);
    assertThat(before.get(0).name()).isEqualTo("ResourceId");
    assertThat(before.get(1).name()).isEqualTo("SectionId");
    assertThat(after.size()).isEqualTo(0);

    // XContainer with children defined in parent after
    before = XomScheme.getParentClassChildrenBefore("PercentButtonDesc", "MenuButtonDesc");
    after = XomScheme.getParentClassChildrenAfter("PercentButtonDesc", "MenuButtonDesc");
    assertThat(before.size()).isEqualTo(0);
    assertThat(after.size()).isEqualTo(29);
    assertThat(after.get(0).name()).isEqualTo("ResourceName");
    assertThat(after.get(1).name()).isEqualTo("HighlightedMessage");
    assertThat(after.get(2).name()).isEqualTo("SelectedMessage");
    assertThat(after.get(3).name()).isEqualTo("BorderResource");
    assertThat(after.get(4).name()).isEqualTo("BorderColour");
    assertThat(after.get(5).name()).isEqualTo("SquareButton");
    assertThat(after.get(6).name()).isEqualTo("BorderBehind");
    assertThat(after.get(7).name()).isEqualTo("HighlightSize");
    assertThat(after.get(8).name()).isEqualTo("OnRelease");
    assertThat(after.get(9).name()).isEqualTo("StartDelay");
    assertThat(after.get(28).name()).isEqualTo("ActivatedAudio");
  }
}
