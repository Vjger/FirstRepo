@ECHO OFF
rem Lancia la generazione della ranking per il mese in corso ma questo può essere bypassato aggiungendo
rem due argomenti alla fine che rappresentino il mese ed anno (in quest'ordine ed in formato numerico) su cui elaborare la ranking

java -cp RD3Analyzer.jar -DpathConfiguration="C:\WorkSpaces Eclipse\RisikoWorkSpace\Rd3Analyzer\configuration" it.desimone.rd3analyzer.main.TorneoRanking %1 %2