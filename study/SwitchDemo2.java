//switch 多case处理.
class SwitchDemo2 {
	public static void main(String[] args){
		int month = 130 ;
		switch(month){
			case 2:
			case 3:
			case 4:
				System.out.println("春天") ;
				break ;
			case 5:
			case 6:
			case 7:
				System.out.println("夏天") ;
				break ;
			case 8:
			case 9:
			case 10:
				System.out.println("秋天") ;
				break ;
			case 11:
			case 12:
			case 1:
				System.out.println("冬天") ;
				break ;
			default:
				System.out.println("非法月份") ;
				break ;
		}
	}
}
