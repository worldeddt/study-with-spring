package media.ftf.mapper;


import media.ftf.domain.entity.RoomDto;
import media.ftf.domain.entity.RoomEntity;
import media.ftf.dto.request.RoomRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class RoomMapper {

    @Mappings(value = {
            @Mapping(target = "id", source = "roomId"),
            @Mapping(target = "createDate", ignore = true),
            @Mapping(target = "closeDate", ignore = true),
    })
    public abstract RoomEntity toEntity(RoomRequest roomRequest);

    public abstract RoomEntity toEntity(RoomDto roomDto);

    public abstract RoomDto toDto(RoomEntity roomEntity);

}
