@ECHO OFF
rem Lancia il caricamento delle schede così come specificato da configurazione ma la stessa può essere bypassata aggiungendo
rem uno o due argomenti alla fine: Se è uno solo, è la url della scheda riepilogativa delle prestigious. 
rem Se sono due, il primo è la url, l'altro un booleano (true/false) che indica se caricare solo le partite non già caricate o tutte.

java -cp RD3Analyzer.jar -DpathConfiguration="C:\WorkSpaces Eclipse\RisikoWorkSpace\Rd3Analyzer\configuration" it.desimone.rd3analyzer.main.LoaderSchede %1 %2