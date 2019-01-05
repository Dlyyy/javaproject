import org.junit.Test;

public class TestDemo {
    @Test
    public void testAdd() {
        assertEquals(2, new UserDao().add(1, 1));
    }
}
