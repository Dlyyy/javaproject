class MultiStateDemo2{
	public static void main(String[] args) 	{
		WomanStar fbb = new WomanStar();
		White w = fbb ;
		Rich r = fbb ;
		
		Beautiful b = (Beautiful)w ;
		b.beau();//�õ�����Ư����

		//Dog d = (Dog)b ;
		WRB wrb = (WRB)b ;

		White www = wrb ;


	}
}

interface White{
	public void veryWhite();
}
interface Rich{
	public void hasMoney();
 interface Beautiful{
	public void beau();
}
//�ӿڼ̳�
interface WRB extends White,Rich,Beautiful{
}
//Ů����
class WomanStar implements WRB{
	public void veryWhite(){
		System.out.println("�ܰ�~~");
	}
	public void hasMoney(){
		System.out.println("����Ǯ");
	}
	public void beau(){
		System.out.println("��Ư��");
	}
}

class Dog{
}