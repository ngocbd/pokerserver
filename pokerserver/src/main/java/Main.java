import com.fcs.pokerserver.Player;

public class Main {

	public static void main(String[] args) {
		Library lib = new Library();
		Player p = new Player();
		p.setName("Kuki");
		p.setBalance(50000);
		
		p.setListenner(lib);
		p.bet(100);
		
		
	}

}
