package dev.abhiroopsantra.schoolmgmtapi.repositories;

import dev.abhiroopsantra.schoolmgmtapi.entities.User;
import dev.abhiroopsantra.schoolmgmtapi.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository public interface UserRepository extends JpaRepository<User, Long> {
    User findByRole(UserRole userRole);

    Optional<User> findFirstByEmail(String email);

    Page<User> findByRoleAndIsActive(UserRole userRole, Boolean isActive, Pageable pageable);
}
