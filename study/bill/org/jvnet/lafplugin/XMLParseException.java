/*     */ package org.jvnet.lafplugin;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMLParseException
/*     */   extends RuntimeException
/*     */ {
/*     */   public static final int NO_LINE = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int lineNr;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public XMLParseException(String name, String message)
/*     */   {
/*  83 */     super("XML Parse Exception during parsing of " + (name == null ? "the XML definition" : new StringBuffer().append("a ").append(name).append(" element").toString()) + ": " + message);
/*     */     
/*     */ 
/*     */ 
/*  87 */     this.lineNr = -1;
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
/*     */   public XMLParseException(String name, int lineNr, String message)
/*     */   {
/* 111 */     super("XML Parse Exception during parsing of " + (name == null ? "the XML definition" : new StringBuffer().append("a ").append(name).append(" element").toString()) + " at line " + lineNr + ": " + message);
/*     */     
/*     */ 
/*     */ 
/* 115 */     this.lineNr = lineNr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getLineNr()
/*     */   {
/* 127 */     return this.lineNr;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jvnet\lafplugin\XMLParseException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */