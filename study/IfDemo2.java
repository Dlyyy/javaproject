class IfDemo2{
	public static void main(String[] args){
		int a = 1 ;
		int b = 2 ;
		int c = 3 ;
		int max ;
		//a 是否大于b ?
		if(a > b){
			if(a > c){
				max = a ;
			}
			else{
				max = c ;
			}
		}
		//a <= b
		else{
			if(b > c){
				max = b ;
			}
			else{
				max = c ;
			}
		}
		System.out.println(max);

		//三元运算符 a ? b : c ;
		System.out.println("max is " + (a > b ? a : b));
		//
		System.out.println("max is " + (a > b ? (a > c ? a : c) : (b > c ? b : c)));
		//
		System.out.println((a > b ? a : b) > c ? (a > b ? a : b)  : c);
		//
		int a = 1, b = 2 ,c = 3 , d = 4 ;
	}
}
