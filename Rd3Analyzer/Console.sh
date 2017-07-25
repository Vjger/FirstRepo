#!/bin/bash
# Apre una console di dialogo che permette di effettuare in base alla scelta indicata le seguenti operazioni
# [0]: Help
# [1]: Avvio elaborazione in background
# [2]: Stop elaborazione in background
# [3]: Verifica status dell'elaborazione in background
# [4]: Avvio elaborazione delle schede partita manualmente
# [5]: Avvio elaborazione della ranking manualmente

# E' possibile anche lanciare la console direttamente con il comando a seguire accodandolo come parametro

java -cp RD3Analyzer.jar -DrootRD3Analyzer="/home/usr/RD3Analyzer" it.desimone.rd3analyzer.main.Console $1

