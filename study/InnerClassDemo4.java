/**
 * �ⲿ�������������ڲ��࣬ʹ��static�����ڲ��࣬ͨ��Outer.InnerClass
 */
class InnerClassDemo4 {
	public static void main(String[] args) 	{
		//
		Benz b = new Benz();
		new Benz.Engine().fire();
	}
}

class Benz{
	static class Engine{
		public void fire(){
			System.out.println("���");
		}
	}
}
