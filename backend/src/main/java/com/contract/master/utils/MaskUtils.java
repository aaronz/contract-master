package com.contract.master.utils;

import org.apache.commons.lang3.StringUtils;

public class MaskUtils {
    public static String mask(String input, String type) {
        if (StringUtils.isBlank(input)) return input;
        switch (type) {
            case "PHONE":
                return input.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            case "ID_CARD":
                return input.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1**********$2");
            default:
                return "******";
        }
    }
}
