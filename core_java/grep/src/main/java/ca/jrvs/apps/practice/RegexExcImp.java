package ca.jrvs.apps.jdbc.practice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexExcImp implements RegexExc {

    /**
     * Return true if filename extension is jpg or jpeg (Case insensitive)
     * @param filename
     * @return
     */
    @Override
    public boolean matchJpeg(String filename) {
        Pattern jpegPattern = Pattern.compile("(.+\\.jpg|.+\\.jpeg)$", Pattern.CASE_INSENSITIVE);
        Matcher jpegMatcher = jpegPattern.matcher(filename);
        return jpegMatcher.matches();
    }

    /**
     * Return true if IP is valid
     * To simplify the problem, IP address range is from 0.0.0.0 to 999.999.999.999
     * @param ip
     * @return
     */
    @Override
    public boolean matchIp(String ip) {
        Pattern ipPattern = Pattern.compile("\\d?\\d?\\d\\.\\d?\\d?\\d\\.\\d?\\d?\\d\\.\\d?\\d?\\d");
        Matcher ipMatcher = ipPattern.matcher(ip);
        return ipMatcher.matches();
    }

    /**
     * Return true if line is empty (e.g. empty char, white space, tabs, etc.)
     * @param line
     * @return
     */
    @Override
    public boolean isEmptyLine(String line) {
        Pattern emptyPattern = Pattern.compile("(\\s)*");
        Matcher emptyMatcher = emptyPattern.matcher(line);
        return emptyMatcher.matches();
    }
}