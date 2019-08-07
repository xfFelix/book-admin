package com.mybatis.mybaties.web;

import com.mybatis.mybaties.entity.HotList;
import com.mybatis.mybaties.service.HotListService;
import com.mybatis.mybaties.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HotListCotroller {

    @Autowired
    private HotListService hotListService;

    @GetMapping("/addhotlist")
    public ResultUtil addHotList(String type) {
        String infoType = "";
        if (type == null) {
            infoType = HotList.TOP;
        } else if (type.equals(HotList.JUNSHI)) {
            infoType = HotList.JUNSHI;
        } else if (type.equals(HotList.YULE)) {
            infoType = HotList.YULE;
        } else if (type.equals(HotList.TIYU)) {
            infoType = HotList.TIYU;
        }else {
            infoType = HotList.TOP;
        }
        ResultUtil result = new ResultUtil();
        result = hotListService.addHotList(infoType);
        return result;
    }

    @PostMapping("/findall")
    public ResultUtil findAll(@RequestBody String test) {
        ResultUtil result = new ResultUtil();
        List<HotList> list = new ArrayList<HotList>();
        list = hotListService.findAll();
        result.setCode(200);
        result.setMsg("获取成功");
        result.setData(list);
        return result;
    }

}
