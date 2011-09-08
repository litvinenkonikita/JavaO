package JavaO;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nikita
 */
public class VMTest {
    
    public VMTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testRun(){
        VM.Memory[0] = 0;
        VM.Memory[2] = 1;
        VM.Memory[3] = 2;
        VM.Memory[1] = VM.CommandStop;
        //System.out.println(VM.Memory.length);
        VM.run();
    }
    
}
