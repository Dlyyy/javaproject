/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Point;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.event.MouseInputAdapter;
/*     */ import org.jdesktop.swingx.multislider.DefaultMultiThumbModel;
/*     */ import org.jdesktop.swingx.multislider.MultiThumbModel;
/*     */ import org.jdesktop.swingx.multislider.Thumb;
/*     */ import org.jdesktop.swingx.multislider.ThumbDataEvent;
/*     */ import org.jdesktop.swingx.multislider.ThumbDataListener;
/*     */ import org.jdesktop.swingx.multislider.ThumbListener;
/*     */ import org.jdesktop.swingx.multislider.ThumbRenderer;
/*     */ import org.jdesktop.swingx.multislider.TrackRenderer;
/*     */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.MultiThumbSliderAddon;
/*     */ import org.jdesktop.swingx.plaf.MultiThumbSliderUI;
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
/*     */ public class JXMultiThumbSlider<E>
/*     */   extends JComponent
/*     */ {
/*     */   public static final String uiClassID = "MultiThumbSliderUI";
/*     */   private ThumbDataListener tdl;
/*     */   private List<ThumbComp> thumbs;
/*     */   private ThumbRenderer thumbRenderer;
/*     */   private TrackRenderer trackRenderer;
/*     */   private MultiThumbModel<E> model;
/*  77 */   private List<ThumbListener> listeners = new ArrayList();
/*     */   private ThumbComp selected;
/*     */   
/*     */   static
/*     */   {
/*  82 */     LookAndFeelAddons.contribute(new MultiThumbSliderAddon());
/*     */   }
/*     */   
/*     */   public JXMultiThumbSlider()
/*     */   {
/*  87 */     this.thumbs = new ArrayList();
/*  88 */     setLayout(null);
/*     */     
/*  90 */     this.tdl = new ThumbHandler(null);
/*     */     
/*  92 */     setModel(new DefaultMultiThumbModel());
/*  93 */     JXMultiThumbSlider<E>.MultiThumbMouseListener mia = new MultiThumbMouseListener(null);
/*  94 */     addMouseListener(mia);
/*  95 */     addMouseMotionListener(mia);
/*     */     
/*  97 */     Dimension dim = new Dimension(60, 16);
/*  98 */     setPreferredSize(dim);
/*  99 */     setSize(dim);
/* 100 */     setMinimumSize(new Dimension(30, 16));
/* 101 */     updateUI();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 109 */     return "MultiThumbSliderUI";
/*     */   }
/*     */   
/*     */   public MultiThumbSliderUI getUI() {
/* 113 */     return (MultiThumbSliderUI)this.ui;
/*     */   }
/*     */   
/*     */   public void setUI(MultiThumbSliderUI ui) {
/* 117 */     super.setUI(ui);
/*     */   }
/*     */   
/*     */   public void updateUI()
/*     */   {
/* 122 */     setUI((MultiThumbSliderUI)LookAndFeelAddons.getUI(this, MultiThumbSliderUI.class));
/* 123 */     invalidate();
/*     */   }
/*     */   
/*     */   protected void paintComponent(Graphics g)
/*     */   {
/* 128 */     if (isVisible()) {
/* 129 */       if (this.trackRenderer != null) {
/* 130 */         JComponent comp = this.trackRenderer.getRendererComponent(this);
/* 131 */         add(comp);
/* 132 */         comp.paint(g);
/* 133 */         remove(comp);
/*     */       } else {
/* 135 */         paintRange((Graphics2D)g);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void paintRange(Graphics2D g) {
/* 141 */     g.setColor(Color.blue);
/* 142 */     g.fillRect(0, 0, getWidth(), getHeight());
/*     */   }
/*     */   
/*     */   private float getThumbValue(int thumbIndex) {
/* 146 */     return getModel().getThumbAt(thumbIndex).getPosition();
/*     */   }
/*     */   
/*     */   private float getThumbValue(ThumbComp thumb) {
/* 150 */     return getThumbValue(this.thumbs.indexOf(thumb));
/*     */   }
/*     */   
/*     */   private int getThumbIndex(ThumbComp thumb) {
/* 154 */     return this.thumbs.indexOf(thumb);
/*     */   }
/*     */   
/*     */   private void clipThumbPosition(ThumbComp thumb) {
/* 158 */     if (getThumbValue(thumb) < getModel().getMinimumValue()) {
/* 159 */       getModel().getThumbAt(getThumbIndex(thumb)).setPosition(getModel().getMinimumValue());
/*     */     }
/*     */     
/* 162 */     if (getThumbValue(thumb) > getModel().getMaximumValue()) {
/* 163 */       getModel().getThumbAt(getThumbIndex(thumb)).setPosition(getModel().getMaximumValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public ThumbRenderer getThumbRenderer()
/*     */   {
/* 169 */     return this.thumbRenderer;
/*     */   }
/*     */   
/*     */   public void setThumbRenderer(ThumbRenderer thumbRenderer) {
/* 173 */     this.thumbRenderer = thumbRenderer;
/*     */   }
/*     */   
/*     */   public TrackRenderer getTrackRenderer() {
/* 177 */     return this.trackRenderer;
/*     */   }
/*     */   
/*     */   public void setTrackRenderer(TrackRenderer trackRenderer) {
/* 181 */     this.trackRenderer = trackRenderer;
/*     */   }
/*     */   
/*     */   public float getMinimumValue() {
/* 185 */     return getModel().getMinimumValue();
/*     */   }
/*     */   
/*     */   public void setMinimumValue(float minimumValue) {
/* 189 */     getModel().setMinimumValue(minimumValue);
/*     */   }
/*     */   
/*     */   public float getMaximumValue() {
/* 193 */     return getModel().getMaximumValue();
/*     */   }
/*     */   
/*     */   public void setMaximumValue(float maximumValue) {
/* 197 */     getModel().setMaximumValue(maximumValue);
/*     */   }
/*     */   
/*     */   private void setThumbPositionByX(ThumbComp selected) {
/* 201 */     float range = getModel().getMaximumValue() - getModel().getMinimumValue();
/* 202 */     int x = selected.getX();
/*     */     
/* 204 */     x += selected.getWidth() / 2;
/*     */     
/* 206 */     x -= selected.getWidth() / 2;
/*     */     
/* 208 */     int w = getWidth();
/*     */     
/* 210 */     w -= selected.getWidth();
/* 211 */     float delta = x / w;
/* 212 */     int thumb_index = getThumbIndex(selected);
/* 213 */     float value = delta * range;
/* 214 */     getModel().getThumbAt(thumb_index).setPosition(value);
/*     */     
/* 216 */     clipThumbPosition(selected);
/*     */   }
/*     */   
/*     */   private void setThumbXByPosition(ThumbComp thumb, float pos) {
/* 220 */     float lp = getWidth() - thumb.getWidth();
/* 221 */     float lu = getModel().getMaximumValue() - getModel().getMinimumValue();
/* 222 */     float tp = pos * lp / lu;
/* 223 */     thumb.setLocation((int)tp - thumb.getWidth() / 2 + thumb.getWidth() / 2, thumb.getY());
/*     */   }
/*     */   
/*     */   private void recalc() {
/* 227 */     for (ThumbComp th : this.thumbs) {
/* 228 */       setThumbXByPosition(th, getModel().getThumbAt(getThumbIndex(th)).getPosition());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void setBounds(int x, int y, int w, int h)
/*     */   {
/* 235 */     super.setBounds(x, y, w, h);
/* 236 */     recalc();
/*     */   }
/*     */   
/*     */   public JComponent getSelectedThumb() {
/* 240 */     return this.selected;
/*     */   }
/*     */   
/*     */   public int getSelectedIndex() {
/* 244 */     return getThumbIndex(this.selected);
/*     */   }
/*     */   
/*     */   public MultiThumbModel<E> getModel() {
/* 248 */     return this.model;
/*     */   }
/*     */   
/*     */   public void setModel(MultiThumbModel<E> model) {
/* 252 */     if (this.model != null) {
/* 253 */       this.model.removeThumbDataListener(this.tdl);
/*     */     }
/* 255 */     this.model = model;
/* 256 */     this.model.addThumbDataListener(this.tdl);
/*     */   }
/*     */   
/*     */ 
/* 260 */   public void addMultiThumbListener(ThumbListener listener) { this.listeners.add(listener); }
/*     */   
/*     */   private class MultiThumbMouseListener extends MouseInputAdapter {
/*     */     private MultiThumbMouseListener() {}
/*     */     
/*     */     public void mousePressed(MouseEvent evt) {
/* 266 */       JXMultiThumbSlider.ThumbComp handle = findHandle(evt);
/* 267 */       if (handle != null) {
/* 268 */         JXMultiThumbSlider.this.selected = handle;
/* 269 */         JXMultiThumbSlider.this.selected.setSelected(true);
/* 270 */         int thumb_index = JXMultiThumbSlider.this.getThumbIndex(JXMultiThumbSlider.this.selected);
/* 271 */         for (ThumbListener tl : JXMultiThumbSlider.this.listeners) {
/* 272 */           tl.thumbSelected(thumb_index);
/*     */         }
/* 274 */         JXMultiThumbSlider.this.repaint();
/*     */       } else {
/* 276 */         JXMultiThumbSlider.this.selected = null;
/* 277 */         for (ThumbListener tl : JXMultiThumbSlider.this.listeners) {
/* 278 */           tl.thumbSelected(-1);
/*     */         }
/* 280 */         JXMultiThumbSlider.this.repaint();
/*     */       }
/* 282 */       for (ThumbListener tl : JXMultiThumbSlider.this.listeners) {
/* 283 */         tl.mousePressed(evt);
/*     */       }
/*     */     }
/*     */     
/*     */     public void mouseReleased(MouseEvent evt)
/*     */     {
/* 289 */       if (JXMultiThumbSlider.this.selected != null) {
/* 290 */         JXMultiThumbSlider.this.selected.setSelected(false);
/*     */       }
/*     */     }
/*     */     
/*     */     public void mouseDragged(MouseEvent evt)
/*     */     {
/* 296 */       if (JXMultiThumbSlider.this.selected != null) {
/* 297 */         int nx = (int)evt.getPoint().getX() - JXMultiThumbSlider.this.selected.getWidth() / 2;
/* 298 */         if (nx < 0) {
/* 299 */           nx = 0;
/*     */         }
/* 301 */         if (nx > JXMultiThumbSlider.this.getWidth() - JXMultiThumbSlider.this.selected.getWidth()) {
/* 302 */           nx = JXMultiThumbSlider.this.getWidth() - JXMultiThumbSlider.this.selected.getWidth();
/*     */         }
/* 304 */         JXMultiThumbSlider.this.selected.setLocation(nx, (int)JXMultiThumbSlider.this.selected.getLocation().getY());
/* 305 */         JXMultiThumbSlider.this.setThumbPositionByX(JXMultiThumbSlider.this.selected);
/* 306 */         int thumb_index = JXMultiThumbSlider.this.getThumbIndex(JXMultiThumbSlider.this.selected);
/*     */         
/* 308 */         for (ThumbListener mtl : JXMultiThumbSlider.this.listeners) {
/* 309 */           mtl.thumbMoved(thumb_index, JXMultiThumbSlider.this.getModel().getThumbAt(thumb_index).getPosition());
/*     */         }
/*     */         
/* 312 */         JXMultiThumbSlider.this.repaint();
/*     */       }
/*     */     }
/*     */     
/*     */     private JXMultiThumbSlider.ThumbComp findHandle(MouseEvent evt)
/*     */     {
/* 318 */       for (JXMultiThumbSlider.ThumbComp hand : JXMultiThumbSlider.this.thumbs) {
/* 319 */         Point p2 = new Point();
/* 320 */         p2.setLocation(evt.getPoint().getX() - hand.getX(), evt.getPoint().getY() - hand.getY());
/*     */         
/* 322 */         if (hand.contains(p2)) {
/* 323 */           return hand;
/*     */         }
/*     */       }
/* 326 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ThumbComp extends JComponent {
/*     */     private JXMultiThumbSlider<?> slider;
/*     */     private boolean selected;
/*     */     
/*     */     public ThumbComp(JXMultiThumbSlider<?> slider) {
/* 335 */       this.slider = slider;
/* 336 */       Dimension dim = new Dimension(10, 10);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 341 */       setSize(dim);
/* 342 */       setMinimumSize(dim);
/* 343 */       setPreferredSize(dim);
/* 344 */       setMaximumSize(dim);
/* 345 */       setBackground(Color.white);
/*     */     }
/*     */     
/*     */     public void paintComponent(Graphics g)
/*     */     {
/* 350 */       if (this.slider.getThumbRenderer() != null) {
/* 351 */         JComponent comp = getRenderer();
/* 352 */         comp.setSize(getSize());
/* 353 */         comp.paint(g);
/*     */       } else {
/* 355 */         g.setColor(getBackground());
/* 356 */         g.fillRect(0, 0, getWidth(), getHeight());
/* 357 */         if (isSelected()) {
/* 358 */           g.setColor(Color.black);
/* 359 */           g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private JComponent getRenderer() {
/* 365 */       return this.slider.getThumbRenderer().getThumbRendererComponent(this.slider, this.slider.getThumbIndex(this), isSelected());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean isSelected()
/*     */     {
/* 372 */       return this.selected;
/*     */     }
/*     */     
/*     */ 
/* 376 */     public void setSelected(boolean selected) { this.selected = selected; }
/*     */   }
/*     */   
/*     */   private class ThumbHandler implements ThumbDataListener {
/*     */     private ThumbHandler() {}
/*     */     
/* 382 */     public void positionChanged(ThumbDataEvent e) { JXMultiThumbSlider.ThumbComp comp = (JXMultiThumbSlider.ThumbComp)JXMultiThumbSlider.this.thumbs.get(e.getIndex());
/* 383 */       JXMultiThumbSlider.this.clipThumbPosition(comp);
/* 384 */       JXMultiThumbSlider.this.setThumbXByPosition(comp, e.getThumb().getPosition());
/* 385 */       JXMultiThumbSlider.this.repaint();
/*     */     }
/*     */     
/*     */     public void thumbAdded(ThumbDataEvent evt) {
/* 389 */       JXMultiThumbSlider.ThumbComp thumb = new JXMultiThumbSlider.ThumbComp(JXMultiThumbSlider.this);
/* 390 */       thumb.setLocation(0, 0);
/* 391 */       JXMultiThumbSlider.this.add(thumb);
/* 392 */       JXMultiThumbSlider.this.thumbs.add(evt.getIndex(), thumb);
/* 393 */       JXMultiThumbSlider.this.clipThumbPosition(thumb);
/* 394 */       JXMultiThumbSlider.this.setThumbXByPosition(thumb, evt.getThumb().getPosition());
/* 395 */       JXMultiThumbSlider.this.repaint();
/*     */     }
/*     */     
/*     */     public void thumbRemoved(ThumbDataEvent evt) {
/* 399 */       JXMultiThumbSlider.ThumbComp thumb = (JXMultiThumbSlider.ThumbComp)JXMultiThumbSlider.this.thumbs.get(evt.getIndex());
/* 400 */       JXMultiThumbSlider.this.remove(thumb);
/* 401 */       JXMultiThumbSlider.this.thumbs.remove(thumb);
/* 402 */       JXMultiThumbSlider.this.repaint();
/*     */     }
/*     */     
/*     */     public void valueChanged(ThumbDataEvent e) {
/* 406 */       JXMultiThumbSlider.this.repaint();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXMultiThumbSlider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */