/*     */ package org.jdesktop.swingx.autocomplete.workarounds;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.PopupMenuEvent;
/*     */ import javax.swing.event.PopupMenuListener;
/*     */ import javax.swing.plaf.ComboBoxUI;
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
/*     */ 
/*     */ public final class MacOSXPopupLocationFix
/*     */ {
/*     */   private final JComboBox comboBox;
/*     */   private final JPopupMenu popupMenu;
/*  59 */   private final Listener listener = new Listener(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private MacOSXPopupLocationFix(JComboBox comboBox)
/*     */   {
/*  66 */     this.comboBox = comboBox;
/*  67 */     this.popupMenu = ((JPopupMenu)comboBox.getUI().getAccessibleChild(comboBox, 0));
/*     */     
/*  69 */     this.popupMenu.addPopupMenuListener(this.listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static MacOSXPopupLocationFix install(JComboBox comboBox)
/*     */   {
/*  76 */     if (comboBox == null) throw new IllegalArgumentException();
/*  77 */     return new MacOSXPopupLocationFix(comboBox);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void uninstall()
/*     */   {
/*  85 */     this.popupMenu.removePopupMenuListener(this.listener);
/*     */   }
/*     */   
/*     */   private class Listener implements PopupMenuListener
/*     */   {
/*     */     private Listener() {}
/*     */     
/*     */     public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
/*  93 */       JComponent popupComponent = (JComponent)e.getSource();
/*  94 */       MacOSXPopupLocationFix.this.fixPopupLocation(popupComponent);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
/*     */     
/*     */ 
/*     */ 
/*     */     public void popupMenuCanceled(PopupMenuEvent e) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void fixPopupLocation(JComponent popupComponent)
/*     */   {
/* 110 */     if (popupComponent.getClass().getName().indexOf("apple.laf") != 0) {
/* 111 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 116 */     Point comboLocationOnScreen = this.comboBox.getLocationOnScreen();
/* 117 */     int comboHeight = this.comboBox.getHeight();
/* 118 */     int popupY = comboLocationOnScreen.y + comboHeight;
/*     */     
/*     */ 
/*     */ 
/* 122 */     Rectangle screenBounds = new ScreenGeometry(this.comboBox).getScreenBounds();
/* 123 */     int popupHeight = popupComponent.getPreferredSize().height;
/* 124 */     if (comboLocationOnScreen.y + comboHeight + popupHeight > screenBounds.x + screenBounds.height) {
/* 125 */       popupY = comboLocationOnScreen.y - popupHeight;
/*     */     }
/*     */     
/* 128 */     popupComponent.setLocation(comboLocationOnScreen.x, popupY);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class ScreenGeometry
/*     */   {
/*     */     final GraphicsConfiguration graphicsConfiguration;
/*     */     
/*     */ 
/*     */     final boolean aqua;
/*     */     
/*     */ 
/*     */ 
/*     */     public ScreenGeometry(JComponent component)
/*     */     {
/* 145 */       this.aqua = (UIManager.getLookAndFeel().getName().indexOf("Aqua") != -1);
/* 146 */       this.graphicsConfiguration = graphicsConfigurationForComponent(component);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private GraphicsConfiguration graphicsConfigurationForComponent(Component component)
/*     */     {
/* 153 */       Point point = component.getLocationOnScreen();
/*     */       
/*     */ 
/* 156 */       GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
/* 157 */       GraphicsDevice[] gd = ge.getScreenDevices();
/* 158 */       for (int i = 0; i < gd.length; i++) {
/* 159 */         if (gd[i].getType() == 0) {
/* 160 */           GraphicsConfiguration defaultGraphicsConfiguration = gd[i].getDefaultConfiguration();
/* 161 */           if (defaultGraphicsConfiguration.getBounds().contains(point)) {
/* 162 */             return defaultGraphicsConfiguration;
/*     */           }
/*     */         }
/*     */       }
/* 166 */       return component.getGraphicsConfiguration();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Rectangle getScreenBounds()
/*     */     {
/* 173 */       Rectangle screenSize = getScreenSize();
/* 174 */       Insets screenInsets = getScreenInsets();
/*     */       
/* 176 */       return new Rectangle(screenSize.x + screenInsets.left, screenSize.y + screenInsets.top, screenSize.width - screenInsets.left - screenInsets.right, screenSize.height - screenInsets.top - screenInsets.bottom);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Rectangle getScreenSize()
/*     */     {
/* 189 */       if (this.graphicsConfiguration != null) {
/* 190 */         return this.graphicsConfiguration.getBounds();
/*     */       }
/*     */       
/*     */ 
/* 194 */       return new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
/*     */     }
/*     */     
/*     */ 
/*     */     public Insets getScreenInsets()
/*     */     {
/*     */       Insets screenInsets;
/*     */       
/*     */       Insets screenInsets;
/* 203 */       if (this.graphicsConfiguration != null) {
/* 204 */         screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(this.graphicsConfiguration);
/*     */       } else {
/* 206 */         screenInsets = new Insets(0, 0, 0, 0);
/*     */       }
/*     */       
/*     */ 
/* 210 */       if (this.aqua) {
/* 211 */         int aquaBottomInsets = 21;
/* 212 */         int aquaTopInsets = 22;
/*     */         
/* 214 */         screenInsets.bottom = Math.max(screenInsets.bottom, aquaBottomInsets);
/* 215 */         screenInsets.top = Math.max(screenInsets.top, aquaTopInsets);
/*     */       }
/*     */       
/* 218 */       return screenInsets;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\autocomplete\workarounds\MacOSXPopupLocationFix.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */