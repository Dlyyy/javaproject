/*     */ package org.jdesktop.swingx.color;
/*     */ 
/*     */ import java.awt.AWTException;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Robot;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JColorChooser;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.colorchooser.AbstractColorChooserPanel;
/*     */ import javax.swing.colorchooser.ColorSelectionModel;
/*     */ import javax.swing.event.MouseInputAdapter;
/*     */ import org.jdesktop.swingx.JXColorSelectionButton;
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
/*     */ public class EyeDropperColorChooserPanel
/*     */   extends AbstractColorChooserPanel
/*     */ {
/*     */   private JButton activeColor;
/*     */   private JButton eyeDropper;
/*     */   private JTextField hexColor;
/*     */   private JLabel jLabel1;
/*     */   private JPanel magPanel;
/*     */   private JTextField rgbColor;
/*     */   
/*     */   public static void main(String... args)
/*     */   {
/*  77 */     SwingUtilities.invokeLater(new Runnable() {
/*     */       public void run() {
/*  79 */         JColorChooser chooser = new JColorChooser();
/*  80 */         chooser.addChooserPanel(new EyeDropperColorChooserPanel());
/*  81 */         JFrame frame = new JFrame();
/*  82 */         frame.setDefaultCloseOperation(3);
/*  83 */         frame.add(chooser);
/*  84 */         frame.pack();
/*  85 */         frame.setVisible(true);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public EyeDropperColorChooserPanel()
/*     */   {
/*  94 */     initComponents();
/*  95 */     MouseInputAdapter mia = new MouseInputAdapter()
/*     */     {
/*     */       public void mousePressed(MouseEvent evt) {}
/*     */       
/*     */       public void mouseDragged(MouseEvent evt)
/*     */       {
/* 101 */         Point pt = evt.getPoint();
/* 102 */         SwingUtilities.convertPointToScreen(pt, evt.getComponent());
/* 103 */         ((EyeDropperColorChooserPanel.MagnifyingPanel)EyeDropperColorChooserPanel.this.magPanel).setMagPoint(pt);
/*     */       }
/*     */       
/*     */       public void mouseReleased(MouseEvent evt) {
/* 107 */         Color newColor = new Color(((EyeDropperColorChooserPanel.MagnifyingPanel)EyeDropperColorChooserPanel.access$000(EyeDropperColorChooserPanel.this)).activeColor);
/* 108 */         EyeDropperColorChooserPanel.this.getColorSelectionModel().setSelectedColor(newColor);
/*     */       }
/* 110 */     };
/* 111 */     this.eyeDropper.addMouseListener(mia);
/* 112 */     this.eyeDropper.addMouseMotionListener(mia);
/*     */     try {
/* 114 */       this.eyeDropper.setIcon(new ImageIcon(EyeDropperColorChooserPanel.class.getResource("mag.png")));
/*     */       
/* 116 */       this.eyeDropper.setText("");
/*     */     } catch (Exception ex) {
/* 118 */       ex.printStackTrace();
/*     */     }
/*     */     
/* 121 */     this.magPanel.addPropertyChangeListener(new PropertyChangeListener() {
/*     */       public void propertyChange(PropertyChangeEvent evt) {
/* 123 */         Color color = new Color(((EyeDropperColorChooserPanel.MagnifyingPanel)EyeDropperColorChooserPanel.access$000(EyeDropperColorChooserPanel.this)).activeColor);
/* 124 */         EyeDropperColorChooserPanel.this.activeColor.setBackground(color);
/* 125 */         EyeDropperColorChooserPanel.this.hexColor.setText(ColorUtil.toHexString(color).substring(1));
/* 126 */         EyeDropperColorChooserPanel.this.rgbColor.setText(color.getRed() + "," + color.getGreen() + "," + color.getBlue());
/*     */       }
/*     */     }); }
/*     */   
/*     */   private class MagnifyingPanel extends JPanel { private Point2D point;
/*     */     private int activeColor;
/*     */     
/*     */     private MagnifyingPanel() {}
/*     */     
/* 135 */     public void setMagPoint(Point2D point) { this.point = point;
/* 136 */       repaint();
/*     */     }
/*     */     
/*     */     public void paintComponent(Graphics g) {
/* 140 */       if (this.point != null) {
/* 141 */         Rectangle rect = new Rectangle((int)this.point.getX() - 10, (int)this.point.getY() - 10, 20, 20);
/*     */         try {
/* 143 */           BufferedImage img = new Robot().createScreenCapture(rect);
/* 144 */           g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
/* 145 */           int oldColor = this.activeColor;
/* 146 */           this.activeColor = img.getRGB(img.getWidth() / 2, img.getHeight() / 2);
/* 147 */           firePropertyChange("activeColor", oldColor, this.activeColor);
/*     */         } catch (AWTException ex) {
/* 149 */           ex.printStackTrace();
/*     */         }
/*     */       }
/* 152 */       g.setColor(Color.black);
/* 153 */       g.drawRect(getWidth() / 2 - 5, getHeight() / 2 - 5, 10, 10);
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
/*     */   private void initComponents()
/*     */   {
/* 167 */     this.eyeDropper = new JButton();
/* 168 */     this.magPanel = new MagnifyingPanel(null);
/* 169 */     this.activeColor = new JXColorSelectionButton();
/* 170 */     this.hexColor = new JTextField();
/* 171 */     JTextArea jTextArea1 = new JTextArea();
/* 172 */     this.jLabel1 = new JLabel();
/* 173 */     this.rgbColor = new JTextField();
/* 174 */     JLabel jLabel2 = new JLabel();
/*     */     
/* 176 */     setLayout(new GridBagLayout());
/*     */     
/* 178 */     this.eyeDropper.setText("eye");
/* 179 */     add(this.eyeDropper, new GridBagConstraints());
/*     */     
/* 181 */     this.magPanel.setLayout(new BorderLayout());
/*     */     
/* 183 */     this.magPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
/* 184 */     this.magPanel.setMinimumSize(new Dimension(100, 100));
/* 185 */     this.magPanel.setPreferredSize(new Dimension(100, 100));
/* 186 */     GridBagConstraints gridBagConstraints = new GridBagConstraints();
/* 187 */     gridBagConstraints.gridx = 0;
/* 188 */     gridBagConstraints.gridy = 1;
/* 189 */     gridBagConstraints.gridheight = 3;
/* 190 */     gridBagConstraints.fill = 1;
/* 191 */     gridBagConstraints.insets = new Insets(0, 0, 0, 12);
/* 192 */     add(this.magPanel, gridBagConstraints);
/*     */     
/* 194 */     this.activeColor.setEnabled(false);
/* 195 */     this.activeColor.setPreferredSize(new Dimension(40, 40));
/* 196 */     gridBagConstraints = new GridBagConstraints();
/* 197 */     gridBagConstraints.gridx = 2;
/* 198 */     gridBagConstraints.gridy = 3;
/* 199 */     gridBagConstraints.fill = 1;
/* 200 */     gridBagConstraints.insets = new Insets(2, 0, 2, 0);
/* 201 */     add(this.activeColor, gridBagConstraints);
/*     */     
/* 203 */     this.hexColor.setEditable(false);
/* 204 */     gridBagConstraints = new GridBagConstraints();
/* 205 */     gridBagConstraints.gridx = 2;
/* 206 */     gridBagConstraints.gridy = 1;
/* 207 */     gridBagConstraints.fill = 2;
/* 208 */     gridBagConstraints.insets = new Insets(2, 0, 2, 0);
/* 209 */     add(this.hexColor, gridBagConstraints);
/*     */     
/* 211 */     jTextArea1.setColumns(20);
/* 212 */     jTextArea1.setEditable(false);
/* 213 */     jTextArea1.setLineWrap(true);
/* 214 */     jTextArea1.setRows(5);
/* 215 */     jTextArea1.setText("Drag the magnifying glass to select a color from the screen.");
/* 216 */     jTextArea1.setWrapStyleWord(true);
/* 217 */     jTextArea1.setOpaque(false);
/* 218 */     gridBagConstraints = new GridBagConstraints();
/* 219 */     gridBagConstraints.gridwidth = 2;
/* 220 */     gridBagConstraints.fill = 2;
/* 221 */     gridBagConstraints.anchor = 11;
/* 222 */     gridBagConstraints.weightx = 10.0D;
/* 223 */     gridBagConstraints.weighty = 10.0D;
/* 224 */     gridBagConstraints.insets = new Insets(0, 0, 7, 0);
/* 225 */     add(jTextArea1, gridBagConstraints);
/*     */     
/* 227 */     this.jLabel1.setText("#");
/* 228 */     gridBagConstraints = new GridBagConstraints();
/* 229 */     gridBagConstraints.gridx = 1;
/* 230 */     gridBagConstraints.gridy = 1;
/* 231 */     gridBagConstraints.anchor = 13;
/* 232 */     gridBagConstraints.insets = new Insets(0, 4, 0, 4);
/* 233 */     add(this.jLabel1, gridBagConstraints);
/*     */     
/* 235 */     this.rgbColor.setEditable(false);
/* 236 */     this.rgbColor.setText("255,255,255");
/* 237 */     gridBagConstraints = new GridBagConstraints();
/* 238 */     gridBagConstraints.gridx = 2;
/* 239 */     gridBagConstraints.gridy = 2;
/* 240 */     gridBagConstraints.fill = 2;
/* 241 */     gridBagConstraints.insets = new Insets(2, 0, 2, 0);
/* 242 */     add(this.rgbColor, gridBagConstraints);
/*     */     
/* 244 */     jLabel2.setText("RGB");
/* 245 */     gridBagConstraints = new GridBagConstraints();
/* 246 */     gridBagConstraints.gridx = 1;
/* 247 */     gridBagConstraints.gridy = 2;
/* 248 */     gridBagConstraints.anchor = 13;
/* 249 */     gridBagConstraints.insets = new Insets(0, 4, 0, 4);
/* 250 */     add(jLabel2, gridBagConstraints);
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
/*     */   public void updateChooser() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void buildChooser() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDisplayName()
/*     */   {
/* 283 */     return "Grab from Screen";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Icon getSmallDisplayIcon()
/*     */   {
/* 291 */     return new ImageIcon();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Icon getLargeDisplayIcon()
/*     */   {
/* 299 */     return new ImageIcon();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\color\EyeDropperColorChooserPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */