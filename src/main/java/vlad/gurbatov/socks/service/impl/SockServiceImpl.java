package vlad.gurbatov.socks.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vlad.gurbatov.socks.entity.Sock;
import vlad.gurbatov.socks.repository.SockRepository;
import vlad.gurbatov.socks.service.SockService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static vlad.gurbatov.socks.util.SockSpecification.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class SockServiceImpl implements SockService {
    private final SockRepository repository;

    @Override
    public Optional<Sock> findById(Long id) {
        log.info("find by id: `{}`", id);
        return repository.findById(id);
    }

    @Override
    public List<Sock> getAll(String color, Integer moreThenCotton, Integer lessThenCotton, Integer equalCotton, String sortParam) {
        log.info("get all");
        Specification<Sock> filter = Specification.where(Objects.nonNull(color) ? equalColor(color) : null)
                .and(Objects.nonNull(moreThenCotton) ? moreThenCotton(moreThenCotton) : null)
                .and(Objects.nonNull(lessThenCotton) ? lessThenCotton(lessThenCotton) : null)
                .and(Objects.nonNull(equalCotton) ? equalCotton(equalCotton) : null);
        Sort sort = Sort.by(Sort.Direction.ASC, Objects.nonNull(sortParam) ? sortParam : "color");
        return repository.findAll(filter, sort);
    }

    @Override
    public void delete(Long id) {
        log.info("delete by id: `{}`", id);
        repository.deleteById(id);
    }

    @Override
    public Sock update(Long id, Sock sock) {
        log.info("update by id: `{}`, {}", id, sock);
        Sock updatedSock = repository.findById(id).map(newSock ->
        {
            newSock.setId(sock.getId());
            newSock.setColor(sock.getColor());
            newSock.setAmount(sock.getAmount());
            newSock.setCotton(sock.getCotton());
            return newSock;
        }).orElseThrow(() -> {
            log.info("Sock with id: `{}` not found", id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Sock with id: `%d` not found".formatted(id));
        });

        return repository.save(updatedSock);
    }

    @Override
    public Sock save(Sock sock) {
        log.info("save: {}", sock);
        if (sock.getId() != null && repository.findById(sock.getId()).isPresent()) {
            log.info("save throw exception: Sock with id: `{}` already exists", sock.getId());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Sock with id `%d` already exists".formatted(sock.getId()));
        }
        return repository.save(sock);
    }

    @Override
    public Sock income(Sock sock) {
        log.info("income: {}", sock);
        Sock find = repository.findByColorAndCotton(sock.getColor(), sock.getCotton())
                .orElseThrow(() -> {
                    log.info("income throw exception: Sock not found");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Sock not found");
                });
        find.setAmount(find.getAmount() + sock.getAmount());
        return repository.save(find);
    }

    @Override
    public Sock outcome(Sock sock) {
        log.info("outcome: {}", sock);
        Sock find = repository.findByColorAndCotton(sock.getColor(), sock.getCotton())
                .orElseThrow(() -> {
                    log.info("outcome throw exception: Sock not found");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Sock not found");
                });
        if (find.getAmount() < sock.getAmount()) {
            log.info("outcome throw exception: Current amount is less then you want to decrees");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current amount is less then you want to decrees");
        }
        find.setAmount(find.getAmount() - sock.getAmount());
        return repository.save(find);
    }

}
