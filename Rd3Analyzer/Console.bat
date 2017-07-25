@ECHO OFF
rem Apre una console di dialogo che permette di effettuare in base alla scelta indicata le seguenti operazioni
rem [0]: Help
rem [1]: Avvio elaborazione in background
rem [2]: Stop elaborazione in background
rem [3]: Verifica status dell'elaborazione in background
rem [4]: Avvio elaborazione delle schede partita manualmente
rem [5]: Avvio elaborazione della ranking manualmente

rem E' possibile anche lanciare la console direttamente con il comando a seguire accodandolo come parametro

java -cp RD3Analyzer.jar -DpathConfiguration="C:\WorkSpaces Eclipse\RisikoWorkSpace\Rd3Analyzer\configuration" it.desimone.rd3analyzer.main.Console %1