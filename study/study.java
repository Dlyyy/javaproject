class IfDemo{
	public static void main (String[] args){
		int age = 100;
		System.out.println(age<= 50 ? "小" : "大");

		int a =1 ,b = 2, c = 3;
		if (a>b) {
			if(a>c){
				System.out.println("max is ："+ a)；
			}
			else{
				System.out.println("max is :" + c);
			}
		}
		else{
			if (b>c) {
				
			}
			else{

			}
		}
		System.out.println("");

		//a ?b :c;
		System.out.println("max is" + (a>b ? a : b))
		System.out.println("max is " + (a > b ? (a > c ? a : c) : (b > c ? b : c)));
		
	}
}

class SwitchDemo{
	public static void main (String[] args){
		int x = 1 ;
		switch(x){
			case 1 :
				System.out.println("x");
				break ;
			case 2 :
				System.out.println("xx");
				break ;
			case 3 :
				System.out.println("xxx");
				break ;
			default:
				System.out.println("x?");
				break ;
		}
	}
}


//switch 多case处理.
class SwitchDemo2 {
	public static void main(String[] args){
		int month = 130 ;
		switch(month){
			case 2:
			case 3:
			case 4:
				System.out.println("春天") ;
				break ;
			case 5:
			case 6:
			case 7:
				System.out.println("夏天") ;
				break ;
			case 8:
			case 9:
			case 10:
				System.out.println("秋天") ;
				break ;
			case 11:
			case 12:
			case 1:
				System.out.println("冬天") ;
				break ;
			default:
				System.out.println("非法月份") ;
				break ;
		}
	}
}


/**
 * if + switch结合使用.
 */
class SwitchDemo3{
	public static void main(String[] args) 	{
		//春: 2 3 4		--1
		//春: 567		--2
		//春: 8910		--3
		//春: 11 12 1	--4 
		//
		int month = 12 ;
		//
		int season = (month + 1) / 3 ;		//3
		//判断是否是非法月份
		if(month < 1 || month > 12){
			System.out.println("非法月份");
		}
		//合法月份
		else{
			switch(season){
				case 1:
					System.out.println("春天");
					break ;
				case 2:
					System.out.println("夏天");
					break ;
				case 3:
					System.out.println("秋天");
					break ;
				case 0 : 
				case 4 : 
					System.out.println("冬天");
					break ;
			}
			
		}
	}
}


/**
 * while(exp){
 *    
 * }
 */
class WhileDemo1 {
	public static void main(String[] args)	{
		int i = 1 ;
		int sum = 0 ;
		while(i <= 100){
			sum = sum + i ;
			i ++ ;
		}
		System.out.println(sum);
	}
}


class WhileDemo3{
	public static void main(String[] args) 	{
		int i = 200 ;
		while(true){
			i ++ ;
			if(i % 5 == 0 && i < 100){
				continue  ;
			}
			System.out.println(i);
		}

	}
}


class DoWhileDemo1 {
	public static void main(String[] args) {
		int i = 1 ;
		do{
			System.out.println(i);
			i ++ ;
		}while(i > 10) ;
	}
}


/*
break :
----------------
	 循环中的break终止循环。

continue
---------------
	结束当前循环，进入下一次循环。

for(exp1;exp2;exp3){
	...
}

\r		//return 回车符
\n		//line 换行符
\t		//tab 制表符
*/


class ForDemo2{
	public static void main(String[] args) {
		int n = 10 ;
		for(int i = 1 ; i <= n ; i ++){
			//每行的程序
			for(int j = 0 ; j < i ; j ++){
				System.out.print("*") ;
			}
			System.out.println() ;
		}
	}
}


class BaiQianMaiBaiJi{
	public static void main (String[] args) {
		int x,y,z;//x为公鸡数量，y为母鸡数量，z为小鸡数量
        int cost;//费用
 
        for(x=0;x<=20;x++){
            for(y=0;y<=33;y++){
                z=100-x-y;
                if(z%3==0){
                    cost=5*x+3*y+z/3;
                    if(cost==100){
                        System.out.println("公鸡的数量为："+x+",母鸡的数量为："+y+"，小鸡的数量为："+z);
                    }
                }
            }
        }

	}
}


class EmptyTriangle{
	public static void main (String[] args){
		int line =5;
		for(int i=0; i<line ; i++){
			for (int j=0; j<(line -1) - i ; j ++ ) {
				System.out.println(" ");
			}
			System.out.println("*");
			if () {
				
			}
			else if () {
				  
			}
		}
	}
}


/*函数
-----------------
	1.函数必须定义在类中，一段具有的代码。
	2.函数格式
									 
		修饰符 返回值类型 函数名称(形式参数类型 形式参数名称,...){
			//执行依据...
			return xx ;		//从函数中返回.
		}

	3.函数特点
		1.封装功能
		2.重用
		3.调用才执行.
		4.无返回值的函数，使用void
		5.函数不能嵌套
	4.函数递归

		fabric(5){...}
		5 * 4!
		5 * 4 * 3!
		5 * 4 * 3 * 2!
		5 * 4 * 3 * 2 * 1!
		5 * 4 * 3 * 2 * 1
		//recursive:递归。

	5.函数重载(overload)
		函数名相同，只能依靠参数(跟参数名无关，个数/类型/顺序)的不同实现重载。
*/

class FunctionDemo1 {
	public static void main(String[] args) {
		//调用函数
		//int ret = add(1,2);
		//System.out.println(ret);
		//printEmptyTriangle(5);
		//int r = getMax(1,2,3);
		//System.out.println(r);

		//System.out.println(getMax(getMax(getMax(1,2),3),4));
		//hello();
		//System.out.println(divide(1,0));
		System.out.println(fabric(0));
	}

	//定义函数
	public static int add(int a,int b){
		return a + b ;
	}

	/**
	 *打印空心三角形函数
	 */
	public static void printEmptyTriangle(int line){
		//循环所有行
		for(int i = 0 ; i < line ; i ++){
			//1.输出n个空格
			for(int j = 0 ; j < line - 1 - i ; j ++){
				System.out.print(" ");
			}
			//2.输出一个*
			System.out.print("*");
			//3.处理最后一行,输出2n个*
			if(i == (line - 1)){
				for(int j = 0 ; j < 2 * i ; j ++){
					System.out.print("*") ;
				}
			}
			//中间行
			if(i != 0 && i != (line - 1)){
				//输出2n - 1个空格
				for(int j = 0 ; j < 2 * i - 1 ; j ++){
					System.out.print(" ");
				}
				System.out.print("*");//最后的*
			}
			System.out.print("\r\n");
		}
	}

	public static int getMax(int a,int b){
		return a > b ? a : b ;
	}

	public static void hello(){
		System.out.println("how are you!");
		hello() ;
	}

	public static float divide(int a , int b){
		if(b == 0){
			return 0 ;
		}
		return (float)a / b ;
	}

	//阶乘,1 x 2 x 3 x 4 x ...x n
//	public static int fabric(int n){
//		int sum = 1 ;
//		for(int i = 1 ; i <= n ; i ++){
//			sum = sum * i ;
//		}
//		return sum ;
//	}

	//n * (n-1) * (n-1 -1) * .. * 1
	public static int fabric(int n){
		if(n == 1){
			return 1 ;
		}
		return n * fabric(n - 1);
	}
}


class FunctionDemo2 {
	public static void main(String[] args) {
		//System.out.println(add(1,2.0f));
		add(1,2.0f);
		out(1,2,3);

	}
	
	//add
	public static int add(int a,int b){
		return a + b ;
	}

	//重载add
	public static int add(int a,int b ,int c){
		return a + b + c ;
	}
	//重载add
	public static double add(int a,float b){
		return a + b ;
	}
	//重载add
	public static float add(float a,int b){
		return a + b ;
	}

	//重载,可变参数
	public static void out(int... xyz){
		for(int i = 0 ; i < xyz.length ; i ++){
			System.out.println(xyz[i]);
		}
	}
}


class ArrayDemo1 {
	public static void main(String[] args){
		//1.创建数组方式一
		int[] arr = new int[3];
		arr[0] = 1 ;
		arr[1] = 2 ;
		arr[2] = 3 ;
		
		//2.创建数组方式二
		int[] arr2 = new int[]{4,5,6};

		//3.创建数组方式三
		int[] arr3 ={7,8,9};
		int arr4[] = {10,11,12};
		
		//空指针异常
		int[] arr5 = null ;
		//System.out.println(arr5[2]);

		int[] arr6 = {-1,-2,-3,-4,-5,-6};
		int[] arr7 = null ;
		System.out.println(getMax(arr7));

		int x = 1 ; 
		int y = 2 ;
		int temp = 0 ;
		temp = x ;
		x = y ;
		y = temp ;
		System.out.println("x=" + x);
		System.out.println("y=" + y);
	}
	//提取数组中最大值
	public static int getMax(int[] arr){
		//数组有效性判断
		if(arr != null && arr.length != 0){
			int temp = arr[0] ;
			for(int i = 0 ; i < arr.length ; i ++){
				if(temp < arr[i]){
					temp = arr[i] ;
				}
			}
			return temp ;
		}
		System.out.println("数组无效");
		return -1 ;
	}
}


class BubbleDemo1 {
	public static void main(String[] args) 	{
		int[] arr = {9,8,7,5,6} ;
		bubble(arr);
		for(int i = 0 ; i < arr.length ; i ++){
			System.out.print(arr[i] + " ");
		}
	}

	//冒泡排序
	public static void bubble(int[] arr){
		//循环轮次
		int tmp = 0 ;
		for(int i = 0 ; i < arr.length - 1 ; i ++){
			for(int j = 0 ; j <  arr.length - 1 - i ; j ++){
				tmp = arr[j];
				if(arr[j] > arr[j + 1]){
					arr[j] = arr[j + 1];
					arr[j + 1] = tmp ;
				}
			}
		}
	}
}



/*OOP
------------
	object oriented program
	面向对象编程。
	class
	基于过程的。

OOP特征
-------------
	封装
	继承
	多态
*/
class ClassDemo1{
	public static void main(String[] args){
		Person p = new Person();//创建对象
		p.name = "坏人" ;
		p.age = 28 ;
		System.out.println(p); //打印出 类名、@ 、内存地址
		System.out.println(p.name);
		System.out.println(p.age);
		System.out.println("********************");
		int r = p.run();
		System.out.println(r);
	}
}

class Person{
	//类成员变量
	String name = "好人";
	int age = 10 ;

	//成员函数
	void walk(){
		System.out.println("walk...")
	}

	int run(){
		int temp  ;
		System.out.println("run... : " + temp);
		return age ;
	}
}


class ClassDemo2 {
	public static void main(String[] args) 	{
		Dog d1 = new Dog();
		Dog d2 = new Dog();
		d1.name = "旺财" ;
		d2.name = "小白" ;
		d1.color = "white" ;
		
		System.out.println(d1.name);
		System.out.println(d2.name);

		System.out.println("匿名对象1.-------------");
		//匿名对象
		new Dog().catchMouse();
		System.out.println("匿名对象2.-------------");
		//
		Dog d3 = buyDog(d1);
		System.out.println(d3.name);
	}
	
	//
	public static Dog buyDog(Dog d){
		System.out.println("开始买狗....");
		d.name = "二黄" ;
		System.out.println("毛色 : " + d.color + " , name : " + d.name);
		System.out.println("买卖结束!");
		return d ;
	} 
}

class Dog{
	private String name ;
	String color ; 
	void watch(){
		System.out.println("有人来了，汪汪...");
	}

	void catchMouse(){
		System.out.println("抓了只老鼠, :))");
	}
}


class ClassDemo3 {
	public static void main(String[] args) 	{
		Man man = new Man();
		man.addMoney(-200);
		man.huabei(3);
		System.out.println(man.getMoney());

	}
}

class Man{
	private int money = 1000;

	public int getMoney(){
		return money ;
	}

	public void addMoney(int amount){
		int temp = 100 ;
		if(amount > 0){
			money = money + amount ;
		}
	}
	//
	private void huabei(int amount){
		if(amount < 5 && amount <= money){
			money = money - amount ;
		}
	}
}



class HeapStackDemo1{
	public static void main(String[] args) {
		//testStack(1);
		testHeap();
	}

	//测试栈空间
	public static void testStack(int i){
		if(i >= 6595){
			return ;
		}
		System.out.println(i + " : hello world");
		int j = i + 1;
		testStack(j);
	}
	//java -Xmx500m -Xms500m 
	public static void testHeap(){
		int size = 1024 * 1024 * 1024 ;
		byte[][] arr = new byte[4][size];
		//arr[0][] = new byte[size];
		//arr[1] = new byte[size];
		//arr[2] = new byte[size];
		//arr[3] = new byte[size];

		System.out.println(arr);

	}
}


class ConstructorDemo1{
	public static void main(String[] args) {
		Dog d = new Dog("小白","yellow");
		d.watch();
		new Dog();
	}
}

class Dog{

	static{
		System.out.println("静态代码块-1");
	}

	static{
		System.out.println("静态代码块-2");
	}


	//
	public String name = "无名" ;

	static{
		
	}
	//构造代码块
	{
		name = "小强" ;
		//System.out.println("111" + name);
	}

	//构造代码块
	{
		name = "旺财" ;
	}

	private String color ;

	public Dog(){

	}

	//public Dog Dog(){
	//	return null ;
	//}

	public Dog(String n){
		new Dog();
		name = n ;
		System.out.println("1");
	}

	public Dog(String c,String n){
		//name = n ;
		//color = c ;
		System.out.println("2 " + name);
	}
	
	//
	public void watch(){
		System.out.println("汪汪~~~~");	
	}
}


class ConstructorDemo2 {
	public static void main(String[] args) 	{
		Dog d = new Dog("大黄","yellow");
		System.out.println(d.name + "," + d.color);

	}
}

class Dog{
	public String name ;
	public String color ;

	public Dog(String name){
		this.name = name;
	}

	public Dog(String name,String color){
		//this(name);
		this.color = color;
		this.name = name;
	}
}


class ThisDemo1 {
	public static void main(String[] args) {
		Dog d = new Dog();
		String name = "大黄" ;
		//d.setName(name);
		name = "大绿" ;
		System.out.println(name);
	}
}

class Dog{
	private String name ;
	private String color ;
	public String getName(){
		return this.name ;
	}
	public void setName(String name){
		 name = "大绿" ;
	}
}


class StaticDemo1{
	public static void main(String[] args) {
		//Benz b1 = new Benz();
		System.out.println(Benz.getBrand());
	}
}

class Benz{
	//static,
	private static String brand = "BENZ" ;
	private String color ;

	public static String getBrand(){
		//System.out.println(this.color);
		return brand ;
	}

	public void setColor(String color){
		this.color = color ;
	}


class ExtendsDemo1 {
	public static void main(String[] args) {
		Jing8 jing8 = new Jing8();
		jing8.name = "大黄" ;
		jing8.watch();
		jing8.owner = "老张" ;
	}
}
class Animal{
	public String name ;
	public int weight ;
	public void move(){
		System.out.println("move~~~~");
	}
}

class Dog extends Animal{
	public void watch(){
		System.out.println("有人来了~~~");
	}
}
class Jing8 extends Dog{
	public String owner ;
}
