package org.hsqldb.map;

import java.math.BigDecimal;

public class ValuePool
{
  static ValuePoolHashMap intPool;
  static ValuePoolHashMap longPool;
  static ValuePoolHashMap doublePool;
  static ValuePoolHashMap bigdecimalPool;
  static ValuePoolHashMap stringPool;
  static final int SPACE_STRING_SIZE = 64;
  static final int DEFAULT_VALUE_POOL_SIZE = 4096;
  static final int[] defaultPoolLookupSize = { 4096, 4096, 4096, 4096, 4096 };
  static final int POOLS_COUNT = defaultPoolLookupSize.length;
  static final int defaultSizeFactor = 2;
  static final int defaultMaxStringLength = 16;
  static ValuePoolHashMap[] poolList;
  static int maxStringLength;
  public static final String spaceString;
  public static final Integer INTEGER_0 = getInt(0);
  public static final Integer INTEGER_1 = getInt(1);
  public static final Integer INTEGER_2 = getInt(2);
  public static final Integer INTEGER_MAX = getInt(Integer.MAX_VALUE);
  public static final BigDecimal BIG_DECIMAL_0 = getBigDecimal(BigDecimal.valueOf(0L));
  public static final BigDecimal BIG_DECIMAL_1 = getBigDecimal(new BigDecimal(1));
  public static final String[] emptyStringArray = new String[0];
  public static final Object[] emptyObjectArray = new Object[0];
  public static final int[] emptyIntArray = new int[0];
  
  private static void initPool()
  {
    int[] arrayOfInt = defaultPoolLookupSize;
    int i = 2;
    synchronized (ValuePool.class)
    {
      maxStringLength = 16;
      poolList = new ValuePoolHashMap[POOLS_COUNT];
      for (int j = 0; j < POOLS_COUNT; j++)
      {
        int k = arrayOfInt[j];
        poolList[j] = new ValuePoolHashMap(k, k * i, 2);
      }
      intPool = poolList[0];
      longPool = poolList[1];
      doublePool = poolList[2];
      bigdecimalPool = poolList[3];
      stringPool = poolList[4];
    }
  }
  
  public static int getMaxStringLength()
  {
    return maxStringLength;
  }
  
  public static void resetPool(int[] paramArrayOfInt, int paramInt)
  {
    synchronized (ValuePool.class)
    {
      for (int i = 0; i < POOLS_COUNT; i++)
      {
        poolList[i].clear();
        poolList[i].resetCapacity(paramArrayOfInt[i] * paramInt, 2);
      }
    }
  }
  
  public static void resetPool()
  {
    synchronized (ValuePool.class)
    {
      resetPool(defaultPoolLookupSize, 2);
    }
  }
  
  public static void clearPool()
  {
    synchronized (ValuePool.class)
    {
      for (int i = 0; i < POOLS_COUNT; i++) {
        poolList[i].clear();
      }
    }
  }
  
  /* Error */
  public static Integer getInt(int paramInt)
  {
    // Byte code:
    //   0: getstatic 9	org/hsqldb/map/ValuePool:intPool	Lorg/hsqldb/map/ValuePoolHashMap;
    //   3: dup
    //   4: astore_1
    //   5: monitorenter
    //   6: getstatic 9	org/hsqldb/map/ValuePool:intPool	Lorg/hsqldb/map/ValuePoolHashMap;
    //   9: iload_0
    //   10: invokevirtual 17	org/hsqldb/map/ValuePoolHashMap:getOrAddInteger	(I)Ljava/lang/Integer;
    //   13: aload_1
    //   14: monitorexit
    //   15: areturn
    //   16: astore_2
    //   17: aload_1
    //   18: monitorexit
    //   19: aload_2
    //   20: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	21	0	paramInt	int
    //   4	14	1	Ljava/lang/Object;	Object
    //   16	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   6	15	16	finally
    //   16	19	16	finally
  }
  
  /* Error */
  public static Long getLong(long paramLong)
  {
    // Byte code:
    //   0: getstatic 10	org/hsqldb/map/ValuePool:longPool	Lorg/hsqldb/map/ValuePoolHashMap;
    //   3: dup
    //   4: astore_2
    //   5: monitorenter
    //   6: getstatic 10	org/hsqldb/map/ValuePool:longPool	Lorg/hsqldb/map/ValuePoolHashMap;
    //   9: lload_0
    //   10: invokevirtual 18	org/hsqldb/map/ValuePoolHashMap:getOrAddLong	(J)Ljava/lang/Long;
    //   13: aload_2
    //   14: monitorexit
    //   15: areturn
    //   16: astore_3
    //   17: aload_2
    //   18: monitorexit
    //   19: aload_3
    //   20: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	21	0	paramLong	long
    //   4	14	2	Ljava/lang/Object;	Object
    //   16	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   6	15	16	finally
    //   16	19	16	finally
  }
  
  /* Error */
  public static Double getDouble(long paramLong)
  {
    // Byte code:
    //   0: getstatic 11	org/hsqldb/map/ValuePool:doublePool	Lorg/hsqldb/map/ValuePoolHashMap;
    //   3: dup
    //   4: astore_2
    //   5: monitorenter
    //   6: getstatic 11	org/hsqldb/map/ValuePool:doublePool	Lorg/hsqldb/map/ValuePoolHashMap;
    //   9: lload_0
    //   10: invokevirtual 19	org/hsqldb/map/ValuePoolHashMap:getOrAddDouble	(J)Ljava/lang/Double;
    //   13: aload_2
    //   14: monitorexit
    //   15: areturn
    //   16: astore_3
    //   17: aload_2
    //   18: monitorexit
    //   19: aload_3
    //   20: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	21	0	paramLong	long
    //   4	14	2	Ljava/lang/Object;	Object
    //   16	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   6	15	16	finally
    //   16	19	16	finally
  }
  
  /* Error */
  public static String getString(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: ifnull +13 -> 14
    //   4: aload_0
    //   5: invokevirtual 20	java/lang/String:length	()I
    //   8: getstatic 4	org/hsqldb/map/ValuePool:maxStringLength	I
    //   11: if_icmple +5 -> 16
    //   14: aload_0
    //   15: areturn
    //   16: getstatic 13	org/hsqldb/map/ValuePool:stringPool	Lorg/hsqldb/map/ValuePoolHashMap;
    //   19: dup
    //   20: astore_1
    //   21: monitorenter
    //   22: getstatic 13	org/hsqldb/map/ValuePool:stringPool	Lorg/hsqldb/map/ValuePoolHashMap;
    //   25: aload_0
    //   26: invokevirtual 21	org/hsqldb/map/ValuePoolHashMap:getOrAddString	(Ljava/lang/Object;)Ljava/lang/String;
    //   29: aload_1
    //   30: monitorexit
    //   31: areturn
    //   32: astore_2
    //   33: aload_1
    //   34: monitorexit
    //   35: aload_2
    //   36: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	37	0	paramString	String
    //   20	14	1	Ljava/lang/Object;	Object
    //   32	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   22	31	32	finally
    //   32	35	32	finally
  }
  
  /* Error */
  public static String getSubString(String paramString, int paramInt1, int paramInt2)
  {
    // Byte code:
    //   0: getstatic 13	org/hsqldb/map/ValuePool:stringPool	Lorg/hsqldb/map/ValuePoolHashMap;
    //   3: dup
    //   4: astore_3
    //   5: monitorenter
    //   6: getstatic 13	org/hsqldb/map/ValuePool:stringPool	Lorg/hsqldb/map/ValuePoolHashMap;
    //   9: aload_0
    //   10: iload_1
    //   11: iload_2
    //   12: invokevirtual 22	java/lang/String:substring	(II)Ljava/lang/String;
    //   15: invokevirtual 21	org/hsqldb/map/ValuePoolHashMap:getOrAddString	(Ljava/lang/Object;)Ljava/lang/String;
    //   18: aload_3
    //   19: monitorexit
    //   20: areturn
    //   21: astore 4
    //   23: aload_3
    //   24: monitorexit
    //   25: aload 4
    //   27: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	28	0	paramString	String
    //   0	28	1	paramInt1	int
    //   0	28	2	paramInt2	int
    //   4	20	3	Ljava/lang/Object;	Object
    //   21	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   6	20	21	finally
    //   21	25	21	finally
  }
  
  /* Error */
  public static BigDecimal getBigDecimal(BigDecimal paramBigDecimal)
  {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull +5 -> 6
    //   4: aload_0
    //   5: areturn
    //   6: getstatic 12	org/hsqldb/map/ValuePool:bigdecimalPool	Lorg/hsqldb/map/ValuePoolHashMap;
    //   9: dup
    //   10: astore_1
    //   11: monitorenter
    //   12: getstatic 12	org/hsqldb/map/ValuePool:bigdecimalPool	Lorg/hsqldb/map/ValuePoolHashMap;
    //   15: aload_0
    //   16: invokevirtual 23	org/hsqldb/map/ValuePoolHashMap:getOrAddObject	(Ljava/lang/Object;)Ljava/lang/Object;
    //   19: checkcast 24	java/math/BigDecimal
    //   22: aload_1
    //   23: monitorexit
    //   24: areturn
    //   25: astore_2
    //   26: aload_1
    //   27: monitorexit
    //   28: aload_2
    //   29: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	30	0	paramBigDecimal	BigDecimal
    //   10	17	1	Ljava/lang/Object;	Object
    //   25	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   12	24	25	finally
    //   25	28	25	finally
  }
  
  public static Boolean getBoolean(boolean paramBoolean)
  {
    return paramBoolean ? Boolean.TRUE : Boolean.FALSE;
  }
  
  static
  {
    char[] arrayOfChar = new char[64];
    for (int i = 0; i < 64; i++) {
      arrayOfChar[i] = ' ';
    }
    spaceString = new String(arrayOfChar);
    initPool();
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\map\ValuePool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */