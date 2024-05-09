package media.ftf.application.interfaces;


import media.ftf.dto.request.RoomRequest;
import media.ftf.dto.response.RoomResponse;

public interface RoomApiService {
    RoomResponse createRoom(RoomRequest roomRequest);
}
