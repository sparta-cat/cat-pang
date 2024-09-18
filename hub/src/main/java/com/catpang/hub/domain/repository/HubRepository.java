package com.catpang.hub.domain.repository;

import java.util.UUID;
import org.springframework.stereotype.Repository;
import com.catpang.core.domain.repository.BaseRepository;
import com.catpang.hub.domain.model.Hub;

@Repository
public interface HubRepository extends BaseRepository<Hub, UUID, HubSearchCondition> {
}
