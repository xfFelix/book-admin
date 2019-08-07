package com.mybatis.mybaties.component;

import com.mybatis.mybaties.dao.BookInfoDao;
import com.mybatis.mybaties.dao.BookListDao;
import com.mybatis.mybaties.entity.BookList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@EnableScheduling
public class getBadgeScheduled {

    @Autowired
    private BookListDao bookListDao;

    @Autowired
    private BookInfoDao bookInfoDao;

    @Scheduled(cron = "0 0/15 0 * * ?")
    public void getBadge(){
        List<BookList> list = bookListDao.findAllBookList();
        for (int i = 0; i < list.size(); i++) {
            try {
                Document document = Jsoup.connect(list.get(i).getBookUrlList().getUrl()+list.get(i).getUrl()).get();

                List<Element> els = document.getElementsByTag("dd");
                // 章节数量
                int count = els.size();
                int max = list.get(i).getSize();
                int badge = count - max;
                if (badge > 0) {
                    list.get(i).setBadge(badge);
                    bookListDao.updateBook(list.get(i));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
