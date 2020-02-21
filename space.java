
public class space {
	private int player;
	private boolean occupied;

	public space() {
		occupied = false;
		player = 0;
	}

	public boolean isOcupied() {
		return occupied;
	}

	public int getPlayer() {
		return player;
	}

	public void remove(space start) {
		start.occupied = false;
		start.player = 0;
	}

	public void move(space destination, space start) {
		destination.occupied = true;
		destination.player = start.player;
		start.remove(start);
	}

	public void move(space destination, int player) {
		destination.occupied = true;
		destination.player = player;
	}
}
