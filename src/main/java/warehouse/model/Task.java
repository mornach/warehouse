package warehouse.model;

public class Task {
	public int taskId;
	public TaskType taskType;
	public final Location location;

	public Task(int taskId, TaskType taskType, Location location) {
		this.taskId = taskId;
		this.taskType = taskType;
		this.location = location;
	}
}
