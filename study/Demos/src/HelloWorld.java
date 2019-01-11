
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        long val = 26L; //以L结尾的字面值表示long型
        int decVal = 26; //默认就是int型
        int hexVal = 0x1a; //16进制
        int oxVal = 032; //8进制
        int binVal = 0b11010; //2进制
        System.out.println(oxVal);

        float f1 = 123.4F;// 以F结尾的字面值表示float类型
        double d1 = 123.4;// 默认就是double类型
        double d2 = 1.234e2;// 科学计数法表示double，e2表示10的二次方

        String name = "盖伦";
        char a= 'c';
        //以下是转义字符
        char tab = '\t'; //制表符
        char carriageReturn = '\r'; //回车
        char newLine = '\n'; //换行
        char doubleQuote = '\"'; //双引号
        char singleQuote = '\''; //单引号
        char backslash = '\\'; //反斜杠

        //数据类型转换  从小到大自动转，从大到小强制转 需要注意的一点是
        //虽然short和char都是16位的，长度是一样的
        //但是彼此之间，依然需要进行强制转换
        char c = 'A';
        short s = 80;
        //虽然short和char都是16位的，长度是一样的
        //但是彼此之间，依然需要进行强制转换
        c = (char) s;
        //直接进行转换，会出现编译错误

        //低精度向高精度转换
        long l = 50;
        int i = 50;
        //int比较小，要放进比较大的long,随便怎么样，都放的进去
        l = i;

        //高精度向低精度转换
        byte b = 5;
        int i1 = 10;
        int i2 = 300;

        b = (byte) i1;
        //因为i1的值是在byte范围之内，所以即便进行强制转换
        //最后得到的值，也是10
        System.out.println(b);

        //因为i2的值是在byte范围之外，所以就会按照byte的长度进行截取
        //i2的值是300，其对应的二进制数是 100101100
        //按照byte的长度8位进行截取后，其值为 00101100 即44
        b =(byte) i2;
        System.out.println(b);

        //查看一个整数对应的二进制的方法：
        System.out.println(Integer.toBinaryString(i2));
    }
}
