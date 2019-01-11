/*    */ package gui.listener;
/*    */ 
/*    */ import gui.panel.BackupPanel;
/*    */ import gui.panel.ConfigPanel;
/*    */ import gui.panel.MainPanel;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import java.io.File;
/*    */ import java.io.PrintStream;
/*    */ import javax.swing.JFileChooser;
/*    */ import javax.swing.JOptionPane;
/*    */ import javax.swing.JTextField;
/*    */ import javax.swing.filechooser.FileFilter;
/*    */ import service.ConfigService;
/*    */ import util.CenterPanel;
/*    */ import util.MysqlUtil;
/*    */ 
/*    */ public class BackupListener implements ActionListener
/*    */ {
/*    */   public void actionPerformed(ActionEvent e)
/*    */   {
/* 22 */     BackupPanel p = BackupPanel.instance;
/* 23 */     String mysqlPath = new ConfigService().get("mysqlPath");
/* 24 */     if (mysqlPath.length() == 0) {
/* 25 */       JOptionPane.showMessageDialog(p, "备份前请事先配置mysql的路径");
/* 26 */       MainPanel.instance.workingPanel.show(ConfigPanel.instance);
/* 27 */       ConfigPanel.instance.tfMysqlPath.grabFocus();
/* 28 */       return;
/*    */     }
/* 30 */     JFileChooser fc = new JFileChooser();
/* 31 */     fc.setSelectedFile(new File("hutubill.sql"));
/* 32 */     fc.setFileFilter(new FileFilter()
/*    */     {
/*    */       public String getDescription()
/*    */       {
/* 36 */         return ".sql";
/*    */       }
/*    */       
/*    */       public boolean accept(File f)
/*    */       {
/* 41 */         return f.getName().toLowerCase().endsWith(".sql");
/*    */       }
/*    */       
/* 44 */     });
/* 45 */     int returnVal = fc.showSaveDialog(p);
/* 46 */     File file = fc.getSelectedFile();
/* 47 */     System.out.println(file);
/* 48 */     if (returnVal == 0)
/*    */     {
/* 50 */       System.out.println(file);
/* 51 */       if (!file.getName().toLowerCase().endsWith(".sql"))
/* 52 */         file = new File(file.getParent(), file.getName() + ".sql");
/* 53 */       System.out.println(file);
/*    */       try
/*    */       {
/* 56 */         MysqlUtil.backup(mysqlPath, file.getAbsolutePath());
/* 57 */         JOptionPane.showMessageDialog(p, "备份成功,备份文件位于:\r\n" + file.getAbsolutePath());
/*    */       } catch (Exception e1) {
/* 59 */         e1.printStackTrace();
/* 60 */         JOptionPane.showMessageDialog(p, "备份失败\r\n,错误:\r\n" + e1.getMessage());
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\gui\listener\BackupListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */