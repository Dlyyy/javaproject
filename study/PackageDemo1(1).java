/**
	mkdir classes										//创建目录
	javac -d classes PackageDemo1.java					//编译java源文件,指定存放目录
	java -cp classes com.it18zhang.java.PackageDemo1	//运行程序,类全限定名
 *
 */
package com.it18zhang.java ;

public class PackageDemo1{
	public static void main(String[] args) 	{
		System.out.println("Hello World!");
		Person p = new Person();
		p.name = "好人";
		System.out.println(p.name);
	}
}
