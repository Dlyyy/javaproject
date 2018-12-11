class IfDemo3{
	public static void main(String[] args){
		int a = 1 , b = 5 , c = 7 , d = 4 ;
		//a > b ?
		int max ;
		String maxVar ;
		if(a > b){
			//a > c ?
			if(a > c){
				//a > d ?
				if(a > d){
					max = a ;
				}
				//a <= d
				else{
					max = d ;
				}
			}
			//a <= c
			else{
				//c > d?
				if(c > d){
					max = c ;
				}
				else{
					max = d ;
				}
			}
		}
		//a <= b
		else{
			//b > c ?
			if(b > c){
				//b > d ?
				if(b > d){
					max = b ;
				}
				else{
					max = d ;
				}
			}
			//b <= c
			else{
				//c > d ?
				if(c > d){
					max = c ;
				}
				else{
					max = d ;
				}
			}
		}
		System.out.println(max);
	}
}