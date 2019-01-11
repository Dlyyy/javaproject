/*    */ package org.jdesktop.swingx.prompt;
/*    */ 
/*    */ import java.awt.Cursor;
/*    */ import java.awt.Insets;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.border.Border;
/*    */ import org.jdesktop.swingx.plaf.SearchFieldUI;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BuddyButton
/*    */   extends JButton
/*    */ {
/*    */   public BuddyButton()
/*    */   {
/* 21 */     this(null);
/*    */   }
/*    */   
/*    */   public BuddyButton(String text) {
/* 25 */     super(text);
/* 26 */     setFocusable(false);
/* 27 */     setMargin(SearchFieldUI.NO_INSETS);
/*    */     
/*    */ 
/* 30 */     setFocusPainted(false);
/*    */     
/* 32 */     setBorderPainted(false);
/* 33 */     setContentAreaFilled(false);
/* 34 */     setIconTextGap(0);
/*    */     
/* 36 */     setBorder(null);
/*    */     
/* 38 */     setOpaque(false);
/*    */     
/* 40 */     setCursor(Cursor.getDefaultCursor());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Insets getInsets()
/*    */   {
/* 47 */     return SearchFieldUI.NO_INSETS;
/*    */   }
/*    */   
/*    */   public Insets getInsets(Insets insets)
/*    */   {
/* 52 */     return getInsets();
/*    */   }
/*    */   
/*    */   public Insets getMargin()
/*    */   {
/* 57 */     return getInsets();
/*    */   }
/*    */   
/*    */ 
/*    */   public void setBorder(Border border)
/*    */   {
/* 63 */     super.setBorder(BorderFactory.createEmptyBorder());
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\prompt\BuddyButton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */