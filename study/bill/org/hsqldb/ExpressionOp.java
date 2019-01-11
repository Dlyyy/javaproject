package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.HsqlList;
import org.hsqldb.map.ValuePool;
import org.hsqldb.navigator.RangeIterator;
import org.hsqldb.types.BinaryData;
import org.hsqldb.types.BinaryType;
import org.hsqldb.types.BlobData;
import org.hsqldb.types.CharacterType;
import org.hsqldb.types.DateTimeType;
import org.hsqldb.types.IntervalType;
import org.hsqldb.types.Type;
import org.hsqldb.types.UserTypeModifier;

public class ExpressionOp
  extends Expression
{
  static final ExpressionOp limitOneExpression = new ExpressionOp(91, new ExpressionValue(ValuePool.INTEGER_0, Type.SQL_INTEGER), new ExpressionValue(ValuePool.INTEGER_1, Type.SQL_INTEGER));
  
  ExpressionOp(int paramInt, Expression[] paramArrayOfExpression)
  {
    super(paramInt);
    switch (this.opType)
    {
    case 86: 
      this.nodes = paramArrayOfExpression;
      return;
    }
    throw Error.runtimeError(201, "ExpressionOp");
  }
  
  ExpressionOp(int paramInt, Expression paramExpression1, Expression paramExpression2)
  {
    super(paramInt);
    this.nodes = new Expression[2];
    this.nodes[0] = paramExpression1;
    this.nodes[1] = paramExpression2;
    switch (this.opType)
    {
    case 37: 
    case 88: 
    case 89: 
    case 91: 
    case 92: 
      return;
    case 84: 
      this.dataType = paramExpression1.dataType;
      return;
    }
    throw Error.runtimeError(201, "ExpressionOp");
  }
  
  ExpressionOp(Expression paramExpression, Type paramType)
  {
    super(87);
    this.nodes = new Expression[1];
    this.nodes[0] = paramExpression;
    this.dataType = paramType;
    this.alias = paramExpression.alias;
  }
  
  ExpressionOp(Expression paramExpression)
  {
    super(paramExpression.dataType.isDateTimeTypeWithZone() ? 87 : 88);
    switch (paramExpression.dataType.typeCode)
    {
    case 94: 
      this.nodes = new Expression[1];
      this.nodes[0] = new ExpressionOp(88, paramExpression, null);
      this.nodes[0].dataType = paramExpression.dataType;
      this.dataType = DateTimeType.getDateTimeType(92, paramExpression.dataType.scale);
      break;
    case 95: 
      this.nodes = new Expression[1];
      this.nodes[0] = new ExpressionOp(88, paramExpression, null);
      this.nodes[0].dataType = paramExpression.dataType;
      this.dataType = DateTimeType.getDateTimeType(93, paramExpression.dataType.scale);
      break;
    case 92: 
      this.nodes = new Expression[2];
      this.nodes[0] = paramExpression;
      this.nodes[0].dataType = paramExpression.dataType;
      this.dataType = DateTimeType.getDateTimeType(94, paramExpression.dataType.scale);
      break;
    case 93: 
      this.nodes = new Expression[2];
      this.nodes[0] = paramExpression;
      this.nodes[0].dataType = paramExpression.dataType;
      this.dataType = DateTimeType.getDateTimeType(95, paramExpression.dataType.scale);
      break;
    default: 
      throw Error.runtimeError(201, "ExpressionOp");
    }
    this.alias = paramExpression.alias;
  }
  
  public static Expression getCastExpression(Session paramSession, Expression paramExpression, Type paramType)
  {
    if (paramExpression.getType() == 1)
    {
      Object localObject = paramType.castToType(paramSession, paramExpression.getValue(paramSession), paramExpression.getDataType());
      return new ExpressionValue(localObject, paramType);
    }
    return new ExpressionOp(paramExpression, paramType);
  }
  
  public String getSQL()
  {
    StringBuffer localStringBuffer = new StringBuffer(64);
    String str1 = getContextSQL(this.nodes.length > 0 ? this.nodes[0] : null);
    String str2 = getContextSQL(this.nodes.length > 1 ? this.nodes[1] : null);
    switch (this.opType)
    {
    case 1: 
      if (this.valueData == null) {
        return "NULL";
      }
      if (this.dataType == null) {
        throw Error.runtimeError(201, "ExpressionOp");
      }
      return this.dataType.convertToSQLString(this.valueData);
    case 37: 
      localStringBuffer.append(' ').append("LIKE").append(' ');
      localStringBuffer.append(str1).append(' ').append(str2).append(' ');
    case 87: 
      localStringBuffer.append(' ').append("CAST").append('(');
      localStringBuffer.append(str1).append(' ').append("AS").append(' ');
      localStringBuffer.append(this.dataType.getTypeDefinition());
      localStringBuffer.append(')');
      return localStringBuffer.toString();
    case 89: 
      localStringBuffer.append(' ').append("CASEWHEN").append('(');
      localStringBuffer.append(str1).append(',').append(str2).append(')');
      return localStringBuffer.toString();
    case 92: 
      localStringBuffer.append(str1).append(',').append(str2);
      return localStringBuffer.toString();
    case 91: 
      if (str1 != null)
      {
        localStringBuffer.append(' ').append("OFFSET").append(' ');
        localStringBuffer.append(str1).append(' ');
      }
      if (str2 != null)
      {
        localStringBuffer.append(' ').append("FETCH").append(' ');
        localStringBuffer.append("FIRST");
        localStringBuffer.append(str2).append(' ').append(str2).append(' ');
        localStringBuffer.append("ROWS").append(' ').append("ONLY");
        localStringBuffer.append(' ');
      }
      break;
    case 88: 
      localStringBuffer.append(str1).append(' ').append("AT").append(' ');
      if (this.nodes[1] == null)
      {
        localStringBuffer.append("LOCAL").append(' ');
      }
      else
      {
        localStringBuffer.append("TIME").append(' ').append("ZONE");
        localStringBuffer.append(' ');
        localStringBuffer.append(str2);
      }
      break;
    case 86: 
      localStringBuffer.append("CONCAT_WS").append(836);
      localStringBuffer.append(str1);
      for (int i = 0; i < this.nodes.length; i++) {
        localStringBuffer.append(',').append(this.nodes[i].getSQL());
      }
      localStringBuffer.append(822);
      break;
    default: 
      throw Error.runtimeError(201, "ExpressionOp");
    }
    return localStringBuffer.toString();
  }
  
  protected String describe(Session paramSession, int paramInt)
  {
    StringBuffer localStringBuffer = new StringBuffer(64);
    localStringBuffer.append('\n');
    for (int i = 0; i < paramInt; i++) {
      localStringBuffer.append(' ');
    }
    switch (this.opType)
    {
    case 1: 
      localStringBuffer.append("VALUE = ").append(this.dataType.convertToSQLString(this.valueData));
      localStringBuffer.append(", TYPE = ").append(this.dataType.getNameString());
      return localStringBuffer.toString();
    case 37: 
      localStringBuffer.append("LIKE").append(' ').append("ARG ");
      localStringBuffer.append(this.dataType.getTypeDefinition());
      localStringBuffer.append(' ');
      break;
    case 26: 
      localStringBuffer.append("VALUE").append(' ').append("LIST ");
      for (i = 0; i < this.nodes.length; i++)
      {
        localStringBuffer.append(this.nodes[i].describe(paramSession, paramInt + 1));
        localStringBuffer.append(' ');
      }
      return localStringBuffer.toString();
    case 87: 
      localStringBuffer.append("CAST").append(' ');
      localStringBuffer.append(this.dataType.getTypeDefinition());
      localStringBuffer.append(' ');
      break;
    case 89: 
      localStringBuffer.append("CASEWHEN").append(' ');
      break;
    case 86: 
      localStringBuffer.append("CONCAT_WS").append(' ');
      break;
    }
    if (getLeftNode() != null)
    {
      localStringBuffer.append(" arg_left=[");
      localStringBuffer.append(this.nodes[0].describe(paramSession, paramInt + 1));
      localStringBuffer.append(']');
    }
    if (getRightNode() != null)
    {
      localStringBuffer.append(" arg_right=[");
      localStringBuffer.append(this.nodes[1].describe(paramSession, paramInt + 1));
      localStringBuffer.append(']');
    }
    return localStringBuffer.toString();
  }
  
  public HsqlList resolveColumnReferences(Session paramSession, RangeGroup paramRangeGroup, int paramInt, RangeGroup[] paramArrayOfRangeGroup, HsqlList paramHsqlList, boolean paramBoolean)
  {
    if (this.opType == 1) {
      return paramHsqlList;
    }
    switch (this.opType)
    {
    case 89: 
      paramBoolean = false;
      break;
    }
    for (int i = 0; i < this.nodes.length; i++) {
      if (this.nodes[i] != null) {
        paramHsqlList = this.nodes[i].resolveColumnReferences(paramSession, paramRangeGroup, paramInt, paramArrayOfRangeGroup, paramHsqlList, paramBoolean);
      }
    }
    return paramHsqlList;
  }
  
  public void resolveTypes(Session paramSession, Expression paramExpression)
  {
    switch (this.opType)
    {
    case 89: 
      break;
    default: 
      for (int i = 0; i < this.nodes.length; i++) {
        if (this.nodes[i] != null) {
          this.nodes[i].resolveTypes(paramSession, this);
        }
      }
    }
    switch (this.opType)
    {
    case 1: 
      break;
    case 37: 
      this.dataType = this.nodes[0].dataType;
      if ((this.nodes[0].opType == 1) && ((this.nodes[1] == null) || (this.nodes[1].opType == 1))) {
        setAsConstantValue(paramSession, paramExpression);
      }
      break;
    case 87: 
      Expression localExpression = this.nodes[0];
      Type localType = localExpression.dataType;
      if ((localType != null) && (!this.dataType.canConvertFrom(localType))) {
        throw Error.error(5561);
      }
      if (localExpression.opType == 1)
      {
        setAsConstantValue(paramSession, paramExpression);
        localExpression.dataType = this.dataType;
        localExpression.valueData = this.valueData;
        if (paramExpression != null) {
          paramExpression.replaceNode(this, localExpression);
        }
      }
      else if (this.nodes[0].opType == 8)
      {
        localExpression.dataType = this.dataType;
      }
      break;
    case 88: 
      if (this.nodes[0].dataType == null) {
        throw Error.error(5567);
      }
      if (this.nodes[1] != null)
      {
        if (this.nodes[1].dataType == null) {
          this.nodes[1].dataType = Type.SQL_INTERVAL_HOUR_TO_MINUTE;
        }
        if (this.nodes[1].dataType.typeCode != 111) {
          if (this.nodes[1].opType == 1)
          {
            this.nodes[1].valueData = Type.SQL_INTERVAL_HOUR_TO_MINUTE.castToType(paramSession, this.nodes[1].valueData, this.nodes[1].dataType);
            this.nodes[1].dataType = Type.SQL_INTERVAL_HOUR_TO_MINUTE;
          }
          else
          {
            throw Error.error(5563);
          }
        }
      }
      switch (this.nodes[0].dataType.typeCode)
      {
      case 92: 
        this.dataType = DateTimeType.getDateTimeType(94, this.nodes[0].dataType.scale);
        break;
      case 93: 
        this.dataType = DateTimeType.getDateTimeType(95, this.nodes[0].dataType.scale);
        break;
      case 94: 
      case 95: 
        this.dataType = this.nodes[0].dataType;
        break;
      default: 
        throw Error.error(5563);
      }
      break;
    case 89: 
      resolveTypesForCaseWhen(paramSession, paramExpression);
      break;
    case 86: 
      for (int j = 0; j < this.nodes.length; j++) {
        this.nodes[j].dataType = Type.SQL_VARCHAR_DEFAULT;
      }
      this.dataType = Type.SQL_VARCHAR_DEFAULT;
      break;
    case 92: 
      resolveTypesForAlternative(paramSession);
      break;
    case 91: 
      if (this.nodes[0] != null)
      {
        if (this.nodes[0].dataType == null) {
          throw Error.error(5567);
        }
        if (!this.nodes[0].dataType.isIntegralType()) {
          throw Error.error(5563);
        }
      }
      if (this.nodes[1] != null)
      {
        if (this.nodes[1].dataType == null) {
          throw Error.error(5567);
        }
        if (!this.nodes[1].dataType.isIntegralType()) {
          throw Error.error(5563);
        }
      }
      break;
    case 84: 
      break;
    default: 
      throw Error.runtimeError(201, "ExpressionOp");
    }
  }
  
  void resolveTypesForAlternative(Session paramSession)
  {
    if (this.nodes[0].dataType == null) {
      this.nodes[0].dataType = this.nodes[1].dataType;
    }
    if (this.nodes[1].dataType == null) {
      this.nodes[1].dataType = this.nodes[0].dataType;
    }
    if (this.exprSubType == 87)
    {
      if (this.nodes[1].dataType == null) {
        this.nodes[1].dataType = (this.nodes[0].dataType = Type.SQL_VARCHAR_DEFAULT);
      }
      this.dataType = this.nodes[1].dataType;
      if (!this.nodes[1].dataType.equals(this.nodes[0].dataType))
      {
        if (this.dataType.isCharacterType()) {
          this.dataType = Type.SQL_VARCHAR_DEFAULT;
        }
        this.nodes[0] = new ExpressionOp(this.nodes[0], this.dataType);
      }
    }
    else
    {
      this.dataType = Type.getAggregateType(this.nodes[0].dataType, this.dataType);
      this.dataType = Type.getAggregateType(this.nodes[1].dataType, this.dataType);
    }
  }
  
  void resolveTypesForCaseWhen(Session paramSession, Expression paramExpression)
  {
    this.nodes[1].resolveTypes(paramSession, this);
    Object localObject = this;
    while (((Expression)localObject).opType == 89)
    {
      if (((Expression)localObject).exprSubType == 87) {
        this.dataType = localObject.nodes[1].dataType;
      } else {
        this.dataType = Type.getAggregateType(localObject.nodes[1].dataType, this.dataType);
      }
      if (localObject.nodes[1].nodes[1].opType == 89) {
        localObject = localObject.nodes[1].nodes[1];
      } else {
        localObject = localObject.nodes[1].nodes[0];
      }
    }
    localObject = this;
    while (((Expression)localObject).opType == 89)
    {
      if (localObject.nodes[1].dataType == null) {
        localObject.nodes[1].dataType = this.dataType;
      }
      if (localObject.nodes[1].nodes[1].dataType == null) {
        localObject.nodes[1].nodes[1].dataType = this.dataType;
      }
      if (localObject.nodes[1].nodes[0].dataType == null) {
        localObject.nodes[1].nodes[0].dataType = this.dataType;
      }
      if (localObject.nodes[1].nodes[1].opType == 89) {
        localObject = localObject.nodes[1].nodes[1];
      } else {
        localObject = localObject.nodes[1].nodes[0];
      }
    }
    for (localObject = this; ((Expression)localObject).opType == 89; localObject = localObject.nodes[1].nodes[1])
    {
      localObject.nodes[0].resolveTypes(paramSession, (Expression)localObject);
      if (localObject.nodes[0].isUnresolvedParam()) {
        localObject.nodes[0].dataType = Type.SQL_BOOLEAN;
      }
      localObject.nodes[1].nodes[0].resolveTypes(paramSession, localObject.nodes[1]);
      if (localObject.nodes[1].nodes[1].opType != 89) {
        localObject.nodes[1].nodes[1].resolveTypes(paramSession, localObject.nodes[1]);
      }
    }
    if (((paramExpression == null) || (paramExpression.opType != 92)) && ((this.dataType == null) || (this.dataType.typeCode == 0))) {
      throw Error.error(5567);
    }
  }
  
  public Object getValue(Session paramSession)
  {
    Object localObject4;
    Object localObject5;
    Object localObject1;
    Object localObject2;
    int k;
    switch (this.opType)
    {
    case 1: 
      return this.valueData;
    case 37: 
      int i = this.nodes[1] != null ? 1 : 0;
      int j = Integer.MAX_VALUE;
      if (this.dataType.isBinaryType())
      {
        localObject3 = (BinaryData)this.nodes[0].getValue(paramSession);
        if (localObject3 == null) {
          return null;
        }
        if (i != 0)
        {
          localObject4 = (BinaryData)this.nodes[1].getValue(paramSession);
          if (localObject4 == null) {
            return null;
          }
          if (((BinaryData)localObject4).length(paramSession) != 1L) {
            throw Error.error(3412);
          }
          j = localObject4.getBytes()[0];
        }
        localObject4 = ((BinaryData)localObject3).getBytes();
        localObject5 = new byte[localObject4.length];
        n = 0;
        i1 = 0;
        i2 = 0;
        i3 = 0;
        while (i2 < localObject4.length)
        {
          if (localObject4[i2] == j)
          {
            if (n != 0)
            {
              i1++;
              localObject5[(i3++)] = localObject4[i2];
              n = 0;
            }
            else
            {
              n = 1;
              if (i2 == localObject4.length - 1) {
                throw Error.error(3458);
              }
            }
          }
          else if ((localObject4[i2] == 95) || (localObject4[i2] == 37))
          {
            if (n == 0) {
              break;
            }
            i1++;
            localObject5[(i3++)] = localObject4[i2];
            n = 0;
          }
          else
          {
            if (n != 0) {
              throw Error.error(3458);
            }
            localObject5[(i3++)] = localObject4[i2];
          }
          i2++;
        }
        localObject5 = (byte[])ArrayUtil.resizeArrayIfDifferent(localObject5, i3);
        return new BinaryData((byte[])localObject5, false);
      }
      Object localObject3 = (String)Type.SQL_VARCHAR.convertToType(paramSession, this.nodes[0].getValue(paramSession), this.nodes[0].getDataType());
      if (localObject3 == null) {
        return null;
      }
      if (i != 0)
      {
        localObject4 = (String)Type.SQL_VARCHAR.convertToType(paramSession, this.nodes[1].getValue(paramSession), this.nodes[1].getDataType());
        if (localObject4 == null) {
          return null;
        }
        if (((String)localObject4).length() != 1) {
          throw Error.error(3439);
        }
        j = localObject4.getBytes()[0];
      }
      localObject4 = ((String)localObject3).toCharArray();
      localObject5 = new char[localObject4.length];
      int n = 0;
      int i1 = 0;
      int i2 = 0;
      int i3 = 0;
      while (i2 < localObject4.length)
      {
        if (localObject4[i2] == j)
        {
          if (n != 0)
          {
            i1++;
            localObject5[(i3++)] = localObject4[i2];
            n = 0;
          }
          else
          {
            n = 1;
            if (i2 == localObject4.length - 1) {
              throw Error.error(3458);
            }
          }
        }
        else if ((localObject4[i2] == '_') || (localObject4[i2] == '%'))
        {
          if (n == 0) {
            break;
          }
          i1++;
          localObject5[(i3++)] = localObject4[i2];
          n = 0;
        }
        else
        {
          if (n != 0) {
            throw Error.error(3458);
          }
          localObject5[(i3++)] = localObject4[i2];
        }
        i2++;
      }
      return new String((char[])localObject5, 0, i3);
    case 5: 
      localObject1 = paramSession.sessionContext.rangeIterators[this.rangePosition].getCurrent(this.columnIndex);
      return localObject1;
    case 90: 
      return this.nodes[0].getValue(paramSession);
    case 84: 
      if (this.nodes[0].dataType.isCharacterType())
      {
        localObject1 = this.nodes[1].getValue(paramSession);
        if (localObject1 == null) {
          return null;
        }
        CharacterType localCharacterType = (CharacterType)this.nodes[1].dataType;
        long l2 = ((CharacterType)this.nodes[1].dataType).size(paramSession, localObject1);
        localCharacterType = (CharacterType)this.nodes[0].dataType;
        localObject1 = this.nodes[0].getValue(paramSession);
        if (localObject1 == null) {
          return null;
        }
        return localCharacterType.substring(paramSession, localObject1, 0L, l2, true, false);
      }
      localObject1 = (BinaryData)this.nodes[1].getValue(paramSession);
      if (localObject1 == null) {
        return null;
      }
      long l1 = ((BinaryData)localObject1).length(paramSession);
      localObject4 = (BinaryType)this.nodes[0].dataType;
      localObject1 = (BinaryData)this.nodes[0].getValue(paramSession);
      if (localObject1 == null) {
        return null;
      }
      return ((BinaryType)localObject4).substring(paramSession, (BlobData)localObject1, 0L, l1, true);
    case 87: 
      localObject1 = this.dataType.castToType(paramSession, this.nodes[0].getValue(paramSession), this.nodes[0].dataType);
      if (this.dataType.userTypeModifier != null)
      {
        localObject2 = this.dataType.userTypeModifier.getConstraints();
        for (k = 0; k < localObject2.length; k++) {
          localObject2[k].checkCheckConstraint(paramSession, null, null, localObject1);
        }
      }
      return localObject1;
    case 89: 
      localObject1 = (Boolean)this.nodes[0].getValue(paramSession);
      if (Boolean.TRUE.equals(localObject1)) {
        return this.nodes[1].nodes[0].getValue(paramSession, this.dataType);
      }
      return this.nodes[1].nodes[1].getValue(paramSession, this.dataType);
    case 86: 
      localObject1 = (String)this.nodes[0].getValue(paramSession);
      if (localObject1 == null) {
        return null;
      }
      localObject2 = new StringBuffer("");
      k = 0;
      for (int m = 1; m < this.nodes.length; m++)
      {
        localObject5 = (String)this.nodes[m].getValue(paramSession);
        if (localObject5 != null)
        {
          if (k != 0) {
            ((StringBuffer)localObject2).append((String)localObject1);
          }
          ((StringBuffer)localObject2).append((String)localObject5);
          k = 1;
        }
      }
      return ((StringBuffer)localObject2).toString();
    case 88: 
      localObject1 = this.nodes[0].getValue(paramSession);
      localObject2 = this.nodes[1] == null ? null : this.nodes[1].getValue(paramSession);
      if (localObject1 == null) {
        return null;
      }
      if ((this.nodes[1] != null) && (localObject2 == null)) {
        return null;
      }
      long l3 = this.nodes[1] == null ? paramSession.getZoneSeconds() : ((IntervalType)this.nodes[1].dataType).getSeconds(localObject2);
      return ((DateTimeType)this.dataType).changeZone(localObject1, this.nodes[0].dataType, (int)l3, paramSession.getZoneSeconds());
    }
    throw Error.runtimeError(201, "ExpressionOp");
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\ExpressionOp.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */