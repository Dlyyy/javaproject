package org.hsqldb.persist;

import org.hsqldb.rowio.RowInputInterface;

public abstract interface TextFileReader
{
  public abstract String getHeaderLine();
  
  public abstract long getLineNumber();
  
  public abstract void readHeaderLine();
  
  public abstract RowInputInterface readObject();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\TextFileReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */