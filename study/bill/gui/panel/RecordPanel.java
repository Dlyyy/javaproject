/*    */ package gui.panel;
/*    */ 
/*    */ import entity.Category;
/*    */ import gui.listener.RecordListener;
/*    */ import gui.model.CategoryComboBoxModel;
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.GridLayout;
/*    */ import java.util.Date;
/*    */ import java.util.List;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JComboBox;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JTextField;
/*    */ import org.jdesktop.swingx.JXDatePicker;
/*    */ import service.CategoryService;
/*    */ import util.ColorUtil;
/*    */ import util.GUIUtil;
/*    */ 
/*    */ public class RecordPanel
/*    */   extends WorkingPanel
/*    */ {
/*    */   static {}
/*    */   
/* 26 */   public static RecordPanel instance = new RecordPanel();
/*    */   
/* 28 */   JLabel lSpend = new JLabel("花费(￥)");
/* 29 */   JLabel lCategory = new JLabel("分类");
/* 30 */   JLabel lComment = new JLabel("备注");
/* 31 */   JLabel lDate = new JLabel("日期");
/*    */   
/* 33 */   public JTextField tfSpend = new JTextField("0");
/*    */   
/* 35 */   public CategoryComboBoxModel cbModel = new CategoryComboBoxModel();
/* 36 */   public JComboBox<Category> cbCategory = new JComboBox(this.cbModel);
/* 37 */   public JTextField tfComment = new JTextField();
/* 38 */   public JXDatePicker datepick = new JXDatePicker(new Date());
/*    */   
/* 40 */   JButton bSubmit = new JButton("记一笔");
/*    */   
/*    */   public RecordPanel() {
/* 43 */     GUIUtil.setColor(ColorUtil.grayColor, new JComponent[] { this.lSpend, this.lCategory, this.lComment, this.lDate });
/* 44 */     GUIUtil.setColor(ColorUtil.blueColor, new JComponent[] { this.bSubmit });
/* 45 */     JPanel pInput = new JPanel();
/* 46 */     JPanel pSubmit = new JPanel();
/* 47 */     int gap = 40;
/* 48 */     pInput.setLayout(new GridLayout(4, 2, gap, gap));
/*    */     
/* 50 */     pInput.add(this.lSpend);
/* 51 */     pInput.add(this.tfSpend);
/* 52 */     pInput.add(this.lCategory);
/* 53 */     pInput.add(this.cbCategory);
/* 54 */     pInput.add(this.lComment);
/* 55 */     pInput.add(this.tfComment);
/* 56 */     pInput.add(this.lDate);
/* 57 */     pInput.add(this.datepick);
/*    */     
/* 59 */     pSubmit.add(this.bSubmit);
/*    */     
/* 61 */     setLayout(new BorderLayout());
/* 62 */     add(pInput, "North");
/* 63 */     add(pSubmit, "Center");
/*    */     
/* 65 */     addListener();
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 69 */     GUIUtil.showPanel(instance);
/*    */   }
/*    */   
/*    */   public Category getSelectedCategory() {
/* 73 */     return (Category)this.cbCategory.getSelectedItem();
/*    */   }
/*    */   
/*    */   public void updateData()
/*    */   {
/* 78 */     this.cbModel.cs = new CategoryService().list();
/* 79 */     this.cbCategory.updateUI();
/* 80 */     resetInput();
/* 81 */     this.tfSpend.grabFocus();
/*    */   }
/*    */   
/*    */   public void resetInput() {
/* 85 */     this.tfSpend.setText("0");
/* 86 */     this.tfComment.setText("");
/* 87 */     if (this.cbModel.cs.size() != 0)
/* 88 */       this.cbCategory.setSelectedIndex(0);
/* 89 */     this.datepick.setDate(new Date());
/*    */   }
/*    */   
/*    */ 
/*    */   public void addListener()
/*    */   {
/* 95 */     RecordListener listener = new RecordListener();
/* 96 */     this.bSubmit.addActionListener(listener);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\gui\panel\RecordPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */