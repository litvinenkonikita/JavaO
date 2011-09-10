package JavaO;

/**
 *
 * @author nikita
 */

import JavaO.Tables.TableItem;
import JavaO.Tables.Table;

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
    // Check lex
    static void checkLex(int Lex, String Message){
        if(Lexer.CurrentLex == Lex){
            ErrorMessage.Error(Message);
        }
        else{
            Lexer.NextLex();
        }
    }
    
    //  [+ | =] (Num | Name)
    static int ConstExpr(){
        int val = 0;
        TableItem X;
        int Op = Lexer.LexPlus;
        
        if(Lexer.CurrentLex == Lexer.LexPlus ||
           Lexer.CurrentLex == Lexer.LexMinus){
           
            Op = Lexer.CurrentLex;
            Lexer.NextLex();
        }
        
        if(Lexer.CurrentLex == Lexer.LexNum){
            val = Lexer.CurrentNum;
            Lexer.NextLex();
        }
        else if(Lexer.CurrentLex == Lexer.LexName){
            X = Table.findName(Lexer.CurrentName);
            if(X.Category == Table.CategoryGuard){
                ErrorMessage.Error("Constant self-definition.");
            }
            else if(X.Category != Table.CategoryConst){
                ErrorMessage.Expected("constant name.");
            }
            else{
                val = X.Val;
                Lexer.NextLex();
            }
        }
        else{
            ErrorMessage.Expected("constant expression.");
        }
        if(Op == Lexer.LexMinus){
            return -val;
        }
        return val;
    }
    
    // Name "=" ConstExpr;
    static void ConstDecl(){
        TableItem ConstItem;
        ConstItem = Table.NewName(Lexer.CurrentName, Table.CategoryGuard);
        Lexer.NextLex();
        checkLex(Lexer.LexEq, "\"=\"");
        ConstItem.Category = Table.CategoryConst;
        ConstItem.Type = Table.TypeInt;
        ConstItem.Val = ConstExpr();
    }
    
    //  Parse type name
    static void Type(){
        TableItem TypeItem;
        if(Lexer.CurrentLex != Lexer.LexName){
            ErrorMessage.Expected("type name.");
        }
        else{
            TypeItem = Table.findName(Lexer.CurrentName);
            if(TypeItem.Category != Table.CategoryType){
                ErrorMessage.Expected("type name.");
            }
            else if(TypeItem.Type != Table.TypeInt){
                ErrorMessage.Expected("integer type.");
            }
            Lexer.NextLex();
        }
    }
    
    // Name {"," Name} ":" Type;
    static void VarDecl(){
        TableItem VarItem;
        
    }
    
}
