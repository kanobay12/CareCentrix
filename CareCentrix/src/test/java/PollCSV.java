import com.github.javafaker.Faker;
import com.opencsv.CSVWriter;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import static javax.swing.JOptionPane.showMessageDialog;

public class PollCSV {
    static File file1;
    static File file2;

    public static void main(String[] args) throws IOException, InterruptedException {
        String path = System.getProperty("user.dir") + "\\src\\main\\resources\\A\\file.csv";
        createCSVFile(path);

        Path bFolder = Paths.get(System.getProperty("user.dir") + "\\src\\main\\resources\\B\\");
        monitorFile(bFolder);

    }

    // Create CSV file with code and assign values
    public static void createCSVFile(String path) {
        File file = new File(path);
        try {
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile, '|',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
            List<String[]> data = new ArrayList<>();
            String[] header = {"ID", "LastName", "FirstName", "Gender", "DOB", "Adress"};
            data.add(header);
            for (int i = 0; i < 5; i++) {
                String ID = Integer.toString((int) Math.floor(Math.random() * (2000 - 1000 + 1) + 1000));
                String gender;
                if (new Random().nextBoolean()) {
                    gender = "M";
                } else {
                    gender = "F";
                }
                Faker faker = new Faker();
                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();
                String adress = faker.address().fullAddress();
                String DOB = Integer.toString((int) Math.floor(Math.random() * (2010 - 1950 + 1) + 1950));
                String[] row = {ID, lastName, firstName, gender, DOB, adress};
                data.add(row);
            }
            writer.writeAll(data);
            writer.close();
            file1 = file;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void monitorFile(Path bFolder) throws IOException, InterruptedException {
        File directory = new File(String.valueOf(bFolder));
        //Delete all files if exists
        FileUtils.cleanDirectory(directory);
        WatchService watchService = FileSystems.getDefault().newWatchService();
        bFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        boolean valid;
        do {
            WatchKey watchKey = watchService.take();
            for (WatchEvent event : watchKey.pollEvents()) {
                WatchEvent.Kind kind = event.kind();
                if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
                    String fileName = event.context().toString();
                    if (fileName.equals("file.csv")) {
                        String p2 = System.getProperty("user.dir") + "\\src\\main\\resources\\B\\file.csv";
                        File f2 = new File(p2);
                        Thread.sleep(2000);
                        if (compareContent(file1.toPath(),f2.toPath())) {
                            System.out.println("File Copied:" + fileName);
                            showMessageDialog(null, "Csv delimited File created File Copied:" + fileName);
                        }
                    }
                    break;
                }
            }
            valid = watchKey.reset();
        } while (valid);
    }
  // Compare content of the file and return true false
    public static boolean compareContent(Path file1, Path file2) throws IOException {
        final long size = Files.size(file1);
        if (size != Files.size(file2))
            return false;
        if (size < 4096)
            return Arrays.equals(Files.readAllBytes(file1), Files.readAllBytes(file2));
        try (InputStream is1 = Files.newInputStream(file1);
             InputStream is2 = Files.newInputStream(file2)) {
            // Compare byte-by-byte.
            // Note that this can be sped up drastically by reading large chunks
            // does not neccessarily read a whole array!
            int data;
            while ((data = is1.read()) != -1)
                if (data != is2.read())
                    return false;
        }

        return true;
    }
}
