/*    */ package util;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.DriverManager;
/*    */ import java.sql.SQLException;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class DBUtil
/*    */ {
/* 10 */   static String ip = "127.0.0.1";
/* 11 */   static int port = 3306;
/* 12 */   static String database = "hutubill";
/* 13 */   static String database_mysql = "mysql";
/* 14 */   static String encoding = "UTF-8";
/* 15 */   static String loginName = "root";
/* 16 */   static String password = "admin";
/* 17 */   public static String hsqldbfile = "";
/*    */   
/*    */   static {
/*    */     try {
/* 21 */       hsqldbfile = System.getProperties().get("user.dir") + "\\hsqldb\\hutubill";
/* 22 */       System.out.println(hsqldbfile);
/* 23 */       Class.forName("org.hsqldb.jdbcDriver");
/*    */     } catch (ClassNotFoundException e) {
/* 25 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */   
/*    */   public static Connection getConnection() throws SQLException {
/* 30 */     String url = String.format("jdbc:hsqldb:file:" + hsqldbfile + ";shutdown=true", new Object[0]);
/* 31 */     return DriverManager.getConnection(url, loginName, password);
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public static void main(String[] args)
/*    */     throws SQLException
/*    */   {
/*    */     // Byte code:
/*    */     //   0: invokestatic 136	util/DBUtil:getConnection	()Ljava/sql/Connection;
/*    */     //   3: astore_1
/*    */     //   4: aload_1
/*    */     //   5: invokeinterface 138 1 0
/*    */     //   10: aconst_null
/*    */     //   11: astore_2
/*    */     //   12: aconst_null
/*    */     //   13: astore_3
/*    */     //   14: invokestatic 136	util/DBUtil:getConnection	()Ljava/sql/Connection;
/*    */     //   17: astore 4
/*    */     //   19: aload 4
/*    */     //   21: ldc -113
/*    */     //   23: invokeinterface 145 2 0
/*    */     //   28: astore 5
/*    */     //   30: aload 5
/*    */     //   32: ifnull +28 -> 60
/*    */     //   35: aload 5
/*    */     //   37: invokeinterface 149 1 0
/*    */     //   42: goto +18 -> 60
/*    */     //   45: astore_2
/*    */     //   46: aload 5
/*    */     //   48: ifnull +10 -> 58
/*    */     //   51: aload 5
/*    */     //   53: invokeinterface 149 1 0
/*    */     //   58: aload_2
/*    */     //   59: athrow
/*    */     //   60: aload 4
/*    */     //   62: ifnull +69 -> 131
/*    */     //   65: aload 4
/*    */     //   67: invokeinterface 138 1 0
/*    */     //   72: goto +59 -> 131
/*    */     //   75: astore_3
/*    */     //   76: aload_2
/*    */     //   77: ifnonnull +8 -> 85
/*    */     //   80: aload_3
/*    */     //   81: astore_2
/*    */     //   82: goto +13 -> 95
/*    */     //   85: aload_2
/*    */     //   86: aload_3
/*    */     //   87: if_acmpeq +8 -> 95
/*    */     //   90: aload_2
/*    */     //   91: aload_3
/*    */     //   92: invokevirtual 152	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*    */     //   95: aload 4
/*    */     //   97: ifnull +10 -> 107
/*    */     //   100: aload 4
/*    */     //   102: invokeinterface 138 1 0
/*    */     //   107: aload_2
/*    */     //   108: athrow
/*    */     //   109: astore_3
/*    */     //   110: aload_2
/*    */     //   111: ifnonnull +8 -> 119
/*    */     //   114: aload_3
/*    */     //   115: astore_2
/*    */     //   116: goto +13 -> 129
/*    */     //   119: aload_2
/*    */     //   120: aload_3
/*    */     //   121: if_acmpeq +8 -> 129
/*    */     //   124: aload_2
/*    */     //   125: aload_3
/*    */     //   126: invokevirtual 152	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*    */     //   129: aload_2
/*    */     //   130: athrow
/*    */     //   131: return
/*    */     // Line number table:
/*    */     //   Java source line #34	-> byte code offset #0
/*    */     //   Java source line #35	-> byte code offset #4
/*    */     //   Java source line #36	-> byte code offset #10
/*    */     //   Java source line #36	-> byte code offset #14
/*    */     //   Java source line #38	-> byte code offset #30
/*    */     //   Java source line #43	-> byte code offset #131
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	132	0	args	String[]
/*    */     //   4	128	1	c1	Connection
/*    */     //   19	88	4	c	Connection
/*    */     //   30	28	5	ps	java.sql.PreparedStatement
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   19	60	75	finally
/*    */     //   14	109	109	finally
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\util\DBUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */