package org.hsqldb.lib.tar;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class TarGeneratorMain
{
  public static void main(String[] paramArrayOfString)
    throws IOException, TarMalformatException
  {
    if (paramArrayOfString.length < 1)
    {
      System.out.println(RB.TarGenerator_syntax.getString(new String[] { DbBackup.class.getName() }));
      System.exit(0);
    }
    TarGenerator localTarGenerator = new TarGenerator(new File(paramArrayOfString[0]), true, null);
    if (paramArrayOfString.length == 1) {
      localTarGenerator.queueEntry("stdin", System.in, 10240);
    } else {
      for (int i = 1; i < paramArrayOfString.length; i++) {
        localTarGenerator.queueEntry(new File(paramArrayOfString[i]));
      }
    }
    localTarGenerator.write();
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\tar\TarGeneratorMain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */