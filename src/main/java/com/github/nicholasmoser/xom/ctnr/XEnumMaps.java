package com.github.nicholasmoser.xom.ctnr;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import java.util.Map;

public class XEnumMaps {
    public static final Map<Long, String> FACTORS = Map.ofEntries(
            Map.entry(0L, "kBlendFactorZero"),
            Map.entry(1L, "kBlendFactorOne"),
            Map.entry(2L, "kBlendFactorDestColor"),
            Map.entry(3L, "kBlendFactorOneMinusDestColor"),
            Map.entry(4L, "kBlendFactorSrcColor"),
            Map.entry(5L, "kBlendFactorOneMinusSrcColor"),
            Map.entry(6L, "kBlendFactorSrcAlpha"),
            Map.entry(7L, "kBlendFactorOneMinusSrcAlpha"),
            Map.entry(8L, "kBlendFactorDestAlpha"),
            Map.entry(9L, "kBlendFactorOneMinusDestAlpha"),
            Map.entry(10L, "kBlendFactorSrcAlphaSaturate"),
            Map.entry(11L, "kBlendFactorMinusOne")
    );

    public static final Map<Long, String> COMPARE_FUNCTIONS = Map.of(0L, "kCompareFunctionNever",
            1L, "kCompareFunctionLess",
            2L, "kCompareFunctionEqual",
            3L, "kCompareFunctionLessEqual",
            4L, "kCompareFunctionGreater",
            5L, "kCompareFunctionNotEqual",
            6L, "kCompareFunctionGreaterEqual",
            7L, "kCompareFunctionAlways");

    public static final Map<Long, String> CULL_MODES = Map.of(0L, "kCullModeOff",
            1L, "kCullModeFront",
            2L, "kCullModeBack",
            3L, "kCullModeForceFront",
            4L, "kCullModeForceBack");

    public static final Map<Long, String> NORMALIZE = Map.of(0L, "kNormalizeNever",
            1L, "kNormalizeAlways",
            2L, "kNormalizeIfRequired");

    public static final BiMap<Long, String> IMAGE_FORMATS = new ImmutableBiMap.Builder<Long, String>()
            .put(0L, "kImageFormat_R8G8B8")
            .put(1L, "kImageFormat_A8R8G8B8")
            .put(2L, "kImageFormat_X8R8G8B8")
            .put(3L, "kImageFormat_X1R5G5B5")
            .put(4L, "kImageFormat_A1R5G5B5")
            .put(5L, "kImageFormat_R5G6B5")
            .put(6L, "kImageFormat_A8")
            .put(7L, "kImageFormat_P8")
            .put(8L, "kImageFormat_P4")
            .put(9L, "kImageFormat_DXT1")
            .put(10L, "kImageFormat_DXT3")
            .put(11L, "kImageFormat_NgcRGBA8")
            .put(12L, "kImageFormat_NgcRGB5A3")
            .put(13L, "kImageFormat_NgcR5G6B5")
            .put(14L, "kImageFormat_NgcIA8")
            .put(15L, "kImageFormat_NgcIA4")
            .put(16L, "kImageFormat_NgcI8")
            .put(17L, "kImageFormat_NgcI4")
            .put(18L, "kImageFormat_NgcCI12A4")
            .put(19L, "kImageFormat_NgcCI8")
            .put(20L, "kImageFormat_NgcCI4")
            .put(21L, "kImageFormat_NgcCMPR")
            .put(22L, "kImageFormat_NgcIndirect")
            .put(23L, "kImageFormat_P2P8")
            .put(24L, "kImageFormat_P2P4")
            .put(25L, "kImageFormat_Linear")
            .put(26L, "kImageFormat_Count")
            .build();

    public static final Map<Long, String> BLENDS = Map.of(0L, "kOglBlendReplace",
            1L, "kOglBlendModulate",
            2L, "kOglBlendDecal",
            3L, "kOglBlendBlend",
            4L, "kOglBlendAdd");

    public static final Map<Long, String> ADDRESS_MODES = Map.of(0L, "kAddressModeInvalid",
            1L, "kAddressModeRepeat",
            2L, "kAddressModeMirror",
            3L, "kAddressModeClamp",
            4L, "kAddressModeBorder");

    public static final Map<Long, String> FILTER_MODES = Map.of(0L, "kFilterModeNone",
            1L, "kFilterModeNearest",
            2L, "kFilterModeLinear",
            3L, "kFilterModeAnisotropic",
            4L, "kFilterMode_Count");

    public static final Map<Long, String> PALETTE_FORMAT = Map.of(0L, " kPaletteFormat_R8G8B8X8",
            1L, "kPaletteFormat_R8G8B8A8",
            2L, "kPaletteFormat_R5G5B5",
            3L, "kPaletteFormat_R5G6B5",
            4L, "kPaletteFormat_NgcRGB5A3",
            5L, "kPaletteFormat_Count");
}
