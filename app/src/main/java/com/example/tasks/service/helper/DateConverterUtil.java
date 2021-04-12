package com.example.tasks.service.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverterUtil {
    private static final SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public static String dateInternationalToPTBR(String value){
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(value);
            assert date != null;
            return mDateFormat.format(date);
        } catch (ParseException e) {
            return "--";
        }

    }
}
