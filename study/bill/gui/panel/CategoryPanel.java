/*    */ package gui.panel;
/*    */ 
/*    */ import entity.Category;
/*    */ import gui.listener.CategoryListener;
/*    */ import gui.model.CategoryTableModel;
/*    */ import java.awt.BorderLayout;
/*    */ import java.util.List;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JScrollPane;
/*    */ import javax.swing.JTable;
/*    */ import service.CategoryService;
/*    */ import util.ColorUtil;
/*    */ import util.GUIUtil;
/*    */ 
/*    */ public class CategoryPanel extends WorkingPanel
/*    */ {
/*    */   static {}
/*    */   
/* 21 */   public static CategoryPanel instance = new CategoryPanel();
/*    */   
/* 23 */   public JButton bAdd = new JButton("新增");
/* 24 */   public JButton bEdit = new JButton("编辑");
/* 25 */   public JButton bDelete = new JButton("删除");
/* 26 */   String[] columNames = { "分类名称", "消费次数" };
/*    */   
/* 28 */   public CategoryTableModel ctm = new CategoryTableModel();
/* 29 */   public JTable t = new JTable(this.ctm);
/*    */   
/*    */   public CategoryPanel() {
/* 32 */     GUIUtil.setColor(ColorUtil.blueColor, new JComponent[] { this.bAdd, this.bEdit, this.bDelete });
/* 33 */     JScrollPane sp = new JScrollPane(this.t);
/* 34 */     JPanel pSubmit = new JPanel();
/* 35 */     pSubmit.add(this.bAdd);
/* 36 */     pSubmit.add(this.bEdit);
/* 37 */     pSubmit.add(this.bDelete);
/*    */     
/* 39 */     setLayout(new BorderLayout());
/* 40 */     add(sp, "Center");
/* 41 */     add(pSubmit, "South");
/*    */     
/* 43 */     addListener();
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 47 */     GUIUtil.showPanel(instance);
/*    */   }
/*    */   
/*    */   public Category getSelectedCategory() {
/* 51 */     int index = this.t.getSelectedRow();
/* 52 */     return (Category)this.ctm.cs.get(index);
/*    */   }
/*    */   
/*    */   public void updateData() {
/* 56 */     this.ctm.cs = new CategoryService().list();
/* 57 */     this.t.updateUI();
/* 58 */     this.t.getSelectionModel().setSelectionInterval(0, 0);
/*    */     
/* 60 */     if (this.ctm.cs.size() == 0) {
/* 61 */       this.bEdit.setEnabled(false);
/* 62 */       this.bDelete.setEnabled(false);
/*    */     }
/*    */     else {
/* 65 */       this.bEdit.setEnabled(true);
/* 66 */       this.bDelete.setEnabled(true);
/*    */     }
/*    */   }
/*    */   
/*    */   public void addListener() {
/* 71 */     CategoryListener listener = new CategoryListener();
/* 72 */     this.bAdd.addActionListener(listener);
/* 73 */     this.bEdit.addActionListener(listener);
/* 74 */     this.bDelete.addActionListener(listener);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\gui\panel\CategoryPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */