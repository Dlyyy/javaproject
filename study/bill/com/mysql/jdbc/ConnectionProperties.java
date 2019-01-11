/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.log.Log;
/*      */ import com.mysql.jdbc.log.StandardLogger;
/*      */ import java.io.Serializable;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Field;
/*      */ import java.sql.DriverPropertyInfo;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.TreeMap;
/*      */ import javax.naming.RefAddr;
/*      */ import javax.naming.Reference;
/*      */ import javax.naming.StringRefAddr;
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
/*      */ public class ConnectionProperties
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 4257801713007640580L;
/*      */   private static final String CONNECTION_AND_AUTH_CATEGORY = "Connection/Authentication";
/*      */   private static final String NETWORK_CATEGORY = "Networking";
/*      */   private static final String DEBUGING_PROFILING_CATEGORY = "Debuging/Profiling";
/*      */   private static final String HA_CATEGORY = "High Availability and Clustering";
/*      */   private static final String MISC_CATEGORY = "Miscellaneous";
/*      */   private static final String PERFORMANCE_CATEGORY = "Performance Extensions";
/*      */   private static final String SECURITY_CATEGORY = "Security";
/*      */   
/*      */   class BooleanConnectionProperty
/*      */     extends ConnectionProperties.ConnectionProperty
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 2540132501709159404L;
/*      */     
/*      */     BooleanConnectionProperty(String propertyNameToSet, boolean defaultValueToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory)
/*      */     {
/*   77 */       super(propertyNameToSet, new Boolean(defaultValueToSet), null, 0, 0, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     String[] getAllowableValues()
/*      */     {
/*   86 */       return new String[] { "true", "false", "yes", "no" };
/*      */     }
/*      */     
/*      */     boolean getValueAsBoolean() {
/*   90 */       return ((Boolean)this.valueAsObject).booleanValue();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     boolean hasValueConstraints()
/*      */     {
/*   97 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */     void initializeFrom(String extractedValue)
/*      */       throws SQLException
/*      */     {
/*  104 */       if (extractedValue != null) {
/*  105 */         validateStringValues(extractedValue);
/*      */         
/*  107 */         this.valueAsObject = new Boolean((extractedValue.equalsIgnoreCase("TRUE")) || (extractedValue.equalsIgnoreCase("YES")));
/*      */       }
/*      */       else
/*      */       {
/*  111 */         this.valueAsObject = this.defaultValue;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     boolean isRangeBased()
/*      */     {
/*  119 */       return false;
/*      */     }
/*      */     
/*      */     void setValue(boolean valueFlag) {
/*  123 */       this.valueAsObject = new Boolean(valueFlag);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   abstract class ConnectionProperty
/*      */     implements Serializable
/*      */   {
/*      */     String[] allowableValues;
/*      */     
/*      */     String categoryName;
/*      */     
/*      */     Object defaultValue;
/*      */     
/*      */     int lowerBound;
/*      */     
/*      */     int order;
/*      */     
/*      */     String propertyName;
/*      */     
/*      */     String sinceVersion;
/*      */     
/*      */     int upperBound;
/*      */     
/*      */     Object valueAsObject;
/*      */     
/*      */     boolean required;
/*      */     
/*      */     String description;
/*      */     
/*      */     public ConnectionProperty() {}
/*      */     
/*      */     ConnectionProperty(String propertyNameToSet, Object defaultValueToSet, String[] allowableValuesToSet, int lowerBoundToSet, int upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory)
/*      */     {
/*  157 */       this.description = descriptionToSet;
/*  158 */       this.propertyName = propertyNameToSet;
/*  159 */       this.defaultValue = defaultValueToSet;
/*  160 */       this.valueAsObject = defaultValueToSet;
/*  161 */       this.allowableValues = allowableValuesToSet;
/*  162 */       this.lowerBound = lowerBoundToSet;
/*  163 */       this.upperBound = upperBoundToSet;
/*  164 */       this.required = false;
/*  165 */       this.sinceVersion = sinceVersionToSet;
/*  166 */       this.categoryName = category;
/*  167 */       this.order = orderInCategory;
/*      */     }
/*      */     
/*      */     String[] getAllowableValues() {
/*  171 */       return this.allowableValues;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     String getCategoryName()
/*      */     {
/*  178 */       return this.categoryName;
/*      */     }
/*      */     
/*      */     Object getDefaultValue() {
/*  182 */       return this.defaultValue;
/*      */     }
/*      */     
/*      */     int getLowerBound() {
/*  186 */       return this.lowerBound;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     int getOrder()
/*      */     {
/*  193 */       return this.order;
/*      */     }
/*      */     
/*      */     String getPropertyName() {
/*  197 */       return this.propertyName;
/*      */     }
/*      */     
/*      */     int getUpperBound() {
/*  201 */       return this.upperBound;
/*      */     }
/*      */     
/*      */     Object getValueAsObject() {
/*  205 */       return this.valueAsObject;
/*      */     }
/*      */     
/*      */     abstract boolean hasValueConstraints();
/*      */     
/*      */     void initializeFrom(Properties extractFrom) throws SQLException {
/*  211 */       String extractedValue = extractFrom.getProperty(getPropertyName());
/*  212 */       extractFrom.remove(getPropertyName());
/*  213 */       initializeFrom(extractedValue);
/*      */     }
/*      */     
/*      */     void initializeFrom(Reference ref) throws SQLException {
/*  217 */       RefAddr refAddr = ref.get(getPropertyName());
/*      */       
/*  219 */       if (refAddr != null) {
/*  220 */         String refContentAsString = (String)refAddr.getContent();
/*      */         
/*  222 */         initializeFrom(refContentAsString);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     abstract void initializeFrom(String paramString)
/*      */       throws SQLException;
/*      */     
/*      */ 
/*      */     abstract boolean isRangeBased();
/*      */     
/*      */     void setCategoryName(String categoryName)
/*      */     {
/*  235 */       this.categoryName = categoryName;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     void setOrder(int order)
/*      */     {
/*  243 */       this.order = order;
/*      */     }
/*      */     
/*      */     void setValueAsObject(Object obj) {
/*  247 */       this.valueAsObject = obj;
/*      */     }
/*      */     
/*      */     void storeTo(Reference ref) {
/*  251 */       if (getValueAsObject() != null) {
/*  252 */         ref.add(new StringRefAddr(getPropertyName(), getValueAsObject().toString()));
/*      */       }
/*      */     }
/*      */     
/*      */     DriverPropertyInfo getAsDriverPropertyInfo()
/*      */     {
/*  258 */       DriverPropertyInfo dpi = new DriverPropertyInfo(this.propertyName, null);
/*  259 */       dpi.choices = getAllowableValues();
/*  260 */       dpi.value = (this.valueAsObject != null ? this.valueAsObject.toString() : null);
/*  261 */       dpi.required = this.required;
/*  262 */       dpi.description = this.description;
/*      */       
/*  264 */       return dpi;
/*      */     }
/*      */     
/*      */     void validateStringValues(String valueToValidate) throws SQLException
/*      */     {
/*  269 */       String[] validateAgainst = getAllowableValues();
/*      */       
/*  271 */       if (valueToValidate == null) {
/*  272 */         return;
/*      */       }
/*      */       
/*  275 */       if ((validateAgainst == null) || (validateAgainst.length == 0)) {
/*  276 */         return;
/*      */       }
/*      */       
/*  279 */       for (int i = 0; i < validateAgainst.length; i++) {
/*  280 */         if ((validateAgainst[i] != null) && (validateAgainst[i].equalsIgnoreCase(valueToValidate)))
/*      */         {
/*  282 */           return;
/*      */         }
/*      */       }
/*      */       
/*  286 */       StringBuffer errorMessageBuf = new StringBuffer();
/*      */       
/*  288 */       errorMessageBuf.append("The connection property '");
/*  289 */       errorMessageBuf.append(getPropertyName());
/*  290 */       errorMessageBuf.append("' only accepts values of the form: ");
/*      */       
/*  292 */       if (validateAgainst.length != 0) {
/*  293 */         errorMessageBuf.append("'");
/*  294 */         errorMessageBuf.append(validateAgainst[0]);
/*  295 */         errorMessageBuf.append("'");
/*      */         
/*  297 */         for (int i = 1; i < validateAgainst.length - 1; i++) {
/*  298 */           errorMessageBuf.append(", ");
/*  299 */           errorMessageBuf.append("'");
/*  300 */           errorMessageBuf.append(validateAgainst[i]);
/*  301 */           errorMessageBuf.append("'");
/*      */         }
/*      */         
/*  304 */         errorMessageBuf.append(" or '");
/*  305 */         errorMessageBuf.append(validateAgainst[(validateAgainst.length - 1)]);
/*      */         
/*  307 */         errorMessageBuf.append("'");
/*      */       }
/*      */       
/*  310 */       errorMessageBuf.append(". The value '");
/*  311 */       errorMessageBuf.append(valueToValidate);
/*  312 */       errorMessageBuf.append("' is not in this set.");
/*      */       
/*  314 */       throw SQLError.createSQLException(errorMessageBuf.toString(), "S1009");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   class IntegerConnectionProperty
/*      */     extends ConnectionProperties.ConnectionProperty
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -3004305481796850832L;
/*      */     
/*      */ 
/*      */     public IntegerConnectionProperty(String propertyNameToSet, Object defaultValueToSet, String[] allowableValuesToSet, int lowerBoundToSet, int upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory)
/*      */     {
/*  328 */       super(propertyNameToSet, defaultValueToSet, allowableValuesToSet, lowerBoundToSet, upperBoundToSet, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  333 */     int multiplier = 1;
/*      */     
/*      */ 
/*      */ 
/*      */     IntegerConnectionProperty(String propertyNameToSet, int defaultValueToSet, int lowerBoundToSet, int upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory)
/*      */     {
/*  339 */       super(propertyNameToSet, new Integer(defaultValueToSet), null, lowerBoundToSet, upperBoundToSet, descriptionToSet, sinceVersionToSet, category, orderInCategory);
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
/*      */ 
/*      */     IntegerConnectionProperty(String propertyNameToSet, int defaultValueToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory)
/*      */     {
/*  357 */       this(propertyNameToSet, defaultValueToSet, 0, 0, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     String[] getAllowableValues()
/*      */     {
/*  365 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     int getLowerBound()
/*      */     {
/*  372 */       return this.lowerBound;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     int getUpperBound()
/*      */     {
/*  379 */       return this.upperBound;
/*      */     }
/*      */     
/*      */     int getValueAsInt() {
/*  383 */       return ((Integer)this.valueAsObject).intValue();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     boolean hasValueConstraints()
/*      */     {
/*  390 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */     void initializeFrom(String extractedValue)
/*      */       throws SQLException
/*      */     {
/*  397 */       if (extractedValue != null) {
/*      */         try
/*      */         {
/*  400 */           int intValue = Double.valueOf(extractedValue).intValue();
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
/*  411 */           this.valueAsObject = new Integer(intValue * this.multiplier);
/*      */         } catch (NumberFormatException nfe) {
/*  413 */           throw SQLError.createSQLException("The connection property '" + getPropertyName() + "' only accepts integer values. The value '" + extractedValue + "' can not be converted to an integer.", "S1009");
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  421 */         this.valueAsObject = this.defaultValue;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     boolean isRangeBased()
/*      */     {
/*  429 */       return getUpperBound() != getLowerBound();
/*      */     }
/*      */     
/*      */     void setValue(int valueFlag) {
/*  433 */       this.valueAsObject = new Integer(valueFlag);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public class LongConnectionProperty
/*      */     extends ConnectionProperties.IntegerConnectionProperty
/*      */   {
/*      */     private static final long serialVersionUID = 6068572984340480895L;
/*      */     
/*      */     LongConnectionProperty(String propertyNameToSet, long defaultValueToSet, long lowerBoundToSet, long upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory)
/*      */     {
/*  445 */       super(propertyNameToSet, new Long(defaultValueToSet), null, (int)lowerBoundToSet, (int)upperBoundToSet, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     LongConnectionProperty(String propertyNameToSet, long defaultValueToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory)
/*      */     {
/*  454 */       this(propertyNameToSet, defaultValueToSet, 0L, 0L, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     void setValue(long value)
/*      */     {
/*  461 */       this.valueAsObject = new Long(value);
/*      */     }
/*      */     
/*      */     long getValueAsLong() {
/*  465 */       return ((Long)this.valueAsObject).longValue();
/*      */     }
/*      */     
/*      */     void initializeFrom(String extractedValue) throws SQLException {
/*  469 */       if (extractedValue != null) {
/*      */         try
/*      */         {
/*  472 */           long longValue = Double.valueOf(extractedValue).longValue();
/*      */           
/*  474 */           this.valueAsObject = new Long(longValue);
/*      */         } catch (NumberFormatException nfe) {
/*  476 */           throw SQLError.createSQLException("The connection property '" + getPropertyName() + "' only accepts long integer values. The value '" + extractedValue + "' can not be converted to a long integer.", "S1009");
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  484 */         this.valueAsObject = this.defaultValue;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   class MemorySizeConnectionProperty
/*      */     extends ConnectionProperties.IntegerConnectionProperty
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 7351065128998572656L;
/*      */     
/*      */     MemorySizeConnectionProperty(String propertyNameToSet, int defaultValueToSet, int lowerBoundToSet, int upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory)
/*      */     {
/*  497 */       super(propertyNameToSet, defaultValueToSet, lowerBoundToSet, upperBoundToSet, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */     }
/*      */     
/*      */ 
/*      */     void initializeFrom(String extractedValue)
/*      */       throws SQLException
/*      */     {
/*  504 */       if (extractedValue != null) {
/*  505 */         if ((extractedValue.endsWith("k")) || (extractedValue.endsWith("K")) || (extractedValue.endsWith("kb")) || (extractedValue.endsWith("Kb")) || (extractedValue.endsWith("kB")))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*  510 */           this.multiplier = 1024;
/*  511 */           int indexOfK = StringUtils.indexOfIgnoreCase(extractedValue, "k");
/*      */           
/*  513 */           extractedValue = extractedValue.substring(0, indexOfK);
/*  514 */         } else if ((extractedValue.endsWith("m")) || (extractedValue.endsWith("M")) || (extractedValue.endsWith("G")) || (extractedValue.endsWith("mb")) || (extractedValue.endsWith("Mb")) || (extractedValue.endsWith("mB")))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  520 */           this.multiplier = 1048576;
/*  521 */           int indexOfM = StringUtils.indexOfIgnoreCase(extractedValue, "m");
/*      */           
/*  523 */           extractedValue = extractedValue.substring(0, indexOfM);
/*  524 */         } else if ((extractedValue.endsWith("g")) || (extractedValue.endsWith("G")) || (extractedValue.endsWith("gb")) || (extractedValue.endsWith("Gb")) || (extractedValue.endsWith("gB")))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*  529 */           this.multiplier = 1073741824;
/*  530 */           int indexOfG = StringUtils.indexOfIgnoreCase(extractedValue, "g");
/*      */           
/*  532 */           extractedValue = extractedValue.substring(0, indexOfG);
/*      */         }
/*      */       }
/*      */       
/*  536 */       super.initializeFrom(extractedValue);
/*      */     }
/*      */     
/*      */     void setValue(String value) throws SQLException {
/*  540 */       initializeFrom(value);
/*      */     }
/*      */   }
/*      */   
/*      */   class StringConnectionProperty
/*      */     extends ConnectionProperties.ConnectionProperty implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 5432127962785948272L;
/*      */     
/*      */     StringConnectionProperty(String propertyNameToSet, String defaultValueToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory)
/*      */     {
/*  551 */       this(propertyNameToSet, defaultValueToSet, null, descriptionToSet, sinceVersionToSet, category, orderInCategory);
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
/*      */ 
/*      */     StringConnectionProperty(String propertyNameToSet, String defaultValueToSet, String[] allowableValuesToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory)
/*      */     {
/*  569 */       super(propertyNameToSet, defaultValueToSet, allowableValuesToSet, 0, 0, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */     }
/*      */     
/*      */ 
/*      */     String getValueAsString()
/*      */     {
/*  575 */       return (String)this.valueAsObject;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     boolean hasValueConstraints()
/*      */     {
/*  582 */       return (this.allowableValues != null) && (this.allowableValues.length > 0);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     void initializeFrom(String extractedValue)
/*      */       throws SQLException
/*      */     {
/*  590 */       if (extractedValue != null) {
/*  591 */         validateStringValues(extractedValue);
/*      */         
/*  593 */         this.valueAsObject = extractedValue;
/*      */       } else {
/*  595 */         this.valueAsObject = this.defaultValue;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     boolean isRangeBased()
/*      */     {
/*  603 */       return false;
/*      */     }
/*      */     
/*      */     void setValue(String valueFlag) {
/*  607 */       this.valueAsObject = valueFlag;
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
/*  625 */   private static final String[] PROPERTY_CATEGORIES = { "Connection/Authentication", "Networking", "High Availability and Clustering", "Security", "Performance Extensions", "Debuging/Profiling", "Miscellaneous" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  630 */   private static final ArrayList PROPERTY_LIST = new ArrayList();
/*      */   
/*  632 */   private static final String STANDARD_LOGGER_NAME = StandardLogger.class.getName();
/*      */   
/*      */   protected static final String ZERO_DATETIME_BEHAVIOR_CONVERT_TO_NULL = "convertToNull";
/*      */   protected static final String ZERO_DATETIME_BEHAVIOR_EXCEPTION = "exception";
/*      */   protected static final String ZERO_DATETIME_BEHAVIOR_ROUND = "round";
/*      */   
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/*  642 */       Field[] declaredFields = ConnectionProperties.class.getDeclaredFields();
/*      */       
/*      */ 
/*  645 */       for (int i = 0; i < declaredFields.length; i++) {
/*  646 */         if (ConnectionProperty.class.isAssignableFrom(declaredFields[i].getType()))
/*      */         {
/*  648 */           PROPERTY_LIST.add(declaredFields[i]);
/*      */         }
/*      */       }
/*      */     } catch (Exception ex) {
/*  652 */       throw new RuntimeException(ex.toString());
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
/*      */   protected static DriverPropertyInfo[] exposeAsDriverPropertyInfo(Properties info, int slotsToReserve)
/*      */     throws SQLException
/*      */   {
/*  672 */     new ConnectionProperties() {}.exposeAsDriverPropertyInfoInternal(info, slotsToReserve);
/*      */   }
/*      */   
/*      */ 
/*  676 */   private BooleanConnectionProperty allowLoadLocalInfile = new BooleanConnectionProperty("allowLoadLocalInfile", true, "Should the driver allow use of 'LOAD DATA LOCAL INFILE...' (defaults to 'true').", "3.0.3", "Security", Integer.MAX_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  682 */   private BooleanConnectionProperty allowMultiQueries = new BooleanConnectionProperty("allowMultiQueries", false, "Allow the use of ';' to delimit multiple queries during one statement (true/false), defaults to 'false'", "3.1.1", "Security", 1);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  688 */   private BooleanConnectionProperty allowNanAndInf = new BooleanConnectionProperty("allowNanAndInf", false, "Should the driver allow NaN or +/- INF values in PreparedStatement.setDouble()?", "3.1.5", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  694 */   private BooleanConnectionProperty allowUrlInLocalInfile = new BooleanConnectionProperty("allowUrlInLocalInfile", false, "Should the driver allow URLs in 'LOAD DATA LOCAL INFILE' statements?", "3.1.4", "Security", Integer.MAX_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  700 */   private BooleanConnectionProperty alwaysSendSetIsolation = new BooleanConnectionProperty("alwaysSendSetIsolation", true, "Should the driver always communicate with the database when  Connection.setTransactionIsolation() is called? If set to false, the driver will only communicate with the database when the requested transaction isolation is different than the whichever is newer, the last value that was set via Connection.setTransactionIsolation(), or the value that was read from the server when the connection was established.", "3.1.7", "Performance Extensions", Integer.MAX_VALUE);
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
/*  712 */   private BooleanConnectionProperty autoClosePStmtStreams = new BooleanConnectionProperty("autoClosePStmtStreams", false, "Should the driver automatically call .close() on streams/readers passed as arguments via set*() methods?", "3.1.12", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  721 */   private BooleanConnectionProperty autoDeserialize = new BooleanConnectionProperty("autoDeserialize", false, "Should the driver automatically detect and de-serialize objects stored in BLOB fields?", "3.1.5", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  727 */   private BooleanConnectionProperty autoGenerateTestcaseScript = new BooleanConnectionProperty("autoGenerateTestcaseScript", false, "Should the driver dump the SQL it is executing, including server-side prepared statements to STDERR?", "3.1.9", "Debuging/Profiling", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  733 */   private boolean autoGenerateTestcaseScriptAsBoolean = false;
/*      */   
/*  735 */   private BooleanConnectionProperty autoReconnect = new BooleanConnectionProperty("autoReconnect", false, "Should the driver try to re-establish stale and/or dead connections?   If enabled the driver will throw an exception for a queries issued on a stale or dead connection,  which belong to the current transaction, but will attempt reconnect before the next query issued on the connection in a new transaction. The use of this feature is not recommended, because it has side effects related to session state and data consistency when applications don'thandle SQLExceptions properly, and is only designed to be used when you are unable to configure your application to handle SQLExceptions resulting from dead andstale connections properly. Alternatively, investigate setting the MySQL server variable \"wait_timeout\"to some high value rather than the default of 8 hours.", "1.1", "High Availability and Clustering", 0);
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
/*  749 */   private BooleanConnectionProperty autoReconnectForPools = new BooleanConnectionProperty("autoReconnectForPools", false, "Use a reconnection strategy appropriate for connection pools (defaults to 'false')", "3.1.3", "High Availability and Clustering", 1);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  755 */   private boolean autoReconnectForPoolsAsBoolean = false;
/*      */   
/*  757 */   private MemorySizeConnectionProperty blobSendChunkSize = new MemorySizeConnectionProperty("blobSendChunkSize", 1048576, 1, Integer.MAX_VALUE, "Chunk to use when sending BLOB/CLOBs via ServerPreparedStatements", "3.1.9", "Performance Extensions", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  765 */   private BooleanConnectionProperty blobsAreStrings = new BooleanConnectionProperty("blobsAreStrings", false, "Should the driver always treat BLOBs as Strings - specifically to work around dubious metadata returned by the server for GROUP BY clauses?", "5.0.8", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  771 */   private BooleanConnectionProperty functionsNeverReturnBlobs = new BooleanConnectionProperty("functionsNeverReturnBlobs", false, "Should the driver always treat data from functions returning BLOBs as Strings - specifically to work around dubious metadata returned by the server for GROUP BY clauses?", "5.0.8", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  777 */   private BooleanConnectionProperty cacheCallableStatements = new BooleanConnectionProperty("cacheCallableStmts", false, "Should the driver cache the parsing stage of CallableStatements", "3.1.2", "Performance Extensions", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  782 */   private BooleanConnectionProperty cachePreparedStatements = new BooleanConnectionProperty("cachePrepStmts", false, "Should the driver cache the parsing stage of PreparedStatements of client-side prepared statements, the \"check\" for suitability of server-side prepared  and server-side prepared statements themselves?", "3.0.10", "Performance Extensions", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  790 */   private BooleanConnectionProperty cacheResultSetMetadata = new BooleanConnectionProperty("cacheResultSetMetadata", false, "Should the driver cache ResultSetMetaData for Statements and PreparedStatements? (Req. JDK-1.4+, true/false, default 'false')", "3.1.1", "Performance Extensions", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean cacheResultSetMetaDataAsBoolean;
/*      */   
/*      */ 
/*      */ 
/*  798 */   private BooleanConnectionProperty cacheServerConfiguration = new BooleanConnectionProperty("cacheServerConfiguration", false, "Should the driver cache the results of 'SHOW VARIABLES' and 'SHOW COLLATION' on a per-URL basis?", "3.1.5", "Performance Extensions", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  805 */   private IntegerConnectionProperty callableStatementCacheSize = new IntegerConnectionProperty("callableStmtCacheSize", 100, 0, Integer.MAX_VALUE, "If 'cacheCallableStmts' is enabled, how many callable statements should be cached?", "3.1.2", "Performance Extensions", 5);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  813 */   private BooleanConnectionProperty capitalizeTypeNames = new BooleanConnectionProperty("capitalizeTypeNames", true, "Capitalize type names in DatabaseMetaData? (usually only useful when using WebObjects, true/false, defaults to 'false')", "2.0.7", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  819 */   private StringConnectionProperty characterEncoding = new StringConnectionProperty("characterEncoding", null, "If 'useUnicode' is set to true, what character encoding should the driver use when dealing with strings? (defaults is to 'autodetect')", "1.1g", "Miscellaneous", 5);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  825 */   private String characterEncodingAsString = null;
/*      */   
/*  827 */   private StringConnectionProperty characterSetResults = new StringConnectionProperty("characterSetResults", null, "Character set to tell the server to return results as.", "3.0.13", "Miscellaneous", 6);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  832 */   private BooleanConnectionProperty clobberStreamingResults = new BooleanConnectionProperty("clobberStreamingResults", false, "This will cause a 'streaming' ResultSet to be automatically closed, and any outstanding data still streaming from the server to be discarded if another query is executed before all the data has been read from the server.", "3.0.9", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  840 */   private StringConnectionProperty clobCharacterEncoding = new StringConnectionProperty("clobCharacterEncoding", null, "The character encoding to use for sending and retrieving TEXT, MEDIUMTEXT and LONGTEXT values instead of the configured connection characterEncoding", "5.0.0", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  847 */   private StringConnectionProperty connectionCollation = new StringConnectionProperty("connectionCollation", null, "If set, tells the server to use this collation via 'set collation_connection'", "3.0.13", "Miscellaneous", 7);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  853 */   private IntegerConnectionProperty connectTimeout = new IntegerConnectionProperty("connectTimeout", 0, 0, Integer.MAX_VALUE, "Timeout for socket connect (in milliseconds), with 0 being no timeout. Only works on JDK-1.4 or newer. Defaults to '0'.", "3.0.1", "Connection/Authentication", 9);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  859 */   private BooleanConnectionProperty continueBatchOnError = new BooleanConnectionProperty("continueBatchOnError", true, "Should the driver continue processing batch commands if one statement fails. The JDBC spec allows either way (defaults to 'true').", "3.0.3", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  866 */   private BooleanConnectionProperty createDatabaseIfNotExist = new BooleanConnectionProperty("createDatabaseIfNotExist", false, "Creates the database given in the URL if it doesn't yet exist. Assumes  the configured user has permissions to create databases.", "3.1.9", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  873 */   private IntegerConnectionProperty defaultFetchSize = new IntegerConnectionProperty("defaultFetchSize", 0, "The driver will call setFetchSize(n) with this value on all newly-created Statements", "3.1.9", "Performance Extensions", Integer.MIN_VALUE);
/*      */   
/*  875 */   private BooleanConnectionProperty detectServerPreparedStmts = new BooleanConnectionProperty("useServerPrepStmts", false, "Use server-side prepared statements if the server supports them?", "3.1.0", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  881 */   private BooleanConnectionProperty dontTrackOpenResources = new BooleanConnectionProperty("dontTrackOpenResources", false, "The JDBC specification requires the driver to automatically track and close resources, however if your application doesn't do a good job of explicitly calling close() on statements or result sets, this can cause memory leakage. Setting this property to true relaxes this constraint, and can be more memory efficient for some applications.", "3.1.7", "Performance Extensions", Integer.MIN_VALUE);
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
/*  892 */   private BooleanConnectionProperty dumpQueriesOnException = new BooleanConnectionProperty("dumpQueriesOnException", false, "Should the driver dump the contents of the query sent to the server in the message for SQLExceptions?", "3.1.3", "Debuging/Profiling", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  898 */   private BooleanConnectionProperty dynamicCalendars = new BooleanConnectionProperty("dynamicCalendars", false, "Should the driver retrieve the default calendar when required, or cache it per connection/session?", "3.1.5", "Performance Extensions", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  905 */   private BooleanConnectionProperty elideSetAutoCommits = new BooleanConnectionProperty("elideSetAutoCommits", false, "If using MySQL-4.1 or newer, should the driver only issue 'set autocommit=n' queries when the server's state doesn't match the requested state by Connection.setAutoCommit(boolean)?", "3.1.3", "Performance Extensions", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  911 */   private BooleanConnectionProperty emptyStringsConvertToZero = new BooleanConnectionProperty("emptyStringsConvertToZero", true, "Should the driver allow conversions from empty string fields to numeric values of '0'?", "3.1.8", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  917 */   private BooleanConnectionProperty emulateLocators = new BooleanConnectionProperty("emulateLocators", false, "N/A", "3.1.0", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*  921 */   private BooleanConnectionProperty emulateUnsupportedPstmts = new BooleanConnectionProperty("emulateUnsupportedPstmts", true, "Should the driver detect prepared statements that are not supported by the server, and replace them with client-side emulated versions?", "3.1.7", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  928 */   private BooleanConnectionProperty enablePacketDebug = new BooleanConnectionProperty("enablePacketDebug", false, "When enabled, a ring-buffer of 'packetDebugBufferSize' packets will be kept, and dumped when exceptions are thrown in key areas in the driver's code", "3.1.3", "Debuging/Profiling", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  934 */   private BooleanConnectionProperty enableQueryTimeouts = new BooleanConnectionProperty("enableQueryTimeouts", true, "When enabled, query timeouts set via Statement.setQueryTimeout() use a shared java.util.Timer instance for scheduling. Even if the timeout doesn't expire before the query is processed, there will be memory used by the TimerTask for the given timeout which won't be reclaimed until the time the timeout would have expired if it hadn't been cancelled by the driver. High-load environments might want to consider disabling this functionality.", "5.0.6", "Performance Extensions", Integer.MIN_VALUE);
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
/*  945 */   private BooleanConnectionProperty explainSlowQueries = new BooleanConnectionProperty("explainSlowQueries", false, "If 'logSlowQueries' is enabled, should the driver automatically issue an 'EXPLAIN' on the server and send the results to the configured log at a WARN level?", "3.1.2", "Debuging/Profiling", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  953 */   private BooleanConnectionProperty failOverReadOnly = new BooleanConnectionProperty("failOverReadOnly", true, "When failing over in autoReconnect mode, should the connection be set to 'read-only'?", "3.0.12", "High Availability and Clustering", 2);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  959 */   private BooleanConnectionProperty gatherPerformanceMetrics = new BooleanConnectionProperty("gatherPerfMetrics", false, "Should the driver gather performance metrics, and report them via the configured logger every 'reportMetricsIntervalMillis' milliseconds?", "3.1.2", "Debuging/Profiling", 1);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  965 */   private BooleanConnectionProperty generateSimpleParameterMetadata = new BooleanConnectionProperty("generateSimpleParameterMetadata", false, "Should the driver generate simplified parameter metadata for PreparedStatements when no metadata is available either because the server couldn't support preparing the statement, or server-side prepared statements are disabled?", "5.0.5", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  971 */   private boolean highAvailabilityAsBoolean = false;
/*      */   
/*  973 */   private BooleanConnectionProperty holdResultsOpenOverStatementClose = new BooleanConnectionProperty("holdResultsOpenOverStatementClose", false, "Should the driver close result sets on Statement.close() as required by the JDBC specification?", "3.1.7", "Performance Extensions", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  979 */   private BooleanConnectionProperty includeInnodbStatusInDeadlockExceptions = new BooleanConnectionProperty("includeInnodbStatusInDeadlockExceptions", false, "Include the output of \"SHOW ENGINE INNODB STATUS\" in exception messages when deadlock exceptions are detected?", "5.0.7", "Debuging/Profiling", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  985 */   private BooleanConnectionProperty ignoreNonTxTables = new BooleanConnectionProperty("ignoreNonTxTables", false, "Ignore non-transactional table warning for rollback? (defaults to 'false').", "3.0.9", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  991 */   private IntegerConnectionProperty initialTimeout = new IntegerConnectionProperty("initialTimeout", 2, 1, Integer.MAX_VALUE, "If autoReconnect is enabled, the initial time to wait between re-connect attempts (in seconds, defaults to '2').", "1.1", "High Availability and Clustering", 5);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  998 */   private BooleanConnectionProperty isInteractiveClient = new BooleanConnectionProperty("interactiveClient", false, "Set the CLIENT_INTERACTIVE flag, which tells MySQL to timeout connections based on INTERACTIVE_TIMEOUT instead of WAIT_TIMEOUT", "3.1.0", "Connection/Authentication", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1005 */   private BooleanConnectionProperty jdbcCompliantTruncation = new BooleanConnectionProperty("jdbcCompliantTruncation", true, "Should the driver throw java.sql.DataTruncation exceptions when data is truncated as is required by the JDBC specification when connected to a server that supports warnings(MySQL 4.1.0 and newer)?", "3.1.2", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1013 */   private boolean jdbcCompliantTruncationForReads = this.jdbcCompliantTruncation.getValueAsBoolean();
/*      */   
/*      */ 
/* 1016 */   private StringConnectionProperty loadBalanceStrategy = new StringConnectionProperty("loadBalanceStrategy", "random", new String[] { "random", "bestResponseTime" }, "If using a load-balanced connection to connect to SQL nodes in a MySQL Cluster/NDB configuration(by using the URL prefix \"jdbc:mysql:loadbalance://\"), which load balancin algorithm should the driver use: (1) \"random\" - the driver will pick a random host for each request. This tends to work better than round-robin, as the randomness will somewhat account for spreading loads where requests vary in response time, while round-robin can sometimes lead to overloaded nodes if there are variations in response times across the workload. (2) \"bestResponseTime\" - the driver will route the request to the host that had the best response time for the previous transaction.", "5.0.6", "Performance Extensions", Integer.MIN_VALUE);
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
/* 1030 */   private StringConnectionProperty localSocketAddress = new StringConnectionProperty("localSocketAddress", null, "Hostname or IP address given to explicitly configure the interface that the driver will bind the client side of the TCP/IP connection to when connecting.", "5.0.5", "Connection/Authentication", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1035 */   private MemorySizeConnectionProperty locatorFetchBufferSize = new MemorySizeConnectionProperty("locatorFetchBufferSize", 1048576, 0, Integer.MAX_VALUE, "If 'emulateLocators' is configured to 'true', what size  buffer should be used when fetching BLOB data for getBinaryInputStream?", "3.2.1", "Performance Extensions", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1044 */   private StringConnectionProperty loggerClassName = new StringConnectionProperty("logger", STANDARD_LOGGER_NAME, "The name of a class that implements '" + Log.class.getName() + "' that will be used to log messages to." + "(default is '" + STANDARD_LOGGER_NAME + "', which " + "logs to STDERR)", "3.1.1", "Debuging/Profiling", 0);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1052 */   private BooleanConnectionProperty logSlowQueries = new BooleanConnectionProperty("logSlowQueries", false, "Should queries that take longer than 'slowQueryThresholdMillis' be logged?", "3.1.2", "Debuging/Profiling", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1058 */   private BooleanConnectionProperty logXaCommands = new BooleanConnectionProperty("logXaCommands", false, "Should the driver log XA commands sent by MysqlXaConnection to the server, at the DEBUG level of logging?", "5.0.5", "Debuging/Profiling", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1065 */   private BooleanConnectionProperty maintainTimeStats = new BooleanConnectionProperty("maintainTimeStats", true, "Should the driver maintain various internal timers to enable idle time calculations as well as more verbose error messages when the connection to the server fails? Setting this property to false removes at least two calls to System.getCurrentTimeMillis() per query.", "3.1.9", "Performance Extensions", Integer.MAX_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1075 */   private boolean maintainTimeStatsAsBoolean = true;
/*      */   
/* 1077 */   private IntegerConnectionProperty maxQuerySizeToLog = new IntegerConnectionProperty("maxQuerySizeToLog", 2048, 0, Integer.MAX_VALUE, "Controls the maximum length/size of a query that will get logged when profiling or tracing", "3.1.3", "Debuging/Profiling", 4);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1085 */   private IntegerConnectionProperty maxReconnects = new IntegerConnectionProperty("maxReconnects", 3, 1, Integer.MAX_VALUE, "Maximum number of reconnects to attempt if autoReconnect is true, default is '3'.", "1.1", "High Availability and Clustering", 4);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1093 */   private IntegerConnectionProperty maxRows = new IntegerConnectionProperty("maxRows", -1, -1, Integer.MAX_VALUE, "The maximum number of rows to return  (0, the default means return all rows).", "all versions", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1099 */   private int maxRowsAsInt = -1;
/*      */   
/* 1101 */   private IntegerConnectionProperty metadataCacheSize = new IntegerConnectionProperty("metadataCacheSize", 50, 1, Integer.MAX_VALUE, "The number of queries to cacheResultSetMetadata for if cacheResultSetMetaData is set to 'true' (default 50)", "3.1.1", "Performance Extensions", 5);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1110 */   private BooleanConnectionProperty noAccessToProcedureBodies = new BooleanConnectionProperty("noAccessToProcedureBodies", false, "When determining procedure parameter types for CallableStatements, and the connected user  can't access procedure bodies through \"SHOW CREATE PROCEDURE\" or select on mysql.proc  should the driver instead create basic metadata (all parameters reported as IN VARCHARs, but allowing registerOutParameter() to be called on them anyway) instead  of throwing an exception?", "5.0.3", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1120 */   private BooleanConnectionProperty noDatetimeStringSync = new BooleanConnectionProperty("noDatetimeStringSync", false, "Don't ensure that ResultSet.getDatetimeType().toString().equals(ResultSet.getString())", "3.1.7", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1126 */   private BooleanConnectionProperty noTimezoneConversionForTimeType = new BooleanConnectionProperty("noTimezoneConversionForTimeType", false, "Don't convert TIME values using the server timezone if 'useTimezone'='true'", "5.0.0", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1132 */   private BooleanConnectionProperty nullCatalogMeansCurrent = new BooleanConnectionProperty("nullCatalogMeansCurrent", true, "When DatabaseMetadataMethods ask for a 'catalog' parameter, does the value null mean use the current catalog? (this is not JDBC-compliant, but follows legacy behavior from earlier versions of the driver)", "3.1.8", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1139 */   private BooleanConnectionProperty nullNamePatternMatchesAll = new BooleanConnectionProperty("nullNamePatternMatchesAll", true, "Should DatabaseMetaData methods that accept *pattern parameters treat null the same as '%'  (this is not JDBC-compliant, however older versions of the driver accepted this departure from the specification)", "3.1.8", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1146 */   private IntegerConnectionProperty packetDebugBufferSize = new IntegerConnectionProperty("packetDebugBufferSize", 20, 0, Integer.MAX_VALUE, "The maximum number of packets to retain when 'enablePacketDebug' is true", "3.1.3", "Debuging/Profiling", 7);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1154 */   private BooleanConnectionProperty padCharsWithSpace = new BooleanConnectionProperty("padCharsWithSpace", false, "If a result set column has the CHAR type and the value does not fill the amount of characters specified in the DDL for the column, should the driver pad the remaining characters with space (for ANSI compliance)?", "5.0.6", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1164 */   private BooleanConnectionProperty paranoid = new BooleanConnectionProperty("paranoid", false, "Take measures to prevent exposure sensitive information in error messages and clear data structures holding sensitive data when possible? (defaults to 'false')", "3.0.1", "Security", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1171 */   private BooleanConnectionProperty pedantic = new BooleanConnectionProperty("pedantic", false, "Follow the JDBC spec to the letter.", "3.0.0", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/* 1175 */   private BooleanConnectionProperty pinGlobalTxToPhysicalConnection = new BooleanConnectionProperty("pinGlobalTxToPhysicalConnection", false, "When using XAConnections, should the driver ensure that  operations on a given XID are always routed to the same physical connection? This allows the XAConnection to support \"XA START ... JOIN\" after \"XA END\" has been called", "5.0.1", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1181 */   private BooleanConnectionProperty populateInsertRowWithDefaultValues = new BooleanConnectionProperty("populateInsertRowWithDefaultValues", false, "When using ResultSets that are CONCUR_UPDATABLE, should the driver pre-poulate the \"insert\" row with default values from the DDL for the table used in the query  so those values are immediately available for ResultSet accessors? This functionality requires a  call to the database for metadata each time a result set of this type is created.  If disabled (the default), the default values will be populated by the an internal call to refreshRow() which pulls back default values and/or values changed by triggers.", "5.0.5", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1191 */   private IntegerConnectionProperty preparedStatementCacheSize = new IntegerConnectionProperty("prepStmtCacheSize", 25, 0, Integer.MAX_VALUE, "If prepared statement caching is enabled, how many prepared statements should be cached?", "3.0.10", "Performance Extensions", 10);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1197 */   private IntegerConnectionProperty preparedStatementCacheSqlLimit = new IntegerConnectionProperty("prepStmtCacheSqlLimit", 256, 1, Integer.MAX_VALUE, "If prepared statement caching is enabled, what's the largest SQL the driver will cache the parsing for?", "3.0.10", "Performance Extensions", 11);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1206 */   private BooleanConnectionProperty processEscapeCodesForPrepStmts = new BooleanConnectionProperty("processEscapeCodesForPrepStmts", true, "Should the driver process escape codes in queries that are prepared?", "3.1.12", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1213 */   private StringConnectionProperty profileSql = new StringConnectionProperty("profileSql", null, "Deprecated, use 'profileSQL' instead. Trace queries and their execution/fetch times on STDERR (true/false) defaults to 'false'", "2.0.14", "Debuging/Profiling", 3);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1219 */   private BooleanConnectionProperty profileSQL = new BooleanConnectionProperty("profileSQL", false, "Trace queries and their execution/fetch times to the configured logger (true/false) defaults to 'false'", "3.1.0", "Debuging/Profiling", 1);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1225 */   private boolean profileSQLAsBoolean = false;
/*      */   
/* 1227 */   private StringConnectionProperty propertiesTransform = new StringConnectionProperty("propertiesTransform", null, "An implementation of com.mysql.jdbc.ConnectionPropertiesTransform that the driver will use to modify URL properties passed to the driver before attempting a connection", "3.1.4", "Connection/Authentication", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1233 */   private IntegerConnectionProperty queriesBeforeRetryMaster = new IntegerConnectionProperty("queriesBeforeRetryMaster", 50, 1, Integer.MAX_VALUE, "Number of queries to issue before falling back to master when failed over (when using multi-host failover). Whichever condition is met first, 'queriesBeforeRetryMaster' or 'secondsBeforeRetryMaster' will cause an attempt to be made to reconnect to the master. Defaults to 50.", "3.0.2", "High Availability and Clustering", 7);
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
/* 1244 */   private BooleanConnectionProperty reconnectAtTxEnd = new BooleanConnectionProperty("reconnectAtTxEnd", false, "If autoReconnect is set to true, should the driver attempt reconnectionsat the end of every transaction?", "3.0.10", "High Availability and Clustering", 4);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1250 */   private boolean reconnectTxAtEndAsBoolean = false;
/*      */   
/* 1252 */   private BooleanConnectionProperty relaxAutoCommit = new BooleanConnectionProperty("relaxAutoCommit", false, "If the version of MySQL the driver connects to does not support transactions, still allow calls to commit(), rollback() and setAutoCommit() (true/false, defaults to 'false')?", "2.0.13", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1258 */   private IntegerConnectionProperty reportMetricsIntervalMillis = new IntegerConnectionProperty("reportMetricsIntervalMillis", 30000, 0, Integer.MAX_VALUE, "If 'gatherPerfMetrics' is enabled, how often should they be logged (in ms)?", "3.1.2", "Debuging/Profiling", 3);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1266 */   private BooleanConnectionProperty requireSSL = new BooleanConnectionProperty("requireSSL", false, "Require SSL connection if useSSL=true? (defaults to 'false').", "3.1.0", "Security", 3);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1271 */   private StringConnectionProperty resourceId = new StringConnectionProperty("resourceId", null, "A globally unique name that identifies the resource that this datasource or connection is connected to, used for XAResource.isSameRM() when the driver can't determine this value based on hostnames used in the URL", "5.0.1", "High Availability and Clustering", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1280 */   private IntegerConnectionProperty resultSetSizeThreshold = new IntegerConnectionProperty("resultSetSizeThreshold", 100, "If the usage advisor is enabled, how many rows should a result set contain before the driver warns that it  is suspiciously large?", "5.0.5", "Debuging/Profiling", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/* 1284 */   private BooleanConnectionProperty retainStatementAfterResultSetClose = new BooleanConnectionProperty("retainStatementAfterResultSetClose", false, "Should the driver retain the Statement reference in a ResultSet after ResultSet.close() has been called. This is not JDBC-compliant after JDBC-4.0.", "3.1.11", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1291 */   private BooleanConnectionProperty rewriteBatchedStatements = new BooleanConnectionProperty("rewriteBatchedStatements", false, "Should the driver use multiqueries (irregardless of the setting of \"allowMultiQueries\") as well as rewriting of prepared statements for INSERT and REPLACE into multi-value inserts/replaces when executeBatch() is called? Notice that this has the potential for SQL injection if using plain java.sql.Statements and your code doesn't sanitize input correctly.\n\nNotice that if you don't specify stream lengths when using PreparedStatement.set*Stream(),the driver won't be able to determine the optimium number of parameters per batch and you might receive an error from the driver that the resultant packet is too large.\n\nStatement.getGeneratedKeys() for these rewritten statements only works when the entire batch includes INSERT statements.", "3.1.13", "Performance Extensions", Integer.MIN_VALUE);
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
/* 1304 */   private BooleanConnectionProperty rollbackOnPooledClose = new BooleanConnectionProperty("rollbackOnPooledClose", true, "Should the driver issue a rollback() when the logical connection in a pool is closed?", "3.0.15", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1310 */   private BooleanConnectionProperty roundRobinLoadBalance = new BooleanConnectionProperty("roundRobinLoadBalance", false, "When autoReconnect is enabled, and failoverReadonly is false, should we pick hosts to connect to on a round-robin basis?", "3.1.2", "High Availability and Clustering", 5);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1316 */   private BooleanConnectionProperty runningCTS13 = new BooleanConnectionProperty("runningCTS13", false, "Enables workarounds for bugs in Sun's JDBC compliance testsuite version 1.3", "3.1.7", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1322 */   private IntegerConnectionProperty secondsBeforeRetryMaster = new IntegerConnectionProperty("secondsBeforeRetryMaster", 30, 1, Integer.MAX_VALUE, "How long should the driver wait, when failed over, before attempting to reconnect to the master server? Whichever condition is met first, 'queriesBeforeRetryMaster' or 'secondsBeforeRetryMaster' will cause an attempt to be made to reconnect to the master. Time in seconds, defaults to 30", "3.0.2", "High Availability and Clustering", 8);
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
/* 1333 */   private StringConnectionProperty serverTimezone = new StringConnectionProperty("serverTimezone", null, "Override detection/mapping of timezone. Used when timezone from server doesn't map to Java timezone", "3.0.2", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1339 */   private StringConnectionProperty sessionVariables = new StringConnectionProperty("sessionVariables", null, "A comma-separated list of name/value pairs to be sent as SET SESSION ... to  the server when the driver connects.", "3.1.8", "Miscellaneous", Integer.MAX_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1345 */   private IntegerConnectionProperty slowQueryThresholdMillis = new IntegerConnectionProperty("slowQueryThresholdMillis", 2000, 0, Integer.MAX_VALUE, "If 'logSlowQueries' is enabled, how long should a query (in ms) before it is logged as 'slow'?", "3.1.2", "Debuging/Profiling", 9);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1353 */   private LongConnectionProperty slowQueryThresholdNanos = new LongConnectionProperty("slowQueryThresholdNanos", 0L, "If 'useNanosForElapsedTime' is set to true, and this property is set to a non-zero value, the driver will use this threshold (in nanosecond units) to determine if a query was slow.", "5.0.7", "Debuging/Profiling", 10);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1362 */   private StringConnectionProperty socketFactoryClassName = new StringConnectionProperty("socketFactory", StandardSocketFactory.class.getName(), "The name of the class that the driver should use for creating socket connections to the server. This class must implement the interface 'com.mysql.jdbc.SocketFactory' and have public no-args constructor.", "3.0.3", "Connection/Authentication", 4);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1368 */   private IntegerConnectionProperty socketTimeout = new IntegerConnectionProperty("socketTimeout", 0, 0, Integer.MAX_VALUE, "Timeout on network socket operations (0, the default means no timeout).", "3.0.1", "Connection/Authentication", 10);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1376 */   private BooleanConnectionProperty strictFloatingPoint = new BooleanConnectionProperty("strictFloatingPoint", false, "Used only in older versions of compliance test", "3.0.0", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1381 */   private BooleanConnectionProperty strictUpdates = new BooleanConnectionProperty("strictUpdates", true, "Should the driver do strict checking (all primary keys selected) of updatable result sets (true, false, defaults to 'true')?", "3.0.4", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1387 */   private BooleanConnectionProperty overrideSupportsIntegrityEnhancementFacility = new BooleanConnectionProperty("overrideSupportsIntegrityEnhancementFacility", false, "Should the driver return \"true\" for DatabaseMetaData.supportsIntegrityEnhancementFacility() even if the database doesn't support it to workaround applications that require this method to return \"true\" to signal support of foreign keys, even though the SQL specification states that this facility contains much more than just foreign key support (one such application being OpenOffice)?", "3.1.12", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1395 */   private BooleanConnectionProperty tcpNoDelay = new BooleanConnectionProperty("tcpNoDelay", Boolean.valueOf("true").booleanValue(), "If connecting using TCP/IP, should the driver set SO_TCP_NODELAY (disabling the Nagle Algorithm)?", "5.0.7", "Networking", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1401 */   private BooleanConnectionProperty tcpKeepAlive = new BooleanConnectionProperty("tcpKeepAlive", Boolean.valueOf("true").booleanValue(), "If connecting using TCP/IP, should the driver set SO_KEEPALIVE?", "5.0.7", "Networking", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1407 */   private IntegerConnectionProperty tcpRcvBuf = new IntegerConnectionProperty("tcpRcvBuf", Integer.parseInt("0"), 0, Integer.MAX_VALUE, "If connecting using TCP/IP, should the driver set SO_RCV_BUF to the given value? The default value of '0', means use the platform default value for this property)", "5.0.7", "Networking", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1415 */   private IntegerConnectionProperty tcpSndBuf = new IntegerConnectionProperty("tcpSndBuf", Integer.parseInt("0"), 0, Integer.MAX_VALUE, "If connecting using TCP/IP, shuold the driver set SO_SND_BUF to the given value? The default value of '0', means use the platform default value for this property)", "5.0.7", "Networking", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1423 */   private IntegerConnectionProperty tcpTrafficClass = new IntegerConnectionProperty("tcpTrafficClass", Integer.parseInt("0"), 0, 255, "If connecting using TCP/IP, should the driver set traffic class or type-of-service fields ? See the documentation for java.net.Socket.setTrafficClass() for more information.", "5.0.7", "Networking", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1431 */   private BooleanConnectionProperty tinyInt1isBit = new BooleanConnectionProperty("tinyInt1isBit", true, "Should the driver treat the datatype TINYINT(1) as the BIT type (because the server silently converts BIT -> TINYINT(1) when creating tables)?", "3.0.16", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1438 */   private BooleanConnectionProperty traceProtocol = new BooleanConnectionProperty("traceProtocol", false, "Should trace-level network protocol be logged?", "3.1.2", "Debuging/Profiling", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1443 */   private BooleanConnectionProperty treatUtilDateAsTimestamp = new BooleanConnectionProperty("treatUtilDateAsTimestamp", true, "Should the driver treat java.util.Date as a TIMESTAMP for the purposes of PreparedStatement.setObject()?", "5.0.5", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1448 */   private BooleanConnectionProperty transformedBitIsBoolean = new BooleanConnectionProperty("transformedBitIsBoolean", false, "If the driver converts TINYINT(1) to a different type, should it use BOOLEAN instead of BIT  for future compatibility with MySQL-5.0, as MySQL-5.0 has a BIT type?", "3.1.9", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1455 */   private BooleanConnectionProperty useCompression = new BooleanConnectionProperty("useCompression", false, "Use zlib compression when communicating with the server (true/false)? Defaults to 'false'.", "3.0.17", "Connection/Authentication", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1461 */   private StringConnectionProperty useConfigs = new StringConnectionProperty("useConfigs", null, "Load the comma-delimited list of configuration properties before parsing the URL or applying user-specified properties. These configurations are explained in the 'Configurations' of the documentation.", "3.1.5", "Connection/Authentication", Integer.MAX_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1468 */   private BooleanConnectionProperty useCursorFetch = new BooleanConnectionProperty("useCursorFetch", false, "If connected to MySQL > 5.0.2, and setFetchSize() > 0 on a statement, should  that statement use cursor-based fetching to retrieve rows?", "5.0.0", "Performance Extensions", Integer.MAX_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1475 */   private BooleanConnectionProperty useDynamicCharsetInfo = new BooleanConnectionProperty("useDynamicCharsetInfo", true, "Should the driver use a per-connection cache of character set information queried from the  server when necessary, or use a built-in static mapping that is more efficient, but isn't  aware of custom character sets or character sets implemented after the release of the JDBC driver?(this only affects the \"padCharsWithSpace\" configuration property and the ResultSetMetaData.getColumnDisplayWidth() method).", "5.0.6", "Performance Extensions", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1485 */   private BooleanConnectionProperty useFastIntParsing = new BooleanConnectionProperty("useFastIntParsing", true, "Use internal String->Integer conversion routines to avoid excessive object creation?", "3.1.4", "Performance Extensions", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1491 */   private BooleanConnectionProperty useFastDateParsing = new BooleanConnectionProperty("useFastDateParsing", true, "Use internal String->Date/Time/Teimstamp conversion routines to avoid excessive object creation?", "5.0.5", "Performance Extensions", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1497 */   private BooleanConnectionProperty useHostsInPrivileges = new BooleanConnectionProperty("useHostsInPrivileges", true, "Add '@hostname' to users in DatabaseMetaData.getColumn/TablePrivileges() (true/false), defaults to 'true'.", "3.0.2", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1502 */   private BooleanConnectionProperty useInformationSchema = new BooleanConnectionProperty("useInformationSchema", false, "When connected to MySQL-5.0.7 or newer, should the driver use the INFORMATION_SCHEMA to  derive information used by DatabaseMetaData?", "5.0.0", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1508 */   private BooleanConnectionProperty useJDBCCompliantTimezoneShift = new BooleanConnectionProperty("useJDBCCompliantTimezoneShift", false, "Should the driver use JDBC-compliant rules when converting TIME/TIMESTAMP/DATETIME values' timezone information for those JDBC arguments which take a java.util.Calendar argument? (Notice that this option is exclusive of the \"useTimezone=true\" configuration option.)", "5.0.0", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1517 */   private BooleanConnectionProperty useLocalSessionState = new BooleanConnectionProperty("useLocalSessionState", false, "Should the driver refer to the internal values of autocommit and transaction isolation that are set by Connection.setAutoCommit() and Connection.setTransactionIsolation() and transaction state as maintained by the protocol, rather than querying the database or blindly sending commands to the database for commit() or rollback() method calls?", "3.1.7", "Performance Extensions", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1526 */   private BooleanConnectionProperty useNanosForElapsedTime = new BooleanConnectionProperty("useNanosForElapsedTime", false, "For profiling/debugging functionality that measures elapsed time, should the driver try to use nanoseconds resolution if available (JDK >= 1.5)?", "5.0.7", "Debuging/Profiling", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1534 */   private BooleanConnectionProperty useOldAliasMetadataBehavior = new BooleanConnectionProperty("useOldAliasMetadataBehavior", true, "Should the driver use the legacy behavior for \"AS\" clauses on columns and tables, and only return aliases (if any) for ResultSetMetaData.getColumnName() or ResultSetMetaData.getTableName() rather than the original column/table name?", "5.0.4", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1544 */   private BooleanConnectionProperty useOldUTF8Behavior = new BooleanConnectionProperty("useOldUTF8Behavior", false, "Use the UTF-8 behavior the driver did when communicating with 4.0 and older servers", "3.1.6", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1550 */   private boolean useOldUTF8BehaviorAsBoolean = false;
/*      */   
/* 1552 */   private BooleanConnectionProperty useOnlyServerErrorMessages = new BooleanConnectionProperty("useOnlyServerErrorMessages", true, "Don't prepend 'standard' SQLState error messages to error messages returned by the server.", "3.0.15", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1558 */   private BooleanConnectionProperty useReadAheadInput = new BooleanConnectionProperty("useReadAheadInput", true, "Use newer, optimized non-blocking, buffered input stream when reading from the server?", "3.1.5", "Performance Extensions", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1564 */   private BooleanConnectionProperty useSqlStateCodes = new BooleanConnectionProperty("useSqlStateCodes", true, "Use SQL Standard state codes instead of 'legacy' X/Open/SQL state codes (true/false), default is 'true'", "3.1.3", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1570 */   private BooleanConnectionProperty useSSL = new BooleanConnectionProperty("useSSL", false, "Use SSL when communicating with the server (true/false), defaults to 'false'", "3.0.2", "Security", 2);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1576 */   private BooleanConnectionProperty useSSPSCompatibleTimezoneShift = new BooleanConnectionProperty("useSSPSCompatibleTimezoneShift", false, "If migrating from an environment that was using server-side prepared statements, and the configuration property \"useJDBCCompliantTimeZoneShift\" set to \"true\", use compatible behavior when not using server-side prepared statements when sending TIMESTAMP values to the MySQL server.", "5.0.5", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1584 */   private BooleanConnectionProperty useStreamLengthsInPrepStmts = new BooleanConnectionProperty("useStreamLengthsInPrepStmts", true, "Honor stream length parameter in PreparedStatement/ResultSet.setXXXStream() method calls (true/false, defaults to 'true')?", "3.0.2", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1591 */   private BooleanConnectionProperty useTimezone = new BooleanConnectionProperty("useTimezone", false, "Convert time/date types between client and server timezones (true/false, defaults to 'false')?", "3.0.2", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1597 */   private BooleanConnectionProperty useUltraDevWorkAround = new BooleanConnectionProperty("ultraDevHack", false, "Create PreparedStatements for prepareCall() when required, because UltraDev  is broken and issues a prepareCall() for _all_ statements? (true/false, defaults to 'false')", "2.0.3", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1604 */   private BooleanConnectionProperty useUnbufferedInput = new BooleanConnectionProperty("useUnbufferedInput", true, "Don't use BufferedInputStream for reading data from the server", "3.0.11", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1609 */   private BooleanConnectionProperty useUnicode = new BooleanConnectionProperty("useUnicode", true, "Should the driver use Unicode character encodings when handling strings? Should only be used when the driver can't determine the character set mapping, or you are trying to 'force' the driver to use a character set that MySQL either doesn't natively support (such as UTF-8), true/false, defaults to 'true'", "1.1g", "Miscellaneous", 0);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1616 */   private boolean useUnicodeAsBoolean = true;
/*      */   
/* 1618 */   private BooleanConnectionProperty useUsageAdvisor = new BooleanConnectionProperty("useUsageAdvisor", false, "Should the driver issue 'usage' warnings advising proper and efficient usage of JDBC and MySQL Connector/J to the log (true/false, defaults to 'false')?", "3.1.1", "Debuging/Profiling", 10);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1624 */   private boolean useUsageAdvisorAsBoolean = false;
/*      */   
/* 1626 */   private BooleanConnectionProperty yearIsDateType = new BooleanConnectionProperty("yearIsDateType", true, "Should the JDBC driver treat the MySQL type \"YEAR\" as a java.sql.Date, or as a SHORT?", "3.1.9", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1632 */   private StringConnectionProperty zeroDateTimeBehavior = new StringConnectionProperty("zeroDateTimeBehavior", "exception", new String[] { "exception", "round", "convertToNull" }, "What should happen when the driver encounters DATETIME values that are composed entirely of zeroes (used by MySQL to represent invalid dates)? Valid values are 'exception', 'round' and 'convertToNull'.", "3.1.4", "Miscellaneous", Integer.MIN_VALUE);
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
/* 1648 */   private BooleanConnectionProperty useJvmCharsetConverters = new BooleanConnectionProperty("useJvmCharsetConverters", false, "Always use the character encoding routines built into the JVM, rather than using lookup tables for single-byte character sets?", "5.0.1", "Performance Extensions", Integer.MIN_VALUE);
/*      */   
/*      */ 
/*      */ 
/* 1652 */   private BooleanConnectionProperty useGmtMillisForDatetimes = new BooleanConnectionProperty("useGmtMillisForDatetimes", false, "Convert between session timezone and GMT before creating Date and Timestamp instances (value of \"false\" is legacy behavior, \"true\" leads to more JDBC-compliant behavior.", "3.1.12", "Miscellaneous", Integer.MIN_VALUE);
/*      */   
/* 1654 */   private BooleanConnectionProperty dumpMetadataOnColumnNotFound = new BooleanConnectionProperty("dumpMetadataOnColumnNotFound", false, "Should the driver dump the field-level metadata of a result set into the exception message when ResultSet.findColumn() fails?", "3.1.13", "Debuging/Profiling", Integer.MIN_VALUE);
/*      */   
/*      */   protected DriverPropertyInfo[] exposeAsDriverPropertyInfoInternal(Properties info, int slotsToReserve) throws SQLException
/*      */   {
/* 1658 */     initializeProperties(info);
/*      */     
/* 1660 */     int numProperties = PROPERTY_LIST.size();
/*      */     
/* 1662 */     int listSize = numProperties + slotsToReserve;
/*      */     
/* 1664 */     DriverPropertyInfo[] driverProperties = new DriverPropertyInfo[listSize];
/*      */     
/* 1666 */     for (int i = slotsToReserve; i < listSize; i++) {
/* 1667 */       Field propertyField = (Field)PROPERTY_LIST.get(i - slotsToReserve);
/*      */       
/*      */       try
/*      */       {
/* 1671 */         ConnectionProperty propToExpose = (ConnectionProperty)propertyField.get(this);
/*      */         
/*      */ 
/* 1674 */         if (info != null) {
/* 1675 */           propToExpose.initializeFrom(info);
/*      */         }
/*      */         
/*      */ 
/* 1679 */         driverProperties[i] = propToExpose.getAsDriverPropertyInfo();
/*      */       } catch (IllegalAccessException iae) {
/* 1681 */         throw SQLError.createSQLException("Internal properties failure", "S1000");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1686 */     return driverProperties;
/*      */   }
/*      */   
/*      */   protected Properties exposeAsProperties(Properties info) throws SQLException
/*      */   {
/* 1691 */     if (info == null) {
/* 1692 */       info = new Properties();
/*      */     }
/*      */     
/* 1695 */     int numPropertiesToSet = PROPERTY_LIST.size();
/*      */     
/* 1697 */     for (int i = 0; i < numPropertiesToSet; i++) {
/* 1698 */       Field propertyField = (Field)PROPERTY_LIST.get(i);
/*      */       
/*      */       try
/*      */       {
/* 1702 */         ConnectionProperty propToGet = (ConnectionProperty)propertyField.get(this);
/*      */         
/*      */ 
/* 1705 */         Object propValue = propToGet.getValueAsObject();
/*      */         
/* 1707 */         if (propValue != null) {
/* 1708 */           info.setProperty(propToGet.getPropertyName(), propValue.toString());
/*      */         }
/*      */       }
/*      */       catch (IllegalAccessException iae) {
/* 1712 */         throw SQLError.createSQLException("Internal properties failure", "S1000");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1717 */     return info;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String exposeAsXml()
/*      */     throws SQLException
/*      */   {
/* 1728 */     StringBuffer xmlBuf = new StringBuffer();
/* 1729 */     xmlBuf.append("<ConnectionProperties>");
/*      */     
/* 1731 */     int numPropertiesToSet = PROPERTY_LIST.size();
/*      */     
/* 1733 */     int numCategories = PROPERTY_CATEGORIES.length;
/*      */     
/* 1735 */     Map propertyListByCategory = new HashMap();
/*      */     
/* 1737 */     for (int i = 0; i < numCategories; i++) {
/* 1738 */       propertyListByCategory.put(PROPERTY_CATEGORIES[i], new Map[] { new TreeMap(), new TreeMap() });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1748 */     StringConnectionProperty userProp = new StringConnectionProperty("user", null, "The user to connect as", "all", "Connection/Authentication", -2147483647);
/*      */     
/*      */ 
/*      */ 
/* 1752 */     StringConnectionProperty passwordProp = new StringConnectionProperty("password", null, "The password to use when connecting", "all", "Connection/Authentication", -2147483646);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1757 */     Map[] connectionSortMaps = (Map[])propertyListByCategory.get("Connection/Authentication");
/*      */     
/* 1759 */     TreeMap userMap = new TreeMap();
/* 1760 */     userMap.put(userProp.getPropertyName(), userProp);
/*      */     
/* 1762 */     connectionSortMaps[0].put(new Integer(userProp.getOrder()), userMap);
/*      */     
/* 1764 */     TreeMap passwordMap = new TreeMap();
/* 1765 */     passwordMap.put(passwordProp.getPropertyName(), passwordProp);
/*      */     
/* 1767 */     connectionSortMaps[0].put(new Integer(passwordProp.getOrder()), passwordMap);
/*      */     
/*      */     try
/*      */     {
/* 1771 */       for (int i = 0; i < numPropertiesToSet; i++) {
/* 1772 */         Field propertyField = (Field)PROPERTY_LIST.get(i);
/*      */         
/* 1774 */         ConnectionProperty propToGet = (ConnectionProperty)propertyField.get(this);
/*      */         
/* 1776 */         Map[] sortMaps = (Map[])propertyListByCategory.get(propToGet.getCategoryName());
/*      */         
/* 1778 */         int orderInCategory = propToGet.getOrder();
/*      */         
/* 1780 */         if (orderInCategory == Integer.MIN_VALUE) {
/* 1781 */           sortMaps[1].put(propToGet.getPropertyName(), propToGet);
/*      */         } else {
/* 1783 */           Integer order = new Integer(orderInCategory);
/*      */           
/* 1785 */           Map orderMap = (Map)sortMaps[0].get(order);
/*      */           
/* 1787 */           if (orderMap == null) {
/* 1788 */             orderMap = new TreeMap();
/* 1789 */             sortMaps[0].put(order, orderMap);
/*      */           }
/*      */           
/* 1792 */           orderMap.put(propToGet.getPropertyName(), propToGet);
/*      */         }
/*      */       }
/*      */       
/* 1796 */       for (int j = 0; j < numCategories; j++) {
/* 1797 */         Map[] sortMaps = (Map[])propertyListByCategory.get(PROPERTY_CATEGORIES[j]);
/*      */         
/* 1799 */         Iterator orderedIter = sortMaps[0].values().iterator();
/* 1800 */         Iterator alphaIter = sortMaps[1].values().iterator();
/*      */         
/* 1802 */         xmlBuf.append("\n <PropertyCategory name=\"");
/* 1803 */         xmlBuf.append(PROPERTY_CATEGORIES[j]);
/* 1804 */         xmlBuf.append("\">");
/*      */         
/* 1806 */         while (orderedIter.hasNext()) {
/* 1807 */           Iterator orderedAlphaIter = ((Map)orderedIter.next()).values().iterator();
/*      */           
/* 1809 */           while (orderedAlphaIter.hasNext()) {
/* 1810 */             ConnectionProperty propToGet = (ConnectionProperty)orderedAlphaIter.next();
/*      */             
/*      */ 
/* 1813 */             xmlBuf.append("\n  <Property name=\"");
/* 1814 */             xmlBuf.append(propToGet.getPropertyName());
/* 1815 */             xmlBuf.append("\" required=\"");
/* 1816 */             xmlBuf.append(propToGet.required ? "Yes" : "No");
/*      */             
/* 1818 */             xmlBuf.append("\" default=\"");
/*      */             
/* 1820 */             if (propToGet.getDefaultValue() != null) {
/* 1821 */               xmlBuf.append(propToGet.getDefaultValue());
/*      */             }
/*      */             
/* 1824 */             xmlBuf.append("\" sortOrder=\"");
/* 1825 */             xmlBuf.append(propToGet.getOrder());
/* 1826 */             xmlBuf.append("\" since=\"");
/* 1827 */             xmlBuf.append(propToGet.sinceVersion);
/* 1828 */             xmlBuf.append("\">\n");
/* 1829 */             xmlBuf.append("    ");
/* 1830 */             xmlBuf.append(propToGet.description);
/* 1831 */             xmlBuf.append("\n  </Property>");
/*      */           }
/*      */         }
/*      */         
/* 1835 */         while (alphaIter.hasNext()) {
/* 1836 */           ConnectionProperty propToGet = (ConnectionProperty)alphaIter.next();
/*      */           
/*      */ 
/* 1839 */           xmlBuf.append("\n  <Property name=\"");
/* 1840 */           xmlBuf.append(propToGet.getPropertyName());
/* 1841 */           xmlBuf.append("\" required=\"");
/* 1842 */           xmlBuf.append(propToGet.required ? "Yes" : "No");
/*      */           
/* 1844 */           xmlBuf.append("\" default=\"");
/*      */           
/* 1846 */           if (propToGet.getDefaultValue() != null) {
/* 1847 */             xmlBuf.append(propToGet.getDefaultValue());
/*      */           }
/*      */           
/* 1850 */           xmlBuf.append("\" sortOrder=\"alpha\" since=\"");
/* 1851 */           xmlBuf.append(propToGet.sinceVersion);
/* 1852 */           xmlBuf.append("\">\n");
/* 1853 */           xmlBuf.append("    ");
/* 1854 */           xmlBuf.append(propToGet.description);
/* 1855 */           xmlBuf.append("\n  </Property>");
/*      */         }
/*      */         
/* 1858 */         xmlBuf.append("\n </PropertyCategory>");
/*      */       }
/*      */     } catch (IllegalAccessException iae) {
/* 1861 */       throw SQLError.createSQLException("Internal properties failure", "S1000");
/*      */     }
/*      */     
/*      */ 
/* 1865 */     xmlBuf.append("\n</ConnectionProperties>");
/*      */     
/* 1867 */     return xmlBuf.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getAllowLoadLocalInfile()
/*      */   {
/* 1876 */     return this.allowLoadLocalInfile.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getAllowMultiQueries()
/*      */   {
/* 1885 */     return this.allowMultiQueries.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getAllowNanAndInf()
/*      */   {
/* 1892 */     return this.allowNanAndInf.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getAllowUrlInLocalInfile()
/*      */   {
/* 1899 */     return this.allowUrlInLocalInfile.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getAlwaysSendSetIsolation()
/*      */   {
/* 1906 */     return this.alwaysSendSetIsolation.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getAutoDeserialize()
/*      */   {
/* 1913 */     return this.autoDeserialize.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getAutoGenerateTestcaseScript() {
/* 1917 */     return this.autoGenerateTestcaseScriptAsBoolean;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getAutoReconnectForPools()
/*      */   {
/* 1926 */     return this.autoReconnectForPoolsAsBoolean;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getBlobSendChunkSize()
/*      */   {
/* 1933 */     return this.blobSendChunkSize.getValueAsInt();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getCacheCallableStatements()
/*      */   {
/* 1942 */     return this.cacheCallableStatements.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getCachePreparedStatements()
/*      */   {
/* 1951 */     return ((Boolean)this.cachePreparedStatements.getValueAsObject()).booleanValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getCacheResultSetMetadata()
/*      */   {
/* 1961 */     return this.cacheResultSetMetaDataAsBoolean;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getCacheServerConfiguration()
/*      */   {
/* 1968 */     return this.cacheServerConfiguration.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getCallableStatementCacheSize()
/*      */   {
/* 1977 */     return this.callableStatementCacheSize.getValueAsInt();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getCapitalizeTypeNames()
/*      */   {
/* 1986 */     return this.capitalizeTypeNames.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCharacterSetResults()
/*      */   {
/* 1995 */     return this.characterSetResults.getValueAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getClobberStreamingResults()
/*      */   {
/* 2004 */     return this.clobberStreamingResults.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public String getClobCharacterEncoding() {
/* 2008 */     return this.clobCharacterEncoding.getValueAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getConnectionCollation()
/*      */   {
/* 2017 */     return this.connectionCollation.getValueAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getConnectTimeout()
/*      */   {
/* 2026 */     return this.connectTimeout.getValueAsInt();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getContinueBatchOnError()
/*      */   {
/* 2035 */     return this.continueBatchOnError.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getCreateDatabaseIfNotExist() {
/* 2039 */     return this.createDatabaseIfNotExist.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public int getDefaultFetchSize() {
/* 2043 */     return this.defaultFetchSize.getValueAsInt();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getDontTrackOpenResources()
/*      */   {
/* 2050 */     return this.dontTrackOpenResources.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getDumpQueriesOnException()
/*      */   {
/* 2059 */     return this.dumpQueriesOnException.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getDynamicCalendars()
/*      */   {
/* 2066 */     return this.dynamicCalendars.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getElideSetAutoCommits()
/*      */   {
/* 2075 */     return this.elideSetAutoCommits.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getEmptyStringsConvertToZero() {
/* 2079 */     return this.emptyStringsConvertToZero.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getEmulateLocators()
/*      */   {
/* 2088 */     return this.emulateLocators.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getEmulateUnsupportedPstmts()
/*      */   {
/* 2095 */     return this.emulateUnsupportedPstmts.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getEnablePacketDebug()
/*      */   {
/* 2104 */     return this.enablePacketDebug.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getEncoding()
/*      */   {
/* 2113 */     return this.characterEncodingAsString;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getExplainSlowQueries()
/*      */   {
/* 2122 */     return this.explainSlowQueries.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getFailOverReadOnly()
/*      */   {
/* 2131 */     return this.failOverReadOnly.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getGatherPerformanceMetrics()
/*      */   {
/* 2140 */     return this.gatherPerformanceMetrics.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean getHighAvailability()
/*      */   {
/* 2149 */     return this.highAvailabilityAsBoolean;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getHoldResultsOpenOverStatementClose()
/*      */   {
/* 2156 */     return this.holdResultsOpenOverStatementClose.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getIgnoreNonTxTables()
/*      */   {
/* 2165 */     return this.ignoreNonTxTables.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getInitialTimeout()
/*      */   {
/* 2174 */     return this.initialTimeout.getValueAsInt();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getInteractiveClient()
/*      */   {
/* 2183 */     return this.isInteractiveClient.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getIsInteractiveClient()
/*      */   {
/* 2192 */     return this.isInteractiveClient.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getJdbcCompliantTruncation()
/*      */   {
/* 2201 */     return this.jdbcCompliantTruncation.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getLocatorFetchBufferSize()
/*      */   {
/* 2208 */     return this.locatorFetchBufferSize.getValueAsInt();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getLogger()
/*      */   {
/* 2217 */     return this.loggerClassName.getValueAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getLoggerClassName()
/*      */   {
/* 2226 */     return this.loggerClassName.getValueAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getLogSlowQueries()
/*      */   {
/* 2235 */     return this.logSlowQueries.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getMaintainTimeStats() {
/* 2239 */     return this.maintainTimeStatsAsBoolean;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxQuerySizeToLog()
/*      */   {
/* 2248 */     return this.maxQuerySizeToLog.getValueAsInt();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxReconnects()
/*      */   {
/* 2257 */     return this.maxReconnects.getValueAsInt();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxRows()
/*      */   {
/* 2266 */     return this.maxRowsAsInt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMetadataCacheSize()
/*      */   {
/* 2276 */     return this.metadataCacheSize.getValueAsInt();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getNoDatetimeStringSync()
/*      */   {
/* 2283 */     return this.noDatetimeStringSync.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getNullCatalogMeansCurrent() {
/* 2287 */     return this.nullCatalogMeansCurrent.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getNullNamePatternMatchesAll() {
/* 2291 */     return this.nullNamePatternMatchesAll.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getPacketDebugBufferSize()
/*      */   {
/* 2300 */     return this.packetDebugBufferSize.getValueAsInt();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getParanoid()
/*      */   {
/* 2309 */     return this.paranoid.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getPedantic()
/*      */   {
/* 2318 */     return this.pedantic.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getPreparedStatementCacheSize()
/*      */   {
/* 2327 */     return ((Integer)this.preparedStatementCacheSize.getValueAsObject()).intValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getPreparedStatementCacheSqlLimit()
/*      */   {
/* 2337 */     return ((Integer)this.preparedStatementCacheSqlLimit.getValueAsObject()).intValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getProfileSql()
/*      */   {
/* 2347 */     return this.profileSQLAsBoolean;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getProfileSQL()
/*      */   {
/* 2356 */     return this.profileSQL.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getPropertiesTransform()
/*      */   {
/* 2363 */     return this.propertiesTransform.getValueAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getQueriesBeforeRetryMaster()
/*      */   {
/* 2372 */     return this.queriesBeforeRetryMaster.getValueAsInt();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getReconnectAtTxEnd()
/*      */   {
/* 2381 */     return this.reconnectTxAtEndAsBoolean;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getRelaxAutoCommit()
/*      */   {
/* 2390 */     return this.relaxAutoCommit.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getReportMetricsIntervalMillis()
/*      */   {
/* 2399 */     return this.reportMetricsIntervalMillis.getValueAsInt();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getRequireSSL()
/*      */   {
/* 2408 */     return this.requireSSL.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getRetainStatementAfterResultSetClose() {
/* 2412 */     return this.retainStatementAfterResultSetClose.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getRollbackOnPooledClose()
/*      */   {
/* 2419 */     return this.rollbackOnPooledClose.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getRoundRobinLoadBalance()
/*      */   {
/* 2428 */     return this.roundRobinLoadBalance.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getRunningCTS13()
/*      */   {
/* 2435 */     return this.runningCTS13.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getSecondsBeforeRetryMaster()
/*      */   {
/* 2444 */     return this.secondsBeforeRetryMaster.getValueAsInt();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getServerTimezone()
/*      */   {
/* 2453 */     return this.serverTimezone.getValueAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getSessionVariables()
/*      */   {
/* 2460 */     return this.sessionVariables.getValueAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getSlowQueryThresholdMillis()
/*      */   {
/* 2469 */     return this.slowQueryThresholdMillis.getValueAsInt();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSocketFactoryClassName()
/*      */   {
/* 2478 */     return this.socketFactoryClassName.getValueAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getSocketTimeout()
/*      */   {
/* 2487 */     return this.socketTimeout.getValueAsInt();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getStrictFloatingPoint()
/*      */   {
/* 2496 */     return this.strictFloatingPoint.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getStrictUpdates()
/*      */   {
/* 2505 */     return this.strictUpdates.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getTinyInt1isBit()
/*      */   {
/* 2512 */     return this.tinyInt1isBit.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getTraceProtocol()
/*      */   {
/* 2521 */     return this.traceProtocol.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getTransformedBitIsBoolean() {
/* 2525 */     return this.transformedBitIsBoolean.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getUseCompression()
/*      */   {
/* 2534 */     return this.useCompression.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getUseFastIntParsing()
/*      */   {
/* 2541 */     return this.useFastIntParsing.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getUseHostsInPrivileges()
/*      */   {
/* 2550 */     return this.useHostsInPrivileges.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getUseInformationSchema() {
/* 2554 */     return this.useInformationSchema.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getUseLocalSessionState()
/*      */   {
/* 2561 */     return this.useLocalSessionState.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getUseOldUTF8Behavior()
/*      */   {
/* 2568 */     return this.useOldUTF8BehaviorAsBoolean;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getUseOnlyServerErrorMessages()
/*      */   {
/* 2575 */     return this.useOnlyServerErrorMessages.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getUseReadAheadInput()
/*      */   {
/* 2582 */     return this.useReadAheadInput.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getUseServerPreparedStmts()
/*      */   {
/* 2591 */     return this.detectServerPreparedStmts.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getUseSqlStateCodes()
/*      */   {
/* 2600 */     return this.useSqlStateCodes.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getUseSSL()
/*      */   {
/* 2609 */     return this.useSSL.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getUseStreamLengthsInPrepStmts()
/*      */   {
/* 2618 */     return this.useStreamLengthsInPrepStmts.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getUseTimezone()
/*      */   {
/* 2627 */     return this.useTimezone.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getUseUltraDevWorkAround()
/*      */   {
/* 2636 */     return this.useUltraDevWorkAround.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getUseUnbufferedInput()
/*      */   {
/* 2645 */     return this.useUnbufferedInput.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getUseUnicode()
/*      */   {
/* 2654 */     return this.useUnicodeAsBoolean;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getUseUsageAdvisor()
/*      */   {
/* 2663 */     return this.useUsageAdvisorAsBoolean;
/*      */   }
/*      */   
/*      */   public boolean getYearIsDateType() {
/* 2667 */     return this.yearIsDateType.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getZeroDateTimeBehavior()
/*      */   {
/* 2674 */     return this.zeroDateTimeBehavior.getValueAsString();
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
/*      */   protected void initializeFromRef(Reference ref)
/*      */     throws SQLException
/*      */   {
/* 2688 */     int numPropertiesToSet = PROPERTY_LIST.size();
/*      */     
/* 2690 */     for (int i = 0; i < numPropertiesToSet; i++) {
/* 2691 */       Field propertyField = (Field)PROPERTY_LIST.get(i);
/*      */       
/*      */       try
/*      */       {
/* 2695 */         ConnectionProperty propToSet = (ConnectionProperty)propertyField.get(this);
/*      */         
/*      */ 
/* 2698 */         if (ref != null) {
/* 2699 */           propToSet.initializeFrom(ref);
/*      */         }
/*      */       } catch (IllegalAccessException iae) {
/* 2702 */         throw SQLError.createSQLException("Internal properties failure", "S1000");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2707 */     postInitialization();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void initializeProperties(Properties info)
/*      */     throws SQLException
/*      */   {
/* 2720 */     if (info != null)
/*      */     {
/* 2722 */       String profileSqlLc = info.getProperty("profileSql");
/*      */       
/* 2724 */       if (profileSqlLc != null) {
/* 2725 */         info.put("profileSQL", profileSqlLc);
/*      */       }
/*      */       
/* 2728 */       Properties infoCopy = (Properties)info.clone();
/*      */       
/* 2730 */       infoCopy.remove("HOST");
/* 2731 */       infoCopy.remove("user");
/* 2732 */       infoCopy.remove("password");
/* 2733 */       infoCopy.remove("DBNAME");
/* 2734 */       infoCopy.remove("PORT");
/* 2735 */       infoCopy.remove("profileSql");
/*      */       
/* 2737 */       int numPropertiesToSet = PROPERTY_LIST.size();
/*      */       
/* 2739 */       for (int i = 0; i < numPropertiesToSet; i++) {
/* 2740 */         Field propertyField = (Field)PROPERTY_LIST.get(i);
/*      */         
/*      */         try
/*      */         {
/* 2744 */           ConnectionProperty propToSet = (ConnectionProperty)propertyField.get(this);
/*      */           
/*      */ 
/* 2747 */           propToSet.initializeFrom(infoCopy);
/*      */         } catch (IllegalAccessException iae) {
/* 2749 */           throw SQLError.createSQLException("Unable to initialize driver properties due to " + iae.toString(), "S1000");
/*      */         }
/*      */       }
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
/* 2772 */       postInitialization();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void postInitialization()
/*      */     throws SQLException
/*      */   {
/* 2779 */     if (this.profileSql.getValueAsObject() != null) {
/* 2780 */       this.profileSQL.initializeFrom(this.profileSql.getValueAsObject().toString());
/*      */     }
/*      */     
/*      */ 
/* 2784 */     this.reconnectTxAtEndAsBoolean = ((Boolean)this.reconnectAtTxEnd.getValueAsObject()).booleanValue();
/*      */     
/*      */ 
/*      */ 
/* 2788 */     if (getMaxRows() == 0)
/*      */     {
/*      */ 
/* 2791 */       this.maxRows.setValueAsObject(new Integer(-1));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2797 */     String testEncoding = getEncoding();
/*      */     
/* 2799 */     if (testEncoding != null)
/*      */     {
/*      */       try
/*      */       {
/* 2803 */         String testString = "abc";
/* 2804 */         testString.getBytes(testEncoding);
/*      */       } catch (UnsupportedEncodingException UE) {
/* 2806 */         throw SQLError.createSQLException("Unsupported character encoding '" + testEncoding + "'.", "0S100");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2814 */     if (((Boolean)this.cacheResultSetMetadata.getValueAsObject()).booleanValue()) {
/*      */       try
/*      */       {
/* 2817 */         Class.forName("java.util.LinkedHashMap");
/*      */       } catch (ClassNotFoundException cnfe) {
/* 2819 */         this.cacheResultSetMetadata.setValue(false);
/*      */       }
/*      */     }
/*      */     
/* 2823 */     this.cacheResultSetMetaDataAsBoolean = this.cacheResultSetMetadata.getValueAsBoolean();
/*      */     
/* 2825 */     this.useUnicodeAsBoolean = this.useUnicode.getValueAsBoolean();
/* 2826 */     this.characterEncodingAsString = ((String)this.characterEncoding.getValueAsObject());
/*      */     
/* 2828 */     this.highAvailabilityAsBoolean = this.autoReconnect.getValueAsBoolean();
/* 2829 */     this.autoReconnectForPoolsAsBoolean = this.autoReconnectForPools.getValueAsBoolean();
/*      */     
/* 2831 */     this.maxRowsAsInt = ((Integer)this.maxRows.getValueAsObject()).intValue();
/*      */     
/* 2833 */     this.profileSQLAsBoolean = this.profileSQL.getValueAsBoolean();
/* 2834 */     this.useUsageAdvisorAsBoolean = this.useUsageAdvisor.getValueAsBoolean();
/*      */     
/* 2836 */     this.useOldUTF8BehaviorAsBoolean = this.useOldUTF8Behavior.getValueAsBoolean();
/*      */     
/* 2838 */     this.autoGenerateTestcaseScriptAsBoolean = this.autoGenerateTestcaseScript.getValueAsBoolean();
/*      */     
/* 2840 */     this.maintainTimeStatsAsBoolean = this.maintainTimeStats.getValueAsBoolean();
/*      */     
/* 2842 */     this.jdbcCompliantTruncationForReads = getJdbcCompliantTruncation();
/*      */     
/* 2844 */     if (getUseCursorFetch())
/*      */     {
/*      */ 
/* 2847 */       setDetectServerPreparedStmts(true);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAllowLoadLocalInfile(boolean property)
/*      */   {
/* 2857 */     this.allowLoadLocalInfile.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAllowMultiQueries(boolean property)
/*      */   {
/* 2866 */     this.allowMultiQueries.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAllowNanAndInf(boolean flag)
/*      */   {
/* 2874 */     this.allowNanAndInf.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAllowUrlInLocalInfile(boolean flag)
/*      */   {
/* 2882 */     this.allowUrlInLocalInfile.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAlwaysSendSetIsolation(boolean flag)
/*      */   {
/* 2890 */     this.alwaysSendSetIsolation.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAutoDeserialize(boolean flag)
/*      */   {
/* 2898 */     this.autoDeserialize.setValue(flag);
/*      */   }
/*      */   
/*      */   public void setAutoGenerateTestcaseScript(boolean flag) {
/* 2902 */     this.autoGenerateTestcaseScript.setValue(flag);
/* 2903 */     this.autoGenerateTestcaseScriptAsBoolean = this.autoGenerateTestcaseScript.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAutoReconnect(boolean flag)
/*      */   {
/* 2914 */     this.autoReconnect.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAutoReconnectForConnectionPools(boolean property)
/*      */   {
/* 2923 */     this.autoReconnectForPools.setValue(property);
/* 2924 */     this.autoReconnectForPoolsAsBoolean = this.autoReconnectForPools.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAutoReconnectForPools(boolean flag)
/*      */   {
/* 2935 */     this.autoReconnectForPools.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setBlobSendChunkSize(String value)
/*      */     throws SQLException
/*      */   {
/* 2943 */     this.blobSendChunkSize.setValue(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCacheCallableStatements(boolean flag)
/*      */   {
/* 2953 */     this.cacheCallableStatements.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCachePreparedStatements(boolean flag)
/*      */   {
/* 2963 */     this.cachePreparedStatements.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCacheResultSetMetadata(boolean property)
/*      */   {
/* 2972 */     this.cacheResultSetMetadata.setValue(property);
/* 2973 */     this.cacheResultSetMetaDataAsBoolean = this.cacheResultSetMetadata.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCacheServerConfiguration(boolean flag)
/*      */   {
/* 2982 */     this.cacheServerConfiguration.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCallableStatementCacheSize(int size)
/*      */   {
/* 2993 */     this.callableStatementCacheSize.setValue(size);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCapitalizeDBMDTypes(boolean property)
/*      */   {
/* 3002 */     this.capitalizeTypeNames.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCapitalizeTypeNames(boolean flag)
/*      */   {
/* 3012 */     this.capitalizeTypeNames.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCharacterEncoding(String encoding)
/*      */   {
/* 3022 */     this.characterEncoding.setValue(encoding);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCharacterSetResults(String characterSet)
/*      */   {
/* 3032 */     this.characterSetResults.setValue(characterSet);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setClobberStreamingResults(boolean flag)
/*      */   {
/* 3042 */     this.clobberStreamingResults.setValue(flag);
/*      */   }
/*      */   
/*      */   public void setClobCharacterEncoding(String encoding) {
/* 3046 */     this.clobCharacterEncoding.setValue(encoding);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConnectionCollation(String collation)
/*      */   {
/* 3056 */     this.connectionCollation.setValue(collation);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConnectTimeout(int timeoutMs)
/*      */   {
/* 3065 */     this.connectTimeout.setValue(timeoutMs);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setContinueBatchOnError(boolean property)
/*      */   {
/* 3074 */     this.continueBatchOnError.setValue(property);
/*      */   }
/*      */   
/*      */   public void setCreateDatabaseIfNotExist(boolean flag) {
/* 3078 */     this.createDatabaseIfNotExist.setValue(flag);
/*      */   }
/*      */   
/*      */   public void setDefaultFetchSize(int n) {
/* 3082 */     this.defaultFetchSize.setValue(n);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDetectServerPreparedStmts(boolean property)
/*      */   {
/* 3091 */     this.detectServerPreparedStmts.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDontTrackOpenResources(boolean flag)
/*      */   {
/* 3099 */     this.dontTrackOpenResources.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDumpQueriesOnException(boolean flag)
/*      */   {
/* 3109 */     this.dumpQueriesOnException.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDynamicCalendars(boolean flag)
/*      */   {
/* 3117 */     this.dynamicCalendars.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setElideSetAutoCommits(boolean flag)
/*      */   {
/* 3127 */     this.elideSetAutoCommits.setValue(flag);
/*      */   }
/*      */   
/*      */   public void setEmptyStringsConvertToZero(boolean flag) {
/* 3131 */     this.emptyStringsConvertToZero.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEmulateLocators(boolean property)
/*      */   {
/* 3140 */     this.emulateLocators.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEmulateUnsupportedPstmts(boolean flag)
/*      */   {
/* 3148 */     this.emulateUnsupportedPstmts.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEnablePacketDebug(boolean flag)
/*      */   {
/* 3158 */     this.enablePacketDebug.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEncoding(String property)
/*      */   {
/* 3167 */     this.characterEncoding.setValue(property);
/* 3168 */     this.characterEncodingAsString = this.characterEncoding.getValueAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setExplainSlowQueries(boolean flag)
/*      */   {
/* 3179 */     this.explainSlowQueries.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFailOverReadOnly(boolean flag)
/*      */   {
/* 3189 */     this.failOverReadOnly.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGatherPerformanceMetrics(boolean flag)
/*      */   {
/* 3199 */     this.gatherPerformanceMetrics.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setHighAvailability(boolean property)
/*      */   {
/* 3208 */     this.autoReconnect.setValue(property);
/* 3209 */     this.highAvailabilityAsBoolean = this.autoReconnect.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setHoldResultsOpenOverStatementClose(boolean flag)
/*      */   {
/* 3217 */     this.holdResultsOpenOverStatementClose.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setIgnoreNonTxTables(boolean property)
/*      */   {
/* 3226 */     this.ignoreNonTxTables.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setInitialTimeout(int property)
/*      */   {
/* 3235 */     this.initialTimeout.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setIsInteractiveClient(boolean property)
/*      */   {
/* 3244 */     this.isInteractiveClient.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setJdbcCompliantTruncation(boolean flag)
/*      */   {
/* 3254 */     this.jdbcCompliantTruncation.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setLocatorFetchBufferSize(String value)
/*      */     throws SQLException
/*      */   {
/* 3262 */     this.locatorFetchBufferSize.setValue(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLogger(String property)
/*      */   {
/* 3271 */     this.loggerClassName.setValueAsObject(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLoggerClassName(String className)
/*      */   {
/* 3281 */     this.loggerClassName.setValue(className);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLogSlowQueries(boolean flag)
/*      */   {
/* 3291 */     this.logSlowQueries.setValue(flag);
/*      */   }
/*      */   
/*      */   public void setMaintainTimeStats(boolean flag) {
/* 3295 */     this.maintainTimeStats.setValue(flag);
/* 3296 */     this.maintainTimeStatsAsBoolean = this.maintainTimeStats.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMaxQuerySizeToLog(int sizeInBytes)
/*      */   {
/* 3307 */     this.maxQuerySizeToLog.setValue(sizeInBytes);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMaxReconnects(int property)
/*      */   {
/* 3316 */     this.maxReconnects.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMaxRows(int property)
/*      */   {
/* 3325 */     this.maxRows.setValue(property);
/* 3326 */     this.maxRowsAsInt = this.maxRows.getValueAsInt();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMetadataCacheSize(int value)
/*      */   {
/* 3337 */     this.metadataCacheSize.setValue(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNoDatetimeStringSync(boolean flag)
/*      */   {
/* 3345 */     this.noDatetimeStringSync.setValue(flag);
/*      */   }
/*      */   
/*      */   public void setNullCatalogMeansCurrent(boolean value) {
/* 3349 */     this.nullCatalogMeansCurrent.setValue(value);
/*      */   }
/*      */   
/*      */   public void setNullNamePatternMatchesAll(boolean value) {
/* 3353 */     this.nullNamePatternMatchesAll.setValue(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPacketDebugBufferSize(int size)
/*      */   {
/* 3363 */     this.packetDebugBufferSize.setValue(size);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setParanoid(boolean property)
/*      */   {
/* 3372 */     this.paranoid.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPedantic(boolean property)
/*      */   {
/* 3381 */     this.pedantic.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPreparedStatementCacheSize(int cacheSize)
/*      */   {
/* 3391 */     this.preparedStatementCacheSize.setValue(cacheSize);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPreparedStatementCacheSqlLimit(int cacheSqlLimit)
/*      */   {
/* 3401 */     this.preparedStatementCacheSqlLimit.setValue(cacheSqlLimit);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setProfileSql(boolean property)
/*      */   {
/* 3410 */     this.profileSQL.setValue(property);
/* 3411 */     this.profileSQLAsBoolean = this.profileSQL.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setProfileSQL(boolean flag)
/*      */   {
/* 3421 */     this.profileSQL.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPropertiesTransform(String value)
/*      */   {
/* 3429 */     this.propertiesTransform.setValue(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setQueriesBeforeRetryMaster(int property)
/*      */   {
/* 3438 */     this.queriesBeforeRetryMaster.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setReconnectAtTxEnd(boolean property)
/*      */   {
/* 3447 */     this.reconnectAtTxEnd.setValue(property);
/* 3448 */     this.reconnectTxAtEndAsBoolean = this.reconnectAtTxEnd.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRelaxAutoCommit(boolean property)
/*      */   {
/* 3458 */     this.relaxAutoCommit.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setReportMetricsIntervalMillis(int millis)
/*      */   {
/* 3468 */     this.reportMetricsIntervalMillis.setValue(millis);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRequireSSL(boolean property)
/*      */   {
/* 3477 */     this.requireSSL.setValue(property);
/*      */   }
/*      */   
/*      */   public void setRetainStatementAfterResultSetClose(boolean flag) {
/* 3481 */     this.retainStatementAfterResultSetClose.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRollbackOnPooledClose(boolean flag)
/*      */   {
/* 3489 */     this.rollbackOnPooledClose.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRoundRobinLoadBalance(boolean flag)
/*      */   {
/* 3499 */     this.roundRobinLoadBalance.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRunningCTS13(boolean flag)
/*      */   {
/* 3507 */     this.runningCTS13.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSecondsBeforeRetryMaster(int property)
/*      */   {
/* 3516 */     this.secondsBeforeRetryMaster.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setServerTimezone(String property)
/*      */   {
/* 3526 */     this.serverTimezone.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSessionVariables(String variables)
/*      */   {
/* 3534 */     this.sessionVariables.setValue(variables);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSlowQueryThresholdMillis(int millis)
/*      */   {
/* 3544 */     this.slowQueryThresholdMillis.setValue(millis);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSocketFactoryClassName(String property)
/*      */   {
/* 3553 */     this.socketFactoryClassName.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSocketTimeout(int property)
/*      */   {
/* 3562 */     this.socketTimeout.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setStrictFloatingPoint(boolean property)
/*      */   {
/* 3571 */     this.strictFloatingPoint.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setStrictUpdates(boolean property)
/*      */   {
/* 3580 */     this.strictUpdates.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTinyInt1isBit(boolean flag)
/*      */   {
/* 3588 */     this.tinyInt1isBit.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTraceProtocol(boolean flag)
/*      */   {
/* 3598 */     this.traceProtocol.setValue(flag);
/*      */   }
/*      */   
/*      */   public void setTransformedBitIsBoolean(boolean flag) {
/* 3602 */     this.transformedBitIsBoolean.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseCompression(boolean property)
/*      */   {
/* 3611 */     this.useCompression.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseFastIntParsing(boolean flag)
/*      */   {
/* 3619 */     this.useFastIntParsing.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseHostsInPrivileges(boolean property)
/*      */   {
/* 3628 */     this.useHostsInPrivileges.setValue(property);
/*      */   }
/*      */   
/*      */   public void setUseInformationSchema(boolean flag) {
/* 3632 */     this.useInformationSchema.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseLocalSessionState(boolean flag)
/*      */   {
/* 3640 */     this.useLocalSessionState.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseOldUTF8Behavior(boolean flag)
/*      */   {
/* 3648 */     this.useOldUTF8Behavior.setValue(flag);
/* 3649 */     this.useOldUTF8BehaviorAsBoolean = this.useOldUTF8Behavior.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseOnlyServerErrorMessages(boolean flag)
/*      */   {
/* 3658 */     this.useOnlyServerErrorMessages.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseReadAheadInput(boolean flag)
/*      */   {
/* 3666 */     this.useReadAheadInput.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseServerPreparedStmts(boolean flag)
/*      */   {
/* 3676 */     this.detectServerPreparedStmts.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseSqlStateCodes(boolean flag)
/*      */   {
/* 3686 */     this.useSqlStateCodes.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseSSL(boolean property)
/*      */   {
/* 3695 */     this.useSSL.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseStreamLengthsInPrepStmts(boolean property)
/*      */   {
/* 3704 */     this.useStreamLengthsInPrepStmts.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseTimezone(boolean property)
/*      */   {
/* 3713 */     this.useTimezone.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseUltraDevWorkAround(boolean property)
/*      */   {
/* 3722 */     this.useUltraDevWorkAround.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseUnbufferedInput(boolean flag)
/*      */   {
/* 3732 */     this.useUnbufferedInput.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseUnicode(boolean flag)
/*      */   {
/* 3742 */     this.useUnicode.setValue(flag);
/* 3743 */     this.useUnicodeAsBoolean = this.useUnicode.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseUsageAdvisor(boolean useUsageAdvisorFlag)
/*      */   {
/* 3753 */     this.useUsageAdvisor.setValue(useUsageAdvisorFlag);
/* 3754 */     this.useUsageAdvisorAsBoolean = this.useUsageAdvisor.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setYearIsDateType(boolean flag)
/*      */   {
/* 3759 */     this.yearIsDateType.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setZeroDateTimeBehavior(String behavior)
/*      */   {
/* 3767 */     this.zeroDateTimeBehavior.setValue(behavior);
/*      */   }
/*      */   
/*      */   protected void storeToRef(Reference ref) throws SQLException {
/* 3771 */     int numPropertiesToSet = PROPERTY_LIST.size();
/*      */     
/* 3773 */     for (int i = 0; i < numPropertiesToSet; i++) {
/* 3774 */       Field propertyField = (Field)PROPERTY_LIST.get(i);
/*      */       
/*      */       try
/*      */       {
/* 3778 */         ConnectionProperty propToStore = (ConnectionProperty)propertyField.get(this);
/*      */         
/*      */ 
/* 3781 */         if (ref != null) {
/* 3782 */           propToStore.storeTo(ref);
/*      */         }
/*      */       } catch (IllegalAccessException iae) {
/* 3785 */         throw SQLError.createSQLException("Huh?");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean useUnbufferedInput()
/*      */   {
/* 3796 */     return this.useUnbufferedInput.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getUseCursorFetch() {
/* 3800 */     return this.useCursorFetch.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseCursorFetch(boolean flag) {
/* 3804 */     this.useCursorFetch.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getOverrideSupportsIntegrityEnhancementFacility() {
/* 3808 */     return this.overrideSupportsIntegrityEnhancementFacility.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setOverrideSupportsIntegrityEnhancementFacility(boolean flag) {
/* 3812 */     this.overrideSupportsIntegrityEnhancementFacility.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getNoTimezoneConversionForTimeType() {
/* 3816 */     return this.noTimezoneConversionForTimeType.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setNoTimezoneConversionForTimeType(boolean flag) {
/* 3820 */     this.noTimezoneConversionForTimeType.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getUseJDBCCompliantTimezoneShift() {
/* 3824 */     return this.useJDBCCompliantTimezoneShift.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseJDBCCompliantTimezoneShift(boolean flag) {
/* 3828 */     this.useJDBCCompliantTimezoneShift.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getAutoClosePStmtStreams() {
/* 3832 */     return this.autoClosePStmtStreams.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setAutoClosePStmtStreams(boolean flag) {
/* 3836 */     this.autoClosePStmtStreams.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getProcessEscapeCodesForPrepStmts() {
/* 3840 */     return this.processEscapeCodesForPrepStmts.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setProcessEscapeCodesForPrepStmts(boolean flag) {
/* 3844 */     this.processEscapeCodesForPrepStmts.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getUseGmtMillisForDatetimes() {
/* 3848 */     return this.useGmtMillisForDatetimes.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseGmtMillisForDatetimes(boolean flag) {
/* 3852 */     this.useGmtMillisForDatetimes.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getDumpMetadataOnColumnNotFound() {
/* 3856 */     return this.dumpMetadataOnColumnNotFound.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setDumpMetadataOnColumnNotFound(boolean flag) {
/* 3860 */     this.dumpMetadataOnColumnNotFound.setValue(flag);
/*      */   }
/*      */   
/*      */   public String getResourceId() {
/* 3864 */     return this.resourceId.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setResourceId(String resourceId) {
/* 3868 */     this.resourceId.setValue(resourceId);
/*      */   }
/*      */   
/*      */   public boolean getRewriteBatchedStatements() {
/* 3872 */     return this.rewriteBatchedStatements.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setRewriteBatchedStatements(boolean flag) {
/* 3876 */     this.rewriteBatchedStatements.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getJdbcCompliantTruncationForReads() {
/* 3880 */     return this.jdbcCompliantTruncationForReads;
/*      */   }
/*      */   
/*      */   public void setJdbcCompliantTruncationForReads(boolean jdbcCompliantTruncationForReads)
/*      */   {
/* 3885 */     this.jdbcCompliantTruncationForReads = jdbcCompliantTruncationForReads;
/*      */   }
/*      */   
/*      */   public boolean getUseJvmCharsetConverters() {
/* 3889 */     return this.useJvmCharsetConverters.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseJvmCharsetConverters(boolean flag) {
/* 3893 */     this.useJvmCharsetConverters.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getPinGlobalTxToPhysicalConnection() {
/* 3897 */     return this.pinGlobalTxToPhysicalConnection.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setPinGlobalTxToPhysicalConnection(boolean flag) {
/* 3901 */     this.pinGlobalTxToPhysicalConnection.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGatherPerfMetrics(boolean flag)
/*      */   {
/* 3910 */     setGatherPerformanceMetrics(flag);
/*      */   }
/*      */   
/*      */   public boolean getGatherPerfMetrics() {
/* 3914 */     return getGatherPerformanceMetrics();
/*      */   }
/*      */   
/*      */   public void setUltraDevHack(boolean flag) {
/* 3918 */     setUseUltraDevWorkAround(flag);
/*      */   }
/*      */   
/*      */   public boolean getUltraDevHack() {
/* 3922 */     return getUseUltraDevWorkAround();
/*      */   }
/*      */   
/*      */   public void setInteractiveClient(boolean property) {
/* 3926 */     setIsInteractiveClient(property);
/*      */   }
/*      */   
/*      */   public void setSocketFactory(String name) {
/* 3930 */     setSocketFactoryClassName(name);
/*      */   }
/*      */   
/*      */   public String getSocketFactory() {
/* 3934 */     return getSocketFactoryClassName();
/*      */   }
/*      */   
/*      */   public void setUseServerPrepStmts(boolean flag) {
/* 3938 */     setUseServerPreparedStmts(flag);
/*      */   }
/*      */   
/*      */   public boolean getUseServerPrepStmts() {
/* 3942 */     return getUseServerPreparedStmts();
/*      */   }
/*      */   
/*      */   public void setCacheCallableStmts(boolean flag) {
/* 3946 */     setCacheCallableStatements(flag);
/*      */   }
/*      */   
/*      */   public boolean getCacheCallableStmts() {
/* 3950 */     return getCacheCallableStatements();
/*      */   }
/*      */   
/*      */   public void setCachePrepStmts(boolean flag) {
/* 3954 */     setCachePreparedStatements(flag);
/*      */   }
/*      */   
/*      */   public boolean getCachePrepStmts() {
/* 3958 */     return getCachePreparedStatements();
/*      */   }
/*      */   
/*      */   public void setCallableStmtCacheSize(int cacheSize) {
/* 3962 */     setCallableStatementCacheSize(cacheSize);
/*      */   }
/*      */   
/*      */   public int getCallableStmtCacheSize() {
/* 3966 */     return getCallableStatementCacheSize();
/*      */   }
/*      */   
/*      */   public void setPrepStmtCacheSize(int cacheSize) {
/* 3970 */     setPreparedStatementCacheSize(cacheSize);
/*      */   }
/*      */   
/*      */   public int getPrepStmtCacheSize() {
/* 3974 */     return getPreparedStatementCacheSize();
/*      */   }
/*      */   
/*      */   public void setPrepStmtCacheSqlLimit(int sqlLimit) {
/* 3978 */     setPreparedStatementCacheSqlLimit(sqlLimit);
/*      */   }
/*      */   
/*      */   public int getPrepStmtCacheSqlLimit() {
/* 3982 */     return getPreparedStatementCacheSqlLimit();
/*      */   }
/*      */   
/*      */   public boolean getNoAccessToProcedureBodies() {
/* 3986 */     return this.noAccessToProcedureBodies.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setNoAccessToProcedureBodies(boolean flag) {
/* 3990 */     this.noAccessToProcedureBodies.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getUseOldAliasMetadataBehavior() {
/* 3994 */     return this.useOldAliasMetadataBehavior.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseOldAliasMetadataBehavior(boolean flag) {
/* 3998 */     this.useOldAliasMetadataBehavior.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getUseSSPSCompatibleTimezoneShift() {
/* 4002 */     return this.useSSPSCompatibleTimezoneShift.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseSSPSCompatibleTimezoneShift(boolean flag) {
/* 4006 */     this.useSSPSCompatibleTimezoneShift.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getTreatUtilDateAsTimestamp() {
/* 4010 */     return this.treatUtilDateAsTimestamp.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setTreatUtilDateAsTimestamp(boolean flag) {
/* 4014 */     this.treatUtilDateAsTimestamp.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getUseFastDateParsing() {
/* 4018 */     return this.useFastDateParsing.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseFastDateParsing(boolean flag) {
/* 4022 */     this.useFastDateParsing.setValue(flag);
/*      */   }
/*      */   
/*      */   public String getLocalSocketAddress() {
/* 4026 */     return this.localSocketAddress.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setLocalSocketAddress(String address) {
/* 4030 */     this.localSocketAddress.setValue(address);
/*      */   }
/*      */   
/*      */   public void setUseConfigs(String configs) {
/* 4034 */     this.useConfigs.setValue(configs);
/*      */   }
/*      */   
/*      */   public String getUseConfigs() {
/* 4038 */     return this.useConfigs.getValueAsString();
/*      */   }
/*      */   
/*      */   public boolean getGenerateSimpleParameterMetadata() {
/* 4042 */     return this.generateSimpleParameterMetadata.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setGenerateSimpleParameterMetadata(boolean flag) {
/* 4046 */     this.generateSimpleParameterMetadata.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getLogXaCommands() {
/* 4050 */     return this.logXaCommands.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setLogXaCommands(boolean flag) {
/* 4054 */     this.logXaCommands.setValue(flag);
/*      */   }
/*      */   
/*      */   public int getResultSetSizeThreshold() {
/* 4058 */     return this.resultSetSizeThreshold.getValueAsInt();
/*      */   }
/*      */   
/*      */   public void setResultSetSizeThreshold(int threshold) {
/* 4062 */     this.resultSetSizeThreshold.setValue(threshold);
/*      */   }
/*      */   
/*      */   public boolean getEnableQueryTimeouts() {
/* 4066 */     return this.enableQueryTimeouts.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setEnableQueryTimeouts(boolean flag) {
/* 4070 */     this.enableQueryTimeouts.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getPadCharsWithSpace() {
/* 4074 */     return this.padCharsWithSpace.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setPadCharsWithSpace(boolean flag) {
/* 4078 */     this.padCharsWithSpace.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getUseDynamicCharsetInfo() {
/* 4082 */     return this.useDynamicCharsetInfo.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseDynamicCharsetInfo(boolean flag) {
/* 4086 */     this.useDynamicCharsetInfo.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getPopulateInsertRowWithDefaultValues() {
/* 4090 */     return this.populateInsertRowWithDefaultValues.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setPopulateInsertRowWithDefaultValues(boolean flag) {
/* 4094 */     this.populateInsertRowWithDefaultValues.setValue(flag);
/*      */   }
/*      */   
/*      */   public String getLoadBalanceStrategy() {
/* 4098 */     return this.loadBalanceStrategy.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setLoadBalanceStrategy(String strategy) {
/* 4102 */     this.loadBalanceStrategy.setValue(strategy);
/*      */   }
/*      */   
/*      */   public boolean getUseNanosForElapsedTime() {
/* 4106 */     return this.useNanosForElapsedTime.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseNanosForElapsedTime(boolean flag) {
/* 4110 */     this.useNanosForElapsedTime.setValue(flag);
/*      */   }
/*      */   
/*      */   public long getSlowQueryThresholdNanos() {
/* 4114 */     return this.slowQueryThresholdNanos.getValueAsLong();
/*      */   }
/*      */   
/*      */   public void setSlowQueryThresholdNanos(long nanos) {
/* 4118 */     this.slowQueryThresholdNanos.setValue(nanos);
/*      */   }
/*      */   
/*      */   public boolean getTcpNoDelay() {
/* 4122 */     return this.tcpNoDelay.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setTcpNoDelay(boolean flag) {
/* 4126 */     this.tcpNoDelay.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getTcpKeepAlive() {
/* 4130 */     return this.tcpKeepAlive.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setTcpKeepAlive(boolean flag) {
/* 4134 */     this.tcpKeepAlive.setValue(flag);
/*      */   }
/*      */   
/*      */   public int getTcpRcvBuf() {
/* 4138 */     return this.tcpRcvBuf.getValueAsInt();
/*      */   }
/*      */   
/*      */   public void setTcpRcvBuf(int bufSize) {
/* 4142 */     this.tcpRcvBuf.setValue(bufSize);
/*      */   }
/*      */   
/*      */   public int getTcpSndBuf() {
/* 4146 */     return this.tcpSndBuf.getValueAsInt();
/*      */   }
/*      */   
/*      */   public void setTcpSndBuf(int bufSize) {
/* 4150 */     this.tcpSndBuf.setValue(bufSize);
/*      */   }
/*      */   
/*      */   public int getTcpTrafficClass() {
/* 4154 */     return this.tcpTrafficClass.getValueAsInt();
/*      */   }
/*      */   
/*      */   public void setTcpTrafficClass(int classFlags) {
/* 4158 */     this.tcpTrafficClass.setValue(classFlags);
/*      */   }
/*      */   
/*      */   public boolean getIncludeInnodbStatusInDeadlockExceptions() {
/* 4162 */     return this.includeInnodbStatusInDeadlockExceptions.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setIncludeInnodbStatusInDeadlockExceptions(boolean flag) {
/* 4166 */     this.includeInnodbStatusInDeadlockExceptions.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getBlobsAreStrings() {
/* 4170 */     return this.blobsAreStrings.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setBlobsAreStrings(boolean flag) {
/* 4174 */     this.blobsAreStrings.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getFunctionsNeverReturnBlobs() {
/* 4178 */     return this.functionsNeverReturnBlobs.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setFunctionsNeverReturnBlobs(boolean flag) {
/* 4182 */     this.functionsNeverReturnBlobs.setValue(flag);
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\ConnectionProperties.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */