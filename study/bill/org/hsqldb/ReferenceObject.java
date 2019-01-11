package org.hsqldb;

import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.rights.Grantee;

public class ReferenceObject
  implements SchemaObject
{
  HsqlNameManager.HsqlName name;
  HsqlNameManager.HsqlName target;
  
  public ReferenceObject(HsqlNameManager.HsqlName paramHsqlName1, HsqlNameManager.HsqlName paramHsqlName2)
  {
    this.name = paramHsqlName1;
    this.target = paramHsqlName2;
  }
  
  public int getType()
  {
    return 29;
  }
  
  public HsqlNameManager.HsqlName getName()
  {
    return this.name;
  }
  
  public HsqlNameManager.HsqlName getSchemaName()
  {
    return this.name.schema;
  }
  
  public HsqlNameManager.HsqlName getCatalogName()
  {
    return this.name.schema.schema;
  }
  
  public Grantee getOwner()
  {
    return this.name.schema.owner;
  }
  
  public OrderedHashSet getReferences()
  {
    OrderedHashSet localOrderedHashSet = new OrderedHashSet();
    localOrderedHashSet.add(this.target);
    return localOrderedHashSet;
  }
  
  public OrderedHashSet getComponents()
  {
    return null;
  }
  
  public void compile(Session paramSession, SchemaObject paramSchemaObject) {}
  
  public String getSQL()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("CREATE").append(' ').append("SYNONYM");
    localStringBuffer.append(' ').append(this.name.getSchemaQualifiedStatementName());
    localStringBuffer.append(' ').append("FOR").append(' ');
    localStringBuffer.append(this.target.getSchemaQualifiedStatementName());
    return localStringBuffer.toString();
  }
  
  public long getChangeTimestamp()
  {
    return 0L;
  }
  
  public HsqlNameManager.HsqlName getTarget()
  {
    return this.target;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\ReferenceObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */