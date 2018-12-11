class ExceptionDemo2{
	public static void main(String[] args) 	{
		Person p = new Person();
		try{
			p.setAge(500);
		}
		catch(AgeTooBigException e){
			e.printlnError();
		}
		catch(Throwable e){
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
//
class Person{
	private int age ;
	public int getAge(){
		return age ;
	}
	//设置,声明抛出异常的方法
	public void setAge(int age) throws AgeTooBigException{
		if(age > 200){
			//抛出异常
			throw new AgeTooBigException();
		}
		this.age = age ;
	}
}
//定义异常
class AgeTooBigException extends Exception{
	private String info ;
	public AgeTooBigException(String info){
		this.info = info ;
	}
	public AgeTooBigException() {
		this("年龄太大！！");
	}

	public void printlnError(){
		System.out.println(info);
	}
}

/**
	1.定义异常
	  class xxx extends Exception{...}

	2.使用异常
	  throw new MyException();

	3.处理异常
	  a.try-catch-finally
	  b.继续抛出
 */