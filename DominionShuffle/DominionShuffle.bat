@echo on
REM Il programma prende da 0 a 7 argomenti
REM Il primo argomento indica il numero di set Base da usare: il default è 3
REM Il secondo argomento indica il numero di tavoli da estrarre: il default è 6
REM Ogni argomento successivo al primo indica quali espansioni utilizzare: se non indicato alcun argomento verranno usate tutte altrimenti
REM si dovrà indicare I per Intrigo - S per Seaside - P per Prosperità - A per Alchimia - N per Nuovi Orizzonti

REM Ad esempio  "java -jar DominionShuffle.jar 2 5 A N" indica che verranno estratti 5 tavoli con 2 set base + le espansioni Alchimia e Nuovi Orizzonti 

java -jar DominionShuffle.jar 2 5

pause