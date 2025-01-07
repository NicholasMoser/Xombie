package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.dol.DolUtil;
import com.github.nicholasmoser.utils.ByteUtils;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CodeCaveController {

  public TextArea branchBytes;
  public TextArea codeCaveBytes;
  public TextField branchAddress;
  public TextField branchOffset;
  public TextField codeCaveStartAddress;
  public TextField codeCaveStartOffset;
  public TextField codeCaveEndAddress;
  public TextField codeCaveEndOffset;


  public void init(int branchOffset, byte[] branchBytes, int codeCaveStart, byte[] codeCaveBytes) {
    this.branchAddress.setText(String.format("0x%X", branchOffset));
    this.branchOffset.setText(String.format("0x%X", DolUtil.ram2dol(branchOffset)));
    this.branchBytes.setText(ByteUtils.bytesToHexStringWords(branchBytes));
    this.codeCaveStartAddress.setText(String.format("0x%X", codeCaveStart));
    this.codeCaveStartOffset.setText(String.format("0x%X", DolUtil.ram2dol(codeCaveStart)));
    this.codeCaveEndAddress.setText(String.format("0x%X", codeCaveStart + codeCaveBytes.length));
    this.codeCaveEndOffset.setText(String.format("0x%X", DolUtil.ram2dol(codeCaveStart + codeCaveBytes.length)));
    this.codeCaveBytes.setText(ByteUtils.bytesToHexStringWords(codeCaveBytes));
  }
}
