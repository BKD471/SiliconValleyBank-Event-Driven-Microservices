package com.siliconvalley.accountsservices.helpers;

public class RegexMatchersHelper {
    public static final String PATTERN_FOR_DOB="^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
    public static final String PATTERN_FOR_EMAIL="^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-]+)(\\.[a-zA-Z]{2,5}){1,2}$";
    public static final String PATTERN_FOR_PHONE_NUMBER="^(\\+\\d{1,2}\\s?)?1?\\-?\\.?\\s?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$";
    public static final String PATTERN_FOR_ADHAR="(^[0-9]{4}[0-9]{4}[0-9]{4}$)|(^[0-9]{4}\\s[0-9]{4}\\s[0-9]{4}$)|(^[0-9]{4}-[0-9]{4}-[0-9]{4}$)";
    public static final String PATTERN_FOR_PAN_NUMBER="^([a-zA-Z]){5}([0-9]){4}([a-zA-Z]){1}?$";
    public static final String PATTERN_FOR_VOTER="^[A-Z]{3}[0-9]{7}$";
    public static final String PATTERN_FOR_DRIVING_LICENSE="(([A-Z]{2}[0-9]{2})( )|([A-Z]{2}-[0-9]{2}))((19|20)[0-9][0-9])[0-9]{7}$";
    public static final String PATTERN_FOR_PASSPORT="^[A-PR-WYa-pr-wy][1-9]\\d\\s?\\d{4}[1-9]$";
    public static final String PATTERN_FOR_PASSWORD="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    public static final String PATTERN_FOR_NOT_NULL_CHARS="^(?!\\s*$).+";
}
