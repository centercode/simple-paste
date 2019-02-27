package io.github.leaf.impl;

import io.github.leaf.facade.LeafBag;
import io.github.leaf.facade.StorageService;
import io.github.leaf.facade.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private StorageService storageService;

    @Autowired
    public UserServiceImpl(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public LeafBag getLeafBag(String userId) {
        return storageService.getLeafBag(userId);
    }

    @Override
    public void modify(LeafBag leafBag) {
        storageService.modify(leafBag);
    }
}
