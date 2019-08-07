package com.mybatis.mybaties.thread;

import com.mybatis.mybaties.config.Bean.ApplicationContextProvider;
import com.mybatis.mybaties.dao.BookInfoDao;
import com.mybatis.mybaties.entity.BookInfo;
import com.mybatis.mybaties.utils.CommonUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class QuanBenThread extends Thread{
    private String baseUrl;

    private Document document;

    private int parentId;

    private BookInfoDao bookInfoDao;

    public QuanBenThread(Document document, String baseUrl, int parentId){
        this.document = document;
        this.baseUrl = baseUrl;
        this.parentId = parentId;
        this.bookInfoDao = ApplicationContextProvider.getBean(BookInfoDao.class);
    }

    @Override
    public void run() {
        FileWriter fw = null;
        BufferedWriter bw = null;
        BookInfo bookInfo = new BookInfo();
        try {
            List<Element> list = document.getElementsByTag("dd");
            // 书名
            String name = document.getElementsByTag("h2").get(0).text().replaceFirst("最新章节","");
            // 章节数量
            int count = list.size();

            fw = new FileWriter("D:\\" + name + ".txt",true);
            bw = new BufferedWriter(fw);

            for (int i = checkMaxNum() + 9; i < list.size(); i++) {
                Element element = list.get(i).child(0);
                String url = element.attr("href");
                // 章节标题
                String title = element.text();
                // 章节HTML
                document = Jsoup.connect(baseUrl + url).get();
                // 章节内容
                String content = document.select("div[id=content]").text();

                bookInfo.setName(name);
                bookInfo.setNum(i-9);
                bookInfo.setCount(list.size());
                bookInfo.setContent(content);
                bookInfo.setTitle(title);
                bookInfo.setCreateTime(new Date());
                bookInfo.setUpdateTime(new Date());
                bookInfo.setFontSize(content.length());
                bookInfo.setParentId(parentId);
                bookInfo.setStatus(CommonUtil.STATUS_SAVE);
                int res = bookInfoDao.addBookInfo(bookInfo);

                bw.write(title);
                bw.newLine();
                bw.write(content);
                bw.newLine();
                bw.newLine();
                bw.flush();
                System.out.println(Thread.currentThread().getName()+ "：" + name + "已完成" + (i-9) + "章");
            }
            System.out.println(Thread.currentThread().getName()+":线程结束");
        } catch (IOException e) {
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

    public Integer checkMaxNum() {
        Integer max = bookInfoDao.findMaxNum(parentId);
        if (max == null) {
            return 0;
        }
        return max + 1;
    }
}
