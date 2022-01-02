package si.fri.rso.uniborrow.requests.models.converters;

import si.fri.rso.uniborrow.requests.lib.Request;
import si.fri.rso.uniborrow.requests.models.entities.RequestEntity;

public class RequestConverter {

    public static Request toDto(RequestEntity entity) {

        Request dto = new Request();
        dto.setTitle(entity.getTitle());
        dto.setTimestampStart(entity.getTimestampStart());
        dto.setTimestampEnd(entity.getTimestampEnd());
        dto.setMessage(entity.getMessage());
        dto.setPrice(entity.getPrice());
        dto.setUserId(entity.getUserId());

        return dto;

    }

    public static RequestEntity toEntity(Request dto) {

        RequestEntity entity = new RequestEntity();
        entity.setMessage(dto.getMessage());
        entity.setTitle(dto.getTitle());
        entity.setTimestampStart(dto.getTimestampStart());
        entity.setTimestampEnd(dto.getTimestampEnd());
        entity.setUserId(dto.getUserId());
        entity.setPrice(dto.getPrice());

        return entity;

    }

}
