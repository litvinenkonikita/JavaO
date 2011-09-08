package JavaO;

/**
 *
 * @author Nikita
 */

import java.util.Iterator;

import JavaO.Tables.TableItem;
import JavaO.Tables.Table;
        
public class CodeGen {
    
    static int PC;
    
    static void init(){
        PC = 0;
    }
    
    static void genCommand(int Command){
        VM.Memory[PC++] = Command;
    }
    
    static void fixup(int A){
        while(A > 0){
            int Temp = VM.Memory[A-2];
            VM.Memory[A-2] = PC;
            A = Temp;
        }
    }
    
    static void Abs(){
        genCommand(VM.CommandDup);
        genCommand(0);
        genCommand(PC+3);  //  Адрес перехода вперёд (?)
        genCommand(VM.CommandIfGreaterEq);
        genCommand(VM.CommandNeg);
    }
    
    static void Min(){
        genCommand(Integer.MAX_VALUE);
        genCommand(VM.CommandNeg);
        genCommand(1);
        genCommand(VM.CommandSub);
    }
    
    static void Odd(){
        genCommand(2);
        genCommand(VM.CommandMod);
        genCommand(0);
        genCommand(0); //  Адрес перехода вперёд
        genCommand(VM.CommandIfEq);
    }
    
    static void Const(int Const){
        genCommand(Math.abs(Const));
        if(Const < 0){
            genCommand(VM.CommandNeg);
        }
    }
    
    static void Compare(int Lex){// "Comparing"
        genCommand(0);
        switch(Lex){
            case Lexer.LexEq :
                genCommand(VM.CommandIfNotEq);
                break;
            case Lexer.LexNotEq :
                genCommand(VM.CommandIfEq);
                break;
            case Lexer.LexLessEq :
                genCommand(VM.CommandIfGreaterThan);
                break;
            case Lexer.LexLessThan :
                genCommand(VM.CommandIfGreaterEq);
                break;
            case Lexer.LexGreaterEq :
                genCommand(VM.CommandIfLessThan);
                break;
            case Lexer.LexGreaterThan :
                genCommand(VM.CommandIfLessEq);
                break;
        }
    }
    
    static void Address(TableItem Item){
        genCommand(Item.Val);
        Item.Val = PC+1;
    }
    
    static void allocateVariables(){
        int i = 0;
        TableItem Item;
        
        // НАОБОРОТ!!! ?
        for(Iterator<TableItem> Iter = Table.NamesTable.iterator(); Iter.hasNext(); i++){
            Item = Iter.next();
            if(Item.Val == 0){
                ErrorMessage.Warning("Varible " + Item.Name + "not used.");
            }
            else{
                fixup(Item.Val);
                PC++;
            }
        }
        
        
//        while(){
//            if(CurrentVar.Val == 0){
//                ErrorMessage.Warning("Varible " + CurrentVar.Name + "not used.");
//            }
//            else{
//                fixup(CurrentVar.Val);
//                PC++;
//            }
//            CurrentVar = Table.NextVar();
//        }
    }
    
    
}
