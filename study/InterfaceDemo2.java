/**
 * �ӿ�
 */
class InterfaceDemo2 {
	public static void main(String[] args) 	{
		TuHao laowang = new TuHao();
		WomanStar fbb = new WomanStar();
		laowang.marry(fbb);
	}
}
class TuHao{
	public void marry(WRB w){
		w.veryWhite();
		w.hasMoney();
		w.beau();
		System.out.println(" ����~~~");
	}
}
interface White{
	public void veryWhite();
}
interface Rich{
	public void hasMoney();
}
interface Beautiful{
	public void beau();
}
//�ӿڼ̳�
interface WRB extends White,Rich,Beautiful{
}
//Ů����
class WomanStar implements White,Rich,Beautiful{
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

/**
 * ��ϰ��
 ������Anmial�ǳ����࣬Dog�̳�Animal��Jing8�̳�Dog
 ����ӿ�Pet,������meng()�ķ�����
 ����Person�࣬������buyPet(...)����,���Թ�����
 ��ʾ:class C extends A implements B,D,...{
 }
 */
