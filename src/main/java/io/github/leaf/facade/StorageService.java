package io.github.leaf.facade;

import java.util.Date;

public interface StorageService {

    int DEFAULT_EXPIRE_SEC = 24 * 3600;

    Long getIncrementId();

    Leaf get(String id);

    void create(Leaf leaf);

    void delete(String id) throws Exception;

    void setExpireAt(String id, Date date);

    Date getExpire(String id);

    //create or update
    void modify(LeafBag leafBag);

    LeafBag getLeafBag(String userId);
}