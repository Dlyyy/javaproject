class OperatorDemo{
	public static void main(String[] args){
		int a = +100 ;		//����
		int b = -100 ;		//����
		b = -0 ;
		System.out.println(a);
		System.out.println(b);
		System.out.println(1 + 2);				//+
		System.out.println(1 - 2 );				//-
		System.out.println(1 * 2);				//*
		System.out.println((float)b / 2);		// /
		// % :ȡģ===������
		System.out.println(-3 % 5);				//3

		int c = 1 ;
		int d = ++c ;							//c++ : c = c + 1
		System.out.println("d = " + d);			// /
		
		// + ���ַ������ӷ�
		String s1= "Hello " ;
		String s2 = "word" ;
		System.out.println(s1 + s2 + 3000);		//

		System.out.println(10.01/ 3 * 3);		//

		c = 100 ;
		c += 20 ;					//c = c + 20 ;
		System.out.println(c);		//

		short s4 = 4 ;
		//s4 = s4 + 5 ;
		System.out.println(s4+=5);		//

		//�Ƚ�����
		boolean bool = 2 >= 2 ;
		System.out.println(bool);			//
		System.out.println(1 <= 2 );		//
		System.out.println(1 != 2 );		//!= : ����
		System.out.println(1 != 2 );		//!= : ����
		
		//�߼������
		//&
		byte b1 = 10 ;
		byte b2 = 11 ; 
		//byte b3 = b1 & b2 ;
		System.out.println(b1 & b2);		// ������������ ֻ��һ����0����0������λ.
		System.out.println(b1 | b2);		// �����ƻ�����,ֻ��һ����1����1������λ 
		System.out.println(b1 ^ b2);		// ������������ 
		
		//
		System.out.println("r = " + (true & false));
		System.out.println(-2 | 3);

		//
		byte b5 =127 ;
		System.out.println((byte)(b5 << 1));
		System.out.println(-1 >>> 1);		
		System.out.println(-1 >> 1);		//�з�������
		System.out.println(~ -1);			//~ ��λȡ����
		System.out.println(3 >> 1);			//~ ��λȡ����


	}
}