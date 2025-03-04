package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceTest {

    @Mock(lenient = true)
    SpecialtyRepository specialtyRepository;

    @InjectMocks
    SpecialitySDJpaService service;

    @Test
    void testDeleteByObject() {

        //Given
        Speciality speciality = new Speciality();

        //when
        service.delete(speciality);

        //then
        then(specialtyRepository).should().delete(any(Speciality.class));
        //verify(specialtyRepository).delete(any(Speciality.class));
    }

    @Test
    void findByIdTest() {
        Speciality speciality = new Speciality();

        when(specialtyRepository.findById(1L)).thenReturn(Optional.of(speciality));

        Speciality foundSpecialty = service.findById(1L);

        assertThat(foundSpecialty).isNotNull();

        verify(specialtyRepository).findById(anyLong());

    }

    @Test
    void findByIdBddTest() {
        //given
        Speciality speciality = new Speciality();
        given(specialtyRepository.findById(1L)).willReturn(Optional.of(speciality));

        //when
        Speciality foundSpecialty = service.findById(1L);

        //then
        assertThat(foundSpecialty).isNotNull();
        then(specialtyRepository).should(timeout(100)).findById(anyLong());
        then(specialtyRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteById() {

        //given - none

        //when
        service.deleteById(1l);
        service.deleteById(1l);

        //then
        then(specialtyRepository).should(timeout(100).times(2)).deleteById(1l);
        //verify(specialtyRepository, times(2)).deleteById(1l);
    }

    @Test
    void deleteByIdAtLeast() {

        //given - none

        //when
        service.deleteById(1l);
        service.deleteById(1l);

        //then
        then(specialtyRepository).should(timeout(100).atLeastOnce()).deleteById(1l);
        //verify(specialtyRepository, atLeastOnce()).deleteById(1l);
    }

    @Test
    void deleteByIdAtMost() {

        //given - none

        //when
        service.deleteById(1l);
        service.deleteById(1l);

        //then
        then(specialtyRepository).should(atMost(5)).deleteById(1l);
        //verify(specialtyRepository, atMost(5)).deleteById(1l);
    }

    @Test
    void deleteByIdNever() {
        //given - none

        //when
        service.deleteById(1l);
        service.deleteById(1l);

        //then
        then(specialtyRepository).should(timeout(100).atLeastOnce()).deleteById(1l);
        then(specialtyRepository).should(never()).deleteById(5l);
        //verify(specialtyRepository, atLeastOnce()).deleteById(1l);

        //verify(specialtyRepository, never()).deleteById(5L);
    }

    @Test
    void testDelete() {

        //given - none

        //when
        service.delete(new Speciality());

        //then
        then(specialtyRepository).should().delete(any());
    }

    @Test
    void testDoThrow() {
        doThrow(new RuntimeException("Boom")).when(specialtyRepository).delete(any());

        assertThrows(RuntimeException.class, ()->specialtyRepository.delete(new Speciality()) );
        verify(specialtyRepository).delete(any());
    }

    @Test
    void testFindByIDThrows() {
        given(specialtyRepository.findById(1l)).willThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, ()->specialtyRepository.findById(1l) );

        then(specialtyRepository).should().findById(1l);
    }

    @Test
    void testDeleteBDD() {
        willThrow(new RuntimeException("boom")).given(specialtyRepository).delete(any());

        assertThrows(RuntimeException.class, () -> specialtyRepository.delete(new Speciality()));

        then(specialtyRepository).should().delete(any());
    }

    @Test
    void testSaveLambda() {
        //given
        final String MATCH_ME = "MATCH_ME";
        Speciality speciality = new Speciality();
        speciality.setDescription(MATCH_ME);

        Speciality savedSpecialty = new Speciality();
        savedSpecialty.setId(1L);

        //need mock to only return on match MATCH_ME string
        given(specialtyRepository.save(argThat(argument -> argument.getDescription().equals(MATCH_ME)))).willReturn(savedSpecialty);

        //when
        Speciality returnedSpecialty = service.save(speciality);

        //then
        assertThat(returnedSpecialty.getId()).isEqualTo(1L);
    }

    @Test
    void testSaveLambdaNoMatch() {
        //given
        final String MATCH_ME = "MATCH_ME";
        Speciality speciality = new Speciality();
        speciality.setDescription("Not a match");

        Speciality savedSpecialty = new Speciality();
        savedSpecialty.setId(1L);

        //need mock to only return on match MATCH_ME string
        given(specialtyRepository.save(argThat(argument -> argument.getDescription().equals(MATCH_ME)))).willReturn(savedSpecialty);

        //when
        Speciality returnedSpecialty = service.save(speciality);

        //then
        assertNull(returnedSpecialty);
    }
}