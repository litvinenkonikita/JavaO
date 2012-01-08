package JavaO;

/**
 *
 * @author nikita
 */

import java.io.StreamTokenizer;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Vector;

public class VM {
    
    static final int MemorySize = 8 * 1024;
    
    static final int CommandStop = -1,
            
                        CommandAdd = -2,
                        CommandSub = -3,
                        CommandMult = -4,
                        CommandDiv = -5,
                        CommandMod = -6,
                        CommandNeg = -7,
            
                        CommandLoad =  -8,
                        CommandSave = -9,
            
                        CommandDup = -10,
                        CommandDrop = -11,
                        CommandSwap = -12,
                        CommandOver = -13,
                        
                        CommandGoTo = -14,
                        CommandIfEq = -15,
                        CommandIfNotEq = -16,
                        CommandIfLessEq = -17,
                        CommandIfLessThan = -18,
                        CommandIfGreaterEq = -19,
                        CommandIfGreaterThan = -20,
                        CommandInput = -21,
                        CommandOutput = -22,
                        CommandOutLn = -23;
    
    static int Memory[];
//    static void readln(){
//        try{
//            while(System.in.read() !=  '\n');
//        }
//        catch(IOException e){
//            
//        }
//    }
    
    private static StreamTokenizer input = 
            new StreamTokenizer(new InputStreamReader(System.in));
    
//    static int readInt(){
//        try{
//            input.nextToken();
//        }
//        catch(IOException e){}
//        return (int)input.nval;
//    }
    

    private static String ByteCode;
    static String Result;
    private static java.util.HashMap<Integer, String> VariablesMap;
    private static Vector StackState;
    private static Vector<Vector> StackStates;
    
    static int getByteCodeCount(){
        int Length = 0;
        while((Memory[Length++]) != CommandStop);
        return Length;
    }
    
    public static void addVariable(int Address, String VarName){
        VariablesMap.put(Address, VarName);
    }
    
    public static Vector getByteCode(){
        Vector ResultByteCodeVector = new Vector();
        Vector Row;
        Integer Command = new Integer(0);
        String CommandStr = new String();
        String CommandDesc = new String();
        int PC = 0;
        while((Command = Memory[PC++]) != CommandStop){
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
                switch(Command){
                    case CommandAdd :
                        CommandStr = "ADD";
                        break;

                    case CommandSub :
                        CommandStr = "SUB";
                        break;

                    case CommandMult :
                        CommandStr = "MULT";
                        break;

                    case CommandDiv :
                        CommandStr = "DIV";
                        break;

                    case CommandMod :
                        CommandStr = "MOD";
                        break;

                    case CommandNeg :
                        CommandStr = "NEG";
                        break;

                    case CommandLoad :
                        CommandStr = "LOAD";
                        break;

                    case CommandSave :
                        CommandStr = "SAVE";
                        break;

                    case CommandDup :
                        CommandStr = "DUP";
                        break;

                    case CommandDrop :
                        CommandStr = "DROP";
                        break;

                    case CommandSwap :
                        CommandStr = "SWAP";
                        break;

                    case CommandOver :
                        CommandStr = "OVER";
                        break;

                    case CommandGoTo :
                        CommandStr = "GOTO";
                        break;

                    case CommandIfEq :
                        CommandStr = "IFEQ";
                        break;

                    case CommandIfNotEq :
                        CommandStr = "IFNE";
                        break;

                    case CommandIfLessEq :
                        CommandStr = "IFLE";
                        break;

                    case CommandIfLessThan :
                        CommandStr = "IFLT";
                        break;

                    case CommandIfGreaterEq :
                        CommandStr = "IFGE";
                        break;

                    case CommandIfGreaterThan :
                        CommandStr = "IFGT";
                        break;

                    case CommandOutput :
                        CommandStr = "OUTPUT";
                        break;

                    case CommandOutLn :
                        CommandStr = "OUTLN";
                        break;

                    case CommandStop :
                        CommandStr = "STOP";
                        break;

//                    default :
//                        Result += "\nInvalid operation code.";
//                        break;
                }
            }
            Row = new Vector(Arrays.asList(PC-1, CommandStr, CommandDesc));
            ResultByteCodeVector.add(Row);
        }
        Row = new Vector(Arrays.asList(PC-1, "STOP", "command"));
        ResultByteCodeVector.add(Row);
        return ResultByteCodeVector;
    }
    
    public static String getResult(){
        return Result;
    }
    
//    public static Vector getStackState(){
//        Vector StackState_ = (Vector)StackState.clone();
//        StackState.clear();
//        return StackState_;
//    }
    
//    public static void setStackState(Vector StackState_){
//        StackState = StackState_;
//    }
    
    public static Vector<Vector> getStackStates(){
        return StackStates;
    }
    
    static void init(){
        Memory = new int[MemorySize];
        //ByteCode = new String();
        Result = new String();
        StackStates = new Vector<Vector>();
        //StackState = new Vector();
        VariablesMap = new java.util.HashMap<Integer, String>();
    }
    
    static void run(){
        //StackState.clear();
        //StackState = new Vector();
        
        Vector Row;
        Result = "";
        int PC = 0;
        int SP = MemorySize;
        Integer Command = new Integer(0);
        int Buffer;
        String CommandStr = new String();

        while( (Command = Memory[PC++]) != CommandStop ){
            //StackState.clear();
            StackState = new Vector();
            //while(!StackState.isEmpty()){/* do nothing */}

            if(Command >= 0){
                Memory[--SP] = Command;
            }
            else{
                switch(Command){
                    case CommandAdd :
                        SP++;
                        Memory[SP] += Memory[SP-1];
                        CommandStr = "ADD";
                        break;

                    case CommandSub :
                        SP++;
                        Memory[SP] -= Memory[SP-1];
                        CommandStr = "SUB";
                        break;

                    case CommandMult :
                        SP++;
                        Memory[SP] *= Memory[SP-1];
                        CommandStr = "MULT";
                        break;

                    case CommandDiv :
                        SP++;
                        Memory[SP] /= Memory[SP-1];
                        CommandStr = "DIV";
                        break;

                    case CommandMod :
                        SP++;
                        Memory[SP] %= Memory[SP-1];
                        CommandStr = "MOD";
                        break;

                    case CommandNeg :
                        Memory[SP] = -Memory[SP];
                        CommandStr = "NEG";
                        break;

                    case CommandLoad :
                        Memory[SP] = Memory[Memory[SP]];
                        CommandStr = "LOAD";
                        break;

                    case CommandSave :
                        Memory[Memory[SP+1]] = Memory[SP];
                        SP+=2;
                        CommandStr = "SAVE";
                        break;

                    case CommandDup :
                        SP--;
                        Memory[SP] = Memory[SP+1];
                        CommandStr = "DUP";
                        break;

                    case CommandDrop :
                        SP++;
                        CommandStr = "DROP";
                        break;

                    case CommandSwap :
                        Buffer = Memory[SP];
                        Memory[SP] = Memory[SP+1];
                        Memory[SP+1] = Buffer;
                        CommandStr = "SWAP";
                        break;

                    case CommandOver :
                        SP--;
                        Memory[SP] = Memory[SP+2];
                        CommandStr = "OVER";
                        break;

                    case CommandGoTo :
                        PC = Memory[SP++];
                        CommandStr = "GOTO";
                        break;

                    case CommandIfEq :
                        if(Memory[SP+2] == Memory[SP+1]){
                            PC = Memory[SP];
                        }
                        SP += 3;
                        CommandStr = "IFEQ";
                        break;

                    case CommandIfNotEq :
                        if(Memory[SP+2] != Memory[SP+1]){
                            PC = Memory[SP];
                        }
                        SP += 3;
                        CommandStr = "IFNE";
                        break;

                    case CommandIfLessEq :
                        if(Memory[SP+2] <= Memory[SP+1]){
                            PC = Memory[SP];
                        }
                        SP += 3;
                        CommandStr = "IFLE";
                        break;

                    case CommandIfLessThan :
                        if(Memory[SP+2] < Memory[SP+1]){
                            PC = Memory[SP];
                        }
                        SP += 3;
                        CommandStr = "IFLT";
                        break;

                    case CommandIfGreaterEq :
                        if(Memory[SP+2] >= Memory[SP+1]){
                            PC = Memory[SP];
                        }
                        SP += 3;
                        CommandStr = "IFGE";
                        break;

                    case CommandIfGreaterThan :
                        if(Memory[SP+2] > Memory[SP+1]){
                            PC = Memory[SP];
                        }
                        SP += 3;
                        CommandStr = "IFGT";
                        break;

//                        case CommandInput :
//                            System.out.println("Input :");
//                            SP--;
//                            Memory[SP] = readInt();
//                            CommandStr = "INPUT";
//                            break;

                    case CommandOutput :
                        int Tab = Memory[SP] - (new Integer(Memory[SP+1])).toString().length();
                        for(int i = 0; i <= Tab; i++){
                            //System.out.print(" ");
                            Result += " ";
                        }
                        Result += Memory[SP+1];
                        SP += 2;
                        CommandStr = "OUTPUT";
                        break;

                    case CommandOutLn :
                        Result += '\n';
                        CommandStr = "OUTLN";
                        break;

                    case CommandStop :
                        CommandStr = "STOP";
                        break;

                    default :
                        Result += "\nInvalid operation code.";
                        break;
                }
            }
            if(Command >= 0){
                CommandStr = Command.toString();
            }

//                if(Command < 0){
//                    StackStates += "command: " + CommandStr + "\n----------\n";
//                }
//                for(int i = SP; i < MemorySize; i++){
//                    StackStates += Memory[i] + "\n";
//                }
//                StackStates += "\n==========\n\n";

            //for(int i = SP; i < MemorySize; i++){
            for(int i = MemorySize-1; i >= SP; i--){
                Row = new Vector(Arrays.asList(Memory[i], i));
                //System.out.println(Row.toString());
                StackState.add(Row);
            }
            //System.out.println();
            StackStates.add(StackState);
        }


        Result += '\n';
        if(SP < MemorySize){
            Result += "Return code: " + Memory[SP] + "\n\n";
        }
    }
            
}
