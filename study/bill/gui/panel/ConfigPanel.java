/*    */ package gui.panel;
/*    */ 
/*    */ import gui.listener.ConfigListener;
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.GridLayout;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JTextField;
/*    */ import service.ConfigService;
/*    */ import util.ColorUtil;
/*    */ import util.GUIUtil;
/*    */ 
/*    */ 
/*    */ public class ConfigPanel
/*    */   extends WorkingPanel
/*    */ {
/*    */   static {}
/*    */   
/* 21 */   public static ConfigPanel instance = new ConfigPanel();
/*    */   
/* 23 */   JLabel lBudget = new JLabel("本月预算(￥)");
/* 24 */   public JTextField tfBudget = new JTextField("0");
/*    */   
/* 26 */   JLabel lMysql = new JLabel("Mysql安装目录");
/* 27 */   public JTextField tfMysqlPath = new JTextField("");
/*    */   
/* 29 */   JButton bSubmit = new JButton("更新");
/*    */   
/*    */   public ConfigPanel() {
/* 32 */     GUIUtil.setColor(ColorUtil.grayColor, new JComponent[] { this.lBudget, this.lMysql });
/* 33 */     GUIUtil.setColor(ColorUtil.blueColor, new JComponent[] { this.bSubmit });
/*    */     
/* 35 */     JPanel pInput = new JPanel();
/* 36 */     JPanel pSubmit = new JPanel();
/* 37 */     int gap = 40;
/* 38 */     pInput.setLayout(new GridLayout(4, 1, gap, gap));
/*    */     
/* 40 */     pInput.add(this.lBudget);
/* 41 */     pInput.add(this.tfBudget);
/* 42 */     pInput.add(this.lMysql);
/* 43 */     pInput.add(this.tfMysqlPath);
/*    */     
/* 45 */     pSubmit.add(this.bSubmit);
/*    */     
/* 47 */     setLayout(new BorderLayout());
/* 48 */     add(pInput, "North");
/* 49 */     add(pSubmit, "Center");
/*    */     
/* 51 */     addListener();
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 55 */     GUIUtil.showPanel(instance);
/*    */   }
/*    */   
/*    */   public void addListener() {
/* 59 */     ConfigListener l = new ConfigListener();
/* 60 */     this.bSubmit.addActionListener(l);
/*    */   }
/*    */   
/*    */   public void updateData()
/*    */   {
/* 65 */     String budget = new ConfigService().get("budget");
/* 66 */     String mysqlPath = new ConfigService().get("mysqlPath");
/* 67 */     this.tfBudget.setText(budget);
/* 68 */     this.tfMysqlPath.setText(mysqlPath);
/* 69 */     this.tfBudget.grabFocus();
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\gui\panel\ConfigPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */