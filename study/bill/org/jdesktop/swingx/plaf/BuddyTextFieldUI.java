/*    */ package org.jdesktop.swingx.plaf;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Insets;
/*    */ import java.awt.LayoutManager;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JTextField;
/*    */ import javax.swing.border.Border;
/*    */ import javax.swing.plaf.TextUI;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BuddyTextFieldUI
/*    */   extends PromptTextFieldUI
/*    */ {
/*    */   protected BuddyLayoutAndBorder layoutAndBorder;
/* 24 */   private static final Insets MAC_MARGIN = new Insets(0, 2, 1, 2);
/*    */   
/*    */ 
/*    */ 
/*    */   public void paint(Graphics g, JComponent c)
/*    */   {
/* 30 */     if (hasMacTextFieldBorder(c)) {
/* 31 */       Insets borderInsets = this.layoutAndBorder.getRealBorderInsets();
/*    */       
/* 33 */       borderInsets.left -= MAC_MARGIN.left;
/* 34 */       int height = c.getHeight() - borderInsets.bottom - borderInsets.top + MAC_MARGIN.bottom + MAC_MARGIN.top;
/* 35 */       int width = c.getWidth() - borderInsets.left - borderInsets.right + MAC_MARGIN.right;
/* 36 */       g.clipRect(borderInsets.left, borderInsets.top, width, height);
/*    */     }
/* 38 */     super.paint(g, c);
/*    */   }
/*    */   
/*    */   private boolean hasMacTextFieldBorder(JComponent c) {
/* 42 */     Border border = c.getBorder();
/* 43 */     if (border == this.layoutAndBorder) {
/* 44 */       border = this.layoutAndBorder.getBorderDelegate();
/*    */     }
/* 46 */     return (border != null) && (border.getClass().getName().equals("apple.laf.CUIAquaTextFieldBorder"));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public BuddyTextFieldUI(TextUI delegate)
/*    */   {
/* 56 */     super(delegate);
/*    */   }
/*    */   
/*    */   public void installUI(JComponent c)
/*    */   {
/* 61 */     super.installUI(c);
/* 62 */     this.layoutAndBorder = createBuddyLayoutAndBorder();
/* 63 */     this.layoutAndBorder.install((JTextField)c);
/*    */   }
/*    */   
/*    */   protected BuddyLayoutAndBorder createBuddyLayoutAndBorder() {
/* 67 */     return new BuddyLayoutAndBorder();
/*    */   }
/*    */   
/*    */   public void uninstallUI(JComponent c)
/*    */   {
/* 72 */     this.layoutAndBorder.uninstall();
/* 73 */     super.uninstallUI(c);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Dimension getPreferredSize(JComponent c)
/*    */   {
/* 83 */     Dimension d = new Dimension();
/* 84 */     Dimension cd = super.getPreferredSize(c);
/* 85 */     Dimension ld = c.getLayout().preferredLayoutSize(c);
/*    */     
/* 87 */     d.height = Math.max(cd.height, ld.height);
/* 88 */     d.width = Math.max(cd.width, ld.width);
/*    */     
/* 90 */     return d;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\BuddyTextFieldUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */