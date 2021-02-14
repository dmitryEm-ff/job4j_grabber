import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class TestFailTest {

    @Test
    public void result() {
        assertThat(true, is(true));
    }
}