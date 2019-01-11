/*      */ package org.jvnet.lafplugin;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.CharArrayReader;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.Reader;
/*      */ import java.io.StringReader;
/*      */ import java.io.Writer;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class XMLElement
/*      */ {
/*      */   static final long serialVersionUID = 6685035139346394777L;
/*      */   public static final int NANOXML_MAJOR_VERSION = 2;
/*      */   public static final int NANOXML_MINOR_VERSION = 2;
/*      */   private Hashtable attributes;
/*      */   private Vector children;
/*      */   private String name;
/*      */   private String contents;
/*      */   private Hashtable entities;
/*      */   private int lineNr;
/*      */   private boolean ignoreCase;
/*      */   private boolean ignoreWhitespace;
/*      */   private char charReadTooMuch;
/*      */   private Reader reader;
/*      */   private int parserLineNr;
/*      */   
/*      */   public XMLElement()
/*      */   {
/*  280 */     this(new Hashtable(), false, true, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public XMLElement(Hashtable entities)
/*      */   {
/*  314 */     this(entities, false, true, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public XMLElement(boolean skipLeadingWhitespace)
/*      */   {
/*  346 */     this(new Hashtable(), skipLeadingWhitespace, true, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public XMLElement(Hashtable entities, boolean skipLeadingWhitespace)
/*      */   {
/*  384 */     this(entities, skipLeadingWhitespace, true, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public XMLElement(Hashtable entities, boolean skipLeadingWhitespace, boolean ignoreCase)
/*      */   {
/*  425 */     this(entities, skipLeadingWhitespace, true, ignoreCase);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected XMLElement(Hashtable entities, boolean skipLeadingWhitespace, boolean fillBasicConversionTable, boolean ignoreCase)
/*      */   {
/*  473 */     this.ignoreWhitespace = skipLeadingWhitespace;
/*  474 */     this.ignoreCase = ignoreCase;
/*  475 */     this.name = null;
/*  476 */     this.contents = "";
/*  477 */     this.attributes = new Hashtable();
/*  478 */     this.children = new Vector();
/*  479 */     this.entities = entities;
/*  480 */     this.lineNr = 0;
/*  481 */     Enumeration enum = this.entities.keys();
/*  482 */     while (enum.hasMoreElements()) {
/*  483 */       Object key = enum.nextElement();
/*  484 */       Object value = this.entities.get(key);
/*  485 */       if ((value instanceof String)) {
/*  486 */         value = ((String)value).toCharArray();
/*  487 */         this.entities.put(key, value);
/*      */       }
/*      */     }
/*  490 */     if (fillBasicConversionTable) {
/*  491 */       this.entities.put("amp", new char[] { '&' });
/*  492 */       this.entities.put("quot", new char[] { '"' });
/*  493 */       this.entities.put("apos", new char[] { '\'' });
/*  494 */       this.entities.put("lt", new char[] { '<' });
/*  495 */       this.entities.put("gt", new char[] { '>' });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addChild(XMLElement child)
/*      */   {
/*  526 */     this.children.addElement(child);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAttribute(String name, Object value)
/*      */   {
/*  576 */     if (this.ignoreCase) {
/*  577 */       name = name.toUpperCase();
/*      */     }
/*  579 */     this.attributes.put(name, value.toString());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public void addProperty(String name, Object value)
/*      */   {
/*  597 */     setAttribute(name, value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setIntAttribute(String name, int value)
/*      */   {
/*  639 */     if (this.ignoreCase) {
/*  640 */       name = name.toUpperCase();
/*      */     }
/*  642 */     this.attributes.put(name, Integer.toString(value));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public void addProperty(String key, int value)
/*      */   {
/*  660 */     setIntAttribute(key, value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDoubleAttribute(String name, double value)
/*      */   {
/*  702 */     if (this.ignoreCase) {
/*  703 */       name = name.toUpperCase();
/*      */     }
/*  705 */     this.attributes.put(name, Double.toString(value));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public void addProperty(String name, double value)
/*      */   {
/*  723 */     setDoubleAttribute(name, value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int countChildren()
/*      */   {
/*  743 */     return this.children.size();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Enumeration enumerateAttributeNames()
/*      */   {
/*  802 */     return this.attributes.keys();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public Enumeration enumeratePropertyNames()
/*      */   {
/*  814 */     return enumerateAttributeNames();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Enumeration enumerateChildren()
/*      */   {
/*  834 */     return this.children.elements();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Vector getChildren()
/*      */   {
/*      */     try
/*      */     {
/*  856 */       return (Vector)this.children.clone();
/*      */     }
/*      */     catch (Exception e) {}
/*      */     
/*  860 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public String getContents()
/*      */   {
/*  873 */     return getContent();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getContent()
/*      */   {
/*  886 */     return this.contents;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getLineNr()
/*      */   {
/*  900 */     return this.lineNr;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getAttribute(String name)
/*      */   {
/*  929 */     return getAttribute(name, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getAttribute(String name, Object defaultValue)
/*      */   {
/*  960 */     if (this.ignoreCase) {
/*  961 */       name = name.toUpperCase();
/*      */     }
/*  963 */     Object value = this.attributes.get(name);
/*  964 */     if (value == null) {
/*  965 */       value = defaultValue;
/*      */     }
/*  967 */     return value;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getAttribute(String name, Hashtable valueSet, String defaultKey, boolean allowLiterals)
/*      */   {
/* 1013 */     if (this.ignoreCase) {
/* 1014 */       name = name.toUpperCase();
/*      */     }
/* 1016 */     Object key = this.attributes.get(name);
/*      */     
/* 1018 */     if (key == null) {
/* 1019 */       key = defaultKey;
/*      */     }
/* 1021 */     Object result = valueSet.get(key);
/* 1022 */     if (result == null) {
/* 1023 */       if (allowLiterals) {
/* 1024 */         result = key;
/*      */       } else {
/* 1026 */         throw invalidValue(name, (String)key);
/*      */       }
/*      */     }
/* 1029 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getStringAttribute(String name)
/*      */   {
/* 1059 */     return getStringAttribute(name, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getStringAttribute(String name, String defaultValue)
/*      */   {
/* 1090 */     return (String)getAttribute(name, defaultValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getStringAttribute(String name, Hashtable valueSet, String defaultKey, boolean allowLiterals)
/*      */   {
/* 1138 */     return (String)getAttribute(name, valueSet, defaultKey, allowLiterals);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getIntAttribute(String name)
/*      */   {
/* 1166 */     return getIntAttribute(name, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getIntAttribute(String name, int defaultValue)
/*      */   {
/* 1195 */     if (this.ignoreCase) {
/* 1196 */       name = name.toUpperCase();
/*      */     }
/* 1198 */     String value = (String)this.attributes.get(name);
/* 1199 */     if (value == null) {
/* 1200 */       return defaultValue;
/*      */     }
/*      */     try {
/* 1203 */       return Integer.parseInt(value);
/*      */     } catch (NumberFormatException e) {
/* 1205 */       throw invalidValue(name, value);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getIntAttribute(String name, Hashtable valueSet, String defaultKey, boolean allowLiteralNumbers)
/*      */   {
/* 1253 */     if (this.ignoreCase) {
/* 1254 */       name = name.toUpperCase();
/*      */     }
/* 1256 */     Object key = this.attributes.get(name);
/*      */     
/* 1258 */     if (key == null) {
/* 1259 */       key = defaultKey;
/*      */     }
/*      */     try {
/* 1262 */       result = (Integer)valueSet.get(key);
/*      */     } catch (ClassCastException e) { Integer result;
/* 1264 */       throw invalidValueSet(name); }
/*      */     Integer result;
/* 1266 */     if (result == null) {
/* 1267 */       if (!allowLiteralNumbers) {
/* 1268 */         throw invalidValue(name, (String)key);
/*      */       }
/*      */       try {
/* 1271 */         result = Integer.valueOf((String)key);
/*      */       } catch (NumberFormatException e) {
/* 1273 */         throw invalidValue(name, (String)key);
/*      */       }
/*      */     }
/* 1276 */     return result.intValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getDoubleAttribute(String name)
/*      */   {
/* 1303 */     return getDoubleAttribute(name, 0.0D);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getDoubleAttribute(String name, double defaultValue)
/*      */   {
/* 1332 */     if (this.ignoreCase) {
/* 1333 */       name = name.toUpperCase();
/*      */     }
/* 1335 */     String value = (String)this.attributes.get(name);
/* 1336 */     if (value == null) {
/* 1337 */       return defaultValue;
/*      */     }
/*      */     try {
/* 1340 */       return Double.valueOf(value).doubleValue();
/*      */     } catch (NumberFormatException e) {
/* 1342 */       throw invalidValue(name, value);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getDoubleAttribute(String name, Hashtable valueSet, String defaultKey, boolean allowLiteralNumbers)
/*      */   {
/* 1391 */     if (this.ignoreCase) {
/* 1392 */       name = name.toUpperCase();
/*      */     }
/* 1394 */     Object key = this.attributes.get(name);
/*      */     
/* 1396 */     if (key == null) {
/* 1397 */       key = defaultKey;
/*      */     }
/*      */     try {
/* 1400 */       result = (Double)valueSet.get(key);
/*      */     } catch (ClassCastException e) { Double result;
/* 1402 */       throw invalidValueSet(name); }
/*      */     Double result;
/* 1404 */     if (result == null) {
/* 1405 */       if (!allowLiteralNumbers) {
/* 1406 */         throw invalidValue(name, (String)key);
/*      */       }
/*      */       try {
/* 1409 */         result = Double.valueOf((String)key);
/*      */       } catch (NumberFormatException e) {
/* 1411 */         throw invalidValue(name, (String)key);
/*      */       }
/*      */     }
/* 1414 */     return result.doubleValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getBooleanAttribute(String name, String trueValue, String falseValue, boolean defaultValue)
/*      */   {
/* 1451 */     if (this.ignoreCase) {
/* 1452 */       name = name.toUpperCase();
/*      */     }
/* 1454 */     Object value = this.attributes.get(name);
/* 1455 */     if (value == null)
/* 1456 */       return defaultValue;
/* 1457 */     if (value.equals(trueValue))
/* 1458 */       return true;
/* 1459 */     if (value.equals(falseValue)) {
/* 1460 */       return false;
/*      */     }
/* 1462 */     throw invalidValue(name, (String)value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public int getIntProperty(String name, Hashtable valueSet, String defaultKey)
/*      */   {
/* 1478 */     return getIntAttribute(name, valueSet, defaultKey, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public String getProperty(String name)
/*      */   {
/* 1490 */     return getStringAttribute(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public String getProperty(String name, String defaultValue)
/*      */   {
/* 1503 */     return getStringAttribute(name, defaultValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public int getProperty(String name, int defaultValue)
/*      */   {
/* 1516 */     return getIntAttribute(name, defaultValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public double getProperty(String name, double defaultValue)
/*      */   {
/* 1529 */     return getDoubleAttribute(name, defaultValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public boolean getProperty(String key, String trueValue, String falseValue, boolean defaultValue)
/*      */   {
/* 1545 */     return getBooleanAttribute(key, trueValue, falseValue, defaultValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public Object getProperty(String name, Hashtable valueSet, String defaultKey)
/*      */   {
/* 1561 */     return getAttribute(name, valueSet, defaultKey, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public String getStringProperty(String name, Hashtable valueSet, String defaultKey)
/*      */   {
/* 1576 */     return getStringAttribute(name, valueSet, defaultKey, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public int getSpecialIntProperty(String name, Hashtable valueSet, String defaultKey)
/*      */   {
/* 1591 */     return getIntAttribute(name, valueSet, defaultKey, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public double getSpecialDoubleProperty(String name, Hashtable valueSet, String defaultKey)
/*      */   {
/* 1606 */     return getDoubleAttribute(name, valueSet, defaultKey, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getName()
/*      */   {
/* 1617 */     return this.name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public String getTagName()
/*      */   {
/* 1628 */     return getName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void parseFromReader(Reader reader)
/*      */     throws IOException, XMLParseException
/*      */   {
/* 1658 */     parseFromReader(reader, 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void parseFromReader(Reader reader, int startingLineNr)
/*      */     throws IOException, XMLParseException
/*      */   {
/* 1691 */     this.name = null;
/* 1692 */     this.contents = "";
/* 1693 */     this.attributes = new Hashtable();
/* 1694 */     this.children = new Vector();
/* 1695 */     this.charReadTooMuch = '\000';
/* 1696 */     this.reader = reader;
/* 1697 */     this.parserLineNr = startingLineNr;
/*      */     for (;;)
/*      */     {
/* 1700 */       char ch = scanWhitespace();
/*      */       
/* 1702 */       if (ch != '<') {
/* 1703 */         throw expectedInput("<");
/*      */       }
/*      */       
/* 1706 */       ch = readChar();
/*      */       
/* 1708 */       if ((ch == '!') || (ch == '?')) {
/* 1709 */         skipSpecialTag(0);
/*      */       } else {
/* 1711 */         unreadChar(ch);
/* 1712 */         scanElement(this);
/* 1713 */         return;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void parseString(String string)
/*      */     throws XMLParseException
/*      */   {
/*      */     try
/*      */     {
/* 1742 */       parseFromReader(new StringReader(string), 1);
/*      */     }
/*      */     catch (IOException e) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void parseString(String string, int offset)
/*      */     throws XMLParseException
/*      */   {
/* 1776 */     parseString(string.substring(offset));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void parseString(String string, int offset, int end)
/*      */     throws XMLParseException
/*      */   {
/* 1811 */     parseString(string.substring(offset, end));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void parseString(String string, int offset, int end, int startingLineNr)
/*      */     throws XMLParseException
/*      */   {
/* 1849 */     string = string.substring(offset, end);
/*      */     try {
/* 1851 */       parseFromReader(new StringReader(string), startingLineNr);
/*      */     }
/*      */     catch (IOException e) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void parseCharArray(char[] input, int offset, int end)
/*      */     throws XMLParseException
/*      */   {
/* 1889 */     parseCharArray(input, offset, end, 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void parseCharArray(char[] input, int offset, int end, int startingLineNr)
/*      */     throws XMLParseException
/*      */   {
/*      */     try
/*      */     {
/* 1928 */       Reader reader = new CharArrayReader(input, offset, end);
/* 1929 */       parseFromReader(reader, startingLineNr);
/*      */     }
/*      */     catch (IOException e) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeChild(XMLElement child)
/*      */   {
/* 1961 */     this.children.removeElement(child);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeAttribute(String name)
/*      */   {
/* 2029 */     if (this.ignoreCase) {
/* 2030 */       name = name.toUpperCase();
/*      */     }
/* 2032 */     this.attributes.remove(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public void removeProperty(String name)
/*      */   {
/* 2047 */     removeAttribute(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public void removeChild(String name)
/*      */   {
/* 2062 */     removeAttribute(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected XMLElement createAnotherElement()
/*      */   {
/* 2073 */     return new XMLElement(this.entities, this.ignoreWhitespace, false, this.ignoreCase);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setContent(String content)
/*      */   {
/* 2088 */     this.contents = content;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public void setTagName(String name)
/*      */   {
/* 2102 */     setName(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setName(String name)
/*      */   {
/* 2121 */     this.name = name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*      */     try
/*      */     {
/* 2133 */       ByteArrayOutputStream out = new ByteArrayOutputStream();
/* 2134 */       OutputStreamWriter writer = new OutputStreamWriter(out);
/* 2135 */       write(writer);
/* 2136 */       writer.flush();
/* 2137 */       return new String(out.toByteArray());
/*      */     }
/*      */     catch (IOException e) {}
/* 2140 */     return super.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void write(Writer writer)
/*      */     throws IOException
/*      */   {
/* 2164 */     if (this.name == null) {
/* 2165 */       writeEncoded(writer, this.contents);
/* 2166 */       return;
/*      */     }
/* 2168 */     writer.write(60);
/* 2169 */     writer.write(this.name);
/* 2170 */     if (!this.attributes.isEmpty()) {
/* 2171 */       Enumeration enum = this.attributes.keys();
/* 2172 */       while (enum.hasMoreElements()) {
/* 2173 */         writer.write(32);
/* 2174 */         String key = (String)enum.nextElement();
/* 2175 */         String value = (String)this.attributes.get(key);
/* 2176 */         writer.write(key);
/* 2177 */         writer.write(61);writer.write(34);
/* 2178 */         writeEncoded(writer, value);
/* 2179 */         writer.write(34);
/*      */       }
/*      */     }
/* 2182 */     if ((this.contents != null) && (this.contents.length() > 0)) {
/* 2183 */       writer.write(62);
/* 2184 */       writeEncoded(writer, this.contents);
/* 2185 */       writer.write(60);writer.write(47);
/* 2186 */       writer.write(this.name);
/* 2187 */       writer.write(62);
/* 2188 */     } else if (this.children.isEmpty()) {
/* 2189 */       writer.write(47);writer.write(62);
/*      */     } else {
/* 2191 */       writer.write(62);
/* 2192 */       Enumeration enum = enumerateChildren();
/* 2193 */       while (enum.hasMoreElements()) {
/* 2194 */         XMLElement child = (XMLElement)enum.nextElement();
/* 2195 */         child.write(writer);
/*      */       }
/* 2197 */       writer.write(60);writer.write(47);
/* 2198 */       writer.write(this.name);
/* 2199 */       writer.write(62);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void writeEncoded(Writer writer, String str)
/*      */     throws IOException
/*      */   {
/* 2222 */     for (int i = 0; i < str.length(); i++) {
/* 2223 */       char ch = str.charAt(i);
/* 2224 */       switch (ch) {
/*      */       case '<': 
/* 2226 */         writer.write(38);writer.write(108);writer.write(116);
/* 2227 */         writer.write(59);
/* 2228 */         break;
/*      */       case '>': 
/* 2230 */         writer.write(38);writer.write(103);writer.write(116);
/* 2231 */         writer.write(59);
/* 2232 */         break;
/*      */       case '&': 
/* 2234 */         writer.write(38);writer.write(97);writer.write(109);
/* 2235 */         writer.write(112);writer.write(59);
/* 2236 */         break;
/*      */       case '"': 
/* 2238 */         writer.write(38);writer.write(113);writer.write(117);
/* 2239 */         writer.write(111);writer.write(116);writer.write(59);
/* 2240 */         break;
/*      */       case '\'': 
/* 2242 */         writer.write(38);writer.write(97);writer.write(112);
/* 2243 */         writer.write(111);writer.write(115);writer.write(59);
/* 2244 */         break;
/*      */       default: 
/* 2246 */         int unicode = ch;
/* 2247 */         if ((unicode < 32) || (unicode > 126)) {
/* 2248 */           writer.write(38);writer.write(35);
/* 2249 */           writer.write(120);
/* 2250 */           writer.write(Integer.toString(unicode, 16));
/* 2251 */           writer.write(59);
/*      */         } else {
/* 2253 */           writer.write(ch);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         break;
/*      */       }
/*      */       
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void scanIdentifier(StringBuffer result)
/*      */     throws IOException
/*      */   {
/*      */     for (;;)
/*      */     {
/* 2282 */       char ch = readChar();
/* 2283 */       if (((ch < 'A') || (ch > 'Z')) && ((ch < 'a') || (ch > 'z')) && ((ch < '0') || (ch > '9')) && (ch != '_') && (ch != '.') && (ch != ':') && (ch != '-') && (ch <= '~'))
/*      */       {
/*      */ 
/* 2286 */         unreadChar(ch);
/* 2287 */         return;
/*      */       }
/* 2289 */       result.append(ch);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected char scanWhitespace()
/*      */     throws IOException
/*      */   {
/*      */     for (;;)
/*      */     {
/* 2303 */       char ch = readChar();
/* 2304 */       switch (ch) {
/*      */       case '\t': 
/*      */       case '\n': 
/*      */       case '\r': 
/*      */       case ' ': 
/*      */         break;
/*      */       default: 
/* 2311 */         return ch;
/*      */       }
/*      */       
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected char scanWhitespace(StringBuffer result)
/*      */     throws IOException
/*      */   {
/*      */     for (;;)
/*      */     {
/* 2331 */       char ch = readChar();
/* 2332 */       switch (ch) {
/*      */       case '\t': 
/*      */       case '\n': 
/*      */       case ' ': 
/* 2336 */         result.append(ch);
/*      */       case '\r': 
/*      */         break;
/*      */       default: 
/* 2340 */         return ch;
/*      */       }
/*      */       
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void scanString(StringBuffer string)
/*      */     throws IOException
/*      */   {
/* 2359 */     char delimiter = readChar();
/* 2360 */     if ((delimiter != '\'') && (delimiter != '"')) {
/* 2361 */       throw expectedInput("' or \"");
/*      */     }
/*      */     for (;;) {
/* 2364 */       char ch = readChar();
/* 2365 */       if (ch == delimiter)
/* 2366 */         return;
/* 2367 */       if (ch == '&') {
/* 2368 */         resolveEntity(string);
/*      */       } else {
/* 2370 */         string.append(ch);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void scanPCData(StringBuffer data)
/*      */     throws IOException
/*      */   {
/*      */     for (;;)
/*      */     {
/* 2389 */       char ch = readChar();
/* 2390 */       if (ch == '<') {
/* 2391 */         ch = readChar();
/* 2392 */         if (ch == '!') {
/* 2393 */           checkCDATA(data);
/*      */         } else {
/* 2395 */           unreadChar(ch);
/*      */         }
/*      */       }
/* 2398 */       else if (ch == '&') {
/* 2399 */         resolveEntity(data);
/*      */       } else {
/* 2401 */         data.append(ch);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean checkCDATA(StringBuffer buf)
/*      */     throws IOException
/*      */   {
/* 2419 */     char ch = readChar();
/* 2420 */     if (ch != '[') {
/* 2421 */       unreadChar(ch);
/* 2422 */       skipSpecialTag(0);
/* 2423 */       return false; }
/* 2424 */     if (!checkLiteral("CDATA[")) {
/* 2425 */       skipSpecialTag(1);
/* 2426 */       return false;
/*      */     }
/* 2428 */     int delimiterCharsSkipped = 0;
/* 2429 */     while (delimiterCharsSkipped < 3) {
/* 2430 */       ch = readChar();
/* 2431 */       switch (ch) {
/*      */       case ']': 
/* 2433 */         if (delimiterCharsSkipped < 2) {
/* 2434 */           delimiterCharsSkipped++;
/*      */         } else {
/* 2436 */           buf.append(']');
/* 2437 */           buf.append(']');
/* 2438 */           delimiterCharsSkipped = 0;
/*      */         }
/* 2440 */         break;
/*      */       case '>': 
/* 2442 */         if (delimiterCharsSkipped < 2) {
/* 2443 */           for (int i = 0; i < delimiterCharsSkipped; i++) {
/* 2444 */             buf.append(']');
/*      */           }
/* 2446 */           delimiterCharsSkipped = 0;
/* 2447 */           buf.append('>');
/*      */         } else {
/* 2449 */           delimiterCharsSkipped = 3;
/*      */         }
/* 2451 */         break;
/*      */       default: 
/* 2453 */         for (int i = 0; i < delimiterCharsSkipped; i++) {
/* 2454 */           buf.append(']');
/*      */         }
/* 2456 */         buf.append(ch);
/* 2457 */         delimiterCharsSkipped = 0;
/*      */       }
/*      */     }
/* 2460 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void skipComment()
/*      */     throws IOException
/*      */   {
/* 2475 */     int dashesToRead = 2;
/* 2476 */     while (dashesToRead > 0) {
/* 2477 */       char ch = readChar();
/* 2478 */       if (ch == '-') {
/* 2479 */         dashesToRead--;
/*      */       } else {
/* 2481 */         dashesToRead = 2;
/*      */       }
/*      */     }
/* 2484 */     if (readChar() != '>') {
/* 2485 */       throw expectedInput(">");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void skipSpecialTag(int bracketLevel)
/*      */     throws IOException
/*      */   {
/* 2504 */     int tagLevel = 1;
/* 2505 */     char stringDelimiter = '\000';
/* 2506 */     if (bracketLevel == 0) {
/* 2507 */       char ch = readChar();
/* 2508 */       if (ch == '[') {
/* 2509 */         bracketLevel++;
/* 2510 */       } else if (ch == '-') {
/* 2511 */         ch = readChar();
/* 2512 */         if (ch == '[') {
/* 2513 */           bracketLevel++;
/* 2514 */         } else if (ch == ']') {
/* 2515 */           bracketLevel--;
/* 2516 */         } else if (ch == '-') {
/* 2517 */           skipComment();
/* 2518 */           return;
/*      */         }
/*      */       }
/*      */     }
/* 2522 */     while (tagLevel > 0) {
/* 2523 */       char ch = readChar();
/* 2524 */       if (stringDelimiter == 0) {
/* 2525 */         if ((ch == '"') || (ch == '\'')) {
/* 2526 */           stringDelimiter = ch;
/* 2527 */         } else if (bracketLevel <= 0) {
/* 2528 */           if (ch == '<') {
/* 2529 */             tagLevel++;
/* 2530 */           } else if (ch == '>') {
/* 2531 */             tagLevel--;
/*      */           }
/*      */         }
/* 2534 */         if (ch == '[') {
/* 2535 */           bracketLevel++;
/* 2536 */         } else if (ch == ']') {
/* 2537 */           bracketLevel--;
/*      */         }
/*      */       }
/* 2540 */       else if (ch == stringDelimiter) {
/* 2541 */         stringDelimiter = '\000';
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean checkLiteral(String literal)
/*      */     throws IOException
/*      */   {
/* 2562 */     int length = literal.length();
/* 2563 */     for (int i = 0; i < length; i++) {
/* 2564 */       if (readChar() != literal.charAt(i)) {
/* 2565 */         return false;
/*      */       }
/*      */     }
/* 2568 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected char readChar()
/*      */     throws IOException
/*      */   {
/* 2578 */     if (this.charReadTooMuch != 0) {
/* 2579 */       char ch = this.charReadTooMuch;
/* 2580 */       this.charReadTooMuch = '\000';
/* 2581 */       return ch;
/*      */     }
/* 2583 */     int i = this.reader.read();
/* 2584 */     if (i < 0)
/* 2585 */       throw unexpectedEndOfData();
/* 2586 */     if (i == 10) {
/* 2587 */       this.parserLineNr += 1;
/* 2588 */       return '\n';
/*      */     }
/* 2590 */     return (char)i;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void scanElement(XMLElement elt)
/*      */     throws IOException
/*      */   {
/* 2609 */     StringBuffer buf = new StringBuffer();
/* 2610 */     scanIdentifier(buf);
/* 2611 */     String name = buf.toString();
/* 2612 */     elt.setName(name);
/* 2613 */     char ch = scanWhitespace();
/* 2614 */     while ((ch != '>') && (ch != '/')) {
/* 2615 */       buf.setLength(0);
/* 2616 */       unreadChar(ch);
/* 2617 */       scanIdentifier(buf);
/* 2618 */       String key = buf.toString();
/* 2619 */       ch = scanWhitespace();
/* 2620 */       if (ch != '=') {
/* 2621 */         throw expectedInput("=");
/*      */       }
/* 2623 */       unreadChar(scanWhitespace());
/* 2624 */       buf.setLength(0);
/* 2625 */       scanString(buf);
/* 2626 */       elt.setAttribute(key, buf);
/* 2627 */       ch = scanWhitespace();
/*      */     }
/* 2629 */     if (ch == '/') {
/* 2630 */       ch = readChar();
/* 2631 */       if (ch != '>') {
/* 2632 */         throw expectedInput(">");
/*      */       }
/* 2634 */       return;
/*      */     }
/* 2636 */     buf.setLength(0);
/* 2637 */     ch = scanWhitespace(buf);
/* 2638 */     if (ch != '<') {
/* 2639 */       unreadChar(ch);
/* 2640 */       scanPCData(buf);
/*      */     } else {
/*      */       do {
/* 2643 */         ch = readChar();
/* 2644 */         if (ch != '!') break;
/* 2645 */         if (checkCDATA(buf)) {
/* 2646 */           scanPCData(buf);
/*      */           break label272;
/*      */         }
/* 2649 */         ch = scanWhitespace(buf);
/* 2650 */       } while (ch == '<');
/* 2651 */       unreadChar(ch);
/* 2652 */       scanPCData(buf);
/*      */       
/*      */ 
/*      */       break label272;
/*      */       
/* 2657 */       if ((ch != '/') || (this.ignoreWhitespace)) {
/* 2658 */         buf.setLength(0);
/*      */       }
/* 2660 */       if (ch == '/') {
/* 2661 */         unreadChar(ch);
/*      */       }
/*      */     }
/*      */     
/*      */     label272:
/*      */     
/* 2667 */     if (buf.length() == 0) {
/* 2668 */       while (ch != '/') {
/* 2669 */         if (ch == '!') {
/* 2670 */           ch = readChar();
/* 2671 */           if (ch != '-') {
/* 2672 */             throw expectedInput("Comment or Element");
/*      */           }
/* 2674 */           ch = readChar();
/* 2675 */           if (ch != '-') {
/* 2676 */             throw expectedInput("Comment or Element");
/*      */           }
/* 2678 */           skipComment();
/*      */         } else {
/* 2680 */           unreadChar(ch);
/* 2681 */           XMLElement child = createAnotherElement();
/* 2682 */           scanElement(child);
/* 2683 */           elt.addChild(child);
/*      */         }
/* 2685 */         ch = scanWhitespace();
/* 2686 */         if (ch != '<') {
/* 2687 */           throw expectedInput("<");
/*      */         }
/* 2689 */         ch = readChar();
/*      */       }
/* 2691 */       unreadChar(ch);
/*      */     }
/* 2693 */     else if (this.ignoreWhitespace) {
/* 2694 */       elt.setContent(buf.toString().trim());
/*      */     } else {
/* 2696 */       elt.setContent(buf.toString());
/*      */     }
/*      */     
/* 2699 */     ch = readChar();
/* 2700 */     if (ch != '/') {
/* 2701 */       throw expectedInput("/");
/*      */     }
/* 2703 */     unreadChar(scanWhitespace());
/* 2704 */     if (!checkLiteral(name)) {
/* 2705 */       throw expectedInput(name);
/*      */     }
/* 2707 */     if (scanWhitespace() != '>') {
/* 2708 */       throw expectedInput(">");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void resolveEntity(StringBuffer buf)
/*      */     throws IOException
/*      */   {
/* 2727 */     char ch = '\000';
/* 2728 */     StringBuffer keyBuf = new StringBuffer();
/*      */     for (;;) {
/* 2730 */       ch = readChar();
/* 2731 */       if (ch == ';') {
/*      */         break;
/*      */       }
/* 2734 */       keyBuf.append(ch);
/*      */     }
/* 2736 */     String key = keyBuf.toString();
/* 2737 */     if (key.charAt(0) == '#') {
/*      */       try {
/* 2739 */         if (key.charAt(1) == 'x') {
/* 2740 */           ch = (char)Integer.parseInt(key.substring(2), 16);
/*      */         } else {
/* 2742 */           ch = (char)Integer.parseInt(key.substring(1), 10);
/*      */         }
/*      */       } catch (NumberFormatException e) {
/* 2745 */         throw unknownEntity(key);
/*      */       }
/* 2747 */       buf.append(ch);
/*      */     } else {
/* 2749 */       char[] value = (char[])this.entities.get(key);
/* 2750 */       if (value == null) {
/* 2751 */         throw unknownEntity(key);
/*      */       }
/* 2753 */       buf.append(value);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void unreadChar(char ch)
/*      */   {
/* 2770 */     this.charReadTooMuch = ch;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected XMLParseException invalidValueSet(String name)
/*      */   {
/* 2786 */     String msg = "Invalid value set (entity name = \"" + name + "\")";
/* 2787 */     return new XMLParseException(getName(), this.parserLineNr, msg);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected XMLParseException invalidValue(String name, String value)
/*      */   {
/* 2806 */     String msg = "Attribute \"" + name + "\" does not contain a valid " + "value (\"" + value + "\")";
/*      */     
/* 2808 */     return new XMLParseException(getName(), this.parserLineNr, msg);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected XMLParseException unexpectedEndOfData()
/*      */   {
/* 2818 */     String msg = "Unexpected end of data reached";
/* 2819 */     return new XMLParseException(getName(), this.parserLineNr, msg);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected XMLParseException syntaxError(String context)
/*      */   {
/* 2835 */     String msg = "Syntax error while parsing " + context;
/* 2836 */     return new XMLParseException(getName(), this.parserLineNr, msg);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected XMLParseException expectedInput(String charSet)
/*      */   {
/* 2854 */     String msg = "Expected: " + charSet;
/* 2855 */     return new XMLParseException(getName(), this.parserLineNr, msg);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected XMLParseException unknownEntity(String name)
/*      */   {
/* 2871 */     String msg = "Unknown or invalid entity: &" + name + ";";
/* 2872 */     return new XMLParseException(getName(), this.parserLineNr, msg);
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jvnet\lafplugin\XMLElement.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */