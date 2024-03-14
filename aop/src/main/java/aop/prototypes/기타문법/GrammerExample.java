package aop.prototypes.기타문법;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class GrammerExample {

    public static void main(String[] args) {
        SpringApplication.run(GrammerExample.class, args);
        convert();
    }

    static void convert() {
        String str = "2024-03-19 13:34:04.996977";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("date whole : "+dateTime.format(formatter2));
        System.out.println( "hour : "+ dateTime.getHour());
        System.out.println( "minute : "+ dateTime.getMinute());
        System.out.println( "second : "+ dateTime.getSecond());
    }

}
