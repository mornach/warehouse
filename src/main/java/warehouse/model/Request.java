package warehouse.model;

import java.util.List;

public class Request {
	public final TaskType taskType;
	public final ProductType product;

	public Request(TaskType taskType, ProductType product) {
		this.taskType = taskType;
		this.product = product;
	}
}
