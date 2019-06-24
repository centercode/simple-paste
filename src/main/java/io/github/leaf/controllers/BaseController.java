package io.github.leaf.controllers;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    public static final String SUCCESS = "{\"state\":\"success\",\"msg\":\"\"}";

    public static final String ERROR = "{\"state\":\"error\",\"reason\":\"\"}";

    protected Map<String, Object> jsonError(String reason) {
        Map<String, Object> map = new HashMap<>();
        map.put("state", "error");
        map.put("reason", reason);
        return map;
    }


    protected Map<String, Object> jsonSuccess(Object msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("state", "success");
        map.put("msg", msg);
        return map;
    }

    protected String getReqValByParam(HttpServletRequest request, String param) {
        String value = request.getParameter(param);
        return (value == null) ? "" : value;
    }
}
