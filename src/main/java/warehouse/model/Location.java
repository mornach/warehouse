package warehouse.model;

public class Location {
	public int x,y;

	private Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static Location of(int x, int y) {
		return new Location(x, y);
	}
}
