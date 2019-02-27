package io.github.leaf.facade;

import java.util.Date;

public interface LeafService {

    String save(Leaf leaf);

    void delete(String id) throws Exception;

    Leaf get(String id);

    void delay(String id);

    Date getExpire(String id);
}