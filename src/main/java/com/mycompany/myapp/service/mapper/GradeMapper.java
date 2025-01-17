package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Grade;
import com.mycompany.myapp.service.dto.GradeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Grade} and its DTO {@link GradeDTO}.
 */
@Mapper(componentModel = "spring")
public interface GradeMapper extends EntityMapper<GradeDTO, Grade> {}
