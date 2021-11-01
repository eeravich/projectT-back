package app.services;

import app.InvalidDataException;
import app.generated.jooq.tables.pojos.Component;
import app.repository.ComponentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ComponentServiceTest {

    @MockBean
    ComponentRepository componentRepository;

    @Autowired
    ComponentService componentService;

    @Test
    void deleteComponentTest() {
        Mockito.doNothing().when(componentRepository).deleteById(1L);
        componentService.deleteComponent(1L);
        Mockito.verify(componentRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void createComponentTest_WithNullFields() {
        Component component = new Component();
        Mockito.when(componentRepository.getNextComponentId()).thenReturn(2L);
        Mockito.doNothing().when(componentRepository).createNewComponent(Mockito.any(Component.class));

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> componentService.createNewComponent(component));
        assertNotNull(ex);
        assertEquals(2, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("name"));
        assertTrue(ex.getErrors().containsKey("description"));

        Mockito.verify(componentRepository, Mockito.times(0)).createNewComponent(Mockito.any(Component.class));
    }

    @Test
    void createComponentTest_WithEmptyFields() {
        Component component = new Component(
                null,
                null,
                null,
                null,
                "",
                ""
        );
        Mockito.when(componentRepository.getNextComponentId()).thenReturn(2L);
        Mockito.doNothing().when(componentRepository).createNewComponent(Mockito.any(Component.class));

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> componentService.createNewComponent(component));
        assertNotNull(ex);
        assertEquals(2, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("name"));
        assertTrue(ex.getErrors().containsKey("description"));

        Mockito.verify(componentRepository, Mockito.times(0)).createNewComponent(Mockito.any(Component.class));
    }

    @Test
    void createComponentTest_WithBlankFields() {
        Component component = new Component(
                null,
                null,
                null,
                null,
                "  ",
                " "
        );
        Mockito.when(componentRepository.getNextComponentId()).thenReturn(2L);
        Mockito.doNothing().when(componentRepository).createNewComponent(Mockito.any(Component.class));

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> componentService.createNewComponent(component));
        assertNotNull(ex);
        assertEquals(2, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("name"));
        assertTrue(ex.getErrors().containsKey("description"));

        Mockito.verify(componentRepository, Mockito.times(0)).createNewComponent(Mockito.any(Component.class));
    }

    @Test
    void createComponentTest_WithValidFields() {
        Component component = new Component(
                null,
                null,
                null,
                null,
                "Cucumber",
                "Breadsnatch"
        );
        Mockito.when(componentRepository.getNextComponentId()).thenReturn(2L);
        Mockito.doNothing().when(componentRepository).createNewComponent(Mockito.any(Component.class));

        componentService.createNewComponent(component);

        Mockito.verify(componentRepository, Mockito.times(1)).createNewComponent(Mockito.any(Component.class));
    }

    @Test
    void editComponentTest_WithValidFields() {
        Component component = new Component(
                null,
                2L,
                null,
                null,
                "Cucumber",
                "Bidecrunch"
        );

        Component oldComponent = new Component(
                null,
                2L,
                null,
                null,
                "Cucumber",
                "Breadsnatch"
        );

        Mockito.when(componentRepository.getById(Mockito.anyLong())).thenReturn(oldComponent);
        Mockito.doNothing().when(componentRepository).createNewComponent(Mockito.any(Component.class));
        Mockito.doNothing().when(componentRepository).deleteById(Mockito.anyLong());

        componentService.editComponent(component);

        Mockito.verify(componentRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verify(componentRepository, Mockito.times(1)).createNewComponent(Mockito.any(Component.class));
    }
}