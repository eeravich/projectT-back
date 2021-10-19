package app.controllers;

import app.entities.enums.Roles;
import app.generated.jooq.tables.pojos.Component;
import app.services.ComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/component")
@RequiredArgsConstructor
public class ComponentController {
    private final ComponentService service;

    @GetMapping("/list")
    public List<Component> getList() {
        return service.getComponentList();
    }

    @GetMapping("/{componentId}")
    public Component getComponentById(@PathVariable Long componentId) {
        return service.getById(componentId);
    }

    @PostMapping("/create")
    @Secured({Roles.Fields.ROLE_MANAGER, Roles.Fields.ROLE_ADMIN})
    @Transactional
    public void createComponent(@RequestBody Component component) {
        service.createNewComponent(component);
    }

    @PutMapping("/edit")
    @Secured({Roles.Fields.ROLE_MANAGER, Roles.Fields.ROLE_ADMIN})
    @Transactional
    public void editComponent(@RequestBody Component component) {
        service.editComponent(component);
    }

    @DeleteMapping("/delete/{componentId}")
    @Secured({Roles.Fields.ROLE_MANAGER, Roles.Fields.ROLE_ADMIN})
    @Transactional
    public void deleteComponent(@PathVariable Long componentId) {
        service.deleteComponent(componentId);
    }
}
