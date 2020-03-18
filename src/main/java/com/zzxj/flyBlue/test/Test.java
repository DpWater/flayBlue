package com.zzxj.flyBlue.test;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CancellationException;

/**
 * Created by Administrator on 2019/12/17.
 */
public class Test {
    public static void main(String[] args) throws Exception {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("zzzz");
        stringBuffer.append("\r\n");
        stringBuffer.append("zzzz");
        System.out.println(stringBuffer);
    }

    public static int getMonthSpace(String date1, String date2) throws ParseException {

        int result = 0;

         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

         Calendar c1 = Calendar.getInstance();
         Calendar c2 = Calendar.getInstance();

         c1.setTime(sdf.parse(date1));
         c2.setTime(sdf.parse(date2));
        int i = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        result = i*12+c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);

         return result == 0 ? 1 : Math.abs(result);
    }
}
