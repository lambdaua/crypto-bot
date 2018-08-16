import org.junit.Test;

/**
 * @author by AlexBlokh, 12/7/17 (aleksandrblokh@gmail.com)
 */
public class ApiApplicationTest {

    @Test
    public void name() throws Exception {
        String[] split = "".split("\\.");

        for (String s : split) {
            System.out.println("string is->" + s);
        }
    }
}