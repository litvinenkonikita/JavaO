package JavaO;

/**
 *
 * @author Nikita
 */

import JavaO.Tables.TableItem;
import JavaO.Tables.Table;
        
public class CodeGen {

    static int PC;
    private static int Memory[];
    
    static int[] getMemory(){
        return Memory;
    }
    
    static void init(){
        PC = 0;
        Memory = new int[VM.MemorySize];
    }
    
    static void Command(int Command){
        Memory[PC++] = Command;
    }
    
    static void fixup(int A){
        while(A > 0){
            int Temp = Memory[A-2];
            Memory[A-2] = PC;
            A = Temp;
        }
    }
    
    static void Abs(){
        Command(VM.CommandDup);
        Command(0);
        Command(PC+3);  //  Адрес перехода вперёд (?)
        Command(VM.CommandIfGreaterEq);
        Command(VM.CommandNeg);
    }
    
    static void Min(){
        Command(Integer.MAX_VALUE);
        Command(VM.CommandNeg);
        Command(1);
        Command(VM.CommandSub);
    }
    
    static void Odd(){
        Command(2);
        Command(VM.CommandMod);
        Command(0);
        Command(0); //  Адрес перехода вперёд
        Command(VM.CommandIfEq);
    }
    
    static void Const(int Const){
        Command(Math.abs(Const));
        if(Const < 0){
            Command(VM.CommandNeg);
        }
    }
    
    static void Compare(int Lex){// "Comparing"
        Command(0);
        switch(Lex){
            case Lexer.LexEq :
                Command(VM.CommandIfNotEq);
                break;
            case Lexer.LexNotEq :
                Command(VM.CommandIfEq);
                break;
            case Lexer.LexLessEq :
                Command(VM.CommandIfGreaterThan);
                break;
            case Lexer.LexLessThan :
                Command(VM.CommandIfGreaterEq);
                break;
            case Lexer.LexGreaterEq :
                Command(VM.CommandIfLessThan);
                break;
            case Lexer.LexGreaterThan :
                Command(VM.CommandIfLessEq);
                break;
        }
    }
    
    static void Address(TableItem Item){
        Command(Item.Val);
        Item.Val = PC+1;
    }
    
    static void allocateVariables(){
        int i = 0;
        TableItem Item = Table.FirstVar();
        
        // НАОБОРОТ!!! ?
//        for(Iterator<TableItem> Iter = Table.NamesTable.iterator(); Iter.hasNext(); i++){
//            Item = Iter.next();
//            if(Item.Val == 0){
//                ErrorMessage.Warning("Varible " + Item.Name + "not used.");
//            }
//            else{
//                fixup(Item.Val);
//                PC++;
//            }
//        }
        
        while(Item != null){
            if(Item.Val == 0){
                ErrorMessage.Warning("Varible " + Item.Name + " not used.");
            }
            else{
                fixup(Item.Val);
                PC++;
            }
            Item = Table.NextVar();
        }
    }
    
}
