package org.hsqldb.scriptio;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import org.hsqldb.Database;
import org.hsqldb.Session;
import org.hsqldb.error.Error;
import org.hsqldb.lib.FileAccess;
import org.hsqldb.lib.LineReader;
import org.hsqldb.lib.StringConverter;
import org.hsqldb.lib.java.JavaSystem;
import org.hsqldb.persist.Crypto;
import org.hsqldb.persist.Logger;
import org.hsqldb.rowio.RowInputTextLog;

public class ScriptReaderDecode
  extends ScriptReaderText
{
  DataInputStream dataInput;
  InputStream cryptoStream;
  Crypto crypto;
  byte[] buffer = new byte['Ā'];
  
  public ScriptReaderDecode(Database paramDatabase, String paramString, Crypto paramCrypto, boolean paramBoolean)
    throws IOException
  {
    super(paramDatabase, paramString);
    this.crypto = paramCrypto;
    try
    {
      this.inputStream = paramDatabase.logger.getFileAccess().openInputStreamElement(paramString);
      this.bufferedStream = new BufferedInputStream(this.inputStream);
      this.rowIn = new RowInputTextLog();
      if (paramBoolean)
      {
        this.dataInput = new DataInputStream(this.bufferedStream);
      }
      else
      {
        this.cryptoStream = paramCrypto.getInputStream(this.bufferedStream);
        this.gzipStream = new GZIPInputStream(this.cryptoStream);
        this.dataStreamIn = new LineReader(this.gzipStream, "ISO-8859-1");
      }
    }
    catch (Throwable localThrowable)
    {
      close();
      throw JavaSystem.toIOException(localThrowable);
    }
  }
  
  public boolean readLoggedStatement(Session paramSession)
  {
    if (this.dataInput == null) {
      return super.readLoggedStatement(paramSession);
    }
    try
    {
      i = this.dataInput.readInt();
      if (i * 2 > this.buffer.length) {
        this.buffer = new byte[i * 2];
      }
      this.dataInput.readFully(this.buffer, 0, i);
    }
    catch (Throwable localThrowable)
    {
      return false;
    }
    int i = this.crypto.decode(this.buffer, 0, i, this.buffer, 0);
    String str;
    try
    {
      str = new String(this.buffer, 0, i, "ISO-8859-1");
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw Error.error(localUnsupportedEncodingException, 452, this.fileNamePath);
    }
    this.lineCount += 1L;
    this.statement = StringConverter.unicodeStringToString(str);
    if (this.statement == null) {
      return false;
    }
    processStatement(paramSession);
    return true;
  }
  
  public void close()
  {
    try
    {
      if (this.dataStreamIn != null) {
        this.dataStreamIn.close();
      }
    }
    catch (Exception localException1) {}
    try
    {
      if (this.gzipStream != null) {
        this.gzipStream.close();
      }
    }
    catch (Exception localException2) {}
    try
    {
      if (this.cryptoStream != null) {
        this.cryptoStream.close();
      }
    }
    catch (Exception localException3) {}
    try
    {
      if (this.inputStream != null) {
        this.inputStream.close();
      }
    }
    catch (Exception localException4) {}
    try
    {
      if (this.dataInput != null) {
        this.dataInput.close();
      }
    }
    catch (Exception localException5) {}
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\scriptio\ScriptReaderDecode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */