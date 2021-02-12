package com.github.jmarq76.beerstockAPI.mapper;

import com.github.jmarq76.beerstockAPI.dto.BeerDTO;
import com.github.jmarq76.beerstockAPI.entity.Beer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BeerMapper {

    BeerMapper INSTANCE = Mappers.getMapper(BeerMapper.class);

    Beer toModel(BeerDTO beerDTO);

    BeerDTO toDTO(Beer beer);
}
