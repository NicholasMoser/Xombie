package com.github.nicholasmoser.iso;

import java.util.Objects;

/**
 * An ISO file. The data for this file will be located on the ISO at the given pos with the given
 * len as the size.
 */
public class ISOFile implements ISOItem {

  private final String parent;

  private int pos;

  private final int len;

  private final String name;

  private final String gamePath;

  /**
   * Creates a new ISOFile.
   *
   * @param parent   The parent game path.
   * @param pos      The offset of the file data on the ISO.
   * @param len      The full length of the file data.
   * @param name     The name of this file.
   * @param gamePath The game path of this file.
   */
  private ISOFile(String parent, int pos, int len,
      String name, String gamePath) {
    if (parent == null) {
      throw new IllegalArgumentException("parent cannot be null");
    } else if (name == null) {
      throw new IllegalArgumentException("name cannot be null");
    } else if (gamePath == null) {
      throw new IllegalArgumentException("gamePath cannot be null");
    }
    this.parent = parent;
    this.pos = pos;
    this.len = len;
    this.name = name;
    this.gamePath = gamePath;
  }

  @Override
  public String getParent() {
    return parent;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getGamePath() {
    return gamePath;
  }

  @Override
  public boolean isDirectory() {
    return false;
  }

  @Override
  public boolean isRoot() {
    return false;
  }

  /**
   * @return The offset of the file data on the ISO.
   */
  public int getPos() {
    return pos;
  }

  /**
   * @return The full length of the file data.
   */
  public int getLen() {
    return len;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ISOFile isoFile = (ISOFile) o;
    return pos == isoFile.pos &&
        len == isoFile.len &&
        parent.equals(isoFile.parent) &&
        name.equals(isoFile.name) &&
        gamePath.equals(isoFile.gamePath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(parent, pos, len, name, gamePath);
  }

  @Override
  public String toString() {
    return "ISOFile{" +
        "parent='" + parent + '\'' +
        ", pos=" + String.format("%08X", pos) +
        ", len=" + String.format("%08X", len) +
        ", name='" + name + '\'' +
        ", gamePath='" + gamePath + '\'' +
        '}';
  }

  /**
   * Allows the position of an ISOFile to be updated. This is necessary so that positions
   * can be updated to push files to the end of an ISO.
   *
   * @param pos The updated position.
   */
  public void updatePosition(int pos) {
    this.pos = pos;
  }

  /**
   * Builder class for ISOFile.
   */
  public static class Builder {

    private String parent;
    private int pos;
    private int len;
    private String name;
    private String gamePath;

    public Builder setParent(String parent) {
      this.parent = parent;
      return this;
    }

    public Builder setPos(int pos) {
      this.pos = pos;
      return this;
    }

    public Builder setLen(int len) {
      this.len = len;
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setGamePath(String gamePath) {
      this.gamePath = gamePath;
      return this;
    }

    public ISOFile build() {
      return new ISOFile(parent, pos, len, name, gamePath);
    }
  }
}
