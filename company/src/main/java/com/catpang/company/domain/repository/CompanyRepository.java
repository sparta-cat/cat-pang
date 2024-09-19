package com.catpang.company.domain.repository;

import java.util.UUID;
import org.springframework.stereotype.Repository;
import com.catpang.core.domain.repository.BaseRepository;
import com.catpang.company.domain.model.Company;

@Repository
public interface CompanyRepository extends BaseRepository<Company, UUID, CompanySearchCondition> {
}
