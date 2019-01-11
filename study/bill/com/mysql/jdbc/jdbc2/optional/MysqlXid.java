/*     */ package com.mysql.jdbc.jdbc2.optional;
/*     */ 
/*     */ import javax.transaction.xa.Xid;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MysqlXid
/*     */   implements Xid
/*     */ {
/*  35 */   int hash = 0;
/*     */   
/*     */   byte[] myBqual;
/*     */   
/*     */   int myFormatId;
/*     */   byte[] myGtrid;
/*     */   
/*     */   public MysqlXid(byte[] gtrid, byte[] bqual, int formatId)
/*     */   {
/*  44 */     this.myGtrid = gtrid;
/*  45 */     this.myBqual = bqual;
/*  46 */     this.myFormatId = formatId;
/*     */   }
/*     */   
/*     */   public boolean equals(Object another)
/*     */   {
/*  51 */     if ((another instanceof Xid)) {
/*  52 */       Xid anotherAsXid = (Xid)another;
/*     */       
/*  54 */       if (this.myFormatId != anotherAsXid.getFormatId()) {
/*  55 */         return false;
/*     */       }
/*     */       
/*  58 */       byte[] otherBqual = anotherAsXid.getBranchQualifier();
/*  59 */       byte[] otherGtrid = anotherAsXid.getGlobalTransactionId();
/*     */       
/*  61 */       if ((otherGtrid != null) && (otherGtrid.length == this.myGtrid.length)) {
/*  62 */         int length = otherGtrid.length;
/*     */         
/*  64 */         for (int i = 0; i < length; i++) {
/*  65 */           if (otherGtrid[i] != this.myGtrid[i]) {
/*  66 */             return false;
/*     */           }
/*     */         }
/*     */         
/*  70 */         if ((otherBqual != null) && (otherBqual.length == this.myBqual.length)) {
/*  71 */           length = otherBqual.length;
/*     */           
/*  73 */           for (int i = 0; i < length; i++) {
/*  74 */             if (otherBqual[i] != this.myBqual[i]) {
/*  75 */               return false;
/*     */             }
/*     */           }
/*     */         } else {
/*  79 */           return false;
/*     */         }
/*     */         
/*  82 */         return true;
/*     */       }
/*  84 */       return false;
/*     */     }
/*     */     
/*  87 */     return false;
/*     */   }
/*     */   
/*     */   public byte[] getBranchQualifier()
/*     */   {
/*  92 */     return this.myBqual;
/*     */   }
/*     */   
/*     */   public int getFormatId() {
/*  96 */     return this.myFormatId;
/*     */   }
/*     */   
/*     */   public byte[] getGlobalTransactionId() {
/* 100 */     return this.myGtrid;
/*     */   }
/*     */   
/*     */   public synchronized int hashCode() {
/* 104 */     if (this.hash == 0) {
/* 105 */       for (int i = 0; i < this.myGtrid.length; i++) {
/* 106 */         this.hash = (33 * this.hash + this.myGtrid[i]);
/*     */       }
/*     */     }
/*     */     
/* 110 */     return this.hash;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\jdbc2\optional\MysqlXid.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */