/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.MultipleGradientPaint;
/*     */ import java.awt.MultipleGradientPaint.CycleMethod;
/*     */ import java.awt.RadialGradientPaint;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JRadioButton;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.JSlider;
/*     */ import javax.swing.JSpinner;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.SpinnerNumberModel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import org.jdesktop.swingx.action.AbstractActionExt;
/*     */ import org.jdesktop.swingx.color.ColorUtil;
/*     */ import org.jdesktop.swingx.color.GradientPreviewPanel;
/*     */ import org.jdesktop.swingx.color.GradientThumbRenderer;
/*     */ import org.jdesktop.swingx.color.GradientTrackRenderer;
/*     */ import org.jdesktop.swingx.multislider.MultiThumbModel;
/*     */ import org.jdesktop.swingx.multislider.Thumb;
/*     */ import org.jdesktop.swingx.multislider.ThumbListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JXGradientChooser
/*     */   extends JXPanel
/*     */ {
/*     */   private JXMultiThumbSlider<Color> slider;
/*     */   private JButton deleteThumbButton;
/*     */   private JButton addThumbButton;
/*     */   private JTextField colorField;
/*     */   private JXColorSelectionButton changeColorButton;
/*     */   private JSpinner colorLocationSpinner;
/*     */   private JSpinner alphaSpinner;
/*     */   private JSlider alphaSlider;
/*     */   private JComboBox styleCombo;
/*     */   private GradientPreviewPanel gradientPreview;
/*     */   private JRadioButton noCycleRadio;
/*     */   private JRadioButton reflectedRadio;
/*     */   private JRadioButton repeatedRadio;
/*     */   private JCheckBox reversedCheck;
/*     */   private MultipleGradientPaint gradient;
/*     */   
/*     */   private static enum GradientStyle
/*     */   {
/*  79 */     Linear,  Radial;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private GradientStyle() {}
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
/*     */   public JXGradientChooser()
/*     */   {
/* 107 */     initComponents2();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MultipleGradientPaint getGradient()
/*     */   {
/* 115 */     return this.gradient;
/*     */   }
/*     */   
/* 118 */   private boolean thumbsMoving = false;
/* 119 */   private Logger log = Logger.getLogger(JXGradientChooser.class.getName());
/*     */   
/*     */ 
/*     */   private JPanel topPanel;
/*     */   
/*     */ 
/*     */   private JPanel previewPanel;
/*     */   
/*     */ 
/*     */   public void setGradient(MultipleGradientPaint mgrad)
/*     */   {
/* 130 */     if (this.gradient == mgrad) {
/* 131 */       return;
/*     */     }
/* 133 */     float[] fracts = mgrad.getFractions();
/* 134 */     Color[] colors = mgrad.getColors();
/*     */     
/* 136 */     if (!this.thumbsMoving)
/*     */     {
/* 138 */       if (this.slider.getModel().getThumbCount() != mgrad.getColors().length)
/*     */       {
/*     */ 
/* 141 */         while (this.slider.getModel().getThumbCount() > 0) {
/* 142 */           this.slider.getModel().removeThumb(0);
/*     */         }
/*     */         
/* 145 */         for (int i = 0; i < fracts.length; i++) {
/* 146 */           this.slider.getModel().addThumb(fracts[i], colors[i]);
/*     */         }
/*     */       } else {
/* 149 */         for (int i = 0; i < fracts.length; i++) {
/* 150 */           this.slider.getModel().getThumbAt(i).setObject(colors[i]);
/* 151 */           this.slider.getModel().getThumbAt(i).setPosition(fracts[i]);
/*     */         }
/*     */       }
/*     */     } else {
/* 155 */       this.log.fine("not updating because it's moving");
/*     */     }
/* 157 */     if ((mgrad instanceof RadialGradientPaint)) {
/* 158 */       if (this.styleCombo.getSelectedItem() != GradientStyle.Radial) {
/* 159 */         this.styleCombo.setSelectedItem(GradientStyle.Radial);
/*     */       }
/*     */     }
/* 162 */     else if (this.styleCombo.getSelectedItem() != GradientStyle.Linear) {
/* 163 */       this.styleCombo.setSelectedItem(GradientStyle.Linear);
/*     */     }
/*     */     
/*     */ 
/* 167 */     if (mgrad.getCycleMethod() == MultipleGradientPaint.CycleMethod.REFLECT) {
/* 168 */       this.reflectedRadio.setSelected(true);
/* 169 */       this.gradientPreview.setReflected(true);
/*     */     }
/* 171 */     if (mgrad.getCycleMethod() == MultipleGradientPaint.CycleMethod.REPEAT) {
/* 172 */       this.repeatedRadio.setSelected(true);
/* 173 */       this.gradientPreview.setRepeated(true);
/*     */     }
/* 175 */     this.gradientPreview.setGradient(mgrad);
/*     */     
/* 177 */     MultipleGradientPaint old = getGradient();
/* 178 */     this.gradient = mgrad;
/* 179 */     firePropertyChange("gradient", old, getGradient());
/* 180 */     repaint();
/*     */   }
/*     */   
/*     */   private void recalcGradientFromStops() {
/* 184 */     setGradient(this.gradientPreview.getGradient());
/*     */   }
/*     */   
/*     */   private void updateFromStop(Thumb<Color> thumb) {
/* 188 */     if (thumb == null) {
/* 189 */       updateFromStop(-1, -1.0F, Color.black);
/*     */     } else {
/* 191 */       updateFromStop(1, thumb.getPosition(), (Color)thumb.getObject());
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateFromStop(int thumb, float position, Color color) {
/* 196 */     this.log.fine("updating: " + thumb + " " + position + " " + color);
/* 197 */     if (thumb == -1) {
/* 198 */       this.colorLocationSpinner.setEnabled(false);
/* 199 */       this.alphaSpinner.setEnabled(false);
/* 200 */       this.alphaSlider.setEnabled(false);
/* 201 */       this.colorField.setEnabled(false);
/* 202 */       this.changeColorButton.setEnabled(false);
/* 203 */       this.changeColorButton.setBackground(Color.black);
/* 204 */       this.deleteThumbButton.setEnabled(false);
/*     */     } else {
/* 206 */       this.colorLocationSpinner.setEnabled(true);
/* 207 */       this.alphaSpinner.setEnabled(true);
/* 208 */       this.alphaSlider.setEnabled(true);
/* 209 */       this.colorField.setEnabled(true);
/* 210 */       this.changeColorButton.setEnabled(true);
/* 211 */       this.colorLocationSpinner.setValue(Integer.valueOf((int)(100.0F * position)));
/* 212 */       this.colorField.setText(Integer.toHexString(color.getRGB()).substring(2));
/* 213 */       this.alphaSpinner.setValue(Integer.valueOf(color.getAlpha() * 100 / 255));
/* 214 */       this.alphaSlider.setValue(color.getAlpha() * 100 / 255);
/* 215 */       this.changeColorButton.setBackground(color);
/* 216 */       this.deleteThumbButton.setEnabled(true);
/*     */     }
/* 218 */     updateDeleteButtons();
/* 219 */     recalcGradientFromStops();
/*     */   }
/*     */   
/*     */   private void updateDeleteButtons() {
/* 223 */     if (this.slider.getModel().getThumbCount() <= 2) {
/* 224 */       this.deleteThumbButton.setEnabled(false);
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateGradientProperty() {
/* 229 */     firePropertyChange("gradient", null, getGradient());
/* 230 */     this.gradientPreview.repaint();
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
/*     */   private void initComponents()
/*     */   {
/* 245 */     this.slider = new JXMultiThumbSlider();
/* 246 */     this.gradientPreview = new GradientPreviewPanel();
/* 247 */     this.gradientPreview.setMultiThumbModel(this.slider.getModel());
/*     */     
/*     */ 
/*     */ 
/* 251 */     ButtonGroup typeGroup = new ButtonGroup();
/* 252 */     JPanel jPanel1 = new JPanel();
/* 253 */     this.topPanel = new JPanel();
/* 254 */     JPanel jPanel2 = new JPanel();
/* 255 */     JLabel jLabel1 = new JLabel();
/* 256 */     JLabel jLabel5 = new JLabel();
/* 257 */     this.colorField = new JTextField();
/* 258 */     JLabel jLabel2 = new JLabel();
/* 259 */     JLabel jLabel6 = new JLabel();
/* 260 */     this.colorLocationSpinner = new JSpinner();
/* 261 */     JLabel jLabel4 = new JLabel();
/* 262 */     JLabel jLabel7 = new JLabel();
/* 263 */     this.alphaSpinner = new JSpinner();
/* 264 */     this.changeColorButton = new JXColorSelectionButton();
/* 265 */     this.alphaSlider = new JSlider();
/*     */     
/* 267 */     JPanel jPanel4 = new JPanel();
/* 268 */     this.addThumbButton = new JButton();
/* 269 */     this.deleteThumbButton = new JButton();
/* 270 */     this.previewPanel = new JPanel();
/* 271 */     JPanel jPanel3 = new JPanel();
/* 272 */     JLabel jLabel8 = new JLabel();
/* 273 */     this.styleCombo = new JComboBox();
/* 274 */     JLabel jLabel9 = new JLabel();
/* 275 */     this.noCycleRadio = new JRadioButton();
/* 276 */     this.reflectedRadio = new JRadioButton();
/* 277 */     this.repeatedRadio = new JRadioButton();
/* 278 */     this.reversedCheck = new JCheckBox();
/*     */     
/*     */ 
/*     */ 
/* 282 */     jPanel1.setLayout(new GridBagLayout());
/*     */     
/* 284 */     this.topPanel.setLayout(new GridBagLayout());
/*     */     
/* 286 */     this.topPanel.setBorder(BorderFactory.createTitledBorder("Gradient"));
/* 287 */     jPanel2.setLayout(new GridBagLayout());
/*     */     
/* 289 */     jLabel1.setText("Color:");
/* 290 */     GridBagConstraints gridBagConstraints = new GridBagConstraints();
/* 291 */     gridBagConstraints.gridx = 0;
/* 292 */     gridBagConstraints.gridy = 0;
/* 293 */     gridBagConstraints.ipadx = 2;
/* 294 */     gridBagConstraints.ipady = 2;
/* 295 */     gridBagConstraints.anchor = 13;
/* 296 */     gridBagConstraints.insets = new Insets(4, 4, 4, 4);
/* 297 */     jPanel2.add(jLabel1, gridBagConstraints);
/*     */     
/* 299 */     jLabel5.setText("#");
/* 300 */     gridBagConstraints = new GridBagConstraints();
/* 301 */     gridBagConstraints.gridx = 1;
/* 302 */     gridBagConstraints.gridy = 0;
/* 303 */     gridBagConstraints.insets = new Insets(4, 0, 4, 4);
/* 304 */     jPanel2.add(jLabel5, gridBagConstraints);
/*     */     
/* 306 */     this.colorField.setColumns(6);
/* 307 */     this.colorField.setEnabled(false);
/* 308 */     this.colorField.setPreferredSize(null);
/* 309 */     gridBagConstraints = new GridBagConstraints();
/* 310 */     gridBagConstraints.gridy = 0;
/* 311 */     gridBagConstraints.fill = 2;
/* 312 */     jPanel2.add(this.colorField, gridBagConstraints);
/*     */     
/* 314 */     jLabel2.setText("Location:");
/* 315 */     gridBagConstraints = new GridBagConstraints();
/* 316 */     gridBagConstraints.gridy = 1;
/* 317 */     gridBagConstraints.anchor = 13;
/* 318 */     gridBagConstraints.insets = new Insets(4, 4, 4, 4);
/* 319 */     jPanel2.add(jLabel2, gridBagConstraints);
/*     */     
/* 321 */     jLabel6.setText("%");
/* 322 */     gridBagConstraints = new GridBagConstraints();
/* 323 */     gridBagConstraints.gridy = 1;
/* 324 */     jPanel2.add(jLabel6, gridBagConstraints);
/*     */     
/* 326 */     this.colorLocationSpinner.setEnabled(false);
/* 327 */     this.colorLocationSpinner.setPreferredSize(null);
/* 328 */     gridBagConstraints = new GridBagConstraints();
/* 329 */     gridBagConstraints.gridy = 1;
/* 330 */     gridBagConstraints.fill = 2;
/* 331 */     jPanel2.add(this.colorLocationSpinner, gridBagConstraints);
/*     */     
/* 333 */     jLabel4.setText("Opacity:");
/* 334 */     gridBagConstraints = new GridBagConstraints();
/* 335 */     gridBagConstraints.gridy = 2;
/* 336 */     gridBagConstraints.anchor = 13;
/* 337 */     gridBagConstraints.insets = new Insets(4, 4, 4, 4);
/* 338 */     jPanel2.add(jLabel4, gridBagConstraints);
/*     */     
/* 340 */     jLabel7.setText("%");
/* 341 */     gridBagConstraints = new GridBagConstraints();
/* 342 */     gridBagConstraints.gridy = 2;
/* 343 */     jPanel2.add(jLabel7, gridBagConstraints);
/*     */     
/* 345 */     this.alphaSpinner.setEnabled(false);
/* 346 */     this.alphaSpinner.setPreferredSize(null);
/* 347 */     gridBagConstraints = new GridBagConstraints();
/* 348 */     gridBagConstraints.gridy = 2;
/* 349 */     gridBagConstraints.fill = 2;
/* 350 */     jPanel2.add(this.alphaSpinner, gridBagConstraints);
/*     */     
/* 352 */     this.changeColorButton.setText("00");
/* 353 */     this.changeColorButton.setEnabled(false);
/* 354 */     gridBagConstraints = new GridBagConstraints();
/* 355 */     gridBagConstraints.fill = 0;
/* 356 */     gridBagConstraints.weightx = 1.0D;
/* 357 */     gridBagConstraints.anchor = 17;
/* 358 */     gridBagConstraints.insets = new Insets(0, 4, 0, 0);
/* 359 */     jPanel2.add(this.changeColorButton, gridBagConstraints);
/*     */     
/* 361 */     this.alphaSlider.setEnabled(false);
/* 362 */     this.alphaSlider.setPreferredSize(new Dimension(20, 25));
/* 363 */     gridBagConstraints = new GridBagConstraints();
/* 364 */     gridBagConstraints.gridy = 2;
/* 365 */     gridBagConstraints.fill = 2;
/* 366 */     gridBagConstraints.weightx = 1.0D;
/* 367 */     jPanel2.add(this.alphaSlider, gridBagConstraints);
/*     */     
/* 369 */     gridBagConstraints = new GridBagConstraints();
/* 370 */     gridBagConstraints.gridx = 0;
/* 371 */     gridBagConstraints.gridy = 2;
/* 372 */     gridBagConstraints.gridwidth = 2;
/* 373 */     gridBagConstraints.fill = 2;
/* 374 */     gridBagConstraints.anchor = 18;
/* 375 */     gridBagConstraints.weightx = 1.0D;
/* 376 */     gridBagConstraints.weighty = 1.0D;
/* 377 */     gridBagConstraints.insets = new Insets(5, 5, 5, 5);
/* 378 */     this.topPanel.add(jPanel2, gridBagConstraints);
/*     */     
/* 380 */     gridBagConstraints = new GridBagConstraints();
/* 381 */     gridBagConstraints.gridy = 0;
/* 382 */     gridBagConstraints.gridwidth = 2;
/* 383 */     gridBagConstraints.fill = 2;
/* 384 */     this.topPanel.add(this.slider, gridBagConstraints);
/*     */     
/* 386 */     jPanel4.setLayout(new GridLayout(1, 0, 2, 0));
/*     */     
/* 388 */     this.addThumbButton.setText("Add");
/* 389 */     jPanel4.add(this.addThumbButton);
/*     */     
/* 391 */     this.deleteThumbButton.setText("Delete");
/* 392 */     jPanel4.add(this.deleteThumbButton);
/*     */     
/* 394 */     gridBagConstraints = new GridBagConstraints();
/* 395 */     gridBagConstraints.gridy = 1;
/* 396 */     gridBagConstraints.anchor = 13;
/* 397 */     gridBagConstraints.weightx = 1.0D;
/* 398 */     gridBagConstraints.insets = new Insets(5, 5, 5, 5);
/* 399 */     this.topPanel.add(jPanel4, gridBagConstraints);
/*     */     
/* 401 */     gridBagConstraints = new GridBagConstraints();
/* 402 */     gridBagConstraints.fill = 2;
/* 403 */     gridBagConstraints.weightx = 1.0D;
/* 404 */     jPanel1.add(this.topPanel, gridBagConstraints);
/*     */     
/* 406 */     this.previewPanel.setLayout(new GridBagLayout());
/*     */     
/* 408 */     this.previewPanel.setBorder(BorderFactory.createTitledBorder("Preview"));
/* 409 */     jPanel3.setLayout(new GridBagLayout());
/*     */     
/* 411 */     jLabel8.setText("Style:");
/* 412 */     gridBagConstraints = new GridBagConstraints();
/* 413 */     gridBagConstraints.gridy = 0;
/* 414 */     gridBagConstraints.anchor = 13;
/* 415 */     gridBagConstraints.insets = new Insets(4, 4, 4, 4);
/* 416 */     jPanel3.add(jLabel8, gridBagConstraints);
/*     */     
/* 418 */     this.styleCombo.setModel(new DefaultComboBoxModel(new String[] { "Linear", "Radial" }));
/* 419 */     gridBagConstraints = new GridBagConstraints();
/* 420 */     gridBagConstraints.gridy = 0;
/* 421 */     gridBagConstraints.anchor = 17;
/* 422 */     gridBagConstraints.insets = new Insets(4, 4, 4, 4);
/* 423 */     jPanel3.add(this.styleCombo, gridBagConstraints);
/*     */     
/* 425 */     jLabel9.setText("Type:");
/* 426 */     gridBagConstraints = new GridBagConstraints();
/* 427 */     gridBagConstraints.gridy = 1;
/* 428 */     gridBagConstraints.anchor = 13;
/* 429 */     gridBagConstraints.insets = new Insets(4, 4, 4, 4);
/* 430 */     jPanel3.add(jLabel9, gridBagConstraints);
/*     */     
/* 432 */     typeGroup.add(this.noCycleRadio);
/* 433 */     this.noCycleRadio.setSelected(true);
/* 434 */     this.noCycleRadio.setText("None");
/* 435 */     this.noCycleRadio.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
/* 436 */     this.noCycleRadio.setMargin(new Insets(0, 0, 0, 0));
/* 437 */     gridBagConstraints = new GridBagConstraints();
/* 438 */     gridBagConstraints.gridy = 1;
/* 439 */     gridBagConstraints.anchor = 17;
/* 440 */     gridBagConstraints.insets = new Insets(4, 4, 4, 4);
/* 441 */     jPanel3.add(this.noCycleRadio, gridBagConstraints);
/*     */     
/* 443 */     typeGroup.add(this.reflectedRadio);
/* 444 */     this.reflectedRadio.setText("Reflect");
/* 445 */     this.reflectedRadio.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
/* 446 */     this.reflectedRadio.setMargin(new Insets(0, 0, 0, 0));
/* 447 */     gridBagConstraints = new GridBagConstraints();
/* 448 */     gridBagConstraints.gridx = 1;
/* 449 */     gridBagConstraints.gridy = 2;
/* 450 */     gridBagConstraints.anchor = 17;
/* 451 */     gridBagConstraints.insets = new Insets(4, 4, 4, 4);
/* 452 */     jPanel3.add(this.reflectedRadio, gridBagConstraints);
/*     */     
/* 454 */     typeGroup.add(this.repeatedRadio);
/* 455 */     this.repeatedRadio.setText("Repeat");
/* 456 */     this.repeatedRadio.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
/* 457 */     this.repeatedRadio.setMargin(new Insets(0, 0, 0, 0));
/* 458 */     gridBagConstraints = new GridBagConstraints();
/* 459 */     gridBagConstraints.gridx = 1;
/* 460 */     gridBagConstraints.gridy = 3;
/* 461 */     gridBagConstraints.anchor = 17;
/* 462 */     gridBagConstraints.insets = new Insets(4, 4, 4, 4);
/* 463 */     jPanel3.add(this.repeatedRadio, gridBagConstraints);
/*     */     
/* 465 */     this.reversedCheck.setText("Reverse");
/* 466 */     this.reversedCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
/* 467 */     this.reversedCheck.setMargin(new Insets(0, 0, 0, 0));
/* 468 */     gridBagConstraints = new GridBagConstraints();
/* 469 */     gridBagConstraints.gridx = 1;
/* 470 */     gridBagConstraints.gridy = 4;
/* 471 */     gridBagConstraints.anchor = 17;
/* 472 */     gridBagConstraints.insets = new Insets(4, 4, 4, 4);
/* 473 */     jPanel3.add(this.reversedCheck, gridBagConstraints);
/*     */     
/* 475 */     gridBagConstraints = new GridBagConstraints();
/* 476 */     gridBagConstraints.anchor = 18;
/* 477 */     this.previewPanel.add(jPanel3, gridBagConstraints);
/*     */     
/* 479 */     this.gradientPreview.setBorder(BorderFactory.createEtchedBorder());
/* 480 */     this.gradientPreview.setPreferredSize(new Dimension(130, 130));
/* 481 */     gridBagConstraints = new GridBagConstraints();
/* 482 */     gridBagConstraints.fill = 1;
/* 483 */     gridBagConstraints.weightx = 10.0D;
/* 484 */     gridBagConstraints.weighty = 10.0D;
/* 485 */     this.previewPanel.add(this.gradientPreview, gridBagConstraints);
/*     */     
/* 487 */     gridBagConstraints = new GridBagConstraints();
/* 488 */     gridBagConstraints.gridy = 1;
/* 489 */     gridBagConstraints.fill = 1;
/* 490 */     gridBagConstraints.anchor = 11;
/* 491 */     gridBagConstraints.weightx = 1.0D;
/* 492 */     gridBagConstraints.weighty = 1.0D;
/* 493 */     jPanel1.add(this.previewPanel, gridBagConstraints);
/*     */   }
/*     */   
/*     */   private void initComponents2() {
/* 497 */     initComponents();
/* 498 */     setLayout(new BorderLayout());
/* 499 */     add(this.topPanel, "North");
/* 500 */     add(this.previewPanel, "Center");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 505 */     AddThumbAction addThumbAction = new AddThumbAction();
/* 506 */     DeleteThumbAction deleteThumbAction = new DeleteThumbAction();
/* 507 */     deleteThumbAction.setEnabled(false);
/*     */     
/* 509 */     ActionMap actions = getActionMap();
/* 510 */     actions.put("add-thumb", addThumbAction);
/* 511 */     actions.put("delete-thumb", deleteThumbAction);
/*     */     
/* 513 */     this.addThumbButton.setAction(addThumbAction);
/* 514 */     this.deleteThumbButton.setAction(deleteThumbAction);
/* 515 */     this.changeColorButton.addPropertyChangeListener("background", new PropertyChangeListener() {
/*     */       public void propertyChange(PropertyChangeEvent evt) {
/* 517 */         JXGradientChooser.this.selectColorForThumb();
/* 518 */         JXGradientChooser.this.updateGradientProperty();
/*     */       }
/* 520 */     });
/* 521 */     this.colorLocationSpinner.addChangeListener(new ChangeLocationListener(null));
/* 522 */     ChangeAlphaListener changeAlphaListener = new ChangeAlphaListener(null);
/* 523 */     this.alphaSpinner.addChangeListener(changeAlphaListener);
/* 524 */     this.alphaSlider.addChangeListener(changeAlphaListener);
/* 525 */     RepaintOnEventListener repaintListener = new RepaintOnEventListener(null);
/* 526 */     this.styleCombo.addItemListener(repaintListener);
/* 527 */     this.styleCombo.setModel(new DefaultComboBoxModel(GradientStyle.values()));
/* 528 */     this.noCycleRadio.addActionListener(repaintListener);
/* 529 */     this.reflectedRadio.addActionListener(repaintListener);
/* 530 */     this.repeatedRadio.addActionListener(repaintListener);
/* 531 */     this.reversedCheck.addActionListener(repaintListener);
/* 532 */     this.gradientPreview.picker = this;
/*     */     
/*     */ 
/*     */ 
/* 536 */     SpinnerNumberModel alpha_model = new SpinnerNumberModel(100, 0, 100, 1);
/* 537 */     this.alphaSpinner.setModel(alpha_model);
/* 538 */     SpinnerNumberModel location_model = new SpinnerNumberModel(100, 0, 100, 1);
/* 539 */     this.colorLocationSpinner.setModel(location_model);
/*     */     
/* 541 */     this.slider.setOpaque(false);
/* 542 */     this.slider.setPreferredSize(new Dimension(100, 35));
/* 543 */     this.slider.getModel().setMinimumValue(0.0F);
/* 544 */     this.slider.getModel().setMaximumValue(1.0F);
/*     */     
/* 546 */     this.slider.getModel().addThumb(0.0F, Color.black);
/* 547 */     this.slider.getModel().addThumb(0.5F, Color.red);
/* 548 */     this.slider.getModel().addThumb(1.0F, Color.white);
/*     */     
/* 550 */     this.slider.setThumbRenderer(new GradientThumbRenderer());
/* 551 */     this.slider.setTrackRenderer(new GradientTrackRenderer());
/* 552 */     this.slider.addMultiThumbListener(new StopListener());
/*     */     
/*     */ 
/* 555 */     this.gradientPreview.addPropertyChangeListener("gradient", new PropertyChangeListener() {
/*     */       public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
/* 557 */         JXGradientChooser.this.recalcGradientFromStops();
/*     */       }
/*     */       
/* 560 */     });
/* 561 */     recalcGradientFromStops();
/*     */   }
/*     */   
/*     */   private final class ChangeLocationListener implements ChangeListener {
/*     */     private ChangeLocationListener() {}
/*     */     
/*     */     public void stateChanged(ChangeEvent evt) {
/* 568 */       if (JXGradientChooser.this.slider.getSelectedIndex() >= 0) {
/* 569 */         Thumb<Color> thumb = JXGradientChooser.this.slider.getModel().getThumbAt(JXGradientChooser.this.slider.getSelectedIndex());
/* 570 */         thumb.setPosition(((Integer)JXGradientChooser.this.colorLocationSpinner.getValue()).intValue() / 100.0F);
/* 571 */         JXGradientChooser.this.updateFromStop(thumb);
/* 572 */         JXGradientChooser.this.updateGradientProperty();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final class ChangeAlphaListener implements ChangeListener {
/*     */     private ChangeAlphaListener() {}
/*     */     
/* 580 */     public void stateChanged(ChangeEvent changeEvent) { if ((JXGradientChooser.this.slider.getSelectedIndex() >= 0) && (!JXGradientChooser.this.thumbsMoving))
/*     */       {
/* 582 */         Thumb<Color> thumb = JXGradientChooser.this.slider.getModel().getThumbAt(JXGradientChooser.this.slider.getSelectedIndex());
/*     */         
/* 584 */         int alpha = changeEvent.getSource() == JXGradientChooser.this.alphaSpinner ? ((Integer)JXGradientChooser.this.alphaSpinner.getValue()).intValue() : JXGradientChooser.this.alphaSlider.getValue();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 590 */         Color col = (Color)thumb.getObject();
/* 591 */         col = ColorUtil.setAlpha(col, alpha * 255 / 100);
/* 592 */         thumb.setObject(col);
/*     */         
/*     */ 
/* 595 */         if (changeEvent.getSource() == JXGradientChooser.this.alphaSpinner) {
/* 596 */           JXGradientChooser.this.alphaSlider.setValue(alpha);
/*     */         } else {
/* 598 */           JXGradientChooser.this.alphaSpinner.setValue(Integer.valueOf(alpha));
/*     */         }
/*     */         
/* 601 */         JXGradientChooser.this.recalcGradientFromStops();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final class AddThumbAction extends AbstractActionExt
/*     */   {
/*     */     public AddThumbAction() {
/* 609 */       super();
/*     */     }
/*     */     
/*     */     public void actionPerformed(ActionEvent actionEvent) {
/* 613 */       float pos = 0.2F;
/* 614 */       Color color = Color.black;
/* 615 */       int num = JXGradientChooser.this.slider.getModel().addThumb(pos, color);
/* 616 */       JXGradientChooser.this.log.fine("new number = " + num);
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
/*     */   private final class DeleteThumbAction
/*     */     extends AbstractActionExt
/*     */   {
/*     */     public DeleteThumbAction()
/*     */     {
/* 634 */       super();
/*     */     }
/*     */     
/*     */     public void actionPerformed(ActionEvent actionEvent) {
/* 638 */       int index = JXGradientChooser.this.slider.getSelectedIndex();
/* 639 */       if (index >= 0) {
/* 640 */         JXGradientChooser.this.slider.getModel().removeThumb(index);
/* 641 */         JXGradientChooser.this.updateFromStop(-1, -1.0F, null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private class StopListener
/*     */     implements ThumbListener
/*     */   {
/*     */     public StopListener() {}
/*     */     
/*     */     public void thumbMoved(int thumb, float pos)
/*     */     {
/* 653 */       JXGradientChooser.this.log.fine("moved: " + thumb + " " + pos);
/* 654 */       Color color = (Color)JXGradientChooser.this.slider.getModel().getThumbAt(thumb).getObject();
/* 655 */       JXGradientChooser.this.thumbsMoving = true;
/* 656 */       JXGradientChooser.this.updateFromStop(thumb, pos, color);
/* 657 */       JXGradientChooser.this.updateDeleteButtons();
/* 658 */       JXGradientChooser.this.thumbsMoving = false;
/*     */     }
/*     */     
/*     */ 
/*     */     public void thumbSelected(int thumb)
/*     */     {
/* 664 */       if (thumb == -1) {
/* 665 */         JXGradientChooser.this.updateFromStop(-1, -1.0F, Color.black);
/* 666 */         return;
/*     */       }
/* 668 */       JXGradientChooser.this.thumbsMoving = true;
/* 669 */       float pos = JXGradientChooser.this.slider.getModel().getThumbAt(thumb).getPosition();
/* 670 */       Color color = (Color)JXGradientChooser.this.slider.getModel().getThumbAt(thumb).getObject();
/* 671 */       JXGradientChooser.this.log.fine("selected = " + thumb + " " + pos + " " + color);
/* 672 */       JXGradientChooser.this.updateFromStop(thumb, pos, color);
/* 673 */       JXGradientChooser.this.updateDeleteButtons();
/* 674 */       JXGradientChooser.this.slider.repaint();
/* 675 */       JXGradientChooser.this.thumbsMoving = false;
/*     */     }
/*     */     
/*     */     public void mousePressed(MouseEvent e)
/*     */     {
/* 680 */       if (e.getClickCount() > 1)
/* 681 */         JXGradientChooser.this.selectColorForThumb();
/*     */     }
/*     */   }
/*     */   
/*     */   private final class RepaintOnEventListener implements ActionListener, ItemListener {
/*     */     private RepaintOnEventListener() {}
/*     */     
/* 688 */     public void actionPerformed(ActionEvent e) { JXGradientChooser.this.gradientPreview.setReflected(JXGradientChooser.this.reflectedRadio.isSelected());
/* 689 */       JXGradientChooser.this.gradientPreview.setReversed(JXGradientChooser.this.reversedCheck.isSelected());
/* 690 */       JXGradientChooser.this.gradientPreview.setRepeated(JXGradientChooser.this.repeatedRadio.isSelected());
/*     */       
/* 692 */       JXGradientChooser.this.recalcGradientFromStops();
/* 693 */       JXGradientChooser.this.gradientPreview.repaint();
/*     */     }
/*     */     
/*     */     public void itemStateChanged(ItemEvent e) {
/* 697 */       if (JXGradientChooser.this.styleCombo.getSelectedItem() == JXGradientChooser.GradientStyle.Radial) {
/* 698 */         JXGradientChooser.this.gradientPreview.setRadial(true);
/*     */       } else {
/* 700 */         JXGradientChooser.this.gradientPreview.setRadial(false);
/*     */       }
/* 702 */       JXGradientChooser.this.recalcGradientFromStops();
/*     */     }
/*     */   }
/*     */   
/*     */   private void selectColorForThumb() {
/* 707 */     int index = this.slider.getSelectedIndex();
/* 708 */     if (index >= 0) {
/* 709 */       Color color = this.changeColorButton.getBackground();
/* 710 */       this.slider.getModel().getThumbAt(index).setObject(color);
/* 711 */       updateFromStop(index, this.slider.getModel().getThumbAt(index).getPosition(), color);
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
/*     */   public static MultipleGradientPaint showDialog(Component comp, String title, MultipleGradientPaint mgrad)
/*     */   {
/* 726 */     Component root = SwingUtilities.getRoot(comp);
/* 727 */     JDialog dialog = new JDialog((JFrame)root, title, true);
/* 728 */     JXGradientChooser picker = new JXGradientChooser();
/* 729 */     if (mgrad != null) {
/* 730 */       picker.setGradient(mgrad);
/*     */     }
/* 732 */     dialog.add(picker);
/*     */     
/*     */ 
/* 735 */     JPanel panel = new JPanel();
/* 736 */     JButton cancel = new JButton("Cancel");
/* 737 */     cancel.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent actionEvent) {
/* 739 */         this.val$dialog.setVisible(false);
/*     */       }
/* 741 */     });
/* 742 */     JButton okay = new JButton("Ok");
/* 743 */     okay.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent actionEvent) {
/* 745 */         this.val$dialog.setVisible(false);
/*     */       }
/* 747 */     });
/* 748 */     okay.setDefaultCapable(true);
/*     */     
/*     */ 
/* 751 */     GridLayout gl = new GridLayout();
/* 752 */     gl.setHgap(2);
/* 753 */     panel.setLayout(gl);
/* 754 */     panel.add(cancel);
/* 755 */     panel.add(okay);
/*     */     
/* 757 */     JPanel p2 = new JPanel();
/* 758 */     p2.setLayout(new GridBagLayout());
/* 759 */     GridBagConstraints gbc = new GridBagConstraints();
/* 760 */     gbc.anchor = 13;
/* 761 */     gbc.weightx = 1.0D;
/* 762 */     p2.add(panel, gbc);
/* 763 */     dialog.add(p2, "South");
/*     */     
/* 765 */     dialog.getRootPane().setDefaultButton(okay);
/* 766 */     dialog.pack();
/* 767 */     dialog.setResizable(false);
/* 768 */     dialog.setVisible(true);
/*     */     
/* 770 */     return picker.getGradient();
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
/*     */   public static String toString(MultipleGradientPaint paint)
/*     */   {
/* 783 */     StringBuffer buffer = new StringBuffer();
/* 784 */     buffer.append(paint.getClass().getName());
/* 785 */     Color[] colors = paint.getColors();
/* 786 */     float[] values = paint.getFractions();
/* 787 */     buffer.append("[");
/* 788 */     for (int i = 0; i < colors.length; i++) {
/* 789 */       buffer.append("#").append(Integer.toHexString(colors[i].getRGB()));
/* 790 */       buffer.append(":");
/* 791 */       buffer.append(values[i]);
/* 792 */       buffer.append(", ");
/*     */     }
/* 794 */     buffer.append("]");
/* 795 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXGradientChooser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */