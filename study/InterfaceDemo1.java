class InterfaceDemo1 {
	public static void main(String[] args) 	{
		PC pc = new PC();
		Mouse m = new Mouse();
		pc.insertUSB(m);
	}
}
class PC{
	public void insertUSB(USB usb){           //usb���βΣ���֪���������
		System.out.println("������usb�豸");
		usb.play();
	}
}
//����ӿڣ��ӿ��ж����˳��󷽷���Ҫ�ھ�������ʵ��
interface USB{
	void play() ;
}
//��ʵ���˽ӿ�
class Mouse implements USB{
	public void play(){
		System.out.println("��껬��");
	}
}


Computer
-------------
	