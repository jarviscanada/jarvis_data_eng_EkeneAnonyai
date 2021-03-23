package ca.jrvs.apps.jdbc.grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JavaGrepLambdaImp extends JavaGrepImp {

    public static void main(String[] args) {
        if (args.length != 3) {

            //creating JavaGrepLambdaImp instead of JavaGrepImp
            //JavaGrepLambdaImp inherits all methods except two override methods in
            JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
            javaGrepLambdaImp.setRegex(args[0]);
            javaGrepLambdaImp.setRootPath(args[1]);
            javaGrepLambdaImp.setOutFile(args[2]);

            try {
                //calling parent method,
                //but it will call override method (in this class)
                javaGrepLambdaImp.process();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public List<String> readLines(File inputFile) {
        if (!inputFile.isFile()) {
            throw new IllegalArgumentException("Error: inputFile was not a file");
        }
        List<String> lines = new ArrayList<String>();
        try {
            Files.lines(inputFile.toPath()).forEach(l -> lines.add(l));
        } catch (IOException ex) {
            logger.error("Failed to open file", ex);
        }
        return lines;
    }

    @Override
    public List<File> listFiles(String rootDir) {
        List<File> fileList = new ArrayList<>();
        try {
            Files.walk(Paths.get(rootDir)).filter(Files::isRegularFile)
                    .forEach(f -> fileList.add(f.toFile()));
        } catch (IOException ex) {
            logger.error("Failed to open file", ex);
        }
        return fileList;
    }

}
