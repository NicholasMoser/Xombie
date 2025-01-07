package com.github.nicholasmoser.dol;

public class DolUtil {

  /**
   * The sections for the Worms dol.
   */
  public enum Section {
    INIT,
    TEXT,
    // located at the start of DATA
    //CTORS,
    DTORS,
    RODATA,
    DATA,
    BSS,
    SDATA,
    SDATA2
  }

  /**
   * Converts an address in ram to an offset in the dol. Most sections you can simply subtract
   * 0x80003000 to get the offset, but since the bss, sbss, and sbss2 have a length of 0 in the dol,
   * you must then subtract by 0x80003000 plus the sizes of each skipped to get offsets for sections
   * after them.
   *
   * @param ramAddress The address in ram.
   * @return The offset in the dol.
   */
  public static long ram2dol(int ramAddress) {
    return ram2dol(Integer.toUnsignedLong(ramAddress));
  }

  /**
   * Converts an address in ram to an offset in the dol. Most sections you can simply subtract
   * 0x80003000 to get the offset, but since the bss, sbss, and sbss2 have a length of 0 in the dol,
   * you must then subtract by 0x80003000 plus the sizes of each skipped to get offsets for sections
   * after them.
   *
   * @param ramAddress The address in ram.
   * @return The offset in the dol.
   */
  public static long ram2dol(long ramAddress) {
    String hexAddress = String.format("%08X", ramAddress);
    return switch (getSection(ramAddress)) {
      case INIT, TEXT, DTORS, RODATA, DATA -> ramAddress - 0x80003000L;
      case BSS -> throw new IllegalArgumentException(
          "Addresses in the bss section do not have an offset in the dol: " + hexAddress);
      case SDATA -> ramAddress - 0x800909E0L;
      case SDATA2 -> ramAddress - 0x80095B00L;
      default -> throw new IllegalArgumentException(
          "Unexpected section for ram address: " + hexAddress);
    };
  }

  /**
   * Returns the Section enum for a given ram address.
   *
   * @param ramAddress The address in ram.
   * @return The corresponding Section in the dol.
   */
  public static Section getSection(long ramAddress) {
    if (ramAddress >= 0x804B25C0L) {
      String message = "Target address of code is outside the bounds of the dol (0x804B25C0+): ";
      throw new IllegalArgumentException(message + String.format("%08x", ramAddress));
    } else if (ramAddress >= 0x804B0C80L) {
      return Section.SDATA2;
    } else if (ramAddress >= 0x804A9880L) {
      return Section.SDATA;
    } else if (ramAddress >= 0x8041BEA0L) {
      return Section.BSS;
    } else if (ramAddress >= 0x803EA900L) {
      return Section.DTORS;
    } else if (ramAddress >= 0x80367DE0L) {
      return Section.RODATA;
    } else if (ramAddress >= 0x8035E520L) {
      return Section.DATA;
    } else if (ramAddress >= 0x80003300L) {
      return Section.TEXT;
    } else if (ramAddress >= 0x80003100L) {
      return Section.INIT;
    } else {
      String message = "Target address of code is outside the bounds the of dol (0x80003100-): ";
      throw new IllegalArgumentException(message + String.format("%08x", ramAddress));
    }
  }
}
