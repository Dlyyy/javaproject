/*    */ package org.jdesktop.swingx.event;
/*    */ 
/*    */ import java.util.Date;
/*    */ import java.util.EventObject;
/*    */ import java.util.SortedSet;
/*    */ import org.jdesktop.swingx.calendar.DateSelectionModel;
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
/*    */ public class DateSelectionEvent
/*    */   extends EventObject
/*    */ {
/*    */   private EventType eventType;
/*    */   private boolean adjusting;
/*    */   
/*    */   public static enum EventType
/*    */   {
/* 34 */     DATES_ADDED, 
/* 35 */     DATES_REMOVED, 
/* 36 */     DATES_SET, 
/* 37 */     SELECTION_CLEARED, 
/* 38 */     SELECTABLE_DATES_CHANGED, 
/* 39 */     SELECTABLE_RANGE_CHANGED, 
/* 40 */     UNSELECTED_DATES_CHANGED, 
/* 41 */     LOWER_BOUND_CHANGED, 
/* 42 */     UPPER_BOUND_CHANGED, 
/* 43 */     ADJUSTING_STARTED,  ADJUSTING_STOPPED, 
/* 44 */     CALENDAR_CHANGED;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     private EventType() {}
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DateSelectionEvent(Object source, EventType eventType, boolean adjusting)
/*    */   {
/* 59 */     super(source);
/* 60 */     this.eventType = eventType;
/* 61 */     this.adjusting = adjusting;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SortedSet<Date> getSelection()
/*    */   {
/* 73 */     return ((DateSelectionModel)this.source).getSelection();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final EventType getEventType()
/*    */   {
/* 82 */     return this.eventType;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isAdjusting()
/*    */   {
/* 91 */     return this.adjusting;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 96 */     return "[" + String.valueOf(getSource()) + " type: " + getEventType() + " isAdjusting: " + isAdjusting();
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\event\DateSelectionEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */