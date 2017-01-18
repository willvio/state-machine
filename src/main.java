import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 25-025 on 2017/1/12.
 */
public class main {
    public static void main(String[] args) {
        /*
        导入待检测的文档
         */
        ArrayList<File> pass = new ArrayList<File>();
        ArrayList<File> fail = new ArrayList<File>();
        String testpath = "C:\\Users\\25-025\\IdeaProjects\\test\\txtFiles";
        File txtFile = new File(testpath);
        for(File file: txtFile.listFiles()){
            Long fileLength = file.length();
            byte[] fileContent = new byte[fileLength.intValue()];
            try{
                FileInputStream in = new FileInputStream(file);
                in.read(fileContent);
                in.close();
            } catch (Exception e){
                e.printStackTrace();
            }
            String[] text0 = new String(fileContent).split("");
            List<String > text01 = Arrays.asList(text0);
            ArrayList<String > text = new ArrayList<String>(text01);
            text.add("-1"); //末尾添加-1作为结束的标志
//            System.out.println("The .txt file waited for check is as follows:\n");
//            for(int i = 0; i < (text.size()-1); i++){
//                System.out.print(text.get(i));
//            }
//            System.out.println("\n");

            Judge judge = new Judge(text);
            //建立状态机
            ArrayList<State> rule = new ArrayList<State>();


            rule.add(new State(0,new String[]{"<"},new int[]{1}));
            rule.add(new State(1,new String[]{"!DOCTYPE"},new int[]{2}));
            rule.add(new State(2,new String[]{" ","\t"},new int[]{3,3}));
            rule.add(new State(3,new String[]{" ","\t","HTML"},new int[]{3,3,4}));
            rule.add(new State(4,new String[]{" ","\t",">"},new int[]{4,4,5}));
            rule.add(new State(5,new String[]{" ","\t","\r\n","<HTML>"},new int[]{5,5,5,6}));
            rule.add(new State(6,judge.allInput(new String[]{" ","\t","\r\n","<HEAD>","<TITLE>"}) ,
                    judge.allNext(new int[]{6,6,6,7,8},8) ));
            rule.add(new State(7,judge.allInput(new String[]{" ","\t","\r\n","<TITLE>"}) ,
                    judge.allNext(new int[]{7,7,7,8},8) ));
            rule.add(new State(8,judge.allInput(new String[]{" ","\t","\r\n","</TITLE>","</HEAD>","<BODY>"}) ,
                    judge.allNext(new int[]{8,8,8,9,10,11},8) ));
            rule.add(new State(9,new String[]{" ","\t","\r\n","</HEAD>","<BODY>"} ,new int[]{9,9,9,10,11}) );
            rule.add(new State(10,new String[]{" ","\t","\r\n","<BODY>"} ,new int[]{10,10,10,11}) );
            rule.add(new State(11,new String[]{" ","\t","\r\n","<P>","<a","</BODY>"},new int[]{11,11,11,12,14,20}));
            rule.add(new State(12,judge.allInput(new String[]{" ","\t","\r\n","</P>"}) ,
                    judge.allNext(new int[]{12,12,12,13},12) ));
            rule.add(new State(13,new String[]{" ","\t","\r\n","<P>","<a","</BODY>"},new int[]{13,13,13,12,14,20}));
            rule.add(new State(14,new String[]{" ","\t"},new int[]{15,15}));
            rule.add(new State(15,new String[]{"href=\""},new int[]{16}));
            rule.add(new State(16,judge.allInput(new String[]{" ","\t","\r\n","\""}) ,
                    judge.allNext(new int[]{16,16,16,17},16) ));
            rule.add(new State(17,new String[]{" ","\t",">"},new int[]{17,17,18}));
            rule.add(new State(18,judge.allInput(new String[]{" ","\t","\r\n","</a>"}) ,
                    judge.allNext(new int[]{18,18,18,19},18) ));
            rule.add(new State(19,new String[]{"</a>"," ","\t","\r\n","<P>","<a","</BODY>"},
                    new int[]{19,19,19,19,12,14,20}));
            rule.add(new State(20,new String[]{" ","\t","\r\n","</HTML>"},new int[]{20,20,20,21}));
            rule.add(new State(21,new String[]{""},new int[]{-1}));

            String textValueNow;
            int[] stateNextNow;
            do{
                judge.check(rule);
                textValueNow = text.get(judge.getIndex());
                stateNextNow = rule.get(judge.getCurrState()).getStateNext();
            }while (!(textValueNow.equals("-1"))&&
                    stateNextNow[0] != -1);                    //修改不方便
            if(text.get(judge.getIndex()).equals("-1")&
                    rule.get(judge.getCurrState()).getStateNext()[0] == -1){
//                System.out.println("The HTML is grammatical!");
                pass.add(file);
                //打印http
                ArrayList<Integer> quotation = new ArrayList<Integer>(0);
//                for(int j = 0; j < text.size(); j++){
//                    if(text.get(j).equals("\"")){
//                        quotation.add(j);
//                    }
//                }
                ArrayList<Integer> httpSequence = judge.getHttp();
                for (int j = 0; j < httpSequence.size(); j++ ){
                    int search = httpSequence.get(j)+6;
                    while (!text.get(search).equals("\"")){
                        search++;
                    }
                    quotation.add(httpSequence.get(j)+5);
                    quotation.add(search);
                }

                for(int q = 0; q < quotation.size(); q+=2){
                    int begin = quotation.get(q);
                    int end = quotation.get(q+1);
                    for(int d = begin+1; d<end; d++){
                        System.out.printf(text.get(d));
                    }
                    System.out.printf("\n");
                }
            }else if(rule.get(judge.getCurrState()).getStateNext()[0] != -1){
//                System.out.println("The HTML is not grammatical!");
                fail.add(file);
            }else{
                ArrayList<String> leftText = new ArrayList<String>();
                for(int i = judge.getIndex(); i < text.size(); i++){
                    leftText.add(text.get(i));
                }
                for(int i = 0; i<leftText.size(); i++){
                    if(leftText.get(i).equals("-1")){
//                        System.out.println("The HTML is grammatical!");
                        pass.add(file);

                        //打印http
                        ArrayList<Integer> quotation = new ArrayList<Integer>(0);
//                for(int j = 0; j < text.size(); j++){
//                    if(text.get(j).equals("\"")){
//                        quotation.add(j);
//                    }
//                }
                        ArrayList<Integer> httpSequence = judge.getHttp();
                        for (int j = 0; j < httpSequence.size(); j++ ){
                            int search = httpSequence.get(j)+6;
                            while (!text.get(search).equals("\"")){
                                search++;
                            }
                            quotation.add(httpSequence.get(j)+5);
                            quotation.add(search);
                        }

                        for(int q = 0; q < quotation.size(); q+=2){
                            int begin = quotation.get(q);
                            int end = quotation.get(q+1);
                            for(int d = begin+1; d<end; d++){
                                System.out.printf(text.get(d));
                            }
                            System.out.printf("\n");
                        }
//                        ArrayList<Integer> quotation = new ArrayList<Integer>();
//                        for(int j = 0; j < text.size(); j++){
//                            if(text.get(j).equals("\"")){
//                                quotation.add(j);
//                            }
//                        }
//                        for(int q = 0; q < quotation.size(); q+=2){
//                            int begin = quotation.get(q);
//                            int end = quotation.get(q+1);
//                            for(int d = begin+1; d<end; d++){
//                                System.out.printf(text.get(d));
//                            }
//                            System.out.printf("\n");
//                        }
                    }
                    else if(leftText.get(i).equals("\t")){continue;}
                    else if(leftText.get(i).equals(" ")){continue;}
                    else if(leftText.get(i).equals("\r")){
                        if(leftText.get(i+1).equals("\n")){i++;}
                        else{
//                            System.out.println("The HTML is not grammatical!");
                            fail.add(file);
                            break;
                        }
                    }
                    else{
//                        System.out.println("The HTML is not grammatical!");
                        fail.add(file);
                        break;
                    }
                }
            }
        }
        if(fail.size() == 0){
            System.out.println("No bug found!");
        }else {
            System.out.println("In files below:");
            for(File failT: fail){
                System.out.println(failT);
            }
            System.out.println("There may be some problems!");
        }
    }
}
