package ca.jrvs.apps.grep;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JavaGrepImp implements JavaGrep {

    final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

    private String regex;
    private String rootPath;
    private String outFile;

    public static void main(String[] args) {
        if (args.length != 3){
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        //Use default logger config
        BasicConfigurator.configure();

        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setOutFile(args[2]);

        try {
            javaGrepImp.process();
        } catch (IOException ex) {
            javaGrepImp.logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void process() throws IOException {
        List matchedLines = new ArrayList<String>();
        for (File file : listFiles(getRootPath())) {
            for (String line : readLines(file)) {
                if (containsPattern(line)) {
                    matchedLines.add(line);
                }
            }
        }
        writeToFile(matchedLines);
    }

    @Override
    public List<File> listFiles(String rootDir) {
        List<File> fileList = new ArrayList<File>();
        Queue<File> fileQueue = new LinkedList<File>();
        fileQueue.add(new File(rootDir));
        while (!fileQueue.isEmpty()) {
            File currentFile = fileQueue.remove();
            if (currentFile.isFile()) {
                fileList.add(currentFile);
            } else {
                for (File file : currentFile.listFiles()) {
                    fileQueue.add(file);
                }
            }
        }
        return fileList;
    }

    @Override
    public List<String> readLines(File inputFile) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(inputFile));
            List<String> lines = new ArrayList<>();
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean containsPattern(String line) {
        Pattern pattern = Pattern.compile(this.getRegex());
        Matcher patternMatcher = pattern.matcher(line);
        return patternMatcher.matches();
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        File outputFile = new File(this.getOutFile());
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        try (BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            for (String line : lines) {
                bufferWriter.write(line);
                bufferWriter.newLine();
            }
        } catch (IOException ex) {
            logger.error("Failed to create buffered writer", ex);
        }
    }

    @Override
    public String getRegex() {
        return regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getOutFile() {
        return outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

}