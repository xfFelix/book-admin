package com.mybatis.mybaties.web;

import com.mybatis.mybaties.service.BookListService;
import com.mybatis.mybaties.utils.CommonUtil;
import com.mybatis.mybaties.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/booklist")
public class BookListController {

    @Autowired
    private BookListService bookListService;

    @Value("${book.xin_bi_qu}")
    private String XIN_BI_QU;

    @Value("${book.jing_cai}")
    private String JING_CAI;

    @Value("${book.quan_ben}")
    private String QUAN_BEN;

    @Value("${book.mian_hua_tang}")
    private String MIAN_HUA_TANG;

    @PostMapping("findall")
    public ResultUtil findAll(@RequestBody Map<String, Object> map){
        Map<String,Object> initMap = new HashMap<String, Object>(5);
        initMap.put("start", null);
        initMap.put("limit", null);
        initMap.put("name", null);
        initMap.put("sort", null);
        initMap.put("status", null);
        initMap.put("parentId", null);

        if (map.containsKey("status") && map.get("status").toString() !=  "") {
            Integer status = Integer.parseInt(map.get("status").toString());
            initMap.put("status", status);
        }
        if (map.containsKey("name")) {
            String name = map.get("name").toString();
            initMap.put("name", name);
        }
        if (map.containsKey("parentId")) {
            String parentId = map.get("parentId").toString();
            initMap.put("parentId", parentId);
        }
        if (map.containsKey("sort")) {
            String sort = map.get("sort").toString();
            initMap.put("sort", sort);
        }
        Integer page = Integer.parseInt(map.get("page").toString());
        Integer limit = Integer.parseInt(map.get("limit").toString());
        Integer start = CommonUtil.getPageStart(page,limit);
        initMap.put("start", start);
        initMap.put("limit", limit);
        ResultUtil resultUtil = bookListService.findAll(initMap);
        return resultUtil;
    }

    @PostMapping("addbook")
    public ResultUtil addBook(@RequestBody Map<String, String> map){
        String identify = map.get("identify");
        String urlId = map.get("urlId");
        ResultUtil resultUtil = new ResultUtil();
        if (urlId.equals(XIN_BI_QU)) {
            if (!identify.startsWith("/")) {
                identify = "/" + identify;
            }
            if (!identify.endsWith("/")) {
                identify = identify + "/";
            }
            resultUtil = bookListService.addXinBiQuBook(identify,urlId);
        } else if (urlId.equals(JING_CAI)) {
            if (!identify.startsWith("/")) {
                identify = "/" + identify;
            }
            if (!identify.endsWith(".html")) {
                identify = identify + ".html";
            }
            resultUtil = bookListService.addJingCaiBook(identify,urlId);
        } else if (urlId.equals(QUAN_BEN)) {
            if (!identify.startsWith("/")) {
                identify = "/" + identify;
            }
            resultUtil = bookListService.addQuanBenBook(identify,urlId);
        } else if (urlId.equals(MIAN_HUA_TANG)) {
            if (identify.startsWith("/")) {
                identify = identify.substring(1);
            }
            resultUtil = bookListService.addMianHuaTang(identify, urlId);
        }
        return resultUtil;
    }

    @PostMapping("updatestatus")
    public ResultUtil updateStatus(@RequestBody Map<String, Integer> map) {
        Integer id = map.get("id");
        ResultUtil resultUtil = bookListService.updateStaus(id);
        return resultUtil;
    }

    @GetMapping("updatebook")
    public ResultUtil updateBook(@RequestParam String urlId, @RequestParam Integer id) {
        ResultUtil resultUtil = bookListService.updateBook(urlId, id);
        return resultUtil;
    }
}
