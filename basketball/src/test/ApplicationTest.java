import org.junit.Test;
import view.MessageView;

/**
 * 测试类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/6
 */
public class ApplicationTest {

    @Test
    public void messageView() {
        MessageView.display("this is title", "content\ncontent");
    }
}
