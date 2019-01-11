/*     */ package org.jdesktop.swingx.plaf.basic.core;
/*     */ 
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.datatransfer.UnsupportedFlavorException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringBufferInputStream;
/*     */ import java.io.StringReader;
/*     */ import javax.swing.plaf.UIResource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicTransferable
/*     */   implements Transferable, UIResource
/*     */ {
/*     */   protected String plainData;
/*     */   protected String htmlData;
/*     */   private static DataFlavor[] htmlFlavors;
/*     */   private static DataFlavor[] stringFlavors;
/*     */   private static DataFlavor[] plainFlavors;
/*     */   
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  47 */       htmlFlavors = new DataFlavor[3];
/*  48 */       htmlFlavors[0] = new DataFlavor("text/html;class=java.lang.String");
/*  49 */       htmlFlavors[1] = new DataFlavor("text/html;class=java.io.Reader");
/*  50 */       htmlFlavors[2] = new DataFlavor("text/html;charset=unicode;class=java.io.InputStream");
/*     */       
/*  52 */       plainFlavors = new DataFlavor[3];
/*  53 */       plainFlavors[0] = new DataFlavor("text/plain;class=java.lang.String");
/*  54 */       plainFlavors[1] = new DataFlavor("text/plain;class=java.io.Reader");
/*  55 */       plainFlavors[2] = new DataFlavor("text/plain;charset=unicode;class=java.io.InputStream");
/*     */       
/*  57 */       stringFlavors = new DataFlavor[2];
/*  58 */       stringFlavors[0] = new DataFlavor("application/x-java-jvm-local-objectref;class=java.lang.String");
/*  59 */       stringFlavors[1] = DataFlavor.stringFlavor;
/*     */     }
/*     */     catch (ClassNotFoundException cle) {
/*  62 */       System.err.println("error initializing javax.swing.plaf.basic.BasicTranserable");
/*     */     }
/*     */   }
/*     */   
/*     */   public BasicTransferable(String plainData, String htmlData) {
/*  67 */     this.plainData = plainData;
/*  68 */     this.htmlData = htmlData;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DataFlavor[] getTransferDataFlavors()
/*     */   {
/*  79 */     DataFlavor[] richerFlavors = getRicherFlavors();
/*  80 */     int nRicher = richerFlavors != null ? richerFlavors.length : 0;
/*  81 */     int nHTML = isHTMLSupported() ? htmlFlavors.length : 0;
/*  82 */     int nPlain = isPlainSupported() ? plainFlavors.length : 0;
/*  83 */     int nString = isPlainSupported() ? stringFlavors.length : 0;
/*  84 */     int nFlavors = nRicher + nHTML + nPlain + nString;
/*  85 */     DataFlavor[] flavors = new DataFlavor[nFlavors];
/*     */     
/*     */ 
/*  88 */     int nDone = 0;
/*  89 */     if (nRicher > 0) {
/*  90 */       System.arraycopy(richerFlavors, 0, flavors, nDone, nRicher);
/*  91 */       nDone += nRicher;
/*     */     }
/*  93 */     if (nHTML > 0) {
/*  94 */       System.arraycopy(htmlFlavors, 0, flavors, nDone, nHTML);
/*  95 */       nDone += nHTML;
/*     */     }
/*  97 */     if (nPlain > 0) {
/*  98 */       System.arraycopy(plainFlavors, 0, flavors, nDone, nPlain);
/*  99 */       nDone += nPlain;
/*     */     }
/* 101 */     if (nString > 0) {
/* 102 */       System.arraycopy(stringFlavors, 0, flavors, nDone, nString);
/* 103 */       nDone += nString;
/*     */     }
/* 105 */     return flavors;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDataFlavorSupported(DataFlavor flavor)
/*     */   {
/* 115 */     DataFlavor[] flavors = getTransferDataFlavors();
/* 116 */     for (int i = 0; i < flavors.length; i++) {
/* 117 */       if (flavors[i].equals(flavor)) {
/* 118 */         return true;
/*     */       }
/*     */     }
/* 121 */     return false;
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
/*     */   public Object getTransferData(DataFlavor flavor)
/*     */     throws UnsupportedFlavorException, IOException
/*     */   {
/* 136 */     DataFlavor[] richerFlavors = getRicherFlavors();
/* 137 */     if (isRicherFlavor(flavor))
/* 138 */       return getRicherData(flavor);
/* 139 */     if (isHTMLFlavor(flavor)) {
/* 140 */       String data = getHTMLData();
/* 141 */       data = data == null ? "" : data;
/* 142 */       if (String.class.equals(flavor.getRepresentationClass()))
/* 143 */         return data;
/* 144 */       if (Reader.class.equals(flavor.getRepresentationClass()))
/* 145 */         return new StringReader(data);
/* 146 */       if (InputStream.class.equals(flavor.getRepresentationClass())) {
/* 147 */         return new StringBufferInputStream(data);
/*     */       }
/*     */     }
/* 150 */     else if (isPlainFlavor(flavor)) {
/* 151 */       String data = getPlainData();
/* 152 */       data = data == null ? "" : data;
/* 153 */       if (String.class.equals(flavor.getRepresentationClass()))
/* 154 */         return data;
/* 155 */       if (Reader.class.equals(flavor.getRepresentationClass()))
/* 156 */         return new StringReader(data);
/* 157 */       if (InputStream.class.equals(flavor.getRepresentationClass())) {
/* 158 */         return new StringBufferInputStream(data);
/*     */       }
/*     */       
/*     */     }
/* 162 */     else if (isStringFlavor(flavor)) {
/* 163 */       String data = getPlainData();
/* 164 */       data = data == null ? "" : data;
/* 165 */       return data;
/*     */     }
/* 167 */     throw new UnsupportedFlavorException(flavor);
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean isRicherFlavor(DataFlavor flavor)
/*     */   {
/* 173 */     DataFlavor[] richerFlavors = getRicherFlavors();
/* 174 */     int nFlavors = richerFlavors != null ? richerFlavors.length : 0;
/* 175 */     for (int i = 0; i < nFlavors; i++) {
/* 176 */       if (richerFlavors[i].equals(flavor)) {
/* 177 */         return true;
/*     */       }
/*     */     }
/* 180 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected DataFlavor[] getRicherFlavors()
/*     */   {
/* 189 */     return null;
/*     */   }
/*     */   
/*     */   protected Object getRicherData(DataFlavor flavor) throws UnsupportedFlavorException {
/* 193 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isHTMLFlavor(DataFlavor flavor)
/*     */   {
/* 205 */     DataFlavor[] flavors = htmlFlavors;
/* 206 */     for (int i = 0; i < flavors.length; i++) {
/* 207 */       if (flavors[i].equals(flavor)) {
/* 208 */         return true;
/*     */       }
/*     */     }
/* 211 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isHTMLSupported()
/*     */   {
/* 219 */     return this.htmlData != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getHTMLData()
/*     */   {
/* 226 */     return this.htmlData;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isPlainFlavor(DataFlavor flavor)
/*     */   {
/* 238 */     DataFlavor[] flavors = plainFlavors;
/* 239 */     for (int i = 0; i < flavors.length; i++) {
/* 240 */       if (flavors[i].equals(flavor)) {
/* 241 */         return true;
/*     */       }
/*     */     }
/* 244 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isPlainSupported()
/*     */   {
/* 252 */     return this.plainData != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getPlainData()
/*     */   {
/* 259 */     return this.plainData;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isStringFlavor(DataFlavor flavor)
/*     */   {
/* 271 */     DataFlavor[] flavors = stringFlavors;
/* 272 */     for (int i = 0; i < flavors.length; i++) {
/* 273 */       if (flavors[i].equals(flavor)) {
/* 274 */         return true;
/*     */       }
/*     */     }
/* 277 */     return false;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\core\BasicTransferable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */