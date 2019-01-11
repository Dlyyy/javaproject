/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.URL;
/*      */ import java.sql.Array;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Date;
/*      */ import java.sql.ParameterMetaData;
/*      */ import java.sql.Ref;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Statement;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CallableStatement
/*      */   extends PreparedStatement
/*      */   implements java.sql.CallableStatement
/*      */ {
/*      */   private static final int NOT_OUTPUT_PARAMETER_INDICATOR = Integer.MIN_VALUE;
/*      */   private static final String PARAMETER_NAMESPACE_PREFIX = "@com_mysql_jdbc_outparam_";
/*      */   
/*      */   class CallableStatementParam
/*      */   {
/*      */     int desiredJdbcType;
/*      */     int index;
/*      */     int inOutModifier;
/*      */     boolean isIn;
/*      */     boolean isOut;
/*      */     int jdbcType;
/*      */     short nullability;
/*      */     String paramName;
/*      */     int precision;
/*      */     int scale;
/*      */     String typeName;
/*      */     
/*      */     CallableStatementParam(String name, int idx, boolean in, boolean out, int jdbcType, String typeName, int precision, int scale, short nullability, int inOutModifier)
/*      */     {
/*   87 */       this.paramName = name;
/*   88 */       this.isIn = in;
/*   89 */       this.isOut = out;
/*   90 */       this.index = idx;
/*      */       
/*   92 */       this.jdbcType = jdbcType;
/*   93 */       this.typeName = typeName;
/*   94 */       this.precision = precision;
/*   95 */       this.scale = scale;
/*   96 */       this.nullability = nullability;
/*   97 */       this.inOutModifier = inOutModifier;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Object clone()
/*      */       throws CloneNotSupportedException
/*      */     {
/*  106 */       return super.clone();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   class CallableStatementParamInfo
/*      */   {
/*      */     String catalogInUse;
/*      */     
/*      */ 
/*      */     boolean isFunctionCall;
/*      */     
/*      */ 
/*      */     String nativeSql;
/*      */     
/*      */     int numParameters;
/*      */     
/*      */     List parameterList;
/*      */     
/*      */     Map parameterMap;
/*      */     
/*      */ 
/*      */     CallableStatementParamInfo(CallableStatementParamInfo fullParamInfo)
/*      */     {
/*  131 */       this.nativeSql = CallableStatement.this.originalSql;
/*  132 */       this.catalogInUse = CallableStatement.this.currentCatalog;
/*  133 */       this.isFunctionCall = fullParamInfo.isFunctionCall;
/*  134 */       int[] localParameterMap = CallableStatement.this.placeholderToParameterIndexMap;
/*  135 */       int parameterMapLength = localParameterMap.length;
/*      */       
/*  137 */       this.parameterList = new ArrayList(fullParamInfo.numParameters);
/*  138 */       this.parameterMap = new HashMap(fullParamInfo.numParameters);
/*      */       
/*  140 */       if (this.isFunctionCall)
/*      */       {
/*  142 */         this.parameterList.add(fullParamInfo.parameterList.get(0));
/*      */       }
/*      */       
/*  145 */       int offset = this.isFunctionCall ? 1 : 0;
/*      */       
/*  147 */       for (int i = 0; i < parameterMapLength; i++) {
/*  148 */         if (localParameterMap[i] != 0) {
/*  149 */           CallableStatement.CallableStatementParam param = (CallableStatement.CallableStatementParam)fullParamInfo.parameterList.get(localParameterMap[i] + offset);
/*      */           
/*  151 */           this.parameterList.add(param);
/*  152 */           this.parameterMap.put(param.paramName, param);
/*      */         }
/*      */       }
/*      */       
/*  156 */       this.numParameters = this.parameterList.size();
/*      */     }
/*      */     
/*      */     CallableStatementParamInfo(java.sql.ResultSet paramTypesRs) throws SQLException
/*      */     {
/*  161 */       boolean hadRows = paramTypesRs.last();
/*      */       
/*  163 */       this.nativeSql = CallableStatement.this.originalSql;
/*  164 */       this.catalogInUse = CallableStatement.this.currentCatalog;
/*  165 */       this.isFunctionCall = CallableStatement.this.callingStoredFunction;
/*      */       
/*  167 */       if (hadRows) {
/*  168 */         this.numParameters = paramTypesRs.getRow();
/*      */         
/*  170 */         this.parameterList = new ArrayList(this.numParameters);
/*  171 */         this.parameterMap = new HashMap(this.numParameters);
/*      */         
/*  173 */         paramTypesRs.beforeFirst();
/*      */         
/*  175 */         addParametersFromDBMD(paramTypesRs);
/*      */       } else {
/*  177 */         this.numParameters = 0;
/*      */       }
/*      */       
/*  180 */       if (this.isFunctionCall) {
/*  181 */         this.numParameters += 1;
/*      */       }
/*      */     }
/*      */     
/*      */     private void addParametersFromDBMD(java.sql.ResultSet paramTypesRs) throws SQLException
/*      */     {
/*  187 */       int i = 0;
/*      */       
/*  189 */       while (paramTypesRs.next()) {
/*  190 */         String paramName = paramTypesRs.getString(4);
/*  191 */         int inOutModifier = paramTypesRs.getInt(5);
/*      */         
/*  193 */         boolean isOutParameter = false;
/*  194 */         boolean isInParameter = false;
/*      */         
/*  196 */         if ((i == 0) && (this.isFunctionCall)) {
/*  197 */           isOutParameter = true;
/*  198 */           isInParameter = false;
/*  199 */         } else if (inOutModifier == 2) {
/*  200 */           isOutParameter = true;
/*  201 */           isInParameter = true;
/*  202 */         } else if (inOutModifier == 1) {
/*  203 */           isOutParameter = false;
/*  204 */           isInParameter = true;
/*  205 */         } else if (inOutModifier == 4) {
/*  206 */           isOutParameter = true;
/*  207 */           isInParameter = false;
/*      */         }
/*      */         
/*  210 */         int jdbcType = paramTypesRs.getInt(6);
/*  211 */         String typeName = paramTypesRs.getString(7);
/*  212 */         int precision = paramTypesRs.getInt(8);
/*  213 */         int scale = paramTypesRs.getInt(10);
/*  214 */         short nullability = paramTypesRs.getShort(12);
/*      */         
/*  216 */         CallableStatement.CallableStatementParam paramInfoToAdd = new CallableStatement.CallableStatementParam(CallableStatement.this, paramName, i++, isInParameter, isOutParameter, jdbcType, typeName, precision, scale, nullability, inOutModifier);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  221 */         this.parameterList.add(paramInfoToAdd);
/*  222 */         this.parameterMap.put(paramName, paramInfoToAdd);
/*      */       }
/*      */     }
/*      */     
/*      */     protected void checkBounds(int paramIndex) throws SQLException {
/*  227 */       int localParamIndex = paramIndex - 1;
/*      */       
/*  229 */       if ((paramIndex < 0) || (localParamIndex >= this.numParameters)) {
/*  230 */         throw SQLError.createSQLException(Messages.getString("CallableStatement.11") + paramIndex + Messages.getString("CallableStatement.12") + this.numParameters + Messages.getString("CallableStatement.13"), "S1009");
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
/*      */     protected Object clone()
/*      */       throws CloneNotSupportedException
/*      */     {
/*  244 */       return super.clone();
/*      */     }
/*      */     
/*      */     CallableStatement.CallableStatementParam getParameter(int index) {
/*  248 */       return (CallableStatement.CallableStatementParam)this.parameterList.get(index);
/*      */     }
/*      */     
/*      */     CallableStatement.CallableStatementParam getParameter(String name) {
/*  252 */       return (CallableStatement.CallableStatementParam)this.parameterMap.get(name);
/*      */     }
/*      */     
/*      */     public String getParameterClassName(int arg0) throws SQLException {
/*  256 */       String mysqlTypeName = getParameterTypeName(arg0);
/*      */       
/*  258 */       boolean isBinaryOrBlob = (StringUtils.indexOfIgnoreCase(mysqlTypeName, "BLOB") != -1) || (StringUtils.indexOfIgnoreCase(mysqlTypeName, "BINARY") != -1);
/*      */       
/*      */ 
/*  261 */       boolean isUnsigned = StringUtils.indexOfIgnoreCase(mysqlTypeName, "UNSIGNED") != -1;
/*      */       
/*  263 */       int mysqlTypeIfKnown = 0;
/*      */       
/*  265 */       if (StringUtils.startsWithIgnoreCase(mysqlTypeName, "MEDIUMINT")) {
/*  266 */         mysqlTypeIfKnown = 9;
/*      */       }
/*      */       
/*  269 */       return ResultSetMetaData.getClassNameForJavaType(getParameterType(arg0), isUnsigned, mysqlTypeIfKnown, isBinaryOrBlob, false);
/*      */     }
/*      */     
/*      */     public int getParameterCount() throws SQLException
/*      */     {
/*  274 */       if (this.parameterList == null) {
/*  275 */         return 0;
/*      */       }
/*      */       
/*  278 */       return this.parameterList.size();
/*      */     }
/*      */     
/*      */     public int getParameterMode(int arg0) throws SQLException {
/*  282 */       checkBounds(arg0);
/*      */       
/*  284 */       return getParameter(arg0 - 1).inOutModifier;
/*      */     }
/*      */     
/*      */     public int getParameterType(int arg0) throws SQLException {
/*  288 */       checkBounds(arg0);
/*      */       
/*  290 */       return getParameter(arg0 - 1).jdbcType;
/*      */     }
/*      */     
/*      */     public String getParameterTypeName(int arg0) throws SQLException {
/*  294 */       checkBounds(arg0);
/*      */       
/*  296 */       return getParameter(arg0 - 1).typeName;
/*      */     }
/*      */     
/*      */     public int getPrecision(int arg0) throws SQLException {
/*  300 */       checkBounds(arg0);
/*      */       
/*  302 */       return getParameter(arg0 - 1).precision;
/*      */     }
/*      */     
/*      */     public int getScale(int arg0) throws SQLException {
/*  306 */       checkBounds(arg0);
/*      */       
/*  308 */       return getParameter(arg0 - 1).scale;
/*      */     }
/*      */     
/*      */     public int isNullable(int arg0) throws SQLException {
/*  312 */       checkBounds(arg0);
/*      */       
/*  314 */       return getParameter(arg0 - 1).nullability;
/*      */     }
/*      */     
/*      */     public boolean isSigned(int arg0) throws SQLException {
/*  318 */       checkBounds(arg0);
/*      */       
/*  320 */       return false;
/*      */     }
/*      */     
/*      */     Iterator iterator() {
/*  324 */       return this.parameterList.iterator();
/*      */     }
/*      */     
/*      */     int numberOfParameters() {
/*  328 */       return this.numParameters;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   class CallableStatementParamInfoJDBC3
/*      */     extends CallableStatement.CallableStatementParamInfo
/*      */     implements ParameterMetaData
/*      */   {
/*      */     CallableStatementParamInfoJDBC3(java.sql.ResultSet paramTypesRs)
/*      */       throws SQLException
/*      */     {
/*  343 */       super(paramTypesRs);
/*      */     }
/*      */     
/*      */     public CallableStatementParamInfoJDBC3(CallableStatement.CallableStatementParamInfo paramInfo) {
/*  347 */       super(paramInfo);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String mangleParameterName(String origParameterName)
/*      */   {
/*  356 */     if (origParameterName == null) {
/*  357 */       return null;
/*      */     }
/*      */     
/*  360 */     int offset = 0;
/*      */     
/*  362 */     if ((origParameterName.length() > 0) && (origParameterName.charAt(0) == '@'))
/*      */     {
/*  364 */       offset = 1;
/*      */     }
/*      */     
/*  367 */     StringBuffer paramNameBuf = new StringBuffer("@com_mysql_jdbc_outparam_".length() + origParameterName.length());
/*      */     
/*      */ 
/*  370 */     paramNameBuf.append("@com_mysql_jdbc_outparam_");
/*  371 */     paramNameBuf.append(origParameterName.substring(offset));
/*      */     
/*  373 */     return paramNameBuf.toString();
/*      */   }
/*      */   
/*  376 */   private boolean callingStoredFunction = false;
/*      */   
/*      */   private ResultSet functionReturnValueResults;
/*      */   
/*  380 */   private boolean hasOutputParams = false;
/*      */   
/*      */ 
/*      */   private ResultSet outputParameterResults;
/*      */   
/*      */ 
/*  386 */   private boolean outputParamWasNull = false;
/*      */   
/*      */ 
/*      */ 
/*      */   private int[] parameterIndexToRsIndex;
/*      */   
/*      */ 
/*      */ 
/*      */   protected CallableStatementParamInfo paramInfo;
/*      */   
/*      */ 
/*      */   private CallableStatementParam returnValueParam;
/*      */   
/*      */ 
/*      */   private int[] placeholderToParameterIndexMap;
/*      */   
/*      */ 
/*      */ 
/*      */   public CallableStatement(Connection conn, CallableStatementParamInfo paramInfo)
/*      */     throws SQLException
/*      */   {
/*  407 */     super(conn, paramInfo.nativeSql, paramInfo.catalogInUse);
/*      */     
/*  409 */     this.paramInfo = paramInfo;
/*  410 */     this.callingStoredFunction = this.paramInfo.isFunctionCall;
/*      */     
/*  412 */     if (this.callingStoredFunction) {
/*  413 */       this.parameterCount += 1;
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
/*      */   public CallableStatement(Connection conn, String catalog)
/*      */     throws SQLException
/*      */   {
/*  430 */     super(conn, catalog, null);
/*      */     
/*  432 */     determineParameterTypes();
/*  433 */     generateParameterMap();
/*      */     
/*  435 */     if (this.callingStoredFunction) {
/*  436 */       this.parameterCount += 1;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void generateParameterMap()
/*      */     throws SQLException
/*      */   {
/*  448 */     int parameterCountFromMetaData = this.paramInfo.getParameterCount();
/*      */     
/*      */ 
/*      */ 
/*  452 */     if (this.callingStoredFunction) {
/*  453 */       parameterCountFromMetaData--;
/*      */     }
/*      */     
/*  456 */     if ((this.paramInfo != null) && (this.parameterCount != parameterCountFromMetaData))
/*      */     {
/*  458 */       this.placeholderToParameterIndexMap = new int[this.parameterCount];
/*      */       
/*  460 */       int startPos = this.callingStoredFunction ? StringUtils.indexOfIgnoreCase(this.originalSql, "SELECT") : StringUtils.indexOfIgnoreCase(this.originalSql, "CALL");
/*      */       
/*      */ 
/*  463 */       if (startPos != -1) {
/*  464 */         int parenOpenPos = this.originalSql.indexOf('(', startPos + 4);
/*      */         
/*  466 */         if (parenOpenPos != -1) {
/*  467 */           int parenClosePos = StringUtils.indexOfIgnoreCaseRespectQuotes(parenOpenPos, this.originalSql, ")", '\'', true);
/*      */           
/*      */ 
/*  470 */           if (parenClosePos != -1) {
/*  471 */             List parsedParameters = StringUtils.split(this.originalSql.substring(parenOpenPos + 1, parenClosePos), ",", "'\"", "'\"", true);
/*      */             
/*  473 */             int numParsedParameters = parsedParameters.size();
/*      */             
/*      */ 
/*      */ 
/*  477 */             if (numParsedParameters != this.parameterCount) {}
/*      */             
/*      */ 
/*      */ 
/*  481 */             int placeholderCount = 0;
/*      */             
/*  483 */             for (int i = 0; i < numParsedParameters; i++) {
/*  484 */               if (((String)parsedParameters.get(i)).equals("?")) {
/*  485 */                 this.placeholderToParameterIndexMap[(placeholderCount++)] = i;
/*      */               }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CallableStatement(Connection conn, String sql, String catalog, boolean isFunctionCall)
/*      */     throws SQLException
/*      */   {
/*  509 */     super(conn, sql, catalog);
/*      */     
/*  511 */     this.callingStoredFunction = isFunctionCall;
/*      */     
/*  513 */     determineParameterTypes();
/*  514 */     generateParameterMap();
/*      */     
/*  516 */     if (this.callingStoredFunction) {
/*  517 */       this.parameterCount += 1;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addBatch()
/*      */     throws SQLException
/*      */   {
/*  527 */     setOutParams();
/*      */     
/*  529 */     super.addBatch();
/*      */   }
/*      */   
/*      */   private CallableStatementParam checkIsOutputParam(int paramIndex)
/*      */     throws SQLException
/*      */   {
/*  535 */     if (this.callingStoredFunction) {
/*  536 */       if (paramIndex == 1)
/*      */       {
/*  538 */         if (this.returnValueParam == null) {
/*  539 */           this.returnValueParam = new CallableStatementParam("", 0, false, true, 12, "VARCHAR", 0, 0, (short)2, 5);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  545 */         return this.returnValueParam;
/*      */       }
/*      */       
/*      */ 
/*  549 */       paramIndex--;
/*      */     }
/*      */     
/*  552 */     checkParameterIndexBounds(paramIndex);
/*      */     
/*  554 */     int localParamIndex = paramIndex - 1;
/*      */     
/*  556 */     if (this.placeholderToParameterIndexMap != null) {
/*  557 */       localParamIndex = this.placeholderToParameterIndexMap[localParamIndex];
/*      */     }
/*      */     
/*  560 */     CallableStatementParam paramDescriptor = this.paramInfo.getParameter(localParamIndex);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  566 */     if (this.connection.getNoAccessToProcedureBodies()) {
/*  567 */       paramDescriptor.isOut = true;
/*  568 */       paramDescriptor.isIn = true;
/*  569 */       paramDescriptor.inOutModifier = 2;
/*  570 */     } else if (!paramDescriptor.isOut) {
/*  571 */       throw SQLError.createSQLException(Messages.getString("CallableStatement.9") + paramIndex + Messages.getString("CallableStatement.10"), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  577 */     this.hasOutputParams = true;
/*      */     
/*  579 */     return paramDescriptor;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void checkParameterIndexBounds(int paramIndex)
/*      */     throws SQLException
/*      */   {
/*  590 */     this.paramInfo.checkBounds(paramIndex);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void checkStreamability()
/*      */     throws SQLException
/*      */   {
/*  602 */     if ((this.hasOutputParams) && (createStreamingResultSet())) {
/*  603 */       throw SQLError.createSQLException(Messages.getString("CallableStatement.14"), "S1C00");
/*      */     }
/*      */   }
/*      */   
/*      */   public synchronized void clearParameters() throws SQLException
/*      */   {
/*  609 */     super.clearParameters();
/*      */     try
/*      */     {
/*  612 */       if (this.outputParameterResults != null) {
/*  613 */         this.outputParameterResults.close();
/*      */       }
/*      */     } finally {
/*  616 */       this.outputParameterResults = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void fakeParameterTypes()
/*      */     throws SQLException
/*      */   {
/*  627 */     Field[] fields = new Field[13];
/*      */     
/*  629 */     fields[0] = new Field("", "PROCEDURE_CAT", 1, 0);
/*  630 */     fields[1] = new Field("", "PROCEDURE_SCHEM", 1, 0);
/*  631 */     fields[2] = new Field("", "PROCEDURE_NAME", 1, 0);
/*  632 */     fields[3] = new Field("", "COLUMN_NAME", 1, 0);
/*  633 */     fields[4] = new Field("", "COLUMN_TYPE", 1, 0);
/*  634 */     fields[5] = new Field("", "DATA_TYPE", 5, 0);
/*  635 */     fields[6] = new Field("", "TYPE_NAME", 1, 0);
/*  636 */     fields[7] = new Field("", "PRECISION", 4, 0);
/*  637 */     fields[8] = new Field("", "LENGTH", 4, 0);
/*  638 */     fields[9] = new Field("", "SCALE", 5, 0);
/*  639 */     fields[10] = new Field("", "RADIX", 5, 0);
/*  640 */     fields[11] = new Field("", "NULLABLE", 5, 0);
/*  641 */     fields[12] = new Field("", "REMARKS", 1, 0);
/*      */     
/*  643 */     String procName = extractProcedureName();
/*      */     
/*  645 */     byte[] procNameAsBytes = null;
/*      */     try
/*      */     {
/*  648 */       procNameAsBytes = procName.getBytes("UTF-8");
/*      */     } catch (UnsupportedEncodingException ueEx) {
/*  650 */       procNameAsBytes = StringUtils.s2b(procName, this.connection);
/*      */     }
/*      */     
/*  653 */     ArrayList resultRows = new ArrayList();
/*      */     
/*  655 */     for (int i = 0; i < this.parameterCount; i++) {
/*  656 */       byte[][] row = new byte[13][];
/*  657 */       row[0] = null;
/*  658 */       row[1] = null;
/*  659 */       row[2] = procNameAsBytes;
/*  660 */       row[3] = StringUtils.s2b(String.valueOf(i), this.connection);
/*      */       
/*  662 */       row[4] = StringUtils.s2b(String.valueOf(1), this.connection);
/*      */       
/*      */ 
/*      */ 
/*  666 */       row[5] = StringUtils.s2b(String.valueOf(12), this.connection);
/*      */       
/*  668 */       row[6] = StringUtils.s2b("VARCHAR", this.connection);
/*  669 */       row[7] = StringUtils.s2b(Integer.toString(65535), this.connection);
/*  670 */       row[8] = StringUtils.s2b(Integer.toString(65535), this.connection);
/*  671 */       row[9] = StringUtils.s2b(Integer.toString(0), this.connection);
/*  672 */       row[10] = StringUtils.s2b(Integer.toString(10), this.connection);
/*      */       
/*  674 */       row[11] = StringUtils.s2b(Integer.toString(2), this.connection);
/*      */       
/*      */ 
/*      */ 
/*  678 */       row[12] = null;
/*      */       
/*  680 */       resultRows.add(row);
/*      */     }
/*      */     
/*  683 */     java.sql.ResultSet paramTypesRs = DatabaseMetaData.buildResultSet(fields, resultRows, this.connection);
/*      */     
/*      */ 
/*  686 */     convertGetProcedureColumnsToInternalDescriptors(paramTypesRs);
/*      */   }
/*      */   
/*      */   private void determineParameterTypes() throws SQLException {
/*  690 */     if (this.connection.getNoAccessToProcedureBodies()) {
/*  691 */       fakeParameterTypes();
/*      */       
/*  693 */       return;
/*      */     }
/*      */     
/*  696 */     java.sql.ResultSet paramTypesRs = null;
/*      */     try
/*      */     {
/*  699 */       String procName = extractProcedureName();
/*      */       
/*  701 */       java.sql.DatabaseMetaData dbmd = this.connection.getMetaData();
/*      */       
/*  703 */       boolean useCatalog = false;
/*      */       
/*  705 */       if (procName.indexOf(".") == -1) {
/*  706 */         useCatalog = true;
/*      */       }
/*      */       
/*  709 */       paramTypesRs = dbmd.getProcedureColumns((this.connection.versionMeetsMinimum(5, 0, 2)) && (useCatalog) ? this.currentCatalog : null, null, procName, "%");
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  714 */       convertGetProcedureColumnsToInternalDescriptors(paramTypesRs);
/*      */     } finally {
/*  716 */       SQLException sqlExRethrow = null;
/*      */       
/*  718 */       if (paramTypesRs != null) {
/*      */         try {
/*  720 */           paramTypesRs.close();
/*      */         } catch (SQLException sqlEx) {
/*  722 */           sqlExRethrow = sqlEx;
/*      */         }
/*      */         
/*  725 */         paramTypesRs = null;
/*      */       }
/*      */       
/*  728 */       if (sqlExRethrow != null) {
/*  729 */         throw sqlExRethrow;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void convertGetProcedureColumnsToInternalDescriptors(java.sql.ResultSet paramTypesRs) throws SQLException {
/*  735 */     if (!this.connection.isRunningOnJDK13()) {
/*  736 */       this.paramInfo = new CallableStatementParamInfoJDBC3(paramTypesRs);
/*      */     }
/*      */     else {
/*  739 */       this.paramInfo = new CallableStatementParamInfo(paramTypesRs);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean execute()
/*      */     throws SQLException
/*      */   {
/*  749 */     boolean returnVal = false;
/*      */     
/*  751 */     checkClosed();
/*      */     
/*  753 */     checkStreamability();
/*      */     
/*  755 */     synchronized (this.connection.getMutex()) {
/*  756 */       setInOutParamsOnServer();
/*  757 */       setOutParams();
/*      */       
/*  759 */       returnVal = super.execute();
/*      */       
/*  761 */       if (this.callingStoredFunction) {
/*  762 */         this.functionReturnValueResults = this.results;
/*  763 */         this.functionReturnValueResults.next();
/*  764 */         this.results = null;
/*      */       }
/*      */       
/*  767 */       retrieveOutParams();
/*      */     }
/*      */     
/*  770 */     if (!this.callingStoredFunction) {
/*  771 */       return returnVal;
/*      */     }
/*      */     
/*      */ 
/*  775 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet executeQuery()
/*      */     throws SQLException
/*      */   {
/*  784 */     checkClosed();
/*      */     
/*  786 */     checkStreamability();
/*      */     
/*  788 */     java.sql.ResultSet execResults = null;
/*      */     
/*  790 */     synchronized (this.connection.getMutex()) {
/*  791 */       setInOutParamsOnServer();
/*  792 */       setOutParams();
/*      */       
/*  794 */       execResults = super.executeQuery();
/*      */       
/*  796 */       retrieveOutParams();
/*      */     }
/*      */     
/*  799 */     return execResults;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int executeUpdate()
/*      */     throws SQLException
/*      */   {
/*  808 */     int returnVal = -1;
/*      */     
/*  810 */     checkClosed();
/*      */     
/*  812 */     checkStreamability();
/*      */     
/*  814 */     if (this.callingStoredFunction) {
/*  815 */       execute();
/*      */       
/*  817 */       return -1;
/*      */     }
/*      */     
/*  820 */     synchronized (this.connection.getMutex()) {
/*  821 */       setInOutParamsOnServer();
/*  822 */       setOutParams();
/*      */       
/*  824 */       returnVal = super.executeUpdate();
/*      */       
/*  826 */       retrieveOutParams();
/*      */     }
/*      */     
/*  829 */     return returnVal;
/*      */   }
/*      */   
/*      */   private String extractProcedureName() throws SQLException {
/*  833 */     String sanitizedSql = StringUtils.stripComments(this.originalSql, "`\"'", "`\"'", true, false, true, true);
/*      */     
/*      */ 
/*      */ 
/*  837 */     int endCallIndex = StringUtils.indexOfIgnoreCase(sanitizedSql, "CALL ");
/*      */     
/*  839 */     int offset = 5;
/*      */     
/*  841 */     if (endCallIndex == -1) {
/*  842 */       endCallIndex = StringUtils.indexOfIgnoreCase(sanitizedSql, "SELECT ");
/*      */       
/*  844 */       offset = 7;
/*      */     }
/*      */     
/*  847 */     if (endCallIndex != -1) {
/*  848 */       StringBuffer nameBuf = new StringBuffer();
/*      */       
/*  850 */       String trimmedStatement = sanitizedSql.substring(endCallIndex + offset).trim();
/*      */       
/*      */ 
/*  853 */       int statementLength = trimmedStatement.length();
/*      */       
/*  855 */       for (int i = 0; i < statementLength; i++) {
/*  856 */         char c = trimmedStatement.charAt(i);
/*      */         
/*  858 */         if ((Character.isWhitespace(c)) || (c == '(') || (c == '?')) {
/*      */           break;
/*      */         }
/*  861 */         nameBuf.append(c);
/*      */       }
/*      */       
/*      */ 
/*  865 */       return nameBuf.toString();
/*      */     }
/*      */     
/*  868 */     throw SQLError.createSQLException(Messages.getString("CallableStatement.1"), "S1000");
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
/*      */   private String fixParameterName(String paramNameIn)
/*      */     throws SQLException
/*      */   {
/*  884 */     if ((paramNameIn == null) || (paramNameIn.length() == 0)) {
/*  885 */       throw SQLError.createSQLException(Messages.getString("CallableStatement.0") + paramNameIn == null ? Messages.getString("CallableStatement.15") : Messages.getString("CallableStatement.16"), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  890 */     if (this.connection.getNoAccessToProcedureBodies()) {
/*  891 */       throw SQLError.createSQLException("No access to parameters by name when connection has been configured not to access procedure bodies", "S1009");
/*      */     }
/*      */     
/*      */ 
/*  895 */     return mangleParameterName(paramNameIn);
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
/*      */   public synchronized Array getArray(int i)
/*      */     throws SQLException
/*      */   {
/*  910 */     ResultSet rs = getOutputParameters(i);
/*      */     
/*  912 */     Array retValue = rs.getArray(mapOutputParameterIndexToRsIndex(i));
/*      */     
/*  914 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/*  916 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized Array getArray(String parameterName)
/*      */     throws SQLException
/*      */   {
/*  924 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/*  927 */     Array retValue = rs.getArray(fixParameterName(parameterName));
/*      */     
/*  929 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/*  931 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized BigDecimal getBigDecimal(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*  939 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/*  941 */     BigDecimal retValue = rs.getBigDecimal(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */     
/*      */ 
/*  944 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/*  946 */     return retValue;
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public synchronized BigDecimal getBigDecimal(int parameterIndex, int scale)
/*      */     throws SQLException
/*      */   {
/*  967 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/*  969 */     BigDecimal retValue = rs.getBigDecimal(mapOutputParameterIndexToRsIndex(parameterIndex), scale);
/*      */     
/*      */ 
/*  972 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/*  974 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized BigDecimal getBigDecimal(String parameterName)
/*      */     throws SQLException
/*      */   {
/*  982 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/*  985 */     BigDecimal retValue = rs.getBigDecimal(fixParameterName(parameterName));
/*      */     
/*  987 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/*  989 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized Blob getBlob(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*  996 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/*  998 */     Blob retValue = rs.getBlob(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */     
/*      */ 
/* 1001 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1003 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized Blob getBlob(String parameterName)
/*      */     throws SQLException
/*      */   {
/* 1010 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1013 */     Blob retValue = rs.getBlob(fixParameterName(parameterName));
/*      */     
/* 1015 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1017 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized boolean getBoolean(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/* 1025 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/* 1027 */     boolean retValue = rs.getBoolean(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */     
/*      */ 
/* 1030 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1032 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized boolean getBoolean(String parameterName)
/*      */     throws SQLException
/*      */   {
/* 1040 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1043 */     boolean retValue = rs.getBoolean(fixParameterName(parameterName));
/*      */     
/* 1045 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1047 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized byte getByte(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/* 1054 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/* 1056 */     byte retValue = rs.getByte(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */     
/*      */ 
/* 1059 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1061 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized byte getByte(String parameterName)
/*      */     throws SQLException
/*      */   {
/* 1068 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1071 */     byte retValue = rs.getByte(fixParameterName(parameterName));
/*      */     
/* 1073 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1075 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized byte[] getBytes(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/* 1082 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/* 1084 */     byte[] retValue = rs.getBytes(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */     
/*      */ 
/* 1087 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1089 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized byte[] getBytes(String parameterName)
/*      */     throws SQLException
/*      */   {
/* 1097 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1100 */     byte[] retValue = rs.getBytes(fixParameterName(parameterName));
/*      */     
/* 1102 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1104 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized Clob getClob(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/* 1111 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/* 1113 */     Clob retValue = rs.getClob(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */     
/*      */ 
/* 1116 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1118 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized Clob getClob(String parameterName)
/*      */     throws SQLException
/*      */   {
/* 1125 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1128 */     Clob retValue = rs.getClob(fixParameterName(parameterName));
/*      */     
/* 1130 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1132 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized Date getDate(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/* 1139 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/* 1141 */     Date retValue = rs.getDate(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */     
/*      */ 
/* 1144 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1146 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized Date getDate(int parameterIndex, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 1154 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/* 1156 */     Date retValue = rs.getDate(mapOutputParameterIndexToRsIndex(parameterIndex), cal);
/*      */     
/*      */ 
/* 1159 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1161 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized Date getDate(String parameterName)
/*      */     throws SQLException
/*      */   {
/* 1168 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1171 */     Date retValue = rs.getDate(fixParameterName(parameterName));
/*      */     
/* 1173 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1175 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized Date getDate(String parameterName, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 1184 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1187 */     Date retValue = rs.getDate(fixParameterName(parameterName), cal);
/*      */     
/* 1189 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1191 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized double getDouble(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/* 1199 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/* 1201 */     double retValue = rs.getDouble(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */     
/*      */ 
/* 1204 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1206 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized double getDouble(String parameterName)
/*      */     throws SQLException
/*      */   {
/* 1214 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1217 */     double retValue = rs.getDouble(fixParameterName(parameterName));
/*      */     
/* 1219 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1221 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized float getFloat(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/* 1228 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/* 1230 */     float retValue = rs.getFloat(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */     
/*      */ 
/* 1233 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1235 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized float getFloat(String parameterName)
/*      */     throws SQLException
/*      */   {
/* 1243 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1246 */     float retValue = rs.getFloat(fixParameterName(parameterName));
/*      */     
/* 1248 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1250 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized int getInt(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/* 1257 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/* 1259 */     int retValue = rs.getInt(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */     
/*      */ 
/* 1262 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1264 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized int getInt(String parameterName)
/*      */     throws SQLException
/*      */   {
/* 1271 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1274 */     int retValue = rs.getInt(fixParameterName(parameterName));
/*      */     
/* 1276 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1278 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized long getLong(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/* 1285 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/* 1287 */     long retValue = rs.getLong(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */     
/*      */ 
/* 1290 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1292 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized long getLong(String parameterName)
/*      */     throws SQLException
/*      */   {
/* 1299 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1302 */     long retValue = rs.getLong(fixParameterName(parameterName));
/*      */     
/* 1304 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1306 */     return retValue;
/*      */   }
/*      */   
/*      */   private int getNamedParamIndex(String paramName, boolean forOut) throws SQLException
/*      */   {
/* 1311 */     if (this.connection.getNoAccessToProcedureBodies()) {
/* 1312 */       throw SQLError.createSQLException("No access to parameters by name when connection has been configured not to access procedure bodies", "S1009");
/*      */     }
/*      */     
/*      */ 
/* 1316 */     if ((paramName == null) || (paramName.length() == 0)) {
/* 1317 */       throw SQLError.createSQLException(Messages.getString("CallableStatement.2"), "S1009");
/*      */     }
/*      */     
/*      */ 
/* 1321 */     CallableStatementParam namedParamInfo = this.paramInfo.getParameter(paramName);
/*      */     
/*      */ 
/* 1324 */     if (this.paramInfo == null) {
/* 1325 */       throw SQLError.createSQLException(Messages.getString("CallableStatement.3") + paramName + Messages.getString("CallableStatement.4"), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1330 */     if ((forOut) && (!namedParamInfo.isOut)) {
/* 1331 */       throw SQLError.createSQLException(Messages.getString("CallableStatement.5") + paramName + Messages.getString("CallableStatement.6"), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1338 */     if (this.placeholderToParameterIndexMap == null) {
/* 1339 */       return namedParamInfo.index + 1;
/*      */     }
/*      */     
/* 1342 */     for (int i = 0; i < this.placeholderToParameterIndexMap.length; i++) {
/* 1343 */       if (this.placeholderToParameterIndexMap[i] == namedParamInfo.index) {
/* 1344 */         return i + 1;
/*      */       }
/*      */     }
/*      */     
/* 1348 */     throw SQLError.createSQLException("Can't find local placeholder mapping for parameter named \"" + paramName + "\".", "S1009");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized Object getObject(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/* 1357 */     CallableStatementParam paramDescriptor = checkIsOutputParam(parameterIndex);
/*      */     
/* 1359 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/* 1361 */     Object retVal = rs.getObjectStoredProc(mapOutputParameterIndexToRsIndex(parameterIndex), paramDescriptor.desiredJdbcType);
/*      */     
/*      */ 
/*      */ 
/* 1365 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1367 */     return retVal;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized Object getObject(int parameterIndex, Map map)
/*      */     throws SQLException
/*      */   {
/* 1375 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/* 1377 */     Object retVal = rs.getObject(mapOutputParameterIndexToRsIndex(parameterIndex), map);
/*      */     
/*      */ 
/* 1380 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1382 */     return retVal;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized Object getObject(String parameterName)
/*      */     throws SQLException
/*      */   {
/* 1390 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1393 */     Object retValue = rs.getObject(fixParameterName(parameterName));
/*      */     
/* 1395 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1397 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized Object getObject(String parameterName, Map map)
/*      */     throws SQLException
/*      */   {
/* 1406 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1409 */     Object retValue = rs.getObject(fixParameterName(parameterName), map);
/*      */     
/* 1411 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1413 */     return retValue;
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
/*      */   private ResultSet getOutputParameters(int paramIndex)
/*      */     throws SQLException
/*      */   {
/* 1427 */     this.outputParamWasNull = false;
/*      */     
/* 1429 */     if ((paramIndex == 1) && (this.callingStoredFunction) && (this.returnValueParam != null))
/*      */     {
/* 1431 */       return this.functionReturnValueResults;
/*      */     }
/*      */     
/* 1434 */     if (this.outputParameterResults == null) {
/* 1435 */       if (this.paramInfo.numberOfParameters() == 0) {
/* 1436 */         throw SQLError.createSQLException(Messages.getString("CallableStatement.7"), "S1009");
/*      */       }
/*      */       
/*      */ 
/* 1440 */       throw SQLError.createSQLException(Messages.getString("CallableStatement.8"), "S1000");
/*      */     }
/*      */     
/*      */ 
/* 1444 */     return this.outputParameterResults;
/*      */   }
/*      */   
/*      */   public synchronized ParameterMetaData getParameterMetaData()
/*      */     throws SQLException
/*      */   {
/* 1450 */     if (this.placeholderToParameterIndexMap == null) {
/* 1451 */       return (CallableStatementParamInfoJDBC3)this.paramInfo;
/*      */     }
/* 1453 */     return new CallableStatementParamInfoJDBC3(this.paramInfo);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized Ref getRef(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/* 1461 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/* 1463 */     Ref retValue = rs.getRef(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */     
/*      */ 
/* 1466 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1468 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized Ref getRef(String parameterName)
/*      */     throws SQLException
/*      */   {
/* 1475 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1478 */     Ref retValue = rs.getRef(fixParameterName(parameterName));
/*      */     
/* 1480 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1482 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized short getShort(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/* 1489 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/* 1491 */     short retValue = rs.getShort(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */     
/*      */ 
/* 1494 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1496 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized short getShort(String parameterName)
/*      */     throws SQLException
/*      */   {
/* 1504 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1507 */     short retValue = rs.getShort(fixParameterName(parameterName));
/*      */     
/* 1509 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1511 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized String getString(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/* 1519 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/* 1521 */     String retValue = rs.getString(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */     
/*      */ 
/* 1524 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1526 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized String getString(String parameterName)
/*      */     throws SQLException
/*      */   {
/* 1534 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1537 */     String retValue = rs.getString(fixParameterName(parameterName));
/*      */     
/* 1539 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1541 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized Time getTime(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/* 1548 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/* 1550 */     Time retValue = rs.getTime(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */     
/*      */ 
/* 1553 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1555 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized Time getTime(int parameterIndex, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 1563 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/* 1565 */     Time retValue = rs.getTime(mapOutputParameterIndexToRsIndex(parameterIndex), cal);
/*      */     
/*      */ 
/* 1568 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1570 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized Time getTime(String parameterName)
/*      */     throws SQLException
/*      */   {
/* 1577 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1580 */     Time retValue = rs.getTime(fixParameterName(parameterName));
/*      */     
/* 1582 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1584 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized Time getTime(String parameterName, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 1593 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1596 */     Time retValue = rs.getTime(fixParameterName(parameterName), cal);
/*      */     
/* 1598 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1600 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized Timestamp getTimestamp(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/* 1608 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/* 1610 */     Timestamp retValue = rs.getTimestamp(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */     
/*      */ 
/* 1613 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1615 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized Timestamp getTimestamp(int parameterIndex, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 1623 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/* 1625 */     Timestamp retValue = rs.getTimestamp(mapOutputParameterIndexToRsIndex(parameterIndex), cal);
/*      */     
/*      */ 
/* 1628 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1630 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized Timestamp getTimestamp(String parameterName)
/*      */     throws SQLException
/*      */   {
/* 1638 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1641 */     Timestamp retValue = rs.getTimestamp(fixParameterName(parameterName));
/*      */     
/* 1643 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1645 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized Timestamp getTimestamp(String parameterName, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 1654 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1657 */     Timestamp retValue = rs.getTimestamp(fixParameterName(parameterName), cal);
/*      */     
/*      */ 
/* 1660 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1662 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized URL getURL(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/* 1669 */     ResultSet rs = getOutputParameters(parameterIndex);
/*      */     
/* 1671 */     URL retValue = rs.getURL(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */     
/*      */ 
/* 1674 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1676 */     return retValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized URL getURL(String parameterName)
/*      */     throws SQLException
/*      */   {
/* 1683 */     ResultSet rs = getOutputParameters(0);
/*      */     
/*      */ 
/* 1686 */     URL retValue = rs.getURL(fixParameterName(parameterName));
/*      */     
/* 1688 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1690 */     return retValue;
/*      */   }
/*      */   
/*      */   private int mapOutputParameterIndexToRsIndex(int paramIndex)
/*      */     throws SQLException
/*      */   {
/* 1696 */     if ((this.returnValueParam != null) && (paramIndex == 1)) {
/* 1697 */       return 1;
/*      */     }
/*      */     
/* 1700 */     checkParameterIndexBounds(paramIndex);
/*      */     
/* 1702 */     int localParamIndex = paramIndex - 1;
/*      */     
/* 1704 */     if (this.placeholderToParameterIndexMap != null) {
/* 1705 */       localParamIndex = this.placeholderToParameterIndexMap[localParamIndex];
/*      */     }
/*      */     
/* 1708 */     int rsIndex = this.parameterIndexToRsIndex[localParamIndex];
/*      */     
/* 1710 */     if (rsIndex == Integer.MIN_VALUE) {
/* 1711 */       throw SQLError.createSQLException(Messages.getString("CallableStatement.21") + paramIndex + Messages.getString("CallableStatement.22"), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1717 */     return rsIndex + 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void registerOutParameter(int parameterIndex, int sqlType)
/*      */     throws SQLException
/*      */   {
/* 1725 */     CallableStatementParam paramDescriptor = checkIsOutputParam(parameterIndex);
/* 1726 */     paramDescriptor.desiredJdbcType = sqlType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void registerOutParameter(int parameterIndex, int sqlType, int scale)
/*      */     throws SQLException
/*      */   {
/* 1734 */     registerOutParameter(parameterIndex, sqlType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void registerOutParameter(int parameterIndex, int sqlType, String typeName)
/*      */     throws SQLException
/*      */   {
/* 1743 */     checkIsOutputParam(parameterIndex);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void registerOutParameter(String parameterName, int sqlType)
/*      */     throws SQLException
/*      */   {
/* 1752 */     registerOutParameter(getNamedParamIndex(parameterName, true), sqlType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void registerOutParameter(String parameterName, int sqlType, int scale)
/*      */     throws SQLException
/*      */   {
/* 1761 */     registerOutParameter(getNamedParamIndex(parameterName, true), sqlType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void registerOutParameter(String parameterName, int sqlType, String typeName)
/*      */     throws SQLException
/*      */   {
/* 1770 */     registerOutParameter(getNamedParamIndex(parameterName, true), sqlType, typeName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void retrieveOutParams()
/*      */     throws SQLException
/*      */   {
/* 1781 */     int numParameters = this.paramInfo.numberOfParameters();
/*      */     
/* 1783 */     this.parameterIndexToRsIndex = new int[numParameters];
/*      */     
/* 1785 */     for (int i = 0; i < numParameters; i++) {
/* 1786 */       this.parameterIndexToRsIndex[i] = Integer.MIN_VALUE;
/*      */     }
/*      */     
/* 1789 */     int localParamIndex = 0;
/*      */     
/* 1791 */     if (numParameters > 0) {
/* 1792 */       StringBuffer outParameterQuery = new StringBuffer("SELECT ");
/*      */       
/* 1794 */       boolean firstParam = true;
/* 1795 */       boolean hadOutputParams = false;
/*      */       
/* 1797 */       Iterator paramIter = this.paramInfo.iterator();
/* 1798 */       while (paramIter.hasNext()) {
/* 1799 */         CallableStatementParam retrParamInfo = (CallableStatementParam)paramIter.next();
/*      */         
/*      */ 
/* 1802 */         if (retrParamInfo.isOut) {
/* 1803 */           hadOutputParams = true;
/*      */           
/* 1805 */           this.parameterIndexToRsIndex[retrParamInfo.index] = (localParamIndex++);
/*      */           
/* 1807 */           String outParameterName = mangleParameterName(retrParamInfo.paramName);
/*      */           
/* 1809 */           if (!firstParam) {
/* 1810 */             outParameterQuery.append(",");
/*      */           } else {
/* 1812 */             firstParam = false;
/*      */           }
/*      */           
/* 1815 */           if (!outParameterName.startsWith("@")) {
/* 1816 */             outParameterQuery.append('@');
/*      */           }
/*      */           
/* 1819 */           outParameterQuery.append(outParameterName);
/*      */         }
/*      */       }
/*      */       
/* 1823 */       if (hadOutputParams)
/*      */       {
/*      */ 
/* 1826 */         Statement outParameterStmt = null;
/* 1827 */         java.sql.ResultSet outParamRs = null;
/*      */         try
/*      */         {
/* 1830 */           outParameterStmt = this.connection.createStatement();
/* 1831 */           outParamRs = outParameterStmt.executeQuery(outParameterQuery.toString());
/*      */           
/* 1833 */           this.outputParameterResults = ((ResultSet)outParamRs).copy();
/*      */           
/*      */ 
/* 1836 */           if (!this.outputParameterResults.next()) {
/* 1837 */             this.outputParameterResults.close();
/* 1838 */             this.outputParameterResults = null;
/*      */           }
/*      */         } finally {
/* 1841 */           if (outParameterStmt != null) {
/* 1842 */             outParameterStmt.close();
/*      */           }
/*      */         }
/*      */       } else {
/* 1846 */         this.outputParameterResults = null;
/*      */       }
/*      */     } else {
/* 1849 */       this.outputParameterResults = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAsciiStream(String parameterName, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 1859 */     setAsciiStream(getNamedParamIndex(parameterName, false), x, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBigDecimal(String parameterName, BigDecimal x)
/*      */     throws SQLException
/*      */   {
/* 1868 */     setBigDecimal(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBinaryStream(String parameterName, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 1877 */     setBinaryStream(getNamedParamIndex(parameterName, false), x, length);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setBoolean(String parameterName, boolean x)
/*      */     throws SQLException
/*      */   {
/* 1884 */     setBoolean(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setByte(String parameterName, byte x)
/*      */     throws SQLException
/*      */   {
/* 1891 */     setByte(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setBytes(String parameterName, byte[] x)
/*      */     throws SQLException
/*      */   {
/* 1898 */     setBytes(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCharacterStream(String parameterName, Reader reader, int length)
/*      */     throws SQLException
/*      */   {
/* 1907 */     setCharacterStream(getNamedParamIndex(parameterName, false), reader, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setDate(String parameterName, Date x)
/*      */     throws SQLException
/*      */   {
/* 1915 */     setDate(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDate(String parameterName, Date x, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 1924 */     setDate(getNamedParamIndex(parameterName, false), x, cal);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setDouble(String parameterName, double x)
/*      */     throws SQLException
/*      */   {
/* 1931 */     setDouble(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setFloat(String parameterName, float x)
/*      */     throws SQLException
/*      */   {
/* 1938 */     setFloat(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */   
/*      */ 
/*      */   private void setInOutParamsOnServer()
/*      */     throws SQLException
/*      */   {
/* 1945 */     if (this.paramInfo.numParameters > 0) {
/* 1946 */       int parameterIndex = 0;
/*      */       
/* 1948 */       Iterator paramIter = this.paramInfo.iterator();
/* 1949 */       while (paramIter.hasNext())
/*      */       {
/* 1951 */         CallableStatementParam inParamInfo = (CallableStatementParam)paramIter.next();
/*      */         
/*      */ 
/* 1954 */         if ((inParamInfo.isOut) && (inParamInfo.isIn)) {
/* 1955 */           String inOutParameterName = mangleParameterName(inParamInfo.paramName);
/* 1956 */           StringBuffer queryBuf = new StringBuffer(4 + inOutParameterName.length() + 1 + 1);
/*      */           
/* 1958 */           queryBuf.append("SET ");
/* 1959 */           queryBuf.append(inOutParameterName);
/* 1960 */           queryBuf.append("=?");
/*      */           
/* 1962 */           PreparedStatement setPstmt = null;
/*      */           try
/*      */           {
/* 1965 */             setPstmt = this.connection.clientPrepareStatement(queryBuf.toString());
/*      */             
/*      */ 
/* 1968 */             byte[] parameterAsBytes = getBytesRepresentation(inParamInfo.index);
/*      */             
/*      */ 
/* 1971 */             if (parameterAsBytes != null) {
/* 1972 */               if ((parameterAsBytes.length > 8) && (parameterAsBytes[0] == 95) && (parameterAsBytes[1] == 98) && (parameterAsBytes[2] == 105) && (parameterAsBytes[3] == 110) && (parameterAsBytes[4] == 97) && (parameterAsBytes[5] == 114) && (parameterAsBytes[6] == 121) && (parameterAsBytes[7] == 39))
/*      */               {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1981 */                 setPstmt.setBytesNoEscapeNoQuotes(1, parameterAsBytes);
/*      */               }
/*      */               else {
/* 1984 */                 int sqlType = inParamInfo.desiredJdbcType;
/*      */                 
/* 1986 */                 switch (sqlType) {
/*      */                 case -7: 
/*      */                 case -4: 
/*      */                 case -3: 
/*      */                 case -2: 
/*      */                 case 2000: 
/*      */                 case 2004: 
/* 1993 */                   setPstmt.setBytes(1, parameterAsBytes);
/* 1994 */                   break;
/*      */                 
/*      */ 
/*      */                 default: 
/* 1998 */                   setPstmt.setBytesNoEscape(1, parameterAsBytes);
/*      */                 }
/*      */               }
/*      */             } else {
/* 2002 */               setPstmt.setNull(1, 0);
/*      */             }
/*      */             
/* 2005 */             setPstmt.executeUpdate();
/*      */           } finally {
/* 2007 */             if (setPstmt != null) {
/* 2008 */               setPstmt.close();
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 2013 */         parameterIndex++;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void setInt(String parameterName, int x)
/*      */     throws SQLException
/*      */   {
/* 2022 */     setInt(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setLong(String parameterName, long x)
/*      */     throws SQLException
/*      */   {
/* 2029 */     setLong(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setNull(String parameterName, int sqlType)
/*      */     throws SQLException
/*      */   {
/* 2036 */     setNull(getNamedParamIndex(parameterName, false), sqlType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNull(String parameterName, int sqlType, String typeName)
/*      */     throws SQLException
/*      */   {
/* 2045 */     setNull(getNamedParamIndex(parameterName, false), sqlType, typeName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setObject(String parameterName, Object x)
/*      */     throws SQLException
/*      */   {
/* 2053 */     setObject(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setObject(String parameterName, Object x, int targetSqlType)
/*      */     throws SQLException
/*      */   {
/* 2062 */     setObject(getNamedParamIndex(parameterName, false), x, targetSqlType);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setObject(String parameterName, Object x, int targetSqlType, int scale)
/*      */     throws SQLException
/*      */   {}
/*      */   
/*      */ 
/*      */   private void setOutParams()
/*      */     throws SQLException
/*      */   {
/* 2074 */     if (this.paramInfo.numParameters > 0) {
/* 2075 */       Iterator paramIter = this.paramInfo.iterator();
/* 2076 */       while (paramIter.hasNext()) {
/* 2077 */         CallableStatementParam outParamInfo = (CallableStatementParam)paramIter.next();
/*      */         
/*      */ 
/* 2080 */         if ((!this.callingStoredFunction) && (outParamInfo.isOut)) {
/* 2081 */           String outParameterName = mangleParameterName(outParamInfo.paramName);
/*      */           
/*      */           int outParamIndex;
/*      */           int outParamIndex;
/* 2085 */           if (this.placeholderToParameterIndexMap == null) {
/* 2086 */             outParamIndex = outParamInfo.index + 1;
/*      */           } else {
/* 2088 */             outParamIndex = this.placeholderToParameterIndexMap[(outParamInfo.index - 1)];
/*      */           }
/*      */           
/* 2091 */           setBytesNoEscapeNoQuotes(outParamIndex, StringUtils.getBytes(outParameterName, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode()));
/*      */         }
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
/*      */   public void setShort(String parameterName, short x)
/*      */     throws SQLException
/*      */   {
/* 2106 */     setShort(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setString(String parameterName, String x)
/*      */     throws SQLException
/*      */   {
/* 2114 */     setString(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setTime(String parameterName, Time x)
/*      */     throws SQLException
/*      */   {
/* 2121 */     setTime(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTime(String parameterName, Time x, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 2130 */     setTime(getNamedParamIndex(parameterName, false), x, cal);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTimestamp(String parameterName, Timestamp x)
/*      */     throws SQLException
/*      */   {
/* 2139 */     setTimestamp(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTimestamp(String parameterName, Timestamp x, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 2148 */     setTimestamp(getNamedParamIndex(parameterName, false), x, cal);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setURL(String parameterName, URL val)
/*      */     throws SQLException
/*      */   {
/* 2155 */     setURL(getNamedParamIndex(parameterName, false), val);
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized boolean wasNull()
/*      */     throws SQLException
/*      */   {
/* 2162 */     return this.outputParamWasNull;
/*      */   }
/*      */   
/*      */   public int[] executeBatch() throws SQLException {
/* 2166 */     if (this.hasOutputParams) {
/* 2167 */       throw SQLError.createSQLException("Can't call executeBatch() on CallableStatement with OUTPUT parameters", "S1009");
/*      */     }
/*      */     
/*      */ 
/* 2171 */     return super.executeBatch();
/*      */   }
/*      */   
/*      */   protected int getParameterIndexOffset() {
/* 2175 */     if (this.callingStoredFunction) {
/* 2176 */       return -1;
/*      */     }
/*      */     
/* 2179 */     return super.getParameterIndexOffset();
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\CallableStatement.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */