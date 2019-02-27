package io.github.leaf.facade;

import java.util.List;

public class LeafBag {

    private String userId;

    private List<String> leafIds;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getLeafIds() {
        return leafIds;
    }

    public void setLeafIds(List<String> leafIds) {
        this.leafIds = leafIds;
    }
}
