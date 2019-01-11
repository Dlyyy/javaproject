/*    */ package org.jdesktop.swingx.renderer;
/*    */ 
/*    */ import java.awt.LayoutManager;
/*    */ import java.awt.Rectangle;
/*    */ import javax.swing.JPanel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JRendererPanel
/*    */   extends JPanel
/*    */ {
/*    */   public JRendererPanel() {}
/*    */   
/*    */   public JRendererPanel(LayoutManager layout)
/*    */   {
/* 22 */     super(layout);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setToolTipText(String text)
/*    */   {
/* 34 */     putClientProperty("ToolTipText", text);
/*    */   }
/*    */   
/*    */   public void revalidate() {}
/*    */   
/*    */   public void repaint(long tm, int x, int y, int width, int height) {}
/*    */   
/*    */   public void repaint(Rectangle r) {}
/*    */   
/*    */   public void repaint() {}
/*    */   
/*    */   protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {}
/*    */   
/*    */   public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\JRendererPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */