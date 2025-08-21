package mk.ukim.finki.wp.seminarska.repository;

import mk.ukim.finki.wp.seminarska.model.AppUser;
import mk.ukim.finki.wp.seminarska.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    List<AppUser> findByRole(Role role);
}
