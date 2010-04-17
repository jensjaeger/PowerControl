PowerControl 
============
Java-Software (Kommandozeile) zum Auslesen und Schalten der Net-PwrCtrl(http://www.anel-elektronik.de/english/Produkte/products.html) Steckdosenleisten:

Voraussetzungen:
================

1.) Net-PwrCtrl Steckdosenleiste mit Firmware 3.0
2.) PC mit Mac OS X, Linux oder Windows und Java 1.5 (oder höher)


Verwendung:
===========

1.) Beispiel: Auslesen des aktuellen Status via UDP

Die IP-Adresse (hier: 192.168.1.13) ist die Adresse der Steckdosenleiste.
Der voreingestellte Port 77 wurde mit Hilfe der Web-Applikation auf 7777 geändert, da sonst Administrator-Rechte für das "Lauschen" an diesem Port nötig wären (gilt für jeden Port < 1024):

java -jar PowerControl.jar -c QUERY -h 192.168.1.13 -p user -x UDP -ur 7777 -up 75

Ausgabe:

Name1: NET-PwrCtrl
Name2: NET-CONTROL    
IP-Adresse: 192.168.1.13
Maske: 255.255.255.0
Gateway: 192.168.1.1
MAC-Adresse: 10:04:A3:09:0C:F1
HTTP-Port: 80
Steckdosen-Nummer-1: Aus
Steckdosen-Nummer-2: Aus
Steckdosen-Nummer-3: Aus


2.) Einschalten der ersten beiden Steckdosen via UDP:

java -jar PowerControl.jar -c SWITCH -h 192.168.1.13 -p user -x UDP -ur 7777 -up 75 -o 1=ON -o 2=ON


3.) Auslesen des aktuellen Status via HTTP (Ergebnis als XML):

Bei der Benutzung des HTTP-Protokolls muß immer der Login (hier: user) UND das Passwort (hier: user) angegeben werden:

java -jar PowerControl.jar -c QUERY -h 192.168.1.13 -l user -p user -k XML

Ausgabe:

<powerControl>
  <name1>NET-PwrCtrl</name1>
  <name2>NET-CONTROL    </name2>
  <host>192.168.1.13</host>
  <mask>255.255.255.0</mask>
  <gateway>192.168.1.1</gateway>
  <macAddress>10:04:A3:09:0C:F1</macAddress>
  <outlets>
    <entry>
      <outlet>1</outlet>
      <state>ON</state>
    </entry>
    <entry>
      <outlet>2</outlet>
      <state>ON</state>
    </entry>
    <entry>
      <outlet>3</outlet>
      <state>OFF</state>
    </entry>
  </outlets>
  <httpPort>80</httpPort>
</powerControl>


4.) Einschalten der zweiten und dritten Steckdose via HTTP:

java -jar PowerControl.jar -c SWITCH -h 192.168.1.13 -l user -p user -o 2=ON -o 3=ON

Ausgabe:

Steckdosen-Nummer-1: Aus
Steckdosen-Nummer-2: An
Steckdosen-Nummer-3: An


5.) Mithilfe des Status "TOGGLE" kann der Zustand einer Steckdose invertiert werden (funktioniert auch mit UDP):

java -jar PowerControl.jar -c SWITCH -h 192.168.1.13 -l user -p user -o 1=TOGGLE

Ausgabe:

Steckdosen-Nummer-1: An
Steckdosen-Nummer-2: Aus
Steckdosen-Nummer-3: Aus



Abschließend noch eine Aufzählung aller Parameter:

usage: PowerControl -c <command> [-f <output file>] -h <host> [-k <kind of output>] [-l <login/user>] [-o <outlet>] -p
       <password> [-t <timeout>] [-up <port>] [-ur <receive port>] [-x <protocol>]

PowerControl - THE MARVELLOUS JAVA BASED POWER CONTROL TOOL:
 -c <command>          command to execute (SWITCH, QUERY)
 -f <output file>      name of the output file
 -h <host>             IP address of the multiple socket outlet
 -k <kind of output>   kind of output (TXT=default, XML)
 -l <login/user>       login/user for HTTP
 -o <outlet>           outlet to switch (e.g. 1=ON|OFF|TOGGLE)
 -p <password>         password for UDP/HTTP
 -t <timeout>          timeout in ms (default: 5000)
 -up <port>            port for UDP (default: 75)
 -ur <receive port>    receive port for UDP (default: 77)
 -x <protocol>         protocol for command (UDP, HTTP=default)


Credit
======

This Software was created by Christoph Nowak

Its now hosted on github by Jens Jäger(http://www.jensjaeger.com) so it can live on.

License
=======

Don't know. Ask Christoph.
