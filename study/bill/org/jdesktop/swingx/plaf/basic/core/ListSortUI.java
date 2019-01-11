/*     */ package org.jdesktop.swingx.plaf.basic.core;
/*     */ 
/*     */ import javax.swing.DefaultListSelectionModel;
/*     */ import javax.swing.ListModel;
/*     */ import javax.swing.ListSelectionModel;
/*     */ import javax.swing.RowSorter;
/*     */ import javax.swing.event.ListDataEvent;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.RowSorterEvent;
/*     */ import javax.swing.event.RowSorterEvent.Type;
/*     */ import javax.swing.event.RowSorterListener;
/*     */ import org.jdesktop.swingx.JXList;
/*     */ import org.jdesktop.swingx.SwingXUtilities;
/*     */ import org.jdesktop.swingx.util.Contract;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ListSortUI
/*     */ {
/*     */   private RowSorter<? extends ListModel> sorter;
/*     */   private JXList list;
/*     */   private ListSelectionModel modelSelection;
/*     */   private int modelLeadIndex;
/*     */   private boolean syncingSelection;
/*     */   private int[] lastModelSelection;
/*     */   private boolean sorterChanged;
/*     */   private boolean ignoreSortChange;
/*     */   private RowSorterListener sorterListener;
/*     */   
/*     */   public ListSortUI(JXList list, RowSorter<? extends ListModel> sorter)
/*     */   {
/*  82 */     this.sorter = ((RowSorter)Contract.asNotNull(sorter, "RowSorter must not be null"));
/*  83 */     this.list = ((JXList)Contract.asNotNull(list, "list must not be null"));
/*  84 */     if (sorter != list.getRowSorter()) { throw new IllegalStateException("sorter must be same as the one on list");
/*     */     }
/*  86 */     this.sorterListener = createRowSorterListener();
/*  87 */     sorter.addRowSorterListener(this.sorterListener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dispose()
/*     */   {
/*  95 */     if (this.sorter != null) {
/*  96 */       this.sorter.removeRowSorterListener(this.sorterListener);
/*     */     }
/*  98 */     this.sorter = null;
/*  99 */     this.list = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void modelChanged(ListDataEvent e)
/*     */   {
/* 109 */     ModelChange change = new ModelChange(e);
/* 110 */     prepareForChange(change);
/* 111 */     notifySorter(change);
/* 112 */     if (change.type != 0)
/*     */     {
/*     */ 
/* 115 */       this.sorterChanged = true;
/*     */     }
/* 117 */     processChange(change);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void viewSelectionChanged(ListSelectionEvent e)
/*     */   {
/* 126 */     if ((!this.syncingSelection) && (this.modelSelection != null)) {
/* 127 */       this.modelSelection = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void sortedChanged(RowSorterEvent e)
/*     */   {
/* 137 */     this.sorterChanged = true;
/* 138 */     if (!this.ignoreSortChange) {
/* 139 */       prepareForChange(e);
/* 140 */       processChange(null);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 151 */       this.list.invalidateCellSizeCache();
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
/*     */   private void prepareForChange(RowSorterEvent sortEvent)
/*     */   {
/* 165 */     Contract.asNotNull(sortEvent, "sorter event not null");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 170 */     if ((this.modelSelection == null) && (this.sorter.getViewRowCount() != this.sorter.getModelRowCount()))
/*     */     {
/* 172 */       this.modelSelection = new DefaultListSelectionModel();
/* 173 */       ListSelectionModel viewSelection = getViewSelectionModel();
/* 174 */       int min = viewSelection.getMinSelectionIndex();
/* 175 */       int max = viewSelection.getMaxSelectionIndex();
/*     */       
/* 177 */       for (int viewIndex = min; viewIndex <= max; viewIndex++) {
/* 178 */         if (viewSelection.isSelectedIndex(viewIndex)) {
/* 179 */           int modelIndex = convertRowIndexToModel(sortEvent, viewIndex);
/*     */           
/* 181 */           if (modelIndex != -1) {
/* 182 */             this.modelSelection.addSelectionInterval(modelIndex, modelIndex);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 187 */       int modelIndex = convertRowIndexToModel(sortEvent, viewSelection.getLeadSelectionIndex());
/*     */       
/* 189 */       SwingXUtilities.setLeadAnchorWithoutSelection(this.modelSelection, modelIndex, modelIndex);
/*     */     }
/* 191 */     else if (this.modelSelection == null)
/*     */     {
/*     */ 
/* 194 */       cacheModelSelection(sortEvent);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void prepareForChange(ModelChange change)
/*     */   {
/* 206 */     Contract.asNotNull(change, "table event not null");
/* 207 */     if (change.allRowsChanged)
/*     */     {
/* 209 */       this.modelSelection = null;
/* 210 */     } else if (this.modelSelection != null)
/*     */     {
/* 212 */       switch (change.type) {
/*     */       case 2: 
/* 214 */         this.modelSelection.removeIndexInterval(change.startModelIndex, change.endModelIndex);
/*     */         
/* 216 */         break;
/*     */       case 1: 
/* 218 */         this.modelSelection.insertIndexInterval(change.startModelIndex, change.endModelIndex, true);
/*     */         
/* 220 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       }
/*     */       
/*     */     } else {
/* 227 */       cacheModelSelection(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void cacheModelSelection(RowSorterEvent sortEvent)
/*     */   {
/* 234 */     this.lastModelSelection = convertSelectionToModel(sortEvent);
/* 235 */     this.modelLeadIndex = convertRowIndexToModel(sortEvent, getViewSelectionModel().getLeadSelectionIndex());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void processChange(ModelChange change)
/*     */   {
/* 246 */     if ((change != null) && (change.allRowsChanged)) {
/* 247 */       allChanged();
/* 248 */       getViewSelectionModel().clearSelection();
/* 249 */     } else if (this.sorterChanged) {
/* 250 */       restoreSelection(change);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void restoreSelection(ModelChange change)
/*     */   {
/* 258 */     this.syncingSelection = true;
/* 259 */     if (this.lastModelSelection != null) {
/* 260 */       restoreSortingSelection(this.lastModelSelection, this.modelLeadIndex, change);
/*     */       
/* 262 */       this.lastModelSelection = null;
/* 263 */     } else if (this.modelSelection != null) {
/* 264 */       ListSelectionModel viewSelection = getViewSelectionModel();
/* 265 */       viewSelection.setValueIsAdjusting(true);
/* 266 */       viewSelection.clearSelection();
/* 267 */       int min = this.modelSelection.getMinSelectionIndex();
/* 268 */       int max = this.modelSelection.getMaxSelectionIndex();
/*     */       
/* 270 */       for (int modelIndex = min; modelIndex <= max; modelIndex++) {
/* 271 */         if (this.modelSelection.isSelectedIndex(modelIndex)) {
/* 272 */           int viewIndex = this.sorter.convertRowIndexToView(modelIndex);
/* 273 */           if (viewIndex != -1) {
/* 274 */             viewSelection.addSelectionInterval(viewIndex, viewIndex);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 280 */       int viewLeadIndex = this.modelSelection.getLeadSelectionIndex();
/* 281 */       if (viewLeadIndex != -1) {
/* 282 */         viewLeadIndex = this.sorter.convertRowIndexToView(viewLeadIndex);
/*     */       }
/* 284 */       SwingXUtilities.setLeadAnchorWithoutSelection(viewSelection, viewLeadIndex, viewLeadIndex);
/*     */       
/* 286 */       viewSelection.setValueIsAdjusting(false);
/*     */     }
/* 288 */     this.syncingSelection = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void restoreSortingSelection(int[] selection, int lead, ModelChange change)
/*     */   {
/* 298 */     for (int i = selection.length - 1; i >= 0; i--) {
/* 299 */       selection[i] = convertRowIndexToView(change, selection[i]);
/*     */     }
/* 301 */     lead = convertRowIndexToView(change, lead);
/*     */     
/*     */ 
/* 304 */     if ((selection.length == 0) || ((selection.length == 1) && (selection[0] == this.list.getSelectedIndex())))
/*     */     {
/* 306 */       return;
/*     */     }
/* 308 */     ListSelectionModel selectionModel = getViewSelectionModel();
/*     */     
/* 310 */     selectionModel.setValueIsAdjusting(true);
/* 311 */     selectionModel.clearSelection();
/* 312 */     for (int i = selection.length - 1; i >= 0; i--) {
/* 313 */       if (selection[i] != -1) {
/* 314 */         selectionModel.addSelectionInterval(selection[i], selection[i]);
/*     */       }
/*     */     }
/*     */     
/* 318 */     SwingXUtilities.setLeadAnchorWithoutSelection(selectionModel, lead, lead);
/*     */     
/* 320 */     selectionModel.setValueIsAdjusting(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int convertRowIndexToView(ModelChange change, int modelIndex)
/*     */   {
/* 332 */     if (modelIndex < 0) {
/* 333 */       return -1;
/*     */     }
/*     */     
/* 336 */     if ((change != null) && (modelIndex >= change.startModelIndex)) {
/* 337 */       if (change.type == 1) {
/* 338 */         if (modelIndex + change.length >= change.modelRowCount) {
/* 339 */           return -1;
/*     */         }
/* 341 */         return this.sorter.convertRowIndexToView(modelIndex + change.length);
/*     */       }
/*     */       
/* 344 */       if (change.type == 2) {
/* 345 */         if (modelIndex <= change.endModelIndex)
/*     */         {
/* 347 */           return -1;
/*     */         }
/*     */         
/* 350 */         if (modelIndex - change.length >= change.modelRowCount) {
/* 351 */           return -1;
/*     */         }
/* 353 */         return this.sorter.convertRowIndexToView(modelIndex - change.length);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 359 */     if (modelIndex >= this.sorter.getModelRowCount()) {
/* 360 */       return -1;
/*     */     }
/* 362 */     return this.sorter.convertRowIndexToView(modelIndex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int convertRowIndexToModel(RowSorterEvent e, int viewIndex)
/*     */   {
/* 371 */     if (e != null) {
/* 372 */       if (e.getPreviousRowCount() == 0) {
/* 373 */         return viewIndex;
/*     */       }
/*     */       
/* 376 */       return e.convertPreviousRowIndexToModel(viewIndex);
/*     */     }
/*     */     
/* 379 */     if ((viewIndex < 0) || (viewIndex >= this.sorter.getViewRowCount())) {
/* 380 */       return -1;
/*     */     }
/* 382 */     return this.sorter.convertRowIndexToModel(viewIndex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int[] convertSelectionToModel(RowSorterEvent e)
/*     */   {
/* 390 */     int[] selection = this.list.getSelectedIndices();
/* 391 */     for (int i = selection.length - 1; i >= 0; i--) {
/* 392 */       selection[i] = convertRowIndexToModel(e, selection[i]);
/*     */     }
/* 394 */     return selection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void notifySorter(ModelChange change)
/*     */   {
/*     */     try
/*     */     {
/* 403 */       this.ignoreSortChange = true;
/* 404 */       this.sorterChanged = false;
/* 405 */       if (change.allRowsChanged) {
/* 406 */         this.sorter.allRowsChanged();
/*     */       } else {
/* 408 */         switch (change.type) {
/*     */         case 0: 
/* 410 */           this.sorter.rowsUpdated(change.startModelIndex, change.endModelIndex);
/*     */           
/* 412 */           break;
/*     */         case 1: 
/* 414 */           this.sorter.rowsInserted(change.startModelIndex, change.endModelIndex);
/*     */           
/* 416 */           break;
/*     */         case 2: 
/* 418 */           this.sorter.rowsDeleted(change.startModelIndex, change.endModelIndex);
/*     */         }
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 424 */       this.ignoreSortChange = false;
/*     */     }
/*     */   }
/*     */   
/*     */   private ListSelectionModel getViewSelectionModel()
/*     */   {
/* 430 */     return this.list.getSelectionModel();
/*     */   }
/*     */   
/*     */ 
/*     */   private void allChanged()
/*     */   {
/* 436 */     this.modelLeadIndex = -1;
/* 437 */     this.modelSelection = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RowSorterListener createRowSorterListener()
/*     */   {
/* 449 */     RowSorterListener l = new RowSorterListener()
/*     */     {
/*     */       public void sorterChanged(RowSorterEvent e)
/*     */       {
/* 453 */         if (e.getType() == RowSorterEvent.Type.SORTED) {
/* 454 */           ListSortUI.this.sortedChanged(e);
/*     */         }
/*     */         
/*     */       }
/*     */       
/* 459 */     };
/* 460 */     return l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static final class ModelChange
/*     */   {
/*     */     int startModelIndex;
/*     */     
/*     */ 
/*     */ 
/*     */     int endModelIndex;
/*     */     
/*     */ 
/*     */ 
/*     */     int length;
/*     */     
/*     */ 
/*     */ 
/*     */     int type;
/*     */     
/*     */ 
/*     */     int modelRowCount;
/*     */     
/*     */ 
/*     */     boolean allRowsChanged;
/*     */     
/*     */ 
/*     */ 
/*     */     public ModelChange(ListDataEvent e)
/*     */     {
/* 492 */       this.type = e.getType();
/* 493 */       this.modelRowCount = ((ListModel)e.getSource()).getSize();
/* 494 */       this.startModelIndex = e.getIndex0();
/* 495 */       this.endModelIndex = e.getIndex1();
/* 496 */       this.allRowsChanged = (this.startModelIndex < 0);
/* 497 */       this.length = (this.allRowsChanged ? -1 : this.endModelIndex - this.startModelIndex + 1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\core\ListSortUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */