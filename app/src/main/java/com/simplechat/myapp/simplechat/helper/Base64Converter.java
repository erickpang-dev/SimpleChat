package com.simplechat.myapp.simplechat.helper;

import android.util.Base64;

public class Base64Converter {

    public static String base64Encode(String textToEncode){
        return Base64.encodeToString(textToEncode.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String base64Decode(String textToDecode){
        return new String (Base64.decode(textToDecode, Base64.DEFAULT));
    }
}
