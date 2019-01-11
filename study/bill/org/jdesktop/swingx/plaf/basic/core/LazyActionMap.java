/*     */ package org.jdesktop.swingx.plaf.basic.core;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ActionMapUIResource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LazyActionMap
/*     */   extends ActionMapUIResource
/*     */ {
/*     */   private transient Object _loader;
/*     */   
/*     */   public static void installLazyActionMap(JComponent c, Class loaderClass, String defaultsKey)
/*     */   {
/*  59 */     ActionMap map = (ActionMap)UIManager.get(defaultsKey);
/*  60 */     if (map == null) {
/*  61 */       map = new LazyActionMap(loaderClass);
/*  62 */       UIManager.getLookAndFeelDefaults().put(defaultsKey, map);
/*     */     }
/*  64 */     SwingUtilities.replaceUIActionMap(c, map);
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
/*     */   static ActionMap getActionMap(Class loaderClass, String defaultsKey)
/*     */   {
/*  82 */     ActionMap map = (ActionMap)UIManager.get(defaultsKey);
/*  83 */     if (map == null) {
/*  84 */       map = new LazyActionMap(loaderClass);
/*  85 */       UIManager.getLookAndFeelDefaults().put(defaultsKey, map);
/*     */     }
/*  87 */     return map;
/*     */   }
/*     */   
/*     */   private LazyActionMap(Class loader)
/*     */   {
/*  92 */     this._loader = loader;
/*     */   }
/*     */   
/*     */   public void put(Action action) {
/*  96 */     put(action.getValue("Name"), action);
/*     */   }
/*     */   
/*     */   public void put(Object key, Action action) {
/* 100 */     loadIfNecessary();
/* 101 */     super.put(key, action);
/*     */   }
/*     */   
/*     */   public Action get(Object key) {
/* 105 */     loadIfNecessary();
/* 106 */     return super.get(key);
/*     */   }
/*     */   
/*     */   public void remove(Object key) {
/* 110 */     loadIfNecessary();
/* 111 */     super.remove(key);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 115 */     loadIfNecessary();
/* 116 */     super.clear();
/*     */   }
/*     */   
/*     */   public Object[] keys() {
/* 120 */     loadIfNecessary();
/* 121 */     return super.keys();
/*     */   }
/*     */   
/*     */   public int size() {
/* 125 */     loadIfNecessary();
/* 126 */     return super.size();
/*     */   }
/*     */   
/*     */   public Object[] allKeys() {
/* 130 */     loadIfNecessary();
/* 131 */     return super.allKeys();
/*     */   }
/*     */   
/*     */   public void setParent(ActionMap map) {
/* 135 */     loadIfNecessary();
/* 136 */     super.setParent(map);
/*     */   }
/*     */   
/*     */   private void loadIfNecessary() {
/* 140 */     if (this._loader != null) {
/* 141 */       Object loader = this._loader;
/*     */       
/* 143 */       this._loader = null;
/* 144 */       Class klass = (Class)loader;
/*     */       try {
/* 146 */         Method method = klass.getDeclaredMethod("loadActionMap", new Class[] { LazyActionMap.class });
/*     */         
/* 148 */         method.invoke(klass, new Object[] { this });
/*     */       } catch (NoSuchMethodException nsme) {
/* 150 */         if (!$assertionsDisabled) throw new AssertionError("LazyActionMap unable to load actions " + klass);
/*     */       }
/*     */       catch (IllegalAccessException iae) {
/* 153 */         if (!$assertionsDisabled) throw new AssertionError("LazyActionMap unable to load actions " + iae);
/*     */       }
/*     */       catch (InvocationTargetException ite) {
/* 156 */         if (!$assertionsDisabled) throw new AssertionError("LazyActionMap unable to load actions " + ite);
/*     */       }
/*     */       catch (IllegalArgumentException iae) {
/* 159 */         if (!$assertionsDisabled) throw new AssertionError("LazyActionMap unable to load actions " + iae);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\core\LazyActionMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */