package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element href = td.child(0);
            System.out.println(href.attr("href"));
            System.out.println(href.text());
            Element date = td.parent().child(5);
            dateConverter(date);
            System.out.println(date.text() + System.lineSeparator());
        }
    }
    public static void dateConverter(Element element) throws ParseException {
        String stringDate = element.text();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMM yy, HH:mm",
                new Locale("ru", "RU"));
        LocalDate date = LocalDate.parse(stringDate, dateFormat);
        System.out.println(date);
    }
}

//        Elements elements = doc.getElementsByClass("forumTable").get(0).getElementsByTag("tr");
//        for (Element e : elements) {
//            System.out.println(e.child(5).text());
//        }