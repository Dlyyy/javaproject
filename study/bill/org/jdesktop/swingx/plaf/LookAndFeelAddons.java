/*     */ package org.jdesktop.swingx.plaf;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import org.jdesktop.swingx.painter.Painter;
/*     */ import org.jdesktop.swingx.plaf.linux.LinuxLookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.macosx.MacOSXLookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.metal.MetalLookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.motif.MotifLookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.nimbus.NimbusLookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.windows.WindowsClassicLookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.windows.WindowsLookAndFeelAddons;
/*     */ import org.jdesktop.swingx.util.OS;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LookAndFeelAddons
/*     */ {
/*  73 */   private static List<ComponentAddon> contributedComponents = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  80 */   private static final Object APPCONTEXT_INITIALIZED = new Object();
/*     */   
/*  82 */   private static boolean trackingChanges = false;
/*     */   private static PropertyChangeListener changeListener;
/*     */   private static LookAndFeelAddons currentAddon;
/*     */   
/*     */   static {
/*  87 */     String addonClassname = getBestMatchAddonClassName();
/*     */     try {
/*  89 */       addonClassname = System.getProperty("swing.addon", addonClassname);
/*     */     }
/*     */     catch (SecurityException e) {}
/*     */     
/*     */     try
/*     */     {
/*  95 */       setAddon(addonClassname);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  99 */       throw new ExceptionInInitializerError(e);
/*     */     }
/*     */     
/* 102 */     setTrackingLookAndFeelChanges(true);
/*     */   }
/*     */   
/*     */ 
/*     */   public void initialize()
/*     */   {
/* 108 */     Iterator<ComponentAddon> iter = contributedComponents.iterator();
/* 109 */     while (iter.hasNext()) {
/* 110 */       ComponentAddon addon = (ComponentAddon)iter.next();
/* 111 */       addon.initialize(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void uninitialize() {
/* 116 */     Iterator<ComponentAddon> iter = contributedComponents.iterator();
/* 117 */     while (iter.hasNext()) {
/* 118 */       ComponentAddon addon = (ComponentAddon)iter.next();
/* 119 */       addon.uninitialize(this);
/*     */     }
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
/*     */   public void loadDefaults(Object[] keysAndValues)
/*     */   {
/* 138 */     for (int i = keysAndValues.length - 2; i >= 0; i -= 2) {
/* 139 */       if (UIManager.getLookAndFeelDefaults().get(keysAndValues[i]) == null) {
/* 140 */         UIManager.getLookAndFeelDefaults().put(keysAndValues[i], keysAndValues[(i + 1)]);
/*     */       }
/*     */     }
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
/*     */   public static void setAddon(String addonClassName)
/*     */     throws InstantiationException, IllegalAccessException, ClassNotFoundException
/*     */   {
/* 157 */     setAddon(Class.forName(addonClassName));
/*     */   }
/*     */   
/*     */   public static void setAddon(Class<?> addonClass) throws InstantiationException, IllegalAccessException
/*     */   {
/* 162 */     LookAndFeelAddons addon = (LookAndFeelAddons)addonClass.newInstance();
/* 163 */     setAddon(addon);
/*     */   }
/*     */   
/*     */   public static void setAddon(LookAndFeelAddons addon) {
/* 167 */     if (currentAddon != null) {
/* 168 */       currentAddon.uninitialize();
/*     */     }
/*     */     
/* 171 */     addon.initialize();
/* 172 */     currentAddon = addon;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 177 */     UIManager.put(APPCONTEXT_INITIALIZED, Boolean.TRUE);
/*     */     
/*     */ 
/*     */ 
/* 181 */     UIManager.getLookAndFeelDefaults().put(APPCONTEXT_INITIALIZED, Boolean.TRUE);
/*     */   }
/*     */   
/*     */   public static LookAndFeelAddons getAddon() {
/* 185 */     return currentAddon;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getBestMatchAddonClassName()
/*     */   {
/* 196 */     String lnf = UIManager.getLookAndFeel().getClass().getName();
/*     */     String addon;
/* 198 */     String addon; if (UIManager.getCrossPlatformLookAndFeelClassName().equals(lnf)) {
/* 199 */       addon = MetalLookAndFeelAddons.class.getName(); } else { String addon;
/* 200 */       if (UIManager.getSystemLookAndFeelClassName().equals(lnf)) {
/* 201 */         addon = getSystemAddonClassName(); } else { String addon;
/* 202 */         if (("com.sun.java.swing.plaf.windows.WindowsLookAndFeel".equals(lnf)) || ("com.jgoodies.looks.windows.WindowsLookAndFeel".equals(lnf))) {
/*     */           String addon;
/* 204 */           if (OS.isUsingWindowsVisualStyles()) {
/* 205 */             addon = WindowsLookAndFeelAddons.class.getName();
/*     */           } else
/* 207 */             addon = WindowsClassicLookAndFeelAddons.class.getName();
/*     */         } else { String addon;
/* 209 */           if ("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel".equals(lnf))
/*     */           {
/* 211 */             addon = WindowsClassicLookAndFeelAddons.class.getName(); } else { String addon;
/* 212 */             if (UIManager.getLookAndFeel().getID().equals("Motif")) {
/* 213 */               addon = MotifLookAndFeelAddons.class.getName(); } else { String addon;
/* 214 */               if (UIManager.getLookAndFeel().getID().equals("Nimbus")) {
/* 215 */                 addon = NimbusLookAndFeelAddons.class.getName();
/*     */               } else
/* 217 */                 addon = getSystemAddonClassName();
/*     */             } } } } }
/* 219 */     return addon;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getSystemAddonClassName()
/*     */   {
/* 229 */     String addon = WindowsClassicLookAndFeelAddons.class.getName();
/*     */     
/* 231 */     if (OS.isMacOSX()) {
/* 232 */       addon = MacOSXLookAndFeelAddons.class.getName();
/* 233 */     } else if (OS.isWindows())
/*     */     {
/* 235 */       if (OS.isUsingWindowsVisualStyles()) {
/* 236 */         addon = WindowsLookAndFeelAddons.class.getName();
/*     */       } else {
/* 238 */         addon = WindowsClassicLookAndFeelAddons.class.getName();
/*     */       }
/* 240 */     } else if (OS.isLinux()) {
/* 241 */       addon = LinuxLookAndFeelAddons.class.getName();
/*     */     }
/*     */     
/* 244 */     return addon;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void contribute(ComponentAddon component)
/*     */   {
/* 255 */     contributedComponents.add(component);
/*     */     
/* 257 */     if (currentAddon != null)
/*     */     {
/*     */ 
/* 260 */       component.initialize(currentAddon);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void uncontribute(ComponentAddon component)
/*     */   {
/* 270 */     contributedComponents.remove(component);
/*     */     
/* 272 */     if (currentAddon != null) {
/* 273 */       component.uninitialize(currentAddon);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ComponentUI getUI(JComponent component, Class<?> expectedUIClass)
/*     */   {
/* 286 */     maybeInitialize();
/*     */     
/*     */ 
/* 289 */     String uiClassname = (String)UIManager.get(component.getUIClassID());
/*     */     
/* 291 */     if (uiClassname == null) {
/* 292 */       Logger logger = Logger.getLogger("LookAndFeelAddons");
/* 293 */       logger.warning("Failed to retrieve UI for " + component.getClass().getName() + " with UIClassID " + component.getUIClassID());
/* 294 */       if (logger.isLoggable(Level.FINE)) {
/* 295 */         logger.fine("Existing UI defaults keys: " + new ArrayList(UIManager.getDefaults().keySet()));
/*     */       }
/*     */       
/*     */ 
/* 299 */       uiClassname = "org.jdesktop.swingx.plaf.basic.Basic" + expectedUIClass.getSimpleName();
/*     */     }
/*     */     try {
/* 302 */       Class<?> uiClass = Class.forName(uiClassname);
/* 303 */       UIManager.put(uiClassname, uiClass);
/*     */     }
/*     */     catch (ClassNotFoundException e) {}
/*     */     
/*     */ 
/* 308 */     ComponentUI ui = UIManager.getUI(component);
/*     */     
/* 310 */     if (expectedUIClass.isInstance(ui)) {
/* 311 */       return ui;
/*     */     }
/* 313 */     String realUI = ui.getClass().getName();
/*     */     Class<?> realUIClass;
/*     */     try {
/* 316 */       realUIClass = expectedUIClass.getClassLoader().loadClass(realUI);
/*     */     }
/*     */     catch (ClassNotFoundException e) {
/* 319 */       throw new RuntimeException("Failed to load class " + realUI, e);
/*     */     }
/* 321 */     Method createUIMethod = null;
/*     */     try {
/* 323 */       createUIMethod = realUIClass.getMethod("createUI", new Class[] { JComponent.class });
/*     */     } catch (NoSuchMethodException e1) {
/* 325 */       throw new RuntimeException("Class " + realUI + " has no method createUI(JComponent)");
/*     */     }
/*     */     try {
/* 328 */       return (ComponentUI)createUIMethod.invoke(null, new Object[] { component });
/*     */     } catch (Exception e2) {
/* 330 */       throw new RuntimeException("Failed to invoke " + realUI + "#createUI(JComponent)");
/*     */     }
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
/*     */   private static synchronized void maybeInitialize()
/*     */   {
/* 347 */     if (currentAddon != null)
/*     */     {
/*     */ 
/* 350 */       UIDefaults defaults = UIManager.getLookAndFeelDefaults();
/*     */       
/*     */ 
/*     */ 
/* 354 */       if (!defaults.getBoolean(APPCONTEXT_INITIALIZED)) {
/* 355 */         setAddon(currentAddon);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static class UpdateAddon implements PropertyChangeListener
/*     */   {
/*     */     public void propertyChange(PropertyChangeEvent evt)
/*     */     {
/*     */       try
/*     */       {
/* 366 */         LookAndFeelAddons.setAddon(LookAndFeelAddons.getBestMatchAddonClassName());
/*     */       }
/*     */       catch (Exception e) {
/* 369 */         throw new RuntimeException(e);
/*     */       }
/*     */     }
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
/*     */   public static synchronized void setTrackingLookAndFeelChanges(boolean tracking)
/*     */   {
/* 384 */     if (trackingChanges != tracking) {
/* 385 */       if (tracking) {
/* 386 */         if (changeListener == null) {
/* 387 */           changeListener = new UpdateAddon(null);
/*     */         }
/* 389 */         UIManager.addPropertyChangeListener(changeListener);
/*     */       } else {
/* 391 */         if (changeListener != null) {
/* 392 */           UIManager.removePropertyChangeListener(changeListener);
/*     */         }
/* 394 */         changeListener = null;
/*     */       }
/* 396 */       trackingChanges = tracking;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static synchronized boolean isTrackingLookAndFeelChanges()
/*     */   {
/* 406 */     return trackingChanges;
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
/*     */   public static void installBackgroundPainter(JComponent c, String painter)
/*     */   {
/* 425 */     Class<?> clazz = c.getClass();
/*     */     try
/*     */     {
/* 428 */       Method getter = clazz.getMethod("getBackgroundPainter", new Class[0]);
/* 429 */       Method setter = clazz.getMethod("setBackgroundPainter", new Class[] { Painter.class });
/*     */       
/* 431 */       Painter<?> p = (Painter)getter.invoke(c, new Object[0]);
/*     */       
/* 433 */       if ((p == null) || ((p instanceof UIResource))) {
/* 434 */         setter.invoke(c, new Object[] { UIManagerExt.getPainter(painter) });
/*     */       }
/*     */     } catch (RuntimeException e) {
/* 437 */       throw e;
/*     */     } catch (Exception e) {
/* 439 */       throw new IllegalArgumentException("cannot set painter on " + c.getClass());
/*     */     }
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
/*     */   public static void uninstallBackgroundPainter(JComponent c)
/*     */   {
/* 456 */     Class<?> clazz = c.getClass();
/*     */     try
/*     */     {
/* 459 */       Method getter = clazz.getMethod("getBackgroundPainter", new Class[0]);
/* 460 */       Method setter = clazz.getMethod("setBackgroundPainter", new Class[] { Painter.class });
/*     */       
/* 462 */       Painter<?> p = (Painter)getter.invoke(c, new Object[0]);
/*     */       
/* 464 */       if ((p == null) || ((p instanceof UIResource))) {
/* 465 */         setter.invoke(c, new Object[] { (Painter)null });
/*     */       }
/*     */     } catch (RuntimeException e) {
/* 468 */       throw e;
/*     */     } catch (Exception e) {
/* 470 */       throw new IllegalArgumentException("cannot set painter on " + c.getClass());
/*     */     }
/*     */   }
/*     */   
/*     */   public void unloadDefaults(Object[] keysAndValues) {}
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\LookAndFeelAddons.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */