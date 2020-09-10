package warehouse.services;

import org.springframework.stereotype.Service;
import warehouse.model.Request;

import java.util.LinkedList;
import java.util.List;

@Service
public class Requests {
	private List<Request> requestsQueue = new LinkedList<>();

	public synchronized void addRequests(List<Request> requests) {
		requestsQueue.addAll(requests);
	}

	public List<Request> pullRequests(int max){
		List<Request> requests = new LinkedList<>();
		int howMuchCanYouTake = Math.min(requestsQueue.size(), max);
		for (int i = 0; i < howMuchCanYouTake; i++) {
			requests.add(requestsQueue.remove(0));
		}
		return requests;
	}

	public synchronized List<Request> getRequestsQueue() {
		return requestsQueue;
	}
}
