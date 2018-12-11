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
