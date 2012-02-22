package JavaO;

import java.util.Vector;

/**
 *
 * @author nikita
 */
class FullStackState{
    int PC;
    int StackStateNumber;
    Vector State;
    String Result;
    
    public FullStackState(int PC, int StackStateNumber, Vector State, String Result){
        this.PC = PC;
        this.StackStateNumber = StackStateNumber;
        this.State = new Vector(State);
        this.Result = Result;
    }
    
    public FullStackState(){
        this.PC = 0;
        this.StackStateNumber = 0;
        this.State = new Vector();
    }
}
