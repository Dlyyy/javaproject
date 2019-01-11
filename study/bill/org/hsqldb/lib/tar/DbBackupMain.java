package org.hsqldb.lib.tar;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class DbBackupMain
{
  public static void main(String[] paramArrayOfString)
    throws IOException, TarMalformatException
  {
    try
    {
      if (paramArrayOfString.length < 1)
      {
        System.out.println(RB.DbBackup_syntax.getString(new String[] { DbBackup.class.getName() }));
        System.out.println();
        System.out.println(RB.listing_format.getString());
        System.exit(0);
      }
      if (paramArrayOfString[0].equals("--save"))
      {
        boolean bool = (paramArrayOfString.length > 1) && (paramArrayOfString[1].equals("--overwrite"));
        if (paramArrayOfString.length != (bool ? 4 : 3)) {
          throw new IllegalArgumentException();
        }
        DbBackup localDbBackup = new DbBackup(new File(paramArrayOfString[(paramArrayOfString.length - 2)]), paramArrayOfString[(paramArrayOfString.length - 1)]);
        localDbBackup.setOverWrite(bool);
        localDbBackup.write();
      }
      else
      {
        int j;
        if (paramArrayOfString[0].equals("--list"))
        {
          if (paramArrayOfString.length < 2) {
            throw new IllegalArgumentException();
          }
          String[] arrayOfString1 = null;
          if (paramArrayOfString.length > 2)
          {
            arrayOfString1 = new String[paramArrayOfString.length - 2];
            for (j = 2; j < paramArrayOfString.length; j++) {
              arrayOfString1[(j - 2)] = paramArrayOfString[j];
            }
          }
          new TarReader(new File(paramArrayOfString[1]), 0, arrayOfString1, Integer.valueOf(DbBackup.generateBufferBlockValue(new File(paramArrayOfString[1]))), null).read();
        }
        else if (paramArrayOfString[0].equals("--extract"))
        {
          int i = (paramArrayOfString.length > 1) && (paramArrayOfString[1].equals("--overwrite")) ? 1 : 0;
          j = i != 0 ? 4 : 3;
          if (paramArrayOfString.length < j) {
            throw new IllegalArgumentException();
          }
          String[] arrayOfString2 = null;
          if (paramArrayOfString.length > j)
          {
            arrayOfString2 = new String[paramArrayOfString.length - j];
            for (int k = j; k < paramArrayOfString.length; k++) {
              arrayOfString2[(k - j)] = paramArrayOfString[k];
            }
          }
          File localFile = new File(paramArrayOfString[1]);
          int m = i != 0 ? 2 : 1;
          new TarReader(localFile, m, arrayOfString2, Integer.valueOf(DbBackup.generateBufferBlockValue(localFile)), new File(paramArrayOfString[(j - 1)])).read();
        }
        else
        {
          throw new IllegalArgumentException();
        }
      }
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      System.out.println(RB.DbBackup_syntaxerr.getString(new String[] { DbBackup.class.getName() }));
      System.exit(2);
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\tar\DbBackupMain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */