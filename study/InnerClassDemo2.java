class InnerClassDemo2 {
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
		final String key = "sss";
		//�ڲ���
		class Engine{
			public void fire(){
				System.out.println("����Կ�� : " + key);
				System.out.println("���");
			}
		}
		new Engine().fire();
		System.out.println("����~~~");
		//key = "xxx" ;
	}
}