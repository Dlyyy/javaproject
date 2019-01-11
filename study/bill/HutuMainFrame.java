/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JToolBar;
/*    */ 
/*    */ class HutuMainFrame
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 13 */     JFrame f = new JFrame();
/* 14 */     f.setSize(500, 450);
/* 15 */     f.setTitle("一本糊涂账");
/* 16 */     f.setLocationRelativeTo(null);
/* 17 */     f.setResizable(false);
/* 18 */     f.setDefaultCloseOperation(3);
/*    */     
/* 20 */     JToolBar tb = new JToolBar();
/* 21 */     JButton bSpend = new JButton("消费一览");
/* 22 */     JButton bRecord = new JButton("记一笔");
/* 23 */     JButton bCategory = new JButton("消费分类");
/* 24 */     JButton bReport = new JButton("月消费报表");
/* 25 */     JButton bConfig = new JButton("设置");
/* 26 */     JButton bBackup = new JButton("备份");
/* 27 */     JButton bRecover = new JButton("恢复");
/*    */     
/* 29 */     tb.add(bSpend);
/* 30 */     tb.add(bRecord);
/* 31 */     tb.add(bCategory);
/* 32 */     tb.add(bReport);
/* 33 */     tb.add(bConfig);
/* 34 */     tb.add(bBackup);
/* 35 */     tb.add(bRecover);
/* 36 */     f.setLayout(new BorderLayout());
/* 37 */     f.add(tb, "North");
/* 38 */     f.add(new JPanel(), "Center");
/*    */     
/* 40 */     f.setVisible(true);
/*    */     
/* 42 */     bSpend.addActionListener(new ActionListener()
/*    */     {
/*    */       public void actionPerformed(ActionEvent e) {}
/*    */ 
/* 46 */     });
/* 47 */     bRecord.addActionListener(new ActionListener()
/*    */     {
/*    */       public void actionPerformed(ActionEvent e) {}
/*    */ 
/* 51 */     });
/* 52 */     bCategory.addActionListener(new ActionListener()
/*    */     {
/*    */       public void actionPerformed(ActionEvent e) {}
/*    */ 
/* 56 */     });
/* 57 */     bConfig.addActionListener(new ActionListener()
/*    */     {
/*    */       public void actionPerformed(ActionEvent e) {}
/*    */ 
/* 61 */     });
/* 62 */     bBackup.addActionListener(new ActionListener()
/*    */     {
/*    */       public void actionPerformed(ActionEvent e) {}
/*    */ 
/* 66 */     });
/* 67 */     bRecover.addActionListener(new ActionListener()
/*    */     {
/*    */       public void actionPerformed(ActionEvent e) {}
/*    */     });
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\HutuMainFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */