package com.mybatis.mybaties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class Text {
    public static void main (String[] args) {
        Document document = null;
        FileWriter fw = null;
        BufferedWriter bw = null;

        try {
            document = Jsoup.connect("http://www.jingcaiyuedu.com/book/59396.html").get();
            // 书名
            String name = document.select("h1").get(0).text();

            List<Element> list = document.select("a[class=text-danger]");
            Element element = list.get(1);
            String string = element.attr("href");
            String[] numlist = string.split("\\.")[0].split("\\/");
            // 章节
            Integer num = Integer.valueOf(numlist[numlist.length - 1]);

            Elements el = document.select("dl[class=panel-body panel-chapterlist]").get(1).getElementsByTag("a");
            fw = new FileWriter("D:\\"+ name +".txt", false);
            bw = new BufferedWriter(fw);
            for (int i = 0; i < el.size(); i++) {
                System.out.println(el.get(i).text());
                String href = el.get(i).attr("href");
                document = Jsoup.connect("http://www.jingcaiyuedu.com/book/59396/" + i + ".html").get();
                String title = document.select("li[class=active]").text();
                String text = document.select("div[id=htmlContent]").text();
                bw.write(title);//添加标题
                bw.newLine();//换行
                bw.write(text);//添加内容
                bw.newLine();
                bw.newLine();
                bw.flush();//清空缓冲区
            }

//            for (Integer i = 0; i < num; i++) {
//                document = Jsoup.connect("http://www.jingcaiyuedu.com/book/59396/" + i + ".html").get();
//                String title = document.select("li[class=active]").text();
//                String text = document.select("div[id=htmlContent]").text();
//                bw.write(title);//添加标题
//                bw.newLine();//换行
//                bw.write(text);//添加内容
//                bw.newLine();
//                bw.newLine();
//                bw.flush();//清空缓冲区
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
//                fw.close();
//                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
