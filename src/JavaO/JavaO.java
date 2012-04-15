package JavaO;

import JavaO.Tables.Table;

/**
 *
 * @author Litvinenko Nikita
 */

public class JavaO {
    
    static void init() throws Exception {
        Text.reset();
        Table.init();
        Lexer.init();
        CodeGen.init();
    }
    
    public static void compile() throws Exception{
        init();
        Syntax.compile();
    }
}
