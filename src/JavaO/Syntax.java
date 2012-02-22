package JavaO;

/**
 *
 * @author nikita
 */

import JavaO.Tables.TableItem;
import JavaO.Tables.Table;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Vector;

public class Syntax {
    
    static final int StProcABS = 1,
                    StProcMAX = 2,
                    StProcMIN = 3,
                    StProcDEC = 4,
                    StProcODD = 5,
                    StProcHALT = 6,
                    StProcINC = 7,
                    StProcInOpen = 8,
                    StProcInInt = 9,
                    StProcOutInt = 10,
                    StProcOutLn = 11;
    
    private static HashMap<Integer, String> VariablesMap = new HashMap<Integer, String>();
    
    static HashMap<Integer, String> getVariables(){
        return VariablesMap;
    }
    
    
    static void addVariable(int Address, String VarName){
        VariablesMap.put(Address, VarName);
    }
    
    
    static int getByteCodeCount(){
        return CodeGen.getMemory().length;
    }
    
    static Vector getByteCode(){
        Vector ResultByteCodeVector = new Vector();
        Vector Row;
        Integer Command = new Integer(0);
        String CommandStr = new String();
        String CommandDesc = new String();
        int PC = 0;
        int Memory[] = CodeGen.getMemory();
        while((Command = Memory[PC++]) != VM.CommandStop){
            if(Command >= 0){
                CommandStr = Command.toString();
                if(VariablesMap.containsKey(PC)){
                    CommandDesc = "variable "+VariablesMap.get(PC);
                }
                else{
                    if(PC == getByteCodeCount()-1){
                        CommandDesc = "return code";
                    }
                    else{
                        CommandDesc = "const";
                    }
                }
            }
            else{
                CommandDesc = "command";
                CommandStr = VM.CommandsMap.get(Command);
            }
            Row = new Vector(Arrays.asList(PC-1, CommandStr, CommandDesc));
            ResultByteCodeVector.add(Row);
        }
        Row = new Vector(Arrays.asList(PC-1, "STOP", "command"));
        ResultByteCodeVector.add(Row);
        return ResultByteCodeVector;
    }
    
    
    // Check lex
    static void checkLex(int Lex, String Message) throws Exception {
        if(Lexer.CurrentLex != Lex){
            ErrorMessage.Expected(Message + " current = " + Lexer.CurrentLex + " Lex = " + Lex);
        }
        else{
            Lexer.NextLex();
        }
    }
    
    //  [+ | =] (Num | Name)
    static int ConstExpr() throws Exception {
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
                ErrorMessage.Expected("Constant name");
            }
            else{
                val = X.Val;
                Lexer.NextLex();
            }
        }
        else{
            ErrorMessage.Expected("Constant expression");
        }
        if(Op == Lexer.LexMinus){
            return -val;
        }
        return val;
    }
    
    // Name "=" ConstExpr;
    static void ConstDeclaration() throws Exception {
        TableItem ConstItem;
        ConstItem = Table.NewName(Lexer.CurrentName, Table.CategoryGuard);
        Lexer.NextLex();
        checkLex(Lexer.LexEq, "\"=\"");
        ConstItem.Category = Table.CategoryConst;
        ConstItem.Type = Table.TypeInt;
        ConstItem.Val = ConstExpr();
    }
    
    //  Parse type name
    static void Type() throws Exception {
        TableItem TypeItem;
        if(Lexer.CurrentLex != Lexer.LexName){
            ErrorMessage.Expected("Type name");
        }
        else{
            TypeItem = Table.findName(Lexer.CurrentName);
            if(TypeItem.Category != Table.CategoryType){
                ErrorMessage.Expected("Type name");
            }
            else if(TypeItem.Type != Table.TypeInt){
                ErrorMessage.Expected("Integer type");
            }
            Lexer.NextLex();
        }
    }
    
    // Name {"," Name} ":" Type;
    static void VarDeclaration() throws Exception {
        TableItem VarItem;
        if(Lexer.CurrentLex != Lexer.LexName){
            ErrorMessage.Expected("Name");
        }
        else {
            VarItem = Table.NewName(Lexer.CurrentName, Table.CategoryVar);
            VarItem.Type = Table.TypeInt;
            Lexer.NextLex();
        }
        
        while(Lexer.CurrentLex == Lexer.LexComma){
            Lexer.NextLex();
            if(Lexer.CurrentLex != Lexer.LexName){
                ErrorMessage.Expected("Name");
            }
            else{
                VarItem = Table.NewName(Lexer.CurrentName, Table.CategoryVar);
                VarItem.Type = Table.TypeInt;
                Lexer.NextLex();
            }
        }
        checkLex(Lexer.LexColon, "\";\"");
        Type();
    }
    
    
    //  { CONST { ConstDeclaration ";"} | VAR { VarDeclaration ";"} }
    static void DeclarationSequence() throws Exception {
        while(Lexer.CurrentLex == Lexer.LexConst || Lexer.CurrentLex == Lexer.LexVar){
            if(Lexer.CurrentLex == Lexer.LexConst){
                Lexer.NextLex();
                while(Lexer.CurrentLex == Lexer.LexName){
                    ConstDeclaration();
                    checkLex(Lexer.LexSemi, "\";\"");
                    
                }
            }
            else{
                Lexer.NextLex();
                while(Lexer.CurrentLex == Lexer.LexName){
                    VarDeclaration();
                    checkLex(Lexer.LexSemi, "\";\"");
                }
            }
        }
    }
    
    
    //  Integer expression (целочисленное выражение) 
    static void IntExpression() throws Exception {
        if(Expression() != Table.TypeInt){
            ErrorMessage.Expected("Int expression");
        }
    }
    
    // Стандартная функция
    static int StandardFunction(int Func) throws Exception {
        switch(Func){
            case StProcABS :
                IntExpression();
                CodeGen.Abs();
                return Table.TypeInt;
                
            case StProcMAX :
                Type();
                CodeGen.Command(Integer.MAX_VALUE);
                return Table.TypeInt;
                
            case StProcMIN :
                Type();
                CodeGen.Min();
                return Table.TypeInt;
                
            case StProcODD :
                IntExpression();
                CodeGen.Odd();
                return Table.TypeBool;
        }
        return Table.TypeNone;
    }
    
    // Множитель
    static int Factor() throws Exception {
        TableItem Item;
        int Type = 0;
        if(Lexer.CurrentLex == Lexer.LexName){
            Item = Table.findName(Lexer.CurrentName);
            if(Item.Category == Table.CategoryVar){
                CodeGen.Address(Item);
                addVariable(CodeGen.PC, Item.Name);
                CodeGen.Command(VM.CommandLoad);
                Lexer.NextLex();
                return Item.Type;
            }
            else if(Item.Category == Table.CategoryConst){
                CodeGen.Const(Item.Val);
                Lexer.NextLex();
                return Item.Type;
            }
            else if(Item.Category == Table.CategoryStProc && Item.Type != Table.TypeNone){
                Lexer.NextLex();
                checkLex(Lexer.LexLeftPar, "\"(\"");
                Type = StandardFunction(Item.Val);
                checkLex(Lexer.LexRightPar, "\")\"");
            }
            else{
                ErrorMessage.Expected("Var, const or standard function");
            }
        }
        else if(Lexer.CurrentLex == Lexer.LexNum){
            CodeGen.Const(Lexer.CurrentNum);
            Lexer.NextLex();
            return Table.TypeInt;
        }
        else if(Lexer.CurrentLex == Lexer.LexLeftPar){
            Lexer.NextLex();
            Type = Expression();
            checkLex(Lexer.LexRightPar, "\")\"");
        }
        else{
            ErrorMessage.Expected("Name, number or \"(\"");
        }
        return Type;
    }
    
    
    // Factor { operation Factor }
    static int Term() throws Exception {
        int Operation;
        int Type = Factor();
        if(Lexer.CurrentLex == Lexer.LexMult || 
                Lexer.CurrentLex == Lexer.LexDiv ||
                Lexer.CurrentLex == Lexer.LexMod){
            if(Type != Table.TypeInt){
                ErrorMessage.Error("invalid operation for non-int operand.");
            }
            
            do{
                Operation = Lexer.CurrentLex;
                Lexer.NextLex();
                if((Type = Factor()) != Table.TypeInt){
                    ErrorMessage.Expected("Int expression");
                }
                switch(Operation){
                    case Lexer.LexMult :
                        CodeGen.Command(VM.CommandMult);
                        break;
                    case Lexer.LexDiv :
                        CodeGen.Command(VM.CommandDiv);
                        break;
                    case Lexer.LexMod :
                        CodeGen.Command(VM.CommandMod);
                        break;
                }
                
            }
            while(Lexer.CurrentLex == Lexer.LexMult ||
                    Lexer.CurrentLex == Lexer.LexDiv ||
                    Lexer.CurrentLex == Lexer.LexMod);
        }
        return Type;
    }
    
    
    // [+|-] Term {[+|-] Term}
    static int SimpleExpression() throws Exception {
        int Type;
        int Operation;
        if(Lexer.CurrentLex == Lexer.LexPlus ||
                Lexer.CurrentLex == Lexer.LexMinus){
            
            Operation = Lexer.CurrentLex;
            Lexer.NextLex();
            if((Type = Term()) != Table.TypeInt){
                ErrorMessage.Expected("Int expression");
            }
            if(Operation == Lexer.LexMinus){
                CodeGen.Command(VM.CommandNeg);
            }
        }
        else{
            Type = Term();
        }
        if(Lexer.CurrentLex == Lexer.LexPlus ||
                Lexer.CurrentLex == Lexer.LexMinus){
            if(Type != Table.TypeInt){
                ErrorMessage.Error("invalid operation for non-int operand.");
            }
            do{
                Operation = Lexer.CurrentLex;
                Lexer.NextLex();
                if((Type = Term()) != Table.TypeInt){
                    ErrorMessage.Expected("Int expression");
                }
                switch(Operation){
                    case Lexer.LexPlus : 
                        CodeGen.Command(VM.CommandAdd);
                        break;
                    case Lexer.LexMinus :
                        CodeGen.Command(VM.CommandSub);
                        break;
                }
            }
            while(Lexer.CurrentLex == Lexer.LexPlus ||
                    Lexer.CurrentLex == Lexer.LexMinus);
            
        }
        
        return Type;
    }
    
    
    //  SimpleExpression [ > | >= | < | <= | = | #  SimpleExpression ]
    static int Expression() throws Exception {
        int Type = SimpleExpression();
        int Operation;
        if(Lexer.CurrentLex == Lexer.LexEq ||
                Lexer.CurrentLex == Lexer.LexNotEq ||
                Lexer.CurrentLex == Lexer.LexGreaterThan ||
                Lexer.CurrentLex == Lexer.LexGreaterEq ||
                Lexer.CurrentLex == Lexer.LexLessThan ||
                Lexer.CurrentLex == Lexer.LexLessEq){
            
            Operation = Lexer.CurrentLex;
            if(Type != Table.TypeInt){
                ErrorMessage.Error("invalid operation for non-int operand.");
            }
            Lexer.NextLex();
            if((Type = SimpleExpression()) != Table.TypeInt){
                ErrorMessage.Expected("Int expression");
            }
            CodeGen.Compare(Operation);
            Type = Table.TypeBool;
        }
        return Type;
    }
    
    
    // Name
    static void Variable() throws Exception {
        TableItem VarItem;
        if(Lexer.CurrentLex != Lexer.LexName){
            ErrorMessage.Expected("Name");
        }
        else{
            if((VarItem = Table.findName(Lexer.CurrentName)).Category != Table.CategoryVar){
                ErrorMessage.Expected("Variable name");
            }
            CodeGen.Address(VarItem);
            addVariable(CodeGen.PC, VarItem.Name);
            Lexer.NextLex();
        }
    }
    
    
    //
    static void StandardProcedure(int StProc) throws Exception {
        switch(StProc){
            case StProcDEC :
                Variable();
                CodeGen.Command(VM.CommandDup);
                CodeGen.Command(VM.CommandLoad);
                if(Lexer.CurrentLex == Lexer.LexComma){ //  ?
                    Lexer.NextLex();
                    IntExpression();
                }
                else{
                    CodeGen.Command(1);
                }
                CodeGen.Command(VM.CommandSub);
                CodeGen.Command(VM.CommandSave);
                return;
                
            case StProcINC :
                Variable();
                CodeGen.Command(VM.CommandDup);
                CodeGen.Command(VM.CommandLoad);
                if(Lexer.CurrentLex == Lexer.LexComma){
                    Lexer.NextLex();
                    IntExpression();
                }
                else{
                    CodeGen.Command(1);
                }
                CodeGen.Command(VM.CommandAdd);
                CodeGen.Command(VM.CommandSave);
                return;
                
            case StProcInOpen :
                return;
                
            case StProcInInt :
                Variable();
                CodeGen.Command(VM.CommandInput);
                CodeGen.Command(VM.CommandSave);
                return;
                
            case StProcOutInt :
                IntExpression();
                checkLex(Lexer.CurrentLex, "\",\"");
                IntExpression();
                CodeGen.Command(VM.CommandOutput);
                return;
                
            case StProcOutLn :
                CodeGen.Command(VM.CommandOutLn);
                return;
                
            case StProcHALT :
                CodeGen.Command(ConstExpr());
                CodeGen.Command(VM.CommandStop);
                return;
        }
    }
    
    
    //
    static void BoolExpression() throws Exception {
        if(Expression() != Table.TypeBool){
            ErrorMessage.Expected("Bool expression");
        }
    }
    
    
    //  Variable "=" IntExpression ";"
    static void AssignmentStatement() throws Exception {
        Variable();
        if(Lexer.CurrentLex == Lexer.LexAss){
            Lexer.NextLex();
            IntExpression();
            CodeGen.Command(VM.CommandSave);
        }
        else{
            ErrorMessage.Expected("\":=\"");
        }
    }
    
    
    //  Name ["(" [IntExpression | Variable] ")"]
    static void CallStatement(int StProc) throws Exception {
        checkLex(Lexer.LexName, "procedure name");
        if(Lexer.CurrentLex == Lexer.LexLeftPar){
            Lexer.NextLex();
            StandardProcedure(StProc);
            checkLex(Lexer.LexRightPar, "\")\"");
        }
        else if(StProc == StProcOutLn || StProc == StProcInOpen){
            StandardProcedure(StProc);
        }
        else{
            ErrorMessage.Expected("\"(\"");
        }
    }
    
    
    //  IF "(" BoolExpression ")" THEN StatementSequence 
    //  { ELSIF StatementSequence } [ ELSE StatementSequence ]
    //  END
    static void IfStatement() throws Exception {
        int ConditionPC;
        int LastGOTO = 0;
        checkLex(Lexer.LexIf, "IF");
        BoolExpression();
        ConditionPC = CodeGen.PC;
        checkLex(Lexer.LexThen, "THEN");
        StatementSequence();
        while(Lexer.CurrentLex == Lexer.LexElsif){
            CodeGen.Command(LastGOTO);
            CodeGen.Command(VM.CommandGoTo);
            LastGOTO = CodeGen.PC;
            Lexer.NextLex();
            CodeGen.fixup(ConditionPC);
            BoolExpression();
            ConditionPC = CodeGen.PC;
            checkLex(Lexer.LexThen, "THEN");
            StatementSequence();
        }
        if(Lexer.CurrentLex == Lexer.LexElse){
            CodeGen.Command(LastGOTO);
            CodeGen.Command(VM.CommandGoTo);
            LastGOTO = CodeGen.PC;
            Lexer.NextLex();
            CodeGen.fixup(ConditionPC);
            StatementSequence();
        }
        else{
            CodeGen.fixup(ConditionPC);
        }
        checkLex(Lexer.LexEnd, "END");
        CodeGen.fixup(LastGOTO);
    }
    
    // WHILE "(" BoolExpression ")" DO StatementSequence END
    static void WhileStatement() throws Exception {
        int WhilePC = CodeGen.PC;
        checkLex(Lexer.LexWhile, "WHILE");
        BoolExpression();
        int ConditionPC = CodeGen.PC;
        checkLex(Lexer.LexDo, "DO");
        StatementSequence();
        checkLex(Lexer.LexEnd, "END");
        CodeGen.Command(WhilePC);
        CodeGen.Command(VM.CommandGoTo);
        CodeGen.fixup(ConditionPC);
    }
    
    
    //  | IfStatement | WhileStatement
    static void Statement() throws Exception {
        TableItem Item;
        if(Lexer.CurrentLex == Lexer.LexName){
            if((Item = Table.findName(Lexer.CurrentName)).Category == Table.CategoryModule){
                Lexer.NextLex();
                checkLex(Lexer.LexDot, "\".\"");

                if(Lexer.CurrentLex == Lexer.LexName && 
                        Item.Name.length()+Lexer.CurrentName.length() <= Lexer.NameLen){
                    
                    Item = Table.findName(Item.Name+"."+Lexer.CurrentName);
                }
                else{
                    ErrorMessage.Expected("Name from module " + Item.Name);
                }
            }
            if(Item.Category == Table.CategoryVar){
                AssignmentStatement();
            }
            else if(Item.Category == Table.CategoryStProc && Item.Type == Table.TypeNone){
                CallStatement(Item.Val);
            }
            else {
                ErrorMessage.Expected("Name of variable or procedure");
            }
        }
        else if(Lexer.CurrentLex == Lexer.LexIf){
            IfStatement();
        }
        else if(Lexer.CurrentLex == Lexer.LexWhile){
            WhileStatement();
        }
    }
    
    
    //  Statement { ";" Statement }
    static void StatementSequence() throws Exception {
        Statement();
        while(Lexer.CurrentLex == Lexer.LexSemi){
            Lexer.NextLex();
            Statement();
        }
    }
    
    
    //
    static void ImportModule() throws Exception {
        if(Lexer.CurrentLex == Lexer.LexName){
            Table.NewName(Lexer.CurrentName, Table.CategoryModule);
            if(Lexer.CurrentName.compareTo("In") == 0){
                Table.enterName("In.open", Table.CategoryStProc, Table.TypeNone, StProcInOpen);
                Table.enterName("In.Int", Table.CategoryStProc, Table.TypeNone, StProcInInt);
            }
            else if(Lexer.CurrentName.compareTo("Out") == 0){
                Table.enterName("Out.Int", Table.CategoryStProc, Table.TypeNone, StProcOutInt);
                Table.enterName("Out.Ln", Table.CategoryStProc, Table.TypeNone, StProcOutLn);
            }
            else{
                ErrorMessage.Error("unknown module.");
            }
            Lexer.NextLex();
        }
        else{
            ErrorMessage.Expected("Module name");
        }
    }
    
    
    // IMPORT Name {"," Name} ";"
    static void Import() throws Exception {
        checkLex(Lexer.LexImport, "IMPORT");
        ImportModule();
        while(Lexer.CurrentLex == Lexer.LexComma){
            Lexer.NextLex();
            ImportModule();
        }
        checkLex(Lexer.LexSemi, "\";\"");
    }
    
    
    //  MODULE Name ";"
    //  [ Import ]
    //  DeclarationSequence
    //  [ BEGIN StatementSequence ] END Name "."
    static void Module() throws Exception {
        TableItem ModItem;
        checkLex(Lexer.LexModule, "MODULE");
        if(Lexer.CurrentLex != Lexer.LexName){
            ErrorMessage.Expected("Module name");
        }
        ModItem = Table.NewName(Lexer.CurrentName, Table.CategoryModule);
        Lexer.NextLex();
        checkLex(Lexer.LexSemi, "\";\"");
        if(Lexer.CurrentLex == Lexer.LexImport){
            Import();
        }
        DeclarationSequence();
        if(Lexer.CurrentLex == Lexer.LexBegin){
            Lexer.NextLex();
            StatementSequence();
        }
        checkLex(Lexer.LexEnd, "END");
        if(Lexer.CurrentLex != Lexer.LexName){
            ErrorMessage.Expected("Module name");
        }
        else if(Lexer.CurrentName.compareTo(ModItem.Name) != 0){
            ErrorMessage.Expected("Module name \"" + ModItem.Name + "\"");
        }
        else{
            Lexer.NextLex();
        }
        checkLex(Lexer.LexDot, "\".\"");
        CodeGen.Command(0);
        CodeGen.Command(VM.CommandStop);
        CodeGen.allocateVariables();
    }
    
    
    //
    static void compile() throws Exception {
        Table.openScope();
        Table.enterName("ABS", Table.CategoryStProc, Table.TypeInt, StProcABS);
        Table.enterName("MAX", Table.CategoryStProc, Table.TypeInt, StProcMAX);
        Table.enterName("MIN", Table.CategoryStProc, Table.TypeInt, StProcMIN);
        Table.enterName("DEC", Table.CategoryStProc, Table.TypeNone, StProcDEC);
        Table.enterName("ODD", Table.CategoryStProc, Table.TypeBool, StProcODD);
        Table.enterName("HALT", Table.CategoryStProc, Table.TypeNone, StProcHALT);
        Table.enterName("INC", Table.CategoryStProc, Table.TypeNone, StProcINC);
        Table.enterName("INTEGER", Table.CategoryType, Table.TypeInt, 0);
        Table.openScope();
        Module();
        Table.closeScope();
        Table.closeScope();
//        VM.Result += "\nCompilation complete.\n";
    }

}
