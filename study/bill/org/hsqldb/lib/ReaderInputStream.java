package org.hsqldb.lib;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class ReaderInputStream
  extends InputStream
{
  protected Reader reader;
  protected long pos;
  int lastChar = -1;
  
  public ReaderInputStream(Reader paramReader)
  {
    this.reader = paramReader;
    this.pos = 0L;
  }
  
  public int read()
    throws IOException
  {
    if (this.lastChar >= 0)
    {
      int i = this.lastChar & 0xFF;
      this.lastChar = -1;
      this.pos += 1L;
      return i;
    }
    this.lastChar = this.reader.read();
    if (this.lastChar < 0) {
      return this.lastChar;
    }
    this.pos += 1L;
    return this.lastChar >> 8;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\ReaderInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */