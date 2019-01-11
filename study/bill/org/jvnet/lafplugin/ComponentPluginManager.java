/*    */ package org.jvnet.lafplugin;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ import javax.swing.UIDefaults;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ComponentPluginManager
/*    */   extends PluginManager
/*    */ {
/*    */   public ComponentPluginManager(String xmlName)
/*    */   {
/* 31 */     super(xmlName, "laf-plugin", "component-plugin-class");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void initializeAll()
/*    */   {
/* 42 */     Set availablePlugins = getAvailablePlugins();
/* 43 */     Iterator iterator = availablePlugins.iterator();
/* 44 */     while (iterator.hasNext()) {
/* 45 */       Object pluginObject = iterator.next();
/* 46 */       if ((pluginObject instanceof LafComponentPlugin)) {
/* 47 */         ((LafComponentPlugin)pluginObject).initialize();
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void uninitializeAll()
/*    */   {
/* 58 */     Set availablePlugins = getAvailablePlugins();
/* 59 */     Iterator iterator = availablePlugins.iterator();
/* 60 */     while (iterator.hasNext()) {
/* 61 */       Object pluginObject = iterator.next();
/* 62 */       if ((pluginObject instanceof LafComponentPlugin)) {
/* 63 */         ((LafComponentPlugin)pluginObject).uninitialize();
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processAllDefaultsEntries(UIDefaults table, Object themeInfo)
/*    */   {
/* 81 */     Set availablePlugins = getAvailablePlugins();
/* 82 */     Iterator iterator = availablePlugins.iterator();
/* 83 */     while (iterator.hasNext()) {
/* 84 */       Object pluginObject = iterator.next();
/* 85 */       if ((pluginObject instanceof LafComponentPlugin)) {
/* 86 */         Object[] defaults = ((LafComponentPlugin)pluginObject).getDefaults(themeInfo);
/*    */         
/* 88 */         if (defaults != null) {
/* 89 */           table.putDefaults(defaults);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jvnet\lafplugin\ComponentPluginManager.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */