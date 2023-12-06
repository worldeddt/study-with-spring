package webrtc.webrtc2phase.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import webrtc.webrtc2phase.dto.ChatUserDto;
import webrtc.webrtc2phase.entity.ChatUser;

@Mapper(componentModel = "spring")
public interface ChatUserMapper {
    ChatUserMapper INSTANCE = Mappers.getMapper(ChatUserMapper.class);
    ChatUserDto toDto(ChatUser e);
    ChatUser toEntity(ChatUserDto d);
}