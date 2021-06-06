public class Location extends Value{
	Environment stored_place;
	String identifier;
	
	public Location(Environment stored_place, String identifier) {
		super("location");
		this.stored_place = stored_place;
		this.identifier = identifier;
	}
}
