package JavaO.Tables;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nikita
 */
public class TableTest {
    
    @BeforeClass
    public static void setUp() throws Exception{
        //Table.enterName("test", Table.CategoryVar, Table.TypeInt, 1);
    }
    
    @AfterClass
    public static void tearDown() throws Exception {
        //Table.NamesTable.clear();
    }
    
    
    /*@Test
    public void testEnterName(){
        Table.enterName("test", Table.CategoryVar, Table.TypeInt, 1);
        //assertEquals(Table.NamesTable.size(), 1);
        //assertEquals(Table.Top, 0);
    }*/
    
    @Test
    public void testOpenScope(){
        Table.openScope();
        //assertEquals(Table.NamesTable.size(), 2);
        assertEquals(Table.Top, 0);
        assertEquals(Table.Bottom, 0);
    }
    
    @Test
    public void testEnterName(){
        Table.enterName("test", Table.CategoryVar, Table.TypeInt, 1);
        //assertEquals(Table.NamesTable.size(), 1);
        assertEquals(Table.Top, 1);
        assertEquals(Table.Bottom, 0);
    }
    
    @Test
    public void testCloseScope(){
        Table.closeScope();
        //assertEquals(Table.NamesTable.size(), 1);
        assertEquals(Table.Top, -1);
        assertEquals(Table.Bottom, -1);
    }
    
    @Test
    public void testNewName(){
        Table.openScope();
        TableItem Item = Table.NewName("test1", Table.CategoryVar);
        //assertEquals(Table.NamesTable.size(), 3);
        assertEquals(Table.Top, 1);
        assertEquals(Table.Bottom, 0);
    }
    
    @Test
    public void testFindName(){
        Table.enterName("test2", Table.CategoryVar, Table.TypeInt, 1);
        Table.enterName("test3", Table.CategoryVar, Table.TypeInt, 1);
        TableItem Item = Table.findName("test2");
        //assertEquals("test2", Item.Name);
        assertEquals(Table.Top, 3);
        assertEquals(Table.Bottom, 0);
    }
    
    /*@Test
    public void testFirstVar(){
        TableItem Item = Table.FirstVar();
        assertEquals("test3", Item.Name);
    }
    
    @Test
    public void testNextvar(){
        TableItem Item = Table.NextVar();
        assertEquals("test2", Item.Name);
        Item = Table.NextVar();
        assertEquals("test1", Item.Name);
    }*/

}
