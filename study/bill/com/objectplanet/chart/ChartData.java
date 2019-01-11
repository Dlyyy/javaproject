/*      */ package com.objectplanet.chart;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Hashtable;
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
/*      */ public class ChartData
/*      */   implements Serializable
/*      */ {
/*      */   private ChartSample[][] data;
/*      */   private Hashtable sampleLookup;
/*      */   private String[] seriesLabels;
/*      */   private String[] sampleLabels;
/*      */   private int seriesCount;
/*      */   private int sampleCount;
/*      */   private long changedTime;
/*      */   
/*      */   public ChartData(int seriesCount, int sampleCount)
/*      */   {
/*   73 */     this.seriesCount = Math.max(0, seriesCount);
/*   74 */     this.sampleCount = Math.max(0, sampleCount);
/*      */     
/*   76 */     this.data = new ChartSample[this.seriesCount][];
/*   77 */     for (int serie = 0; serie < this.data.length; serie++) {
/*   78 */       this.data[serie] = new ChartSample[this.sampleCount];
/*      */     }
/*      */     
/*   81 */     this.seriesLabels = new String[this.seriesCount];
/*   82 */     this.sampleLabels = new String[this.sampleCount];
/*      */     
/*      */ 
/*   85 */     int count = Math.max(1, sampleCount) * Math.max(1, seriesCount);
/*   86 */     this.sampleLookup = new Hashtable(count);
/*   87 */     this.changedTime = System.currentTimeMillis();
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
/*      */   public void setSamples(int serie, ChartSample[] samples)
/*      */   {
/*  105 */     if ((serie < 0) || (serie >= this.data.length)) {
/*  106 */       throw new IllegalArgumentException("Invalid series: " + serie);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  111 */     if ((this.data[serie] == null) || (samples == null)) {
/*  112 */       this.data[serie] = new ChartSample[this.sampleCount];
/*      */     }
/*      */     
/*      */ 
/*  116 */     if (samples != null) {
/*  117 */       for (int sample = 0; sample < this.sampleCount; sample++) {
/*  118 */         if ((sample < samples.length) && (samples[sample] != null))
/*      */         {
/*  120 */           samples[sample].setIndex(sample);
/*  121 */           samples[sample].setSeries(serie);
/*  122 */           this.data[serie][sample] = samples[sample];
/*      */           
/*  124 */           if (samples[sample].key == null) {
/*  125 */             samples[sample].key = (serie + "." + sample);
/*      */           }
/*  127 */           this.sampleLookup.put(samples[sample].key, samples[sample]);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  134 */     setIndividualSampleLabels();
/*  135 */     this.changedTime = System.currentTimeMillis();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChartSample[] getSamples(int serie)
/*      */   {
/*      */     try
/*      */     {
/*  147 */       return this.data[serie];
/*      */     } catch (IndexOutOfBoundsException e) {
/*  149 */       throw new IllegalArgumentException("Invalid series: " + serie);
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
/*      */   public void setSample(int serie, int index, ChartSample sample)
/*      */   {
/*  164 */     if ((serie < 0) || (serie >= this.data.length)) {
/*  165 */       throw new IllegalArgumentException("Invalid series: " + serie);
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  170 */       sample.setIndex(index);
/*  171 */       sample.setSeries(serie);
/*  172 */       this.data[serie][index] = sample;
/*      */       
/*  174 */       if (sample.key == null) {
/*  175 */         sample.key = (serie + "." + index);
/*      */       }
/*  177 */       this.sampleLookup.put(sample.key, sample);
/*      */     } catch (IndexOutOfBoundsException e) {
/*  179 */       throw new IllegalArgumentException("Invalid index: " + index);
/*      */     } catch (NullPointerException e) {
/*  181 */       System.out.println("Internal error: setSample(serie, index, sample) method");
/*      */     }
/*      */     
/*  184 */     setIndividualSampleLabel(serie, index);
/*  185 */     this.changedTime = System.currentTimeMillis();
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
/*      */   public ChartSample getSample(int serie, int index)
/*      */   {
/*  198 */     if ((serie < 0) || (serie >= this.data.length)) {
/*  199 */       throw new IllegalArgumentException("Invalid series: " + serie);
/*      */     }
/*      */     try
/*      */     {
/*  203 */       return this.data[serie][index];
/*      */     } catch (IndexOutOfBoundsException e) {
/*  205 */       throw new IllegalArgumentException("Invalid index: " + index);
/*      */     } catch (NullPointerException e) {
/*  207 */       System.out.println("Internal error: getSample(serie, index"); }
/*  208 */     return null;
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
/*      */   public ChartSample getSample(Object key)
/*      */   {
/*  223 */     return (ChartSample)this.sampleLookup.get(key);
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
/*      */   public void setSampleValues(int serie, double[] values)
/*      */   {
/*  241 */     if ((serie < 0) || (serie >= this.data.length)) {
/*  242 */       throw new IllegalArgumentException("Invalid series: " + serie);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  247 */     if ((this.data[serie] == null) || (values == null)) {
/*  248 */       this.data[serie] = new ChartSample[this.sampleCount];
/*      */     }
/*      */     
/*  251 */     if (values != null) {
/*  252 */       for (int sample = 0; (sample < this.sampleCount) && (sample < this.data[serie].length); sample++)
/*      */       {
/*  254 */         double value = NaN.0D;
/*  255 */         if (sample < values.length) {
/*  256 */           value = values[sample];
/*      */         }
/*      */         
/*  259 */         if (this.data[serie][sample] == null) {
/*  260 */           this.data[serie][sample] = new ChartSample(sample);
/*  261 */           this.data[serie][sample].setSeries(serie);
/*      */           
/*  263 */           this.data[serie][sample].key = (serie + "." + sample);
/*  264 */           this.sampleLookup.put(this.data[serie][sample].key, this.data[serie][sample]);
/*      */         }
/*      */         
/*  267 */         this.data[serie][sample].setValue(value);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  272 */     setIndividualSampleLabels();
/*  273 */     this.changedTime = System.currentTimeMillis();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double[] getSampleValues(int serie)
/*      */   {
/*      */     try
/*      */     {
/*  286 */       ChartSample[] samples = this.data[serie];
/*      */       
/*  288 */       double[] values = new double[samples.length];
/*  289 */       for (int i = 0; i < samples.length; i++) {
/*  290 */         if ((samples[i] != null) && (samples[i].value != null) && (!samples[i].value.isNaN())) {
/*  291 */           values[i] = samples[i].getFloatValue();
/*      */         } else {
/*  293 */           values[i] = NaN.0D;
/*      */         }
/*      */       }
/*  296 */       return values;
/*      */     } catch (IndexOutOfBoundsException e) {
/*  298 */       throw new IllegalArgumentException("Invalid series: " + serie);
/*      */     } catch (Exception e) {
/*  300 */       System.out.println("Internal error: ChartData.getSampleValues(serie)"); }
/*  301 */     return null;
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
/*      */   public void setSampleValue(int serie, int index, double value)
/*      */   {
/*  315 */     if ((serie < 0) || (serie >= this.data.length)) {
/*  316 */       throw new IllegalArgumentException("Invalid series: " + serie);
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  321 */       if (this.data[serie][index] == null) {
/*  322 */         this.data[serie][index] = new ChartSample(index);
/*  323 */         this.data[serie][index].setSeries(serie);
/*      */         
/*  325 */         this.data[serie][index].key = (serie + "." + index);
/*  326 */         this.sampleLookup.put(this.data[serie][index].key, this.data[serie][index]);
/*      */       }
/*      */       
/*  329 */       this.data[serie][index].setValue(value);
/*      */     } catch (IndexOutOfBoundsException e) {
/*  331 */       throw new IllegalArgumentException("Invalid index: " + index);
/*      */     } catch (NullPointerException e) {
/*  333 */       System.out.println("Internal error: setSampleValue(serie, index, value) method");
/*      */     }
/*      */     
/*  336 */     setIndividualSampleLabel(serie, index);
/*  337 */     this.changedTime = System.currentTimeMillis();
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
/*      */   public double getSampleValue(int serie, int index)
/*      */   {
/*  350 */     if ((serie < 0) || (serie >= this.data.length)) {
/*  351 */       throw new IllegalArgumentException("Invalid series: " + serie);
/*      */     }
/*  353 */     if (this.data[serie] == null) {
/*  354 */       return NaN.0D;
/*      */     }
/*  356 */     if ((index < 0) || (index >= this.data[serie].length)) {
/*  357 */       throw new IllegalArgumentException("Invalid index: " + index);
/*      */     }
/*      */     
/*  360 */     if (this.data[serie][index] != null) {
/*  361 */       return this.data[serie][index].getFloatValue();
/*      */     }
/*  363 */     return NaN.0D;
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
/*      */   public int appendSample(int serie, ChartSample sample, boolean makeSpace)
/*      */   {
/*  393 */     if ((serie < 0) || (serie >= this.seriesCount)) {
/*  394 */       throw new IllegalArgumentException("Illegal serie: " + serie);
/*      */     }
/*  396 */     this.changedTime = System.currentTimeMillis();
/*      */     
/*      */ 
/*  399 */     int lastSampleIndex = -1;
/*  400 */     for (int i = 0; i < this.sampleCount; i++) {
/*  401 */       ChartSample s = this.data[serie][i];
/*  402 */       if ((s == null) || (s.value == null)) {
/*  403 */         lastSampleIndex = i;
/*  404 */         break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  409 */     if (lastSampleIndex >= 0) {
/*  410 */       setSample(serie, lastSampleIndex, sample);
/*  411 */       return lastSampleIndex;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  416 */     if (makeSpace)
/*      */     {
/*  418 */       int count = this.sampleCount;
/*  419 */       setSampleCount(count + 1);
/*  420 */       setSample(serie, count, sample);
/*  421 */       return count; }
/*  422 */     if (this.sampleCount > 0)
/*      */     {
/*  424 */       for (int i = 0; i < this.data[serie].length - 1; i++) {
/*  425 */         this.data[serie][i] = this.data[serie][(i + 1)];
/*      */       }
/*  427 */       setSample(serie, this.sampleCount - 1, sample);
/*  428 */       return this.sampleCount - 1;
/*      */     }
/*  430 */     return -1;
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
/*      */   public int appendSampleValue(int serie, double value, boolean makeSpace)
/*      */   {
/*  456 */     if ((serie < 0) || (serie >= this.seriesCount)) {
/*  457 */       throw new IllegalArgumentException("Illegal serie: " + serie);
/*      */     }
/*  459 */     this.changedTime = System.currentTimeMillis();
/*      */     
/*      */ 
/*  462 */     int lastSampleIndex = -1;
/*  463 */     for (int i = 0; i < this.sampleCount; i++) {
/*  464 */       ChartSample s = this.data[serie][i];
/*  465 */       if ((s == null) || (!s.hasValue())) {
/*  466 */         lastSampleIndex = i;
/*  467 */         break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  472 */     int return_index = lastSampleIndex;
/*  473 */     if (lastSampleIndex >= 0) {
/*  474 */       setSampleValue(serie, lastSampleIndex, value);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*  480 */     else if (makeSpace)
/*      */     {
/*  482 */       int count = this.sampleCount;
/*  483 */       setSampleCount(count + 1);
/*  484 */       setSampleValue(serie, count, value);
/*  485 */       return_index = count;
/*  486 */     } else if (this.sampleCount > 0)
/*      */     {
/*  488 */       double[] values = getSampleValues(serie);
/*  489 */       for (int i = 0; i < values.length - 1; i++) {
/*  490 */         values[i] = values[(i + 1)];
/*      */       }
/*  492 */       values[(values.length - 1)] = value;
/*  493 */       setSampleValues(serie, values);
/*  494 */       return_index = values.length - 1;
/*      */     }
/*      */     
/*  497 */     return return_index;
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
/*      */   public int appendSampleLabel(String label, boolean makeSpace)
/*      */   {
/*  512 */     this.changedTime = System.currentTimeMillis();
/*      */     
/*      */ 
/*  515 */     int lastSampleIndex = -1;
/*  516 */     int sampleCount = getSampleCount();
/*  517 */     if (this.sampleLabels != null) {
/*  518 */       for (int i = 0; i < sampleCount; i++) {
/*  519 */         if (getSampleLabel(i) == null) {
/*  520 */           lastSampleIndex = i;
/*  521 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  527 */     int return_index = lastSampleIndex;
/*  528 */     if (lastSampleIndex >= 0) {
/*  529 */       setSampleLabel(lastSampleIndex, label);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*  535 */     else if (makeSpace)
/*      */     {
/*  537 */       setSampleLabel(this.sampleLabels.length, label);
/*  538 */       return_index = this.sampleLabels.length;
/*  539 */     } else if (sampleCount > 0)
/*      */     {
/*  541 */       for (int i = 0; i < this.sampleLabels.length - 1; i++) {
/*  542 */         this.sampleLabels[i] = this.sampleLabels[(i + 1)];
/*      */       }
/*  544 */       this.sampleLabels[(this.sampleLabels.length - 1)] = label;
/*  545 */       return_index = this.sampleLabels.length - 1;
/*      */     }
/*      */     
/*  548 */     return return_index;
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
/*      */   public void setSampleLabels(String[] labels)
/*      */   {
/*  562 */     if ((labels != null) && (labels.length > this.sampleLabels.length)) {
/*  563 */       this.sampleLabels = new String[labels.length];
/*      */     }
/*      */     
/*      */ 
/*  567 */     int count = Math.max(labels != null ? labels.length : 0, this.sampleLabels.length);
/*  568 */     for (int i = 0; i < count; i++) {
/*  569 */       if (i < this.sampleLabels.length) {
/*  570 */         if (labels == null) {
/*  571 */           this.sampleLabels[i] = null;
/*  572 */         } else if (i < labels.length) {
/*  573 */           this.sampleLabels[i] = labels[i];
/*      */         } else {
/*  575 */           this.sampleLabels[i] = null;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  581 */     setIndividualSampleLabels();
/*  582 */     this.changedTime = System.currentTimeMillis();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getSampleLabels()
/*      */   {
/*  592 */     if (this.sampleLabels != null) {
/*  593 */       return this.sampleLabels;
/*      */     }
/*  595 */     return new String[0];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSampleLabel(int index, String label)
/*      */   {
/*  607 */     if (index < 0) {
/*  608 */       return;
/*      */     }
/*      */     
/*      */ 
/*  612 */     if ((this.sampleLabels == null) || (index >= this.sampleLabels.length)) {
/*  613 */       int count = Math.max(getSampleCount(), index + 1);
/*  614 */       String[] newSampleLabels = new String[count];
/*  615 */       if (this.sampleLabels != null) {
/*  616 */         System.arraycopy(this.sampleLabels, 0, newSampleLabels, 0, Math.min(count, this.sampleLabels.length));
/*      */       }
/*  618 */       this.sampleLabels = newSampleLabels;
/*      */     }
/*      */     
/*      */ 
/*  622 */     this.sampleLabels[index] = label;
/*  623 */     for (int serie = 0; serie < this.seriesCount; serie++) {
/*  624 */       setIndividualSampleLabel(serie, index);
/*      */     }
/*  626 */     this.changedTime = System.currentTimeMillis();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSampleLabel(int index)
/*      */   {
/*      */     try
/*      */     {
/*  638 */       return this.sampleLabels[index];
/*      */     } catch (IndexOutOfBoundsException e) {
/*  640 */       throw new IllegalArgumentException("Invalid index: " + index);
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
/*      */   public void setSeriesLabels(String[] labels)
/*      */   {
/*  653 */     for (int i = 0; i < this.seriesCount; i++) {
/*  654 */       if (labels == null) {
/*  655 */         this.seriesLabels[i] = null;
/*  656 */       } else if (i < labels.length) {
/*  657 */         this.seriesLabels[i] = labels[i];
/*      */       } else {
/*  659 */         this.seriesLabels[i] = null;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  664 */     setIndividualSampleLabels();
/*  665 */     this.changedTime = System.currentTimeMillis();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getSeriesLabels()
/*      */   {
/*  675 */     String[] labels = new String[this.seriesCount];
/*  676 */     System.arraycopy(this.seriesLabels, 0, labels, 0, labels.length);
/*  677 */     return labels;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSeriesLabel(int serie, String label)
/*      */   {
/*      */     try
/*      */     {
/*  689 */       this.seriesLabels[serie] = label;
/*  690 */       for (int index = 0; index < this.sampleCount; index++) {
/*  691 */         setIndividualSampleLabel(serie, index);
/*      */       }
/*      */     } catch (IndexOutOfBoundsException e) {
/*  694 */       throw new IllegalArgumentException("Invalid series: " + serie);
/*      */     }
/*  696 */     this.changedTime = System.currentTimeMillis();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSeriesLabel(int serie)
/*      */   {
/*      */     try
/*      */     {
/*  708 */       return this.seriesLabels[serie];
/*      */     } catch (IndexOutOfBoundsException e) {
/*  710 */       throw new IllegalArgumentException("Invalid series: " + serie);
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
/*      */   public void setSelection(int serie, int sample, boolean selected)
/*      */   {
/*  728 */     setSelection(serie, sample, selected, false);
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
/*      */   void setSelection(int serie, int sample, boolean selected, boolean clear)
/*      */   {
/*  744 */     if (clear) {
/*  745 */       setSelection(false);
/*      */     }
/*      */     
/*      */ 
/*  749 */     if ((serie == -1) && (sample == -1)) {
/*  750 */       for (int i = 0; i < this.seriesCount; i++) {
/*  751 */         for (int j = 0; j < this.sampleCount; j++) {
/*  752 */           if ((this.data[i] != null) && (this.data[i][j] != null)) {
/*  753 */             this.data[i][j].setSelection(selected);
/*      */           }
/*      */           
/*      */         }
/*      */         
/*      */       }
/*      */       
/*  760 */     } else if (serie == -1) {
/*  761 */       for (int i = 0; i < this.sampleCount; i++) {
/*  762 */         if ((this.data[i] != null) && (sample >= 0) && (sample < this.data[i].length) && (this.data[i][sample] != null)) {
/*  763 */           this.data[i][sample].setSelection(selected);
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */     }
/*  769 */     else if (sample == -1) {
/*  770 */       for (int i = 0; i < this.sampleCount; i++) {
/*  771 */         if ((serie >= 0) && (serie < this.data.length) && (this.data[serie] != null) && (this.data[serie][i] != null)) {
/*  772 */           this.data[serie][i].setSelection(selected);
/*      */ 
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */     }
/*  779 */     else if ((this.data != null) && (serie >= 0) && (serie < this.data.length) && (this.data[serie] != null) && 
/*  780 */       (sample >= 0) && (sample < this.data[serie].length) && (this.data[serie][sample] != null)) {
/*  781 */       this.data[serie][sample].setSelection(selected);
/*      */     }
/*      */     
/*      */ 
/*  785 */     this.changedTime = System.currentTimeMillis();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setSelection(boolean selected)
/*      */   {
/*  794 */     for (int serie = 0; serie < this.seriesCount; serie++) {
/*  795 */       for (int sample = 0; sample < this.sampleCount; sample++) {
/*      */         try {
/*  797 */           if ((this.data[serie] != null) && (this.data[serie][sample] != null)) {
/*  798 */             this.data[serie][sample].setSelection(selected);
/*      */           }
/*      */         }
/*      */         catch (Exception localException) {}
/*      */       }
/*      */     }
/*      */     
/*  805 */     this.changedTime = System.currentTimeMillis();
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
/*      */   public boolean isSelected(int serie, int sample)
/*      */   {
/*  818 */     if ((serie < -1) || (serie >= this.seriesCount)) {
/*  819 */       throw new IllegalArgumentException("Invalid series index: " + serie);
/*      */     }
/*  821 */     if ((sample < -1) || (sample >= this.sampleCount)) {
/*  822 */       throw new IllegalArgumentException("Invalid sample index: " + sample);
/*      */     }
/*      */     
/*      */ 
/*  826 */     boolean selected = true;
/*  827 */     if ((this.data != null) && (serie == -1) && (sample == -1)) {
/*  828 */       int i = 0;
/*  829 */       do { for (int j = 0; j < this.sampleCount; j++) {
/*  830 */           if ((this.data[i] == null) || (this.data[i][j] == null) || (!this.data[i][j].isSelected())) {
/*  831 */             selected = false;
/*      */           }
/*      */         }
/*  828 */         i++; if (i >= this.seriesCount) break; } while (i < this.data.length);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*  838 */     else if ((this.data != null) && (sample == -1) && (serie >= 0)) {
/*  839 */       for (int i = 0; i < this.sampleCount; i++) {
/*  840 */         if ((serie < this.data.length) && (this.data[serie] != null) && (i < this.data[serie].length) && (this.data[serie][i] != null) && 
/*  841 */           (!this.data[serie][i].isSelected())) {
/*  842 */           selected = false;
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*  849 */     else if ((this.data != null) && (serie == -1)) {
/*  850 */       int i = 0;
/*  851 */       do { if ((this.data[i] != null) && (sample < this.data[i].length) && (this.data[i][sample] != null) && 
/*  852 */           (!this.data[i][sample].isSelected())) {
/*  853 */           selected = false;
/*      */         }
/*  850 */         i++; if (i >= this.seriesCount) break; } while (i < this.data.length);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*  860 */     else if ((this.data != null) && (
/*  861 */       (this.data[serie] == null) || (this.data[serie][sample] == null) || (!this.data[serie][sample].isSelected()))) {
/*  862 */       selected = false;
/*      */     }
/*      */     
/*      */ 
/*  866 */     return selected;
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
/*      */   public void setSeriesCount(int count)
/*      */   {
/*  879 */     setDataCount(count, this.sampleCount);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getSeriesCount()
/*      */   {
/*  887 */     return this.seriesCount;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSampleCount(int count)
/*      */   {
/*  896 */     setDataCount(this.seriesCount, count);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getSampleCount()
/*      */   {
/*  904 */     return this.sampleCount;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getMaxValue(int serie)
/*      */   {
/*  916 */     boolean maximumOfOneSeries = (serie >= 0) && (serie < this.seriesCount);
/*  917 */     boolean sampleFound = false;
/*      */     
/*      */ 
/*  920 */     double max = -1.7976931348623157E308D;
/*  921 */     if (maximumOfOneSeries) {
/*  922 */       for (int i = 0; i < this.sampleCount; i++) {
/*  923 */         ChartSample sample = this.data[serie][i];
/*  924 */         if ((sample != null) && (sample.value != null) && (!sample.value.isNaN())) {
/*  925 */           max = Math.max(max, sample.getFloatValue());
/*  926 */           sampleFound = true;
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */     }
/*      */     else {
/*  933 */       for (int i = 0; i < this.seriesCount; i++) {
/*  934 */         for (int j = 0; j < this.sampleCount; j++) {
/*  935 */           ChartSample sample = this.data[i][j];
/*  936 */           if ((sample != null) && (sample.value != null) && (!sample.value.isNaN())) {
/*  937 */             max = Math.max(max, sample.getFloatValue());
/*  938 */             sampleFound = true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  946 */     if (sampleFound) {
/*  947 */       return max;
/*      */     }
/*  949 */     return 0.0D;
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
/*      */   public double getMinValue(int serie)
/*      */   {
/*  962 */     boolean minimumOfOneSeries = (serie >= 0) && (serie < this.seriesCount);
/*  963 */     boolean sampleFound = false;
/*      */     
/*      */ 
/*  966 */     double min = Double.MAX_VALUE;
/*  967 */     if (minimumOfOneSeries) {
/*  968 */       for (int i = 0; i < this.sampleCount; i++) {
/*  969 */         ChartSample sample = this.data[serie][i];
/*  970 */         if ((sample != null) && (sample.value != null) && (!sample.value.isNaN())) {
/*  971 */           min = Math.min(min, sample.getFloatValue());
/*  972 */           sampleFound = true;
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */     }
/*      */     else {
/*  979 */       for (int i = 0; i < this.seriesCount; i++) {
/*  980 */         for (int j = 0; j < this.sampleCount; j++) {
/*  981 */           ChartSample sample = this.data[i][j];
/*  982 */           if ((sample != null) && (sample.value != null) && (!sample.value.isNaN())) {
/*  983 */             min = Math.min(min, sample.getFloatValue());
/*  984 */             sampleFound = true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  991 */     if (sampleFound) {
/*  992 */       return min;
/*      */     }
/*  994 */     return 0.0D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasChangedSince(long time)
/*      */   {
/* 1005 */     return this.changedTime >= time;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1013 */     return "ChartData " + this.seriesCount + " series, " + this.sampleCount + " samples";
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
/*      */   private void setDataCount(int seriesCount, int sampleCount)
/*      */   {
/* 1027 */     if ((this.seriesCount == seriesCount) && (this.sampleCount == sampleCount)) {
/* 1028 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1032 */     this.seriesCount = Math.max(0, seriesCount);
/* 1033 */     this.sampleCount = Math.max(0, sampleCount);
/*      */     
/*      */ 
/* 1036 */     ChartSample[][] newData = new ChartSample[this.seriesCount][this.sampleCount];
/* 1037 */     for (int serie = 0; serie < newData.length; serie++) {
/* 1038 */       if (serie < this.data.length) {
/* 1039 */         ChartSample[] samples = newData[serie];
/* 1040 */         for (int i = 0; i < samples.length; i++) {
/* 1041 */           if (i < this.data[serie].length) {
/* 1042 */             newData[serie][i] = this.data[serie][i];
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1047 */     this.data = newData;
/*      */     
/*      */ 
/* 1050 */     String[] newSeriesLabels = new String[this.seriesCount];
/* 1051 */     for (int i = 0; i < newSeriesLabels.length; i++) {
/* 1052 */       if (i < this.seriesLabels.length) {
/* 1053 */         newSeriesLabels[i] = this.seriesLabels[i];
/*      */       }
/*      */     }
/* 1056 */     this.seriesLabels = newSeriesLabels;
/*      */     
/*      */ 
/* 1059 */     String[] newSampleLabels = new String[this.sampleCount];
/* 1060 */     for (int i = 0; i < newSampleLabels.length; i++) {
/* 1061 */       if (i < this.sampleLabels.length) {
/* 1062 */         newSampleLabels[i] = this.sampleLabels[i];
/*      */       }
/*      */     }
/* 1065 */     this.sampleLabels = newSampleLabels;
/* 1066 */     this.changedTime = System.currentTimeMillis();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setIndividualSampleLabel(int serie, int index)
/*      */   {
/* 1077 */     if ((this.data != null) && (serie < this.data.length) && (this.data[serie] != null) && (index < this.data[serie].length)) {
/* 1078 */       ChartSample sample = this.data[serie][index];
/* 1079 */       if (sample != null) {
/* 1080 */         String seriesLabel = this.seriesLabels[serie];
/* 1081 */         String sampleLabel = null;
/* 1082 */         if (index < this.sampleLabels.length) {
/* 1083 */           sampleLabel = this.sampleLabels[index];
/*      */         }
/* 1085 */         String label = "";
/* 1086 */         if ((seriesLabel != null) && (sampleLabel != null)) {
/* 1087 */           label = seriesLabel + " " + sampleLabel;
/* 1088 */         } else if (seriesLabel != null) {
/* 1089 */           label = seriesLabel;
/* 1090 */         } else if (sampleLabel != null) {
/* 1091 */           label = sampleLabel;
/*      */         }
/* 1093 */         sample.setLabel(label);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setIndividualSampleLabels()
/*      */   {
/* 1104 */     for (int serie = 0; serie < this.seriesCount; serie++)
/*      */     {
/* 1106 */       ChartSample[] samples = this.data[serie];
/* 1107 */       if (samples != null)
/*      */       {
/*      */ 
/*      */ 
/* 1111 */         for (int i = 0; i < samples.length; i++) {
/* 1112 */           if (samples[i] != null) {
/* 1113 */             String seriesLabel = this.seriesLabels[serie];
/* 1114 */             String sampleLabel = this.sampleLabels[i];
/* 1115 */             if ((seriesLabel != null) && (sampleLabel != null)) {
/* 1116 */               samples[i].setLabel(seriesLabel + " " + sampleLabel);
/* 1117 */             } else if (seriesLabel != null) {
/* 1118 */               samples[i].setLabel(seriesLabel);
/* 1119 */             } else if (sampleLabel != null) {
/* 1120 */               samples[i].setLabel(sampleLabel);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\objectplanet\chart\ChartData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */