/*     */ package org.jdesktop.swingx.plaf.basic;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Font;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import org.jdesktop.swingx.JXMonthView;
/*     */ import org.jdesktop.swingx.action.AbstractActionExt;
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
/*     */ public abstract class CalendarHeaderHandler
/*     */ {
/*  74 */   private static final Logger LOG = Logger.getLogger(CalendarHeaderHandler.class.getName());
/*     */   
/*     */ 
/*     */   public static final String uiControllerID = "CalendarHeaderHandler";
/*     */   
/*     */ 
/*     */   protected JXMonthView monthView;
/*     */   
/*     */ 
/*     */   private JComponent calendarHeader;
/*     */   
/*     */ 
/*     */   protected Icon monthDownImage;
/*     */   
/*     */   protected Icon monthUpImage;
/*     */   
/*     */   private PropertyChangeListener monthViewPropertyChangeListener;
/*     */   
/*     */ 
/*     */   public void install(JXMonthView monthView)
/*     */   {
/*  95 */     this.monthView = monthView;
/*     */     
/*     */ 
/*     */ 
/*  99 */     this.monthDownImage = UIManager.getIcon("JXMonthView.monthDownFileName");
/* 100 */     this.monthUpImage = UIManager.getIcon("JXMonthView.monthUpFileName");
/* 101 */     installNavigationActions();
/* 102 */     installListeners();
/* 103 */     componentOrientationChanged();
/* 104 */     monthStringBackgroundChanged();
/* 105 */     fontChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void uninstall(JXMonthView monthView)
/*     */   {
/* 114 */     this.monthView.remove(getHeaderComponent());
/* 115 */     uninstallListeners();
/* 116 */     this.monthView = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JComponent getHeaderComponent()
/*     */   {
/* 126 */     if (this.calendarHeader == null) {
/* 127 */       this.calendarHeader = createCalendarHeader();
/*     */     }
/* 129 */     return this.calendarHeader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/* 139 */     this.monthView.addPropertyChangeListener(getMonthViewPropertyChangeListener());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 147 */     this.monthView.removePropertyChangeListener(this.monthViewPropertyChangeListener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private PropertyChangeListener getMonthViewPropertyChangeListener()
/*     */   {
/* 156 */     if (this.monthViewPropertyChangeListener == null) {
/* 157 */       this.monthViewPropertyChangeListener = new PropertyChangeListener()
/*     */       {
/*     */         public void propertyChange(PropertyChangeEvent evt) {
/* 160 */           if ("componentOrientation".equals(evt.getPropertyName())) {
/* 161 */             CalendarHeaderHandler.this.componentOrientationChanged();
/* 162 */           } else if ("font".equals(evt.getPropertyName())) {
/* 163 */             CalendarHeaderHandler.this.fontChanged();
/* 164 */           } else if ("monthStringBackground".equals(evt.getPropertyName()))
/*     */           {
/* 166 */             CalendarHeaderHandler.this.monthStringBackgroundChanged();
/*     */           }
/*     */         }
/*     */       };
/*     */     }
/*     */     
/* 172 */     return this.monthViewPropertyChangeListener;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void monthStringBackgroundChanged()
/*     */   {
/* 180 */     getHeaderComponent().setBackground(getAsNotUIResource(this.monthView.getMonthStringBackground()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void fontChanged()
/*     */   {
/* 189 */     getHeaderComponent().setFont(getAsNotUIResource(createDerivedFont()));
/* 190 */     this.monthView.revalidate();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void componentOrientationChanged()
/*     */   {
/* 201 */     getHeaderComponent().applyComponentOrientation(this.monthView.getComponentOrientation());
/*     */     
/* 203 */     if (this.monthView.getComponentOrientation().isLeftToRight()) {
/* 204 */       updateMonthNavigationIcons(this.monthDownImage, this.monthUpImage);
/*     */     } else {
/* 206 */       updateMonthNavigationIcons(this.monthUpImage, this.monthDownImage);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateMonthNavigationIcons(Icon previous, Icon next)
/*     */   {
/* 215 */     updateActionIcon("previousMonth", previous);
/* 216 */     updateActionIcon("nextMonth", next);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateActionIcon(String previousKey, Icon previous)
/*     */   {
/* 224 */     Action action = this.monthView.getActionMap().get(previousKey);
/* 225 */     if (action != null) {
/* 226 */       action.putValue("SmallIcon", previous);
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
/*     */   protected abstract JComponent createCalendarHeader();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void installNavigationActions()
/*     */   {
/* 247 */     installWrapper("scrollToPreviousMonth", "previousMonth", this.monthView.getComponentOrientation().isLeftToRight() ? this.monthDownImage : this.monthUpImage);
/*     */     
/*     */ 
/* 250 */     installWrapper("scrollToNextMonth", "nextMonth", this.monthView.getComponentOrientation().isLeftToRight() ? this.monthUpImage : this.monthDownImage);
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
/*     */   private void installWrapper(final String actionKey, String newActionKey, Icon icon)
/*     */   {
/* 266 */     AbstractActionExt wrapper = new AbstractActionExt(null, icon)
/*     */     {
/*     */       public void actionPerformed(ActionEvent e) {
/* 269 */         Action action = CalendarHeaderHandler.this.monthView.getActionMap().get(actionKey);
/* 270 */         if (action != null) {
/* 271 */           action.actionPerformed(e);
/*     */         }
/*     */         
/*     */       }
/* 275 */     };
/* 276 */     this.monthView.getActionMap().put(newActionKey, wrapper);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Font getAsNotUIResource(Font font)
/*     */   {
/* 286 */     if (!(font instanceof UIResource)) {
/* 287 */       return font;
/*     */     }
/* 289 */     return font.deriveFont(font.getAttributes());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Color getAsNotUIResource(Color color)
/*     */   {
/* 299 */     if (!(color instanceof UIResource)) {
/* 300 */       return color;
/*     */     }
/* 302 */     float[] rgb = color.getRGBComponents(null);
/* 303 */     return new Color(rgb[0], rgb[1], rgb[2], rgb[3]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Font createDerivedFont()
/*     */   {
/* 312 */     return this.monthView.getFont().deriveFont(1);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\CalendarHeaderHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */