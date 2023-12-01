import java.io.File;
import java.util.*;



public class fileReader {
    int worldsize_file;
    String type;
    protected ArrayList<String[]> inputLines;
    int min;
    int max;


    public void Reader() throws Exception {
        File file = new File("data/InputFiles_UGE1/t1-2b.txt");
        Scanner s = new Scanner(file);


            worldsize_file = s.nextInt();
            s.nextLine();
            while (s.hasNext()){
                inputLines.add(s.nextLine().split("\\W+"));
            }

            for(int i = 0; i<inputLines.size(); i++){
                if(inputLines.get(i).length==2){
                    inputLines.get(i)[2] = inputLines.get(i)[1];
                }
            }
    }
}

