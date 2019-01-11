/*    */ package gui.listener;
/*    */ 
/*    */ import entity.Category;
/*    */ import gui.panel.CategoryPanel;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JOptionPane;
/*    */ import service.CategoryService;
/*    */ 
/*    */ 
/*    */ public class CategoryListener
/*    */   implements ActionListener
/*    */ {
/*    */   public void actionPerformed(ActionEvent e)
/*    */   {
/* 17 */     CategoryPanel p = CategoryPanel.instance;
/*    */     
/* 19 */     JButton b = (JButton)e.getSource();
/* 20 */     if (b == p.bAdd) {
/* 21 */       String name = JOptionPane.showInputDialog(null);
/* 22 */       if (name.length() == 0) {
/* 23 */         JOptionPane.showMessageDialog(p, "分类名称不能为空");
/* 24 */         return;
/*    */       }
/*    */       
/* 27 */       new CategoryService().add(name);
/*    */     }
/*    */     
/* 30 */     if (b == p.bEdit) {
/* 31 */       Category c = p.getSelectedCategory();
/* 32 */       int id = c.id;
/* 33 */       String name = JOptionPane.showInputDialog("修改分类名称", c.name);
/* 34 */       if (name.length() == 0) {
/* 35 */         JOptionPane.showMessageDialog(p, "分类名称不能为空");
/* 36 */         return;
/*    */       }
/*    */       
/* 39 */       new CategoryService().update(id, name);
/*    */     }
/* 41 */     if (b == p.bDelete) {
/* 42 */       Category c = p.getSelectedCategory();
/* 43 */       if (c.recordNumber != 0) {
/* 44 */         JOptionPane.showMessageDialog(p, "本分类下有消费记录存在，不能删除");
/* 45 */         return;
/*    */       }
/* 47 */       if (JOptionPane.showConfirmDialog(p, "确认要删除？") != 0) {
/* 48 */         return;
/*    */       }
/* 50 */       int id = c.id;
/* 51 */       new CategoryService().delete(id);
/*    */     }
/* 53 */     p.updateData();
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\gui\listener\CategoryListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */