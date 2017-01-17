import java.io.File;
import java.util.*;

/**
 * Created by 25-025 on 2017/1/11.
 */
public class Learn {
    public static void main(String[] args) {
//        File f = new File("C:\\Users\\25-025\\IdeaProjects\\test\\txtFiles");
        File f = new File("C:\\Users\\25-025\\IdeaProjects\\test");
        String[] a = f.list();
        File[] b = f.listFiles();
        System.out.println();
    }
}
