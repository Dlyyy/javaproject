package org.hsqldb.resources;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.hsqldb.lib.HashMap;
import org.hsqldb.lib.HsqlArrayList;

public final class ResourceBundleHandler
{
  private static final Object mutex = new Object();
  private static Locale locale = Locale.getDefault();
  private static HashMap bundleHandleMap = new HashMap();
  private static HsqlArrayList bundleList = new HsqlArrayList();
  private static final String prefix = "org.hsqldb.resources.";
  private static final Method newGetBundleMethod = getNewGetBundleMethod();
  
  /* Error */
  public static Locale getLocale()
  {
    // Byte code:
    //   0: getstatic 2	org/hsqldb/resources/ResourceBundleHandler:mutex	Ljava/lang/Object;
    //   3: dup
    //   4: astore_0
    //   5: monitorenter
    //   6: getstatic 3	org/hsqldb/resources/ResourceBundleHandler:locale	Ljava/util/Locale;
    //   9: aload_0
    //   10: monitorexit
    //   11: areturn
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   4	10	0	Ljava/lang/Object;	Object
    //   12	4	1	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   6	11	12	finally
    //   12	15	12	finally
  }
  
  public static void setLocale(Locale paramLocale)
    throws IllegalArgumentException
  {
    synchronized (mutex)
    {
      if (paramLocale == null) {
        throw new IllegalArgumentException("null locale");
      }
      locale = paramLocale;
    }
  }
  
  public static int getBundleHandle(String paramString, ClassLoader paramClassLoader)
  {
    String str1 = "org.hsqldb.resources." + paramString;
    Integer localInteger;
    synchronized (mutex)
    {
      String str2 = locale.toString() + str1;
      localInteger = (Integer)bundleHandleMap.get(str2);
      if (localInteger == null)
      {
        ResourceBundle localResourceBundle = getBundle(str1, locale, paramClassLoader);
        bundleList.add(localResourceBundle);
        localInteger = Integer.valueOf(bundleList.size() - 1);
        bundleHandleMap.put(str2, localInteger);
      }
    }
    return localInteger.intValue();
  }
  
  public static String getString(int paramInt, String paramString)
  {
    ResourceBundle localResourceBundle;
    synchronized (mutex)
    {
      if ((paramInt < 0) || (paramInt >= bundleList.size()) || (paramString == null)) {
        localResourceBundle = null;
      } else {
        localResourceBundle = (ResourceBundle)bundleList.get(paramInt);
      }
    }
    String str;
    if (localResourceBundle == null) {
      str = null;
    } else {
      try
      {
        str = localResourceBundle.getString(paramString);
      }
      catch (Exception localException)
      {
        str = null;
      }
    }
    return str;
  }
  
  private static Method getNewGetBundleMethod()
  {
    Class localClass = ResourceBundle.class;
    Class[] arrayOfClass = { String.class, Locale.class, ClassLoader.class };
    try
    {
      return localClass.getMethod("getBundle", arrayOfClass);
    }
    catch (Exception localException) {}
    return null;
  }
  
  public static ResourceBundle getBundle(String paramString, Locale paramLocale, ClassLoader paramClassLoader)
    throws NullPointerException, MissingResourceException
  {
    if (paramClassLoader == null) {
      return ResourceBundle.getBundle(paramString, paramLocale);
    }
    if (newGetBundleMethod == null) {
      return ResourceBundle.getBundle(paramString, paramLocale);
    }
    try
    {
      return (ResourceBundle)newGetBundleMethod.invoke(null, new Object[] { paramString, paramLocale, paramClassLoader });
    }
    catch (Exception localException) {}
    return ResourceBundle.getBundle(paramString, paramLocale);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\resources\ResourceBundleHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */