class InnerClassDemo {
	public static void main(String[] args) {
		Benz benz = new Benz();
		benz.run();
	}
}
class Benz{
	public String color = "��ɫ" ;
	public int tires ;

	public void run(){
		//�ֲ�����
		String key = "sss";
		new Engine().fire(key);
		System.out.println("����~~~");
	}
	//�ڲ���
	class Engine{
		public void fire(String key){
			System.out.println("����Կ�� : " + key);
			System.out.println("���");
		}
	}
}