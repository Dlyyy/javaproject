package org.hsqldb.lib;

public class ArrayCounter
{
  public static int[] countSegments(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    int[] arrayOfInt = new int[paramInt2];
    if (paramInt3 <= 0) {
      return arrayOfInt;
    }
    for (int k = 0; k < paramInt1; k++)
    {
      int j = paramArrayOfInt[k];
      if ((j >= paramInt4) && (j < paramInt5))
      {
        int i = (j - paramInt4) / paramInt3;
        arrayOfInt[i] += 1;
      }
    }
    return arrayOfInt;
  }
  
  public static int rank(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    int i = 0;
    int k;
    for (int j = paramInt4;; j = paramInt3 + k < paramInt4 ? paramInt3 + k : paramInt4)
    {
      k = calcInterval(256, paramInt3, j);
      int[] arrayOfInt = countSegments(paramArrayOfInt, paramInt1, 256, k, paramInt3, j);
      for (int m = 0; (m < arrayOfInt.length) && (i + arrayOfInt[m] < paramInt2); m++)
      {
        i += arrayOfInt[m];
        paramInt3 += k;
      }
      if (i + paramInt5 >= paramInt2) {
        return paramInt3;
      }
      if (k <= 1) {
        return paramInt3;
      }
    }
  }
  
  static int calcInterval(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramInt3 - paramInt2;
    if (i <= 0) {
      return 0;
    }
    int j = i % paramInt1 == 0 ? 0 : 1;
    return i / paramInt1 + j;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\ArrayCounter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */