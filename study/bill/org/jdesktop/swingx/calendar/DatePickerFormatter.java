/*     */ package org.jdesktop.swingx.calendar;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.JFormattedTextField.AbstractFormatter;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import org.jdesktop.swingx.plaf.UIManagerExt;
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
/*     */ public class DatePickerFormatter
/*     */   extends JFormattedTextField.AbstractFormatter
/*     */ {
/*  46 */   private static final Logger LOG = Logger.getLogger(DatePickerFormatter.class.getName());
/*     */   
/*  48 */   private DateFormat[] _formats = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DatePickerFormatter()
/*     */   {
/*  66 */     this(null, null);
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
/*     */   public DatePickerFormatter(DateFormat[] formats)
/*     */   {
/*  79 */     this(formats, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DatePickerFormatter(Locale locale)
/*     */   {
/*  90 */     this(null, locale);
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
/*     */   public DatePickerFormatter(DateFormat[] formats, Locale locale)
/*     */   {
/* 105 */     if (locale == null) {
/* 106 */       locale = Locale.getDefault();
/*     */     }
/* 108 */     if (formats == null) {
/* 109 */       formats = createDefaultFormats(locale);
/*     */     }
/* 111 */     Contract.asNotNull(formats, "The array of DateFormats must not contain null formats");
/* 112 */     this._formats = formats;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DateFormat[] getFormats()
/*     */   {
/* 122 */     DateFormat[] results = new DateFormat[this._formats.length];
/* 123 */     System.arraycopy(this._formats, 0, results, 0, results.length);
/* 124 */     return results;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object stringToValue(String text)
/*     */     throws ParseException
/*     */   {
/* 132 */     Object result = null;
/* 133 */     ParseException pex = null;
/*     */     
/* 135 */     if ((text == null) || (text.trim().length() == 0)) {
/* 136 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 142 */     for (DateFormat _format : this._formats) {
/*     */       try {
/* 144 */         result = _format.parse(text);
/* 145 */         pex = null;
/*     */       }
/*     */       catch (ParseException ex) {
/* 148 */         pex = ex;
/*     */       }
/*     */     }
/*     */     
/* 152 */     if (pex != null) {
/* 153 */       throw pex;
/*     */     }
/*     */     
/* 156 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String valueToString(Object value)
/*     */     throws ParseException
/*     */   {
/* 164 */     if ((value != null) && (this._formats.length > 0)) {
/* 165 */       return this._formats[0].format(value);
/*     */     }
/* 167 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected DateFormat[] createDefaultFormats(Locale locale)
/*     */   {
/* 179 */     List<DateFormat> f = new ArrayList();
/* 180 */     addFormat(f, "JXDatePicker.longFormat", locale);
/* 181 */     addFormat(f, "JXDatePicker.mediumFormat", locale);
/* 182 */     addFormat(f, "JXDatePicker.shortFormat", locale);
/* 183 */     if (f.size() == 0) {
/* 184 */       addSystemDefaultFormat(f, locale);
/*     */     }
/* 186 */     return (DateFormat[])f.toArray(new DateFormat[f.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addSystemDefaultFormat(List<DateFormat> f, Locale locale)
/*     */   {
/* 197 */     f.add(DateFormat.getDateInstance(3, locale));
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
/*     */   private void addFormat(List<DateFormat> f, String key, Locale locale)
/*     */   {
/* 211 */     String pattern = UIManagerExt.getString(key, locale);
/* 212 */     if (pattern == null) return;
/*     */     try {
/* 214 */       SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
/* 215 */       f.add(format);
/*     */     }
/*     */     catch (RuntimeException e) {
/* 218 */       LOG.finer("creating date format failed for key/pattern: " + key + "/" + pattern);
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
/*     */   public static class DatePickerFormatterUIResource
/*     */     extends DatePickerFormatter
/*     */     implements UIResource
/*     */   {
/*     */     public DatePickerFormatterUIResource(Locale locale)
/*     */     {
/* 235 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public DatePickerFormatterUIResource()
/*     */     {
/* 242 */       this(null);
/*     */     }
/*     */     
/*     */     public DatePickerFormatterUIResource(DateFormat[] formats, Locale locale) {
/* 246 */       super(locale);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\calendar\DatePickerFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */