/*     */ package org.jdesktop.swingx.plaf.basic;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.text.DateFormatSymbols;
/*     */ import java.util.Calendar;
/*     */ import java.util.Locale;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.BoxLayout;
/*     */ import org.jdesktop.swingx.JXHyperlink;
/*     */ import org.jdesktop.swingx.JXMonthView;
/*     */ import org.jdesktop.swingx.JXPanel;
/*     */ import org.jdesktop.swingx.hyperlink.AbstractHyperlinkAction;
/*     */ import org.jdesktop.swingx.renderer.StringValue;
/*     */ import org.jdesktop.swingx.renderer.StringValues;
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
/*     */ public class BasicCalendarHeaderHandler
/*     */   extends CalendarHeaderHandler
/*     */ {
/*     */   public void install(JXMonthView monthView)
/*     */   {
/*  58 */     super.install(monthView);
/*  59 */     getHeaderComponent().setActions(monthView.getActionMap().get("previousMonth"), monthView.getActionMap().get("nextMonth"), monthView.getActionMap().get("zoomOut"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void installNavigationActions()
/*     */   {
/*  70 */     super.installNavigationActions();
/*  71 */     ZoomOutAction zoomOutAction = new ZoomOutAction();
/*  72 */     zoomOutAction.setTarget(this.monthView);
/*  73 */     this.monthView.getActionMap().put("zoomOut", zoomOutAction);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void uninstall(JXMonthView monthView)
/*     */   {
/*  80 */     getHeaderComponent().setActions(null, null, null);
/*  81 */     super.uninstall(monthView);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BasicCalendarHeader getHeaderComponent()
/*     */   {
/*  88 */     return (BasicCalendarHeader)super.getHeaderComponent();
/*     */   }
/*     */   
/*     */   protected BasicCalendarHeader createCalendarHeader()
/*     */   {
/*  93 */     return new BasicCalendarHeader();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static class ZoomOutAction
/*     */     extends AbstractHyperlinkAction<JXMonthView>
/*     */   {
/*     */     private PropertyChangeListener linkListener;
/*     */     
/*     */     private String[] monthNames;
/*     */     
/*     */     private StringValue tsv;
/*     */     
/*     */ 
/*     */     public ZoomOutAction()
/*     */     {
/* 110 */       this.tsv = new StringValue()
/*     */       {
/*     */         public String getString(Object value) {
/* 113 */           if ((value instanceof Calendar)) {
/* 114 */             String month = BasicCalendarHeaderHandler.ZoomOutAction.this.monthNames[((Calendar)value).get(2)];
/*     */             
/* 116 */             return month + " " + ((Calendar)value).get(1);
/*     */           }
/*     */           
/* 119 */           return StringValues.TO_STRING.getString(value);
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void actionPerformed(ActionEvent e) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected void installTarget()
/*     */     {
/* 137 */       if (getTarget() != null) {
/* 138 */         ((JXMonthView)getTarget()).addPropertyChangeListener(getTargetListener());
/*     */       }
/* 140 */       updateLocale();
/* 141 */       updateFromTarget();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void updateLocale()
/*     */     {
/* 148 */       Locale current = getTarget() != null ? ((JXMonthView)getTarget()).getLocale() : Locale.getDefault();
/* 149 */       this.monthNames = DateFormatSymbols.getInstance(current).getMonths();
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
/*     */     protected void uninstallTarget()
/*     */     {
/* 162 */       if (getTarget() == null) return;
/* 163 */       ((JXMonthView)getTarget()).removePropertyChangeListener(getTargetListener());
/*     */     }
/*     */     
/*     */     protected void updateFromTarget()
/*     */     {
/* 168 */       if (this.tsv == null) return;
/* 169 */       Calendar calendar = getTarget() != null ? ((JXMonthView)getTarget()).getCalendar() : null;
/* 170 */       setName(this.tsv.getString(calendar));
/*     */     }
/*     */     
/*     */     private PropertyChangeListener getTargetListener() {
/* 174 */       if (this.linkListener == null) {
/* 175 */         this.linkListener = new PropertyChangeListener()
/*     */         {
/*     */           public void propertyChange(PropertyChangeEvent evt) {
/* 178 */             if ("firstDisplayedDay".equals(evt.getPropertyName())) {
/* 179 */               BasicCalendarHeaderHandler.ZoomOutAction.this.updateFromTarget();
/* 180 */             } else if ("locale".equals(evt.getPropertyName())) {
/* 181 */               BasicCalendarHeaderHandler.ZoomOutAction.this.updateLocale();
/* 182 */               BasicCalendarHeaderHandler.ZoomOutAction.this.updateFromTarget();
/*     */             }
/*     */           }
/*     */         };
/*     */       }
/*     */       
/* 188 */       return this.linkListener;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static class BasicCalendarHeader
/*     */     extends JXPanel
/*     */   {
/*     */     protected AbstractButton prevButton;
/*     */     
/*     */ 
/*     */     protected AbstractButton nextButton;
/*     */     
/*     */     protected JXHyperlink zoomOutLink;
/*     */     
/*     */ 
/*     */     public BasicCalendarHeader()
/*     */     {
/* 207 */       setLayout(new BoxLayout(this, 2));
/* 208 */       this.prevButton = createNavigationButton();
/* 209 */       this.nextButton = createNavigationButton();
/* 210 */       this.zoomOutLink = createZoomLink();
/* 211 */       add(this.prevButton);
/* 212 */       add(Box.createHorizontalGlue());
/* 213 */       add(this.zoomOutLink);
/* 214 */       add(Box.createHorizontalGlue());
/* 215 */       add(this.nextButton);
/* 216 */       setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setActions(Action prev, Action next, Action zoomOut)
/*     */     {
/* 227 */       this.prevButton.setAction(prev);
/* 228 */       this.nextButton.setAction(next);
/* 229 */       this.zoomOutLink.setAction(zoomOut);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setFont(Font font)
/*     */     {
/* 240 */       super.setFont(font);
/* 241 */       if (this.zoomOutLink != null)
/* 242 */         this.zoomOutLink.setFont(font);
/*     */     }
/*     */     
/*     */     private JXHyperlink createZoomLink() {
/* 246 */       JXHyperlink zoomOutLink = new JXHyperlink();
/* 247 */       Color textColor = new Color(16, 66, 104);
/* 248 */       zoomOutLink.setUnclickedColor(textColor);
/* 249 */       zoomOutLink.setClickedColor(textColor);
/* 250 */       zoomOutLink.setFocusable(false);
/* 251 */       return zoomOutLink;
/*     */     }
/*     */     
/*     */     private AbstractButton createNavigationButton() {
/* 255 */       JXHyperlink b = new JXHyperlink();
/* 256 */       b.setContentAreaFilled(false);
/* 257 */       b.setBorder(BorderFactory.createEmptyBorder());
/* 258 */       b.setRolloverEnabled(true);
/* 259 */       b.setFocusable(false);
/* 260 */       return b;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\BasicCalendarHeaderHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */