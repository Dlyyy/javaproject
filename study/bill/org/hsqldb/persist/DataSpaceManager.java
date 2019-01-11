package org.hsqldb.persist;

import org.hsqldb.lib.DoubleIntIndex;

public abstract interface DataSpaceManager
{
  public static final int tableIdEmpty = 0;
  public static final int tableIdDirectory = 1;
  public static final int tableIdLookup = 5;
  public static final int tableIdDefault = 7;
  public static final int tableIdFirst = 8;
  public static final int fixedBlockSizeUnit = 4096;
  
  public abstract TableSpaceManager getDefaultTableSpace();
  
  public abstract TableSpaceManager getTableSpace(int paramInt);
  
  public abstract int getNewTableSpaceID();
  
  public abstract long getFileBlocks(int paramInt1, int paramInt2);
  
  public abstract void freeTableSpace(int paramInt);
  
  public abstract void freeTableSpace(int paramInt, DoubleIntIndex paramDoubleIntIndex, long paramLong1, long paramLong2, boolean paramBoolean);
  
  public abstract long getLostBlocksSize();
  
  public abstract boolean isModified();
  
  public abstract int getFileBlockSize();
  
  public abstract void initialiseSpaces();
  
  public abstract void reset();
  
  public abstract boolean isMultiSpace();
  
  public abstract int getFileBlockItemCount();
  
  public abstract DirectoryBlockCachedObject[] getDirectoryList();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\DataSpaceManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */