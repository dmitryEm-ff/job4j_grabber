package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element href = td.child(0);
            System.out.println(href.attr("href"));
            System.out.println(href.text());
            Element date = td.parent().child(5);
            System.out.println(dateConverter(date));
            System.out.println(date.text() + System.lineSeparator());
        }
    }
    public static LocalDateTime dateConverter(Element element) throws ParseException {
//        String stringDate = element.text();
//        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMM yy, HH:mm",
//                new Locale("ru", "RU"));
//        LocalDate date = LocalDate.parse(stringDate, dateFormat);
//        System.out.println(date);
        String[] strings = element.text().split(", ");
        if (strings[0].contains("сегодня")) {
            return LocalDateTime.of(LocalDate.now(), LocalTime.parse(strings[1]));
        } else if (strings[0].contains("вчера")) {
            return LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.parse(strings[1]));
        } else {
            return null;
        }

    }
}

//        Elements elements = doc.getElementsByClass("forumTable").get(0).getElementsByTag("tr");
//        for (Element e : elements) {
//            System.out.println(e.child(5).text());
//        }