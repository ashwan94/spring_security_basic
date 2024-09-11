package org.example.security_test.repo;

import org.example.security_test.entity.IpBlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IpBlockRepo extends JpaRepository<IpBlockEntity, Long> {
}
