package org.example.file;

import java.io.*;
import java.util.*;

public final class FileUtils {

    public static Map<String, Integer> loadDataIntoMap(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Map<String, Integer> res = new HashMap<>();
            String line = reader.readLine();
            while (line != null) {
                String[] split = line.split(":");
                res.put(split[0], Integer.parseInt(split[1]));
                line = reader.readLine();
            }
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Set<String> loadDataIntoSet(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Set<String> res = new HashSet<>();
            String line = reader.readLine();
            while (line != null) {
                res.add(line);
                line = reader.readLine();
            }
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveMapToFile(File file, Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
                writer.write(String.format("%s:%d\n", entry.getKey(), entry.getValue()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveSetToFile(File file, Set<String> set){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            for(String line : set){
                writer.write(line.concat("\n"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
