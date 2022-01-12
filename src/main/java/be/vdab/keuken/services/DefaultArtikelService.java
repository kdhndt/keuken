package be.vdab.keuken.services;

import be.vdab.keuken.exceptions.ArtikelNietGevondenException;
import be.vdab.keuken.repositories.ArtikelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
class DefaultArtikelService implements ArtikelService {
    private final ArtikelRepository repository;

    DefaultArtikelService(ArtikelRepository repository) {
        this.repository = repository;
    }

    @Override
    public void verhoogVerkoopPrijs(long id, BigDecimal waarde) {
        repository.findById(id)
                .orElseThrow(ArtikelNietGevondenException::new)
                .verhoogVerkoopPrijs(waarde);
    }
}
