package vn.udn.dut.it2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.udn.dut.it2.model.entity.AcademicEvent;
import vn.udn.dut.it2.repository.AcademicEventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicEventService {

    private final AcademicEventRepository eventRepository;

    public List<AcademicEvent> getEvents(String eventType, String faculty, String className) {
        return eventRepository.findEventsWithFilter(eventType, faculty, className);
    }
}
