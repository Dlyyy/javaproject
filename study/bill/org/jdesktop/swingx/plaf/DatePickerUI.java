/*    */ package org.jdesktop.swingx.plaf;
/*    */ 
/*    */ import java.beans.PropertyVetoException;
/*    */ import java.util.Date;
/*    */ import javax.swing.plaf.ComponentUI;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class DatePickerUI
/*    */   extends ComponentUI
/*    */ {
/*    */   public int getBaseline(int width, int height)
/*    */   {
/* 56 */     return -1;
/*    */   }
/*    */   
/*    */   public abstract Date getSelectableDate(Date paramDate)
/*    */     throws PropertyVetoException;
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\DatePickerUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */