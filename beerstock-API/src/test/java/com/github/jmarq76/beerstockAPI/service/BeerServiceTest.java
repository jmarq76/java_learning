package com.github.jmarq76.beerstockAPI.service;

import com.github.jmarq76.beerstockAPI.builder.BeerDTOBuilder;
import com.github.jmarq76.beerstockAPI.dto.BeerDTO;
import com.github.jmarq76.beerstockAPI.entity.Beer;
import com.github.jmarq76.beerstockAPI.exception.BeerAlreadyRegisteredException;
import com.github.jmarq76.beerstockAPI.exception.BeerNotFoundException;
import com.github.jmarq76.beerstockAPI.mapper.BeerMapper;
import com.github.jmarq76.beerstockAPI.repository.BeerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTest {

    private static final long INVALID_BEER_ID = 1L;

    @Mock
    private BeerRepository beerRepository;

    private BeerMapper beerMapper = BeerMapper.INSTANCE;

    @InjectMocks
    private BeerService beerService;

    @Test
    void whenBeerInformedThenItShouldBeCreated() throws BeerAlreadyRegisteredException {
        //given
        BeerDTO exprectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedSabedBeer = beerMapper.toModel(exprectedBeerDTO);

        //when
        when(beerRepository.findByName(exprectedBeerDTO.getName())).thenReturn(Optional.empty());
        when(beerRepository.save(expectedSabedBeer)).thenReturn(expectedSabedBeer);

        //then
        BeerDTO createdBeerDTO = beerService.createBeer(exprectedBeerDTO);

        assertThat(createdBeerDTO.getId(), is(equalTo(exprectedBeerDTO.getId())));
        assertThat(createdBeerDTO.getName(), is(equalTo(exprectedBeerDTO.getName())));
        assertThat(createdBeerDTO.getQuantity(), is(equalTo(exprectedBeerDTO.getQuantity())));

//        assertEquals(exprectedBeerDTO.getId(), createdBeerDTO.getId());
//        assertEquals(exprectedBeerDTO.getName(), createdBeerDTO.getName());
    }

    @Test
    void whenAlreadyRegisteredBeerInformedThenAnExceptionShouldBeThrown() {
        //given
        BeerDTO exprectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer duplicatedBeer = beerMapper.toModel(exprectedBeerDTO);

        //when
        when(beerRepository.findByName(exprectedBeerDTO.getName())).thenReturn(Optional.of(duplicatedBeer));

        //then
        assertThrows(BeerAlreadyRegisteredException.class, () -> beerService.createBeer(exprectedBeerDTO));
    }

    @Test
    void whenValidBeerNameIsGivenThenReturnABeer() throws BeerNotFoundException {
        //given
        BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedFoundBeer = beerMapper.toModel(expectedFoundBeerDTO);

        //when
        when(beerRepository.findByName(expectedFoundBeer.getName())).thenReturn(Optional.of(expectedFoundBeer));

        //then
        BeerDTO foundBeerDTO = beerService.findByName(expectedFoundBeer.getName());

        assertThat(foundBeerDTO, is(equalTo(expectedFoundBeerDTO)));

    }

    @Test
    void whenNoRegisteredBeerNameIsGivenThenThrowAnException(){
        //given
        BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        //when
        when(beerRepository.findByName(expectedFoundBeerDTO.getName())).thenReturn(Optional.empty());

        //then
        assertThrows(BeerNotFoundException.class, () -> beerService.findByName(expectedFoundBeerDTO.getName()));

    }


}
