package vn.udn.dut.it2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.udn.dut.it2.model.entity.Student;
import vn.udn.dut.it2.repository.StudentRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public Optional<Student> getStudentProfile(String studentCode) {
        return studentRepository.findByStudentCode(studentCode);
    }

    public Student getDefaultMockStudent() {
        return studentRepository.findByStudentCode("102230001")
                .orElseGet(() -> studentRepository.findAll().stream().findFirst().orElse(null));
    }
}
