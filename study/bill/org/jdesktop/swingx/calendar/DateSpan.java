/*     */ package org.jdesktop.swingx.calendar;
/*     */ 
/*     */ import java.util.Date;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DateSpan
/*     */ {
/*     */   private long _start;
/*     */   private long _end;
/*     */   
/*     */   public DateSpan(long start, long end)
/*     */   {
/*  47 */     this._start = start;
/*  48 */     this._end = end;
/*  49 */     if (this._start > this._end) {
/*  50 */       throw new IllegalArgumentException("Start date must be before end date");
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
/*     */   public DateSpan(Date start, Date end)
/*     */   {
/*  64 */     this(start.getTime(), end.getTime());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getStart()
/*     */   {
/*  73 */     return this._start;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getEnd()
/*     */   {
/*  82 */     return this._end;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getStartAsDate()
/*     */   {
/*  91 */     return new Date(getStart());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getEndAsDate()
/*     */   {
/* 100 */     return new Date(getEnd());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean contains(DateSpan span)
/*     */   {
/* 111 */     return (contains(span.getStart())) && (contains(span.getEnd()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean contains(long time)
/*     */   {
/* 122 */     return (time >= getStart()) && (time <= getEnd());
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
/*     */   public boolean contains(long start, long end)
/*     */   {
/* 135 */     return (start >= getStart()) && (end <= getEnd());
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
/*     */   public boolean intersects(long start, long end)
/*     */   {
/* 148 */     return (start <= getEnd()) && (end >= getStart());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean intersects(DateSpan span)
/*     */   {
/* 160 */     return intersects(span.getStart(), span.getEnd());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DateSpan add(DateSpan span)
/*     */   {
/* 171 */     return add(span.getStart(), span.getEnd());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DateSpan add(long start, long end)
/*     */   {
/* 183 */     return new DateSpan(Math.min(start, getStart()), Math.max(end, getEnd()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 193 */     if (o == this) {
/* 194 */       return true;
/*     */     }
/* 196 */     if ((o instanceof DateSpan)) {
/* 197 */       DateSpan ds = (DateSpan)o;
/* 198 */       return (this._start == ds.getStart()) && (this._end == ds.getEnd());
/*     */     }
/* 200 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 208 */     int result = 17;
/* 209 */     result = 37 * result + (int)(this._start ^ this._start >>> 32);
/* 210 */     result = 37 * result + (int)(this._end ^ this._end >>> 32);
/* 211 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 219 */     return "DateSpan [" + getStartAsDate() + "-" + getEndAsDate() + "]";
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\calendar\DateSpan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */