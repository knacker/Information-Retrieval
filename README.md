# Information-Retrieval

Zum ausführen der JAR Datei, muss die aktuelle Java Version(OpenJDK 16) installiert sein. Diese kann hier gefunden werden : https://jdk.java.net/16/.
Um das Programm zu starten, muss die Konsole im Information-Retrieval Ordner geöffnet werden und die Datei mittels "java -jar Information-Retrieval.jar" geöffnet werden.

Hinweise zur Eingabe:

Wenn der Suchterm nur ein einziges Wort enthält, kann man folgendermaßen dananch suchen:
Gebe einen Suchterm ein : Wort

Sobald der Suchterm mehrere Wörter umfasst, welche mittels einer Operation (Konjunktion "&", Disjunktion "|", Negation "!") verknüpft sind, muss die Eingabe folgende Form haben:
Gebe einen Suchterm ein : Operation ( Wort1 Wort2 ... Wortn )

Beispiel: fish AND fox
Gebe einen Suchterm ein : & ( fish fox )