package JavaO;

/**
 *
 * @author nikita
 */

import java.io.StreamTokenizer;
import java.io.InputStreamReader;
import java.lang.Thread;
import javax.swing.SwingWorker;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.HashMap;


public class VM extends SwingWorker<Vector<FullStackState>, FullStackState>{
    
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
    
    static final HashMap<Integer, String> CommandsMap = new HashMap<Integer, String>(){
        {
            put(CommandStop, "STOP");
            put(CommandAdd, "ADD");
            put(CommandSub, "SUB");
            put(CommandMult, "MULT");
            put(CommandDiv, "DIV");
            put(CommandMod, "MOD");
            put(CommandNeg, "NEG");
            put(CommandLoad, "LOAD");
            put(CommandSave, "SAVE");
            put(CommandDup, "DUP");
            put(CommandDrop, "DROP");
            put(CommandSwap, "SWAP");
            put(CommandOver, "OVER");
            put(CommandGoTo, "GOTO");
            put(CommandIfEq, "IFEQ");
            put(CommandIfNotEq, "IFNE");
            put(CommandIfLessEq, "IFLE");
            put(CommandIfLessThan, "IFLT");
            put(CommandIfGreaterEq, "IFGE");
            put(CommandIfGreaterThan, "IFGT");
            put(CommandInput, "INPUT");
            put(CommandOutput, "OUTPUT");
            put(CommandOutLn, "OUTLN");
        }
    };
    
    static int Memory[];
    /*static */final Object monitor = new Object();
    
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
    
    String Result;
    private HashMap<Integer, String> VariablesMap;
    private Vector StackState;
    private Vector<FullStackState> StackStates;
    private MainFrame mainFrame;
    private int StackStatesCounter;
    //private long ThreadID;
    
    public VM(int Memory[], HashMap<Integer, String> VariablesMap, MainFrame mainFrame){
        init();
        this.Memory = Memory;
        this.VariablesMap = VariablesMap;
        this.mainFrame = mainFrame;
    }
     
    public String getResult(){
        return Result;
    }
    
    void init(){
        Memory = new int[MemorySize];
        Result = new String();
        StackStates = new Vector<FullStackState>();
        VariablesMap = new HashMap<Integer, String>();
    }
    
    void runVM(){
        //System.out.println(n);
        FullStackState FStackState;
        Vector Row;
        Result = "";
        int PC = 0;
        StackStatesCounter = 0;
        int SP = MemorySize;
        Integer Command = new Integer(0);
        int Buffer;
        String CommandStr = new String();

        while( (Command = Memory[PC++]) != CommandStop ){
            //System.out.println(Thread.currentThread().getId());
            StackStatesCounter++;
            if(Command >= 0){
                Memory[--SP] = Command;
            }
            else{
                StackState = new Vector(StackState);
                CommandStr = CommandsMap.get(Command);
                ((Vector)StackState.lastElement()).setElementAt(CommandStr, 1);
                FStackState = new FullStackState(PC-1, StackStatesCounter-1, StackState, Result);
                publish(FStackState);
                StackStates.add(FStackState);
                try{
                    Thread.sleep(mainFrame.getDelay() * 1000);
                }
                catch(InterruptedException e){
                    //запись в лог
                }
                switch(Command){
                    case CommandAdd :
                        SP++;
                        Memory[SP] += Memory[SP-1];
                        //CommandStr = "ADD";
                        break;

                    case CommandSub :
                        SP++;
                        Memory[SP] -= Memory[SP-1];
                        //CommandStr = "SUB";
                        break;

                    case CommandMult :
                        SP++;
                        Memory[SP] *= Memory[SP-1];
                        //CommandStr = "MULT";
                        break;

                    case CommandDiv :
                        SP++;
                        Memory[SP] /= Memory[SP-1];
                        //CommandStr = "DIV";
                        break;

                    case CommandMod :
                        SP++;
                        Memory[SP] %= Memory[SP-1];
                        //CommandStr = "MOD";
                        break;

                    case CommandNeg :
                        Memory[SP] = -Memory[SP];
                        //CommandStr = "NEG";
                        break;

                    case CommandLoad :
                        Memory[SP] = Memory[Memory[SP]];
                        //CommandStr = "LOAD";
                        break;

                    case CommandSave :
                        Memory[Memory[SP+1]] = Memory[SP];
                        SP+=2;
                        //CommandStr = "SAVE";
                        break;

                    case CommandDup :
                        SP--;
                        Memory[SP] = Memory[SP+1];
                        //CommandStr = "DUP";
                        break;

                    case CommandDrop :
                        SP++;
                        //CommandStr = "DROP";
                        break;

                    case CommandSwap :
                        Buffer = Memory[SP];
                        Memory[SP] = Memory[SP+1];
                        Memory[SP+1] = Buffer;
                        //CommandStr = "SWAP";
                        break;

                    case CommandOver :
                        SP--;
                        Memory[SP] = Memory[SP+2];
                        //CommandStr = "OVER";
                        break;

                    case CommandGoTo :
                        PC = Memory[SP++];
                        //CommandStr = "GOTO";
                        break;

                    case CommandIfEq :
                        if(Memory[SP+2] == Memory[SP+1]){
                            PC = Memory[SP];
                        }
                        SP += 3;
                        //CommandStr = "IFEQ";
                        break;

                    case CommandIfNotEq :
                        if(Memory[SP+2] != Memory[SP+1]){
                            PC = Memory[SP];
                        }
                        SP += 3;
                        //CommandStr = "IFNE";
                        break;

                    case CommandIfLessEq :
                        if(Memory[SP+2] <= Memory[SP+1]){
                            PC = Memory[SP];
                        }
                        SP += 3;
                        //CommandStr = "IFLE";
                        break;

                    case CommandIfLessThan :
                        if(Memory[SP+2] < Memory[SP+1]){
                            PC = Memory[SP];
                        }
                        SP += 3;
                        //CommandStr = "IFLT";
                        break;

                    case CommandIfGreaterEq :
                        if(Memory[SP+2] >= Memory[SP+1]){
                            PC = Memory[SP];
                        }
                        SP += 3;
                        //CommandStr = "IFGE";
                        break;

                    case CommandIfGreaterThan :
                        if(Memory[SP+2] > Memory[SP+1]){
                            PC = Memory[SP];
                        }
                        SP += 3;
                        //CommandStr = "IFGT";
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
                            Result += " ";
                        }
                        Result += Memory[SP+1];
                        SP += 2;
                        //CommandStr = "OUTPUT";
                        break;

                    case CommandOutLn :
                        Result += '\n';
                        CommandStr = "OUTLN";
                        break;

                    case CommandStop :
                        //CommandStr = "STOP";
                        break;

                    default :
                        Result += "\nInvalid operation code.";
                        break;
                }
            }
            if(Command >= 0){
                CommandStr = Command.toString();
            }

            StackState = new Vector();
            for(int i = MemorySize-1; i >= SP; i--){
                Row = new Vector(Arrays.asList(Memory[i], null));
                StackState.add(Row);
            }
            if(Command == CommandGoTo || 
                    Command == CommandIfEq || Command == CommandIfNotEq ||
                    Command == CommandIfLessEq || Command == CommandIfLessThan ||
                    Command == CommandIfGreaterEq || Command == CommandIfGreaterThan){
                
                FStackState = new FullStackState(PC, StackStatesCounter, StackState, Result);
            }
            else{
                FStackState = new FullStackState(PC-1, StackStatesCounter-1, StackState, Result);
            }
            publish(FStackState);
            StackStates.add(FStackState);
            
//            if(!mainFrame.getStepForward()){
//                try{
//                    Thread.sleep(mainFrame.getDelay() * 1000);
//                }
//                catch(InterruptedException e){
//                    //запись в лог
//                }
//            }
//            
            if(mainFrame.getStepForward()){ //  Это не успевает
                //System.out.println("!");
                mainFrame.Running = false; 
                mainFrame.Paused = true;
                mainFrame.setStepForward(false);
            }
            else{
                try{
                    Thread.sleep(mainFrame.getDelay() * 1000);
                }
                catch(InterruptedException e){
                    //запись в лог
                }
            }
            
            synchronized(monitor){
                while(mainFrame.Paused || !mainFrame.Running){
                    try{
                        monitor.wait();
                    }
                    catch(InterruptedException e){
                        //System.err.println("Monitor exception: "+e.getMessage());
                        Thread.currentThread().stop();
                    }
                }
            }

        }
        StackState.clear();
        FStackState = new FullStackState(PC-1, StackStatesCounter-1, StackState, Result);
        publish(FStackState);
        StackStates.add(FStackState);

        Result += '\n';
        if(SP < MemorySize){
            Result += "Return code: " + Memory[SP] + "\n\n";
        }
    }
    
    @Override
    protected Vector<FullStackState> doInBackground(){
        //ThreadID = Thread.currentThread().getId();
        runVM();
        return StackStates;
    }
    
    @Override
    protected void done(){
        mainFrame.setResult(Result);
        mainFrame.setRunning(false);
        //mainFrame.setStopped(true);
        //mainFrame.setStackStates(StackStates);
        
        mainFrame.setRunEnabled(true);
        mainFrame.setCompileEnabled(true);
        mainFrame.setPauseEnabled(false);
        mainFrame.setStopEnabled(false);
        
        mainFrame.setStepForwardEnabled(true);
        mainFrame.setStepBackEnabled(true);
        //System.err.println("END "+ThreadID);
    }
    
    @Override
    protected void process(List<FullStackState> FStackStates){
        for(FullStackState FStackState : FStackStates){
//            mainFrame.selectByteCode(FStackState.PC);
//            mainFrame.setStackState(FStackState.State);
//            mainFrame.setResult(FStackState.Result);
            mainFrame.setFullStackState(FStackState);
        }
        mainFrame.setStackStates(StackStates);
    }
}
