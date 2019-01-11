/*     */ package util;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.File;
/*     */ import java.net.URL;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ public class GUIUtil
/*     */ {
/*  17 */   private static String imageFolder = "e:/project/hutubill/img";
/*     */   
/*     */   public static void setImageIcon(JButton b, String fileName, String tip) {
/*  20 */     File f = new File(imageFolder, fileName);
/*  21 */     ImageIcon i = null;
/*  22 */     if (f.exists()) {
/*  23 */       i = new ImageIcon(f.getAbsolutePath());
/*     */     }
/*     */     else {
/*  26 */       URL u = ClassLoader.getSystemResource("img/" + fileName);
/*  27 */       i = new ImageIcon(u);
/*     */     }
/*     */     
/*     */ 
/*  31 */     b.setIcon(i);
/*  32 */     b.setPreferredSize(new java.awt.Dimension(61, 81));
/*  33 */     b.setToolTipText(tip);
/*  34 */     b.setVerticalTextPosition(3);
/*  35 */     b.setHorizontalTextPosition(0);
/*  36 */     b.setText(tip);
/*     */   }
/*     */   
/*     */   public static void setColor(Color color, JComponent... cs) { JComponent[] arrayOfJComponent;
/*  40 */     int j = (arrayOfJComponent = cs).length; for (int i = 0; i < j; i++) { JComponent c = arrayOfJComponent[i];
/*  41 */       c.setForeground(color);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void showPanel(JPanel p, double strechRate)
/*     */   {
/*  51 */     useLNF();
/*  52 */     JFrame f = new JFrame();
/*  53 */     f.setSize(500, 500);
/*  54 */     f.setLocationRelativeTo(null);
/*  55 */     CenterPanel cp = new CenterPanel(strechRate);
/*  56 */     f.setContentPane(cp);
/*  57 */     f.setDefaultCloseOperation(3);
/*  58 */     f.setVisible(true);
/*  59 */     cp.show(p);
/*     */   }
/*     */   
/*     */   public static void showPanel(JPanel p) {
/*  63 */     showPanel(p, 0.85D);
/*     */   }
/*     */   
/*     */   public static boolean checkNumber(JTextField tf, String input) {
/*  67 */     if (!checkEmpty(tf, input))
/*  68 */       return false;
/*  69 */     String text = tf.getText().trim();
/*     */     try {
/*  71 */       Integer.parseInt(text);
/*  72 */       return true;
/*     */     } catch (NumberFormatException e1) {
/*  74 */       JOptionPane.showMessageDialog(null, input + " 需要是整数");
/*  75 */       tf.grabFocus(); }
/*  76 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean checkZero(JTextField tf, String input)
/*     */   {
/*  81 */     if (!checkNumber(tf, input))
/*  82 */       return false;
/*  83 */     String text = tf.getText().trim();
/*     */     
/*  85 */     if (Integer.parseInt(text) == 0) {
/*  86 */       JOptionPane.showMessageDialog(null, input + " 不能为零");
/*  87 */       tf.grabFocus();
/*  88 */       return false;
/*     */     }
/*  90 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean checkEmpty(JTextField tf, String input) {
/*  94 */     String text = tf.getText().trim();
/*  95 */     if (text.length() == 0) {
/*  96 */       JOptionPane.showMessageDialog(null, input + " 不能为空");
/*  97 */       tf.grabFocus();
/*  98 */       return false;
/*     */     }
/* 100 */     return true;
/*     */   }
/*     */   
/*     */   public static int getInt(JTextField tf)
/*     */   {
/* 105 */     return Integer.parseInt(tf.getText());
/*     */   }
/*     */   
/*     */   public static void useLNF() {
/*     */     try {
/* 110 */       UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
/*     */     }
/*     */     catch (Exception e) {
/* 113 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\util\GUIUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */