package JavaO;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
//import static org.junit.Assert.*;

/**
 *
 * @author nikita
 */
public class ScanTest {
    
    public ScanTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Text.reset();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of Number method, of class Scan.
     */
    /*@Test
    public void testNumber() {
        System.out.println("Number");
        Scan.Number();
        System.out.println(Scan.CurrentNum);
        //System.out.println(Scan.CurrentLex);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }*/

    /**
     * Test of Comment method, of class Scan.
     */
    /*@Test
    public void testComment() {
        System.out.println("Comment");
        Scan.Comment();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }*/
    
    /*@Test
    public void testIdent() {
        System.out.println("Ident");
        Scan.Ident();
        System.out.println(Scan.CurrentName);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }*/

    /**
     * Test of NextLex method, of class Scan.
     */
    /*@Test
    public void testNextLex() {
        System.out.println("NextLex");
        Scan.NextLex();
        System.out.println(Scan.CurrentLex);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }*/

    /**
     * Test of init method, of class Scan.
     */
    /*@Test
    public void testInit() {
        System.out.println("init");
        Lexer.init();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }*/
}
