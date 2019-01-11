package org.hsqldb.jdbc;

import java.sql.NClob;
import java.sql.SQLException;

public class JDBCNClob
  extends JDBCClob
  implements NClob
{
  protected JDBCNClob() {}
  
  public JDBCNClob(String paramString)
    throws SQLException
  {
    super(paramString);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\jdbc\JDBCNClob.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */