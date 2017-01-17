import java.util.ArrayList;

/**
 * Created by 25-025 on 2017/1/11.
 */
public class State {
    private int state;
    ArrayList<String> input;
    private int[] stateNext;

    public int getState(){
        return state;
    }
    public  int[] getStateNext(){
        return  stateNext;
    }
    public State(int state, String [] input0, int[] stateNext){
        this.state = state;
        this.stateNext = stateNext;
        input = new ArrayList<String>();
        for(int i = 0; i < input0.length; i++){
            input.add(input0[i]);
        }
    }

}
