package org.hsqldb;

import org.hsqldb.types.CharacterType;
import org.hsqldb.types.DateTimeType;
import org.hsqldb.types.NumberType;
import org.hsqldb.types.Type;
import org.hsqldb.types.UserTypeModifier;

public class TypeInvariants
{
  public static final Type CARDINAL_NUMBER;
  public static final Type YES_OR_NO;
  public static final Type CHARACTER_DATA;
  public static final Type SQL_IDENTIFIER;
  public static final Type TIME_STAMP;
  
  static
  {
    HsqlNameManager.HsqlName localHsqlName = HsqlNameManager.newInfoSchemaObjectName("CARDINAL_NUMBER", false, 13);
    CARDINAL_NUMBER = new NumberType(25, 0L, 0);
    CARDINAL_NUMBER.userTypeModifier = new UserTypeModifier(localHsqlName, 13, CARDINAL_NUMBER);
    localHsqlName = HsqlNameManager.newInfoSchemaObjectName("YES_OR_NO", false, 13);
    YES_OR_NO = new CharacterType(12, 3L);
    YES_OR_NO.userTypeModifier = new UserTypeModifier(localHsqlName, 13, YES_OR_NO);
    localHsqlName = HsqlNameManager.newInfoSchemaObjectName("CHARACTER_DATA", false, 13);
    CHARACTER_DATA = new CharacterType(12, 65536L);
    CHARACTER_DATA.userTypeModifier = new UserTypeModifier(localHsqlName, 13, CHARACTER_DATA);
    localHsqlName = HsqlNameManager.newInfoSchemaObjectName("SQL_IDENTIFIER", false, 13);
    SQL_IDENTIFIER = new CharacterType(12, 128L);
    SQL_IDENTIFIER.userTypeModifier = new UserTypeModifier(localHsqlName, 13, SQL_IDENTIFIER);
    localHsqlName = HsqlNameManager.newInfoSchemaObjectName("TIME_STAMP", false, 13);
    TIME_STAMP = new DateTimeType(93, 93, 6);
    TIME_STAMP.userTypeModifier = new UserTypeModifier(localHsqlName, 13, TIME_STAMP);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\TypeInvariants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */