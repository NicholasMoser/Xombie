package com.github.nicholasmoser.xom;

import com.github.nicholasmoser.xom.ctnr.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class XomParserTest {
    @Test
    public void testSpecificXomFile() throws Exception {
        Path f = Paths.get("E:\\GNTLargeFiles\\Extracted\\Worms3D\\files\\Bundles\\Bundle00.xom");
        XomParser.parse(f);
    }

    @Test
    public void testAllXomFiles() throws Exception {
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
                throw new RuntimeException("Failed to parse " + f, e);
            }
        }
    }

    @Test
    public void testAITwk() throws Exception {
        Path f = Paths.get("E:\\GNTLargeFiles\\Extracted\\Worms3D\\files\\AITwk.xom");
        Xom xom = XomParser.parse(f);

        // Check header
        XomHeader header = xom.header();
        assertThat(header.flag()).isEqualTo(2);
        assertThat(header.numberOfTypes()).isEqualTo(6);
        assertThat(header.maxCount()).isEqualTo(50);
        assertThat(header.rootCount()).isEqualTo(50);

        // Check types
        List<XomType> types = xom.types();
        assertThat(types.size()).isEqualTo(6);
        XomType first = types.get(0);
        assertThat(first.subType()).isEqualTo(0);
        assertThat(first.size()).isEqualTo(0);
        assertThat(first.name()).isEqualTo("XContainer");
        assertThat(first.guid()).isEqualTo("5E1CD446-48A3-44FE-A55A-E247B8F5E713");
        XomType second = types.get(1);
        assertThat(second.subType()).isEqualTo(0);
        assertThat(second.name()).isEqualTo("AIParametersContainer");
        assertThat(second.guid()).isEqualTo("6A0DEE10-2E94-4844-B2D4-6C39CA979C90");
        XomType third = types.get(2);
        assertThat(third.subType()).isEqualTo(0);
        assertThat(third.name()).isEqualTo("XResourceDetails");
        assertThat(third.guid()).isEqualTo("E77556F2-A495-491A-8563-006CE2577244");
        XomType fourth = types.get(3);
        assertThat(fourth.subType()).isEqualTo(0);
        assertThat(fourth.name()).isEqualTo("XUintResourceDetails");
        assertThat(fourth.guid()).isEqualTo("93E5117C-8ADA-4AFB-B17A-668D0BDD15E6");
        XomType fifth = types.get(4);
        assertThat(fifth.subType()).isEqualTo(0);
        assertThat(fifth.name()).isEqualTo("XContainerResourceDetails");
        assertThat(fifth.guid()).isEqualTo("E0B6BF20-F80B-4329-B6EB-1998ED8AEC1F");
        XomType sixth = types.get(5);
        assertThat(sixth.subType()).isEqualTo(0);
        assertThat(sixth.name()).isEqualTo("XDataBank");
        assertThat(sixth.guid()).isEqualTo("93E661AC-D941-442A-A18E-E99B795879DC");

        // Check string table
        StringTable stringTable = xom.stringTable();
        assertThat(stringTable.lenStrs()).isEqualTo(391);
        assertThat(stringTable.sizeStrs()).isEqualTo(26);
        List<Integer> offsets = stringTable.offsets();
        List<Integer> expected = List.of(0, 376, 1, 15, 29, 43, 57, 71, 248, 264, 280, 296, 312, 328, 344, 360, 88, 104, 120, 136, 152, 168, 184, 200, 216, 232);
        assertThat(offsets).isEqualTo(expected);
        assertThat(stringTable.offsetToStr().size()).isEqualTo(26);
        assertThat(stringTable.getString(0)).isEqualTo("");
        assertThat(stringTable.getString(1)).isEqualTo("TwkEdVer.AITWK");
        assertThat(stringTable.getString(2)).isEqualTo("AIParams.CPU1");
        assertThat(stringTable.getString(3)).isEqualTo("AIParams.CPU2");
        assertThat(stringTable.getString(4)).isEqualTo("AIParams.CPU3");
        assertThat(stringTable.getString(5)).isEqualTo("AIParams.CPU4");
        assertThat(stringTable.getString(6)).isEqualTo("AIParams.CPU5");
        assertThat(stringTable.getString(7)).isEqualTo("AIParams.CPUTest");
        assertThat(stringTable.getString(8)).isEqualTo("AIParams.Worm10");
        assertThat(stringTable.getString(9)).isEqualTo("AIParams.Worm11");
        assertThat(stringTable.getString(10)).isEqualTo("AIParams.Worm12");
        assertThat(stringTable.getString(11)).isEqualTo("AIParams.Worm13");
        assertThat(stringTable.getString(12)).isEqualTo("AIParams.Worm14");
        assertThat(stringTable.getString(13)).isEqualTo("AIParams.Worm15");
        assertThat(stringTable.getString(14)).isEqualTo("AIParams.Worm16");
        assertThat(stringTable.getString(15)).isEqualTo("AIParams.Worm17");
        assertThat(stringTable.getString(16)).isEqualTo("AIParams.Worm00");
        assertThat(stringTable.getString(17)).isEqualTo("AIParams.Worm01");
        assertThat(stringTable.getString(18)).isEqualTo("AIParams.Worm02");
        assertThat(stringTable.getString(19)).isEqualTo("AIParams.Worm03");
        assertThat(stringTable.getString(20)).isEqualTo("AIParams.Worm04");
        assertThat(stringTable.getString(21)).isEqualTo("AIParams.Worm05");
        assertThat(stringTable.getString(22)).isEqualTo("AIParams.Worm06");
        assertThat(stringTable.getString(23)).isEqualTo("AIParams.Worm07");
        assertThat(stringTable.getString(24)).isEqualTo("AIParams.Worm08");
        assertThat(stringTable.getString(25)).isEqualTo("AIParams.Worm09");

        // Check containers
        List<XContainer> containers = xom.containers();
        assertThat(containers.size()).isEqualTo(50);
        // First container
        XContainer firstAI = containers.get(0);
        assertThat(firstAI.name()).isEqualTo("AIParametersContainer");
        List<Value> values = firstAI.values();
        XFloat floatVal = (XFloat) values.get(0);
        assertThat(floatVal.name()).isEqualTo("WeightingThisWormValue");
        assertThat(floatVal.value()).isEqualTo(1.0f);
        floatVal = (XFloat) values.get(2);
        assertThat(floatVal.name()).isEqualTo("WeightingWormHealth");
        assertThat(floatVal.value()).isEqualTo(0.04f);
        XBool boolVal = (XBool) values.get(39);
        assertThat(boolVal.name()).isEqualTo("AllowCollectNormalCrate");
        assertThat(boolVal.value()).isEqualTo(true);
        XString stringVal = (XString) values.get(46);
        assertThat(stringVal.name()).isEqualTo("TargetDetailObject");
        assertThat(stringVal.value()).isEqualTo("");
        XFloat lastVal = (XFloat) values.get(values.size() - 1);
        assertThat(lastVal.name()).isEqualTo("PrefWeaponAnimal");
        assertThat(lastVal.value()).isEqualTo(1.0f);
        // First XContainerResourceDetails
        XContainer resourceContainer = containers.get(25);
        assertThat(resourceContainer.name()).isEqualTo("XContainerResourceDetails");
        List<Value> resourceValues = resourceContainer.values();
        XUInt8 containerIndex = (XUInt8) resourceValues.get(0);
        assertThat(containerIndex.name()).isEqualTo("ContainerIndex");
        assertThat(containerIndex.value()).isEqualTo(1);
        XString name = (XString) resourceValues.get(1);
        assertThat(name.name()).isEqualTo("Name");
        assertThat(name.value()).isEqualTo("AIParams.CPU1");
        XUInt flag = (XUInt) resourceValues.get(2);
        assertThat(flag.name()).isEqualTo("Flag");
        assertThat(flag.value()).isEqualTo(17);
        // Last container
        XContainer last = containers.get(containers.size() - 1);
        assertThat(last.name()).isEqualTo("XDataBank");
        List<Value> lastValues = last.values();
        assertThat(lastValues.size()).isEqualTo(34);
        XUInt8 section = (XUInt8) lastValues.get(0);
        assertThat(section.name()).isEqualTo("Section");
        assertThat(section.value()).isEqualTo(0);
        XUInt8 containerResources = (XUInt8) lastValues.get(7);
        assertThat(containerResources.name()).isEqualTo("ContainerResources");
        assertThat(containerResources.value()).isEqualTo(24);
        containerIndex = (XUInt8) lastValues.get(8);
        assertThat(containerIndex.name()).isEqualTo("ContainerIndex");
        assertThat(containerIndex.value()).isEqualTo(26);
    }
}
