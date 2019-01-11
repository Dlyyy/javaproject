package org.hsqldb.lib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract interface FileAccess
{
  public static final int ELEMENT_READ = 1;
  public static final int ELEMENT_SEEKABLEREAD = 3;
  public static final int ELEMENT_WRITE = 4;
  public static final int ELEMENT_READWRITE = 7;
  public static final int ELEMENT_TRUNCATE = 8;
  
  public abstract InputStream openInputStreamElement(String paramString)
    throws IOException;
  
  public abstract OutputStream openOutputStreamElement(String paramString)
    throws IOException;
  
  public abstract boolean isStreamElement(String paramString);
  
  public abstract void createParentDirs(String paramString);
  
  public abstract void removeElement(String paramString);
  
  public abstract void renameElement(String paramString1, String paramString2);
  
  public abstract FileSync getFileSync(OutputStream paramOutputStream)
    throws IOException;
  
  public static abstract interface FileSync
  {
    public abstract void sync()
      throws IOException;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\FileAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */