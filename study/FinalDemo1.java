
/**
 * final�����࣬���ܱ��̳С�
 */
class FinalDemo1 {
	public static void main(String[] args) {
		Jing8 j = new Jing8();
		j.name = "������" ;
		j.watch();
		System.out.println(j.name);
	}
}

//
class Dog{
	//����
	public final String name = "ERHA" ; 
	
	//final���η�����������д��
	public /*final*/ void watch(){
		System.out.println("������~~~");
	}
}

class Jing8 extends Dog{
	public void watch(){
		System.out.println("��һ��~~~");
		super.watch();
	}
}
