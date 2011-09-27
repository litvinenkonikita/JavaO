package JavaO;

/**
 *
 * @author nikita
 */


//import java.lang.StringBuffer;

public class Lexer {
    
    static int NameLen = 31;    // max name length
    
    //  Все типы лексем
    final static int LexNone = 0, LexName = 1, LexNum = 2,
            LexModule = 3, LexImport = 4, LexBegin = 5, LexEnd = 6,
            LexVar = 7, LexConst = 8, LexWhile = 9, LexDo = 10,
            LexIf = 11, LexThen = 12, LexElsif = 13, LexElse = 14,
            LexMult = 15, LexDiv = 16, LexMod = 17, LexPlus = 18,
            LexMinus = 19, LexEq = 20, LexNotEq = 21, LexLessThan = 22,
            LexLessEq = 23, LexGreaterThan = 24, LexGreaterEq = 25,
            LexDot = 26, LexComma = 27, LexColon = 28, LexSemi = 29,
            LexAss = 30, LexRightPar = 31, LexLeftPar = 32, LexEOT = 33;
    
    static int CurrentLex;
    
    private static StringBuffer Buffer = new StringBuffer(NameLen);
    
    static String CurrentName;
    
    static int CurrentNum;
    
    private static int KeywordsNum = 34;
    private static int NextKeywordNumber;
    
    static private class Item {
        String Word;
        int Lex;
    }
    
    //private static Item[] KeywordsTable = new Item[KeywordsNum];
    private static Item[] KeywordsTable;

    private static void enterKeyword(String LexName , int LexType){
        (KeywordsTable[NextKeywordNumber] = new Item()).Word = new String(LexName);
        KeywordsTable[NextKeywordNumber++].Lex = LexType;
    }
    
    private static int testKeyword(){
        for(int i = NextKeywordNumber - 1; i >= 0; i--){
            if(KeywordsTable[i].Word.compareTo(CurrentName) == 0){
                return KeywordsTable[i].Lex;
            }
        }
        return LexName;
    }
    
    
    private static void Ident() throws Exception{
        int i = 0;
        Buffer.setLength(0);
        do{
            if(i++ < NameLen){
                Buffer.append((char)Text.CurrentChar);
            } 
            else {
                ErrorMessage.Error("Name is too long.");
            }
            Text.NextChar();
        } while(Character.isLetterOrDigit((char)Text.CurrentChar));
        CurrentName = Buffer.toString();
        CurrentLex = testKeyword();
    }
    
    
    private static void Number() throws Exception{
        CurrentLex = LexNum;
        CurrentNum = 0;
        do {
            int d = Text.CurrentChar - '0';
            if((Integer.MAX_VALUE - d)/10 >= CurrentNum){
                CurrentNum = 10*CurrentNum + d;
            }
            else{
                ErrorMessage.Error("Number is too big");
            }
            Text.NextChar();
        } while(Character.isDigit((char)Text.CurrentChar));
    }
    
    
    private static void  Comment() throws Exception{
        Text.NextChar();
        do{
            while(Text.CurrentChar != '*' && Text.CurrentChar != Text.CharEOT){
                /*if(Text.CurrentChar == '('){
                    Text.NextChar();
                    if(Text.CurrentChar == '*'){
                        Comment();
                    }
                }
                else{*/
                    Text.NextChar();
                //}
            }
            
            if(Text.CurrentChar == '*'){
                Text.NextChar();
            }
            
        } while(Text.CurrentChar != ')' && Text.CurrentChar != Text.CharEOT);
        
        if(Text.CurrentChar == ')'){
            Text.NextChar();
        }
        else{
            Location.LexPos = Location.Pos;
            ErrorMessage.Error("Comment unfinished!");
        }
    }
    
    
    static void NextLex() throws Exception{
        while(Text.CurrentChar == Text.CharEOL ||
              Text.CurrentChar == Text.CharSpace ||
              Text.CurrentChar == Text.CharTab)
        {
            Text.NextChar();
        }
        
        Location.LexPos = Location.Pos;
        if(Character.isLetter((char)Text.CurrentChar)){
            Ident();
        }
        else if(Character.isDigit((char)Text.CurrentChar)){
            Number();
        }
        else{
            switch((char)Text.CurrentChar){
                case ';' :
                    Text.NextChar();
                    CurrentLex = LexSemi;
                    break;
                    
                case ':' :
                    Text.NextChar();
                    if(Text.CurrentChar == (int) '='){
                        CurrentLex = LexAss;
                        Text.NextChar();
                    }
                    else{
                        CurrentLex = LexColon;
                    }
                    break;
                    
                case '.' :
                    Text.NextChar();
                    CurrentLex = LexDot;
                    break;
                    
                case ',' :
                    Text.NextChar();
                    CurrentLex = LexComma;
                    break;
                    
                case '=' :
                    Text.NextChar();
                    CurrentLex = LexEq;
                    break;
                    
                case '#' :
                    Text.NextChar();
                    CurrentLex = LexEq;
                    break;
                    
                case '>' :
                    Text.NextChar();
                    if(Text.CurrentChar == (int) '='){
                        CurrentLex = LexGreaterEq;
                        Text.NextChar();
                    }
                    else{
                        CurrentLex = LexGreaterThan;
                    }
                    break;
                    
                case '<' :
                    Text.NextChar();
                    if(Text.CurrentChar == (int) '='){
                        CurrentLex = LexLessEq;
                        Text.NextChar();
                    }
                    else{
                        CurrentLex = LexLessThan;
                    }
                    break;
                    
                case '(' :
                    Text.NextChar();
                    if(Text.CurrentChar == '*'){
                        Comment();
                        Lexer.NextLex();
                    }
                    else{
                        CurrentLex = LexLeftPar;
                    }
                    break;
                    
                case ')' :
                    Text.NextChar();
                    CurrentLex = LexRightPar;
                    break;
                    
                case '+' :
                    Text.NextChar();
                    CurrentLex = LexPlus;
                    break;
                    
                case '-' :
                    Text.NextChar();
                    CurrentLex = LexMinus;
                    break;
                    
                case '*' :
                    Text.NextChar();
                    CurrentLex = LexMult;
                    break;
                    
                case Text.CharEOT :
//                    if(FirstEOT){
//                        FirstEOT = false;
//                    }
                    CurrentLex = LexEOT;
                    break;
                    
                default :
                    ErrorMessage.Error("Invalid symbol.");
                    
            }
        }
    }
    
    
    static void init() throws Exception {
        KeywordsTable = new Item[KeywordsNum];
        NextKeywordNumber = 0;
        enterKeyword("ARRAY", LexNone);
        enterKeyword("BY", LexNone);
        enterKeyword("BEGIN", LexBegin);
        enterKeyword("CASE", LexNone);
        enterKeyword("CONST", LexConst);
        enterKeyword("DIV", LexDiv);
        enterKeyword("DO", LexDo);
        enterKeyword("ELSE", LexElse);
        enterKeyword("ELSIF", LexElsif);
        enterKeyword("END", LexEnd);
        enterKeyword("EXIT", LexNone);
        enterKeyword("FOR", LexNone);
        enterKeyword("IF", LexIf);
        enterKeyword("IMPORT", LexImport);
        enterKeyword("IN", LexNone);
        enterKeyword("IS", LexNone);
        enterKeyword("LOOP", LexNone);
        enterKeyword("MOD", LexMod);
        enterKeyword("MODULE", LexModule);
        enterKeyword("NIL", LexNone);
        enterKeyword("OF", LexNone);
        enterKeyword("OR", LexNone);
        enterKeyword("POINTER", LexNone);
        enterKeyword("PROCEDURE", LexNone);
        enterKeyword("RECORD", LexNone);
        enterKeyword("REPEAT", LexNone);
        enterKeyword("RETURN", LexNone);
        enterKeyword("THEN", LexThen);
        enterKeyword("TO", LexNone);
        enterKeyword("TYPE", LexNone);
        enterKeyword("UNTIL", LexNone);
        enterKeyword("VAR", LexVar);
        enterKeyword("WHILE", LexWhile);
        enterKeyword("WITH", LexNone);

        Lexer.NextLex();
    }
}
