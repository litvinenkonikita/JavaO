package JavaO;

/**
 *
 * @author nikita
 */
public class Syntax {
    
    static final int StProcABS = 1,
                    StProcMAX = 2,
                    StProcMIN = 3,
                    StProcDEC = 4,
                    StProcODD = 5,
                    StProcHalt = 6,
                    StProcINC = 7,
                    StProcInOpen = 8,
                    StProcInInt = 9,
                    StProcOutInt = 10,
                    StProcOutLn = 11;
    
    static void checkLex(int Lex, String Message){
        if(Lexer.CurrentLex == Lex){
            ErrorMessage.Error(Message);
        }
        else{
            Lexer.NextLex();
        }
    }
    
    static int ConstExpr(){
        int val = 0;
        //TableItem X;
        int Op = Lexer.LexPlus;
        
        
        return val;
    }
    
}
