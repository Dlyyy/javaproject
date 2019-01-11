package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.lib.HsqlList;
import org.hsqldb.types.BinaryData;
import org.hsqldb.types.Collation;
import org.hsqldb.types.Type;

public final class ExpressionLike
  extends ExpressionLogical
{
  private static final int ESCAPE = 2;
  private Like likeObject;
  
  ExpressionLike(Expression paramExpression1, Expression paramExpression2, Expression paramExpression3, boolean paramBoolean)
  {
    super(53);
    this.nodes = new Expression[3];
    this.nodes[0] = paramExpression1;
    this.nodes[1] = paramExpression2;
    this.nodes[2] = paramExpression3;
    this.likeObject = new Like();
    this.noOptimisation = paramBoolean;
  }
  
  private ExpressionLike(ExpressionLike paramExpressionLike)
  {
    super(53);
    this.nodes = paramExpressionLike.nodes;
    this.likeObject = paramExpressionLike.likeObject;
  }
  
  public HsqlList resolveColumnReferences(Session paramSession, RangeGroup paramRangeGroup, int paramInt, RangeGroup[] paramArrayOfRangeGroup, HsqlList paramHsqlList, boolean paramBoolean)
  {
    for (int i = 0; i < this.nodes.length; i++) {
      if (this.nodes[i] != null) {
        paramHsqlList = this.nodes[i].resolveColumnReferences(paramSession, paramRangeGroup, paramInt, paramArrayOfRangeGroup, paramHsqlList, paramBoolean);
      }
    }
    return paramHsqlList;
  }
  
  public Object getValue(Session paramSession)
  {
    if (this.opType != 53) {
      return super.getValue(paramSession);
    }
    Object localObject1 = this.nodes[0].getValue(paramSession);
    Object localObject2 = this.nodes[1].getValue(paramSession);
    Object localObject3 = this.nodes[2] == null ? null : this.nodes[2].getValue(paramSession);
    if (this.likeObject.isVariable) {
      synchronized (this.likeObject)
      {
        this.likeObject.setPattern(paramSession, localObject2, localObject3, this.nodes[2] != null);
        return this.likeObject.compare(paramSession, localObject1);
      }
    }
    return this.likeObject.compare(paramSession, localObject1);
  }
  
  public void resolveTypes(Session paramSession, Expression paramExpression)
  {
    if (this.opType != 53) {
      return;
    }
    for (int i = 0; i < this.nodes.length; i++) {
      if (this.nodes[i] != null) {
        this.nodes[i].resolveTypes(paramSession, this);
      }
    }
    if ((this.nodes[0].isUnresolvedParam()) && (this.nodes[1].isUnresolvedParam())) {
      this.nodes[0].dataType = Type.SQL_VARCHAR_DEFAULT;
    }
    if ((this.nodes[0].dataType == null) && (this.nodes[1].dataType == null)) {
      throw Error.error(5567);
    }
    if (this.nodes[0].isUnresolvedParam()) {
      this.nodes[0].dataType = (this.nodes[1].dataType.isBinaryType() ? Type.SQL_VARBINARY_DEFAULT : Type.SQL_VARCHAR_DEFAULT);
    } else if (this.nodes[1].isUnresolvedParam()) {
      this.nodes[1].dataType = (this.nodes[0].dataType.isBinaryType() ? Type.SQL_VARBINARY_DEFAULT : Type.SQL_VARCHAR_DEFAULT);
    }
    if ((this.nodes[0].dataType == null) || (this.nodes[1].dataType == null)) {
      throw Error.error(5567);
    }
    i = this.nodes[0].dataType.typeComparisonGroup;
    if (i != 12) {
      if (i == 61)
      {
        this.likeObject.isBinary = true;
      }
      else
      {
        if (paramSession.database.sqlEnforceTypes) {
          throw Error.error(5562);
        }
        if (i == 1111) {
          throw Error.error(5563);
        }
        this.nodes[0] = ExpressionOp.getCastExpression(paramSession, this.nodes[0], Type.SQL_VARCHAR_DEFAULT);
        i = 12;
      }
    }
    if (this.nodes[1].dataType.typeComparisonGroup != i) {
      throw Error.error(5563);
    }
    if (i == 12)
    {
      bool = (!this.nodes[0].dataType.getCollation().isCaseSensitive()) || (!this.nodes[1].dataType.getCollation().isCaseSensitive());
      this.likeObject.setIgnoreCase(bool);
    }
    this.likeObject.dataType = this.nodes[0].dataType;
    boolean bool = true;
    if (this.nodes[2] != null)
    {
      if (this.nodes[2].isUnresolvedParam()) {
        this.nodes[2].dataType = (this.likeObject.isBinary ? Type.SQL_VARBINARY : Type.SQL_VARCHAR);
      }
      if (this.nodes[2].dataType.typeComparisonGroup != i) {
        throw Error.error(5563);
      }
      this.nodes[2].resolveTypes(paramSession, this);
      bool = this.nodes[2].opType == 1;
      if (bool)
      {
        this.nodes[2].setAsConstantValue(paramSession, paramExpression);
        if (this.nodes[2].dataType == null) {
          throw Error.error(5567);
        }
        if (this.nodes[2].valueData != null)
        {
          long l;
          switch (this.nodes[2].dataType.typeCode)
          {
          case 1: 
          case 12: 
            l = ((String)this.nodes[2].valueData).length();
            break;
          case 60: 
          case 61: 
            l = ((BinaryData)this.nodes[2].valueData).length(paramSession);
            break;
          default: 
            throw Error.error(5563);
          }
          if (l != 1L) {
            throw Error.error(3439);
          }
        }
      }
    }
    int j = this.nodes[1].opType == 1 ? 1 : 0;
    if ((j != 0) && (bool))
    {
      if (this.nodes[0].opType == 1)
      {
        setAsConstantValue(paramSession, paramExpression);
        this.likeObject = null;
        return;
      }
      this.likeObject.isVariable = false;
    }
    Object localObject1 = j != 0 ? this.nodes[1].getValue(paramSession) : null;
    int k = (bool) && (this.nodes[2] != null) ? 1 : 0;
    Object localObject2 = k != 0 ? this.nodes[2].getValue(paramSession) : null;
    this.likeObject.setPattern(paramSession, localObject1, localObject2, this.nodes[2] != null);
    if (this.noOptimisation) {
      return;
    }
    if (this.likeObject.isEquivalentToUnknownPredicate())
    {
      setAsConstantValue(paramSession, paramExpression);
      this.likeObject = null;
      return;
    }
    if (this.likeObject.isEquivalentToEqualsPredicate())
    {
      this.opType = 40;
      this.nodes[1] = new ExpressionValue(this.likeObject.getRangeLow(), Type.SQL_VARCHAR);
      this.likeObject = null;
      setEqualityMode();
      return;
    }
    Object localObject3;
    if (this.likeObject.isEquivalentToNotNullPredicate())
    {
      localObject3 = new ExpressionLogical(47, this.nodes[0]);
      this.opType = 48;
      this.nodes = new Expression[1];
      this.nodes[0] = localObject3;
      this.likeObject = null;
      return;
    }
    if (this.nodes[0].opType == 2)
    {
      localObject3 = new ExpressionLike(this);
      ExpressionOp localExpressionOp1 = new ExpressionOp(37, this.nodes[1], this.nodes[2]);
      localExpressionOp1.resolveTypes(paramSession, null);
      ExpressionOp localExpressionOp2 = new ExpressionOp(84, this.nodes[0], localExpressionOp1);
      ExpressionLogical localExpressionLogical = new ExpressionLogical(40, localExpressionOp2, localExpressionOp1);
      localExpressionLogical = new ExpressionLogical(42, this.nodes[0], localExpressionOp1, localExpressionLogical);
      this.nodes = new Expression[2];
      this.likeObject = null;
      this.nodes[0] = localExpressionLogical;
      this.nodes[1] = localObject3;
      this.opType = 49;
    }
  }
  
  public String getSQL()
  {
    if (this.likeObject == null) {
      return super.getSQL();
    }
    String str1 = getContextSQL(this.nodes[0]);
    String str2 = getContextSQL(this.nodes[1]);
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(str1).append(' ').append("LIKE").append(' ');
    localStringBuffer.append(str2);
    if (this.nodes[2] != null)
    {
      localStringBuffer.append(' ').append("ESCAPE").append(' ');
      localStringBuffer.append(this.nodes[2].getSQL());
      localStringBuffer.append(' ');
    }
    return localStringBuffer.toString();
  }
  
  protected String describe(Session paramSession, int paramInt)
  {
    if (this.likeObject == null) {
      return super.describe(paramSession, paramInt);
    }
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append('\n');
    for (int i = 0; i < paramInt; i++) {
      localStringBuffer.append(' ');
    }
    localStringBuffer.append("LIKE ");
    localStringBuffer.append(this.likeObject.describe(paramSession));
    return localStringBuffer.toString();
  }
  
  public Expression duplicate()
  {
    ExpressionLike localExpressionLike = (ExpressionLike)super.duplicate();
    if (this.likeObject != null) {
      localExpressionLike.likeObject = this.likeObject.duplicate();
    }
    return localExpressionLike;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\ExpressionLike.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */