/*    */ package org.jdesktop.swingx.plaf.basic;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Font;
/*    */ import javax.swing.AbstractButton;
/*    */ import javax.swing.Action;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.Box;
/*    */ import javax.swing.BoxLayout;
/*    */ import org.jdesktop.swingx.JXHyperlink;
/*    */ import org.jdesktop.swingx.JXPanel;
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
/*    */ @Deprecated
/*    */ class BasicCalendarHeader
/*    */   extends JXPanel
/*    */ {
/*    */   protected AbstractButton prevButton;
/*    */   protected AbstractButton nextButton;
/*    */   protected JXHyperlink zoomOutLink;
/*    */   
/*    */   public BasicCalendarHeader()
/*    */   {
/* 38 */     setLayout(new BoxLayout(this, 2));
/* 39 */     this.prevButton = createNavigationButton();
/* 40 */     this.nextButton = createNavigationButton();
/* 41 */     this.zoomOutLink = createZoomLink();
/* 42 */     add(this.prevButton);
/* 43 */     add(Box.createHorizontalGlue());
/* 44 */     add(this.zoomOutLink);
/* 45 */     add(Box.createHorizontalGlue());
/* 46 */     add(this.nextButton);
/* 47 */     setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setActions(Action prev, Action next, Action zoomOut)
/*    */   {
/* 58 */     this.prevButton.setAction(prev);
/* 59 */     this.nextButton.setAction(next);
/* 60 */     this.zoomOutLink.setAction(zoomOut);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setFont(Font font)
/*    */   {
/* 71 */     super.setFont(font);
/* 72 */     if (this.zoomOutLink != null)
/* 73 */       this.zoomOutLink.setFont(font);
/*    */   }
/*    */   
/*    */   private JXHyperlink createZoomLink() {
/* 77 */     JXHyperlink zoomOutLink = new JXHyperlink();
/* 78 */     Color textColor = new Color(16, 66, 104);
/* 79 */     zoomOutLink.setUnclickedColor(textColor);
/* 80 */     zoomOutLink.setClickedColor(textColor);
/* 81 */     zoomOutLink.setFocusable(false);
/* 82 */     return zoomOutLink;
/*    */   }
/*    */   
/*    */   private AbstractButton createNavigationButton() {
/* 86 */     JXHyperlink b = new JXHyperlink();
/* 87 */     b.setContentAreaFilled(false);
/* 88 */     b.setBorder(BorderFactory.createEmptyBorder());
/* 89 */     b.setRolloverEnabled(true);
/* 90 */     b.setFocusable(false);
/* 91 */     return b;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\BasicCalendarHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */