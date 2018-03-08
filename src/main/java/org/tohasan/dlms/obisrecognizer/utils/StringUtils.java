package org.tohasan.dlms.obisrecognizer.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author LehaSan
 * date 06.03.2018
 */
public class StringUtils {
    public static String getSubstringByRegexp(String originStr, String regexp) {
        final Pattern pattern = Pattern.compile(regexp);
        final Matcher matcher = pattern.matcher(originStr);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }
}
