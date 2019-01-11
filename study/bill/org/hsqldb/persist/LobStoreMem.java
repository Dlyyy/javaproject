package org.hsqldb.persist;

import org.hsqldb.lib.HsqlArrayList;

public class LobStoreMem
  implements LobStore
{
  final int lobBlockSize;
  int blocksInLargeBlock = 128;
  int largeBlockSize;
  HsqlArrayList byteStoreList;
  
  public LobStoreMem(int paramInt)
  {
    this.lobBlockSize = paramInt;
    this.largeBlockSize = (paramInt * this.blocksInLargeBlock);
    this.byteStoreList = new HsqlArrayList();
  }
  
  public byte[] getBlockBytes(int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte1 = new byte[paramInt2 * this.lobBlockSize];
    int i = 0;
    while (paramInt2 > 0)
    {
      int j = paramInt1 / this.blocksInLargeBlock;
      byte[] arrayOfByte2 = (byte[])this.byteStoreList.get(j);
      int k = paramInt1 % this.blocksInLargeBlock;
      int m = paramInt2;
      if (k + m > this.blocksInLargeBlock) {
        m = this.blocksInLargeBlock - k;
      }
      System.arraycopy(arrayOfByte2, k * this.lobBlockSize, arrayOfByte1, i * this.lobBlockSize, m * this.lobBlockSize);
      paramInt1 += m;
      i += m;
      paramInt2 -= m;
    }
    return arrayOfByte1;
  }
  
  public void setBlockBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = 0;
    while (paramInt2 > 0)
    {
      int j = paramInt1 / this.blocksInLargeBlock;
      if (j >= this.byteStoreList.size()) {
        this.byteStoreList.add(new byte[this.largeBlockSize]);
      }
      byte[] arrayOfByte = (byte[])this.byteStoreList.get(j);
      int k = paramInt1 % this.blocksInLargeBlock;
      int m = paramInt2;
      if (k + m > this.blocksInLargeBlock) {
        m = this.blocksInLargeBlock - k;
      }
      System.arraycopy(paramArrayOfByte, i * this.lobBlockSize, arrayOfByte, k * this.lobBlockSize, m * this.lobBlockSize);
      paramInt1 += m;
      i += m;
      paramInt2 -= m;
    }
  }
  
  public void setBlockBytes(byte[] paramArrayOfByte, long paramLong, int paramInt1, int paramInt2)
  {
    while (paramInt2 > 0)
    {
      int i = (int)(paramLong / this.largeBlockSize);
      if (i >= this.byteStoreList.size()) {
        this.byteStoreList.add(new byte[this.largeBlockSize]);
      }
      byte[] arrayOfByte = (byte[])this.byteStoreList.get(i);
      int j = (int)(paramLong % this.largeBlockSize);
      int k = paramInt2;
      if (j + k > this.largeBlockSize) {
        k = this.largeBlockSize - j;
      }
      System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, j, k);
      paramLong += k;
      paramInt1 += k;
      paramInt2 -= k;
    }
  }
  
  public int getBlockSize()
  {
    return this.lobBlockSize;
  }
  
  public long getLength()
  {
    return this.byteStoreList.size() * this.largeBlockSize;
  }
  
  public void setLength(long paramLong)
  {
    int i = (int)(paramLong / this.largeBlockSize);
    this.byteStoreList.setSize(i + 1);
  }
  
  public void close()
  {
    this.byteStoreList.clear();
  }
  
  public void synch() {}
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\LobStoreMem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */