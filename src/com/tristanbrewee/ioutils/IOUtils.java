package com.tristanbrewee.ioutils;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.DoubleStream;

public class IOUtils {

    public static void doubleArrayToFile(double[] doubleArray){
        String filename = JOptionPane.showInputDialog("Filename for output file:");
        String filePath = "src/com/tristanbrewee/output/" + filename;
        StringBuilder stringBuilder = new StringBuilder();
        DoubleStream doubleStream = DoubleStream.of(doubleArray);
        doubleStream
                .forEachOrdered(e -> stringBuilder.append(e + ";"));
        String output = stringBuilder.toString();
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(output);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Double> doubleArrayListFromFile(String filename){
        String path = "src/com/tristanbrewee/output/" + filename;
        File file = new File(path);
        ArrayList<Double> doubles = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)){
            scanner.useDelimiter(";");
            while (scanner.hasNext()){
                double number = Double.parseDouble(scanner.next());
                doubles.add(number);
            }
            return doubles;
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }
}
