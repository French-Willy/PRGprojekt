import java.io.File;
import java.util.*;



public class fileReader {
    int worldsize_file;
    public ArrayList<ArrayList<String>> inputLines = new ArrayList<>();
    public void Reader() throws Exception {
        File file = new File("data/InputFiles_UGE1/tf1-1.txt");
        Scanner s = new Scanner(file);

        //The first line in every file is the world-size, so therefor it's easy to assign it as the first step.
        worldsize_file = s.nextInt();
        //Goes to the next line
        s.nextLine();

        //Reads each line and inserts it in a arraylist in a arraylist.
        while (s.hasNext()){
            //Splits the sentence at " " & "-"
            ArrayList<String> temp = new ArrayList<>(Arrays.asList(s.nextLine().split("\\W+")));
            inputLines.add(temp);
        }

        //If there is only one number to the assigned spawn object, then it sets the number for both min and max value.
        for (ArrayList<String> inputLine : inputLines) {
            if (inputLine.size() == 2) {
                inputLine.add(inputLine.get(1));
            }
        }
    }
}

