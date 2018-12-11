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
