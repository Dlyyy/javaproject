class TypeConvert{
	public static void main(String[] args){
		byte b = 100 ;
		int i = b ;		//�Զ�ת��(��ʽת��)
		short s = 100 ;		//������
		byte b2 = (byte)i ;	//ǿ��ת��(��ʽת��)

		char c = 99;		//ascii
		System.out.println(c);
		System.out.println('a');
		System.out.println('a' + 1);

		int ii = 128 ;
		byte b0 = (byte)ii ;
		System.out.println(b0);
	}
}