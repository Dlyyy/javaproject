class ExceptionDemo4 {
	public static void main(String[] args){
		try{
			Person p = new Person(30);
			p.setAge(20000);
		}
		catch(Exception e){
			System.out.println("出错了!!");
		}
		System.out.println("over");
	}
}
class Person{
	private int age ;
	//1.构造函数可以使用抛出异常声明
	//2.RuntimeException也可以在方法中进行抛出声明(可抛可不抛)
	public Person(int age) throws AgeInvalidException  {
		if(age < 0 || age > 200){
			throw new AgeInvalidException("年龄非法");
		}
		this.age = age ;
	}
	public int getAge(){
		return age ;
	}
	//使用异常
	public void setAge(int age) throws AgeTooBigException,
									AgeTooSmallException,
									AgeInvalidException{
		if(age > 200){
			throw new AgeTooBigException(/*"太大"*/) ;
		}
		else if( age == 0){
			throw new AgeTooSmallException();
		}
		else if(age < 0){
			throw new AgeInvalidException("年龄非法");
		}
		this.age = age ;
	}
}
//年龄非法异常,运行时异常
class AgeInvalidException extends Exception{
	private String info ;
	public AgeInvalidException(String info){
		this.info = info ;
	}
	public void printError(){
		System.out.println(info);
	}
}
//年龄太大异常
class AgeTooBigException extends AgeInvalidException{
	public AgeTooBigException(String info){
		super(info);
	}
	public AgeTooBigException(){
		this("年龄太大");
	}
}
//年龄太小异常
class AgeTooSmallException extends AgeInvalidException{
	public AgeTooSmallException(String info){
		super(info);
	}
	public AgeTooSmallException(){
		this("年龄太小");
	}
}
