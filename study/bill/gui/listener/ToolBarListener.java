/*    */ package gui.listener;
/*    */ 
/*    */ import gui.panel.BackupPanel;
/*    */ import gui.panel.CategoryPanel;
/*    */ import gui.panel.ConfigPanel;
/*    */ import gui.panel.MainPanel;
/*    */ import gui.panel.RecordPanel;
/*    */ import gui.panel.RecoverPanel;
/*    */ import gui.panel.ReportPanel;
/*    */ import gui.panel.SpendPanel;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.JButton;
/*    */ import util.CenterPanel;
/*    */ 
/*    */ public class ToolBarListener implements ActionListener
/*    */ {
/*    */   public void actionPerformed(ActionEvent e)
/*    */   {
/* 20 */     MainPanel p = MainPanel.instance;
/* 21 */     JButton b = (JButton)e.getSource();
/* 22 */     if (b == p.bReport)
/* 23 */       p.workingPanel.show(ReportPanel.instance);
/* 24 */     if (b == p.bCategory)
/* 25 */       p.workingPanel.show(CategoryPanel.instance);
/* 26 */     if (b == p.bSpend)
/* 27 */       p.workingPanel.show(SpendPanel.instance);
/* 28 */     if (b == p.bRecord)
/* 29 */       p.workingPanel.show(RecordPanel.instance);
/* 30 */     if (b == p.bConfig)
/* 31 */       p.workingPanel.show(ConfigPanel.instance);
/* 32 */     if (b == p.bBackup)
/* 33 */       p.workingPanel.show(BackupPanel.instance);
/* 34 */     if (b == p.bRecover) {
/* 35 */       p.workingPanel.show(RecoverPanel.instance);
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\gui\listener\ToolBarListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */