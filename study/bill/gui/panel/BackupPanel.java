/*    */ package gui.panel;
/*    */ 
/*    */ import gui.listener.BackupListener;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JComponent;
/*    */ import util.ColorUtil;
/*    */ import util.GUIUtil;
/*    */ 
/*    */ public class BackupPanel
/*    */   extends WorkingPanel
/*    */ {
/*    */   static {}
/*    */   
/* 14 */   public static BackupPanel instance = new BackupPanel();
/* 15 */   JButton bBackup = new JButton("备份");
/*    */   
/*    */   public BackupPanel() {
/* 18 */     GUIUtil.setColor(ColorUtil.blueColor, new JComponent[] { this.bBackup });
/* 19 */     add(this.bBackup);
/* 20 */     addListener();
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 24 */     GUIUtil.showPanel(instance);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void updateData() {}
/*    */   
/*    */ 
/*    */   public void addListener()
/*    */   {
/* 34 */     BackupListener listener = new BackupListener();
/* 35 */     this.bBackup.addActionListener(listener);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\gui\panel\BackupPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */