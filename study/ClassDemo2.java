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
