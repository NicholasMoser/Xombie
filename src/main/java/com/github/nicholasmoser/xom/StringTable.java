package com.github.nicholasmoser.xom;

import java.util.List;
import java.util.Map;

public record StringTable (int sizeStrs,
                           int lenStrs,
                           List<Integer> offsets,
                           Map<Integer, String> offsetToStr){
}
