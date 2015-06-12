package com.tkmtwo.hc.uri;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;


/**
 *
 */
public class PercentCodec {

  public static final String DEFAULT_CHARSET = "UTF-8";
  public static final int RADIX = 16;
  public static final byte ESCAPE_CHAR = '%';
  
  private final String charset;

  private BitSet safeBitSet = new BitSet(256);
  


  public String getCharset() { return charset; }

  /*
  public BitSet getSafeBitSet() {
    return (BitSet) getSafeBitSetInternal().clone();
  }
  
  private BitSet getSafeBitSetInternal() {
    if (safeBitSet == null) {
      safeBitSet = new BitSet(256);
    }
    
    return safeBitSet;
  }
  */
  private BitSet getSafeBitSet() {
    if (safeBitSet == null) {
      safeBitSet = new BitSet(256);
    }
    return safeBitSet;
  }

  public void orBitSet(BitSet bs) { getSafeBitSet().or(bs); }
  



  
  public PercentCodec() {
    this("UTF-8");
  }

  private PercentCodec(String cs) {
    charset = cs;
    //orBitSet(getAlphaBitSet());
    //orBitSet(getDigitBitSet());
  }
  
  public PercentCodec(BitSet... bss) {
    this();
    for (BitSet bs : bss) {
      orBitSet(bs);
    }
  }
  
  
  
  
  
  
  
  
  







  public static final byte[] encodePercent(BitSet percentSafe, byte[] bytes) {
    if (bytes == null) { return new byte[0]; }
    if (percentSafe == null) { throw new IllegalArgumentException("percentSafe is null"); }


    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    for (byte c : bytes) {
      int b = c;
      if (b < 0) {
        b = 256 + b;
      }
      if (percentSafe.get(b)) {
        buffer.write(b);
      } else {
        buffer.write(ESCAPE_CHAR);
        char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, RADIX));
        char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, RADIX));
        buffer.write(hex1);
        buffer.write(hex2);
      }
    }
    return buffer.toByteArray();
  } 
  public static final byte[] decodePercent(byte[] bytes) {
    if (bytes == null) {
      //return null;
      return new byte[0];
    }
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    for (int i = 0; i < bytes.length; i++) {
      int b = bytes[i];
      if (b == ESCAPE_CHAR) {
        try {
          int u = digit16(bytes[++i]);
          int l = digit16(bytes[++i]);
          buffer.write((char) ((u << 4) + l));
        } catch (ArrayIndexOutOfBoundsException e) {
          throw new IllegalArgumentException(String
                                             .format("Invalid encoding at position %s, not enough characters.",
                                                     String.valueOf(i)));
        }
      } else {
        buffer.write(b);
      }
    }
    return buffer.toByteArray();
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  public byte[] encode(byte[] bytes) {
    return encodePercent(getSafeBitSet(), bytes);
  }
  public byte[] decode(byte[] bytes) {
    return decodePercent(bytes);
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  public String encode(String s, String charset) {
    if (s == null) { return null; }
    try {
      return new String(encode(s.getBytes(charset)), "US-ASCII");
    } catch (UnsupportedEncodingException uex) {
      throw new IllegalArgumentException(uex.getMessage(), uex);
    }
  }
  public String decode(String s, String charset) {
    if (s == null) { return null; }
    try {
      return new String(decode(s.getBytes("US-ASCII")), charset);
    } catch (UnsupportedEncodingException uex) {
      throw new IllegalArgumentException(uex.getMessage(), uex);
    }

  }










  public String encode(String s) {
    if (s == null) { return null; }

    return encode(s, getCharset());
  }
  public String decode(String s) {
    if (s == null) { return null; }
    return decode(s, getCharset());

  }

  
  
  
  
  
  
  
  
  
  
  
  public Object encode(Object obj) {
    if (obj == null) {
      return null;
    } else if (obj instanceof byte[]) {
      return encode((byte[]) obj);
    } else if (obj instanceof String) {
      return encode((String) obj);
    } else {
      throw new IllegalArgumentException(String.format("Objects of type %s cannot be percent-encoded.",
                                                       obj.getClass().getName()));
    }
  }

  public Object decode(Object obj) {
    if (obj == null) {
      return null;
    } else if (obj instanceof byte[]) {
      return decode((byte[]) obj);
    } else if (obj instanceof String) {
      return decode((String) obj);
    } else {
      throw new IllegalArgumentException(String.format("Objects of type %s cannot be percent-encoded.",
                                                       obj.getClass().getName()));

    }
  }
  
  
  
  
  /*
  public String getDefaultCharset() {
    return this.charset;
  }
  public String getEncoding() {
    return this.charset;
  }
  */
  
  
  private static int digit16(byte b) {
    int i = Character.digit((char) b, RADIX);
    if (i == -1) {
      throw new IllegalArgumentException(String.format("Invalid encoding at character %c", b));
    }
    return i;
  }


}
