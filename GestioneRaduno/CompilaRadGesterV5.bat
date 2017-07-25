@echo on
javac -target 1.5 -classpath .\lib\commons-lang-2.4.jar;.\lib\itextpdf-5.1.1.jar;.\lib\poi-3.7-20101029.jar .\src\it\desimone\utils\*.java .\src\it\desimone\risiko\torneo\batch\*.java .\src\it\desimone\risiko\torneo\utils\*.java .\src\it\desimone\risiko\torneo\dto\*.java .\src\it\desimone\risiko\torneo\panels\*.java -d .

jar cmf MANIFEST.MF GestioneRaduno.jar .\it

pause