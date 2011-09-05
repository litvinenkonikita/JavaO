package JavaO;

/**
 *
 * @author nikita
 */

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.InputStreamReader;

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
                        CommandLessThan = -18,
                        CommandGreaterEq = -19,
                        CommandGreaterThan = -20,
                        CommandInput = -21,
                        CommandOutput = -22,
                        CommandOutLn = -23;
    
    
    static int Memory[] = new int[MemorySize];
    
    static void readln(){
        try{
            while(System.in.read() !=  '\n');
        }
        catch(IOException e){
            
        }
    }
    
    private static StreamTokenizer input = 
            new StreamTokenizer(new InputStreamReader(System.in));
    
    static int readInt(){
        try{
            input.nextToken();
        }
        catch(IOException e){}
        return (int)input.nval;
    }
    
    
    static void run(){
        int PC = 0;
        int SP = MemorySize;
        int Command;
        int Buffer;
        
        while((Command = Memory[PC++]) != CommandStop){
            if(Command >= 0){
                Memory[--SP] = Command;
            }
            else{
                switch(Command){
                    case CommandAdd :
                        SP++;
                        Memory[SP] += Memory[SP-1];
                        break;
                        
                    case CommandSub :
                        SP++;
                        Memory[SP] -= Memory[SP-1];
                        break;
                        
                    case CommandMult :
                        SP++;
                        Memory[SP] *= Memory[SP-1];
                        break;
                        
                    case CommandDiv :
                        SP++;
                        Memory[SP] /= Memory[SP-1];
                        break;
                        
                    case CommandMod :
                        SP++;
                        Memory[SP] %= Memory[SP-1]; 
                        break;
                        
                    case CommandNeg :
                        Memory[SP] = -Memory[SP];
                        break;
                        
                    case CommandLoad :
                        Memory[SP] = Memory[Memory[SP]];
                        break;
                        
                    case CommandSave :
                        Memory[Memory[SP+1]] = Memory[SP];
                        SP+=2;
                        break;
                        
                    case CommandDup :
                        SP--;
                        Memory[SP+1] = Memory[SP];
                        break;
                        
                    case CommandDrop :
                        SP++;
                        break;
                        
                    case CommandSwap :
                        Buffer = Memory[SP];
                        Memory[SP] = Memory[SP+1];
                        Memory[SP+1] = Buffer;
                        break;
                        
                    case CommandOver :
                        SP--;
                        Memory[SP] = Memory[SP+2];
                        break;
                        
                    case CommandGoTo :
                        PC = Memory[SP++];
                        break;
                        
                    case CommandIfEq :
                        if(Memory[SP+2] == Memory[SP+1]){
                            PC = Memory[SP];
                        }
                        SP += 3;
                        break;
                        
                    case CommandIfNotEq :
                        if(Memory[SP+2] != Memory[SP+1]){
                            PC = Memory[SP];
                        }
                        SP += 3;
                        break;
                        
                    case CommandIfLessEq :
                        if(Memory[SP+2] <= Memory[SP+1]){
                            PC = Memory[SP];
                        }
                        SP += 3;
                        break;
                        
                    case CommandLessThan :
                        if(Memory[SP+2] < Memory[SP+1]){
                            PC = Memory[SP];
                        }
                        SP += 3;
                        break;
                        
                    case CommandGreaterEq :
                        if(Memory[SP+2] >= Memory[SP+1]){
                            PC = Memory[SP];
                        }
                        SP += 3;
                        break;
                        
                    case CommandGreaterThan :
                        if(Memory[SP+2] > Memory[SP+1]){
                            PC = Memory[SP];
                        }
                        SP += 3;
                        break;
                        
                    case CommandInput :
                        System.out.println("Input :");
                        SP--;
                        Memory[SP] = readInt();
                        break;
                        
                    case CommandOutput :
                        break;
                        
                    case CommandOutLn :
                        System.out.println();
                        break;
                        
                    case CommandStop :
                        break;
                        
                    default :
                        System.out.println("Invalid operation code.");
                        break;
                }
            }
        }
        System.out.println();
        if(SP < MemorySize){
            System.out.println("Return code: " + Memory[SP]);
        }
        System.out.println("Press Enter!");
        readln();
    }
            
}
