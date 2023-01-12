
public class Piece {

	private String hero_name;
	private String piece_type;
	private int gold;
	private int attack_points;
	private boolean select;
	
	public Piece(String name, String type, int gold, int points, boolean select) {
		this.hero_name = name;
		this.piece_type = type;
		this.gold = gold;
		this.attack_points = points;
		this.select = select;
	}

	public String getHero_name() {return hero_name;}
	public void setHero_name(String hero_name) {this.hero_name = hero_name;}
	public String getPiece_type() {return piece_type;}
	public void setPiece_type(String piece_type) {this.piece_type = piece_type;}
	public int getGold() {return gold;}
	public void setGold(int gold) {this.gold = gold;}
	public int getAttack_points() {return attack_points;}
	public void setAttack_points(int attack_points) {this.attack_points = attack_points;}
	public boolean isSelect() {return select;}
	public void setSelect(boolean select) {this.select = select;}

	public void display() {
		System.out.println("Name: " + hero_name + "  Gold: " + gold + "  Attack Points: " + attack_points);
	}
	
	

	
	
}
