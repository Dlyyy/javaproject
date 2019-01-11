package org.hsqldb.lib.tar;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class TarReaderMain
{
  public static void main(String[] paramArrayOfString)
    throws IOException, TarMalformatException
  {
    if (paramArrayOfString.length < 1)
    {
      System.out.println(RB.TarReader_syntax.getString(new String[] { TarReader.class.getName() }));
      System.out.println(RB.listing_format.getString());
      System.exit(0);
    }
    File localFile = (paramArrayOfString.length > 1) && (paramArrayOfString[1].startsWith("--directory=")) ? new File(paramArrayOfString[1].substring("--directory=".length())) : null;
    int i = localFile == null ? 2 : 3;
    if ((paramArrayOfString.length < i) || ((!paramArrayOfString[0].equals("t")) && (!paramArrayOfString[0].equals("x")))) {
      throw new IllegalArgumentException(RB.tarreader_syntaxerr.getString(new String[] { TarReader.class.getName() }));
    }
    String[] arrayOfString = null;
    if (paramArrayOfString.length > i)
    {
      arrayOfString = new String[paramArrayOfString.length - i];
      for (j = i; j < paramArrayOfString.length; j++) {
        arrayOfString[(j - i)] = paramArrayOfString[j];
      }
    }
    if ((paramArrayOfString[0].equals("t")) && (localFile != null)) {
      throw new IllegalArgumentException(RB.dir_x_conflict.getString());
    }
    int j = localFile == null ? 1 : 2;
    int k = paramArrayOfString[0].equals("t") ? 0 : 1;
    new TarReader(new File(paramArrayOfString[j]), k, arrayOfString, null, localFile).read();
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\tar\TarReaderMain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */