package quickdesk.quickdesk_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quickdesk.quickdesk_backend.model.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
