/*     */ package org.jvnet.lafplugin;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PluginManager
/*     */ {
/*     */   private String mainTag;
/*     */   private String pluginTag;
/*     */   private String xmlName;
/*     */   private Set plugins;
/*     */   
/*     */   public PluginManager(String xmlName, String mainTag, String pluginTag)
/*     */   {
/*  44 */     this.xmlName = xmlName;
/*  45 */     this.mainTag = mainTag;
/*  46 */     this.pluginTag = pluginTag;
/*  47 */     this.plugins = null;
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
/*     */   protected String getPluginClass(URL pluginUrl)
/*     */   {
/*  86 */     InputStream is = null;
/*  87 */     InputStreamReader isr = null;
/*     */     try {
/*  89 */       XMLElement xml = new XMLElement();
/*  90 */       is = pluginUrl.openStream();
/*  91 */       isr = new InputStreamReader(is);
/*  92 */       xml.parseFromReader(isr);
/*  93 */       if (!this.mainTag.equals(xml.getName()))
/*  94 */         return null;
/*  95 */       children = xml.enumerateChildren();
/*  96 */       XMLElement child; while (children.hasMoreElements()) {
/*  97 */         child = (XMLElement)children.nextElement();
/*  98 */         if (this.pluginTag.equals(child.getName())) {
/*     */           String str2;
/* 100 */           if (child.countChildren() != 0)
/* 101 */             return null;
/* 102 */           return child.getContent();
/*     */         } }
/* 104 */       return null;
/*     */     } catch (Exception exc) { Enumeration children;
/* 106 */       return null;
/*     */     } finally {
/* 108 */       if (isr != null) {
/*     */         try {
/* 110 */           isr.close();
/*     */         }
/*     */         catch (Exception e) {}
/*     */       }
/* 114 */       if (is != null) {
/*     */         try {
/* 116 */           is.close();
/*     */         }
/*     */         catch (Exception e) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected Object getPlugin(URL pluginUrl) throws Exception {
/* 124 */     String pluginClassName = getPluginClass(pluginUrl);
/* 125 */     if (pluginClassName == null)
/* 126 */       return null;
/* 127 */     Class pluginClass = Class.forName(pluginClassName);
/* 128 */     if (pluginClass == null)
/* 129 */       return null;
/* 130 */     Object pluginInstance = pluginClass.newInstance();
/* 131 */     if (pluginInstance == null)
/* 132 */       return null;
/* 133 */     return pluginInstance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set getAvailablePlugins()
/*     */   {
/* 144 */     return getAvailablePlugins(false);
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
/*     */   public Set getAvailablePlugins(boolean toReload)
/*     */   {
/* 161 */     if ((toReload) && (this.plugins != null)) {
/* 162 */       return this.plugins;
/*     */     }
/* 164 */     this.plugins = new HashSet();
/*     */     
/* 166 */     ClassLoader cl = PluginManager.class.getClassLoader();
/*     */     try {
/* 168 */       Enumeration urls = cl.getResources(this.xmlName);
/* 169 */       while (urls.hasMoreElements()) {
/* 170 */         URL pluginUrl = (URL)urls.nextElement();
/* 171 */         Object pluginInstance = getPlugin(pluginUrl);
/* 172 */         if (pluginInstance != null) {
/* 173 */           this.plugins.add(pluginInstance);
/*     */         }
/*     */       }
/*     */     } catch (Exception exc) {
/* 177 */       return null;
/*     */     }
/*     */     
/* 180 */     return this.plugins;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jvnet\lafplugin\PluginManager.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */