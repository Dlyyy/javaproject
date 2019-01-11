/*    */ package gui.panel;
/*    */ 
/*    */ import gui.listener.RecoverListener;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JComponent;
/*    */ import util.ColorUtil;
/*    */ import util.GUIUtil;
/*    */ 
/*    */ 
/*    */ public class RecoverPanel
/*    */   extends WorkingPanel
/*    */ {
/* 13 */   public static RecoverPanel instance = new RecoverPanel();
/*    */   
/* 15 */   JButton bRecover = new JButton("恢复");
/*    */   
/*    */   static {}
/*    */   
/* 19 */   public RecoverPanel() { GUIUtil.setColor(ColorUtil.blueColor, new JComponent[] { this.bRecover });
/* 20 */     add(this.bRecover);
/*    */     
/* 22 */     addListener();
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 26 */     GUIUtil.showPanel(instance);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void updateData() {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void addListener()
/*    */   {
/* 37 */     RecoverListener listener = new RecoverListener();
/* 38 */     this.bRecover.addActionListener(listener);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\gui\panel\RecoverPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */