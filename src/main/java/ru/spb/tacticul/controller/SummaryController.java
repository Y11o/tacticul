package ru.spb.tacticul.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.spb.tacticul.dto.SummaryDTO;
import ru.spb.tacticul.service.SummaryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/summary")
@Tag(name = "Общее", description = "Вывод всей информации")
public class SummaryController {

    private final SummaryService summaryService;

    @GetMapping
    @Operation(summary = "Вывод общего json")
    public SummaryDTO summaryDTO(){
        return summaryService.getSummary();
    }

}
