package com.mediconnect.auth.infrastructure;
import java.util.Optional; import java.util.UUID;
import com.mediconnect.auth.domain.UserEntity; import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<UserEntity,UUID>{ Optional<UserEntity> findByEmailIgnoreCase(String email); boolean existsByEmailIgnoreCase(String email); }
