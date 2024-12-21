package vlad.gurbatov.socks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vlad.gurbatov.socks.entity.Sock;

import java.util.Optional;

@Repository
public interface SockRepository extends JpaRepository<Sock, Long>, JpaSpecificationExecutor<Sock> {

    Optional<Sock> findByColorAndCottonPercentage(String color, Integer cottonPercentage);
}