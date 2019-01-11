/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EscapeTokenizer
/*     */ {
/*  36 */   private int bracesLevel = 0;
/*     */   
/*  38 */   private boolean emittingEscapeCode = false;
/*     */   
/*  40 */   private boolean inComment = false;
/*     */   
/*  42 */   private boolean inQuotes = false;
/*     */   
/*  44 */   private char lastChar = '\000';
/*     */   
/*  46 */   private char lastLastChar = '\000';
/*     */   
/*  48 */   private int pos = 0;
/*     */   
/*  50 */   private char quoteChar = '\000';
/*     */   
/*  52 */   private boolean sawVariableUse = false;
/*     */   
/*  54 */   private String source = null;
/*     */   
/*  56 */   private int sourceLength = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EscapeTokenizer(String s)
/*     */   {
/*  68 */     this.source = s;
/*  69 */     this.sourceLength = s.length();
/*  70 */     this.pos = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized boolean hasMoreTokens()
/*     */   {
/*  82 */     return this.pos < this.sourceLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized String nextToken()
/*     */   {
/*  91 */     StringBuffer tokenBuf = new StringBuffer();
/*     */     
/*  93 */     if (this.emittingEscapeCode) {
/*  94 */       tokenBuf.append("{");
/*  95 */       this.emittingEscapeCode = false;
/*     */     }
/*  98 */     for (; 
/*  98 */         this.pos < this.sourceLength; this.pos += 1) {
/*  99 */       char c = this.source.charAt(this.pos);
/*     */       
/*     */ 
/*     */ 
/* 103 */       if ((!this.inQuotes) && (c == '@')) {
/* 104 */         this.sawVariableUse = true;
/*     */       }
/*     */       
/* 107 */       if ((c == '\'') || (c == '"')) {
/* 108 */         if ((this.inQuotes) && (c == this.quoteChar) && 
/* 109 */           (this.pos + 1 < this.sourceLength) && 
/* 110 */           (this.source.charAt(this.pos + 1) == this.quoteChar))
/*     */         {
/* 112 */           tokenBuf.append(this.quoteChar);
/* 113 */           tokenBuf.append(this.quoteChar);
/* 114 */           this.pos += 1;
/* 115 */           continue;
/*     */         }
/*     */         
/*     */ 
/* 119 */         if (this.lastChar != '\\') {
/* 120 */           if (this.inQuotes) {
/* 121 */             if (this.quoteChar == c) {
/* 122 */               this.inQuotes = false;
/*     */             }
/*     */           } else {
/* 125 */             this.inQuotes = true;
/* 126 */             this.quoteChar = c;
/*     */           }
/* 128 */         } else if (this.lastLastChar == '\\') {
/* 129 */           if (this.inQuotes) {
/* 130 */             if (this.quoteChar == c) {
/* 131 */               this.inQuotes = false;
/*     */             }
/*     */           } else {
/* 134 */             this.inQuotes = true;
/* 135 */             this.quoteChar = c;
/*     */           }
/*     */         }
/*     */         
/* 139 */         tokenBuf.append(c);
/* 140 */       } else if (c == '-') {
/* 141 */         if (this.lastChar == '-') { if (((this.lastLastChar != '\\' ? 1 : 0) & (!this.inQuotes ? 1 : 0)) != 0)
/*     */           {
/* 143 */             this.inComment = true;
/*     */           }
/*     */         }
/* 146 */         tokenBuf.append(c);
/* 147 */       } else if ((c == '\n') || (c == '\r')) {
/* 148 */         this.inComment = false;
/*     */         
/* 150 */         tokenBuf.append(c);
/* 151 */       } else if (c == '{') {
/* 152 */         if ((this.inQuotes) || (this.inComment)) {
/* 153 */           tokenBuf.append(c);
/*     */         } else {
/* 155 */           this.bracesLevel += 1;
/*     */           
/* 157 */           if (this.bracesLevel == 1) {
/* 158 */             this.pos += 1;
/* 159 */             this.emittingEscapeCode = true;
/*     */             
/* 161 */             return tokenBuf.toString();
/*     */           }
/*     */           
/* 164 */           tokenBuf.append(c);
/*     */         }
/* 166 */       } else if (c == '}') {
/* 167 */         tokenBuf.append(c);
/*     */         
/* 169 */         if ((!this.inQuotes) && (!this.inComment)) {
/* 170 */           this.lastChar = c;
/*     */           
/* 172 */           this.bracesLevel -= 1;
/*     */           
/* 174 */           if (this.bracesLevel == 0) {
/* 175 */             this.pos += 1;
/*     */             
/* 177 */             return tokenBuf.toString();
/*     */           }
/*     */         }
/*     */       } else {
/* 181 */         tokenBuf.append(c);
/*     */       }
/*     */       
/* 184 */       this.lastLastChar = this.lastChar;
/* 185 */       this.lastChar = c;
/*     */     }
/*     */     
/* 188 */     return tokenBuf.toString();
/*     */   }
/*     */   
/*     */   boolean sawVariableUse() {
/* 192 */     return this.sawVariableUse;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\EscapeTokenizer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */