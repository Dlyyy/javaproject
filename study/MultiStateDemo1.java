/**
 * ��̬
 */
class MultiStateDemo1 {
	public static void main(String[] args) 	{
		Jing8 jing8 = new Jing8(); //Jing8 jing8 ��������+��������
		jing8.meng();
		//��̬
		Dog dog = jing8 ;
		dog.watch();
		//��̬
		Animal a = dog ;
		a.cry();
		//��̬
		Pet pet = (Pet)a ;
		
		//
		Animal	aa = (Animal)pet ;
		Dog		dd = (Dog)pet ;
		Jing8	cc = (Jing8)pet ;

		Animal aaa = jing8 ;
		Dog bbb = jing8 ;
		Jing8 ccc  = jing8 ;
		Pet ddd = jing8 ;

	}
}
abstract class Animal{
	public void cry(){
		System.out.println("����~~~");
	}
}
class Dog extends Animal{
	public void watch(){
		System.out.println("������~~~");
	}
}
class Jing8 extends Dog implements Pet{
	public void meng(){
		System.out.println("!@!@!@");
	}
}
interface Pet{
	void meng();
}