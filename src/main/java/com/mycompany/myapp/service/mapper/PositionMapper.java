package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Position;
import com.mycompany.myapp.service.dto.PositionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Position} and its DTO {@link PositionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PositionMapper extends EntityMapper<PositionDTO, Position> {}
