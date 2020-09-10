package warehouse.controller;

import org.springframework.web.bind.annotation.*;
import warehouse.db.Stock;
import warehouse.model.*;
import warehouse.services.Requests;
import warehouse.services.Tasks;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/warehouse/api")
public class Controller {

	final Requests requests;
	final Tasks tasks;
	final Stock stock;

	public Controller(Requests requests, Tasks tasks, Stock stock) {
		this.requests = requests;
		this.tasks = tasks;
		this.stock = stock;
	}

	@GetMapping(path = "/hello")
	public String hello() {
		return "hello to you too!";
	}

	@PostMapping(path = "/orders")
	public void newOrder(@RequestParam String[] productNames) {
		List<Request> requests = getRequests(productNames, TaskType.pick_from_stock);
		this.requests.addRequests(requests);
	}

	@PostMapping(path = "/supply")
	public void newSupply(@RequestParam String[] productNames){
		requests.addRequests(getRequests(productNames, TaskType.put_to_stock));
	}

	private List<Request> getRequests(String[] productNames, TaskType taskType) {
		return toProducts(productNames).stream()
				.map(product -> new Request(taskType, product))
				.collect(Collectors.toList());
	}

	private List<ProductType> toProducts(String[] productNames) {
		LinkedList<ProductType> productTypes = new LinkedList<>();
		for (String productName : productNames) {
			productTypes.add(ProductType.valueOf(productName));
		}
		return productTypes;
	}

	@PutMapping(path = "/tasks/{id}/complete")
	public void taskCompleted(@PathVariable int id) throws Exception {
		tasks.taskComplete(id);
	}

	@GetMapping(path = "/nexttasks")
	public Task[] getNextTasks(int freeSlots){
		List<Task> tasks = this.tasks.giveMeTasks(freeSlots);
		return tasks.toArray(Task[]::new);
	}

	@GetMapping(path = "/stock")
	public Map<ProductType, Integer> getStockInfo() {
		return stock.getStock();
	}

	@GetMapping(path = "/requests")
	public List<Request> getRequests(){
		return requests.getRequestsQueue();
	}

	@GetMapping(path = "/tasks")
	public List<Task> getTasks(){
		return tasks.getTasksInProgress();
	}
}
