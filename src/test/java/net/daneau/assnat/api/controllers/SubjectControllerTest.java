package net.daneau.assnat.api.controllers;

import net.daneau.assnat.api.models.subjects.Sujet;
import net.daneau.assnat.api.models.subjects.responses.SujetReponse;
import net.daneau.assnat.api.services.SubjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubjectControllerTest {

    @Mock
    private SubjectService subjectServiceMock;
    @InjectMocks
    private SubjectController subjectController;

    @Test
    void getSubjects() {
        List<Sujet> sujets = List.of(Sujet.builder().build());
        when(subjectServiceMock.getSubjects(Set.of("1", "2"))).thenReturn(sujets);
        SujetReponse response = this.subjectController.getSubjects(Set.of("1", "2"));
        assertEquals(response.getSujets(), sujets);
    }
}