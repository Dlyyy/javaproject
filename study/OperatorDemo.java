class OperatorDemo{
	public static void main(String[] args){
		int a = +100 ;		//整数
		int b = -100 ;		//负数
		b = -0 ;
		System.out.println(a);
		System.out.println(b);
		System.out.println(1 + 2);				//+
		System.out.println(1 - 2 );				//-
		System.out.println(1 * 2);				//*
		System.out.println((float)b / 2);		// /
		// % :取模===求余数
		System.out.println(-3 % 5);				//3

		int c = 1 ;
		int d = ++c ;							//c++ : c = c + 1
		System.out.println("d = " + d);			// /
		
		// + 是字符串连接符
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

		//比较运算
		boolean bool = 2 >= 2 ;
		System.out.println(bool);			//
		System.out.println(1 <= 2 );		//
		System.out.println(1 != 2 );		//!= : 等于
		System.out.println(1 != 2 );		//!= : 等于
		
		//逻辑运算符
		//&
		byte b1 = 10 ;
		byte b2 = 11 ; 
		//byte b3 = b1 & b2 ;
		System.out.println(b1 & b2);		// 二进制与运算 只有一个是0就是0，不进位.
		System.out.println(b1 | b2);		// 二进制或运算,只有一个是1就是1，不进位 
		System.out.println(b1 ^ b2);		// 二进制与运算 
		
		//
		System.out.println("r = " + (true & false));
		System.out.println(-2 | 3);

		//
		byte b5 =127 ;
		System.out.println((byte)(b5 << 1));
		System.out.println(-1 >>> 1);		
		System.out.println(-1 >> 1);		//有符号右移
		System.out.println(~ -1);			//~ 按位取反。
		System.out.println(3 >> 1);			//~ 按位取反。


	}
}