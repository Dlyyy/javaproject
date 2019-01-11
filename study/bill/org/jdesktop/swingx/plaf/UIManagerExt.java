/*     */ package org.jdesktop.swingx.plaf;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Shape;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Vector;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.BorderUIResource;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.DimensionUIResource;
/*     */ import javax.swing.plaf.FontUIResource;
/*     */ import javax.swing.plaf.IconUIResource;
/*     */ import javax.swing.plaf.InsetsUIResource;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import org.jdesktop.swingx.painter.Painter;
/*     */ import org.jdesktop.swingx.util.Contract;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UIManagerExt
/*     */ {
/*     */   private static class UIDefaultsExt
/*     */   {
/*     */     private Vector<String> resourceBundles;
/*     */     private Map<Locale, Map<String, String>> resourceCache;
/*     */     
/*     */     UIDefaultsExt()
/*     */     {
/* 126 */       this.resourceCache = new HashMap();
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     private Object getFromResourceBundle(Object key, Locale l)
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 6	org/jdesktop/swingx/plaf/UIManagerExt$UIDefaultsExt:resourceBundles	Ljava/util/Vector;
/*     */       //   4: ifnull +20 -> 24
/*     */       //   7: aload_0
/*     */       //   8: getfield 6	org/jdesktop/swingx/plaf/UIManagerExt$UIDefaultsExt:resourceBundles	Ljava/util/Vector;
/*     */       //   11: invokevirtual 7	java/util/Vector:isEmpty	()Z
/*     */       //   14: ifne +10 -> 24
/*     */       //   17: aload_1
/*     */       //   18: instanceof 8
/*     */       //   21: ifne +5 -> 26
/*     */       //   24: aconst_null
/*     */       //   25: areturn
/*     */       //   26: aload_2
/*     */       //   27: ifnonnull +7 -> 34
/*     */       //   30: invokestatic 9	java/util/Locale:getDefault	()Ljava/util/Locale;
/*     */       //   33: astore_2
/*     */       //   34: aload_0
/*     */       //   35: dup
/*     */       //   36: astore_3
/*     */       //   37: monitorenter
/*     */       //   38: aload_0
/*     */       //   39: aload_2
/*     */       //   40: invokespecial 10	org/jdesktop/swingx/plaf/UIManagerExt$UIDefaultsExt:getResourceCache	(Ljava/util/Locale;)Ljava/util/Map;
/*     */       //   43: aload_1
/*     */       //   44: checkcast 8	java/lang/String
/*     */       //   47: invokeinterface 11 2 0
/*     */       //   52: aload_3
/*     */       //   53: monitorexit
/*     */       //   54: areturn
/*     */       //   55: astore 4
/*     */       //   57: aload_3
/*     */       //   58: monitorexit
/*     */       //   59: aload 4
/*     */       //   61: athrow
/*     */       // Line number table:
/*     */       //   Java source line #132	-> byte code offset #0
/*     */       //   Java source line #135	-> byte code offset #24
/*     */       //   Java source line #139	-> byte code offset #26
/*     */       //   Java source line #140	-> byte code offset #30
/*     */       //   Java source line #143	-> byte code offset #34
/*     */       //   Java source line #144	-> byte code offset #38
/*     */       //   Java source line #145	-> byte code offset #55
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	62	0	this	UIDefaultsExt
/*     */       //   0	62	1	key	Object
/*     */       //   0	62	2	l	Locale
/*     */       //   36	22	3	Ljava/lang/Object;	Object
/*     */       //   55	5	4	localObject1	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   38	54	55	finally
/*     */       //   55	59	55	finally
/*     */     }
/*     */     
/*     */     private Map<String, String> getResourceCache(Locale l)
/*     */     {
/* 152 */       Map<String, String> values = (Map)this.resourceCache.get(l);
/*     */       
/* 154 */       if (values == null) {
/* 155 */         values = new HashMap();
/* 156 */         for (int i = this.resourceBundles.size() - 1; i >= 0; i--) {
/* 157 */           String bundleName = (String)this.resourceBundles.get(i);
/*     */           try
/*     */           {
/* 160 */             ResourceBundle b = ResourceBundle.getBundle(bundleName, l, UIManagerExt.class.getClassLoader());
/*     */             
/* 162 */             Enumeration<String> keys = b.getKeys();
/*     */             
/* 164 */             while (keys.hasMoreElements()) {
/* 165 */               String key = (String)keys.nextElement();
/*     */               
/* 167 */               if (values.get(key) == null) {
/* 168 */                 Object value = b.getObject(key);
/*     */                 
/* 170 */                 values.put(key, (String)value);
/*     */               }
/*     */             }
/*     */           }
/*     */           catch (MissingResourceException mre) {}
/*     */         }
/*     */         
/* 177 */         this.resourceCache.put(l, values);
/*     */       }
/* 179 */       return values;
/*     */     }
/*     */     
/*     */     public synchronized void addResourceBundle(String bundleName) {
/* 183 */       if (bundleName == null) {
/* 184 */         return;
/*     */       }
/* 186 */       if (this.resourceBundles == null) {
/* 187 */         this.resourceBundles = new Vector(5);
/*     */       }
/* 189 */       if (!this.resourceBundles.contains(bundleName)) {
/* 190 */         this.resourceBundles.add(bundleName);
/* 191 */         this.resourceCache.clear();
/*     */       }
/*     */     }
/*     */     
/*     */     public synchronized void removeResourceBundle(String bundleName) {
/* 196 */       if (this.resourceBundles != null) {
/* 197 */         this.resourceBundles.remove(bundleName);
/*     */       }
/* 199 */       this.resourceCache.clear();
/*     */     }
/*     */   }
/*     */   
/* 203 */   private static UIDefaultsExt uiDefaultsExt = new UIDefaultsExt();
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
/*     */   public static void addResourceBundle(String bundleName)
/*     */   {
/* 221 */     uiDefaultsExt.addResourceBundle(bundleName);
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
/*     */   public static void removeResourceBundle(String bundleName)
/*     */   {
/* 234 */     uiDefaultsExt.removeResourceBundle(bundleName);
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
/*     */   public static String getString(Object key)
/*     */   {
/* 248 */     return getString(key, null);
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
/*     */   public static String getString(Object key, Locale l)
/*     */   {
/* 266 */     Object value = UIManager.get(key, l);
/*     */     
/* 268 */     if ((value instanceof String)) {
/* 269 */       return (String)value;
/*     */     }
/*     */     
/*     */ 
/* 273 */     if (value == null) {
/* 274 */       value = uiDefaultsExt.getFromResourceBundle(key, l);
/*     */       
/* 276 */       if ((value instanceof String)) {
/* 277 */         return (String)value;
/*     */       }
/*     */     }
/*     */     
/* 281 */     return null;
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
/*     */   public static int getInt(Object key)
/*     */   {
/* 295 */     return getInt(key, null);
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
/*     */   public static int getInt(Object key, Locale l)
/*     */   {
/* 313 */     Object value = UIManager.get(key, l);
/*     */     
/* 315 */     if ((value instanceof Integer)) {
/* 316 */       return ((Integer)value).intValue();
/*     */     }
/*     */     
/* 319 */     if (value == null) {
/* 320 */       value = uiDefaultsExt.getFromResourceBundle(key, l);
/*     */       
/* 322 */       if ((value instanceof Integer)) {
/* 323 */         return ((Integer)value).intValue();
/*     */       }
/*     */       
/* 326 */       if ((value instanceof String)) {
/*     */         try {
/* 328 */           return Integer.decode((String)value).intValue();
/*     */         }
/*     */         catch (NumberFormatException e) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 336 */     return 0;
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
/*     */   public static boolean getBoolean(Object key)
/*     */   {
/* 350 */     return getBoolean(key, null);
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
/*     */   public static boolean getBoolean(Object key, Locale l)
/*     */   {
/* 368 */     Object value = UIManager.get(key, l);
/*     */     
/* 370 */     if ((value instanceof Boolean)) {
/* 371 */       return ((Boolean)value).booleanValue();
/*     */     }
/*     */     
/*     */ 
/* 375 */     if (value == null) {
/* 376 */       value = uiDefaultsExt.getFromResourceBundle(key, l);
/*     */       
/* 378 */       if ((value instanceof Boolean)) {
/* 379 */         return ((Boolean)value).booleanValue();
/*     */       }
/*     */       
/* 382 */       if ((value instanceof String)) {
/* 383 */         return Boolean.valueOf((String)value).booleanValue();
/*     */       }
/*     */     }
/*     */     
/* 387 */     return false;
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
/*     */   public static Color getColor(Object key)
/*     */   {
/* 401 */     return getColor(key, null);
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
/*     */   public static Color getColor(Object key, Locale l)
/*     */   {
/* 419 */     Object value = UIManager.get(key, l);
/*     */     
/* 421 */     if ((value instanceof Color)) {
/* 422 */       return (Color)value;
/*     */     }
/*     */     
/*     */ 
/* 426 */     if (value == null) {
/* 427 */       value = uiDefaultsExt.getFromResourceBundle(key, l);
/*     */       
/* 429 */       if ((value instanceof Color)) {
/* 430 */         return (Color)value;
/*     */       }
/*     */       
/* 433 */       if ((value instanceof String)) {
/*     */         try {
/* 435 */           return Color.decode((String)value);
/*     */         }
/*     */         catch (NumberFormatException e) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 442 */     return null;
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
/*     */   public static Shape getShape(Object key)
/*     */   {
/* 506 */     Object value = UIManager.getDefaults().get(key);
/* 507 */     return (value instanceof Shape) ? (Shape)value : null;
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
/*     */   public static Shape getShape(Object key, Locale l)
/*     */   {
/* 526 */     Object value = UIManager.getDefaults().get(key, l);
/* 527 */     return (value instanceof Shape) ? (Shape)value : null;
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
/*     */   public static Painter<?> getPainter(Object key)
/*     */   {
/* 541 */     Object value = UIManager.getDefaults().get(key);
/* 542 */     return (value instanceof Painter) ? (Painter)value : null;
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
/*     */   public static Painter<?> getPainter(Object key, Locale l)
/*     */   {
/* 561 */     Object value = UIManager.getDefaults().get(key, l);
/* 562 */     return (value instanceof Painter) ? (Painter)value : null;
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
/*     */   public static Border getSafeBorder(Object key, Border defaultBorder)
/*     */   {
/* 579 */     Contract.asNotNull(defaultBorder, "defaultBorder cannot be null");
/*     */     
/* 581 */     Border safeBorder = UIManager.getBorder(key);
/*     */     
/* 583 */     if (safeBorder == null) {
/* 584 */       safeBorder = defaultBorder;
/*     */     }
/*     */     
/* 587 */     if (!(safeBorder instanceof UIResource)) {
/* 588 */       safeBorder = new BorderUIResource(safeBorder);
/*     */     }
/*     */     
/* 591 */     return safeBorder;
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
/*     */   public static Color getSafeColor(Object key, Color defaultColor)
/*     */   {
/* 608 */     Contract.asNotNull(defaultColor, "defaultColor cannot be null");
/*     */     
/* 610 */     Color safeColor = UIManager.getColor(key);
/*     */     
/* 612 */     if (safeColor == null) {
/* 613 */       safeColor = defaultColor;
/*     */     }
/*     */     
/* 616 */     if (!(safeColor instanceof UIResource)) {
/* 617 */       safeColor = new ColorUIResource(safeColor);
/*     */     }
/*     */     
/* 620 */     return safeColor;
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
/*     */   public static Dimension getSafeDimension(Object key, Dimension defaultDimension)
/*     */   {
/* 637 */     Contract.asNotNull(defaultDimension, "defaultDimension cannot be null");
/*     */     
/* 639 */     Dimension safeDimension = UIManager.getDimension(key);
/*     */     
/* 641 */     if (safeDimension == null) {
/* 642 */       safeDimension = defaultDimension;
/*     */     }
/*     */     
/* 645 */     if (!(safeDimension instanceof UIResource)) {
/* 646 */       safeDimension = new DimensionUIResource(safeDimension.width, safeDimension.height);
/*     */     }
/*     */     
/* 649 */     return safeDimension;
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
/*     */   public static Font getSafeFont(Object key, Font defaultFont)
/*     */   {
/* 666 */     Contract.asNotNull(defaultFont, "defaultFont cannot be null");
/*     */     
/* 668 */     Font safeFont = UIManager.getFont(key);
/*     */     
/* 670 */     if (safeFont == null) {
/* 671 */       safeFont = defaultFont;
/*     */     }
/*     */     
/* 674 */     if (!(safeFont instanceof UIResource)) {
/* 675 */       safeFont = new FontUIResource(safeFont);
/*     */     }
/*     */     
/* 678 */     return safeFont;
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
/*     */   public static Icon getSafeIcon(Object key, Icon defaultIcon)
/*     */   {
/* 695 */     Contract.asNotNull(defaultIcon, "defaultIcon cannot be null");
/*     */     
/* 697 */     Icon safeIcon = UIManager.getIcon(key);
/*     */     
/* 699 */     if (safeIcon == null) {
/* 700 */       safeIcon = defaultIcon;
/*     */     }
/*     */     
/* 703 */     if (!(safeIcon instanceof UIResource)) {
/* 704 */       safeIcon = new IconUIResource(safeIcon);
/*     */     }
/*     */     
/* 707 */     return safeIcon;
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
/*     */   public static Insets getSafeInsets(Object key, Insets defaultInsets)
/*     */   {
/* 724 */     Contract.asNotNull(defaultInsets, "defaultInsets cannot be null");
/*     */     
/* 726 */     Insets safeInsets = UIManager.getInsets(key);
/*     */     
/* 728 */     if (safeInsets == null) {
/* 729 */       safeInsets = defaultInsets;
/*     */     }
/*     */     
/* 732 */     if (!(safeInsets instanceof UIResource)) {
/* 733 */       safeInsets = new InsetsUIResource(safeInsets.top, safeInsets.left, safeInsets.bottom, safeInsets.right);
/*     */     }
/*     */     
/*     */ 
/* 737 */     return safeInsets;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\UIManagerExt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */