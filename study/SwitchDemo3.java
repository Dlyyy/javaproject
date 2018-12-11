/**
 * if + switch结合使用.
 */
class SwitchDemo3{
	public static void main(String[] args) 	{
		//春: 2 3 4		--1
		//春: 567		--2
		//春: 8910		--3
		//春: 11 12 1	--4 
		//
		int month = 12 ;
		//
		int season = (month + 1) / 3 ;		//3
		//判断是否是非法月份
		if(month < 1 || month > 12){
			System.out.println("非法月份");
		}
		//合法月份
		else{
			switch(season){
				case 1:
					System.out.println("春天");
					break ;
				case 2:
					System.out.println("夏天");
					break ;
				case 3:
					System.out.println("秋天");
					break ;
				case 0 : 
				case 4 : 
					System.out.println("冬天");
					break ;
			}
			
		}
	}
}
