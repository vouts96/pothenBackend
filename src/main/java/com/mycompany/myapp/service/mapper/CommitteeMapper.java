package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Committee;
import com.mycompany.myapp.service.dto.CommitteeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Committee} and its DTO {@link CommitteeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommitteeMapper extends EntityMapper<CommitteeDTO, Committee> {}
