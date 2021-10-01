import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ServerLog {
    // Run main method, before you may want to change to error message
    public static void main(String[] args) throws IOException {
        //Here is the path to the file
        File file = new File(System.getProperty("user.dir")+"\\src\\main\\resources\\Server.log");
        System.out.println();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        //Storing into Array List
        ArrayList<String> arrayList = new ArrayList<String>();
        while ((st = br.readLine()) != null) {
            arrayList.add(st.trim());
        }
        // Here we can change any error message
        String errorMessege = "500 Server error";

        calculateString(arrayList, errorMessege);

    }

    public static void calculateString(ArrayList<String> arrayList, String error) {
        int countOccurances = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).equals(error)) {
                countOccurances++;
                if (i + 5 < arrayList.size()) {
                    for (int j = i; j < i + 5; j++) {
                        System.out.println(arrayList.get(j));
                    }
                }
            }
        }
        System.out.println("Total number of occurrence's are " + countOccurances);
    }
}
