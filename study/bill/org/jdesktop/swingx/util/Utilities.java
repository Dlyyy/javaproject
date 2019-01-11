/*     */ package org.jdesktop.swingx.util;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Insets;
/*     */ import java.awt.KeyboardFocusManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.text.BreakIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.SwingUtilities;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Utilities
/*     */ {
/*     */   private static final int CTRL_WILDCARD_MASK = 32768;
/*     */   private static final int ALT_WILDCARD_MASK = 65536;
/*     */   public static final int OS_WINNT = 1;
/*     */   public static final int OS_WIN95 = 2;
/*     */   public static final int OS_WIN98 = 4;
/*     */   public static final int OS_SOLARIS = 8;
/*     */   public static final int OS_LINUX = 16;
/*     */   public static final int OS_HP = 32;
/*     */   public static final int OS_AIX = 64;
/*     */   public static final int OS_IRIX = 128;
/*     */   public static final int OS_SUNOS = 256;
/*     */   public static final int OS_TRU64 = 512;
/*     */   public static final int OS_OS2 = 2048;
/*     */   public static final int OS_MAC = 4096;
/*     */   public static final int OS_WIN2000 = 8192;
/*     */   public static final int OS_VMS = 16384;
/*     */   public static final int OS_WIN_OTHER = 32768;
/*     */   public static final int OS_OTHER = 65536;
/*     */   public static final int OS_FREEBSD = 131072;
/*     */   public static final int OS_WINDOWS_MASK = 40967;
/*     */   public static final int OS_UNIX_MASK = 136184;
/*     */   public static final int TYPICAL_WINDOWS_TASKBAR_HEIGHT = 27;
/*     */   private static final int TYPICAL_MACOSX_MENU_HEIGHT = 24;
/* 133 */   private static int operatingSystem = -1;
/*     */   
/*     */ 
/*     */ 
/*     */   private static Reference<Object> namesAndValues;
/*     */   
/*     */ 
/*     */ 
/*     */   public static int getOperatingSystem()
/*     */   {
/* 143 */     if (operatingSystem == -1) {
/* 144 */       String osName = System.getProperty("os.name");
/*     */       
/* 146 */       if ("Windows NT".equals(osName)) {
/* 147 */         operatingSystem = 1;
/* 148 */       } else if ("Windows 95".equals(osName)) {
/* 149 */         operatingSystem = 2;
/* 150 */       } else if ("Windows 98".equals(osName)) {
/* 151 */         operatingSystem = 4;
/* 152 */       } else if ("Windows 2000".equals(osName)) {
/* 153 */         operatingSystem = 8192;
/* 154 */       } else if (osName.startsWith("Windows ")) {
/* 155 */         operatingSystem = 32768;
/* 156 */       } else if ("Solaris".equals(osName)) {
/* 157 */         operatingSystem = 8;
/* 158 */       } else if (osName.startsWith("SunOS")) {
/* 159 */         operatingSystem = 8;
/*     */ 
/*     */       }
/* 162 */       else if (osName.endsWith("Linux")) {
/* 163 */         operatingSystem = 16;
/* 164 */       } else if ("HP-UX".equals(osName)) {
/* 165 */         operatingSystem = 32;
/* 166 */       } else if ("AIX".equals(osName)) {
/* 167 */         operatingSystem = 64;
/* 168 */       } else if ("Irix".equals(osName)) {
/* 169 */         operatingSystem = 128;
/* 170 */       } else if ("SunOS".equals(osName)) {
/* 171 */         operatingSystem = 256;
/* 172 */       } else if ("Digital UNIX".equals(osName)) {
/* 173 */         operatingSystem = 512;
/* 174 */       } else if ("OS/2".equals(osName)) {
/* 175 */         operatingSystem = 2048;
/* 176 */       } else if ("OpenVMS".equals(osName)) {
/* 177 */         operatingSystem = 16384;
/* 178 */       } else if (osName.equals("Mac OS X")) {
/* 179 */         operatingSystem = 4096;
/* 180 */       } else if (osName.startsWith("Darwin")) {
/* 181 */         operatingSystem = 4096;
/* 182 */       } else if (osName.toLowerCase(Locale.US).startsWith("freebsd")) {
/* 183 */         operatingSystem = 131072;
/*     */       } else {
/* 185 */         operatingSystem = 65536;
/*     */       }
/*     */     }
/* 188 */     return operatingSystem;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isWindows()
/*     */   {
/* 195 */     return (getOperatingSystem() & 0xA007) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isUnix()
/*     */   {
/* 203 */     return (getOperatingSystem() & 0x213F8) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isLargeFrameIcons()
/*     */   {
/* 211 */     return (getOperatingSystem() == 8) || (getOperatingSystem() == 32);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static GraphicsConfiguration getCurrentGraphicsConfiguration()
/*     */   {
/* 223 */     Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
/* 224 */     if (focusOwner != null) {
/* 225 */       Window w = SwingUtilities.getWindowAncestor(focusOwner);
/* 226 */       if (w != null) {
/* 227 */         return w.getGraphicsConfiguration();
/*     */       }
/*     */     }
/*     */     
/* 231 */     return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Rectangle getUsableScreenBounds()
/*     */   {
/* 246 */     return getUsableScreenBounds(getCurrentGraphicsConfiguration());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Rectangle getUsableScreenBounds(GraphicsConfiguration gconf)
/*     */   {
/* 260 */     if (gconf == null) {
/* 261 */       gconf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
/*     */     }
/*     */     
/* 264 */     Rectangle bounds = new Rectangle(gconf.getBounds());
/*     */     
/*     */ 
/*     */ 
/* 268 */     String str = System.getProperty("netbeans.screen.insets");
/*     */     
/* 270 */     if (str != null) {
/* 271 */       StringTokenizer st = new StringTokenizer(str, ", ");
/*     */       
/* 273 */       if (st.countTokens() == 4) {
/*     */         try {
/* 275 */           bounds.y = Integer.parseInt(st.nextToken());
/* 276 */           bounds.x = Integer.parseInt(st.nextToken());
/* 277 */           bounds.height -= bounds.y + Integer.parseInt(st.nextToken());
/* 278 */           bounds.width -= bounds.x + Integer.parseInt(st.nextToken());
/*     */         } catch (NumberFormatException ex) {
/* 280 */           Logger.getAnonymousLogger().log(Level.WARNING, null, ex);
/*     */         }
/*     */       }
/*     */       
/* 284 */       return bounds;
/*     */     }
/*     */     
/* 287 */     str = System.getProperty("netbeans.taskbar.height");
/*     */     
/* 289 */     if (str != null) {
/* 290 */       bounds.height -= Integer.getInteger(str, 0).intValue();
/*     */       
/* 292 */       return bounds;
/*     */     }
/*     */     try
/*     */     {
/* 296 */       Toolkit toolkit = Toolkit.getDefaultToolkit();
/* 297 */       Insets insets = toolkit.getScreenInsets(gconf);
/* 298 */       bounds.y += insets.top;
/* 299 */       bounds.x += insets.left;
/* 300 */       bounds.height -= insets.top + insets.bottom;
/* 301 */       bounds.width -= insets.left + insets.right;
/*     */     } catch (Exception ex) {
/* 303 */       Logger.getAnonymousLogger().log(Level.WARNING, null, ex);
/*     */     }
/*     */     
/* 306 */     return bounds;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static synchronized HashMap[] initNameAndValues()
/*     */   {
/* 317 */     if (namesAndValues != null) {
/* 318 */       HashMap[] arr = (HashMap[])namesAndValues.get();
/*     */       
/* 320 */       if (arr != null) {
/* 321 */         return arr;
/*     */       }
/*     */     }
/*     */     
/*     */     Field[] fields;
/*     */     try
/*     */     {
/* 328 */       fields = KeyEvent.class.getDeclaredFields();
/*     */ 
/*     */     }
/*     */     catch (SecurityException e)
/*     */     {
/* 333 */       fields = new Field[0];
/*     */     }
/*     */     
/* 336 */     HashMap<String, Integer> names = new HashMap(fields.length * 4 / 3 + 5, 0.75F);
/* 337 */     HashMap<Integer, String> values = new HashMap(fields.length * 4 / 3 + 5, 0.75F);
/*     */     
/* 339 */     for (int i = 0; i < fields.length; i++) {
/* 340 */       if (Modifier.isStatic(fields[i].getModifiers())) {
/* 341 */         String name = fields[i].getName();
/*     */         
/* 343 */         if (name.startsWith("VK_"))
/*     */         {
/*     */ 
/* 346 */           name = name.substring(3);
/*     */           try
/*     */           {
/* 349 */             int numb = fields[i].getInt(null);
/* 350 */             Integer value = new Integer(numb);
/* 351 */             names.put(name, value);
/* 352 */             values.put(value, name);
/*     */           }
/*     */           catch (IllegalArgumentException ex) {}catch (IllegalAccessException ex) {}
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 360 */     if (names.get("CONTEXT_MENU") == null)
/*     */     {
/* 362 */       Integer n = new Integer(524);
/* 363 */       names.put("CONTEXT_MENU", n);
/* 364 */       values.put(n, "CONTEXT_MENU");
/*     */       
/* 366 */       n = new Integer(525);
/* 367 */       names.put("WINDOWS", n);
/* 368 */       values.put(n, "WINDOWS");
/*     */     }
/*     */     
/* 371 */     HashMap[] arr = { names, values };
/*     */     
/* 373 */     namesAndValues = new SoftReference(arr);
/*     */     
/* 375 */     return arr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String keyToString(KeyStroke stroke)
/*     */   {
/* 384 */     StringBuffer sb = new StringBuffer();
/*     */     
/*     */ 
/* 387 */     if (addModifiers(sb, stroke.getModifiers())) {
/* 388 */       sb.append('-');
/*     */     }
/*     */     
/* 391 */     HashMap[] namesAndValues = initNameAndValues();
/*     */     
/* 393 */     String c = (String)namesAndValues[1].get(new Integer(stroke.getKeyCode()));
/*     */     
/* 395 */     if (c == null) {
/* 396 */       sb.append(stroke.getKeyChar());
/*     */     } else {
/* 398 */       sb.append(c);
/*     */     }
/*     */     
/* 401 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static KeyStroke stringToKey(String s)
/*     */   {
/* 444 */     StringTokenizer st = new StringTokenizer(s.toUpperCase(Locale.ENGLISH), "-", true);
/*     */     
/* 446 */     int needed = 0;
/*     */     
/* 448 */     HashMap names = initNameAndValues()[0];
/*     */     
/* 450 */     int lastModif = -1;
/*     */     try
/*     */     {
/*     */       for (;;) {
/* 454 */         String el = st.nextToken();
/*     */         
/*     */ 
/* 457 */         if (el.equals("-"))
/*     */         {
/* 459 */           if (lastModif != -1) {
/* 460 */             needed |= lastModif;
/* 461 */             lastModif = -1;
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */         }
/* 468 */         else if (st.hasMoreElements())
/*     */         {
/* 470 */           lastModif = readModifiers(el);
/*     */         }
/*     */         else {
/* 473 */           Integer i = (Integer)names.get(el);
/* 474 */           boolean wildcard = (needed & 0x8000) != 0;
/*     */           
/*     */ 
/*     */ 
/* 478 */           needed &= 0xFFFF7FFF;
/*     */           
/* 480 */           boolean macAlt = (needed & 0x10000) != 0;
/* 481 */           needed &= 0xFFFEFFFF;
/*     */           
/* 483 */           if (i != null)
/*     */           {
/* 485 */             if (wildcard) {
/* 486 */               needed |= getMenuShortCutKeyMask();
/*     */               
/* 488 */               if (((getOperatingSystem() & 0x1000) != 0) && 
/* 489 */                 (!usableKeyOnMac(i.intValue(), needed))) {
/* 490 */                 needed &= (getMenuShortCutKeyMask() ^ 0xFFFFFFFF);
/* 491 */                 needed |= 0x2;
/*     */               }
/*     */             }
/*     */             
/*     */ 
/* 496 */             if (macAlt) {
/* 497 */               if (getOperatingSystem() == 4096) {
/* 498 */                 needed |= 0x2;
/*     */               } else {
/* 500 */                 needed |= 0x8;
/*     */               }
/*     */             }
/*     */             
/* 504 */             return KeyStroke.getKeyStroke(i.intValue(), needed);
/*     */           }
/* 506 */           return null;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 511 */       return null;
/*     */     }
/*     */     catch (NoSuchElementException ex) {}
/*     */   }
/*     */   
/*     */ 
/*     */   private static int getMenuShortCutKeyMask()
/*     */   {
/* 519 */     if (GraphicsEnvironment.isHeadless()) {
/* 520 */       return (getOperatingSystem() & 0x1000) != 0 ? 4 : 2;
/*     */     }
/*     */     
/*     */ 
/* 524 */     return Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
/*     */   }
/*     */   
/*     */   private static boolean usableKeyOnMac(int key, int mask)
/*     */   {
/* 529 */     if (key == 81) {
/* 530 */       return false;
/*     */     }
/*     */     
/* 533 */     boolean isMeta = ((mask & 0x4) != 0) || ((mask & 0x80) != 0);
/*     */     
/* 535 */     boolean isAlt = ((mask & 0x8) != 0) || ((mask & 0x200) != 0);
/*     */     
/* 537 */     boolean isOnlyMeta = (isMeta) && ((mask & 0xFEFB) == 0);
/*     */     
/*     */ 
/*     */ 
/* 541 */     if (isOnlyMeta)
/* 542 */       return (key != 72) && (key != 32) && (key != 9);
/* 543 */     return (key != 68) || (!isMeta) || (!isAlt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static KeyStroke[] stringToKeys(String s)
/*     */   {
/* 552 */     StringTokenizer st = new StringTokenizer(s.toUpperCase(Locale.ENGLISH), " ");
/* 553 */     ArrayList<KeyStroke> arr = new ArrayList();
/*     */     
/* 555 */     while (st.hasMoreElements()) {
/* 556 */       s = st.nextToken();
/*     */       
/* 558 */       KeyStroke k = stringToKey(s);
/*     */       
/* 560 */       if (k == null) {
/* 561 */         return null;
/*     */       }
/*     */       
/* 564 */       arr.add(k);
/*     */     }
/*     */     
/* 567 */     return (KeyStroke[])arr.toArray(new KeyStroke[arr.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean addModifiers(StringBuffer buf, int modif)
/*     */   {
/* 576 */     boolean b = false;
/*     */     
/* 578 */     if ((modif & 0x2) != 0) {
/* 579 */       buf.append("C");
/* 580 */       b = true;
/*     */     }
/*     */     
/* 583 */     if ((modif & 0x8) != 0) {
/* 584 */       buf.append("A");
/* 585 */       b = true;
/*     */     }
/*     */     
/* 588 */     if ((modif & 0x1) != 0) {
/* 589 */       buf.append("S");
/* 590 */       b = true;
/*     */     }
/*     */     
/* 593 */     if ((modif & 0x4) != 0) {
/* 594 */       buf.append("M");
/* 595 */       b = true;
/*     */     }
/*     */     
/* 598 */     if ((modif & 0x8000) != 0) {
/* 599 */       buf.append("D");
/* 600 */       b = true;
/*     */     }
/*     */     
/* 603 */     if ((modif & 0x10000) != 0) {
/* 604 */       buf.append("O");
/* 605 */       b = true;
/*     */     }
/*     */     
/* 608 */     return b;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int readModifiers(String s)
/*     */     throws NoSuchElementException
/*     */   {
/* 617 */     int m = 0;
/*     */     
/* 619 */     for (int i = 0; i < s.length(); i++) {
/* 620 */       switch (s.charAt(i)) {
/*     */       case 'C': 
/* 622 */         m |= 0x2;
/* 623 */         break;
/*     */       
/*     */       case 'A': 
/* 626 */         m |= 0x8;
/* 627 */         break;
/*     */       
/*     */       case 'M': 
/* 630 */         m |= 0x4;
/* 631 */         break;
/*     */       
/*     */       case 'S': 
/* 634 */         m |= 0x1;
/* 635 */         break;
/*     */       
/*     */       case 'D': 
/* 638 */         m |= 0x8000;
/* 639 */         break;
/*     */       
/*     */       case 'O': 
/* 642 */         m |= 0x10000;
/* 643 */         break;
/*     */       case 'B': case 'E': case 'F': case 'G': case 'H': case 'I': case 'J': 
/*     */       case 'K': case 'L': case 'N': case 'P': case 'Q': case 'R': default: 
/* 646 */         throw new NoSuchElementException(s);
/*     */       }
/*     */       
/*     */     }
/* 650 */     return m;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object toPrimitiveArray(Object[] array)
/*     */   {
/* 661 */     if ((array instanceof Integer[])) {
/* 662 */       int[] r = new int[array.length];
/*     */       
/* 664 */       int k = array.length;
/*     */       
/* 666 */       for (int i = 0; i < k; i++) {
/* 667 */         r[i] = (array[i] == null ? 0 : ((Integer)array[i]).intValue());
/*     */       }
/* 669 */       return r;
/*     */     }
/*     */     
/* 672 */     if ((array instanceof Boolean[])) {
/* 673 */       boolean[] r = new boolean[array.length];
/*     */       
/* 675 */       int k = array.length;
/*     */       
/* 677 */       for (int i = 0; i < k; i++) {
/* 678 */         r[i] = ((array[i] != null) && (((Boolean)array[i]).booleanValue()) ? 1 : false);
/*     */       }
/* 680 */       return r;
/*     */     }
/*     */     
/* 683 */     if ((array instanceof Byte[])) {
/* 684 */       byte[] r = new byte[array.length];
/*     */       
/* 686 */       int k = array.length;
/*     */       
/* 688 */       for (int i = 0; i < k; i++) {
/* 689 */         r[i] = (array[i] == null ? 0 : ((Byte)array[i]).byteValue());
/*     */       }
/* 691 */       return r;
/*     */     }
/*     */     
/* 694 */     if ((array instanceof Character[])) {
/* 695 */       char[] r = new char[array.length];
/*     */       
/* 697 */       int k = array.length;
/*     */       
/* 699 */       for (int i = 0; i < k; i++) {
/* 700 */         r[i] = (array[i] == null ? 0 : ((Character)array[i]).charValue());
/*     */       }
/* 702 */       return r;
/*     */     }
/*     */     
/* 705 */     if ((array instanceof Double[])) {
/* 706 */       double[] r = new double[array.length];
/*     */       
/* 708 */       int k = array.length;
/*     */       
/* 710 */       for (int i = 0; i < k; i++) {
/* 711 */         r[i] = (array[i] == null ? 0.0D : ((Double)array[i]).doubleValue());
/*     */       }
/* 713 */       return r;
/*     */     }
/*     */     
/* 716 */     if ((array instanceof Float[])) {
/* 717 */       float[] r = new float[array.length];
/*     */       
/* 719 */       int k = array.length;
/*     */       
/* 721 */       for (int i = 0; i < k; i++) {
/* 722 */         r[i] = (array[i] == null ? 0.0F : ((Float)array[i]).floatValue());
/*     */       }
/* 724 */       return r;
/*     */     }
/*     */     
/* 727 */     if ((array instanceof Long[])) {
/* 728 */       long[] r = new long[array.length];
/*     */       
/* 730 */       int k = array.length;
/*     */       
/* 732 */       for (int i = 0; i < k; i++) {
/* 733 */         r[i] = (array[i] == null ? 0L : ((Long)array[i]).longValue());
/*     */       }
/* 735 */       return r;
/*     */     }
/*     */     
/* 738 */     if ((array instanceof Short[])) {
/* 739 */       short[] r = new short[array.length];
/*     */       
/* 741 */       int k = array.length;
/*     */       
/* 743 */       for (int i = 0; i < k; i++) {
/* 744 */         r[i] = (array[i] == null ? 0 : ((Short)array[i]).shortValue());
/*     */       }
/* 746 */       return r;
/*     */     }
/*     */     
/* 749 */     throw new IllegalArgumentException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object[] toObjectArray(Object array)
/*     */   {
/* 760 */     if ((array instanceof Object[])) {
/* 761 */       return (Object[])array;
/*     */     }
/*     */     
/* 764 */     if ((array instanceof int[]))
/*     */     {
/* 766 */       int k = ((int[])array).length;
/* 767 */       Integer[] r = new Integer[k];
/*     */       
/* 769 */       for (int i = 0; i < k; i++) {
/* 770 */         r[i] = new Integer(((int[])(int[])array)[i]);
/*     */       }
/* 772 */       return r;
/*     */     }
/*     */     
/* 775 */     if ((array instanceof boolean[]))
/*     */     {
/* 777 */       int k = ((boolean[])array).length;
/* 778 */       Boolean[] r = new Boolean[k];
/*     */       
/* 780 */       for (int i = 0; i < k; i++) {
/* 781 */         r[i] = (((boolean[])(boolean[])array)[i] != 0 ? Boolean.TRUE : Boolean.FALSE);
/*     */       }
/* 783 */       return r;
/*     */     }
/*     */     
/* 786 */     if ((array instanceof byte[]))
/*     */     {
/* 788 */       int k = ((byte[])array).length;
/* 789 */       Byte[] r = new Byte[k];
/*     */       
/* 791 */       for (int i = 0; i < k; i++) {
/* 792 */         r[i] = new Byte(((byte[])(byte[])array)[i]);
/*     */       }
/* 794 */       return r;
/*     */     }
/*     */     
/* 797 */     if ((array instanceof char[]))
/*     */     {
/* 799 */       int k = ((char[])array).length;
/* 800 */       Character[] r = new Character[k];
/*     */       
/* 802 */       for (int i = 0; i < k; i++) {
/* 803 */         r[i] = new Character(((char[])(char[])array)[i]);
/*     */       }
/* 805 */       return r;
/*     */     }
/*     */     
/* 808 */     if ((array instanceof double[]))
/*     */     {
/* 810 */       int k = ((double[])array).length;
/* 811 */       Double[] r = new Double[k];
/*     */       
/* 813 */       for (int i = 0; i < k; i++) {
/* 814 */         r[i] = new Double(((double[])(double[])array)[i]);
/*     */       }
/* 816 */       return r;
/*     */     }
/*     */     
/* 819 */     if ((array instanceof float[]))
/*     */     {
/* 821 */       int k = ((float[])array).length;
/* 822 */       Float[] r = new Float[k];
/*     */       
/* 824 */       for (int i = 0; i < k; i++) {
/* 825 */         r[i] = new Float(((float[])(float[])array)[i]);
/*     */       }
/* 827 */       return r;
/*     */     }
/*     */     
/* 830 */     if ((array instanceof long[]))
/*     */     {
/* 832 */       int k = ((long[])array).length;
/* 833 */       Long[] r = new Long[k];
/*     */       
/* 835 */       for (int i = 0; i < k; i++) {
/* 836 */         r[i] = new Long(((long[])(long[])array)[i]);
/*     */       }
/* 838 */       return r;
/*     */     }
/*     */     
/* 841 */     if ((array instanceof short[]))
/*     */     {
/* 843 */       int k = ((short[])array).length;
/* 844 */       Short[] r = new Short[k];
/*     */       
/* 846 */       for (int i = 0; i < k; i++) {
/* 847 */         r[i] = new Short(((short[])(short[])array)[i]);
/*     */       }
/* 849 */       return r;
/*     */     }
/*     */     
/* 852 */     throw new IllegalArgumentException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] wrapStringToArray(String original, int width, BreakIterator breakIterator, boolean removeNewLines)
/*     */   {
/* 865 */     if (original.length() == 0) {
/* 866 */       return new String[] { original };
/*     */     }
/*     */     
/*     */     String[] workingSet;
/*     */     
/*     */     String[] workingSet;
/*     */     
/* 873 */     if (removeNewLines) {
/* 874 */       original = trimString(original);
/* 875 */       original = original.replace('\n', ' ');
/* 876 */       workingSet = new String[] { original };
/*     */     } else {
/* 878 */       StringTokenizer tokens = new StringTokenizer(original, "\n");
/* 879 */       int len = tokens.countTokens();
/* 880 */       workingSet = new String[len];
/*     */       
/* 882 */       for (int i = 0; i < len; i++) {
/* 883 */         workingSet[i] = tokens.nextToken();
/*     */       }
/*     */     }
/*     */     
/* 887 */     if (width < 1) {
/* 888 */       width = 1;
/*     */     }
/*     */     
/* 891 */     if (original.length() <= width) {
/* 892 */       return workingSet;
/*     */     }
/*     */     
/*     */ 
/* 896 */     boolean ok = true;
/*     */     
/* 898 */     for (int i = 0; i < workingSet.length; i++) {
/* 899 */       ok = (ok) && (workingSet[i].length() < width);
/*     */       
/* 901 */       if (!ok) {
/*     */         break label172;
/*     */       }
/*     */     }
/*     */     
/* 906 */     return workingSet;
/*     */     
/*     */     label172:
/* 909 */     ArrayList<String> lines = new ArrayList();
/*     */     
/* 911 */     int lineStart = 0;
/*     */     
/* 913 */     for (int i = 0; i < workingSet.length; i++) {
/* 914 */       if (workingSet[i].length() < width) {
/* 915 */         lines.add(workingSet[i]);
/*     */       } else {
/* 917 */         breakIterator.setText(workingSet[i]);
/*     */         
/* 919 */         int nextStart = breakIterator.next();
/* 920 */         int prevStart = 0;
/*     */         do
/*     */         {
/* 923 */           while ((nextStart - lineStart < width) && (nextStart != -1)) {
/* 924 */             prevStart = nextStart;
/* 925 */             nextStart = breakIterator.next();
/*     */           }
/*     */           
/* 928 */           if (nextStart == -1) {
/* 929 */             nextStart = prevStart = workingSet[i].length();
/*     */           }
/*     */           
/* 932 */           if (prevStart == 0) {
/* 933 */             prevStart = nextStart;
/*     */           }
/*     */           
/* 936 */           lines.add(workingSet[i].substring(lineStart, prevStart));
/*     */           
/* 938 */           lineStart = prevStart;
/* 939 */           prevStart = 0;
/* 940 */         } while (lineStart < workingSet[i].length());
/*     */         
/* 942 */         lineStart = 0;
/*     */       }
/*     */     }
/*     */     
/* 946 */     String[] s = new String[lines.size()];
/*     */     
/* 948 */     return (String[])lines.toArray(s);
/*     */   }
/*     */   
/*     */   private static String trimString(String s) {
/* 952 */     int idx = 0;
/*     */     
/* 954 */     int slen = s.length();
/*     */     
/* 956 */     if (slen == 0) {
/* 957 */       return s;
/*     */     }
/*     */     char c;
/*     */     do {
/* 961 */       c = s.charAt(idx++);
/* 962 */     } while (((c == '\n') || (c == '\r')) && (idx < slen));
/*     */     
/* 964 */     s = s.substring(--idx);
/* 965 */     idx = s.length() - 1;
/*     */     
/* 967 */     if (idx < 0) {
/* 968 */       return s;
/*     */     }
/*     */     do
/*     */     {
/* 972 */       c = s.charAt(idx--);
/* 973 */     } while (((c == '\n') || (c == '\r')) && (idx >= 0));
/*     */     
/* 975 */     return s.substring(0, idx + 2);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\util\Utilities.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */