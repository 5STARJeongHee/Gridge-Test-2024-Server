package com.example.demo.utils;

import com.example.demo.common.PasswordValidStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {

    public static boolean isRegexNickName(String target){
        String regex = "^[a-zA-Z0-9_!.]{4,20}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }
    public static boolean isRegexEmail(String target) {
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    public static boolean isRegexPhone(String target){
        String koreaRegex = "^\\+(82)(010|011|016|017|018|019|10|11|16|17|18|19)\\d{3,4}\\d{4}$";
        Pattern pattern = Pattern.compile(koreaRegex);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    public static PasswordValidStatus isPasswordValid(String password){
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{7,20}$";
        String duplicateRegex = "(\\w)\\1\\1\\1\\1";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        Pattern duplicatePattern = Pattern.compile(duplicateRegex);

        if(!pattern.matcher(password).matches()){
            return PasswordValidStatus.FORMAT_NOT_VALID;
        }
        if(duplicatePattern.matcher(password).find()){
            return PasswordValidStatus.DUPLICATE;
        }
        if(continuousPwd(password)){
            return PasswordValidStatus.CONTINUOS;
        }
        return PasswordValidStatus.VALID;
    }

    private static boolean continuousPwd(String pwd) {
        int o = 0;
        int d = 0;
        int p = 0;
        int n = 0;
        int limit = 4;

        for(int i=0; i<pwd.length(); i++) {
            char tempVal = pwd.charAt(i);
            if(i > 0 && (p = o - tempVal) > -2 && (n = p == d ? n + 1 :0) > limit -3) {
                return true;
            }
            d = p;
            o = tempVal;
        }
        return false;
    }
}

