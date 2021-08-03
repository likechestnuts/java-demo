import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;

/**
 * @author wangguodong
 * @since 2021/8/3
 */
public class PooledConnectionProxy extends AbstractConnectionProxy {

	Queue<PooledConnectionProxy> queue;

	private Connection target;

	public PooledConnectionProxy(Queue<PooledConnectionProxy> queue, Connection target) {
		this.queue = queue;
		this.target = target;
	}

	@Override
	protected Connection getRealConnection() {
		return target;
	}

	@Override
	public void close() throws SQLException {
		System.out.println("放入空闲队列");
		queue.offer(this);
	}
}
