module com.github.nicholasmoser {
  exports com.github.nicholasmoser;
  exports com.github.nicholasmoser.gamecube;
  exports com.github.nicholasmoser.utils;

  opens com.github.nicholasmoser to javafx.fxml;
  opens com.github.nicholasmoser.tools to javafx.fxml;
  opens com.github.nicholasmoser.xom to javafx.fxml;

  requires com.google.common;

  requires java.sql;
  requires java.desktop;
  requires java.logging;
  requires java.net.http;
  requires jdk.crypto.ec;
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;

  requires org.json;
  requires com.j2html;
  requires org.xerial.sqlitejdbc;
  requires org.checkerframework.checker.qual;
  requires decentxml;
  requires kaitai.struct.runtime;
}
