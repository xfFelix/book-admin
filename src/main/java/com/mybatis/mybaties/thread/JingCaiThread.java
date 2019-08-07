package com.mybatis.mybaties.thread;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class JingCaiThread extends Thread {

    private Document document;

    private String baseUrl;

    public JingCaiThread (Document document, String baseUrl){
        this.document = document;
        this.baseUrl = baseUrl;
    }

    @Override
    public void run() {
        FileWriter fw = null;
        BufferedWriter bw = null;

        try {
            // 书名
            String name = document.select("h1").get(0).text();

            List<Element> list = document.select("a[class=text-danger]");
            Element element = list.get(1);
            String string = element.attr("href");

            Elements el = document.select("dl[class=panel-body panel-chapterlist]").get(1).getElementsByTag("a");
            fw = new FileWriter("D:\\"+ name +".txt", false);
            bw = new BufferedWriter(fw);
            for (int i = 0; i < el.size(); i++) {
                String href = el.get(i).attr("href");
                document = Jsoup.connect(baseUrl+ href).get();
                String title = document.select("li[class=active]").text();
                String text = document.select("div[id=htmlContent]").text();
                bw.write(title);//添加标题
                bw.newLine();//换行
                bw.write(text);//添加内容
                bw.newLine();
                bw.newLine();
                bw.flush();//清空缓冲区
                System.out.println(Thread.currentThread().getName() + ": 已下载"+ i+ "章节");
            }
            System.out.println(Thread.currentThread().getName()+":线程结束");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
