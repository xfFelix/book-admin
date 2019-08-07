package com.mybatis.mybaties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新笔趣阁小说网爬虫
 */
public class NewBiQu {

    public static void main (String[] args) throws IOException {

        String baseUrl = "https://www.qb5200.tw";
        Document document = null;
        FileWriter fw = null;
        BufferedWriter bw = null;

        try {
            // 列表HTML
            document = Jsoup.connect(baseUrl + "/xiaoshuo/55/55174/").userAgent("Mozilla/17.0").get();

            // 书名
            String name = document.getElementsByTag("h2").get(0).text().replaceFirst("最新章节", "");

            List<Element> list = document.getElementsByTag("dd");
            // 章节数量
            int count = list.size();
            fw = new FileWriter("D:\\" + name + ".txt");
            bw = new BufferedWriter(fw);
            for (int i = 0; i < list.size(); i++) {
                Element element = list.get(i).child(0);
                String url = element.attr("href");
                // 章节标题
                String title = element.text();
                // 章节HTML
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
                headers.put("Accept-Encoding","gzip, deflate, br");
                headers.put("Accept-Language","zh-CN,zh;q=0.9");
                headers.put("Cache-Control","no-cache");
                headers.put("Connection","keep-alive");
                headers.put("Cookie","UM_distinctid=1674df8babf5c-09a7d15b24e92d-4313362-144000-1674df8bac0161; bcolor=; font=; size=; fontcolor=; width=; fikker-8eFS-xQV5=xmFJMsF7TgBgR1xJTyKwZWRYZgtyZwIA; fikker-8eFS-xQV5=xmFJMsF7TgBgR1xJTyKwZWRYZgtyZwIA; CNZZDATA1260750615=1706979025-1543199331-https%253A%252F%252Fwww.baidu.com%252F%7C1543237143");
                headers.put("Host","www.qb5200.tw");
                headers.put("Pragma","no-cache");
                headers.put("Upgrade-Insecure-Requests","1");
                headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");

                document = Jsoup.connect(baseUrl + url)
                        .userAgent("Mozilla/5.0 Chrome/26.0.1410.64 Safari/537.31")
                        .headers(headers)
                        .timeout(30000).maxBodySize(1024*1024*3)
                        .followRedirects(true)
                        .validateTLSCertificates(true)
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .get();
                // 章节内容
                String content = document.select("div[id=content]").text();
                bw.write(title);
                bw.newLine();
                bw.write(content);
                bw.newLine();
                bw.newLine();
                bw.flush();
                Thread.sleep(10000);
                System.out.println("已完成"+ i +"章");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
