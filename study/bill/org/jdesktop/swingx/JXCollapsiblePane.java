/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JViewport;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.Timer;
/*     */ import javax.swing.border.Border;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JXCollapsiblePane
/*     */   extends JXPanel
/*     */ {
/*     */   public static final String ANIMATION_STATE_KEY = "animationState";
/*     */   public static final String TOGGLE_ACTION = "toggle";
/*     */   public static final String COLLAPSE_ICON = "collapseIcon";
/*     */   public static final String EXPAND_ICON = "expandIcon";
/*     */   
/*     */   public static enum Direction
/*     */   {
/* 171 */     LEFT(false), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 176 */     RIGHT(false), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 181 */     UP(true), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 186 */     DOWN(true);
/*     */     
/*     */     private final boolean vertical;
/*     */     
/*     */     private Direction(boolean vertical) {
/* 191 */       this.vertical = vertical;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isVertical()
/*     */     {
/* 201 */       return this.vertical;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 241 */   private boolean collapsed = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 246 */   private Direction direction = Direction.UP;
/*     */   
/*     */ 
/*     */   private Timer animateTimer;
/*     */   
/*     */   private AnimationListener animator;
/*     */   
/* 253 */   private int currentDimension = -1;
/*     */   private WrapperContainer wrapper;
/* 255 */   private boolean useAnimation = true;
/*     */   
/*     */ 
/*     */   private AnimationParams animationParams;
/*     */   
/*     */ 
/*     */ 
/*     */   public JXCollapsiblePane()
/*     */   {
/* 264 */     this(Direction.UP, new BorderLayout(0, 0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXCollapsiblePane(Direction direction)
/*     */   {
/* 275 */     this(direction, new BorderLayout(0, 0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXCollapsiblePane(LayoutManager layout)
/*     */   {
/* 283 */     this(Direction.UP, layout);
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
/*     */   public JXCollapsiblePane(Direction direction, LayoutManager layout)
/*     */   {
/* 299 */     super.setLayout(layout);
/*     */     
/* 301 */     this.direction = direction;
/* 302 */     this.animator = new AnimationListener(null);
/* 303 */     setAnimationParams(new AnimationParams(30, 8, 0.01F, 1.0F));
/*     */     
/* 305 */     JXPanel panel = new JXPanel();
/* 306 */     setContentPane(panel);
/* 307 */     setDirection(direction);
/*     */     
/*     */ 
/* 310 */     getActionMap().put("toggle", new ToggleAction());
/*     */   }
/*     */   
/*     */ 
/*     */   private class ToggleAction
/*     */     extends AbstractAction
/*     */     implements PropertyChangeListener
/*     */   {
/*     */     public ToggleAction()
/*     */     {
/* 320 */       super();
/*     */       
/*     */ 
/* 323 */       JXCollapsiblePane.this.addPropertyChangeListener("collapsed", this);
/*     */     }
/*     */     
/*     */     public void putValue(String key, Object newValue)
/*     */     {
/* 328 */       super.putValue(key, newValue);
/* 329 */       if (("expandIcon".equals(key)) || ("collapseIcon".equals(key))) {
/* 330 */         updateIcon();
/*     */       }
/*     */     }
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 335 */       JXCollapsiblePane.this.setCollapsed(!JXCollapsiblePane.this.isCollapsed());
/*     */     }
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent evt) {
/* 339 */       updateIcon();
/*     */     }
/*     */     
/*     */     void updateIcon() {
/* 343 */       if (JXCollapsiblePane.this.isCollapsed()) {
/* 344 */         putValue("SmallIcon", getValue("expandIcon"));
/*     */       } else {
/* 346 */         putValue("SmallIcon", getValue("collapseIcon"));
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
/*     */   public void setContentPane(Container contentPanel)
/*     */   {
/* 368 */     if (contentPanel == null) {
/* 369 */       throw new IllegalArgumentException("Content pane can't be null");
/*     */     }
/*     */     
/* 372 */     if (this.wrapper != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 377 */       assert (super.getComponent(0) == this.wrapper);
/* 378 */       super.remove(0);
/*     */     }
/* 380 */     this.wrapper = new WrapperContainer(contentPanel);
/* 381 */     this.wrapper.collapsedState = isCollapsed();
/* 382 */     super.addImpl(this.wrapper, "Center", -1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Container getContentPane()
/*     */   {
/* 389 */     if (this.wrapper == null) {
/* 390 */       return null;
/*     */     }
/*     */     
/* 393 */     return (Container)this.wrapper.getView();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLayout(LayoutManager mgr)
/*     */   {
/* 402 */     if (this.wrapper != null) {
/* 403 */       getContentPane().setLayout(mgr);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addImpl(Component comp, Object constraints, int index)
/*     */   {
/* 412 */     getContentPane().add(comp, constraints, index);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void remove(Component comp)
/*     */   {
/* 420 */     getContentPane().remove(comp);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void remove(int index)
/*     */   {
/* 428 */     getContentPane().remove(index);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeAll()
/*     */   {
/* 436 */     getContentPane().removeAll();
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
/*     */   public void setAnimated(boolean animated)
/*     */   {
/* 458 */     if (animated != this.useAnimation) {
/* 459 */       this.useAnimation = animated;
/*     */       
/* 461 */       if (!animated) {
/* 462 */         if (this.animateTimer.isRunning())
/*     */         {
/*     */ 
/* 465 */           SwingUtilities.invokeLater(new Runnable()
/*     */           {
/*     */             public void run() {
/* 468 */               JXCollapsiblePane.this.currentDimension = -1;
/*     */             }
/*     */           });
/*     */         } else {
/* 472 */           this.currentDimension = -1;
/*     */         }
/*     */       }
/* 475 */       firePropertyChange("animated", !this.useAnimation, this.useAnimation);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAnimated()
/*     */   {
/* 484 */     return this.useAnimation;
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
/*     */   public void setDirection(Direction direction)
/*     */   {
/* 502 */     if (this.animateTimer.isRunning()) {
/* 503 */       throw new IllegalStateException("cannot be change direction while collapsing.");
/*     */     }
/*     */     
/* 506 */     Direction oldValue = getDirection();
/* 507 */     this.direction = direction;
/*     */     
/* 509 */     if (direction.isVertical()) {
/* 510 */       getContentPane().setLayout(new VerticalLayout(2));
/*     */     } else {
/* 512 */       getContentPane().setLayout(new HorizontalLayout(2));
/*     */     }
/*     */     
/* 515 */     firePropertyChange("direction", oldValue, getDirection());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Direction getDirection()
/*     */   {
/* 523 */     return this.direction;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isCollapsed()
/*     */   {
/* 530 */     return this.collapsed;
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
/*     */   public void setCollapsed(boolean val)
/*     */   {
/* 555 */     if (this.collapsed != val) {
/* 556 */       this.collapsed = val;
/* 557 */       if (isAnimated()) {
/* 558 */         if (this.collapsed) {
/* 559 */           int dimension = this.direction.isVertical() ? this.wrapper.getHeight() : this.wrapper.getWidth();
/*     */           
/* 561 */           setAnimationParams(new AnimationParams(30, Math.max(8, dimension / 10), 1.0F, 0.01F));
/*     */           
/* 563 */           this.animator.reinit(dimension, 0);
/* 564 */           this.animateTimer.start();
/*     */         } else {
/* 566 */           int dimension = this.direction.isVertical() ? this.wrapper.getHeight() : this.wrapper.getWidth();
/*     */           
/* 568 */           int preferredDimension = this.direction.isVertical() ? getContentPane().getPreferredSize().height : getContentPane().getPreferredSize().width;
/*     */           
/*     */ 
/* 571 */           int delta = Math.max(8, preferredDimension / 10);
/*     */           
/* 573 */           setAnimationParams(new AnimationParams(30, delta, 0.01F, 1.0F));
/* 574 */           this.animator.reinit(dimension, preferredDimension);
/* 575 */           this.wrapper.getView().setVisible(true);
/* 576 */           this.animateTimer.start();
/*     */         }
/*     */       } else {
/* 579 */         this.wrapper.collapsedState = this.collapsed;
/* 580 */         this.wrapper.getView().setVisible(!this.collapsed);
/* 581 */         revalidate();
/*     */       }
/* 583 */       repaint();
/* 584 */       firePropertyChange("collapsed", !this.collapsed, this.collapsed);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Border getBorder()
/*     */   {
/* 593 */     if ((getContentPane() instanceof JComponent)) {
/* 594 */       return ((JComponent)getContentPane()).getBorder();
/*     */     }
/*     */     
/* 597 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBorder(Border border)
/*     */   {
/* 605 */     if ((getContentPane() instanceof JComponent)) {
/* 606 */       ((JComponent)getContentPane()).setBorder(border);
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
/*     */   public Dimension getMinimumSize()
/*     */   {
/* 621 */     return getPreferredSize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMinimumSize(Dimension minimumSize)
/*     */   {
/* 632 */     getContentPane().setMinimumSize(minimumSize);
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
/*     */   public Dimension getPreferredSize()
/*     */   {
/* 655 */     Dimension dim = getContentPane().getPreferredSize();
/* 656 */     if (this.currentDimension != -1) {
/* 657 */       if (this.direction.isVertical()) {
/* 658 */         dim.height = this.currentDimension;
/*     */       } else {
/* 660 */         dim.width = this.currentDimension;
/*     */       }
/* 662 */     } else if (this.wrapper.collapsedState) {
/* 663 */       if (this.direction.isVertical()) {
/* 664 */         dim.height = 0;
/*     */       } else {
/* 666 */         dim.width = 0;
/*     */       }
/*     */     }
/* 669 */     return dim;
/*     */   }
/*     */   
/*     */   public void setPreferredSize(Dimension preferredSize)
/*     */   {
/* 674 */     getContentPane().setPreferredSize(preferredSize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setAnimationParams(AnimationParams params)
/*     */   {
/* 685 */     if (params == null) { throw new IllegalArgumentException("params can't be null");
/*     */     }
/* 687 */     if (this.animateTimer != null) {
/* 688 */       this.animateTimer.stop();
/*     */     }
/* 690 */     this.animationParams = params;
/* 691 */     this.animateTimer = new Timer(this.animationParams.waitTime, this.animator);
/* 692 */     this.animateTimer.setInitialDelay(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static abstract interface CollapsiblePaneContainer
/*     */   {
/*     */     public abstract Container getValidatingContainer();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class AnimationParams
/*     */   {
/*     */     final int waitTime;
/*     */     
/*     */ 
/*     */ 
/*     */     final int delta;
/*     */     
/*     */ 
/*     */ 
/*     */     final float alphaStart;
/*     */     
/*     */ 
/*     */ 
/*     */     final float alphaEnd;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public AnimationParams(int waitTime, int delta, float alphaStart, float alphaEnd)
/*     */     {
/* 729 */       this.waitTime = waitTime;
/* 730 */       this.delta = delta;
/* 731 */       this.alphaStart = alphaStart;
/* 732 */       this.alphaEnd = alphaEnd;
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
/*     */   private final class AnimationListener
/*     */     implements ActionListener
/*     */   {
/* 750 */     private final Object ANIMATION_MUTEX = "Animation Synchronization Mutex";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 756 */     private int startDimension = 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 761 */     private int finalDimension = 0;
/*     */     
/*     */ 
/*     */ 
/* 765 */     private float animateAlpha = 1.0F;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private AnimationListener() {}
/*     */     
/*     */ 
/*     */ 
/*     */     public void actionPerformed(ActionEvent e)
/*     */     {
/* 776 */       synchronized (this.ANIMATION_MUTEX) {
/* 777 */         if (this.startDimension == this.finalDimension) {
/* 778 */           JXCollapsiblePane.this.animateTimer.stop();
/* 779 */           this.animateAlpha = JXCollapsiblePane.this.animationParams.alphaEnd;
/*     */           
/*     */ 
/* 782 */           if (this.finalDimension > 0) {
/* 783 */             JXCollapsiblePane.this.currentDimension = -1;
/* 784 */             JXCollapsiblePane.this.wrapper.collapsedState = false;
/* 785 */             validate();
/* 786 */             JXCollapsiblePane.this.firePropertyChange("animationState", null, "expanded");
/*     */             
/* 788 */             return;
/*     */           }
/* 790 */           JXCollapsiblePane.this.wrapper.collapsedState = true;
/* 791 */           JXCollapsiblePane.this.wrapper.getView().setVisible(false);
/* 792 */           JXCollapsiblePane.this.firePropertyChange("animationState", null, "collapsed");
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 797 */         boolean contracting = this.startDimension > this.finalDimension;
/* 798 */         int delta = contracting ? -1 * JXCollapsiblePane.this.animationParams.delta : JXCollapsiblePane.this.animationParams.delta;
/*     */         int newDimension;
/*     */         int newDimension;
/* 801 */         if (JXCollapsiblePane.this.direction.isVertical()) {
/* 802 */           newDimension = JXCollapsiblePane.this.wrapper.getHeight() + delta;
/*     */         } else {
/* 804 */           newDimension = JXCollapsiblePane.this.wrapper.getWidth() + delta;
/*     */         }
/* 806 */         if (contracting) {
/* 807 */           if (newDimension < this.finalDimension) {
/* 808 */             newDimension = this.finalDimension;
/*     */           }
/*     */         }
/* 811 */         else if (newDimension > this.finalDimension) {
/* 812 */           newDimension = this.finalDimension;
/*     */         }
/*     */         int dimension;
/*     */         int dimension;
/* 816 */         if (JXCollapsiblePane.this.direction.isVertical()) {
/* 817 */           dimension = JXCollapsiblePane.this.wrapper.getView().getPreferredSize().height;
/*     */         } else {
/* 819 */           dimension = JXCollapsiblePane.this.wrapper.getView().getPreferredSize().width;
/*     */         }
/* 821 */         this.animateAlpha = (newDimension / dimension);
/*     */         
/* 823 */         Rectangle bounds = JXCollapsiblePane.this.wrapper.getBounds();
/*     */         
/* 825 */         if (JXCollapsiblePane.this.direction.isVertical()) {
/* 826 */           int oldHeight = bounds.height;
/* 827 */           bounds.height = newDimension;
/* 828 */           JXCollapsiblePane.this.wrapper.setBounds(bounds);
/*     */           
/* 830 */           if (JXCollapsiblePane.this.direction == JXCollapsiblePane.Direction.DOWN) {
/* 831 */             JXCollapsiblePane.this.wrapper.setViewPosition(new Point(0, JXCollapsiblePane.this.wrapper.getView().getPreferredSize().height - newDimension));
/*     */           } else {
/* 833 */             JXCollapsiblePane.this.wrapper.setViewPosition(new Point(0, newDimension));
/*     */           }
/*     */           
/* 836 */           bounds = JXCollapsiblePane.this.getBounds();
/* 837 */           bounds.height = (bounds.height - oldHeight + newDimension);
/* 838 */           JXCollapsiblePane.this.currentDimension = bounds.height;
/*     */         } else {
/* 840 */           int oldWidth = bounds.width;
/* 841 */           bounds.width = newDimension;
/* 842 */           JXCollapsiblePane.this.wrapper.setBounds(bounds);
/*     */           
/* 844 */           if (JXCollapsiblePane.this.direction == JXCollapsiblePane.Direction.RIGHT) {
/* 845 */             JXCollapsiblePane.this.wrapper.setViewPosition(new Point(JXCollapsiblePane.this.wrapper.getView().getPreferredSize().width - newDimension, 0));
/*     */           } else {
/* 847 */             JXCollapsiblePane.this.wrapper.setViewPosition(new Point(newDimension, 0));
/*     */           }
/*     */           
/* 850 */           bounds = JXCollapsiblePane.this.getBounds();
/* 851 */           bounds.width = (bounds.width - oldWidth + newDimension);
/* 852 */           JXCollapsiblePane.this.currentDimension = bounds.width;
/*     */         }
/*     */         
/* 855 */         JXCollapsiblePane.this.setBounds(bounds);
/* 856 */         this.startDimension = newDimension;
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 861 */         if (contracting)
/*     */         {
/* 863 */           if (this.animateAlpha < JXCollapsiblePane.this.animationParams.alphaEnd) {
/* 864 */             this.animateAlpha = JXCollapsiblePane.this.animationParams.alphaEnd;
/*     */           }
/* 866 */           if (this.animateAlpha > JXCollapsiblePane.this.animationParams.alphaStart) {
/* 867 */             this.animateAlpha = JXCollapsiblePane.this.animationParams.alphaStart;
/*     */           }
/*     */         }
/*     */         else {
/* 871 */           if (this.animateAlpha > JXCollapsiblePane.this.animationParams.alphaEnd) {
/* 872 */             this.animateAlpha = JXCollapsiblePane.this.animationParams.alphaEnd;
/*     */           }
/* 874 */           if (this.animateAlpha < JXCollapsiblePane.this.animationParams.alphaStart) {
/* 875 */             this.animateAlpha = JXCollapsiblePane.this.animationParams.alphaStart;
/*     */           }
/*     */         }
/* 878 */         JXCollapsiblePane.this.wrapper.alpha = this.animateAlpha;
/*     */         
/* 880 */         validate();
/*     */       }
/*     */     }
/*     */     
/*     */     void validate() {
/* 885 */       Container parent = SwingUtilities.getAncestorOfClass(JXCollapsiblePane.CollapsiblePaneContainer.class, JXCollapsiblePane.this);
/*     */       
/* 887 */       if (parent != null) {
/* 888 */         parent = ((JXCollapsiblePane.CollapsiblePaneContainer)parent).getValidatingContainer();
/*     */       } else {
/* 890 */         parent = JXCollapsiblePane.this.getParent();
/*     */       }
/*     */       
/* 893 */       if (parent != null) {
/* 894 */         if ((parent instanceof JComponent)) {
/* 895 */           ((JComponent)parent).revalidate();
/*     */         } else {
/* 897 */           parent.invalidate();
/*     */         }
/* 899 */         parent.doLayout();
/* 900 */         parent.repaint();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void reinit(int startDimension, int stopDimension)
/*     */     {
/* 913 */       synchronized (this.ANIMATION_MUTEX) {
/* 914 */         JXCollapsiblePane.this.firePropertyChange("animationState", null, "reinit");
/*     */         
/* 916 */         this.startDimension = startDimension;
/* 917 */         this.finalDimension = stopDimension;
/* 918 */         this.animateAlpha = JXCollapsiblePane.this.animationParams.alphaStart;
/* 919 */         JXCollapsiblePane.this.currentDimension = -1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final class WrapperContainer extends JViewport {
/*     */     float alpha;
/*     */     boolean collapsedState;
/*     */     
/*     */     public WrapperContainer(Container c) {
/* 929 */       this.alpha = 1.0F;
/* 930 */       this.collapsedState = false;
/* 931 */       setView(c);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 936 */       if (((c instanceof JComponent)) && (!c.isOpaque())) {
/* 937 */         ((JComponent)c).setOpaque(true);
/*     */       }
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
/*     */     public void scrollRectToVisible(Rectangle aRect)
/*     */     {
/* 952 */       JXCollapsiblePane.this.scrollRectToVisible(aRect);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXCollapsiblePane.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */