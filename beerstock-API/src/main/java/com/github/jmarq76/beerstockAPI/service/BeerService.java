package com.github.jmarq76.beerstockAPI.service;

import com.github.jmarq76.beerstockAPI.dto.BeerDTO;
import com.github.jmarq76.beerstockAPI.entity.Beer;
import com.github.jmarq76.beerstockAPI.exception.BeerAlreadyRegisteredException;
import com.github.jmarq76.beerstockAPI.exception.BeerNotFoundException;
import com.github.jmarq76.beerstockAPI.exception.BeerStockBelowZeroException;
import com.github.jmarq76.beerstockAPI.exception.BeerStockExceededException;
import com.github.jmarq76.beerstockAPI.mapper.BeerMapper;
import com.github.jmarq76.beerstockAPI.repository.BeerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper = BeerMapper.INSTANCE;

    public BeerDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(beerDTO.getName());
        Beer beer = beerMapper.toModel(beerDTO);
        Beer savedBeer = beerRepository.save(beer);
        return beerMapper.toDTO(savedBeer);
    }

    public BeerDTO findByName(String name) throws BeerNotFoundException {
        Beer foundBeer = beerRepository.findByName(name)
                .orElseThrow(() -> new BeerNotFoundException(name));
        return beerMapper.toDTO(foundBeer);
    }

    public List<BeerDTO> listAll(){
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws BeerNotFoundException {
        verifyIfExists(id);
        beerRepository.deleteById(id);
    }

    private Beer verifyIfExists(Long id) throws BeerNotFoundException {
        return beerRepository.findById(id)
                .orElseThrow(() -> new BeerNotFoundException(id));
    }

    private void verifyIfIsAlreadyRegistered(String name) throws BeerAlreadyRegisteredException {
        Optional<Beer> optSavedBeer = beerRepository.findByName(name);
        if (optSavedBeer.isPresent()) {
            throw new BeerAlreadyRegisteredException(name);
        }
    }

    public BeerDTO increment(Long id, int quantityToIncrement) throws BeerStockExceededException, BeerNotFoundException {
        Beer beerToIncrementStock = verifyIfExists(id);
        int verifyIncrement = quantityToIncrement + beerToIncrementStock.getQuantity();
        if (verifyIncrement <= beerToIncrementStock.getMax()){
            beerToIncrementStock.setQuantity(verifyIncrement);
            Beer incrementStock = beerRepository.save(beerToIncrementStock);
            return beerMapper.toDTO(incrementStock);
        }

        throw new BeerStockExceededException(id, quantityToIncrement);
    }

    public BeerDTO decrement(Long id, int quantityToDecrement) throws BeerNotFoundException, BeerStockBelowZeroException {
        Beer beerToDecrementStock = verifyIfExists(id);
        int verifyDecrement = beerToDecrementStock.getQuantity() - quantityToDecrement;
        if (verifyDecrement >= 0){
            beerToDecrementStock.setQuantity(verifyDecrement);
            Beer decrementStock = beerRepository.save(beerToDecrementStock);
            return beerMapper.toDTO(decrementStock);
        }

        throw new BeerStockBelowZeroException(id, quantityToDecrement);
    }
}
