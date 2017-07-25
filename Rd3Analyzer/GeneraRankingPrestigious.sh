#!/bin/bash
# Lancia la generazione della ranking per il mese in corso ma questo può essere bypassato aggiungendo
# due argomenti alla fine che rappresentino il mese ed anno (in quest'ordine ed in formato numerico) su cui elaborare la ranking

java -cp RD3Analyzer.jar -DrootRD3Analyzer="/home/usr/RD3Analyzer" it.desimone.rd3analyzer.main.TorneoRanking $1 $2