package com.contract.master.domain;

import com.contract.master.domain.DownstreamSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DownstreamSystemRepository extends JpaRepository<DownstreamSystem, String> {
    Optional<DownstreamSystem> findByAccessKeyAndIsEnabledTrue(String accessKey);
}
