import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class MainTest {

	public static void main(String[] args) throws IOException {
				
		int counter = 0;
		int piece_counter = 0;
		String line = "";
		Piece[] pieces = new Piece[90];
		String[] arr;		
		int goldAmount;
		int maxLevel;
		int availablePieces;//numberOfAvailablePiecesPerLevel
		
		Scanner sc = new Scanner(System.in);
		System.out.print("Please enter gold amount: ");
		goldAmount = sc.nextInt();
		System.out.print("Please enter max level allowed: ");
		maxLevel = sc.nextInt();
		System.out.print("Please enter number of available pieces per level: ");
		availablePieces = sc.nextInt();
			
		
		//dosyadan okunup bütün parçalar bir arraye atýldýðý kýsým
		BufferedReader r = new BufferedReader(new FileReader("D:\\eclipse\\homework2\\input_1.csv"));		
		while((line = r.readLine()) != null) {			
			if(counter != 0) {//ilk satýrda Hero, Piece Type, Gold, Attack Points yazdýðý için kontrol
				Piece p = new Piece(null, null, 1, 1, true);
				arr = line.split(",");
				p.setHero_name(arr[0]);
				p.setPiece_type(arr[1]);
				p.setGold(Integer.parseInt(arr[2]));
				p.setAttack_points(Integer.parseInt(arr[3]));	
				pieces[piece_counter] = p;
				piece_counter++;
			}
			counter++;			
		}
		
		
		// oyun sýrasýnda seçilebilecek parçalarýn bir arraye atýldýðý yer
		counter = 0;
		Piece[] selectablePieces = new Piece[(maxLevel * availablePieces)];//piece havuzu //seçilebilir pieces
		
		for(int i = 0; i < maxLevel; i++) {
			for(int j = 0; j < availablePieces; j++) {
				selectablePieces[counter] = pieces[i * 10 + j];
				counter++;
			}
		}
		
		System.out.println("\n\n============== TRIAL #1 ==============");
		System.out.println("\n*Computer's Greedy Aproach result");
		greedyAproach(selectablePieces, maxLevel, availablePieces, goldAmount);
		System.out.println("\n*User's Dynamic Programming result");
		dynamicPrgrammingApproach(selectablePieces, maxLevel, availablePieces, goldAmount);
		
		System.out.println("\n\n============== TRIAL #2 ==============");
		System.out.println("\n*Computer's Random Aproach result");
		randomApproach(selectablePieces, maxLevel, availablePieces, goldAmount);
		System.out.println("\n*User's Dynamic Programming result");
		dynamicPrgrammingApproach(selectablePieces, maxLevel, availablePieces, goldAmount);
	}
	

	public static void randomApproach(Piece[] p, int max_level, int avaliable_pieces, int gold_amount) {
		
		int num1,num2;		
		int temp, temp2;
		int attack_point = 0;
		Piece[] tempP = new Piece[(max_level * avaliable_pieces)];
		int countP = 0;
		

		while(gold_amount > 0) {//gold bitene kadar ya da hiçbir piece gold'una yetmiyorsa döngü biter
			
			num1 = randomGenerator(1, max_level);//level seçme//pawn or rook or ...
			num2 = randomGenerator(1, avaliable_pieces);//bir levelden rastgele birini seçme p1 or p2
			temp = ((num1 - 1) * avaliable_pieces) + num2 - 1;
						
			if(p[temp].getGold() <= gold_amount && p[temp].isSelect() == true) {//rastgele seçilen piece in kontrolü				
				gold_amount = gold_amount - p[temp].getGold();
				temp2 = ((num1 - 1) * avaliable_pieces);				
				for(int i = temp2; i < temp2 + avaliable_pieces; i++) {
					p[i].setSelect(false);
				}
				attack_point += p[temp].getAttack_points(); 
				tempP[countP] = p[temp];
				countP++;
			}
			else if(pieceCheck(p, gold_amount) == false){//kalan parçalara gold yetmiyor ya da bütün parçalar false
				break;
			}			
		}
		
		System.out.println("\nAttack points: " + attack_point);
		System.out.println("Selected pieces: " );
		for(int i = 0; i < tempP.length; i++) {
			System.out.print("  "+(i + 1) + ". ");
			tempP[i].display();
			if(tempP[i+1]==null)
				break;
		}
		
	}

	
	public static void greedyAproach(Piece[] p, int max_level, int avaliable_pieces, int gold_amount) {
		
		Piece[] tempP = new Piece[max_level];
		Piece tempPiece;		
		int attack_point = 0;		
		int temp = 0;
		int count = 0;
		int count2 = 0;
		double ratio;
		double tempRatio;
		
		while(gold_amount > 0) {
			
			temp = count * avaliable_pieces;
			if(temp == max_level * avaliable_pieces) break;
			
			for(int i = temp; i < (temp + avaliable_pieces) - 1; i++) {//büyükten küçüðe sýralama
				ratio = (double)p[i].getGold() /(double)p[i].getAttack_points();
				tempPiece = p[i];
				
				for(int j = i; j < (temp + avaliable_pieces) - 1; j++) {
					tempRatio = (double)p[j].getGold() / (double)p[j].getAttack_points();					
					if(tempRatio > ratio) {
						p[i] = p[j];
						p[j] = tempPiece;
					}					
				}				
			}
			for(int i = temp; i < temp + avaliable_pieces; i++) {
				if(p[i].getGold() <= gold_amount) {//gold'un yettiði kategorideki ilk piece alýnýr ve arraye atýlýr
					gold_amount -= p[temp].getGold();		
					tempP[count2] = p[temp];
					attack_point += tempP[count2].getAttack_points();
					count2++;
					break;
				}
			}			
			count++;
		}
		
		System.out.println("\nAttack points: " + attack_point);
		System.out.println("Selected pieces:" );
		for(int i = 0; i < count2; i++) {
			System.out.print("  "+(i + 1)+ ". ");
			tempP[i].display();
		}

	}

	
	public static void dynamicPrgrammingApproach(Piece[] p, int max_level, int avaliable_pieces, int gold_amount) {
		
		int[][]arr = new int[max_level + 1][gold_amount + 1];
		Piece tempPiece;
		int tempPiecePoint = 0;
		
		for(int i = 0; i <= max_level; i++) {//en fazla 9 tane parça alýnabileceði için
			for(int j = 0; j <= gold_amount; j++) {//burda 5er 5er artmasý daha mantýklý onu dene
				
				if(i == 0 || j == 0) {
					arr[i][j] = 0;
				}
				else {
					tempPiece = maxPiece(i, j, max_level, avaliable_pieces, p);
					if(tempPiece != null)
						tempPiecePoint = tempPiece.getAttack_points();
									
					if(tempPiece != null && gold_amount > tempPiece.getGold()) {//max olan üst mü piece mi
						int temp = j - tempPiece.getGold();
						arr[i][j] = max(tempPiecePoint + arr[i - 1 ][temp], arr[i][j-1]);
					}
					else {
						arr[i][j] = arr[i - 1][j];//max o anki deðilse bir öncekini yaz diye
					}
				}			
			}
		}
		

		System.out.println("\nAttack points: " + arr[max_level][gold_amount]);
		//System.out.println("Selected pieces:" );
	}
	
		
	//örneðin en en büyük atak pointe sahip pawn'ý bulabilmek için 
	public static Piece maxPiece(int i, int j, int max_level, int avaliable_pieces, Piece[] p) {
		Piece tempPiece = null;
		int max = 0;
		int temp = (i - 1) * avaliable_pieces;
		
		for(int k = temp; k < temp + avaliable_pieces; k++) {//ayný türdeki parçalarý gezer
			if(p[k].getGold() <= j && max < p[k].getAttack_points()) {//goldu yeten parçalara bakar
				max = p[k].getAttack_points();
				tempPiece = p[k];
			}
		}

		return tempPiece;		
	}
	
	
	public static int max(int a, int b) {
		return (a > b) ? a : b;
	}
		
	
	public static int randomGenerator(int min, int max) {	
		Random random = new Random();
		int number = random.nextInt((max - min) + 1) + min;
		return number;		
	}	
	
	
	public static boolean pieceCheck(Piece[]p, int gold_amount) {
		boolean flag = false;
		for(int i = 0; i < p.length; i++) {
			if(p[i].getGold() <= gold_amount && p[i].isSelect() == true) {
				flag = true;
			}
		}
		return flag;
	}

}
