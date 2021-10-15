package app.controllers;

import app.entities.pojos.ComboPojo;
import app.generated.jooq.tables.pojos.Combo;
import app.services.ComboService;
import app.services.ComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/combo")
@RequiredArgsConstructor
public class ComboController {
    private final ComboService service;

    @GetMapping("/list")
    public List<ComboPojo> getList() {
        return service.getList();
    }

    @GetMapping("/{comboId}")
    public ComboPojo getById(@PathVariable Long comboId) {
        return service.getById(comboId);
    }

    @PostMapping("/create")
    @Transactional
    public void createCombo(@RequestBody Combo combo) {
        service.createCombo(combo);
    }

    @PutMapping("/edit")
    @Transactional
    public void editCombo(@RequestBody Combo combo) {
        service.editCombo(combo);
    }

    @DeleteMapping("/delete/{comboId}")
    @Transactional
    public void deleteCombo(@PathVariable Long comboId) {
        service.deleteCombo(comboId);
    }
}
