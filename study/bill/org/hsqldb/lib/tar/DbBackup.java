package org.hsqldb.lib.tar;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Properties;
import org.hsqldb.lib.InputStreamInterface;
import org.hsqldb.lib.InputStreamWrapper;

public class DbBackup
{
  protected File dbDir;
  protected File archiveFile;
  protected String instanceName;
  protected boolean overWrite = false;
  protected boolean abortUponModify = true;
  File[] componentFiles;
  InputStreamInterface[] componentStreams;
  boolean[] existList;
  boolean[] ignoreList;
  
  public DbBackup(File paramFile, String paramString)
  {
    this.archiveFile = paramFile;
    File localFile = new File(paramString);
    this.dbDir = localFile.getAbsoluteFile().getParentFile();
    this.instanceName = localFile.getName();
    this.componentFiles = new File[] { new File(this.dbDir, this.instanceName + ".properties"), new File(this.dbDir, this.instanceName + ".script"), new File(this.dbDir, this.instanceName + ".data"), new File(this.dbDir, this.instanceName + ".backup"), new File(this.dbDir, this.instanceName + ".log"), new File(this.dbDir, this.instanceName + ".lobs") };
    this.componentStreams = new InputStreamInterface[this.componentFiles.length];
    this.existList = new boolean[this.componentFiles.length];
    this.ignoreList = new boolean[this.componentFiles.length];
  }
  
  public DbBackup(File paramFile, String paramString, boolean paramBoolean)
  {
    this.archiveFile = paramFile;
    File localFile = new File(paramString);
    this.dbDir = localFile.getAbsoluteFile().getParentFile();
    this.instanceName = localFile.getName();
    this.componentFiles = new File[] { new File(this.dbDir, this.instanceName + ".script") };
    this.componentStreams = new InputStreamInterface[this.componentFiles.length];
    this.existList = new boolean[this.componentFiles.length];
    this.ignoreList = new boolean[this.componentFiles.length];
    this.abortUponModify = false;
  }
  
  public void setStream(String paramString, InputStreamInterface paramInputStreamInterface)
  {
    for (int i = 0; i < this.componentFiles.length; i++) {
      if (this.componentFiles[i].getName().endsWith(paramString))
      {
        this.componentStreams[i] = paramInputStreamInterface;
        break;
      }
    }
  }
  
  public void setFileIgnore(String paramString)
  {
    for (int i = 0; i < this.componentFiles.length; i++) {
      if (this.componentFiles[i].getName().endsWith(paramString))
      {
        this.ignoreList[i] = true;
        break;
      }
    }
  }
  
  public void setOverWrite(boolean paramBoolean)
  {
    this.overWrite = paramBoolean;
  }
  
  public void setAbortUponModify(boolean paramBoolean)
  {
    this.abortUponModify = paramBoolean;
  }
  
  public boolean getOverWrite()
  {
    return this.overWrite;
  }
  
  public boolean getAbortUponModify()
  {
    return this.abortUponModify;
  }
  
  public void write()
    throws IOException, TarMalformatException
  {
    long l = new Date().getTime();
    checkEssentialFiles();
    TarGenerator localTarGenerator = new TarGenerator(this.archiveFile, this.overWrite, Integer.valueOf(generateBufferBlockValue(this.componentFiles)));
    for (int i = 0; i < this.componentFiles.length; i++)
    {
      int j = (this.componentStreams[i] != null) || (this.componentFiles[i].exists()) ? 1 : 0;
      if ((j != 0) && (this.ignoreList[i] == 0)) {
        if (this.componentStreams[i] == null)
        {
          localTarGenerator.queueEntry(this.componentFiles[i].getName(), this.componentFiles[i]);
          this.existList[i] = true;
        }
        else
        {
          localTarGenerator.queueEntry(this.componentFiles[i].getName(), this.componentStreams[i]);
        }
      }
    }
    localTarGenerator.write();
    checkFilesNotChanged(l);
  }
  
  public void writeAsFiles()
    throws IOException
  {
    int i = 512 * generateBufferBlockValue(this.componentFiles);
    byte[] arrayOfByte = new byte[i];
    checkEssentialFiles();
    FileOutputStream localFileOutputStream = null;
    for (int j = 0; j < this.componentFiles.length; j++) {
      try
      {
        if (this.ignoreList[j] != 0)
        {
          if (localFileOutputStream != null)
          {
            localFileOutputStream.close();
            localFileOutputStream = null;
          }
        }
        else if (!this.componentFiles[j].exists())
        {
          if (localFileOutputStream != null)
          {
            localFileOutputStream.close();
            localFileOutputStream = null;
          }
        }
        else
        {
          File localFile = new File(this.archiveFile, this.componentFiles[j].getName());
          localFileOutputStream = new FileOutputStream(localFile);
          if (this.componentStreams[j] == null) {
            this.componentStreams[j] = new InputStreamWrapper(new FileInputStream(this.componentFiles[j]));
          }
          InputStreamInterface localInputStreamInterface = this.componentStreams[j];
          for (;;)
          {
            int k = localInputStreamInterface.read(arrayOfByte, 0, arrayOfByte.length);
            if (k <= 0) {
              break;
            }
            localFileOutputStream.write(arrayOfByte, 0, k);
          }
          localInputStreamInterface.close();
          localFileOutputStream.flush();
          localFileOutputStream.getFD().sync();
        }
      }
      finally
      {
        if (localFileOutputStream != null)
        {
          localFileOutputStream.close();
          localFileOutputStream = null;
        }
      }
    }
  }
  
  void checkEssentialFiles()
    throws FileNotFoundException, IllegalStateException
  {
    if (!this.componentFiles[0].getName().endsWith(".properties")) {
      return;
    }
    for (int i = 0; i < 2; i++)
    {
      int j = (this.componentStreams[i] != null) || (this.componentFiles[i].exists()) ? 1 : 0;
      if (j == 0) {
        throw new FileNotFoundException(RB.file_missing.getString(new String[] { this.componentFiles[i].getAbsolutePath() }));
      }
    }
    if (!this.abortUponModify) {
      return;
    }
    Properties localProperties = new Properties();
    FileInputStream localFileInputStream = null;
    try
    {
      File localFile = this.componentFiles[0];
      localFileInputStream = new FileInputStream(localFile);
      localProperties.load(localFileInputStream);
    }
    catch (IOException localIOException2) {}finally
    {
      try
      {
        if (localFileInputStream != null) {
          localFileInputStream.close();
        }
      }
      catch (IOException localIOException4) {}finally
      {
        localFileInputStream = null;
      }
    }
    String str = localProperties.getProperty("modified");
    if ((str != null) && ((str.equalsIgnoreCase("yes")) || (str.equalsIgnoreCase("true")))) {
      throw new IllegalStateException(RB.modified_property.getString(new String[] { str }));
    }
  }
  
  void checkFilesNotChanged(long paramLong)
    throws FileNotFoundException
  {
    if (!this.abortUponModify) {
      return;
    }
    try
    {
      for (int i = 0; i < this.componentFiles.length; i++) {
        if (this.componentFiles[i].exists())
        {
          if (this.existList[i] == 0) {
            throw new FileNotFoundException(RB.file_disappeared.getString(new String[] { this.componentFiles[i].getAbsolutePath() }));
          }
          if (this.componentFiles[i].lastModified() > paramLong) {
            throw new FileNotFoundException(RB.file_changed.getString(new String[] { this.componentFiles[i].getAbsolutePath() }));
          }
        }
        else if (this.existList[i] != 0)
        {
          throw new FileNotFoundException(RB.file_appeared.getString(new String[] { this.componentFiles[i].getAbsolutePath() }));
        }
      }
    }
    catch (IllegalStateException localIllegalStateException)
    {
      if (!this.archiveFile.delete()) {
        System.out.println(RB.cleanup_rmfail.getString(new String[] { this.archiveFile.getAbsolutePath() }));
      }
      throw localIllegalStateException;
    }
  }
  
  protected static int generateBufferBlockValue(File[] paramArrayOfFile)
  {
    long l = 0L;
    for (int i = 0; i < paramArrayOfFile.length; i++) {
      if ((paramArrayOfFile[i] != null) && (paramArrayOfFile[i].length() > l)) {
        l = paramArrayOfFile[i].length();
      }
    }
    i = (int)(l / 5120L);
    if (i < 1) {
      return 1;
    }
    if (i > 40960) {
      return 40960;
    }
    return i;
  }
  
  protected static int generateBufferBlockValue(File paramFile)
  {
    return generateBufferBlockValue(new File[] { paramFile });
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\tar\DbBackup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */