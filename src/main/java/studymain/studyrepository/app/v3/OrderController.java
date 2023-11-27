package studymain.studyrepository.app.v3;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studymain.studyrepository.logtrace.LogTrace;
import studymain.studyrepository.trace.TraceStatus;

@RestController
@RequestMapping("/v3")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final LogTrace trace;

    @GetMapping("/request")
    public String request(String itemId) {
        TraceStatus status = null;
        try {
            status = trace.begin("OrderController.request()");
            orderService.orderItem(itemId);
            trace.end(status);
            return "ok";
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }

    }
}
