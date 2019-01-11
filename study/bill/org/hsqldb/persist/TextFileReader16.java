package org.hsqldb.persist;

import java.io.IOException;
import org.hsqldb.error.Error;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowInputText;

public class TextFileReader16
  extends TextFileReader8
  implements TextFileReader
{
  TextFileReader16(RandomAccessInterface paramRandomAccessInterface, TextFileSettings paramTextFileSettings, RowInputInterface paramRowInputInterface, boolean paramBoolean)
  {
    this.dataFile = paramRandomAccessInterface;
    this.textFileSettings = paramTextFileSettings;
    this.rowIn = paramRowInputInterface;
    this.buffer = TextFileReader8.StringCreator.getStringCreator(Character.TYPE, paramTextFileSettings.charEncoding);
    skipBOM();
  }
  
  private void skipBOM()
  {
    try
    {
      if (this.textFileSettings.isUTF16)
      {
        this.dataFile.seek(0L);
        if ((this.dataFile.read() == 254) && (this.dataFile.read() == 255))
        {
          this.position = 2L;
        }
        else
        {
          this.dataFile.seek(0L);
          if ((this.dataFile.read() == 255) && (this.dataFile.read() == 254))
          {
            this.position = 2L;
            this.textFileSettings.setLittleEndianByteOrderMark();
          }
        }
      }
    }
    catch (IOException localIOException)
    {
      throw Error.error(484, localIOException);
    }
  }
  
  int getByteSizeForChar()
  {
    return 2;
  }
  
  int readChar()
  {
    try
    {
      int i = this.dataFile.read();
      if (i == -1) {
        return -1;
      }
      int j = this.dataFile.read();
      if (j == -1) {
        return -1;
      }
      if (this.textFileSettings.isLittleEndian)
      {
        int k = i;
        i = j;
        j = k;
      }
      return (char)((i << 8) + j);
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
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\TextFileReader16.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */