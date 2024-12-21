package vlad.gurbatov.socks.configuration;

import jakarta.validation.Valid;
import org.springframework.batch.item.ItemProcessor;
import vlad.gurbatov.socks.entity.Sock;
import vlad.gurbatov.socks.entity.dto.SockDto;

public class SockProcessor implements ItemProcessor<SockDto, Sock> {
    @Override
    public Sock process(@Valid SockDto dto) {
        Sock sock = new Sock();
        sock.setColor(dto.getColor());
        sock.setCottonPercentage(dto.getCottonPercentage());
        sock.setAmount(dto.getAmount());
        return sock;
    }
}
