package vlad.gurbatov.socks.service;

import vlad.gurbatov.socks.entity.Sock;

import java.util.List;
import java.util.Optional;

public interface SockService {
    Optional<Sock> findById(Long id);

    List<Sock> getAll(String color, Integer moreThenCotton, Integer lessThenCotton, Integer equalCotton, String sort);

    void delete(Long id);

    Sock update(Long id, Sock sock);

    Sock save(Sock sock);

    Sock income(Sock sock);

    Sock outcome(Sock sock);
}
