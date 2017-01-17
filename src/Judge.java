import java.util.Arrays;
import java.util.ArrayList;

/**
 * Created by 25-025 on 2017/1/11.
 */
public class Judge {
    private int currState = 0;
    private ArrayList<String> text;
    private int index = 0;
    private int line = 1;

    public int getCurrState(){
        return currState;
    }
    public int getIndex(){
        return index;
    }
    public Judge(ArrayList<String > text){
        this.text = text;
    }
    // 当当前状态可能包含STRING输入时，用一个数组allNext保存下一个状态的值
    public int[] allNext(int[] notCh,int ch){
        int len = notCh.length;
        int[] p = new int[63];
        for(int i = 0; i < 63; i++){
            p[i] = ch;
        }
        notCh = Arrays.copyOf(notCh,len+63);
        System.arraycopy(p,0,notCh,len,63);
        return notCh;
    }
    public String[] allInput(String[] inStr){
        int len = inStr.length;
        byte[] allChar0 = new byte[63];
        for(int i = 0; i < 26; i++){
            allChar0[i] = (byte)(65 + i);
            allChar0[i+26] = (byte)(97 + i);
        }
        for(int i = 0; i < 10; i++){
            allChar0[i+52] = (byte)(48+i);
        }
        allChar0[62] = (byte)(32);
        String allChar[] = new String(allChar0).split("");
        inStr =Arrays.copyOf(inStr,len+63);
        System.arraycopy(allChar,0,inStr,len,63);
        return inStr;
    }
    public int ifFind(ArrayList<String > ruleStr){
        for(int j = 0; j < ruleStr.size(); j++){
            String docStr = "";
            for(int i = 0; i < ruleStr.get(j).length(); i++){
                docStr = docStr + text.get(index+i);
            }
            if(ruleStr.get(j).equals(docStr)){
                if(docStr.equals("\r\n")){
                    line++;
                }
                return j;
            }
        }
        return -1;
    }
    public void check(ArrayList<State> stateMachine){
            ArrayList<String> ruleStr = stateMachine.get(currState).input;
            int sequence = ifFind(ruleStr);
            if(sequence >= 0){
//                System.out.printf("The %s th state has got a valid input !\n",currState);
                index += ruleStr.get(sequence).length();
                currState = stateMachine.get(currState).getStateNext()[sequence];
            }
            else {
                index = text.size()-1;
//                System.out.printf("The %s th state has got a invalid input!\n",currState);
                System.out.printf("Syntax error happens at line %s !\n",line);
            }
        }
}




