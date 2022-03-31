package mx.itam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class TestCSV {
    public static StringBuilder sb = new StringBuilder();
    public static String nombreCsv = "";

    public static void escribeCSV(String mensaje, String archivo){
        try (PrintWriter writer = new PrintWriter(new File(archivo))) {
            sb.append(mensaje);
            writer.write(sb.toString());
            writer.close();

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        TestCSV testCSV =  new TestCSV();
        testCSV.readCSVFile(archivo);
    }

    public void readCSVFile(String archivo){
        List<List<String>> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(archivo));) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(records.toString());
    }
    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }
}
