package io.github.leaf.controllers;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.github.leaf.common.DateUtil;
import io.github.leaf.facade.Leaf;
import io.github.leaf.facade.LeafBag;
import io.github.leaf.facade.LeafService;
import io.github.leaf.facade.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class IndexController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private LeafService leafService;

    private UserService userService;

    @Autowired
    public IndexController(LeafService leafService, UserService userService) {
        this.leafService = leafService;
        this.userService = userService;
    }


    private String getUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        return session.getId();
    }

    @RequestMapping("getLeafs.json")
    public List<String> getLeafs(HttpServletRequest request) {
        List<String> leafIds = Collections.emptyList();

        String userId = getUserId(request);
        LeafBag leafBag = userService.getLeafBag(userId);
        if (null != leafBag) {
            leafIds = leafBag.getLeafIds();
        }

        return leafIds;
    }


    @RequestMapping("save")
    public Map<String, Object> save(HttpServletRequest request) {
        String content = getReqValByParam(request, "content");
        String type = getReqValByParam(request, "type");

        if (StringUtils.isBlank(content)) {
            return jsonError("发布内容不能为空");
        }

        Leaf leaf = new Leaf();
        leaf.setContent(content);

        String id = leafService.save(leaf);
        addLeafToUser(request, id);

        return jsonSuccess(id);
    }

    private void addLeafToUser(HttpServletRequest request, String leafId) {
        try {
            String userId = getUserId(request);
            LeafBag leafBag = userService.getLeafBag(userId);
            if (null == leafBag || CollectionUtils.isEmpty(leafBag.getLeafIds())) {
                leafBag = new LeafBag();
                leafBag.setUserId(userId);
                leafBag.setLeafIds(Collections.singletonList(leafId));
                userService.modify(leafBag);
            } else {
                List<String> leafIds = Lists.newArrayList(leafBag.getLeafIds());
                leafIds.add(leafId);
                leafBag.setLeafIds(leafIds);
                userService.modify(leafBag);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    @RequestMapping("get/{id}")
    public String get(@PathVariable String id) {
        Leaf leaf = leafService.get(id);
        return leaf.getContent();
    }

    @RequestMapping(value = "show.json", produces = "application/json")
    public String showJson(HttpServletRequest request) {
        String id = getReqValByParam(request, "id");
        Leaf leaf = leafService.get(id);

        if (null == leaf || null == leaf.getEndTime()) {
            return null;
        }

        JSONObject json = new JSONObject();
        json.put("leaf", leaf);
        json.put("endTime", format(leaf.getEndTime()));

        return json.toJSONString();
    }

    @RequestMapping("delay/{id}")
    public Map<String, Object> delay(@PathVariable String id) {
        leafService.delay(id);

        Date expire = leafService.getExpire(id);
        String format = format(expire);

        return jsonSuccess(format);
    }

    private String format(Date date) {
        return DateUtil.getFormat("MM月dd日 HH:mm").format(date);
    }

}