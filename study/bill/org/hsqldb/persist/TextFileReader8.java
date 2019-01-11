package org.hsqldb.persist;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.hsqldb.error.Error;
import org.hsqldb.lib.CharArrayWriter;
import org.hsqldb.lib.HsqlByteArrayOutputStream;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowInputText;

public class TextFileReader8
  implements TextFileReader
{
  RandomAccessInterface dataFile;
  RowInputInterface rowIn;
  TextFileSettings textFileSettings;
  String header;
  boolean isReadOnly;
  StringCreator buffer;
  long position = 0L;
  
  TextFileReader8() {}
  
  TextFileReader8(RandomAccessInterface paramRandomAccessInterface, TextFileSettings paramTextFileSettings, RowInputInterface paramRowInputInterface, boolean paramBoolean)
  {
    this.dataFile = paramRandomAccessInterface;
    this.textFileSettings = paramTextFileSettings;
    this.rowIn = paramRowInputInterface;
    this.isReadOnly = paramBoolean;
    this.buffer = StringCreator.getStringCreator(Byte.TYPE, paramTextFileSettings.charEncoding);
    skipBOM();
  }
  
  static TextFileReader newTextFileReader(RandomAccessInterface paramRandomAccessInterface, TextFileSettings paramTextFileSettings, RowInputInterface paramRowInputInterface, boolean paramBoolean)
  {
    if (paramTextFileSettings.isUTF16) {
      return new TextFileReader16(paramRandomAccessInterface, paramTextFileSettings, paramRowInputInterface, paramBoolean);
    }
    return new TextFileReader8(paramRandomAccessInterface, paramTextFileSettings, paramRowInputInterface, paramBoolean);
  }
  
  private void skipBOM()
  {
    try
    {
      if (this.textFileSettings.isUTF8)
      {
        this.dataFile.seek(0L);
        if ((this.dataFile.read() == 239) && (this.dataFile.read() == 187) && (this.dataFile.read() == 191)) {
          this.position = 3L;
        }
      }
    }
    catch (IOException localIOException)
    {
      throw Error.error(484, localIOException);
    }
  }
  
  public RowInputInterface readObject()
  {
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    this.buffer.reset();
    this.position = findNextUsedLinePos();
    if (this.position == -1L) {
      return null;
    }
    try
    {
      this.dataFile.seek(this.position);
      long l1 = 0L;
      long l2 = 0L;
      while (j == 0)
      {
        int i1 = readChar();
        m = 0;
        if (i1 == -1)
        {
          if (this.buffer.size() == 0) {
            return null;
          }
          j = 1;
          if ((k != 0) || (this.isReadOnly)) {
            break;
          }
          this.dataFile.write(this.textFileSettings.bytesForLineEnd, 0, this.textFileSettings.bytesForLineEnd.length);
          for (int i2 = 0; i2 < this.textFileSettings.bytesForLineEnd.length; i2++) {
            this.buffer.write(this.textFileSettings.bytesForLineEnd[i2]);
          }
          break;
        }
        if (i1 == this.textFileSettings.singleSeparator)
        {
          if (i == 0)
          {
            l2 = l1;
            n = 0;
            i = 0;
          }
        }
        else if (i1 == this.textFileSettings.quoteChar)
        {
          m = 1;
          j = k;
          k = 0;
          if ((this.textFileSettings.isQuoted) && ((this.textFileSettings.singleSeparator == 0) || (l1 == l2 + 1L))) {
            n = 1;
          }
          if (n != 0) {
            i = i == 0 ? 1 : 0;
          }
        }
        else
        {
          switch (i1)
          {
          case 13: 
            k = i == 0 ? 1 : 0;
            break;
          case 10: 
            j = i == 0 ? 1 : 0;
            break;
          default: 
            m = 1;
            j = k;
            k = 0;
          }
        }
        this.buffer.write(i1);
        l1 += 1L;
      }
      if (j != 0)
      {
        if (m != 0) {
          this.buffer.setSize(this.buffer.size() - 1);
        }
        String str;
        try
        {
          str = this.buffer.getString();
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException)
        {
          throw Error.error(331);
        }
        ((RowInputText)this.rowIn).setSource(str, this.position, this.buffer.getByteSize());
        this.position += this.rowIn.getSize();
        return this.rowIn;
      }
      return null;
    }
    catch (IOException localIOException)
    {
      throw Error.error(484, localIOException);
    }
  }
  
  public void readHeaderLine()
  {
    int i = 0;
    int j = 0;
    int k = 0;
    this.buffer.reset();
    try
    {
      this.dataFile.seek(this.position);
    }
    catch (IOException localIOException1)
    {
      throw Error.error(484, localIOException1);
    }
    while (i == 0)
    {
      k = 0;
      int m;
      try
      {
        m = readChar();
        if (m == -1)
        {
          if (this.buffer.size() == 0) {
            return;
          }
          i = 1;
          if (!this.isReadOnly)
          {
            this.dataFile.write(this.textFileSettings.bytesForLineEnd, 0, this.textFileSettings.bytesForLineEnd.length);
            for (int n = 0; n < this.textFileSettings.bytesForLineEnd.length; n++) {
              this.buffer.write(this.textFileSettings.bytesForLineEnd[n]);
            }
          }
          break;
        }
      }
      catch (IOException localIOException2)
      {
        throw Error.error(483);
      }
      switch (m)
      {
      case 13: 
        j = 1;
        break;
      case 10: 
        i = 1;
        break;
      default: 
        k = 1;
        i = j;
        j = 0;
      }
      if ((j == 0) && (i == 0)) {
        this.buffer.write(m);
      }
    }
    if (k != 0) {
      this.buffer.setSize(this.buffer.size() - 1);
    }
    try
    {
      this.header = this.buffer.getString();
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw Error.error(331);
    }
    this.position += this.buffer.getByteSize();
  }
  
  /* Error */
  private long findNextUsedLinePos()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2	org/hsqldb/persist/TextFileReader8:position	J
    //   4: lstore_1
    //   5: aload_0
    //   6: getfield 2	org/hsqldb/persist/TextFileReader8:position	J
    //   9: lstore_3
    //   10: iconst_0
    //   11: istore 5
    //   13: aload_0
    //   14: getfield 3	org/hsqldb/persist/TextFileReader8:dataFile	Lorg/hsqldb/persist/RandomAccessInterface;
    //   17: aload_0
    //   18: getfield 2	org/hsqldb/persist/TextFileReader8:position	J
    //   21: invokeinterface 18 3 0
    //   26: aload_0
    //   27: invokevirtual 28	org/hsqldb/persist/TextFileReader8:readChar	()I
    //   30: istore 6
    //   32: lload_3
    //   33: aload_0
    //   34: invokevirtual 45	org/hsqldb/persist/TextFileReader8:getByteSizeForChar	()I
    //   37: i2l
    //   38: ladd
    //   39: lstore_3
    //   40: iload 6
    //   42: lookupswitch	default:+91->133, -1:+87->129, 10:+48->90, 13:+42->84, 32:+66->108
    //   84: iconst_1
    //   85: istore 5
    //   87: goto +66 -> 153
    //   90: iconst_0
    //   91: istore 5
    //   93: aload_0
    //   94: getfield 5	org/hsqldb/persist/TextFileReader8:rowIn	Lorg/hsqldb/rowio/RowInputInterface;
    //   97: checkcast 40	org/hsqldb/rowio/RowInputText
    //   100: invokevirtual 46	org/hsqldb/rowio/RowInputText:skippedLine	()V
    //   103: lload_3
    //   104: lstore_1
    //   105: goto +48 -> 153
    //   108: iload 5
    //   110: ifeq +43 -> 153
    //   113: iconst_0
    //   114: istore 5
    //   116: aload_0
    //   117: getfield 5	org/hsqldb/persist/TextFileReader8:rowIn	Lorg/hsqldb/rowio/RowInputInterface;
    //   120: checkcast 40	org/hsqldb/rowio/RowInputText
    //   123: invokevirtual 46	org/hsqldb/rowio/RowInputText:skippedLine	()V
    //   126: goto +27 -> 153
    //   129: ldc2_w 26
    //   132: lreturn
    //   133: iload 5
    //   135: ifeq +16 -> 151
    //   138: iconst_0
    //   139: istore 5
    //   141: aload_0
    //   142: getfield 5	org/hsqldb/persist/TextFileReader8:rowIn	Lorg/hsqldb/rowio/RowInputInterface;
    //   145: checkcast 40	org/hsqldb/rowio/RowInputText
    //   148: invokevirtual 46	org/hsqldb/rowio/RowInputText:skippedLine	()V
    //   151: lload_1
    //   152: lreturn
    //   153: goto -127 -> 26
    //   156: astore_1
    //   157: sipush 484
    //   160: aload_1
    //   161: invokestatic 23	org/hsqldb/error/Error:error	(ILjava/lang/Throwable;)Lorg/hsqldb/HsqlException;
    //   164: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	165	0	this	TextFileReader8
    //   4	148	1	l1	long
    //   156	5	1	localIOException	IOException
    //   9	95	3	l2	long
    //   11	129	5	i	int
    //   30	11	6	j	int
    // Exception table:
    //   from	to	target	type
    //   0	132	156	java/io/IOException
    //   133	152	156	java/io/IOException
    //   153	156	156	java/io/IOException
  }
  
  int getByteSizeForChar()
  {
    return 1;
  }
  
  int readChar()
  {
    try
    {
      int i = this.dataFile.read();
      return i;
    }
    catch (IOException localIOException)
    {
      throw Error.error(484, localIOException);
    }
  }
  
  public String getHeaderLine()
  {
    return this.header;
  }
  
  public long getLineNumber()
  {
    return ((RowInputText)this.rowIn).getLineNumber();
  }
  
  static class StringCreatorChars
    extends TextFileReader8.StringCreator
  {
    private CharArrayWriter buffer = new CharArrayWriter(128);
    
    void reset()
    {
      this.buffer.reset();
    }
    
    void write(int paramInt)
    {
      this.buffer.write(paramInt);
    }
    
    int size()
    {
      return this.buffer.size();
    }
    
    void setSize(int paramInt)
    {
      this.buffer.setSize(paramInt);
    }
    
    String getString()
    {
      String str = new String(this.buffer.getBuffer(), 0, this.buffer.size());
      return str;
    }
    
    int getByteSize()
    {
      return this.buffer.size() * 2;
    }
  }
  
  static class StringCreatorBytes
    extends TextFileReader8.StringCreator
  {
    private HsqlByteArrayOutputStream buffer = new HsqlByteArrayOutputStream(128);
    private String encoding;
    
    StringCreatorBytes(String paramString)
    {
      this.encoding = paramString;
    }
    
    void reset()
    {
      this.buffer.reset();
    }
    
    void write(int paramInt)
    {
      this.buffer.write(paramInt);
    }
    
    int size()
    {
      return this.buffer.size();
    }
    
    void setSize(int paramInt)
    {
      this.buffer.setSize(paramInt);
    }
    
    String getString()
      throws UnsupportedEncodingException
    {
      return this.buffer.toString(this.encoding);
    }
    
    int getByteSize()
    {
      return this.buffer.size();
    }
  }
  
  static abstract class StringCreator
  {
    static StringCreator getStringCreator(Class paramClass, String paramString)
    {
      if (Byte.TYPE.equals(paramClass)) {
        return new TextFileReader8.StringCreatorBytes(paramString);
      }
      if (Character.TYPE.equals(paramClass)) {
        return new TextFileReader8.StringCreatorChars();
      }
      throw Error.runtimeError(201, "StringCreator");
    }
    
    abstract void reset();
    
    abstract void write(int paramInt);
    
    abstract int size();
    
    abstract void setSize(int paramInt);
    
    abstract String getString()
      throws UnsupportedEncodingException;
    
    abstract int getByteSize();
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\TextFileReader8.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */