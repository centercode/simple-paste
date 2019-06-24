package io.github.leaf.impl;

import io.github.leaf.facade.Leaf;
import io.github.leaf.facade.LeafService;
import io.github.leaf.facade.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LeafServiceImpl implements LeafService {

    private static StorageService storageService;

    @Autowired
    public LeafServiceImpl(StorageService storageService) {
        this.storageService = storageService;
    }

    private static final char[] BASE_LITERAL = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

    private String createId() {
        Long id = storageService.getIncrementId();
        return to36BasedNumber(id);
    }

    String to36BasedNumber(long id) {
        int base = BASE_LITERAL.length;

        StringBuilder sb = new StringBuilder();

        int n;
        while (base <= id) {
            n = (int) (id % base);
            sb.append(BASE_LITERAL[n]);
            id = id / base;
        }
        sb.append(BASE_LITERAL[(int) id]);

        return sb.reverse().toString();
    }

    @Override
    public String save(Leaf leaf) {
        if (null == leaf) {
            return "";
        }

        String id = createId();
        Date now = new Date();
        leaf.setId(id);
        leaf.setCreateTime(now);

        storageService.create(leaf);
        return id;
    }

    @Override
    public void delete(String id) throws Exception {
        storageService.delete(id);
    }

    @Override
    public Leaf get(String id) {
        Leaf leaf = storageService.get(id);
        if (null != leaf) {
            Date expire = storageService.getExpire(id);
            leaf.setEndTime(expire);
        }

        return leaf;
    }

    @Override
    public void delay(String id) {
        Date expire = storageService.getExpire(id);
        if (null == expire) {
            return;
        }

        //delay 2 hours
        long l = expire.getTime() + 1000 * 3600 * 2;
        Date date = new Date(l);

        storageService.setExpireAt(id, date);
    }

    @Override
    public Date getExpire(String id) {
        return storageService.getExpire(id);
    }
}