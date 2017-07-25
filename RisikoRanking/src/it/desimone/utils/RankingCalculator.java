package it.desimone.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import it.desimone.risiko.torneo.dto.GiocatoreDTO;

public class RankingCalculator {
	
	public  static final BigDecimal RANKING_INIZIALE = new BigDecimal(1500); 
	private static final BigDecimal FATTORE_MOLTIPLICATIVO = new BigDecimal(30); 
	private static final BigDecimal NORMALIZZATORE = new BigDecimal(400);
	private static final BigDecimal TEN = new BigDecimal(10);

	public static BigDecimal computeProbabilityOfVictoryAgainstAll(GiocatoreDTO giocatore, List<GiocatoreDTO> avversari){
		BigDecimal result = null;
		BigDecimal divisore = BigDecimal.ONE;
		for (GiocatoreDTO avversario: avversari){
			divisore = divisore.add(computeFactoryAvsB(giocatore, avversario));
		}
		result = BigDecimal.ONE.divide(divisore, MathContext.DECIMAL32); //ERRORE
		//System.out.println("divisore: "+divisore+" probabilità: "+result);
		return result;
	}
	
	public static BigDecimal computeProbabilityOfVictoryAgainstAll(BigDecimal giocatore, List<BigDecimal> avversari){
		BigDecimal result = null;
		BigDecimal divisore = BigDecimal.ONE;
		for (BigDecimal avversario: avversari){
			divisore = divisore.add(computeFactoryAvsB(giocatore, avversario));
		}
		result = BigDecimal.ONE.divide(divisore, MathContext.DECIMAL32);
		return result;
	}
	
	private static BigDecimal computeFactoryAvsB(GiocatoreDTO a, GiocatoreDTO b){
		BigDecimal result = null;
		
		BigDecimal potenza = (b.getRanking().subtract(a.getRanking())).divide(NORMALIZZATORE);
        // Perform X^(A+B)=X^A*X^B (B = remainder)
        double dn1 = TEN.doubleValue();
        BigDecimal remainderOf2 = potenza.remainder(BigDecimal.ONE);
        BigDecimal n2IntPart = potenza.subtract(remainderOf2);
        BigDecimal intPow = TEN.pow(n2IntPart.intValueExact());
        BigDecimal doublePow = new BigDecimal(Math.pow(dn1, remainderOf2.doubleValue()));
        result = intPow.multiply(doublePow);
		
		return result;
	}
	
	private static BigDecimal computeFactoryAvsB(BigDecimal a, BigDecimal b){
		BigDecimal result = null;
		
		BigDecimal potenza = (b.subtract(a)).divide(NORMALIZZATORE);
        // Perform X^(A+B)=X^A*X^B (B = remainder)
        double dn1 = TEN.doubleValue();
        BigDecimal remainderOf2 = potenza.remainder(BigDecimal.ONE);
        BigDecimal n2IntPart = potenza.subtract(remainderOf2);
        BigDecimal intPow = TEN.pow(n2IntPart.intValueExact());
        BigDecimal doublePow = new BigDecimal(Math.pow(dn1, remainderOf2.doubleValue()));
        result = intPow.multiply(doublePow);
		
		return result;
	}
	
	public static BigDecimal computeNewRanking(GiocatoreDTO giocatore, List<GiocatoreDTO> avversari, boolean isVincitore){
		BigDecimal newRanking = null;
		BigDecimal oldRanking = giocatore.getRanking();
		
		BigDecimal probabilitaVittoria = computeProbabilityOfVictoryAgainstAll(giocatore, avversari);
		if (isVincitore){
			newRanking = oldRanking.add(FATTORE_MOLTIPLICATIVO.multiply(BigDecimal.ONE.subtract(probabilitaVittoria)));
		}else{
			newRanking = oldRanking.add(FATTORE_MOLTIPLICATIVO.multiply(BigDecimal.ZERO.subtract(probabilitaVittoria)));
		}
		return newRanking;
	}
	
	public static void main (String[] args){
		BigDecimal newRanking = null;
		BigDecimal oldRanking = new BigDecimal("1507.7715930");
		boolean isVincitore = true;
		List<BigDecimal> avversari = new ArrayList<BigDecimal>();
		avversari.add(new BigDecimal("1489.0084230"));
		avversari.add(new BigDecimal("1514.8977600"));
		avversari.add(new BigDecimal("1513.3711650"));
		BigDecimal probabilitaVittoria = computeProbabilityOfVictoryAgainstAll(oldRanking, avversari);
		System.out.println("probabilitaVittoria: "+probabilitaVittoria);
		if (isVincitore){
			newRanking = oldRanking.add(FATTORE_MOLTIPLICATIVO.multiply(BigDecimal.ONE.subtract(probabilitaVittoria)));
		}else{
			newRanking = oldRanking.add(FATTORE_MOLTIPLICATIVO.multiply(BigDecimal.ZERO.subtract(probabilitaVittoria)));
		}
		System.out.println("oldRanking: "+oldRanking+" newRanking "+newRanking);
	}
	
}
