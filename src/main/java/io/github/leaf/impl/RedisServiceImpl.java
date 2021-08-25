package io.github.leaf.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import io.github.leaf.facade.Leaf;
import io.github.leaf.facade.LeafBag;
import io.github.leaf.facade.StorageService;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RedisServiceImpl implements StorageService {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    private static final String LEAF_PREFIX = "leaf:note:";

    private static final String BAG_PREFIX = "leaf:bag:";

    private static final String CURRENT_ID = "leaf:CURRENT_ID";

    private RedisCommands<String, String> redis;

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private String port;

    @Value("${redis.password}")
    private String password;

    @Value("${redis.database}")
    private String database;

    @PostConstruct
    public void init() {
        String uri = "redis://:" + password + "@" + host + ":" + port + "/" + database;
//        logger.info("redis uri:" + uri.replaceAll(password));
        RedisClient client = RedisClient.create(uri);
        StatefulRedisConnection<String, String> connect = client.connect();
        redis = connect.sync();
    }

    @Override
    public Long getIncrementId() {
        return redis.incr(CURRENT_ID);
    }

    @Override
    public Leaf get(String id) {
        String key = getLeafKey(id);
        Map<String, String> map = redis.hgetall(key);
        Leaf leaf = fromMap(map);
        if (null == leaf) {
            return null;
        }

        leaf.setId(id);

        return leaf;
    }

    @Override
    public void create(Leaf leaf) {
        String id = leaf.getId();

        String key = getLeafKey(id);

        Map<String, String> map = toMap(leaf);

        redis.hmset(key, map);
        redis.expire(key, DEFAULT_EXPIRE_SEC);
    }

    private Map<String, String> toMap(Leaf leaf) {
        if (null == leaf) {
            return Collections.emptyMap();
        }

        Map<String, String> map = Maps.newHashMap();
        map.put(Fields.CREATE_TIME.name(), String.valueOf(leaf.getCreateTime().getTime()));
        map.put(Fields.CONTENT.name(), leaf.getContent());

        return map;
    }

    private Leaf fromMap(Map<String, String> map) {
        if (MapUtils.isEmpty(map)) {
            return null;
        }

        String createTime = map.get(Fields.CREATE_TIME.name());
        String content = map.get(Fields.CONTENT.name());

        Leaf leaf = new Leaf();
        leaf.setCreateTime(new Date(Long.valueOf(createTime)));
        leaf.setContent(content);

        return leaf;
    }

    @Override
    public void delete(String id) throws Exception {
        String key = getLeafKey(id);
        redis.del(key);

    }

    @Override
    public void setExpireAt(String id, Date date) {
        String key = getLeafKey(id);
        redis.expireat(key, (int) (date.getTime() / 1000));
    }

    @Override
    public Date getExpire(String id) {
        String key = getLeafKey(id);
        Long ttl = redis.ttl(key);
        if (null != ttl && 0 < ttl) {
            long time = ttl * 1000 + System.currentTimeMillis();
            return new Date(time);
        }

        return null;
    }

    private String getLeafKey(String id) {
        if (null == id) {
            return null;
        }

        return LEAF_PREFIX + id;
    }

    private String getLeafBagKey(String id) {
        if (null == id) {
            return null;
        }

        return BAG_PREFIX + id;
    }

    @Override
    public void modify(LeafBag leafBag) {
        if (null == leafBag
                || StringUtils.isBlank(leafBag.getUserId())) {
            return;
        }

        String leafBagKey = getLeafBagKey(leafBag.getUserId());

        String value = StringUtils.join(leafBag.getLeafIds(), ",");

        redis.set(leafBagKey, value);
    }

    @Override
    public LeafBag getLeafBag(String userId) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }

        LeafBag leafBag = new LeafBag();
        leafBag.setUserId(userId);

        String leafBagKey = getLeafBagKey(userId);

        String value = redis.get(leafBagKey);
        if (StringUtils.isNotBlank(value)) {
            List<String> leafs = Splitter
                    .on(",")
                    .splitToList(value);
            leafBag.setLeafIds(leafs);
        }

        return leafBag;
    }

    private enum Fields {
        CREATE_TIME, CONTENT
    }
}