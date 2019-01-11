/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.Timer;
/*     */ import javax.swing.plaf.LabelUI;
/*     */ import org.jdesktop.swingx.icon.PainterIcon;
/*     */ import org.jdesktop.swingx.painter.BusyPainter;
/*     */ import org.jdesktop.swingx.plaf.BusyLabelAddon;
/*     */ import org.jdesktop.swingx.plaf.BusyLabelUI;
/*     */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
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
/*     */ public class JXBusyLabel
/*     */   extends JLabel
/*     */ {
/*     */   private static final long serialVersionUID = 5979268460848257147L;
/*     */   private BusyPainter busyPainter;
/*     */   private Timer busy;
/*     */   private int delay;
/* 103 */   private boolean wasBusyOnNotify = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String uiClassID = "BusyLabelUI";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Direction direction;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum Direction
/*     */   {
/* 120 */     RIGHT, 
/*     */     
/* 122 */     LEFT;
/*     */     
/*     */ 
/*     */     private Direction() {}
/*     */   }
/*     */   
/*     */ 
/*     */   public void setDirection(Direction dir)
/*     */   {
/* 131 */     this.direction = dir;
/* 132 */     getBusyPainter().setDirection(dir);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/* 141 */     LookAndFeelAddons.contribute(new BusyLabelAddon());
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
/*     */   public JXBusyLabel()
/*     */   {
/* 154 */     this(null);
/*     */   }
/*     */   
/*     */   public JXBusyLabel(Dimension dim)
/*     */   {
/* 146 */     BusyLabelUI ui = (BusyLabelUI)getUI();
/* 147 */     if (ui != null) {
/* 148 */       this.delay = ui.getDelay();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 163 */     setPreferredSize(dim);
/*     */     
/*     */ 
/* 166 */     getBusyPainter();
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
/*     */   protected void initPainter(Dimension dim)
/*     */   {
/* 180 */     BusyPainter busyPainter = getBusyPainter();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 186 */     if (null != busyPainter) {
/* 187 */       busyPainter.setCacheable(false);
/*     */     }
/*     */     
/* 190 */     PainterIcon icon = new PainterIcon(dim);
/* 191 */     icon.setPainter(busyPainter);
/* 192 */     setIcon(icon);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BusyPainter createBusyPainter(Dimension dim)
/*     */   {
/* 203 */     BusyPainter busyPainter = null;
/*     */     
/* 205 */     BusyLabelUI ui = (BusyLabelUI)getUI();
/* 206 */     if (ui != null) {
/* 207 */       busyPainter = ui.getBusyPainter(dim);
/*     */     }
/*     */     
/*     */ 
/* 211 */     return busyPainter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBusy()
/*     */   {
/* 222 */     return this.busy != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBusy(boolean busy)
/*     */   {
/* 234 */     boolean old = isBusy();
/* 235 */     if ((!old) && (busy)) {
/* 236 */       startAnimation();
/* 237 */       firePropertyChange("busy", old, isBusy());
/* 238 */     } else if ((old) && (!busy)) {
/* 239 */       stopAnimation();
/* 240 */       firePropertyChange("busy", old, isBusy());
/*     */     }
/*     */   }
/*     */   
/*     */   private void startAnimation() {
/* 245 */     if (this.busy != null) {
/* 246 */       stopAnimation();
/*     */     }
/*     */     
/* 249 */     this.busy = new Timer(this.delay, new ActionListener() {
/* 250 */       BusyPainter busyPainter = JXBusyLabel.this.getBusyPainter();
/* 251 */       int frame = this.busyPainter.getPoints();
/*     */       
/* 253 */       public void actionPerformed(ActionEvent e) { this.frame = ((this.frame + 1) % this.busyPainter.getPoints());
/* 254 */         this.busyPainter.setFrame(JXBusyLabel.this.direction == JXBusyLabel.Direction.LEFT ? this.busyPainter.getPoints() - this.frame : this.frame);
/* 255 */         JXBusyLabel.this.frameChanged();
/*     */       }
/* 257 */     });
/* 258 */     this.busy.start();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void stopAnimation()
/*     */   {
/* 265 */     if (this.busy != null) {
/* 266 */       this.busy.stop();
/* 267 */       getBusyPainter().setFrame(-1);
/* 268 */       repaint();
/* 269 */       this.busy = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void removeNotify()
/*     */   {
/* 276 */     this.wasBusyOnNotify = isBusy();
/*     */     
/* 278 */     stopAnimation();
/* 279 */     super.removeNotify();
/*     */   }
/*     */   
/*     */   public void addNotify()
/*     */   {
/* 284 */     super.addNotify();
/*     */     
/* 286 */     if (this.wasBusyOnNotify)
/*     */     {
/* 288 */       startAnimation();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void frameChanged() {
/* 293 */     repaint();
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
/*     */   public final BusyPainter getBusyPainter()
/*     */   {
/* 309 */     if (null == this.busyPainter) {
/* 310 */       Dimension prefSize = getPreferredSize();
/*     */       
/* 312 */       this.busyPainter = createBusyPainter((prefSize.width == 0) && (prefSize.height == 0) && (!isPreferredSizeSet()) ? null : prefSize);
/*     */       
/* 314 */       if (null != this.busyPainter) {
/* 315 */         if ((!isPreferredSizeSet()) && ((null == prefSize) || (prefSize.width == 0) || (prefSize.height == 0))) {
/* 316 */           Rectangle rt = this.busyPainter.getTrajectory().getBounds();
/* 317 */           Rectangle rp = this.busyPainter.getPointShape().getBounds();
/* 318 */           int max = Math.max(rp.width, rp.height);
/* 319 */           prefSize = new Dimension(rt.width + max, rt.height + max);
/*     */         }
/*     */         
/* 322 */         initPainter(prefSize);
/*     */       }
/*     */     }
/* 325 */     return this.busyPainter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void setBusyPainter(BusyPainter busyPainter)
/*     */   {
/* 332 */     this.busyPainter = busyPainter;
/* 333 */     initPainter(new Dimension(getIcon().getIconWidth(), getIcon().getIconHeight()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getDelay()
/*     */   {
/* 340 */     return this.delay;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setDelay(int delay)
/*     */   {
/* 347 */     int old = getDelay();
/* 348 */     this.delay = delay;
/* 349 */     if (old != getDelay()) {
/* 350 */       if ((this.busy != null) && (this.busy.isRunning())) {
/* 351 */         this.busy.setDelay(getDelay());
/*     */       }
/* 353 */       firePropertyChange("delay", old, getDelay());
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
/*     */   public void updateUI()
/*     */   {
/* 367 */     setUI((LabelUI)LookAndFeelAddons.getUI(this, BusyLabelUI.class));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 379 */     return "BusyLabelUI";
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXBusyLabel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */