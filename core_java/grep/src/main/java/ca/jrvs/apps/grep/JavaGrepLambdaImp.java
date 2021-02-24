//package ca.jrvs.apps.grep;
//
//import java.io.File;
//import java.util.List;
//
//public class JavaGrepLambdaImp extends JavaGrepImp{
//
//    public static void main(String[] args) {
//        if (args.length != 3) {
//
//            //creating JavaGrepLambdaImp instead of JavaGrepImp
//            //JavaGrepLambdaImp inherits all methods except two override methods in
//            JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
//            javaGrepLambdaImp.setRegex(args[0]);
//            javaGrepLambdaImp.setRootPath(args[1]);
//            javaGrepLambdaImp.setOutFile(args[2]);
//
//            try {
//                //calling parent method,
//                //but it will call override method (in this class)
//                javaGrepLambdaImp.process();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * Implement using lambda and stream APIs
//     */
//    @Override
//    public List<String> readlines(File inputFile){
//
//    }
//
//    /**
//     * Implement using lambda and stream APIs
//     */
//    @Override
//    public List<File> listFiles(String rootDir) {
//
//    }
//}
