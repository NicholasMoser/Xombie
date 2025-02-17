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
        Path f = Paths.get("E:\\GNTLargeFiles\\Extracted\\Worms3D\\files\\Bundles\\Bundle02.xom");
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
    public void testBundle02() throws Exception {
        Path f = Paths.get("E:\\GNTLargeFiles\\Extracted\\Worms3D\\files\\Bundles\\Bundle02.xom");
        Xom xom = XomParser.parse(f);

        // Check header
        XomHeader header = xom.header();
        assertThat(header.flag()).isEqualTo(2);
        assertThat(header.numberOfTypes()).isEqualTo(18);
        assertThat(header.maxCount()).isEqualTo(23);
        assertThat(header.rootCount()).isEqualTo(5);

        // Check types
        List<XomType> types = xom.types();
        assertThat(types.size()).isEqualTo(18);
        assertThat(types.get(0).subType()).isEqualTo(0);
        assertThat(types.get(0).size()).isEqualTo(4);
        assertThat(types.get(0).name()).isEqualTo("XBitmapDescriptor");
        assertThat(types.get(0).guid()).isEqualTo("87CF794F-A2AA-4C44-A9DB-7C9091245CB3");
        assertThat(types.get(1).subType()).isEqualTo(0);
        assertThat(types.get(1).size()).isEqualTo(0);
        assertThat(types.get(1).name()).isEqualTo("XBaseResourceDescriptor");
        assertThat(types.get(1).guid()).isEqualTo("4DCD7EFF-E238-4182-8828-CD940A39D137");
        assertThat(types.get(2).subType()).isEqualTo(0);
        assertThat(types.get(2).size()).isEqualTo(1);
        assertThat(types.get(2).name()).isEqualTo("XGraphSet");
        assertThat(types.get(2).guid()).isEqualTo("64BF3D0B-3941-40BB-B179-8F882D14449B");
        assertThat(types.get(3).subType()).isEqualTo(0);
        assertThat(types.get(3).size()).isEqualTo(0);
        assertThat(types.get(3).name()).isEqualTo("XShader");
        assertThat(types.get(3).guid()).isEqualTo("58A4C3A0-8C4D-4E94-AE28-975CC0D246E0");
        assertThat(types.get(4).subType()).isEqualTo(0);
        assertThat(types.get(4).size()).isEqualTo(0);
        assertThat(types.get(4).name()).isEqualTo("XSimpleShader");
        assertThat(types.get(4).guid()).isEqualTo("21BE2651-4990-41C0-A528-E1562E8F1FD0");
        assertThat(types.get(17).subType()).isEqualTo(0);
        assertThat(types.get(17).size()).isEqualTo(0);
        assertThat(types.get(17).name()).isEqualTo("XContainer");
        assertThat(types.get(17).guid()).isEqualTo("5E1CD446-48A3-44FE-A55A-E247B8F5E713");

        // Check string table
        StringTable stringTable = xom.stringTable();
        assertThat(stringTable.lenStrs()).isEqualTo(121);
        assertThat(stringTable.sizeStrs()).isEqualTo(9);
        assertThat(stringTable.offsetToStr().size()).isEqualTo(9);
        assertThat(stringTable.getString(0)).isEqualTo("");
        assertThat(stringTable.getString(1)).isEqualTo("NGC.MCIcon0");
        assertThat(stringTable.getString(8)).isEqualTo("Logos/NGCMC03.tga");

        // Check containers
        List<XContainer> containers = xom.containers();
        assertThat(containers.size()).isEqualTo(23);
        // First container
        XContainerGeneric first = (XContainerGeneric) containers.get(0);
        assertThat(first.name()).isEqualTo("XBitmapDescriptor");
        assertThat(first.values().size()).isEqualTo(5);
        XString resourceId = (XString) first.values().get(0);
        assertThat(resourceId.name()).isEqualTo("ResourceId");
        assertThat(resourceId.value()).isEqualTo("NGC.MCIcon0");
        XUInt16 imageWidth = (XUInt16) first.values().get(3);
        assertThat(imageWidth.name()).isEqualTo("ImageWidth");
        assertThat(imageWidth.value()).isEqualTo(32);

        // XImage resource
        XContainerGeneric xImage = (XContainerGeneric) containers.get(15);
        assertThat(xImage.name()).isEqualTo("XImage");
        XString name = (XString) xImage.values().get(0);
        assertThat(name.name()).isEqualTo("Name");
        assertThat(name.value()).isEqualTo("Logos/NGCMC00.tga");
        XCollection strides = (XCollection) xImage.values().get(5);
        assertThat(strides.name()).isEqualTo("Strides");
        assertThat(strides.values().size()).isEqualTo(1);
        XEnum format = (XEnum) xImage.values().get(7);
        assertThat(format.name()).isEqualTo("Format");
        assertThat(format.value()).isEqualTo(1);
        assertThat(format.mappedValue()).isEqualTo("kImageFormat_A8R8G8B8");
        XCollection data = (XCollection) xImage.values().get(8);
        assertThat(data.values().size()).isEqualTo(4096);
        XByte firstByte = (XByte) data.values().get(0);
        assertThat(firstByte.value()).isEqualTo((byte) 0x6C);

        // Last XContainer, XOglTextureMap
        XContainerGeneric xOgl = (XContainerGeneric) containers.get(22);
        assertThat(xOgl.name()).isEqualTo("XOglTextureMap");
        XEnum blend = (XEnum) xOgl.values().get(0);
        assertThat(blend.name()).isEqualTo("Blend");
        assertThat(blend.value()).isEqualTo(1);
        assertThat(blend.mappedValue()).isEqualTo("kOglBlendModulate");
        Ref texture = (Ref) xOgl.values().get(2);
        assertThat(texture.name()).isEqualTo("Texture");
        assertThat(texture.value()).isEqualTo(19);
        XEnum addressModeS = (XEnum) xOgl.values().get(6);
        assertThat(addressModeS.name()).isEqualTo("AddressModeS");
        assertThat(addressModeS.value()).isEqualTo(1);
        assertThat(addressModeS.mappedValue()).isEqualTo("kAddressModeRepeat");
        XEnum magFilter = (XEnum) xOgl.values().get(8);
        assertThat(magFilter.name()).isEqualTo("MagFilter");
        assertThat(magFilter.value()).isEqualTo(2);
        assertThat(magFilter.mappedValue()).isEqualTo("kFilterModeLinear");
        XEnum mipFilter = (XEnum) xOgl.values().get(10);
        assertThat(mipFilter.name()).isEqualTo("MipFilter");
        assertThat(mipFilter.value()).isEqualTo(0);
        assertThat(mipFilter.mappedValue()).isEqualTo("kFilterModeNone");

        // Verify they are all XContainers
        for (XContainer child : containers) {
            assertThat(child).isOfAnyClassIn(XContainerGeneric.class);
        }

        // Check the name of a few of them
        assertContainerName(containers.get(0), "XBitmapDescriptor");
        assertContainerName(containers.get(3), "XBitmapDescriptor");
        assertContainerName(containers.get(4), "XGraphSet");
        assertContainerName(containers.get(5), "XTexFont");
        assertContainerName(containers.get(8), "XTexFont");
        assertContainerName(containers.get(9), "XAlphaTest");
        assertContainerName(containers.get(10), "XZBufferWriteEnable");
        assertContainerName(containers.get(11), "XDepthTest");
        assertContainerName(containers.get(12), "XCullFace");
        assertContainerName(containers.get(13), "XLightingEnable");
        assertContainerName(containers.get(14), "XBlendModeGL");
        assertContainerName(containers.get(15), "XImage");
        assertContainerName(containers.get(18), "XImage");
        assertContainerName(containers.get(19), "XOglTextureMap");
        assertContainerName(containers.get(22), "XOglTextureMap");
    }

    @Test
    public void testCamTwk() throws Exception {
        Path f = Paths.get("E:\\GNTLargeFiles\\Extracted\\Worms3D\\files\\CamTwk.xom");
        Xom xom = XomParser.parse(f);

        // Check header
        XomHeader header = xom.header();
        assertThat(header.flag()).isEqualTo(2);
        assertThat(header.numberOfTypes()).isEqualTo(13);
        assertThat(header.maxCount()).isEqualTo(100);
        assertThat(header.rootCount()).isEqualTo(100);

        // Check types
        List<XomType> types = xom.types();
        assertThat(types.size()).isEqualTo(13);
        assertThat(types.get(0).subType()).isEqualTo(0);
        assertThat(types.get(0).size()).isEqualTo(0);
        assertThat(types.get(0).name()).isEqualTo("XContainer");
        assertThat(types.get(0).guid()).isEqualTo("5E1CD446-48A3-44FE-A55A-E247B8F5E713");
        assertThat(types.get(1).subType()).isEqualTo(0);
        assertThat(types.get(1).size()).isEqualTo(4);
        assertThat(types.get(1).name()).isEqualTo("TrackCameraContainer");
        assertThat(types.get(1).guid()).isEqualTo("E746407C-AB92-4641-A171-880E86B8602C");
        assertThat(types.get(2).subType()).isEqualTo(0);
        assertThat(types.get(2).size()).isEqualTo(3);
        assertThat(types.get(2).name()).isEqualTo("OccludingCameraPropertiesContai");
        assertThat(types.get(2).guid()).isEqualTo("51D74BFA-CA52-41A3-ABCB-A0D0F330B512");
        assertThat(types.get(3).subType()).isEqualTo(0);
        assertThat(types.get(3).size()).isEqualTo(5);
        assertThat(types.get(3).name()).isEqualTo("ChaseCameraPropertiesContainer");
        assertThat(types.get(3).guid()).isEqualTo("A6FE8A24-4082-4782-8C5A-798A695CC732");
        assertThat(types.get(4).subType()).isEqualTo(0);
        assertThat(types.get(4).size()).isEqualTo(3);
        assertThat(types.get(4).name()).isEqualTo("FlyCameraPropertiesContainer");
        assertThat(types.get(4).guid()).isEqualTo("90B14B7E-56D2-4E88-93E6-CD92007A486D");
        assertThat(types.get(12).subType()).isEqualTo(0);
        assertThat(types.get(12).size()).isEqualTo(1);
        assertThat(types.get(12).name()).isEqualTo("XDataBank");
        assertThat(types.get(12).guid()).isEqualTo("93E661AC-D941-442A-A18E-E99B795879DC");

        // Check string table
        StringTable stringTable = xom.stringTable();
        assertThat(stringTable.lenStrs()).isEqualTo(1995);
        assertThat(stringTable.sizeStrs()).isEqualTo(86);
        assertThat(stringTable.offsetToStr().size()).isEqualTo(86);
        assertThat(stringTable.getString(0)).isEqualTo("");
        assertThat(stringTable.getString(1)).isEqualTo("Camera.Shake.Length");
        assertThat(stringTable.getString(85)).isEqualTo("FallCamera");

        // Check containers
        List<XContainer> containers = xom.containers();
        assertThat(containers.size()).isEqualTo(100);
        // First container
        XContainerGeneric first = (XContainerGeneric) containers.get(0);
        assertThat(first.name()).isEqualTo("TrackCameraContainer");
        assertThat(first.values().size()).isEqualTo(8);
        XFloat lookSpeed = (XFloat) first.values().get(1);
        assertThat(lookSpeed.name()).isEqualTo("LookSpeed");
        assertThat(lookSpeed.value()).isEqualTo(0.1f);
        XCollection viewPoints = (XCollection) first.values().get(7);
        assertThat(viewPoints.name()).isEqualTo("ViewPoint");
        assertThat(viewPoints.values().size()).isEqualTo(11);
        Tuple firstTuple = (Tuple) viewPoints.values().get(0);
        assertThat(firstTuple.name()).isEqualTo("ViewPoint");
        assertThat(firstTuple.values().size()).isEqualTo(3);
        List<Value> xyz = firstTuple.values();
        XFloat z = (XFloat) xyz.get(2);
        assertThat(z.name()).isEqualTo("z");
        assertThat(z.value()).isEqualTo(300.0f);

        // XUInt resource
        XContainerGeneric xuint = (XContainerGeneric) containers.get(22);
        assertThat(xuint.name()).isEqualTo("XUintResourceDetails");
        XUInt value = (XUInt) xuint.values().get(0);
        assertThat(value.name()).isEqualTo("Value");
        assertThat(value.value()).isEqualTo(1000);
        XString name = (XString) xuint.values().get(1);
        assertThat(name.name()).isEqualTo("Name");
        assertThat(name.value()).isEqualTo("Camera.Track.MinEventTime");
        XUInt flags = (XUInt) xuint.values().get(2);
        assertThat(flags.name()).isEqualTo("Flags");
        assertThat(flags.value()).isEqualTo(0);

        // XVector resource
        XContainerGeneric xvector = (XContainerGeneric) containers.get(80);
        assertThat(xvector.name()).isEqualTo("XVectorResourceDetails");
        Tuple xvectorTuple = (Tuple) xvector.values().get(0);
        assertThat(xvectorTuple.name()).isEqualTo("Value");
        XFloat xvectorY = (XFloat) xvectorTuple.values().get(1);
        assertThat(xvectorY.name()).isEqualTo("y");
        assertThat(xvectorY.value()).isEqualTo(0f);
        name = (XString) xvector.values().get(1);
        assertThat(name.name()).isEqualTo("Name");
        assertThat(name.value()).isEqualTo("Camera.Track.EventPosition");
        flags = (XUInt) xvector.values().get(2);
        assertThat(flags.name()).isEqualTo("Flags");
        assertThat(flags.value()).isEqualTo(0);

        // Verify they are all XContainers
        for (XContainer child : containers) {
            assertThat(child).isOfAnyClassIn(XContainerGeneric.class);
        }

        // Check the name of a few of them
        assertContainerName(containers.get(4), "OccludingCameraPropertiesContai");
        assertContainerName(containers.get(7), "ChaseCameraPropertiesContainer");
        assertContainerName(containers.get(14), "FlyCameraPropertiesContainer");
        assertContainerName(containers.get(19), "XUintResourceDetails");
        assertContainerName(containers.get(29), "XStringResourceDetails");
        assertContainerName(containers.get(33), "XStringResourceDetails");
        assertContainerName(containers.get(34), "XFloatResourceDetails");
        assertContainerName(containers.get(79), "XFloatResourceDetails");
        assertContainerName(containers.get(80), "XVectorResourceDetails");
        assertContainerName(containers.get(83), "XVectorResourceDetails");
        assertContainerName(containers.get(84), "XContainerResourceDetails");
        assertContainerName(containers.get(98), "XContainerResourceDetails");
        assertContainerName(containers.get(99), "XDataBank");
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
        assertThat(types.get(0).subType()).isEqualTo(0);
        assertThat(types.get(0).size()).isEqualTo(0);
        assertThat(types.get(0).name()).isEqualTo("XContainer");
        assertThat(types.get(0).guid()).isEqualTo("5E1CD446-48A3-44FE-A55A-E247B8F5E713");
        assertThat(types.get(1).subType()).isEqualTo(0);
        assertThat(types.get(1).name()).isEqualTo("AIParametersContainer");
        assertThat(types.get(1).guid()).isEqualTo("6A0DEE10-2E94-4844-B2D4-6C39CA979C90");
        assertThat(types.get(2).subType()).isEqualTo(0);
        assertThat(types.get(2).name()).isEqualTo("XResourceDetails");
        assertThat(types.get(2).guid()).isEqualTo("E77556F2-A495-491A-8563-006CE2577244");
        assertThat(types.get(3).subType()).isEqualTo(0);
        assertThat(types.get(3).name()).isEqualTo("XUintResourceDetails");
        assertThat(types.get(3).guid()).isEqualTo("93E5117C-8ADA-4AFB-B17A-668D0BDD15E6");
        assertThat(types.get(4).subType()).isEqualTo(0);
        assertThat(types.get(4).name()).isEqualTo("XContainerResourceDetails");
        assertThat(types.get(4).guid()).isEqualTo("E0B6BF20-F80B-4329-B6EB-1998ED8AEC1F");
        assertThat(types.get(5).subType()).isEqualTo(0);
        assertThat(types.get(5).name()).isEqualTo("XDataBank");
        assertThat(types.get(5).guid()).isEqualTo("93E661AC-D941-442A-A18E-E99B795879DC");

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
        XContainerGeneric firstAI = (XContainerGeneric) containers.get(0);
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
        XContainerGeneric resourceContainer = (XContainerGeneric) containers.get(25);
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
        XContainerGeneric last = (XContainerGeneric) containers.get(containers.size() - 1);
        assertThat(last.name()).isEqualTo("XDataBank");
        List<Value> lastValues = last.values();
        assertThat(lastValues.size()).isEqualTo(9);
        XUInt8 section = (XUInt8) lastValues.get(0);
        assertThat(section.name()).isEqualTo("Section");
        assertThat(section.value()).isEqualTo(0);
        XCollection containerResources = (XCollection) lastValues.get(6);
        assertThat(containerResources.name()).isEqualTo("ContainerResources");
        assertThat(containerResources.values().size()).isEqualTo(24);
        Ref ref = (Ref) containerResources.values().get(0);
        assertThat(ref.name()).isEqualTo("ContainerResources");
        assertThat(ref.value()).isEqualTo(26);
        XCollection colorResources = (XCollection) lastValues.get(8);
        assertThat(colorResources.name()).isEqualTo("ColorResources");
        assertThat(colorResources.values().size()).isEqualTo(0);
    }

    private void assertContainerName(XContainer container, String name) {
        XContainerGeneric xContainer = (XContainerGeneric) container;
        assertThat(xContainer.name()).isEqualTo(name);
    }
}
