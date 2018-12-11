class ExceptionDemo4 {
	public static void main(String[] args){
		try{
			Person p = new Person(30);
			p.setAge(20000);
		}
		catch(Exception e){
			System.out.println("������!!");
		}
		System.out.println("over");
	}
}
class Person{
	private int age ;
	//1.���캯������ʹ���׳��쳣����
	//2.RuntimeExceptionҲ�����ڷ����н����׳�����(���׿ɲ���)
	public Person(int age) throws AgeInvalidException  {
		if(age < 0 || age > 200){
			throw new AgeInvalidException("����Ƿ�");
		}
		this.age = age ;
	}
	public int getAge(){
		return age ;
	}
	//ʹ���쳣
	public void setAge(int age) throws AgeTooBigException,
									AgeTooSmallException,
									AgeInvalidException{
		if(age > 200){
			throw new AgeTooBigException(/*"̫��"*/) ;
		}
		else if( age == 0){
			throw new AgeTooSmallException();
		}
		else if(age < 0){
			throw new AgeInvalidException("����Ƿ�");
		}
		this.age = age ;
	}
}
//����Ƿ��쳣,����ʱ�쳣
class AgeInvalidException extends Exception{
	private String info ;
	public AgeInvalidException(String info){
		this.info = info ;
	}
	public void printError(){
		System.out.println(info);
	}
}
//����̫���쳣
class AgeTooBigException extends AgeInvalidException{
	public AgeTooBigException(String info){
		super(info);
	}
	public AgeTooBigException(){
		this("����̫��");
	}
}
//����̫С�쳣
class AgeTooSmallException extends AgeInvalidException{
	public AgeTooSmallException(String info){
		super(info);
	}
	public AgeTooSmallException(){
		this("����̫С");
	}
}
