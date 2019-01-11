/*    */ package gui.listener;
/*    */ 
/*    */ import gui.model.CategoryComboBoxModel;
/*    */ import gui.panel.CategoryPanel;
/*    */ import gui.panel.MainPanel;
/*    */ import gui.panel.RecordPanel;
/*    */ import gui.panel.SpendPanel;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import java.util.Date;
/*    */ import javax.swing.JOptionPane;
/*    */ import javax.swing.JTextField;
/*    */ import service.RecordService;
/*    */ import util.CenterPanel;
/*    */ import util.GUIUtil;
/*    */ 
/*    */ public class RecordListener implements ActionListener
/*    */ {
/*    */   public void actionPerformed(ActionEvent e)
/*    */   {
/* 21 */     RecordPanel p = RecordPanel.instance;
/* 22 */     if (p.cbModel.cs.size() == 0) {
/* 23 */       JOptionPane.showMessageDialog(p, "暂无消费分类，无法添加，请先增加消费分类");
/* 24 */       MainPanel.instance.workingPanel.show(CategoryPanel.instance);
/* 25 */       return;
/*    */     }
/*    */     
/* 28 */     if (!GUIUtil.checkZero(p.tfSpend, "花费金额"))
/* 29 */       return;
/* 30 */     int spend = Integer.parseInt(p.tfSpend.getText());
/* 31 */     entity.Category c = p.getSelectedCategory();
/* 32 */     String comment = p.tfComment.getText();
/* 33 */     Date d = p.datepick.getDate();
/* 34 */     new RecordService().add(spend, c, comment, d);
/* 35 */     JOptionPane.showMessageDialog(p, "添加成功");
/*    */     
/* 37 */     MainPanel.instance.workingPanel.show(SpendPanel.instance);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\gui\listener\RecordListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */