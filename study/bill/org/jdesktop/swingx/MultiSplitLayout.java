/*      */ package org.jdesktop.swingx;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Rectangle;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.beans.PropertyChangeSupport;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Reader;
/*      */ import java.io.StreamTokenizer;
/*      */ import java.io.StringReader;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import javax.swing.UIManager;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MultiSplitLayout
/*      */   implements LayoutManager
/*      */ {
/*      */   public static final int DEFAULT_LAYOUT = 0;
/*      */   public static final int NO_MIN_SIZE_LAYOUT = 1;
/*      */   public static final int USER_MIN_SIZE_LAYOUT = 2;
/*   87 */   private final Map<String, Component> childMap = new HashMap();
/*   88 */   private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
/*      */   private Node model;
/*      */   private int dividerSize;
/*   91 */   private boolean floatingDividers = true;
/*      */   
/*   93 */   private boolean removeDividers = true;
/*   94 */   private boolean layoutByWeight = false;
/*      */   
/*      */   private int layoutMode;
/*   97 */   private int userMinSize = 20;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MultiSplitLayout()
/*      */   {
/*  107 */     this(new Leaf("default"));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MultiSplitLayout(boolean layoutByWeight)
/*      */   {
/*  120 */     this(new Leaf("default"));
/*  121 */     this.layoutByWeight = layoutByWeight;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void layoutByWeight(Container parent)
/*      */   {
/*  131 */     doLayoutByWeight(parent);
/*      */     
/*  133 */     layoutContainer(parent);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void doLayoutByWeight(Container parent)
/*      */   {
/*  143 */     Dimension size = parent.getSize();
/*  144 */     Insets insets = parent.getInsets();
/*  145 */     int width = size.width - (insets.left + insets.right);
/*  146 */     int height = size.height - (insets.top + insets.bottom);
/*  147 */     Rectangle bounds = new Rectangle(insets.left, insets.top, width, height);
/*      */     
/*  149 */     if ((this.model instanceof Leaf)) {
/*  150 */       this.model.setBounds(bounds);
/*  151 */     } else if ((this.model instanceof Split)) {
/*  152 */       doLayoutByWeight(this.model, bounds);
/*      */     }
/*      */   }
/*      */   
/*      */   private void doLayoutByWeight(Node node, Rectangle bounds) {
/*  157 */     int width = bounds.width;
/*  158 */     int height = bounds.height;
/*  159 */     Split split = (Split)node;
/*  160 */     List<Node> splitChildren = split.getChildren();
/*  161 */     double distributableWeight = 1.0D;
/*  162 */     int unweightedComponents = 0;
/*  163 */     int dividerSpace = 0;
/*  164 */     for (Node splitChild : splitChildren)
/*  165 */       if (splitChild.isVisible())
/*      */       {
/*  167 */         if ((splitChild instanceof Divider)) {
/*  168 */           dividerSpace += this.dividerSize;
/*      */         }
/*      */         else
/*      */         {
/*  172 */           double weight = splitChild.getWeight();
/*  173 */           if (weight > 0.0D) {
/*  174 */             distributableWeight -= weight;
/*      */           } else
/*  176 */             unweightedComponents++; } }
/*      */     double distributableWidth;
/*      */     double distributableHeight;
/*  179 */     if (split.isRowLayout()) {
/*  180 */       width -= dividerSpace;
/*  181 */       distributableWidth = width * distributableWeight;
/*  182 */       for (Node splitChild : splitChildren) {
/*  183 */         if ((splitChild.isVisible()) && (!(splitChild instanceof Divider)))
/*      */         {
/*      */ 
/*  186 */           double weight = splitChild.getWeight();
/*  187 */           Rectangle splitChildBounds = splitChild.getBounds();
/*  188 */           if (weight >= 0.0D) {
/*  189 */             splitChildBounds = new Rectangle(splitChildBounds.x, splitChildBounds.y, (int)(width * weight), height);
/*      */           } else {
/*  191 */             splitChildBounds = new Rectangle(splitChildBounds.x, splitChildBounds.y, (int)(distributableWidth / unweightedComponents), height);
/*      */           }
/*  193 */           if (this.layoutMode == 2) {
/*  194 */             splitChildBounds.setSize(Math.max(splitChildBounds.width, this.userMinSize), splitChildBounds.height);
/*      */           }
/*      */           
/*  197 */           splitChild.setBounds(splitChildBounds);
/*      */           
/*  199 */           if ((splitChild instanceof Split)) {
/*  200 */             doLayoutByWeight(splitChild, splitChildBounds);
/*      */           } else {
/*  202 */             Component comp = getComponentForNode(splitChild);
/*  203 */             if (comp != null)
/*  204 */               comp.setPreferredSize(splitChildBounds.getSize());
/*      */           }
/*      */         }
/*      */       }
/*      */     } else {
/*  209 */       height -= dividerSpace;
/*  210 */       distributableHeight = height * distributableWeight;
/*  211 */       for (Node splitChild : splitChildren) {
/*  212 */         if ((splitChild.isVisible()) && (!(splitChild instanceof Divider)))
/*      */         {
/*      */ 
/*  215 */           double weight = splitChild.getWeight();
/*  216 */           Rectangle splitChildBounds = splitChild.getBounds();
/*  217 */           if (weight >= 0.0D) {
/*  218 */             splitChildBounds = new Rectangle(splitChildBounds.x, splitChildBounds.y, width, (int)(height * weight));
/*      */           } else {
/*  220 */             splitChildBounds = new Rectangle(splitChildBounds.x, splitChildBounds.y, width, (int)(distributableHeight / unweightedComponents));
/*      */           }
/*  222 */           if (this.layoutMode == 2) {
/*  223 */             splitChildBounds.setSize(splitChildBounds.width, Math.max(splitChildBounds.height, this.userMinSize));
/*      */           }
/*      */           
/*  226 */           splitChild.setBounds(splitChildBounds);
/*      */           
/*  228 */           if ((splitChild instanceof Split)) {
/*  229 */             doLayoutByWeight(splitChild, splitChildBounds);
/*      */           } else {
/*  231 */             Component comp = getComponentForNode(splitChild);
/*  232 */             if (comp != null) {
/*  233 */               comp.setPreferredSize(splitChildBounds.getSize());
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Component getComponentForNode(Node n)
/*      */   {
/*  246 */     String name = ((Leaf)n).getName();
/*  247 */     return name != null ? (Component)this.childMap.get(name) : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Node getNodeForComponent(Component comp)
/*      */   {
/*  257 */     return getNodeForName(getNameForComponent(comp));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Node getNodeForName(String name)
/*      */   {
/*  267 */     if ((this.model instanceof Split)) {
/*  268 */       Split split = (Split)this.model;
/*  269 */       return getNodeForName(split, name);
/*      */     }
/*      */     
/*  272 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getNameForComponent(Component child)
/*      */   {
/*  282 */     String name = null;
/*  283 */     for (Map.Entry<String, Component> kv : this.childMap.entrySet()) {
/*  284 */       if (kv.getValue() == child) {
/*  285 */         name = (String)kv.getKey();
/*  286 */         break;
/*      */       }
/*      */     }
/*      */     
/*  290 */     return name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Node getNodeForComponent(Split split, Component comp)
/*      */   {
/*  301 */     return getNodeForName(split, getNameForComponent(comp));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Node getNodeForName(Split split, String name)
/*      */   {
/*  312 */     for (Node n : split.getChildren()) {
/*  313 */       if ((n instanceof Leaf)) {
/*  314 */         if (((Leaf)n).getName().equals(name)) {
/*  315 */           return n;
/*      */         }
/*  317 */       } else if ((n instanceof Split)) {
/*  318 */         Node n1 = getNodeForName((Split)n, name);
/*  319 */         if (n1 != null)
/*  320 */           return n1;
/*      */       }
/*      */     }
/*  323 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasModel()
/*      */   {
/*  332 */     return this.model != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MultiSplitLayout(Node model)
/*      */   {
/*  341 */     this.model = model;
/*  342 */     this.dividerSize = UIManager.getInt("SplitPane.dividerSize");
/*  343 */     if (this.dividerSize == 0) {
/*  344 */       this.dividerSize = 7;
/*      */     }
/*      */   }
/*      */   
/*      */   public void addPropertyChangeListener(PropertyChangeListener listener) {
/*  349 */     if (listener != null)
/*  350 */       this.pcs.addPropertyChangeListener(listener);
/*      */   }
/*      */   
/*      */   public void removePropertyChangeListener(PropertyChangeListener listener) {
/*  354 */     if (listener != null)
/*  355 */       this.pcs.removePropertyChangeListener(listener);
/*      */   }
/*      */   
/*      */   public PropertyChangeListener[] getPropertyChangeListeners() {
/*  359 */     return this.pcs.getPropertyChangeListeners();
/*      */   }
/*      */   
/*      */   private void firePCS(String propertyName, Object oldValue, Object newValue) {
/*  363 */     if ((oldValue == null) || (newValue == null) || (!oldValue.equals(newValue))) {
/*  364 */       this.pcs.firePropertyChange(propertyName, oldValue, newValue);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Node getModel()
/*      */   {
/*  375 */     return this.model;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setModel(Node model)
/*      */   {
/*  388 */     if ((model == null) || ((model instanceof Divider))) {
/*  389 */       throw new IllegalArgumentException("invalid model");
/*      */     }
/*  391 */     Node oldModel = getModel();
/*  392 */     this.model = model;
/*  393 */     firePCS("model", oldModel, getModel());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getDividerSize()
/*      */   {
/*  403 */     return this.dividerSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDividerSize(int dividerSize)
/*      */   {
/*  415 */     if (dividerSize < 0) {
/*  416 */       throw new IllegalArgumentException("invalid dividerSize");
/*      */     }
/*  418 */     int oldDividerSize = this.dividerSize;
/*  419 */     this.dividerSize = dividerSize;
/*  420 */     firePCS("dividerSize", new Integer(oldDividerSize), new Integer(dividerSize));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getFloatingDividers()
/*      */   {
/*  427 */     return this.floatingDividers;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFloatingDividers(boolean floatingDividers)
/*      */   {
/*  441 */     boolean oldFloatingDividers = this.floatingDividers;
/*  442 */     this.floatingDividers = floatingDividers;
/*  443 */     firePCS("floatingDividers", new Boolean(oldFloatingDividers), new Boolean(floatingDividers));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getRemoveDividers()
/*      */   {
/*  450 */     return this.removeDividers;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRemoveDividers(boolean removeDividers)
/*      */   {
/*  461 */     boolean oldRemoveDividers = this.removeDividers;
/*  462 */     this.removeDividers = removeDividers;
/*  463 */     firePCS("removeDividers", new Boolean(oldRemoveDividers), new Boolean(removeDividers));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addLayoutComponent(String name, Component child)
/*      */   {
/*  481 */     if (name == null) {
/*  482 */       throw new IllegalArgumentException("name not specified");
/*      */     }
/*  484 */     this.childMap.put(name, child);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeLayoutComponent(Component child)
/*      */   {
/*  494 */     String name = getNameForComponent(child);
/*      */     
/*  496 */     if (name != null) {
/*  497 */       this.childMap.remove(name);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeLayoutNode(String name)
/*      */   {
/*  509 */     if (name != null) { Node n;
/*      */       Node n;
/*  511 */       if (!(this.model instanceof Split)) {
/*  512 */         n = this.model;
/*      */       } else {
/*  514 */         n = getNodeForName(name);
/*      */       }
/*  516 */       this.childMap.remove(name);
/*      */       
/*  518 */       if (n != null) {
/*  519 */         Split s = n.getParent();
/*  520 */         s.remove(n);
/*  521 */         if (this.removeDividers) {
/*  522 */           while (s.getChildren().size() < 2) {
/*  523 */             Split p = s.getParent();
/*  524 */             if (p == null) {
/*  525 */               if (s.getChildren().size() > 0) {
/*  526 */                 this.model = ((Node)s.getChildren().get(0));
/*      */               } else
/*  528 */                 this.model = null;
/*  529 */               return;
/*      */             }
/*  531 */             if (s.getChildren().size() == 1) {
/*  532 */               Node next = (Node)s.getChildren().get(0);
/*  533 */               p.replace(s, next);
/*  534 */               next.setParent(p);
/*      */             }
/*      */             else {
/*  537 */               p.remove(s); }
/*  538 */             s = p;
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/*  543 */         this.childMap.remove(name);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void displayNode(String name, boolean visible)
/*      */   {
/*  557 */     Node node = getNodeForName(name);
/*  558 */     if (node != null) {
/*  559 */       Component comp = getComponentForNode(node);
/*  560 */       comp.setVisible(visible);
/*  561 */       node.setVisible(visible);
/*      */       
/*  563 */       Split p = node.getParent();
/*  564 */       if (!visible) {
/*  565 */         p.hide(node);
/*  566 */         if (!p.isVisible()) {
/*  567 */           p.getParent().hide(p);
/*      */         }
/*  569 */         p.checkDividers(p);
/*      */         
/*      */ 
/*  572 */         while (!p.isVisible()) {
/*  573 */           p = p.getParent();
/*  574 */           if (p == null) break;
/*  575 */           p.checkDividers(p);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  581 */       p.restoreDividers(p);
/*      */     }
/*  583 */     setFloatingDividers(false);
/*      */   }
/*      */   
/*      */   private Component childForNode(Node node) {
/*  587 */     if ((node instanceof Leaf)) {
/*  588 */       Leaf leaf = (Leaf)node;
/*  589 */       String name = leaf.getName();
/*  590 */       return name != null ? (Component)this.childMap.get(name) : null;
/*      */     }
/*  592 */     return null;
/*      */   }
/*      */   
/*      */   private Dimension preferredComponentSize(Node node)
/*      */   {
/*  597 */     if (this.layoutMode == 1) {
/*  598 */       return new Dimension(0, 0);
/*      */     }
/*  600 */     Component child = childForNode(node);
/*  601 */     return (child != null) && (child.isVisible()) ? child.getPreferredSize() : new Dimension(0, 0);
/*      */   }
/*      */   
/*      */   private Dimension minimumComponentSize(Node node) {
/*  605 */     if (this.layoutMode == 1) {
/*  606 */       return new Dimension(0, 0);
/*      */     }
/*  608 */     Component child = childForNode(node);
/*  609 */     return (child != null) && (child.isVisible()) ? child.getMinimumSize() : new Dimension(0, 0);
/*      */   }
/*      */   
/*      */   private Dimension preferredNodeSize(Node root) {
/*  613 */     if ((root instanceof Leaf)) {
/*  614 */       return preferredComponentSize(root);
/*      */     }
/*  616 */     if ((root instanceof Divider)) {
/*  617 */       if (!((Divider)root).isVisible())
/*  618 */         return new Dimension(0, 0);
/*  619 */       int divSize = getDividerSize();
/*  620 */       return new Dimension(divSize, divSize);
/*      */     }
/*      */     
/*  623 */     Split split = (Split)root;
/*  624 */     List<Node> splitChildren = split.getChildren();
/*  625 */     int width = 0;
/*  626 */     int height = 0;
/*  627 */     if (split.isRowLayout()) {
/*  628 */       for (Node splitChild : splitChildren) {
/*  629 */         if (splitChild.isVisible())
/*      */         {
/*  631 */           Dimension size = preferredNodeSize(splitChild);
/*  632 */           width += size.width;
/*  633 */           height = Math.max(height, size.height);
/*      */         }
/*      */       }
/*      */     } else {
/*  637 */       for (Node splitChild : splitChildren)
/*  638 */         if (splitChild.isVisible())
/*      */         {
/*  640 */           Dimension size = preferredNodeSize(splitChild);
/*  641 */           width = Math.max(width, size.width);
/*  642 */           height += size.height;
/*      */         }
/*      */     }
/*  645 */     return new Dimension(width, height);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Dimension minimumNodeSize(Node root)
/*      */   {
/*  657 */     assert (root.isVisible);
/*  658 */     if ((root instanceof Leaf)) {
/*  659 */       if (this.layoutMode == 1) {
/*  660 */         return new Dimension(0, 0);
/*      */       }
/*  662 */       Component child = childForNode(root);
/*  663 */       return (child != null) && (child.isVisible()) ? child.getMinimumSize() : new Dimension(0, 0);
/*      */     }
/*  665 */     if ((root instanceof Divider)) {
/*  666 */       if (!((Divider)root).isVisible())
/*  667 */         return new Dimension(0, 0);
/*  668 */       int divSize = getDividerSize();
/*  669 */       return new Dimension(divSize, divSize);
/*      */     }
/*      */     
/*  672 */     Split split = (Split)root;
/*  673 */     List<Node> splitChildren = split.getChildren();
/*  674 */     int width = 0;
/*  675 */     int height = 0;
/*  676 */     if (split.isRowLayout()) {
/*  677 */       for (Node splitChild : splitChildren) {
/*  678 */         if (splitChild.isVisible())
/*      */         {
/*  680 */           Dimension size = minimumNodeSize(splitChild);
/*  681 */           width += size.width;
/*  682 */           height = Math.max(height, size.height);
/*      */         }
/*      */       }
/*      */     } else {
/*  686 */       for (Node splitChild : splitChildren)
/*  687 */         if (splitChild.isVisible())
/*      */         {
/*  689 */           Dimension size = minimumNodeSize(splitChild);
/*  690 */           width = Math.max(width, size.width);
/*  691 */           height += size.height;
/*      */         }
/*      */     }
/*  694 */     return new Dimension(width, height);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Dimension maximumNodeSize(Node root)
/*      */   {
/*  706 */     assert (root.isVisible);
/*  707 */     if ((root instanceof Leaf)) {
/*  708 */       Component child = childForNode(root);
/*  709 */       return (child != null) && (child.isVisible()) ? child.getMaximumSize() : new Dimension(0, 0);
/*      */     }
/*  711 */     if ((root instanceof Divider)) {
/*  712 */       if (!((Divider)root).isVisible())
/*  713 */         return new Dimension(0, 0);
/*  714 */       int divSize = getDividerSize();
/*  715 */       return new Dimension(divSize, divSize);
/*      */     }
/*      */     
/*  718 */     Split split = (Split)root;
/*  719 */     List<Node> splitChildren = split.getChildren();
/*  720 */     int width = Integer.MAX_VALUE;
/*  721 */     int height = Integer.MAX_VALUE;
/*  722 */     if (split.isRowLayout()) {
/*  723 */       for (Node splitChild : splitChildren) {
/*  724 */         if (splitChild.isVisible())
/*      */         {
/*  726 */           Dimension size = maximumNodeSize(splitChild);
/*  727 */           width += size.width;
/*  728 */           height = Math.min(height, size.height);
/*      */         }
/*      */       }
/*      */     } else {
/*  732 */       for (Node splitChild : splitChildren)
/*  733 */         if (splitChild.isVisible())
/*      */         {
/*  735 */           Dimension size = maximumNodeSize(splitChild);
/*  736 */           width = Math.min(width, size.width);
/*  737 */           height += size.height;
/*      */         }
/*      */     }
/*  740 */     return new Dimension(width, height);
/*      */   }
/*      */   
/*      */   private Dimension sizeWithInsets(Container parent, Dimension size)
/*      */   {
/*  745 */     Insets insets = parent.getInsets();
/*  746 */     int width = size.width + insets.left + insets.right;
/*  747 */     int height = size.height + insets.top + insets.bottom;
/*  748 */     return new Dimension(width, height);
/*      */   }
/*      */   
/*      */   public Dimension preferredLayoutSize(Container parent) {
/*  752 */     Dimension size = preferredNodeSize(getModel());
/*  753 */     return sizeWithInsets(parent, size);
/*      */   }
/*      */   
/*      */   public Dimension minimumLayoutSize(Container parent) {
/*  757 */     Dimension size = minimumNodeSize(getModel());
/*  758 */     return sizeWithInsets(parent, size);
/*      */   }
/*      */   
/*      */   private Rectangle boundsWithYandHeight(Rectangle bounds, double y, double height)
/*      */   {
/*  763 */     Rectangle r = new Rectangle();
/*  764 */     r.setBounds((int)bounds.getX(), (int)y, (int)bounds.getWidth(), (int)height);
/*  765 */     return r;
/*      */   }
/*      */   
/*      */   private Rectangle boundsWithXandWidth(Rectangle bounds, double x, double width) {
/*  769 */     Rectangle r = new Rectangle();
/*  770 */     r.setBounds((int)x, (int)bounds.getY(), (int)width, (int)bounds.getHeight());
/*  771 */     return r;
/*      */   }
/*      */   
/*      */   private void minimizeSplitBounds(Split split, Rectangle bounds)
/*      */   {
/*  776 */     assert (split.isVisible());
/*  777 */     Rectangle splitBounds = new Rectangle(bounds.x, bounds.y, 0, 0);
/*  778 */     List<Node> splitChildren = split.getChildren();
/*  779 */     Node lastChild = null;
/*  780 */     int lastVisibleChildIdx = splitChildren.size();
/*      */     do {
/*  782 */       lastVisibleChildIdx--;
/*  783 */       lastChild = (Node)splitChildren.get(lastVisibleChildIdx);
/*  784 */     } while ((lastVisibleChildIdx > 0) && (!lastChild.isVisible()));
/*      */     
/*  786 */     if (!lastChild.isVisible())
/*  787 */       return;
/*  788 */     if (lastVisibleChildIdx >= 0) {
/*  789 */       Rectangle lastChildBounds = lastChild.getBounds();
/*  790 */       if (split.isRowLayout()) {
/*  791 */         int lastChildMaxX = lastChildBounds.x + lastChildBounds.width;
/*  792 */         splitBounds.add(lastChildMaxX, bounds.y + bounds.height);
/*      */       }
/*      */       else {
/*  795 */         int lastChildMaxY = lastChildBounds.y + lastChildBounds.height;
/*  796 */         splitBounds.add(bounds.x + bounds.width, lastChildMaxY);
/*      */       }
/*      */     }
/*  799 */     split.setBounds(splitBounds);
/*      */   }
/*      */   
/*      */   private void layoutShrink(Split split, Rectangle bounds)
/*      */   {
/*  804 */     Rectangle splitBounds = split.getBounds();
/*  805 */     ListIterator<Node> splitChildren = split.getChildren().listIterator();
/*  806 */     Node lastWeightedChild = split.lastWeightedChild();
/*      */     
/*  808 */     if (split.isRowLayout()) {
/*  809 */       int totalWidth = 0;
/*  810 */       int minWeightedWidth = 0;
/*  811 */       int totalWeightedWidth = 0;
/*  812 */       for (Node splitChild : split.getChildren()) {
/*  813 */         if (splitChild.isVisible())
/*      */         {
/*  815 */           int nodeWidth = splitChild.getBounds().width;
/*  816 */           int nodeMinWidth = 0;
/*  817 */           if ((this.layoutMode == 2) && (!(splitChild instanceof Divider))) {
/*  818 */             nodeMinWidth = this.userMinSize;
/*  819 */           } else if (this.layoutMode == 0)
/*  820 */             nodeMinWidth = Math.min(nodeWidth, minimumNodeSize(splitChild).width);
/*  821 */           totalWidth += nodeWidth;
/*  822 */           if (splitChild.getWeight() > 0.0D) {
/*  823 */             minWeightedWidth += nodeMinWidth;
/*  824 */             totalWeightedWidth += nodeWidth;
/*      */           }
/*      */         }
/*      */       }
/*  828 */       double x = bounds.getX();
/*  829 */       double extraWidth = splitBounds.getWidth() - bounds.getWidth();
/*  830 */       double availableWidth = extraWidth;
/*  831 */       boolean onlyShrinkWeightedComponents = totalWeightedWidth - minWeightedWidth > extraWidth;
/*      */       
/*      */ 
/*  834 */       while (splitChildren.hasNext()) {
/*  835 */         Node splitChild = (Node)splitChildren.next();
/*  836 */         if (!splitChild.isVisible()) {
/*  837 */           if (splitChildren.hasNext()) {
/*  838 */             splitChildren.next();
/*      */           }
/*      */         } else {
/*  841 */           Rectangle splitChildBounds = splitChild.getBounds();
/*  842 */           double minSplitChildWidth = 0.0D;
/*  843 */           if ((this.layoutMode == 2) && (!(splitChild instanceof Divider))) {
/*  844 */             minSplitChildWidth = this.userMinSize;
/*  845 */           } else if (this.layoutMode == 0)
/*  846 */             minSplitChildWidth = minimumNodeSize(splitChild).getWidth();
/*  847 */           double splitChildWeight = onlyShrinkWeightedComponents ? splitChild.getWeight() : splitChildBounds.getWidth() / totalWidth;
/*      */           
/*      */ 
/*      */ 
/*  851 */           if (!splitChildren.hasNext()) {
/*  852 */             double newWidth = Math.max(minSplitChildWidth, bounds.getMaxX() - x);
/*  853 */             Rectangle newSplitChildBounds = boundsWithXandWidth(bounds, x, newWidth);
/*  854 */             layout2(splitChild, newSplitChildBounds);
/*      */           }
/*  856 */           if (splitChild.isVisible()) {
/*  857 */             if ((availableWidth > 0.0D) && (splitChildWeight > 0.0D)) {
/*  858 */               double oldWidth = splitChildBounds.getWidth();
/*      */               double newWidth;
/*  860 */               double newWidth; if ((splitChild instanceof Divider)) {
/*  861 */                 newWidth = this.dividerSize;
/*      */               }
/*      */               else {
/*  864 */                 double allocatedWidth = Math.rint(splitChildWeight * extraWidth);
/*  865 */                 newWidth = Math.max(minSplitChildWidth, oldWidth - allocatedWidth);
/*      */               }
/*  867 */               Rectangle newSplitChildBounds = boundsWithXandWidth(bounds, x, newWidth);
/*  868 */               layout2(splitChild, newSplitChildBounds);
/*  869 */               availableWidth -= oldWidth - splitChild.getBounds().getWidth();
/*      */             }
/*      */             else {
/*  872 */               double existingWidth = splitChildBounds.getWidth();
/*  873 */               Rectangle newSplitChildBounds = boundsWithXandWidth(bounds, x, existingWidth);
/*  874 */               layout2(splitChild, newSplitChildBounds);
/*      */             }
/*  876 */             x = splitChild.getBounds().getMaxX();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/*  882 */       int totalHeight = 0;
/*  883 */       int minWeightedHeight = 0;
/*  884 */       int totalWeightedHeight = 0;
/*  885 */       for (Node splitChild : split.getChildren()) {
/*  886 */         if (splitChild.isVisible())
/*      */         {
/*  888 */           int nodeHeight = splitChild.getBounds().height;
/*  889 */           int nodeMinHeight = 0;
/*  890 */           if ((this.layoutMode == 2) && (!(splitChild instanceof Divider))) {
/*  891 */             nodeMinHeight = this.userMinSize;
/*  892 */           } else if (this.layoutMode == 0)
/*  893 */             nodeMinHeight = Math.min(nodeHeight, minimumNodeSize(splitChild).height);
/*  894 */           totalHeight += nodeHeight;
/*  895 */           if (splitChild.getWeight() > 0.0D) {
/*  896 */             minWeightedHeight += nodeMinHeight;
/*  897 */             totalWeightedHeight += nodeHeight;
/*      */           }
/*      */         }
/*      */       }
/*  901 */       double y = bounds.getY();
/*  902 */       double extraHeight = splitBounds.getHeight() - bounds.getHeight();
/*  903 */       double availableHeight = extraHeight;
/*  904 */       boolean onlyShrinkWeightedComponents = totalWeightedHeight - minWeightedHeight > extraHeight;
/*      */       
/*      */ 
/*  907 */       while (splitChildren.hasNext()) {
/*  908 */         Node splitChild = (Node)splitChildren.next();
/*  909 */         if (!splitChild.isVisible()) {
/*  910 */           if (splitChildren.hasNext()) {
/*  911 */             splitChildren.next();
/*      */           }
/*      */         } else {
/*  914 */           Rectangle splitChildBounds = splitChild.getBounds();
/*  915 */           double minSplitChildHeight = 0.0D;
/*  916 */           if ((this.layoutMode == 2) && (!(splitChild instanceof Divider))) {
/*  917 */             minSplitChildHeight = this.userMinSize;
/*  918 */           } else if (this.layoutMode == 0)
/*  919 */             minSplitChildHeight = minimumNodeSize(splitChild).getHeight();
/*  920 */           double splitChildWeight = onlyShrinkWeightedComponents ? splitChild.getWeight() : splitChildBounds.getHeight() / totalHeight;
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  926 */           if (!hasMoreVisibleSiblings(splitChild)) {
/*  927 */             double oldHeight = splitChildBounds.getHeight();
/*      */             double newHeight;
/*  929 */             double newHeight; if ((splitChild instanceof Divider)) {
/*  930 */               newHeight = this.dividerSize;
/*      */             }
/*      */             else {
/*  933 */               newHeight = Math.max(minSplitChildHeight, bounds.getMaxY() - y);
/*      */             }
/*  935 */             Rectangle newSplitChildBounds = boundsWithYandHeight(bounds, y, newHeight);
/*  936 */             layout2(splitChild, newSplitChildBounds);
/*  937 */             availableHeight -= oldHeight - splitChild.getBounds().getHeight();
/*      */ 
/*      */           }
/*  940 */           else if ((availableHeight > 0.0D) && (splitChildWeight > 0.0D))
/*      */           {
/*  942 */             double oldHeight = splitChildBounds.getHeight();
/*      */             double newHeight;
/*  944 */             double newHeight; if ((splitChild instanceof Divider)) {
/*  945 */               newHeight = this.dividerSize;
/*      */             }
/*      */             else {
/*  948 */               double allocatedHeight = Math.rint(splitChildWeight * extraHeight);
/*  949 */               newHeight = Math.max(minSplitChildHeight, oldHeight - allocatedHeight);
/*      */             }
/*  951 */             Rectangle newSplitChildBounds = boundsWithYandHeight(bounds, y, newHeight);
/*  952 */             layout2(splitChild, newSplitChildBounds);
/*  953 */             availableHeight -= oldHeight - splitChild.getBounds().getHeight();
/*      */           }
/*      */           else {
/*  956 */             double existingHeight = splitChildBounds.getHeight();
/*  957 */             Rectangle newSplitChildBounds = boundsWithYandHeight(bounds, y, existingHeight);
/*  958 */             layout2(splitChild, newSplitChildBounds);
/*      */           }
/*  960 */           y = splitChild.getBounds().getMaxY();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  971 */     minimizeSplitBounds(split, bounds);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean hasMoreVisibleSiblings(Node splitChild)
/*      */   {
/*  980 */     Node next = splitChild.nextSibling();
/*  981 */     if (next == null) {
/*  982 */       return false;
/*      */     }
/*      */     do {
/*  985 */       if (next.isVisible())
/*  986 */         return true;
/*  987 */       next = next.nextSibling();
/*  988 */     } while (next != null);
/*      */     
/*  990 */     return false;
/*      */   }
/*      */   
/*      */   private void layoutGrow(Split split, Rectangle bounds) {
/*  994 */     Rectangle splitBounds = split.getBounds();
/*  995 */     ListIterator<Node> splitChildren = split.getChildren().listIterator();
/*  996 */     Node lastWeightedChild = split.lastWeightedChild();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1007 */     if (split.isRowLayout()) {
/* 1008 */       double x = bounds.getX();
/* 1009 */       double extraWidth = bounds.getWidth() - splitBounds.getWidth();
/* 1010 */       double availableWidth = extraWidth;
/*      */       
/* 1012 */       while (splitChildren.hasNext()) {
/* 1013 */         Node splitChild = (Node)splitChildren.next();
/* 1014 */         if (splitChild.isVisible())
/*      */         {
/*      */ 
/* 1017 */           Rectangle splitChildBounds = splitChild.getBounds();
/* 1018 */           double splitChildWeight = splitChild.getWeight();
/*      */           
/* 1020 */           if (!hasMoreVisibleSiblings(splitChild)) {
/* 1021 */             double newWidth = bounds.getMaxX() - x;
/* 1022 */             Rectangle newSplitChildBounds = boundsWithXandWidth(bounds, x, newWidth);
/* 1023 */             layout2(splitChild, newSplitChildBounds);
/*      */           }
/* 1025 */           else if ((availableWidth > 0.0D) && (splitChildWeight > 0.0D)) {
/* 1026 */             double allocatedWidth = splitChild.equals(lastWeightedChild) ? availableWidth : Math.rint(splitChildWeight * extraWidth);
/*      */             
/*      */ 
/* 1029 */             double newWidth = splitChildBounds.getWidth() + allocatedWidth;
/* 1030 */             Rectangle newSplitChildBounds = boundsWithXandWidth(bounds, x, newWidth);
/* 1031 */             layout2(splitChild, newSplitChildBounds);
/* 1032 */             availableWidth -= allocatedWidth;
/*      */           }
/*      */           else {
/* 1035 */             double existingWidth = splitChildBounds.getWidth();
/* 1036 */             Rectangle newSplitChildBounds = boundsWithXandWidth(bounds, x, existingWidth);
/* 1037 */             layout2(splitChild, newSplitChildBounds);
/*      */           }
/* 1039 */           x = splitChild.getBounds().getMaxX();
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/* 1053 */       double y = bounds.getY();
/* 1054 */       double extraHeight = bounds.getHeight() - splitBounds.getHeight();
/* 1055 */       double availableHeight = extraHeight;
/*      */       
/* 1057 */       while (splitChildren.hasNext()) {
/* 1058 */         Node splitChild = (Node)splitChildren.next();
/* 1059 */         if (splitChild.isVisible())
/*      */         {
/*      */ 
/* 1062 */           Rectangle splitChildBounds = splitChild.getBounds();
/* 1063 */           double splitChildWeight = splitChild.getWeight();
/*      */           
/* 1065 */           if (!splitChildren.hasNext()) {
/* 1066 */             double newHeight = bounds.getMaxY() - y;
/* 1067 */             Rectangle newSplitChildBounds = boundsWithYandHeight(bounds, y, newHeight);
/* 1068 */             layout2(splitChild, newSplitChildBounds);
/*      */           }
/* 1070 */           else if ((availableHeight > 0.0D) && (splitChildWeight > 0.0D)) {
/* 1071 */             double allocatedHeight = splitChild.equals(lastWeightedChild) ? availableHeight : Math.rint(splitChildWeight * extraHeight);
/*      */             
/*      */ 
/* 1074 */             double newHeight = splitChildBounds.getHeight() + allocatedHeight;
/* 1075 */             Rectangle newSplitChildBounds = boundsWithYandHeight(bounds, y, newHeight);
/* 1076 */             layout2(splitChild, newSplitChildBounds);
/* 1077 */             availableHeight -= allocatedHeight;
/*      */           }
/*      */           else {
/* 1080 */             double existingHeight = splitChildBounds.getHeight();
/* 1081 */             Rectangle newSplitChildBounds = boundsWithYandHeight(bounds, y, existingHeight);
/* 1082 */             layout2(splitChild, newSplitChildBounds);
/*      */           }
/* 1084 */           y = splitChild.getBounds().getMaxY();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void layout2(Node root, Rectangle bounds)
/*      */   {
/* 1094 */     if ((root instanceof Leaf)) {
/* 1095 */       Component child = childForNode(root);
/* 1096 */       if (child != null) {
/* 1097 */         child.setBounds(bounds);
/*      */       }
/* 1099 */       root.setBounds(bounds);
/*      */     }
/* 1101 */     else if ((root instanceof Divider)) {
/* 1102 */       root.setBounds(bounds);
/*      */     }
/* 1104 */     else if ((root instanceof Split)) {
/* 1105 */       Split split = (Split)root;
/* 1106 */       boolean grow = split.getBounds().width <= bounds.width;
/*      */       
/*      */ 
/* 1109 */       if (grow) {
/* 1110 */         layoutGrow(split, bounds);
/* 1111 */         root.setBounds(bounds);
/*      */       }
/*      */       else {
/* 1114 */         layoutShrink(split, bounds);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void layout1(Node root, Rectangle bounds)
/*      */   {
/* 1136 */     if ((root instanceof Leaf)) {
/* 1137 */       root.setBounds(bounds);
/*      */     }
/* 1139 */     else if ((root instanceof Split)) {
/* 1140 */       Split split = (Split)root;
/* 1141 */       Iterator<Node> splitChildren = split.getChildren().iterator();
/* 1142 */       Rectangle childBounds = null;
/* 1143 */       int divSize = getDividerSize();
/* 1144 */       boolean initSplit = false;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1154 */       if (split.isRowLayout()) {
/* 1155 */         double x = bounds.getX();
/* 1156 */         while (splitChildren.hasNext()) {
/* 1157 */           Node splitChild = (Node)splitChildren.next();
/* 1158 */           if (!splitChild.isVisible()) {
/* 1159 */             if (splitChildren.hasNext()) {
/* 1160 */               splitChildren.next();
/*      */             }
/*      */           } else {
/* 1163 */             Divider dividerChild = splitChildren.hasNext() ? (Divider)splitChildren.next() : null;
/*      */             
/*      */ 
/* 1166 */             double childWidth = 0.0D;
/* 1167 */             if (getFloatingDividers()) {
/* 1168 */               childWidth = preferredNodeSize(splitChild).getWidth();
/*      */ 
/*      */             }
/* 1171 */             else if ((dividerChild != null) && (dividerChild.isVisible())) {
/* 1172 */               double cw = dividerChild.getBounds().getX() - x;
/* 1173 */               if (cw > 0.0D) {
/* 1174 */                 childWidth = cw;
/*      */               } else {
/* 1176 */                 childWidth = preferredNodeSize(splitChild).getWidth();
/* 1177 */                 initSplit = true;
/*      */               }
/*      */             }
/*      */             else {
/* 1181 */               childWidth = split.getBounds().getMaxX() - x;
/*      */             }
/*      */             
/* 1184 */             childBounds = boundsWithXandWidth(bounds, x, childWidth);
/* 1185 */             layout1(splitChild, childBounds);
/*      */             
/* 1187 */             if (((initSplit) || (getFloatingDividers())) && (dividerChild != null) && (dividerChild.isVisible())) {
/* 1188 */               double dividerX = childBounds.getMaxX();
/*      */               
/* 1190 */               Rectangle dividerBounds = boundsWithXandWidth(bounds, dividerX, divSize);
/* 1191 */               dividerChild.setBounds(dividerBounds);
/*      */             }
/* 1193 */             if ((dividerChild != null) && (dividerChild.isVisible())) {
/* 1194 */               x = dividerChild.getBounds().getMaxX();
/*      */             }
/*      */             
/*      */           }
/*      */           
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1205 */         double y = bounds.getY();
/* 1206 */         while (splitChildren.hasNext()) {
/* 1207 */           Node splitChild = (Node)splitChildren.next();
/* 1208 */           if (!splitChild.isVisible()) {
/* 1209 */             if (splitChildren.hasNext()) {
/* 1210 */               splitChildren.next();
/*      */             }
/*      */           } else {
/* 1213 */             Divider dividerChild = splitChildren.hasNext() ? (Divider)splitChildren.next() : null;
/*      */             
/*      */ 
/* 1216 */             double childHeight = 0.0D;
/* 1217 */             if (getFloatingDividers()) {
/* 1218 */               childHeight = preferredNodeSize(splitChild).getHeight();
/*      */ 
/*      */             }
/* 1221 */             else if ((dividerChild != null) && (dividerChild.isVisible())) {
/* 1222 */               double cy = dividerChild.getBounds().getY() - y;
/* 1223 */               if (cy > 0.0D) {
/* 1224 */                 childHeight = cy;
/*      */               } else {
/* 1226 */                 childHeight = preferredNodeSize(splitChild).getHeight();
/* 1227 */                 initSplit = true;
/*      */               }
/*      */             }
/*      */             else {
/* 1231 */               childHeight = split.getBounds().getMaxY() - y;
/*      */             }
/*      */             
/* 1234 */             childBounds = boundsWithYandHeight(bounds, y, childHeight);
/* 1235 */             layout1(splitChild, childBounds);
/*      */             
/* 1237 */             if (((initSplit) || (getFloatingDividers())) && (dividerChild != null) && (dividerChild.isVisible())) {
/* 1238 */               double dividerY = childBounds.getMaxY();
/* 1239 */               Rectangle dividerBounds = boundsWithYandHeight(bounds, dividerY, divSize);
/* 1240 */               dividerChild.setBounds(dividerBounds);
/*      */             }
/* 1242 */             if ((dividerChild != null) && (dividerChild.isVisible())) {
/* 1243 */               y = dividerChild.getBounds().getMaxY();
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1254 */       minimizeSplitBounds(split, bounds);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getLayoutMode()
/*      */   {
/* 1264 */     return this.layoutMode;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLayoutMode(int layoutMode)
/*      */   {
/* 1279 */     this.layoutMode = layoutMode;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getUserMinSize()
/*      */   {
/* 1288 */     return this.userMinSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUserMinSize(int minSize)
/*      */   {
/* 1298 */     this.userMinSize = minSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getLayoutByWeight()
/*      */   {
/* 1308 */     return this.layoutByWeight;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLayoutByWeight(boolean state)
/*      */   {
/* 1318 */     this.layoutByWeight = state;
/*      */   }
/*      */   
/*      */   public static class InvalidLayoutException
/*      */     extends RuntimeException
/*      */   {
/*      */     private final MultiSplitLayout.Node node;
/*      */     
/*      */     public InvalidLayoutException(String msg, MultiSplitLayout.Node node)
/*      */     {
/* 1328 */       super();
/* 1329 */       this.node = node;
/*      */     }
/*      */     
/*      */     public MultiSplitLayout.Node getNode()
/*      */     {
/* 1334 */       return this.node;
/*      */     }
/*      */   }
/*      */   
/* 1338 */   private void throwInvalidLayout(String msg, Node node) { throw new InvalidLayoutException(msg, node); }
/*      */   
/*      */   private void checkLayout(Node root)
/*      */   {
/* 1342 */     if ((root instanceof Split)) {
/* 1343 */       Split split = (Split)root;
/* 1344 */       if (split.getChildren().size() <= 2) {
/* 1345 */         throwInvalidLayout("Split must have > 2 children", root);
/*      */       }
/* 1347 */       Iterator<Node> splitChildren = split.getChildren().iterator();
/* 1348 */       double weight = 0.0D;
/* 1349 */       while (splitChildren.hasNext()) {
/* 1350 */         Node splitChild = (Node)splitChildren.next();
/* 1351 */         if (!splitChild.isVisible()) {
/* 1352 */           if (splitChildren.hasNext()) {
/* 1353 */             splitChildren.next();
/*      */           }
/*      */         }
/* 1356 */         else if (!(splitChild instanceof Divider))
/*      */         {
/*      */ 
/*      */ 
/* 1360 */           if (splitChildren.hasNext()) {
/* 1361 */             Node dividerChild = (Node)splitChildren.next();
/* 1362 */             if (!(dividerChild instanceof Divider)) {
/* 1363 */               throwInvalidLayout("expected a Divider Node", dividerChild);
/*      */             }
/*      */           }
/* 1366 */           weight += splitChild.getWeight();
/* 1367 */           checkLayout(splitChild);
/*      */         } }
/* 1369 */       if (weight > 1.0D) {
/* 1370 */         throwInvalidLayout("Split children's total weight > 1.0", root);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void layoutContainer(Container parent)
/*      */   {
/* 1382 */     if ((this.layoutByWeight) && (this.floatingDividers)) {
/* 1383 */       doLayoutByWeight(parent);
/*      */     }
/* 1385 */     checkLayout(getModel());
/* 1386 */     Insets insets = parent.getInsets();
/* 1387 */     Dimension size = parent.getSize();
/* 1388 */     int width = size.width - (insets.left + insets.right);
/* 1389 */     int height = size.height - (insets.top + insets.bottom);
/* 1390 */     Rectangle bounds = new Rectangle(insets.left, insets.top, width, height);
/* 1391 */     layout1(getModel(), bounds);
/* 1392 */     layout2(getModel(), bounds);
/*      */   }
/*      */   
/*      */   private Divider dividerAt(Node root, int x, int y)
/*      */   {
/* 1397 */     if ((root instanceof Divider)) {
/* 1398 */       Divider divider = (Divider)root;
/* 1399 */       return divider.getBounds().contains(x, y) ? divider : null;
/*      */     }
/* 1401 */     if ((root instanceof Split)) {
/* 1402 */       Split split = (Split)root;
/* 1403 */       for (Node child : split.getChildren()) {
/* 1404 */         if (child.isVisible())
/*      */         {
/* 1406 */           if (child.getBounds().contains(x, y))
/* 1407 */             return dividerAt(child, x, y);
/*      */         }
/*      */       }
/*      */     }
/* 1411 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Divider dividerAt(int x, int y)
/*      */   {
/* 1423 */     return dividerAt(getModel(), x, y);
/*      */   }
/*      */   
/*      */   private boolean nodeOverlapsRectangle(Node node, Rectangle r2) {
/* 1427 */     Rectangle r1 = node.getBounds();
/* 1428 */     return (r1.x <= r2.x + r2.width) && (r1.x + r1.width >= r2.x) && (r1.y <= r2.y + r2.height) && (r1.y + r1.height >= r2.y);
/*      */   }
/*      */   
/*      */ 
/*      */   private List<Divider> dividersThatOverlap(Node root, Rectangle r)
/*      */   {
/* 1434 */     if ((nodeOverlapsRectangle(root, r)) && ((root instanceof Split))) {
/* 1435 */       List<Divider> dividers = new ArrayList();
/* 1436 */       for (Node child : ((Split)root).getChildren()) {
/* 1437 */         if ((child instanceof Divider)) {
/* 1438 */           if (nodeOverlapsRectangle(child, r)) {
/* 1439 */             dividers.add((Divider)child);
/*      */           }
/*      */         }
/* 1442 */         else if ((child instanceof Split)) {
/* 1443 */           dividers.addAll(dividersThatOverlap(child, r));
/*      */         }
/*      */       }
/* 1446 */       return dividers;
/*      */     }
/*      */     
/* 1449 */     return Collections.emptyList();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Divider> dividersThatOverlap(Rectangle r)
/*      */   {
/* 1462 */     if (r == null) {
/* 1463 */       throw new IllegalArgumentException("null Rectangle");
/*      */     }
/* 1465 */     return dividersThatOverlap(getModel(), r);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static abstract class Node
/*      */   {
/* 1473 */     private MultiSplitLayout.Split parent = null;
/* 1474 */     private Rectangle bounds = new Rectangle();
/* 1475 */     private double weight = 0.0D;
/* 1476 */     private boolean isVisible = true;
/*      */     
/* 1478 */     public void setVisible(boolean b) { this.isVisible = b; }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isVisible()
/*      */     {
/* 1489 */       return this.isVisible;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public MultiSplitLayout.Split getParent()
/*      */     {
/* 1498 */       return this.parent;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setParent(MultiSplitLayout.Split parent)
/*      */     {
/* 1508 */       this.parent = parent;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Rectangle getBounds()
/*      */     {
/* 1518 */       return new Rectangle(this.bounds);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setBounds(Rectangle bounds)
/*      */     {
/* 1531 */       if (bounds == null) {
/* 1532 */         throw new IllegalArgumentException("null bounds");
/*      */       }
/* 1534 */       this.bounds = new Rectangle(bounds);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public double getWeight()
/*      */     {
/* 1545 */       return this.weight;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setWeight(double weight)
/*      */     {
/* 1562 */       if ((weight < 0.0D) || (weight > 1.0D)) {
/* 1563 */         throw new IllegalArgumentException("invalid weight");
/*      */       }
/* 1565 */       this.weight = weight;
/*      */     }
/*      */     
/*      */     private Node siblingAtOffset(int offset) {
/* 1569 */       MultiSplitLayout.Split p = getParent();
/* 1570 */       if (p == null) return null;
/* 1571 */       List<Node> siblings = p.getChildren();
/* 1572 */       int index = siblings.indexOf(this);
/* 1573 */       if (index == -1) return null;
/* 1574 */       index += offset;
/* 1575 */       return (index > -1) && (index < siblings.size()) ? (Node)siblings.get(index) : null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Node nextSibling()
/*      */     {
/* 1588 */       return siblingAtOffset(1);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Node previousSibling()
/*      */     {
/* 1601 */       return siblingAtOffset(-1);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class RowSplit extends MultiSplitLayout.Split
/*      */   {
/*      */     public RowSplit() {}
/*      */     
/*      */     public RowSplit(MultiSplitLayout.Node... children) {
/* 1610 */       setChildren(children);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public final boolean isRowLayout()
/*      */     {
/* 1623 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ColSplit extends MultiSplitLayout.Split {
/*      */     public ColSplit() {}
/*      */     
/*      */     public ColSplit(MultiSplitLayout.Node... children) {
/* 1631 */       setChildren(children);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public final boolean isRowLayout()
/*      */     {
/* 1644 */       return false;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Split
/*      */     extends MultiSplitLayout.Node
/*      */   {
/* 1652 */     private List<MultiSplitLayout.Node> children = Collections.emptyList();
/* 1653 */     private boolean rowLayout = true;
/*      */     private String name;
/*      */     
/*      */     public Split(MultiSplitLayout.Node... children) {
/* 1657 */       setChildren(children);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Split() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isVisible()
/*      */     {
/* 1676 */       for (MultiSplitLayout.Node child : this.children) {
/* 1677 */         if ((child.isVisible()) && (!(child instanceof MultiSplitLayout.Divider)))
/* 1678 */           return true;
/*      */       }
/* 1680 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isRowLayout()
/*      */     {
/* 1692 */       return this.rowLayout;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setRowLayout(boolean rowLayout)
/*      */     {
/* 1705 */       this.rowLayout = rowLayout;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public List<MultiSplitLayout.Node> getChildren()
/*      */     {
/* 1716 */       return new ArrayList(this.children);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void remove(MultiSplitLayout.Node n)
/*      */     {
/* 1725 */       if ((n.nextSibling() instanceof MultiSplitLayout.Divider)) {
/* 1726 */         this.children.remove(n.nextSibling());
/* 1727 */       } else if ((n.previousSibling() instanceof MultiSplitLayout.Divider))
/* 1728 */         this.children.remove(n.previousSibling());
/* 1729 */       this.children.remove(n);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void replace(MultiSplitLayout.Node target, MultiSplitLayout.Node replacement)
/*      */     {
/* 1741 */       int idx = this.children.indexOf(target);
/* 1742 */       this.children.remove(target);
/* 1743 */       this.children.add(idx, replacement);
/*      */       
/* 1745 */       replacement.setParent(this);
/* 1746 */       target.setParent(this);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void hide(MultiSplitLayout.Node target)
/*      */     {
/* 1754 */       MultiSplitLayout.Node next = target.nextSibling();
/* 1755 */       if ((next instanceof MultiSplitLayout.Divider)) {
/* 1756 */         next.setVisible(false);
/*      */       } else {
/* 1758 */         MultiSplitLayout.Node prev = target.previousSibling();
/* 1759 */         if ((prev instanceof MultiSplitLayout.Divider))
/* 1760 */           prev.setVisible(false);
/*      */       }
/* 1762 */       target.setVisible(false);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void checkDividers(Split split)
/*      */     {
/* 1773 */       ListIterator<MultiSplitLayout.Node> splitChildren = split.getChildren().listIterator();
/* 1774 */       while (splitChildren.hasNext()) {
/* 1775 */         MultiSplitLayout.Node splitChild = (MultiSplitLayout.Node)splitChildren.next();
/* 1776 */         if (splitChild.isVisible())
/*      */         {
/*      */ 
/* 1779 */           if (splitChildren.hasNext()) {
/* 1780 */             MultiSplitLayout.Node dividerChild = (MultiSplitLayout.Node)splitChildren.next();
/* 1781 */             if ((dividerChild instanceof MultiSplitLayout.Divider)) {
/* 1782 */               if (splitChildren.hasNext()) {
/* 1783 */                 MultiSplitLayout.Node rightChild = (MultiSplitLayout.Node)splitChildren.next();
/* 1784 */                 while (!rightChild.isVisible()) {
/* 1785 */                   rightChild = rightChild.nextSibling();
/* 1786 */                   if (rightChild == null)
/*      */                   {
/* 1788 */                     dividerChild.setVisible(false);
/*      */                   }
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/* 1795 */                 if ((rightChild != null) && ((rightChild instanceof MultiSplitLayout.Divider))) {
/* 1796 */                   dividerChild.setVisible(false);
/*      */                 }
/*      */               }
/* 1799 */             } else if (((splitChild instanceof MultiSplitLayout.Divider)) && ((dividerChild instanceof MultiSplitLayout.Divider))) {
/* 1800 */               splitChild.setVisible(false);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void restoreDividers(Split split)
/*      */     {
/* 1811 */       boolean nextDividerVisible = false;
/* 1812 */       ListIterator<MultiSplitLayout.Node> splitChildren = split.getChildren().listIterator();
/* 1813 */       while (splitChildren.hasNext()) {
/* 1814 */         MultiSplitLayout.Node splitChild = (MultiSplitLayout.Node)splitChildren.next();
/* 1815 */         if ((splitChild instanceof MultiSplitLayout.Divider)) {
/* 1816 */           MultiSplitLayout.Node prev = splitChild.previousSibling();
/* 1817 */           if (prev.isVisible()) {
/* 1818 */             MultiSplitLayout.Node next = splitChild.nextSibling();
/* 1819 */             while (next != null) {
/* 1820 */               if (next.isVisible()) {
/* 1821 */                 splitChild.setVisible(true);
/* 1822 */                 break;
/*      */               }
/* 1824 */               next = next.nextSibling();
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1829 */       if (split.getParent() != null) {
/* 1830 */         restoreDividers(split.getParent());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setChildren(List<MultiSplitLayout.Node> children)
/*      */     {
/* 1845 */       if (children == null) {
/* 1846 */         throw new IllegalArgumentException("children must be a non-null List");
/*      */       }
/* 1848 */       for (MultiSplitLayout.Node child : this.children) {
/* 1849 */         child.setParent(null);
/*      */       }
/*      */       
/* 1852 */       this.children = new ArrayList(children);
/* 1853 */       for (MultiSplitLayout.Node child : this.children) {
/* 1854 */         child.setParent(this);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setChildren(MultiSplitLayout.Node... children)
/*      */     {
/* 1869 */       setChildren(children == null ? null : Arrays.asList(children));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public final MultiSplitLayout.Node lastWeightedChild()
/*      */     {
/* 1881 */       List<MultiSplitLayout.Node> kids = getChildren();
/* 1882 */       MultiSplitLayout.Node weightedChild = null;
/* 1883 */       for (MultiSplitLayout.Node child : kids) {
/* 1884 */         if (child.isVisible())
/*      */         {
/* 1886 */           if (child.getWeight() > 0.0D)
/* 1887 */             weightedChild = child;
/*      */         }
/*      */       }
/* 1890 */       return weightedChild;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String getName()
/*      */     {
/* 1899 */       return this.name;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setName(String name)
/*      */     {
/* 1908 */       if (name == null) {
/* 1909 */         throw new IllegalArgumentException("name is null");
/*      */       }
/* 1911 */       this.name = name;
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 1916 */       int nChildren = getChildren().size();
/* 1917 */       StringBuffer sb = new StringBuffer("MultiSplitLayout.Split");
/* 1918 */       sb.append(" \"");
/* 1919 */       sb.append(getName());
/* 1920 */       sb.append("\"");
/* 1921 */       sb.append(isRowLayout() ? " ROW [" : " COLUMN [");
/* 1922 */       sb.append(nChildren + (nChildren == 1 ? " child" : " children"));
/* 1923 */       sb.append("] ");
/* 1924 */       sb.append(getBounds());
/* 1925 */       return sb.toString();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static class Leaf
/*      */     extends MultiSplitLayout.Node
/*      */   {
/* 1934 */     private String name = "";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Leaf() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Leaf(String name)
/*      */     {
/* 1950 */       if (name == null) {
/* 1951 */         throw new IllegalArgumentException("name is null");
/*      */       }
/* 1953 */       this.name = name;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String getName()
/*      */     {
/* 1962 */       return this.name;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setName(String name)
/*      */     {
/* 1971 */       if (name == null) {
/* 1972 */         throw new IllegalArgumentException("name is null");
/*      */       }
/* 1974 */       this.name = name;
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 1979 */       StringBuffer sb = new StringBuffer("MultiSplitLayout.Leaf");
/* 1980 */       sb.append(" \"");
/* 1981 */       sb.append(getName());
/* 1982 */       sb.append("\"");
/* 1983 */       sb.append(" weight=");
/* 1984 */       sb.append(getWeight());
/* 1985 */       sb.append(" ");
/* 1986 */       sb.append(getBounds());
/* 1987 */       return sb.toString();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class Divider
/*      */     extends MultiSplitLayout.Node
/*      */   {
/*      */     public final boolean isVertical()
/*      */     {
/* 2005 */       MultiSplitLayout.Split parent = getParent();
/* 2006 */       return parent != null ? parent.isRowLayout() : false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setWeight(double weight)
/*      */     {
/* 2015 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 2020 */       return "MultiSplitLayout.Divider " + getBounds().toString();
/*      */     }
/*      */   }
/*      */   
/*      */   private static void throwParseException(StreamTokenizer st, String msg) throws Exception
/*      */   {
/* 2026 */     throw new Exception("MultiSplitLayout.parseModel Error: " + msg);
/*      */   }
/*      */   
/*      */   private static void parseAttribute(String name, StreamTokenizer st, Node node) throws Exception {
/* 2030 */     if (st.nextToken() != 61) {
/* 2031 */       throwParseException(st, "expected '=' after " + name);
/*      */     }
/* 2033 */     if (name.equalsIgnoreCase("WEIGHT")) {
/* 2034 */       if (st.nextToken() == -2) {
/* 2035 */         node.setWeight(st.nval);
/*      */       }
/*      */       else {
/* 2038 */         throwParseException(st, "invalid weight");
/*      */       }
/*      */     }
/* 2041 */     else if (name.equalsIgnoreCase("NAME")) {
/* 2042 */       if (st.nextToken() == -3) {
/* 2043 */         if ((node instanceof Leaf)) {
/* 2044 */           ((Leaf)node).setName(st.sval);
/*      */         }
/* 2046 */         else if ((node instanceof Split)) {
/* 2047 */           ((Split)node).setName(st.sval);
/*      */         }
/*      */         else {
/* 2050 */           throwParseException(st, "can't specify name for " + node);
/*      */         }
/*      */       }
/*      */       else {
/* 2054 */         throwParseException(st, "invalid name");
/*      */       }
/*      */     }
/*      */     else {
/* 2058 */       throwParseException(st, "unrecognized attribute \"" + name + "\"");
/*      */     }
/*      */   }
/*      */   
/*      */   private static void addSplitChild(Split parent, Node child) {
/* 2063 */     List<Node> children = new ArrayList(parent.getChildren());
/* 2064 */     if (children.size() == 0) {
/* 2065 */       children.add(child);
/*      */     }
/*      */     else {
/* 2068 */       children.add(new Divider());
/* 2069 */       children.add(child);
/*      */     }
/* 2071 */     parent.setChildren(children);
/*      */   }
/*      */   
/*      */   private static void parseLeaf(StreamTokenizer st, Split parent) throws Exception {
/* 2075 */     Leaf leaf = new Leaf();
/*      */     int token;
/* 2077 */     while (((token = st.nextToken()) != -1) && 
/* 2078 */       (token != 41))
/*      */     {
/*      */ 
/* 2081 */       if (token == -3) {
/* 2082 */         parseAttribute(st.sval, st, leaf);
/*      */       }
/*      */       else {
/* 2085 */         throwParseException(st, "Bad Leaf: " + leaf);
/*      */       }
/*      */     }
/* 2088 */     addSplitChild(parent, leaf);
/*      */   }
/*      */   
/*      */   private static void parseSplit(StreamTokenizer st, Split parent) throws Exception {
/*      */     int token;
/* 2093 */     while (((token = st.nextToken()) != -1) && 
/* 2094 */       (token != 41))
/*      */     {
/*      */ 
/* 2097 */       if (token == -3) {
/* 2098 */         if (st.sval.equalsIgnoreCase("WEIGHT")) {
/* 2099 */           parseAttribute(st.sval, st, parent);
/*      */         }
/* 2101 */         else if (st.sval.equalsIgnoreCase("NAME")) {
/* 2102 */           parseAttribute(st.sval, st, parent);
/*      */         }
/*      */         else {
/* 2105 */           addSplitChild(parent, new Leaf(st.sval));
/*      */         }
/*      */       }
/* 2108 */       else if (token == 40) {
/* 2109 */         if ((token = st.nextToken()) != -3) {
/* 2110 */           throwParseException(st, "invalid node type");
/*      */         }
/* 2112 */         String nodeType = st.sval.toUpperCase();
/* 2113 */         if (nodeType.equals("LEAF")) {
/* 2114 */           parseLeaf(st, parent);
/*      */         }
/* 2116 */         else if ((nodeType.equals("ROW")) || (nodeType.equals("COLUMN"))) {
/* 2117 */           Split split = new Split();
/* 2118 */           split.setRowLayout(nodeType.equals("ROW"));
/* 2119 */           addSplitChild(parent, split);
/* 2120 */           parseSplit(st, split);
/*      */         }
/*      */         else {
/* 2123 */           throwParseException(st, "unrecognized node type '" + nodeType + "'");
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static Node parseModel(Reader r) {
/* 2130 */     StreamTokenizer st = new StreamTokenizer(r);
/*      */     try {
/* 2132 */       Split root = new Split();
/* 2133 */       parseSplit(st, root);
/* 2134 */       return (Node)root.getChildren().get(0);
/*      */     }
/*      */     catch (Exception e) {
/* 2137 */       System.err.println(e);
/*      */     } finally {
/*      */       try {
/* 2140 */         r.close();
/*      */       } catch (IOException ignore) {} }
/* 2142 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Node parseModel(String s)
/*      */   {
/* 2192 */     return parseModel(new StringReader(s));
/*      */   }
/*      */   
/*      */   private static void printModel(String indent, Node root)
/*      */   {
/* 2197 */     if ((root instanceof Split)) {
/* 2198 */       Split split = (Split)root;
/* 2199 */       System.out.println(indent + split);
/* 2200 */       for (Node child : split.getChildren()) {
/* 2201 */         printModel(indent + "  ", child);
/*      */       }
/*      */     }
/*      */     else {
/* 2205 */       System.out.println(indent + root);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static void printModel(Node root)
/*      */   {
/* 2213 */     printModel("", root);
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\MultiSplitLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */