package app.services;

import app.InvalidDataException;
import app.UserContext;
import app.generated.jooq.tables.pojos.Component;
import app.repository.ComponentRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.tools.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ComponentService {
    private final ComponentRepository repository;

    public List<Component> getComponentList() {
        return repository.getList();
    }

    public Component getById(Long componentId) {
        return repository.getById(componentId);
    }

    public void deleteComponent(Long componentId) {
        repository.deleteById(componentId);
    }

    public void createNewComponent(Component component) {
        validateCreateComponent(component);
        component.setComponentId(repository.getNextComponentId());
        repository.createNewComponent(component);
    }

    public void editComponent(Component component) {
        validateCreateComponent(component);
        Component oldComponent = repository.getById(component.getComponentId());
        deleteComponent(oldComponent.getComponentId());
        repository.createNewComponent(component);
    }

    public void validateCreateComponent(Component component) {
        Map<String, String> errors = new HashMap<>();
        if (StringUtils.isBlank(component.getName())) {
            errors.put("name", "Name can't be null");
        }
        if (StringUtils.isBlank(component.getDescription())) {
            errors.put("description", "Description can't be null");
        }

        if (!errors.isEmpty()) {
            throw new InvalidDataException(errors);
        }
    }
}
