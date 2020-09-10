package warehouse.services;

import org.springframework.stereotype.Service;
import warehouse.db.Stock;
import warehouse.model.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class Tasks {

	AtomicInteger taskIds = new AtomicInteger();

	List<Task> tasksInProgress = Collections.synchronizedList(new LinkedList<>());

	final LocationInfo locationInfo;
	final Requests requests;
	final Stock stock;

	public Tasks(LocationInfo locationInfo, Requests requests, Stock stock) {
		this.locationInfo = locationInfo;
		this.requests = requests;
		this.stock = stock;
	}

	public synchronized List<Task> giveMeTasks(int freeSlots) {
		List<Request> requests = this.requests.pullRequests(freeSlots);
		List<Task> tasks = requestsToTasks(requests);
		tasksInProgress.addAll(tasks);
		return tasks;
	}

	private List<Task> requestsToTasks(List<Request> requests) {
		List<Task> tasks = new LinkedList<>();
		for (Request request : requests) {
			if (request.taskType == TaskType.pick_from_stock) {
				if (!productAvailable(request.product)) {
					continue;
				}
			}
			tasks.add(new Task(taskIds.getAndIncrement(), request.taskType, locationInfo.find(request.product)));
		}
		return tasks;
	}

	private boolean productAvailable(ProductType product) {
		return stock.getStock().get(product) > 0;
	}

	public void taskComplete(int id) throws Exception {
		Optional<Task> aTask = tasksInProgress.stream().filter(task -> task.taskId == id).findFirst();
		if (aTask.isPresent()){
			Task task = aTask.get();
			ProductType productType = locationInfo.find(task.location);
			if (task.taskType == TaskType.put_to_stock) {
				stock.putToStock(productType);
			}
			if (task.taskType == TaskType.pick_from_stock){
				stock.pickFromStock(productType);
			}

			tasksInProgress.remove(task);
		} else {
			throw new Exception("Task " + id + " not found");
		}
	}

	public List<Task> getTasksInProgress() {
		return tasksInProgress;
	}
}
