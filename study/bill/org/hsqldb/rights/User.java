package org.hsqldb.rights;

import org.hsqldb.Database;
import org.hsqldb.HsqlNameManager.HsqlName;
import org.hsqldb.SchemaManager;
import org.hsqldb.error.Error;
import org.hsqldb.lib.StringConverter;

public class User
  extends Grantee
{
  private String password;
  public boolean isLocalOnly;
  public boolean isExternalOnly;
  private HsqlNameManager.HsqlName initialSchema = null;
  
  User(HsqlNameManager.HsqlName paramHsqlName, GranteeManager paramGranteeManager)
  {
    super(paramHsqlName, paramGranteeManager);
    if (paramGranteeManager != null) {
      updateAllRights();
    }
  }
  
  public String getSQL()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("CREATE").append(' ').append("USER");
    localStringBuffer.append(' ').append(this.granteeName.statementName).append(' ');
    localStringBuffer.append("PASSWORD").append(' ').append("DIGEST");
    localStringBuffer.append(' ').append('\'').append(this.password).append('\'');
    return localStringBuffer.toString();
  }
  
  public String getPasswordDigest()
  {
    return this.password;
  }
  
  public void setPassword(String paramString, boolean paramBoolean)
  {
    if (!paramBoolean) {
      paramString = this.granteeManager.digest(paramString);
    }
    this.password = paramString;
  }
  
  public void checkPassword(String paramString)
  {
    String str = this.granteeManager.digest(paramString);
    if (!str.equals(this.password)) {
      throw Error.error(4000);
    }
  }
  
  public HsqlNameManager.HsqlName getInitialSchema()
  {
    return this.initialSchema;
  }
  
  public HsqlNameManager.HsqlName getInitialOrDefaultSchema()
  {
    if (this.initialSchema != null) {
      return this.initialSchema;
    }
    HsqlNameManager.HsqlName localHsqlName = this.granteeManager.database.schemaManager.findSchemaHsqlName(getName().getNameString());
    if (localHsqlName == null) {
      return this.granteeManager.database.schemaManager.getDefaultSchemaHsqlName();
    }
    return localHsqlName;
  }
  
  public void setInitialSchema(HsqlNameManager.HsqlName paramHsqlName)
  {
    this.initialSchema = paramHsqlName;
  }
  
  public String getInitialSchemaSQL()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("ALTER").append(' ');
    localStringBuffer.append("USER").append(' ');
    localStringBuffer.append(getName().getStatementName()).append(' ');
    localStringBuffer.append("SET").append(' ');
    localStringBuffer.append("INITIAL").append(' ');
    localStringBuffer.append("SCHEMA").append(' ');
    localStringBuffer.append(this.initialSchema.getStatementName());
    return localStringBuffer.toString();
  }
  
  public String getLocalUserSQL()
  {
    StringBuffer localStringBuffer = new StringBuffer(64);
    localStringBuffer.append("ALTER").append(' ');
    localStringBuffer.append("USER").append(' ');
    localStringBuffer.append(getName().getStatementName()).append(' ');
    localStringBuffer.append("SET").append(' ').append("LOCAL");
    localStringBuffer.append(' ').append("TRUE");
    return localStringBuffer.toString();
  }
  
  public String getSetUserPasswordDigestSQL(String paramString, boolean paramBoolean)
  {
    if (!paramBoolean) {
      paramString = this.granteeManager.digest(paramString);
    }
    StringBuffer localStringBuffer = new StringBuffer(64);
    localStringBuffer.append("ALTER").append(' ');
    localStringBuffer.append("USER").append(' ');
    localStringBuffer.append(getName().getStatementName()).append(' ');
    localStringBuffer.append("SET").append(' ');
    localStringBuffer.append("PASSWORD").append(' ').append("DIGEST");
    localStringBuffer.append(' ').append('\'').append(paramString).append('\'');
    return localStringBuffer.toString();
  }
  
  public static String getSetCurrentPasswordDigestSQL(GranteeManager paramGranteeManager, String paramString, boolean paramBoolean)
  {
    if (!paramBoolean) {
      paramString = paramGranteeManager.digest(paramString);
    }
    StringBuffer localStringBuffer = new StringBuffer(64);
    localStringBuffer.append("SET").append(' ');
    localStringBuffer.append("PASSWORD").append(' ').append("DIGEST");
    localStringBuffer.append(' ').append('\'').append(paramString).append('\'');
    return localStringBuffer.toString();
  }
  
  public String getConnectUserSQL()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("SET").append(' ');
    localStringBuffer.append("SESSION").append(' ');
    localStringBuffer.append("AUTHORIZATION").append(' ');
    localStringBuffer.append(StringConverter.toQuotedString(getName().getNameString(), '\'', true));
    return localStringBuffer.toString();
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\rights\User.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */