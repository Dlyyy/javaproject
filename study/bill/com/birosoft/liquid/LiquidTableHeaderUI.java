/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import com.birosoft.liquid.skin.Skin;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.Enumeration;
/*     */ import javax.swing.CellRendererPane;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.JViewport;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.event.MouseInputListener;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.TableHeaderUI;
/*     */ import javax.swing.table.JTableHeader;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import javax.swing.table.TableColumn;
/*     */ import javax.swing.table.TableColumnModel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LiquidTableHeaderUI
/*     */   extends TableHeaderUI
/*     */ {
/*  39 */   private static Cursor resizeCursor = Cursor.getPredefinedCursor(11);
/*     */   
/*  41 */   public LiquidTableHeaderUI() { this.columnSelected = -1; }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static final int HEADER_HEIGHT = 22;
/*     */   
/*     */ 
/*     */   private int columnSelected;
/*     */   
/*     */ 
/*     */   private Skin skin;
/*     */   
/*     */ 
/*     */   protected JTableHeader header;
/*     */   
/*     */ 
/*     */   protected CellRendererPane rendererPane;
/*     */   
/*     */   protected MouseInputListener mouseInputListener;
/*     */   
/*     */   protected MouseInputListener createMouseInputListener()
/*     */   {
/*  63 */     return new MouseInputHandler();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ComponentUI createUI(JComponent h)
/*     */   {
/*  70 */     return new LiquidTableHeaderUI();
/*     */   }
/*     */   
/*     */   public void installUI(JComponent c)
/*     */   {
/*  75 */     this.header = ((JTableHeader)c);
/*     */     
/*  77 */     this.rendererPane = new CellRendererPane();
/*  78 */     this.header.add(this.rendererPane);
/*     */     
/*  80 */     installDefaults();
/*  81 */     installListeners();
/*  82 */     installKeyboardActions();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  94 */     LookAndFeel.installColorsAndFont(this.header, "TableHeader.background", "TableHeader.foreground", "TableHeader.font");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/* 102 */     this.mouseInputListener = createMouseInputListener();
/*     */     
/* 104 */     this.header.addMouseListener(this.mouseInputListener);
/* 105 */     this.header.addMouseMotionListener(this.mouseInputListener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void installKeyboardActions() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void uninstallUI(JComponent c)
/*     */   {
/* 116 */     uninstallDefaults();
/* 117 */     uninstallListeners();
/* 118 */     uninstallKeyboardActions();
/*     */     
/* 120 */     this.header.remove(this.rendererPane);
/* 121 */     this.rendererPane = null;
/* 122 */     this.header = null;
/*     */   }
/*     */   
/*     */   protected void uninstallDefaults() {}
/*     */   
/*     */   protected void uninstallListeners()
/*     */   {
/* 129 */     this.header.removeMouseListener(this.mouseInputListener);
/* 130 */     this.header.removeMouseMotionListener(this.mouseInputListener);
/*     */     
/* 132 */     this.mouseInputListener = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void uninstallKeyboardActions() {}
/*     */   
/*     */ 
/*     */   public void paint(Graphics g, JComponent c)
/*     */   {
/* 142 */     if (this.header.getColumnModel().getColumnCount() <= 0) {
/* 143 */       return;
/*     */     }
/*     */     
/* 146 */     boolean ltr = this.header.getComponentOrientation().isLeftToRight();
/*     */     
/* 148 */     Rectangle clip = g.getClipBounds();
/* 149 */     Point left = clip.getLocation();
/* 150 */     Point right = new Point(clip.x + clip.width - 1, clip.y);
/* 151 */     TableColumnModel cm = this.header.getColumnModel();
/* 152 */     int cMin = this.header.columnAtPoint(ltr ? left : right);
/* 153 */     int cMax = this.header.columnAtPoint(ltr ? right : left);
/*     */     
/*     */ 
/* 156 */     if (cMin == -1) {
/* 157 */       cMin = 0;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 162 */     if (cMax == -1) {
/* 163 */       cMax = cm.getColumnCount() - 1;
/*     */     }
/*     */     
/* 166 */     TableColumn draggedColumn = this.header.getDraggedColumn();
/*     */     
/* 168 */     int columnMargin = cm.getColumnMargin();
/* 169 */     Rectangle cellRect = this.header.getHeaderRect(cMin);
/*     */     
/*     */ 
/* 172 */     if (ltr) {
/* 173 */       for (int column = cMin; column <= cMax; column++) {
/* 174 */         TableColumn aColumn = cm.getColumn(column);
/* 175 */         int columnWidth = aColumn.getWidth();
/* 176 */         cellRect.width = (columnWidth - columnMargin);
/*     */         
/* 178 */         if (aColumn != draggedColumn) {
/* 179 */           paintCell(g, cellRect, column);
/*     */         }
/*     */         
/* 182 */         cellRect.x += columnWidth;
/*     */       }
/*     */     } else {
/* 185 */       TableColumn aColumn = cm.getColumn(cMin);
/*     */       
/* 187 */       if (aColumn != draggedColumn) {
/* 188 */         int columnWidth = aColumn.getWidth();
/* 189 */         cellRect.width = (columnWidth - columnMargin);
/* 190 */         cellRect.x += columnMargin;
/* 191 */         paintCell(g, cellRect, cMin);
/*     */       }
/*     */       
/* 194 */       for (int column = cMin + 1; column <= cMax; column++) {
/* 195 */         aColumn = cm.getColumn(column);
/* 196 */         int columnWidth = aColumn.getWidth();
/* 197 */         cellRect.width = (columnWidth - columnMargin);
/* 198 */         cellRect.x -= columnWidth;
/*     */         
/* 200 */         if (aColumn != draggedColumn) {
/* 201 */           paintCell(g, cellRect, column);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 207 */     if (draggedColumn != null) {
/* 208 */       int draggedColumnIndex = viewIndexForColumn(draggedColumn);
/* 209 */       Rectangle draggedCellRect = this.header.getHeaderRect(draggedColumnIndex);
/*     */       
/*     */ 
/* 212 */       g.setColor(this.header.getParent().getBackground());
/* 213 */       g.fillRect(draggedCellRect.x, draggedCellRect.y, draggedCellRect.width, draggedCellRect.height);
/*     */       
/*     */ 
/* 216 */       draggedCellRect.x += this.header.getDraggedDistance();
/*     */       
/*     */ 
/* 219 */       g.setColor(this.header.getBackground());
/* 220 */       g.fillRect(draggedCellRect.x, draggedCellRect.y, draggedCellRect.width, draggedCellRect.height);
/*     */       
/*     */ 
/* 223 */       paintCell(g, draggedCellRect, draggedColumnIndex);
/*     */     }
/*     */     
/*     */ 
/* 227 */     this.rendererPane.removeAll();
/*     */   }
/*     */   
/*     */   private Component getHeaderRenderer(int columnIndex) {
/* 231 */     TableColumn aColumn = this.header.getColumnModel().getColumn(columnIndex);
/* 232 */     TableCellRenderer renderer = aColumn.getHeaderRenderer();
/*     */     
/* 234 */     if (renderer == null) {
/* 235 */       renderer = this.header.getDefaultRenderer();
/*     */     }
/*     */     
/* 238 */     return renderer.getTableCellRendererComponent(this.header.getTable(), aColumn.getHeaderValue(), false, false, -1, columnIndex);
/*     */   }
/*     */   
/*     */   private void paintCell(Graphics g, Rectangle cellRect, int columnIndex)
/*     */   {
/* 243 */     int index = 0;
/* 244 */     Component component = getHeaderRenderer(columnIndex);
/*     */     
/* 246 */     if (columnIndex == this.columnSelected) {
/* 247 */       index = 1;
/*     */     }
/*     */     
/* 250 */     getSkin().draw(g, index, cellRect.x, cellRect.y, cellRect.width, cellRect.height);
/*     */     
/* 252 */     this.rendererPane.paintComponent(g, component, this.header, cellRect.x, cellRect.y, cellRect.width, cellRect.height, true);
/*     */   }
/*     */   
/*     */   private int viewIndexForColumn(TableColumn aColumn)
/*     */   {
/* 257 */     TableColumnModel cm = this.header.getColumnModel();
/*     */     
/* 259 */     for (int column = 0; column < cm.getColumnCount(); column++) {
/* 260 */       if (cm.getColumn(column) == aColumn) {
/* 261 */         return column;
/*     */       }
/*     */     }
/*     */     
/* 265 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private int getHeaderHeight()
/*     */   {
/* 272 */     return 22;
/*     */   }
/*     */   
/*     */   private Dimension createHeaderSize(long width) {
/* 276 */     TableColumnModel columnModel = this.header.getColumnModel();
/*     */     
/*     */ 
/* 279 */     if (width > 2147483647L) {
/* 280 */       width = 2147483647L;
/*     */     }
/*     */     
/* 283 */     return new Dimension((int)width, getHeaderHeight());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent c)
/*     */   {
/* 291 */     long width = 0L;
/* 292 */     Enumeration enumeration = this.header.getColumnModel().getColumns();
/*     */     
/* 294 */     while (enumeration.hasMoreElements()) {
/* 295 */       TableColumn aColumn = (TableColumn)enumeration.nextElement();
/* 296 */       width += aColumn.getMinWidth();
/*     */     }
/*     */     
/* 299 */     return createHeaderSize(width);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent c)
/*     */   {
/* 309 */     long width = 0L;
/* 310 */     Enumeration enumeration = this.header.getColumnModel().getColumns();
/*     */     
/* 312 */     while (enumeration.hasMoreElements()) {
/* 313 */       TableColumn aColumn = (TableColumn)enumeration.nextElement();
/* 314 */       width += aColumn.getPreferredWidth();
/*     */     }
/*     */     
/* 317 */     return createHeaderSize(width);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent c)
/*     */   {
/* 325 */     long width = 0L;
/* 326 */     Enumeration enumeration = this.header.getColumnModel().getColumns();
/*     */     
/* 328 */     while (enumeration.hasMoreElements()) {
/* 329 */       TableColumn aColumn = (TableColumn)enumeration.nextElement();
/* 330 */       width += aColumn.getMaxWidth();
/*     */     }
/*     */     
/* 333 */     return createHeaderSize(width);
/*     */   }
/*     */   
/*     */   public Skin getSkin() {
/* 337 */     if (this.skin == null) {
/* 338 */       this.skin = new Skin("tableheader.png", 8, 4, 13, 4, 10);
/*     */     }
/*     */     
/* 341 */     return this.skin;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public class MouseInputHandler
/*     */     implements MouseInputListener
/*     */   {
/*     */     private int mouseXOffset;
/*     */     
/* 351 */     private Cursor otherCursor = LiquidTableHeaderUI.resizeCursor;
/*     */     
/*     */     public MouseInputHandler() {}
/*     */     
/*     */     public void mouseClicked(MouseEvent e) {}
/*     */     
/* 357 */     private boolean canResize(TableColumn column) { return (column != null) && (LiquidTableHeaderUI.this.header.getResizingAllowed()) && (column.getResizable()); }
/*     */     
/*     */ 
/*     */     private TableColumn getResizingColumn(Point p)
/*     */     {
/* 362 */       return getResizingColumn(p, LiquidTableHeaderUI.this.header.columnAtPoint(p));
/*     */     }
/*     */     
/*     */     private TableColumn getResizingColumn(Point p, int column) {
/* 366 */       if (column == -1) {
/* 367 */         return null;
/*     */       }
/*     */       
/* 370 */       Rectangle r = LiquidTableHeaderUI.this.header.getHeaderRect(column);
/* 371 */       r.grow(-3, 0);
/*     */       
/* 373 */       if (r.contains(p)) {
/* 374 */         return null;
/*     */       }
/*     */       
/* 377 */       int midPoint = r.x + r.width / 2;
/*     */       int columnIndex;
/*     */       int columnIndex;
/* 380 */       if (LiquidTableHeaderUI.this.header.getComponentOrientation().isLeftToRight()) {
/* 381 */         columnIndex = p.x < midPoint ? column - 1 : column;
/*     */       } else {
/* 383 */         columnIndex = p.x < midPoint ? column : column - 1;
/*     */       }
/*     */       
/* 386 */       if (columnIndex == -1) {
/* 387 */         return null;
/*     */       }
/*     */       
/* 390 */       return LiquidTableHeaderUI.this.header.getColumnModel().getColumn(columnIndex);
/*     */     }
/*     */     
/*     */     public void mousePressed(MouseEvent e) {
/* 394 */       LiquidTableHeaderUI.this.header.setDraggedColumn(null);
/* 395 */       LiquidTableHeaderUI.this.header.setResizingColumn(null);
/* 396 */       LiquidTableHeaderUI.this.header.setDraggedDistance(0);
/*     */       
/* 398 */       Point p = e.getPoint();
/*     */       
/*     */ 
/* 401 */       TableColumnModel columnModel = LiquidTableHeaderUI.this.header.getColumnModel();
/* 402 */       int index = LiquidTableHeaderUI.this.header.columnAtPoint(p);
/*     */       
/* 404 */       if (index != -1)
/*     */       {
/* 406 */         TableColumn resizingColumn = getResizingColumn(p, index);
/*     */         
/* 408 */         if (canResize(resizingColumn)) {
/* 409 */           LiquidTableHeaderUI.this.header.setResizingColumn(resizingColumn);
/*     */           
/* 411 */           if (LiquidTableHeaderUI.this.header.getComponentOrientation().isLeftToRight()) {
/* 412 */             this.mouseXOffset = (p.x - resizingColumn.getWidth());
/*     */           } else {
/* 414 */             this.mouseXOffset = (p.x + resizingColumn.getWidth());
/*     */           }
/* 416 */         } else if (LiquidTableHeaderUI.this.header.getReorderingAllowed()) {
/* 417 */           TableColumn hitColumn = columnModel.getColumn(index);
/* 418 */           LiquidTableHeaderUI.this.header.setDraggedColumn(hitColumn);
/* 419 */           this.mouseXOffset = p.x;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void swapCursor() {
/* 425 */       Cursor tmp = LiquidTableHeaderUI.this.header.getCursor();
/* 426 */       LiquidTableHeaderUI.this.header.setCursor(this.otherCursor);
/* 427 */       this.otherCursor = tmp;
/*     */     }
/*     */     
/*     */     public void mouseMoved(MouseEvent e) {
/* 431 */       if (canResize(getResizingColumn(e.getPoint())) != (LiquidTableHeaderUI.this.header.getCursor() == LiquidTableHeaderUI.resizeCursor)) {
/* 432 */         swapCursor();
/*     */       }
/*     */       
/* 435 */       Point p = e.getPoint();
/* 436 */       TableColumnModel columnModel = LiquidTableHeaderUI.this.header.getColumnModel();
/* 437 */       int index = LiquidTableHeaderUI.this.header.columnAtPoint(p);
/*     */       
/* 439 */       if (index != LiquidTableHeaderUI.this.columnSelected) {
/* 440 */         LiquidTableHeaderUI.this.columnSelected = index;
/* 441 */         LiquidTableHeaderUI.this.header.repaint();
/*     */       }
/*     */     }
/*     */     
/*     */     public void mouseDragged(MouseEvent e) {
/* 446 */       int mouseX = e.getX();
/*     */       
/* 448 */       TableColumn resizingColumn = LiquidTableHeaderUI.this.header.getResizingColumn();
/* 449 */       TableColumn draggedColumn = LiquidTableHeaderUI.this.header.getDraggedColumn();
/*     */       
/* 451 */       boolean headerLeftToRight = LiquidTableHeaderUI.this.header.getComponentOrientation().isLeftToRight();
/*     */       
/*     */ 
/* 454 */       if (resizingColumn != null) {
/* 455 */         int oldWidth = resizingColumn.getWidth();
/*     */         int newWidth;
/*     */         int newWidth;
/* 458 */         if (headerLeftToRight) {
/* 459 */           newWidth = mouseX - this.mouseXOffset;
/*     */         } else {
/* 461 */           newWidth = this.mouseXOffset - mouseX;
/*     */         }
/*     */         
/* 464 */         resizingColumn.setWidth(newWidth);
/*     */         
/*     */         Container container;
/*     */         
/* 468 */         if ((LiquidTableHeaderUI.this.header.getParent() == null) || ((container = LiquidTableHeaderUI.this.header.getParent().getParent()) == null) || (!(container instanceof JScrollPane))) {
/*     */           return;
/*     */         }
/*     */         
/*     */         Container container;
/*     */         
/* 474 */         if ((!container.getComponentOrientation().isLeftToRight()) && (!headerLeftToRight))
/*     */         {
/* 476 */           JTable table = LiquidTableHeaderUI.this.header.getTable();
/*     */           
/* 478 */           if (table != null) {
/* 479 */             JViewport viewport = ((JScrollPane)container).getViewport();
/* 480 */             int viewportWidth = viewport.getWidth();
/* 481 */             int diff = newWidth - oldWidth;
/* 482 */             int newHeaderWidth = table.getWidth() + diff;
/*     */             
/*     */ 
/* 485 */             Dimension tableSize = table.getSize();
/* 486 */             tableSize.width += diff;
/* 487 */             table.setSize(tableSize);
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 493 */             if ((newHeaderWidth >= viewportWidth) && (table.getAutoResizeMode() == 0))
/*     */             {
/* 495 */               Point p = viewport.getViewPosition();
/* 496 */               p.x = Math.max(0, Math.min(newHeaderWidth - viewportWidth, p.x + diff));
/*     */               
/*     */ 
/* 499 */               viewport.setViewPosition(p);
/*     */               
/*     */ 
/* 502 */               this.mouseXOffset += diff;
/*     */             }
/*     */           }
/*     */         }
/* 506 */       } else if (draggedColumn != null) {
/* 507 */         TableColumnModel cm = LiquidTableHeaderUI.this.header.getColumnModel();
/* 508 */         int draggedDistance = mouseX - this.mouseXOffset;
/* 509 */         int direction = draggedDistance < 0 ? -1 : 1;
/* 510 */         int columnIndex = LiquidTableHeaderUI.this.viewIndexForColumn(draggedColumn);
/* 511 */         int newColumnIndex = columnIndex + (headerLeftToRight ? direction : -direction);
/*     */         
/*     */ 
/* 514 */         if ((0 <= newColumnIndex) && (newColumnIndex < cm.getColumnCount()))
/*     */         {
/* 516 */           int width = cm.getColumn(newColumnIndex).getWidth();
/*     */           
/* 518 */           if (Math.abs(draggedDistance) > width / 2) {
/* 519 */             this.mouseXOffset += direction * width;
/* 520 */             LiquidTableHeaderUI.this.header.setDraggedDistance(draggedDistance - direction * width);
/*     */             
/* 522 */             cm.moveColumn(columnIndex, newColumnIndex);
/*     */             
/* 524 */             return;
/*     */           }
/*     */         }
/*     */         
/* 528 */         setDraggedDistance(draggedDistance, columnIndex);
/*     */       }
/*     */     }
/*     */     
/*     */     public void mouseReleased(MouseEvent e) {
/* 533 */       setDraggedDistance(0, LiquidTableHeaderUI.this.viewIndexForColumn(LiquidTableHeaderUI.this.header.getDraggedColumn()));
/*     */       
/* 535 */       LiquidTableHeaderUI.this.header.setResizingColumn(null);
/* 536 */       LiquidTableHeaderUI.this.header.setDraggedColumn(null);
/*     */     }
/*     */     
/*     */     public void mouseEntered(MouseEvent e) {}
/*     */     
/*     */     public void mouseExited(MouseEvent e)
/*     */     {
/* 543 */       LiquidTableHeaderUI.this.columnSelected = -1;
/* 544 */       LiquidTableHeaderUI.this.header.repaint();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void setDraggedDistance(int draggedDistance, int column)
/*     */     {
/* 551 */       LiquidTableHeaderUI.this.header.setDraggedDistance(draggedDistance);
/*     */       
/* 553 */       if (column != -1) {
/* 554 */         LiquidTableHeaderUI.this.header.getColumnModel().moveColumn(column, column);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidTableHeaderUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */