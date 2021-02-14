import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.*;

class TestTest extends TestCase {

    @org.junit.jupiter.api.Test
    void result() {
        assertThat(true, is(true));
    }
}