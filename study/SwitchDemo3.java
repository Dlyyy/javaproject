/**
 * if + switch���ʹ��.
 */
class SwitchDemo3{
	public static void main(String[] args) 	{
		//��: 2 3 4		--1
		//��: 567		--2
		//��: 8910		--3
		//��: 11 12 1	--4 
		//
		int month = 12 ;
		//
		int season = (month + 1) / 3 ;		//3
		//�ж��Ƿ��ǷǷ��·�
		if(month < 1 || month > 12){
			System.out.println("�Ƿ��·�");
		}
		//�Ϸ��·�
		else{
			switch(season){
				case 1:
					System.out.println("����");
					break ;
				case 2:
					System.out.println("����");
					break ;
				case 3:
					System.out.println("����");
					break ;
				case 0 : 
				case 4 : 
					System.out.println("����");
					break ;
			}
			
		}
	}
}
