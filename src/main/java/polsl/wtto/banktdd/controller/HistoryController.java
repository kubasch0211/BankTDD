package polsl.wtto.banktdd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import polsl.wtto.banktdd.domain.OperationLog;
import polsl.wtto.banktdd.service.HistoryService;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.OK)
    public List<OperationLog> getHistory(@PathVariable String accountNumber) {
        return historyService.getAccountHistory(accountNumber);
    }
}