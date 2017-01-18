import org.w3c.dom.ls.LSException;

import java.io.File;
import java.util.*;

import static java.lang.Character.isLetter;

/**
 * Created by 25-025 on 2017/1/11.
 */
public class Learn<R>{
    private R r;
    public void add(R r){
        this.r = r;
    }
    public R get(){
        return r;
    }
    public static void main(String[] args) {
        ArrayList<String> a = new ArrayList<>(0);
        a.add("a");
    }
}
