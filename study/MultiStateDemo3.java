class MultiStateDemo3 {
	public static void main(String[] args) {
		Jing8 jing8 = new Jing8();
		jing8.cry();
		Dog dog = jing8 ;
		dog.cry();
		System.out.println("=============");
		System.out.println("jing8.name" + jing8.name);
		System.out.println("dog.name" + dog.name);
	}
}
abstract class Animal{
	abstract public void cry();
}
class Dog extends Animal{
	private String name = "大黄";
	public void cry(){
		System.out.println("汪汪~~");
	}
}
class Jing8 extends Dog{
	public String name = "大绿" ;
	public void cry(){
		System.out.println("嘿嘿~~" + super.name);//方法可以覆盖，字段不能，因为字段有数据
	}
}
/**
 *
 * class Dog{
     public String name = "大黄";
	 public String getName(){
		return name ;
	 }

	 public void setName(String name){
		this.name = name ;
	 }
   }

   class Jing8 extends Dog{
	public String name = "大绿";
	public String getName(){
		return super.name ;
	}
   }

   main(){
	Jing8 jing8 = new Jing8();
	jing8.getName();
   }
 */