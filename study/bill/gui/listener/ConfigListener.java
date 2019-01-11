/*    */ package gui.listener;
/*    */ 
/*    */ import gui.panel.ConfigPanel;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import java.io.File;
/*    */ import javax.swing.JOptionPane;
/*    */ import javax.swing.JTextField;
/*    */ import service.ConfigService;
/*    */ import util.GUIUtil;
/*    */ 
/*    */ 
/*    */ public class ConfigListener
/*    */   implements ActionListener
/*    */ {
/*    */   public void actionPerformed(ActionEvent e)
/*    */   {
/* 18 */     ConfigPanel p = ConfigPanel.instance;
/* 19 */     if (!GUIUtil.checkNumber(p.tfBudget, "本月预算"))
/* 20 */       return;
/* 21 */     String mysqlPath = p.tfMysqlPath.getText();
/* 22 */     if (mysqlPath.length() != 0) {
/* 23 */       File commandFile = new File(mysqlPath, "bin/mysql.exe");
/* 24 */       if (!commandFile.exists()) {
/* 25 */         JOptionPane.showMessageDialog(p, "Mysql路径不正确");
/* 26 */         p.tfMysqlPath.grabFocus();
/* 27 */         return;
/*    */       }
/*    */     }
/*    */     
/* 31 */     ConfigService cs = new ConfigService();
/* 32 */     cs.update("budget", p.tfBudget.getText());
/* 33 */     cs.update("mysqlPath", mysqlPath);
/*    */     
/* 35 */     JOptionPane.showMessageDialog(p, "设置修改成功");
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\gui\listener\ConfigListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */