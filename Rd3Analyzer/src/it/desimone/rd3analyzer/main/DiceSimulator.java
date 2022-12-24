package it.desimone.rd3analyzer.main;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Random;

public class DiceSimulator {

	
	private static final Long ITERATIONS = 34846680L;
	private static final Long ITERATIONS_ATTACCHI = 100000000L;

	public static void main(String[] args) {

		//onlyDices();
		_3vs3Simulator();
		_3vs2Simulator();
		_3vs1Simulator();
		_2vs2Simulator();
		_2vs1Simulator();
		_1vs1Simulator();
	}

	
	public static void onlyDices(){

		long counter[] = new long[6];
		
		Random random = new Random();
		for (long index = 1; index <= ITERATIONS; index++){
			int dice = random.nextInt(6)+1;
			
			counter[dice-1]++;
		}
		
		for (Long count: counter){
			System.out.println(count);
		}
	}
	
	public static void _3vs3Simulator(){

		long counter_3_0 = 0;
		long counter_2_1 = 0;
		long counter_1_2 = 0;
		long counter_0_3 = 0;
		
		Random random = new Random();
		for (long index = 1; index <= ITERATIONS_ATTACCHI; index++){
			int attacco[] = new int[3];
			int difesa [] = new int[3];
			attacco[0] = random.nextInt(6)+1;
			difesa [0] = random.nextInt(6)+1;	
			attacco[1] = random.nextInt(6)+1;
			difesa [1] = random.nextInt(6)+1;	
			attacco[2] = random.nextInt(6)+1;
			difesa [2] = random.nextInt(6)+1;
			
			Arrays.sort(attacco);
			Arrays.sort(difesa);

			if (attacco[2] > difesa[2]){
				if (attacco[1] > difesa[1]){
					if (attacco[0] > difesa[0]){
						counter_3_0++;
					}else{
						counter_2_1++;
					}
				}else{
					if (attacco[0] > difesa[0]){
						counter_2_1++;
					}else{
						counter_1_2++;
					}
				}
			}else{
				if (attacco[1] > difesa[1]){
					if (attacco[0] > difesa[0]){
						counter_2_1++;
					}else{
						counter_1_2++;
					}
				}else{
					if (attacco[0] > difesa[0]){
						counter_1_2++;
					}else{
						counter_0_3++;
					}
				}	
			}	
		}
		BigDecimal totaleAttacchi3vs3 = new BigDecimal(counter_3_0+counter_2_1+counter_1_2+counter_0_3);
		BigDecimal perc3_0 = new BigDecimal(counter_3_0).divide(totaleAttacchi3vs3, 8, RoundingMode.HALF_UP);
		BigDecimal perc2_1 = new BigDecimal(counter_2_1).divide(totaleAttacchi3vs3, 8, RoundingMode.HALF_UP);
		BigDecimal perc1_2 = new BigDecimal(counter_1_2).divide(totaleAttacchi3vs3, 8, RoundingMode.HALF_UP);
		BigDecimal perc0_3 = new BigDecimal(counter_0_3).divide(totaleAttacchi3vs3, 8, RoundingMode.HALF_UP);
		System.out.println("3_0: "+counter_3_0+" "+perc3_0);
		System.out.println("2_1: "+counter_2_1+" "+perc2_1);
		System.out.println("1_2: "+counter_1_2+" "+perc1_2);
		System.out.println("0_3: "+counter_0_3+" "+perc0_3);
		System.out.println("Totale Attacchi 3vs3: "+totaleAttacchi3vs3);
	}
	
	
	public static void _3vs2Simulator(){

		long counter_2_0 = 0;
		long counter_1_1 = 0;
		long counter_0_2 = 0;
		
		Random random = new Random();
		for (long index = 1; index <= ITERATIONS_ATTACCHI; index++){
			int attacco[] = new int[3];
			int difesa [] = new int[2];
			attacco[0] = random.nextInt(6)+1;
			difesa [0] = random.nextInt(6)+1;	
			attacco[1] = random.nextInt(6)+1;
			difesa [1] = random.nextInt(6)+1;	
			attacco[2] = random.nextInt(6)+1;
			
			Arrays.sort(attacco);
			Arrays.sort(difesa);

			if (attacco[2] > difesa[1]){
				if (attacco[1] > difesa[0]){
					counter_2_0++;
				}else{
					counter_1_1++;
				}
			}else{
				if (attacco[1] > difesa[0]){
					counter_1_1++;
				}else{
					counter_0_2++;
				}
			}
		}
		BigDecimal totaleAttacchi3vs2 = new BigDecimal(counter_2_0+counter_1_1+counter_0_2);
		BigDecimal perc2_0 = new BigDecimal(counter_2_0).divide(totaleAttacchi3vs2, 8, RoundingMode.HALF_UP);
		BigDecimal perc1_1 = new BigDecimal(counter_1_1).divide(totaleAttacchi3vs2, 8, RoundingMode.HALF_UP);
		BigDecimal perc0_2 = new BigDecimal(counter_0_2).divide(totaleAttacchi3vs2, 8, RoundingMode.HALF_UP);
		System.out.println("2_0: "+counter_2_0+" "+perc2_0);
		System.out.println("1_1: "+counter_1_1+" "+perc1_1);
		System.out.println("1_2: "+counter_0_2+" "+perc0_2);
		System.out.println("Totale Attacchi 3vs2: "+totaleAttacchi3vs2);
	}
	
	public static void _3vs1Simulator(){

		long counter_1_0 = 0;
		long counter_0_1 = 0;
		
		Random random = new Random();
		for (long index = 1; index <= ITERATIONS_ATTACCHI; index++){
			int attacco[] = new int[3];
			int difesa  = 0;
			attacco[0] = random.nextInt(6)+1;
			difesa     = random.nextInt(6)+1;	
			attacco[1] = random.nextInt(6)+1;
			attacco[2] = random.nextInt(6)+1;
			
			Arrays.sort(attacco);

			if (attacco[2] > difesa || attacco[1] > difesa || attacco[0] > difesa){
				counter_1_0++;
			}else{
				counter_0_1++;
			}
		}
		BigDecimal totaleAttacchi3vs1 = new BigDecimal(counter_1_0+counter_0_1);
		BigDecimal perc1_0 = new BigDecimal(counter_1_0).divide(totaleAttacchi3vs1, 8, RoundingMode.HALF_UP);
		BigDecimal perc0_1 = new BigDecimal(counter_0_1).divide(totaleAttacchi3vs1, 8, RoundingMode.HALF_UP);
		System.out.println("1_0: "+counter_1_0+" "+perc1_0);
		System.out.println("0_1: "+counter_0_1+" "+perc0_1);
		System.out.println("Totale Attacchi 3vs1: "+totaleAttacchi3vs1);
	}
	
	public static void _2vs2Simulator(){

		long counter_2_0 = 0;
		long counter_1_1 = 0;
		long counter_0_2 = 0;
		
		Random random = new Random();
		for (long index = 1; index <= ITERATIONS_ATTACCHI; index++){
			int attacco[] = new int[2];
			int difesa [] = new int[2];
			attacco[0] = random.nextInt(6)+1;
			difesa [0] = random.nextInt(6)+1;	
			attacco[1] = random.nextInt(6)+1;
			difesa [1] = random.nextInt(6)+1;	
			
			Arrays.sort(attacco);
			Arrays.sort(difesa);

			if (attacco[1] > difesa[1]){
				if (attacco[0] > difesa[0]){
					counter_2_0++;
				}else{
					counter_1_1++;
				}
			}else{
				if (attacco[0] > difesa[0]){
					counter_1_1++;
				}else{
					counter_0_2++;
				}
			}
		}
		BigDecimal totaleAttacchi2vs2 = new BigDecimal(counter_2_0+counter_1_1+counter_0_2);
		BigDecimal perc2_0 = new BigDecimal(counter_2_0).divide(totaleAttacchi2vs2, 8, RoundingMode.HALF_UP);
		BigDecimal perc1_1 = new BigDecimal(counter_1_1).divide(totaleAttacchi2vs2, 8, RoundingMode.HALF_UP);
		BigDecimal perc0_2 = new BigDecimal(counter_0_2).divide(totaleAttacchi2vs2, 8, RoundingMode.HALF_UP);
		System.out.println("2_0: "+counter_2_0+" "+perc2_0);
		System.out.println("1_1: "+counter_1_1+" "+perc1_1);
		System.out.println("1_2: "+counter_0_2+" "+perc0_2);
		System.out.println("Totale Attacchi 2vs2: "+totaleAttacchi2vs2);
	}
	
	public static void _2vs1Simulator(){

		long counter_1_0 = 0;
		long counter_0_1 = 0;
		
		Random random = new Random();
		for (long index = 1; index <= ITERATIONS_ATTACCHI; index++){
			int attacco[] = new int[2];
			int difesa  = 0;
			attacco[0] = random.nextInt(6)+1;
			difesa     = random.nextInt(6)+1;	
			attacco[1] = random.nextInt(6)+1;
			
			Arrays.sort(attacco);

			if (attacco[1] > difesa || attacco[0] > difesa){
				counter_1_0++;
			}else{
				counter_0_1++;
			}
		}
		BigDecimal totaleAttacchi2vs1 = new BigDecimal(counter_1_0+counter_0_1);
		BigDecimal perc1_0 = new BigDecimal(counter_1_0).divide(totaleAttacchi2vs1, 8, RoundingMode.HALF_UP);
		BigDecimal perc0_1 = new BigDecimal(counter_0_1).divide(totaleAttacchi2vs1, 8, RoundingMode.HALF_UP);
		System.out.println("1_0: "+counter_1_0+" "+perc1_0);
		System.out.println("0_1: "+counter_0_1+" "+perc0_1);
		System.out.println("Totale Attacchi 2vs1: "+totaleAttacchi2vs1);
	}
	
	public static void _1vs1Simulator(){

		long counter_1_0 = 0;
		long counter_0_1 = 0;
		
		Random random = new Random();
		for (long index = 1; index <= ITERATIONS_ATTACCHI; index++){
			int attacco = 0;
			int difesa  = 0;
			attacco    = random.nextInt(6)+1;
			difesa     = random.nextInt(6)+1;	

			if (attacco > difesa){
				counter_1_0++;
			}else{
				counter_0_1++;
			}
		}
		BigDecimal totaleAttacchi1vs1 = new BigDecimal(counter_1_0+counter_0_1);
		BigDecimal perc1_0 = new BigDecimal(counter_1_0).divide(totaleAttacchi1vs1, 8, RoundingMode.HALF_UP);
		BigDecimal perc0_1 = new BigDecimal(counter_0_1).divide(totaleAttacchi1vs1, 8, RoundingMode.HALF_UP);
		System.out.println("1_0: "+counter_1_0+" "+perc1_0);
		System.out.println("0_1: "+counter_0_1+" "+perc0_1);
		System.out.println("Totale Attacchi 1vs1: "+totaleAttacchi1vs1);
	}
}
