package it.desimone.rd3analyzer.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CalcolaStatistiche {

	private static Comparator<Integer> dadiComparator;
	
	static {
		dadiComparator = new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o2.compareTo(o1);
			}
		};
	}
	
	public static void main(String[] args) {
	
		List<Integer[]> terneDadiAttacco = combinazioniTernaDadi();
		List<Integer[]> coppiaDadiAttacco = combinazioniCoppiaDadi();
		List<Integer>   dadiAttacco = combinazioniDadi();

		System.out.println("Combinazioni terna dadi: "+terneDadiAttacco.size());
		System.out.println("Combinazioni coppia dadi: "+coppiaDadiAttacco.size());
		System.out.println("Combinazioni dadi: "+dadiAttacco.size());

		
		List<Integer[]> terneDadiDifesa = new ArrayList<Integer[]>(terneDadiAttacco);
		System.out.println("************* 3 VS 3 ********************");
		calcola3vs3(terneDadiAttacco, terneDadiDifesa);
		
		List<Integer[]> coppiaDadiDifesa = new ArrayList<Integer[]>(coppiaDadiAttacco);
		System.out.println("************* 3 VS 2 ********************");
		calcola3vs2(terneDadiAttacco, coppiaDadiDifesa);

		System.out.println("************* 2 VS 2 ********************");
		calcola2vs2(coppiaDadiAttacco, coppiaDadiDifesa);
		
		List<Integer> dadiDifesa = new ArrayList<Integer>(dadiAttacco);
		System.out.println("************* 3 VS 1 ********************");
		calcola3vs1(terneDadiAttacco, dadiDifesa);
		
		System.out.println("************* 2 VS 1 ********************");
		calcola2vs1(coppiaDadiAttacco, dadiDifesa);
		
		System.out.println("************* 1 VS 1 ********************");
		calcola1vs1(dadiAttacco, dadiDifesa);
		
		
	}

	
	public static void calcola3vs3(List<Integer[]> terneDadiAttacco, List<Integer[]> terneDadiDifesa) {

		int counter     = 0;
		int counter_0_3 = 0;
		int counter_3_0 = 0;
		int counter_2_1 = 0;
		int counter_1_2 = 0;
		
		for (Integer[] ternaAttacco: terneDadiAttacco){
			for (Integer[] ternaDifesa: terneDadiDifesa){
				Arrays.sort(ternaAttacco, dadiComparator);
				Arrays.sort(ternaDifesa, dadiComparator);
				int iA = ternaAttacco[0]; int jA = ternaAttacco[1]; int kA = ternaAttacco[2];
				int iD = ternaDifesa[0]; int jD = ternaDifesa[1]; int kD = ternaDifesa[2];
				int attacchiRiusciti = 0;
				if (iA > iD) attacchiRiusciti++;
				if (jA > jD) attacchiRiusciti++;
				if (kA > kD) attacchiRiusciti++;
				switch (attacchiRiusciti) {
				case 0:
					counter_0_3++;
					break;
				case 1:
					counter_1_2++;
					break;
				case 2:
					counter_2_1++;
					break;
				case 3:
					counter_3_0++;
					break;
				default:
					throw new IllegalStateException("attacchiRiusciti: "+attacchiRiusciti);
				}
				counter++;
			}
		}
		
		System.out.println("Combinazioni totali: ["+counter+"]");
		System.out.println("3 a 0: ["+counter_3_0+"] ["+(((float)counter_3_0)*100/counter)+"]");
		System.out.println("2 a 1: ["+counter_2_1+"] ["+(((float)counter_2_1)*100/counter)+"]");
		System.out.println("1 a 2: ["+counter_1_2+"] ["+(((float)counter_1_2)*100/counter)+"]");
		System.out.println("0 a 3: ["+counter_0_3+"] ["+(((float)counter_0_3)*100/counter)+"]");
	}
	
	public static void calcola3vs2(List<Integer[]> terneDadiAttacco, List<Integer[]> coppiaDadiDifesa) {

		int counter     = 0;
		int counter_0_2 = 0;
		int counter_2_0 = 0;
		int counter_1_1 = 0;
		
		for (Integer[] ternaAttacco: terneDadiAttacco){
			for (Integer[] coppiaDifesa: coppiaDadiDifesa){
				Arrays.sort(ternaAttacco, dadiComparator);
				Arrays.sort(coppiaDifesa, dadiComparator);
				int iA = ternaAttacco[0]; int jA = ternaAttacco[1]; 
				int iD = coppiaDifesa[0]; int jD = coppiaDifesa[1]; 
				int attacchiRiusciti = 0;
				if (iA > iD) attacchiRiusciti++;
				if (jA > jD) attacchiRiusciti++;
				switch (attacchiRiusciti) {
				case 0:
					counter_0_2++;
					break;
				case 1:
					counter_1_1++;
					break;
				case 2:
					counter_2_0++;
					break;
				default:
					throw new IllegalStateException("attacchiRiusciti: "+attacchiRiusciti);
				}
				counter++;
			}
		}
		
		System.out.println("Combinazioni totali: ["+counter+"]");
		System.out.println("2 a 0: ["+counter_2_0+"] ["+(((float)counter_2_0)*100/counter)+"]");
		System.out.println("1 a 1: ["+counter_1_1+"] ["+(((float)counter_1_1)*100/counter)+"]");
		System.out.println("0 a 2: ["+counter_0_2+"] ["+(((float)counter_0_2)*100/counter)+"]");
	}
	
	public static void calcola3vs1(List<Integer[]> terneDadiAttacco, List<Integer> dadiDifesa) {

		int counter     = 0;
		int counter_1_0 = 0;
		int counter_0_1 = 0;
		
		for (Integer[] ternaAttacco: terneDadiAttacco){
			for (Integer dadoDifesa: dadiDifesa){
				Arrays.sort(ternaAttacco, dadiComparator);
				int iA = ternaAttacco[0];
				int iD = dadoDifesa; 
				int attacchiRiusciti = 0;
				if (iA > iD) attacchiRiusciti++;
				switch (attacchiRiusciti) {
				case 0:
					counter_0_1++;
					break;
				case 1:
					counter_1_0++;
					break;
				default:
					throw new IllegalStateException("attacchiRiusciti: "+attacchiRiusciti);
				}
				counter++;
			}
		}
		
		System.out.println("Combinazioni totali: ["+counter+"]");
		System.out.println("1 a 0: ["+counter_1_0+"] ["+(((float)counter_1_0)*100/counter)+"]");
		System.out.println("0 a 1: ["+counter_0_1+"] ["+(((float)counter_0_1)*100/counter)+"]");
	}

	public static void calcola2vs2(List<Integer[]> coppiaDadiAttacco, List<Integer[]> coppiaDadiDifesa) {

		int counter     = 0;
		int counter_0_2 = 0;
		int counter_2_0 = 0;
		int counter_1_1 = 0;
		
		for (Integer[] coppiaAttacco: coppiaDadiAttacco){
			for (Integer[] coppiaDifesa: coppiaDadiDifesa){
				Arrays.sort(coppiaAttacco, dadiComparator);
				Arrays.sort(coppiaDifesa, dadiComparator);
				int iA = coppiaAttacco[0]; int jA = coppiaAttacco[1]; 
				int iD = coppiaDifesa[0]; int jD = coppiaDifesa[1]; 
				int attacchiRiusciti = 0;
				if (iA > iD) attacchiRiusciti++;
				if (jA > jD) attacchiRiusciti++;
				switch (attacchiRiusciti) {
				case 0:
					counter_0_2++;
					break;
				case 1:
					counter_1_1++;
					break;
				case 2:
					counter_2_0++;
					break;
				default:
					throw new IllegalStateException("attacchiRiusciti: "+attacchiRiusciti);
				}
				counter++;
			}
		}
		
		System.out.println("Combinazioni totali: ["+counter+"]");
		System.out.println("2 a 0: ["+counter_2_0+"] ["+(((float)counter_2_0)*100/counter)+"]");
		System.out.println("1 a 1: ["+counter_1_1+"] ["+(((float)counter_1_1)*100/counter)+"]");
		System.out.println("0 a 2: ["+counter_0_2+"] ["+(((float)counter_0_2)*100/counter)+"]");
	}
	
	public static void calcola2vs1(List<Integer[]> coppiaDadiAttacco, List<Integer> dadiDifesa) {

		int counter     = 0;
		int counter_1_0 = 0;
		int counter_0_1 = 0;
		
		for (Integer[] dadiAttacco: coppiaDadiAttacco){
			for (Integer dadoDifesa: dadiDifesa){
				Arrays.sort(dadiAttacco, dadiComparator);
				int iA = dadiAttacco[0];
				int iD = dadoDifesa; 
				int attacchiRiusciti = 0;
				if (iA > iD) attacchiRiusciti++;
				switch (attacchiRiusciti) {
				case 0:
					counter_0_1++;
					break;
				case 1:
					counter_1_0++;
					break;
				default:
					throw new IllegalStateException("attacchiRiusciti: "+attacchiRiusciti);
				}
				counter++;
			}
		}
		
		System.out.println("Combinazioni totali: ["+counter+"]");
		System.out.println("1 a 0: ["+counter_1_0+"] ["+(((float)counter_1_0)*100/counter)+"]");
		System.out.println("0 a 1: ["+counter_0_1+"] ["+(((float)counter_0_1)*100/counter)+"]");
	}
	
	public static void calcola1vs1(List<Integer> dadiAttacco, List<Integer> dadiDifesa) {

		int counter     = 0;
		int counter_1_0 = 0;
		int counter_0_1 = 0;
		
		for (Integer dadoAttacco: dadiAttacco){
			for (Integer dadoDifesa: dadiDifesa){
				int iA = dadoAttacco;
				int iD = dadoDifesa; 
				int attacchiRiusciti = 0;
				if (iA > iD) attacchiRiusciti++;
				switch (attacchiRiusciti) {
				case 0:
					counter_0_1++;
					break;
				case 1:
					counter_1_0++;
					break;
				default:
					throw new IllegalStateException("attacchiRiusciti: "+attacchiRiusciti);
				}
				counter++;
			}
		}
		
		System.out.println("Combinazioni totali: ["+counter+"]");
		System.out.println("1 a 0: ["+counter_1_0+"] ["+(((float)counter_1_0)*100/counter)+"]");
		System.out.println("0 a 1: ["+counter_0_1+"] ["+(((float)counter_0_1)*100/counter)+"]");
	}
	
	private static List<Integer[]> combinazioniTernaDadi(){
		List<Integer[]> terneDadi = new ArrayList<Integer[]>();
		for (int i = 1; i <= 6; i++){
			for (int j = 1; j <= 6; j++){
				for (int k = 1; k <= 6; k++){
					Integer[] terna = new Integer[]{i,j,k};
					terneDadi.add(terna);
				}
			}
		}
		return terneDadi;
	}
	
	private static List<Integer[]> combinazioniCoppiaDadi(){
		List<Integer[]> coppieDadi = new ArrayList<Integer[]>();
		for (int i = 1; i <= 6; i++){
			for (int j = 1; j <= 6; j++){
				Integer[] coppia = new Integer[]{i,j};
				coppieDadi.add(coppia);
			}
		}
		return coppieDadi;
	}
	
	private static List<Integer> combinazioniDadi(){
		List<Integer> dadi = Arrays.asList(new Integer[]{1,2,3,4,5,6});
		return dadi;
	}
}
