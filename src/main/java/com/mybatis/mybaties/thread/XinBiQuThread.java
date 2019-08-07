package com.mybatis.mybaties.thread;

import com.mybatis.mybaties.config.Bean.ApplicationContextProvider;
import com.mybatis.mybaties.dao.BookInfoDao;
import com.mybatis.mybaties.dao.BookListDao;
import com.mybatis.mybaties.entity.BookInfo;
import com.mybatis.mybaties.entity.BookList;
import com.mybatis.mybaties.utils.CommonUtil;
import com.mybatis.mybaties.utils.FileManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XinBiQuThread extends Thread {

    private String baseUrl;

    private Document document;

    private int parentId;

    private BookInfoDao bookInfoDao;

    private BookListDao bookListDao;

    private String FILE_PATH = "D:\\TXTDownload\\newBiQu";

    public XinBiQuThread(Document document, String baseUrl, int parentId){
        this.document = document;
        this.baseUrl = baseUrl;
        this.parentId = parentId;
        this.bookInfoDao = ApplicationContextProvider.getBean(BookInfoDao.class);
        this.bookListDao = ApplicationContextProvider.getBean(BookListDao.class);
    }

    @Override
    public void run() {
        FileWriter fw = null;
        BufferedWriter bw = null;
        BookInfo bookInfo = new BookInfo();
        try {
            List<Element> list = document.getElementsByTag("dd");
            // 书名
            String name = document.getElementsByTag("h1").get(0).text();
            // 章节数量
            int count = list.size();

            File myPath = new File(FILE_PATH);
            if (!myPath.exists() && !myPath.isDirectory()) {
                myPath.mkdirs();
                System.out.println("创建的文件夹的路径为：" + FILE_PATH);
            }
            fw = new FileWriter(FILE_PATH + "\\" + name + ".txt",true);
            bw = new BufferedWriter(fw);

            for (int i = checkMaxNum(); i < list.size(); i++) {
                Element element = list.get(i).child(0);
                String url = element.attr("href");
                // 章节标题
                String title = element.text();
                // 章节HTML
                document = Jsoup.connect(baseUrl + url).get();
                // 章节内容
                String content = document.select("div[id=content]").text();

                bookInfo.setName(name);
                bookInfo.setNum(i);
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
                System.out.println(Thread.currentThread().getName()+ "：" + name + "已完成" + i + "章");
            }
            System.out.println(Thread.currentThread().getName()+":线程结束");
            FileInputStream in_file = new FileInputStream(myPath + "\\" + name + ".txt");
            MultipartFile multipartFile = new MockMultipartFile(name + ".txt", in_file);
            String downloadUrl = FileManager.saveFile(multipartFile);
            Map<String, Object> map = new HashMap<>();
            map.put("id", parentId);
            map.put("downloadUrl", downloadUrl);
            bookListDao.updateUrlById(map);
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
