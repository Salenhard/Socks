package vlad.gurbatov.socks.entity.dto.mapper;

import org.mapstruct.*;
import vlad.gurbatov.socks.entity.Sock;
import vlad.gurbatov.socks.entity.dto.SockDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SockMapper {
    Sock toEntity(SockDto sockDto);

    SockDto toDto(Sock sock);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Sock partialUpdate(SockDto sockDto, @MappingTarget Sock sock);
}