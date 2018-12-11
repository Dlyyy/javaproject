class TypeConvert{
	public static void main(String[] args){
		byte b = 100 ;
		int i = b ;		//自动转换(隐式转换)
		short s = 100 ;		//短整型
		byte b2 = (byte)i ;	//强制转换(显式转换)

		char c = 99;		//ascii
		System.out.println(c);
		System.out.println('a');
		System.out.println('a' + 1);

		int ii = 128 ;
		byte b0 = (byte)ii ;
		System.out.println(b0);
	}
}