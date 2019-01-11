package org.hsqldb.auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Properties;

public class LdapAuthBeanTester
{
  public static void main(String[] paramArrayOfString)
    throws IOException
  {
    if (paramArrayOfString.length != 3) {
      throw new IllegalArgumentException("SYNTAX:  java " + AuthBeanMultiplexer.class.getName() + " path/to/file.properties <USERNAME> <PASSWORD>");
    }
    File localFile = new File(paramArrayOfString[0]);
    if (!localFile.isFile()) {
      throw new IllegalArgumentException("Not a file: " + localFile.getAbsolutePath());
    }
    Properties localProperties = new Properties();
    localProperties.load(new FileInputStream(localFile));
    String str1 = localProperties.getProperty("trustStore");
    String str2 = localProperties.getProperty("startTls");
    String str3 = localProperties.getProperty("ldapPort");
    String str4 = localProperties.getProperty("roleSchemaValuePattern");
    String str5 = localProperties.getProperty("accessValuePattern");
    String str6 = localProperties.getProperty("securityMechanism");
    String str7 = localProperties.getProperty("ldapHost");
    String str8 = localProperties.getProperty("principalTemplate");
    String str9 = localProperties.getProperty("initialContextFactory");
    String str10 = localProperties.getProperty("saslRealm");
    String str11 = localProperties.getProperty("parentDn");
    String str12 = localProperties.getProperty("rdnAttribute");
    String str13 = localProperties.getProperty("rolesSchemaAttribute");
    String str14 = localProperties.getProperty("accessAttribute");
    if (str1 != null)
    {
      if (!new File(str1).isFile()) {
        throw new IllegalArgumentException("Specified trust store is not a file: " + str1);
      }
      System.setProperty("javax.net.ssl.trustStore", str1);
    }
    LdapAuthBean localLdapAuthBean = new LdapAuthBean();
    if (str2 != null) {
      localLdapAuthBean.setStartTls(Boolean.parseBoolean(str2));
    }
    if (str3 != null) {
      localLdapAuthBean.setLdapPort(Integer.parseInt(str3));
    }
    if (str4 != null) {
      localLdapAuthBean.setRoleSchemaValuePatternString(str4);
    }
    if (str5 != null) {
      localLdapAuthBean.setAccessValuePatternString(str5);
    }
    if (str6 != null) {
      localLdapAuthBean.setSecurityMechanism(str6);
    }
    if (str7 != null) {
      localLdapAuthBean.setLdapHost(str7);
    }
    if (str8 != null) {
      localLdapAuthBean.setPrincipalTemplate(str8);
    }
    if (str9 != null) {
      localLdapAuthBean.setInitialContextFactory(str9);
    }
    if (str10 != null) {
      localLdapAuthBean.setSaslRealm(str10);
    }
    if (str11 != null) {
      localLdapAuthBean.setParentDn(str11);
    }
    if (str12 != null) {
      localLdapAuthBean.setRdnAttribute(str12);
    }
    if (str13 != null) {
      localLdapAuthBean.setRolesSchemaAttribute(str13);
    }
    if (str14 != null) {
      localLdapAuthBean.setAccessAttribute(str14);
    }
    localLdapAuthBean.init();
    String[] arrayOfString = null;
    try
    {
      arrayOfString = localLdapAuthBean.authenticate(paramArrayOfString[1], paramArrayOfString[2]);
    }
    catch (DenyException localDenyException)
    {
      System.out.println("<DENY ACCESS>");
      return;
    }
    if (arrayOfString == null) {
      System.out.println("<ALLOW ACCESS w/ local Roles+Schema>");
    } else {
      System.out.println(Integer.toString(arrayOfString.length) + " Roles/Schema: " + Arrays.toString(arrayOfString));
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\auth\LdapAuthBeanTester.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */