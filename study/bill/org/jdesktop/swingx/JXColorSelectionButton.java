/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.geom.Ellipse2D.Float;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JColorChooser;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.colorchooser.ColorSelectionModel;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import org.jdesktop.swingx.color.ColorUtil;
/*     */ import org.jdesktop.swingx.color.EyeDropperColorChooserPanel;
/*     */ import org.jdesktop.swingx.plaf.UIManagerExt;
/*     */ import org.jdesktop.swingx.util.OS;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JXColorSelectionButton
/*     */   extends JButton
/*     */ {
/*     */   private BufferedImage colorwell;
/*  65 */   private JDialog dialog = null;
/*  66 */   private JColorChooser chooser = null;
/*     */   
/*     */ 
/*     */ 
/*     */   public JXColorSelectionButton()
/*     */   {
/*  72 */     this(Color.red);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXColorSelectionButton(Color col)
/*     */   {
/*  80 */     setBackground(col);
/*  81 */     addActionListener(new ActionHandler(null));
/*  82 */     setContentAreaFilled(false);
/*  83 */     setOpaque(false);
/*     */     try
/*     */     {
/*  86 */       this.colorwell = ImageIO.read(getClass().getResourceAsStream("color/colorwell.png"));
/*     */     } catch (Exception ex) {
/*  88 */       ex.printStackTrace();
/*     */     }
/*     */     
/*  91 */     addPropertyChangeListener("background", new PropertyChangeListener() {
/*     */       public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
/*  93 */         JXColorSelectionButton.this.getChooser().setColor(JXColorSelectionButton.this.getBackground());
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */   private class ColorChangeListener
/*     */     implements ChangeListener
/*     */   {
/*     */     public JXColorSelectionButton button;
/*     */     
/*     */     public ColorChangeListener(JXColorSelectionButton button)
/*     */     {
/* 106 */       this.button = button;
/*     */     }
/*     */     
/* 109 */     public void stateChanged(ChangeEvent changeEvent) { this.button.setBackground(this.button.getChooser().getColor()); }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void paintComponent(Graphics g)
/*     */   {
/* 119 */     Color FILL_COLOR = isEnabled() ? ColorUtil.removeAlpha(getBackground()) : UIManagerExt.getSafeColor("Button.disabledForeground", Color.LIGHT_GRAY);
/*     */     
/*     */ 
/*     */ 
/* 123 */     if ((OS.isMacOSX()) && (this.colorwell != null)) {
/* 124 */       Insets ins = new Insets(5, 5, 5, 5);
/* 125 */       ColorUtil.tileStretchPaint(g, this, this.colorwell, ins);
/*     */       
/*     */ 
/* 128 */       g.setColor(FILL_COLOR);
/* 129 */       g.fillRect(ins.left, ins.top, getWidth() - ins.left - ins.right, getHeight() - ins.top - ins.bottom);
/*     */       
/*     */ 
/*     */ 
/* 133 */       g.setColor(ColorUtil.setBrightness(FILL_COLOR, 0.85F));
/* 134 */       g.drawRect(ins.left, ins.top, getWidth() - ins.left - ins.right - 1, getHeight() - ins.top - ins.bottom - 1);
/*     */       
/*     */ 
/* 137 */       g.drawRect(ins.left + 1, ins.top + 1, getWidth() - ins.left - ins.right - 3, getHeight() - ins.top - ins.bottom - 3);
/*     */     }
/*     */     else
/*     */     {
/* 141 */       Graphics2D g2 = (Graphics2D)g.create();
/*     */       try
/*     */       {
/* 144 */         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/* 145 */         g2.setColor(Color.LIGHT_GRAY);
/* 146 */         int DIAM = Math.min(getWidth(), getHeight());
/* 147 */         int inset = 3;
/* 148 */         g2.fill(new Ellipse2D.Float(3.0F, 3.0F, DIAM - 6, DIAM - 6));
/* 149 */         g2.setColor(FILL_COLOR);
/* 150 */         int border = 1;
/* 151 */         g2.fill(new Ellipse2D.Float(4.0F, 4.0F, DIAM - 6 - 2, DIAM - 6 - 2));
/*     */       } finally {
/* 153 */         g2.dispose();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void showDialog()
/*     */   {
/* 181 */     final Color oldColor = getBackground();
/*     */     
/* 183 */     if (this.dialog == null) {
/* 184 */       this.dialog = JColorChooser.createDialog(this, "Choose a color", true, getChooser(), new ActionListener()
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 190 */         new ActionListener
/*     */         {
/*     */           public void actionPerformed(ActionEvent actionEvent)
/*     */           {
/* 188 */             Color color = JXColorSelectionButton.this.getChooser().getColor();
/* 189 */             if (color != null)
/* 190 */               JXColorSelectionButton.this.setBackground(color); } }, new ActionListener()
/*     */         {
/*     */ 
/*     */           public void actionPerformed(ActionEvent actionEvent)
/*     */           {
/*     */ 
/* 196 */             JXColorSelectionButton.this.setBackground(oldColor);
/*     */           }
/* 198 */         });
/* 199 */       this.dialog.getContentPane().add(getChooser());
/* 200 */       getChooser().getSelectionModel().addChangeListener(new ColorChangeListener(this));
/*     */     }
/*     */     
/*     */ 
/* 204 */     this.dialog.setVisible(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JColorChooser getChooser()
/*     */   {
/* 215 */     if (this.chooser == null) {
/* 216 */       this.chooser = new JColorChooser();
/*     */       
/* 218 */       this.chooser.addChooserPanel(new EyeDropperColorChooserPanel());
/*     */     }
/* 220 */     return this.chooser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setChooser(JColorChooser chooser)
/*     */   {
/* 231 */     JColorChooser oldChooser = getChooser();
/* 232 */     this.chooser = chooser;
/* 233 */     firePropertyChange("chooser", oldChooser, chooser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Dimension getPreferredSize()
/*     */   {
/* 241 */     if ((isPreferredSizeSet()) || (this.colorwell == null)) {
/* 242 */       return super.getPreferredSize();
/*     */     }
/*     */     
/* 245 */     return new Dimension(this.colorwell.getWidth(), this.colorwell.getHeight());
/*     */   }
/*     */   
/*     */   private class ActionHandler
/*     */     implements ActionListener
/*     */   {
/*     */     private ActionHandler() {}
/*     */     
/*     */     public void actionPerformed(ActionEvent actionEvent)
/*     */     {
/* 255 */       JXColorSelectionButton.this.showDialog();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXColorSelectionButton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */