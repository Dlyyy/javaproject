/*    */ package util;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.OutputStream;
/*    */ import java.io.OutputStreamWriter;
/*    */ 
/*    */ public class MysqlUtil
/*    */ {
/*    */   public static void backup(String mysqlPath, String backupfile) throws IOException
/*    */   {
/* 14 */     String commandFormat = "\"%s/bin/mysqldump.exe\" -u%s -p%s   -hlocalhost   -P%d %s -r \"%s\"";
/*    */     
/* 16 */     String command = String.format(commandFormat, new Object[] { mysqlPath, DBUtil.loginName, DBUtil.password, Integer.valueOf(DBUtil.port), 
/* 17 */       DBUtil.database, backupfile });
/* 18 */     Runtime.getRuntime().exec(command);
/*    */   }
/*    */   
/*    */   public static void recover(String mysqlPath, String recoverfile) {
/*    */     try {
/* 23 */       String commandFormat = "\"%s/bin/mysql.exe\" -u%s -p%s   %s ";
/* 24 */       String command = String.format(commandFormat, new Object[] { mysqlPath, DBUtil.loginName, DBUtil.password, 
/* 25 */         DBUtil.database });
/*    */       
/* 27 */       Process p = Runtime.getRuntime().exec(command);
/* 28 */       OutputStream out = p.getOutputStream();
/*    */       
/* 30 */       StringBuffer sb = new StringBuffer("");
/*    */       
/* 32 */       BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(recoverfile), "utf8"));
/* 33 */       String inStr; while ((inStr = br.readLine()) != null) { String inStr;
/* 34 */         sb.append(inStr + "\r\n");
/*    */       }
/* 36 */       String outStr = sb.toString();
/*    */       
/* 38 */       OutputStreamWriter writer = new OutputStreamWriter(out, "utf8");
/* 39 */       writer.write(outStr);
/* 40 */       writer.flush();
/* 41 */       out.close();
/* 42 */       br.close();
/* 43 */       writer.close();
/*    */     } catch (Exception e) {
/* 45 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */   
/*    */   public static void main(String[] args) throws IOException
/*    */   {
/* 51 */     String mysqlPath = "D:/tools/MYSQL/mysql-5.1.57-win32";
/* 52 */     String file = "C:/Documents and Settings/Administrator/My Documents/hutubill.sql";
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 58 */     recover(mysqlPath, file);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\util\MysqlUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */