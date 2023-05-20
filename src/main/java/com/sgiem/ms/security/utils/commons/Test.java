package com.sgiem.ms.security.utils.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Test {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(new Date());
        System.out.println(formattedDate);

        Date date = dateFormat.parse(formattedDate);
        System.out.println("date = " + date);
        
        Date dt = new Date();
        LocalDate localDate = dt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        System.out.println("localDate = " + localDate);

        System.out.println("Hola " + Commons.validateDate(null));
    }
}
