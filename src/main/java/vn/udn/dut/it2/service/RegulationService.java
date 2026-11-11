package vn.udn.dut.it2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.udn.dut.it2.model.entity.Regulation;
import vn.udn.dut.it2.repository.RegulationRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegulationService {

    private final RegulationRepository regulationRepository;

    public List<Regulation> search(String category, String keyword) {
        return regulationRepository.searchRegulations(category, keyword);
    }

    public Optional<Regulation> getById(Long id) {
        return regulationRepository.findById(id);
    }

    public List<Regulation> getAll() {
        return regulationRepository.findAll();
    }
}
