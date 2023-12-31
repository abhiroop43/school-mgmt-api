package dev.abhiroopsantra.schoolmgmtapi.repositories;

import dev.abhiroopsantra.schoolmgmtapi.entities.Fee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeRepository extends JpaRepository<Fee, Long> {
}
