/**
 * �������
 */
class RunnableDemo2 {
	public static void main(String[] args)	{
		//�����ڲ������
		/*
		new Thread(new Runnable(){    //runnable�Ǹ��ӿ� ��Ҫ�����żӷ���ʵ��
			public void run(){
				System.out.println("hello world");
			}
		}).start();
		*/

		new Thread(){
			public void run(){
				print();
			}

			private void print(){
				System.out.println("hello world");
			}
		}.start();

		//MyThread d = new MyThread();
		//d.start();
	}
}

class MyThread extends Thread{
	public void run(){
		System.out.println("hello world");
	}
}