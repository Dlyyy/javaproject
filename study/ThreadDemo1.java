/**
 * �߳�
 */
class ThreadDemo1{
	public static void main(String[] args) 	{		
		MyThread t1 = new MyThread();
		YourThread t2 = new YourThread();
		//�����߳�
		t1.run();			//1���߳�.
		t2.start();
	}
}

class MyThread extends Thread{
	//ִ���̵߳�ʱ����е���,jvm�������
	public void run(){
		while(true){
			System.out.println("myThread");
			yield();		//����cpu����ռȨ����Ϊ����һ������ִ��һ��
		}
	}
}
//
class YourThread extends Thread{
	public void run(){
		while(true){
			System.out.println("yourThread");
			yield();
		}
	}
}
