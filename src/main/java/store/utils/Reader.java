package store.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Reader {
    private static final String COMMA = ",";
    private static final ClassLoader classLoader = Reader.class.getClassLoader();

    public static List<List<String>> readFile(final String fileName) {
        try {
            File file = getFile(fileName);
            return readFileInfo(file);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static File getFile(final String fileName) throws FileNotFoundException {
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new FileNotFoundException(fileName + "파일을 찾을 수 없습니다.");
        }
        File file = new File(resource.getFile());
        return file;
    }

    private static List<List<String>> readFileInfo(final File file) throws FileNotFoundException {
        List<List<String>> fileInfo = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        removeHeader(scanner);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            fileInfo.add(Arrays.asList(line.split(COMMA)));
        }
        scanner.close();
        return fileInfo;
    }

    private static void removeHeader(final Scanner scanner) {
        if (scanner.hasNextLine()) {
            scanner.nextLine(); // 헤더 제거
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<List<String>> k = readFile("products.md");
        for (List<String> strings : k) {
            for (String string : strings) {
                System.out.println(string);
            }
        }
    }
}
