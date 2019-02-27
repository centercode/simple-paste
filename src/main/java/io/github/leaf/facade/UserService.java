package io.github.leaf.facade;

public interface UserService {

    LeafBag getLeafBag(String userId);

    void modify(LeafBag leafBag);
}
