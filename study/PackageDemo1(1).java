/**
	mkdir classes										//����Ŀ¼
	javac -d classes PackageDemo1.java					//����javaԴ�ļ�,ָ�����Ŀ¼
	java -cp classes com.it18zhang.java.PackageDemo1	//���г���,��ȫ�޶���
 *
 */
package com.it18zhang.java ;

public class PackageDemo1{
	public static void main(String[] args) 	{
		System.out.println("Hello World!");
		Person p = new Person();
		p.name = "����";
		System.out.println(p.name);
	}
}
