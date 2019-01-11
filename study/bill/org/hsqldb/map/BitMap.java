package org.hsqldb.map;

import org.hsqldb.lib.ArrayUtil;

public class BitMap
{
  private boolean canChangeSize;
  private int initialSize;
  private int[] map;
  private int limitPos;
  
  public BitMap(int paramInt, boolean paramBoolean)
  {
    int i = paramInt / 32;
    if ((paramInt == 0) || (paramInt % 32 != 0)) {
      i++;
    }
    this.map = new int[i];
    this.canChangeSize = paramBoolean;
    this.limitPos = paramInt;
    this.initialSize = paramInt;
  }
  
  public BitMap(int[] paramArrayOfInt)
  {
    this.map = paramArrayOfInt;
    this.initialSize = (paramArrayOfInt.length * 32);
    this.limitPos = this.initialSize;
    this.canChangeSize = false;
  }
  
  public BitMap duplicate()
  {
    BitMap localBitMap = new BitMap((int[])ArrayUtil.duplicateArray(this.map));
    localBitMap.canChangeSize = this.canChangeSize;
    localBitMap.initialSize = this.initialSize;
    localBitMap.limitPos = this.limitPos;
    return localBitMap;
  }
  
  public int size()
  {
    return this.limitPos;
  }
  
  public void setSize(int paramInt)
  {
    if (!this.canChangeSize) {
      throw new UnsupportedOperationException("BitMap");
    }
    ensureCapacity(paramInt);
    if (this.limitPos > paramInt)
    {
      unsetRange(paramInt, this.limitPos - paramInt);
      this.limitPos = paramInt;
    }
  }
  
  public void reset()
  {
    for (int i = 0; i < this.map.length; i++) {
      this.map[i] = 0;
    }
    this.limitPos = this.initialSize;
  }
  
  public int countSetMatches(BitMap paramBitMap)
  {
    int i = 0;
    for (int j = 0; j < this.map.length; j++)
    {
      int k = this.map[j] & paramBitMap.map[j];
      if (k != 0) {
        i += Integer.bitCount(k);
      }
    }
    return i;
  }
  
  public int setRange(int paramInt1, int paramInt2)
  {
    return setOrUnsetRange(paramInt1, paramInt2, true);
  }
  
  public int unsetRange(int paramInt1, int paramInt2)
  {
    return setOrUnsetRange(paramInt1, paramInt2, false);
  }
  
  private int setOrUnsetRange(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (paramInt2 == 0) {
      return 0;
    }
    ensureCapacity(paramInt1 + paramInt2);
    int i = paramInt1 >> 5;
    int j = paramInt1 + paramInt2 - 1 >> 5;
    int k = -1 >>> (paramInt1 & 0x1F);
    int m = Integer.MIN_VALUE >> (paramInt1 + paramInt2 - 1 & 0x1F);
    if (i == j) {
      k &= m;
    }
    int n = this.map[i];
    int i1 = Integer.bitCount(n & k);
    if (paramBoolean)
    {
      this.map[i] = (n | k);
    }
    else
    {
      k ^= 0xFFFFFFFF;
      this.map[i] = (n & k);
    }
    if (i != j)
    {
      n = this.map[j];
      i1 += Integer.bitCount(n & m);
      if (paramBoolean)
      {
        this.map[j] = (n | m);
      }
      else
      {
        m ^= 0xFFFFFFFF;
        this.map[j] = (n & m);
      }
      for (int i2 = i + 1; i2 < j; i2++)
      {
        i1 += Integer.bitCount(this.map[i2]);
        this.map[i2] = (paramBoolean ? -1 : 0);
      }
    }
    return paramBoolean ? paramInt2 - i1 : i1;
  }
  
  public int setValue(int paramInt, boolean paramBoolean)
  {
    return paramBoolean ? set(paramInt) : unset(paramInt);
  }
  
  public int set(int paramInt)
  {
    ensureCapacity(paramInt + 1);
    int i = paramInt >> 5;
    int j = Integer.MIN_VALUE >>> (paramInt & 0x1F);
    int k = this.map[i];
    int m = (k & j) == 0 ? 0 : 1;
    this.map[i] = (k | j);
    return m;
  }
  
  public int unset(int paramInt)
  {
    ensureCapacity(paramInt + 1);
    int i = paramInt >> 5;
    int j = Integer.MIN_VALUE >>> (paramInt & 0x1F);
    int k = this.map[i];
    int m = (k & j) == 0 ? 0 : 1;
    j ^= 0xFFFFFFFF;
    this.map[i] = (k & j);
    return m;
  }
  
  public int get(int paramInt)
  {
    if (paramInt >= this.limitPos) {
      throw new ArrayIndexOutOfBoundsException(paramInt);
    }
    int i = paramInt >> 5;
    int j = Integer.MIN_VALUE >>> (paramInt & 0x1F);
    int k = this.map[i];
    return (k & j) == 0 ? 0 : 1;
  }
  
  public boolean isSet(int paramInt)
  {
    return get(paramInt) == 1;
  }
  
  public void set(BitMap paramBitMap)
  {
    for (int i = 0; i < this.map.length; i++)
    {
      int j = paramBitMap.map[i];
      this.map[i] |= j;
    }
  }
  
  public int countSet(int paramInt1, int paramInt2)
  {
    int i = 0;
    for (int j = paramInt1; j < paramInt1 + paramInt2; j++) {
      if (isSet(j)) {
        i++;
      }
    }
    return i;
  }
  
  public int countSetBits()
  {
    int i = 0;
    int k;
    for (int j = 0; j < this.limitPos / 32; j++)
    {
      k = this.map[j];
      if (k != 0) {
        if (k == -1) {
          i += 32;
        } else {
          i += Integer.bitCount(k);
        }
      }
    }
    if (this.limitPos % 32 != 0)
    {
      j = Integer.MIN_VALUE >> (this.limitPos - 1 & 0x1F);
      k = this.map[(this.limitPos / 32)] & j;
      i += Integer.bitCount(k);
    }
    return i;
  }
  
  public int countSetBitsEnd()
  {
    int i = 0;
    for (int j = this.limitPos / 32 - 1; j >= 0; j--) {
      if (this.map[j] == -1)
      {
        i += 32;
      }
      else
      {
        int k = countSetBitsEnd(this.map[j]);
        i += k;
        break;
      }
    }
    return i;
  }
  
  public int[] getIntArray()
  {
    return this.map;
  }
  
  public byte[] getBytes()
  {
    byte[] arrayOfByte = new byte[(this.limitPos + 7) / 8];
    if (arrayOfByte.length == 0) {
      return arrayOfByte;
    }
    int i = 0;
    for (;;)
    {
      int j = this.map[(i / 4)];
      arrayOfByte[(i++)] = ((byte)(j >>> 24));
      if (i == arrayOfByte.length) {
        break;
      }
      arrayOfByte[(i++)] = ((byte)(j >>> 16));
      if (i == arrayOfByte.length) {
        break;
      }
      arrayOfByte[(i++)] = ((byte)(j >>> 8));
      if (i == arrayOfByte.length) {
        break;
      }
      arrayOfByte[(i++)] = ((byte)j);
      if (i == arrayOfByte.length) {
        break;
      }
    }
    return arrayOfByte;
  }
  
  private void ensureCapacity(int paramInt)
  {
    if ((paramInt > this.limitPos) && (!this.canChangeSize)) {
      throw new ArrayStoreException("BitMap extend");
    }
    if (paramInt <= this.map.length * 32)
    {
      if (paramInt > this.limitPos) {
        this.limitPos = paramInt;
      }
      return;
    }
    int i = this.map.length;
    while (paramInt > i * 32) {
      i *= 2;
    }
    int[] arrayOfInt = new int[i];
    System.arraycopy(this.map, 0, arrayOfInt, 0, this.map.length);
    this.map = arrayOfInt;
    this.limitPos = paramInt;
  }
  
  public static int countSetBitsEnd(int paramInt)
  {
    int i = 1;
    for (int j = 0; (j < 32) && ((paramInt & i) != 0); j++) {
      paramInt >>= 1;
    }
    return j;
  }
  
  public static int countUnsetBitsStart(int paramInt)
  {
    int i = Integer.MIN_VALUE;
    int j = 0;
    if (paramInt == 0) {
      return 32;
    }
    while ((j < 32) && ((paramInt & i) == 0))
    {
      i >>>= 1;
      j++;
    }
    return j;
  }
  
  public static int setByte(int paramInt1, byte paramByte, int paramInt2)
  {
    int i = (paramByte & 0xFF) << 24 - paramInt2;
    int j = -16777216 >>> paramInt2;
    j ^= 0xFFFFFFFF;
    paramInt1 &= j;
    return paramInt1 | i;
  }
  
  public static int set(int paramInt1, int paramInt2)
  {
    int i = Integer.MIN_VALUE >>> paramInt2;
    return paramInt1 | i;
  }
  
  public static byte set(byte paramByte, int paramInt)
  {
    byte b = 128 >>> paramInt;
    return (byte)(paramByte | b);
  }
  
  public static int unset(int paramInt1, int paramInt2)
  {
    int i = Integer.MIN_VALUE >>> paramInt2;
    i ^= 0xFFFFFFFF;
    return paramInt1 & i;
  }
  
  public static boolean isSet(int paramInt1, int paramInt2)
  {
    int i = Integer.MIN_VALUE >>> paramInt2;
    return (paramInt1 & i) != 0;
  }
  
  public static boolean isSet(byte paramByte, int paramInt)
  {
    byte b = 128 >>> paramInt;
    return (paramByte & b) != 0;
  }
  
  public static boolean isSet(byte[] paramArrayOfByte, int paramInt)
  {
    int i = 128 >>> (paramInt & 0x7);
    int j = paramInt / 8;
    if (j >= paramArrayOfByte.length) {
      return false;
    }
    int k = paramArrayOfByte[j];
    return (k & i) != 0;
  }
  
  public static void unset(byte[] paramArrayOfByte, int paramInt)
  {
    int i = 128 >>> (paramInt & 0x7);
    i ^= 0xFFFFFFFF;
    int j = paramInt / 8;
    if (j >= paramArrayOfByte.length) {
      return;
    }
    int k = paramArrayOfByte[j];
    paramArrayOfByte[j] = ((byte)(k & i));
  }
  
  public static void set(byte[] paramArrayOfByte, int paramInt)
  {
    int i = 128 >>> (paramInt & 0x7);
    int j = paramInt / 8;
    if (j >= paramArrayOfByte.length) {
      return;
    }
    int k = paramArrayOfByte[j];
    paramArrayOfByte[j] = ((byte)(k | i));
  }
  
  public static void and(byte[] paramArrayOfByte, int paramInt1, byte paramByte, int paramInt2)
  {
    int i = paramInt1 & 0x7;
    int j = (paramByte & 0xFF) >>> i;
    int k = 255 >> i;
    int m = paramInt1 / 8;
    if (paramInt2 < 8)
    {
      k >>>= 8 - paramInt2;
      k <<= 8 - paramInt2;
    }
    j &= k;
    k ^= 0xFFFFFFFF;
    if (m >= paramArrayOfByte.length) {
      return;
    }
    int n = paramArrayOfByte[m];
    paramArrayOfByte[m] = ((byte)(n & k));
    n = (byte)(n & j);
    paramArrayOfByte[m] = ((byte)(paramArrayOfByte[m] | n));
    if (i == 0) {
      return;
    }
    i = 8 - i;
    if (paramInt2 > i)
    {
      j = (paramByte & 0xFF) << 8 >>> i;
      k = 65280 >>> i;
      k ^= 0xFFFFFFFF;
      n = paramArrayOfByte[(m + 1)];
      paramArrayOfByte[(m + 1)] = ((byte)(n & k));
      n = (byte)(n & j);
      paramArrayOfByte[(m + 1)] = ((byte)(paramArrayOfByte[(m + 1)] | n));
    }
  }
  
  public static void or(byte[] paramArrayOfByte, int paramInt1, byte paramByte, int paramInt2)
  {
    int i = paramInt1 & 0x7;
    int j = (paramByte & 0xFF) >>> i;
    int k = paramInt1 / 8;
    if (k >= paramArrayOfByte.length) {
      return;
    }
    int m = (byte)(paramArrayOfByte[k] | j);
    paramArrayOfByte[k] = m;
    if (i == 0) {
      return;
    }
    i = 8 - i;
    if (paramInt2 > i)
    {
      j = (paramByte & 0xFF) << 8 >>> i;
      m = (byte)(paramArrayOfByte[(k + 1)] | j);
      paramArrayOfByte[(k + 1)] = m;
    }
  }
  
  public static void overlay(byte[] paramArrayOfByte, int paramInt1, byte paramByte, int paramInt2)
  {
    int i = paramInt1 & 0x7;
    int j = (paramByte & 0xFF) >>> i;
    int k = 255 >> i;
    int m = paramInt1 / 8;
    if (paramInt2 < 8)
    {
      k >>>= 8 - paramInt2;
      k <<= 8 - paramInt2;
    }
    j &= k;
    k ^= 0xFFFFFFFF;
    if (m >= paramArrayOfByte.length) {
      return;
    }
    int n = paramArrayOfByte[m];
    n = (byte)(n & k);
    paramArrayOfByte[m] = ((byte)(n | j));
    if (i == 0) {
      return;
    }
    i = 8 - i;
    if (paramInt2 > i)
    {
      j = (paramByte & 0xFF) << 8 >>> i;
      k = 65280 >>> i;
      k ^= 0xFFFFFFFF;
      n = paramArrayOfByte[(m + 1)];
      n = (byte)(n & k);
      paramArrayOfByte[(m + 1)] = ((byte)(n | j));
    }
  }
  
  public static byte[] and(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    int i = paramArrayOfByte1.length > paramArrayOfByte2.length ? paramArrayOfByte1.length : paramArrayOfByte2.length;
    int j = paramArrayOfByte1.length > paramArrayOfByte2.length ? paramArrayOfByte2.length : paramArrayOfByte1.length;
    byte[] arrayOfByte = new byte[i];
    for (int k = 0; k < j; k++) {
      arrayOfByte[k] = ((byte)(paramArrayOfByte1[k] & paramArrayOfByte2[k]));
    }
    return arrayOfByte;
  }
  
  public static byte[] or(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    int i = paramArrayOfByte1.length > paramArrayOfByte2.length ? paramArrayOfByte1.length : paramArrayOfByte2.length;
    int j = paramArrayOfByte1.length > paramArrayOfByte2.length ? paramArrayOfByte2.length : paramArrayOfByte1.length;
    byte[] arrayOfByte1 = new byte[i];
    if (i != j)
    {
      byte[] arrayOfByte2 = paramArrayOfByte1.length > paramArrayOfByte2.length ? paramArrayOfByte1 : paramArrayOfByte2;
      System.arraycopy(arrayOfByte2, j, arrayOfByte1, j, i - j);
    }
    for (int k = 0; k < j; k++) {
      arrayOfByte1[k] = ((byte)(paramArrayOfByte1[k] | paramArrayOfByte2[k]));
    }
    return arrayOfByte1;
  }
  
  public static byte[] xor(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    int i = paramArrayOfByte1.length > paramArrayOfByte2.length ? paramArrayOfByte1.length : paramArrayOfByte2.length;
    int j = paramArrayOfByte1.length > paramArrayOfByte2.length ? paramArrayOfByte2.length : paramArrayOfByte1.length;
    byte[] arrayOfByte1 = new byte[i];
    if (i != j)
    {
      byte[] arrayOfByte2 = paramArrayOfByte1.length > paramArrayOfByte2.length ? paramArrayOfByte1 : paramArrayOfByte2;
      System.arraycopy(arrayOfByte2, j, arrayOfByte1, j, i - j);
    }
    for (int k = 0; k < j; k++) {
      arrayOfByte1[k] = ((byte)(paramArrayOfByte1[k] ^ paramArrayOfByte2[k]));
    }
    return arrayOfByte1;
  }
  
  public static byte[] not(byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length];
    for (int i = 0; i < paramArrayOfByte.length; i++) {
      arrayOfByte[i] = ((byte)(paramArrayOfByte[i] ^ 0xFFFFFFFF));
    }
    return arrayOfByte;
  }
  
  public static boolean hasAnyBitSet(byte[] paramArrayOfByte)
  {
    for (int i = 0; i < paramArrayOfByte.length; i++) {
      if (paramArrayOfByte[i] != 0) {
        return true;
      }
    }
    return false;
  }
  
  public static byte[] leftShift(byte[] paramArrayOfByte, int paramInt)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length];
    int i = paramInt / 8;
    if (i >= paramArrayOfByte.length) {
      return arrayOfByte;
    }
    paramInt %= 8;
    int j;
    int k;
    if (paramInt == 0)
    {
      j = 0;
      for (k = i; k < paramArrayOfByte.length; k++)
      {
        arrayOfByte[j] = paramArrayOfByte[k];
        j++;
      }
    }
    else
    {
      j = 0;
      for (k = i; k < paramArrayOfByte.length; k++)
      {
        int m = (paramArrayOfByte[k] & 0xFF) << paramInt;
        arrayOfByte[j] = ((byte)m);
        if (j > 0)
        {
          int tmp102_101 = (j - 1);
          byte[] tmp102_97 = arrayOfByte;
          tmp102_97[tmp102_101] = ((byte)(tmp102_97[tmp102_101] | (byte)(m >>> 8)));
        }
        j++;
      }
    }
    return arrayOfByte;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\map\BitMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */