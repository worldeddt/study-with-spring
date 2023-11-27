package studymain.studyrepository.app.v3;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import studymain.studyrepository.logtrace.LogTrace;
import studymain.studyrepository.trace.TraceStatus;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final LogTrace trace;
    public void orderItem(String itemId) {
        TraceStatus status = null;
        try {
            status = trace.begin("OrderService.orderItem()");
            orderRepository.save(itemId);
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }

}
