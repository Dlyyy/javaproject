class EmptyTriangle{
	public static void main(String[] arg){
		int line = 20 ;
		//ѭ��������
		for(int i = 0 ; i < line ; i ++){
			//1.���n���ո�
			for(int j = 0 ; j < line - 1 - i ; j ++){
				System.out.print(" ");
			}
			//2.���һ��*
			System.out.print("*");
			//3.�������һ��,���2n��*
			if(i == (line - 1)){
				for(int j = 0 ; j < 2 * i ; j ++){
					System.out.print("*") ;
				}
			}
			//�м���
			if(i != 0 && i != (line - 1)){
				//���2n - 1���ո�
				for(int j = 0 ; j < 2 * i - 1 ; j ++){
					System.out.print(" ");
				}
				System.out.print("*");//����*
			}
			System.out.print("\r\n");
		}
	}
}