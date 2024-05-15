# S2

## Grammatik for Leona-språket:

### Tokens som representerar respektive kommando:
FORW, BACK, LEFT, RIGHT, DOWN, UP, COLOR, REP
### Tokens som representerar punkt och citattecken:
PERIOD, QUOTE
### Token som representerar en hex-färg:
HEX
### Token som representerar ett heltal:
DECIMAL
### Token som representerar syntax-fel på lexikal nivå:
ERROR

### Tokenlista för Grammatiken
* FORW = "FORW"
* BACK = "BACK"
* RIGHT = "RIGHT"
* LEFT = "LEFT"
* UP = "UP"
* DOWN = "DOWN"
* COLOR = "COLOR"
* REP = "REP"
* HEX = "^#([A-Fa-f0-9]{6})$"
* DECIMAL = "[0-9]+"
* PERIOD = "."
* QUOTE = " " "
* ERROR = "ERROR"


### Backus-Naur form (BNF)
```
<Program> ::= <Instruction> | <Instruction> <Program>
<Instruction> ::= <Movement> | <Pen> | <Rotation> | <Color> | <Repeat>
<Movement> ::= FORW DECIMAL PERIOD | BACK DECIMAL PERIOD
<Pen> ::= UP PERIOD | DOWN PERIOD
<Rotation> ::= LEFT DECIMAL PERIOD | RIGHT DECIMAL PERIOD
<Color> ::= COLOR HEX PERIOD
<Repeat> ::= REP DECIMAL <Instruction> | REP DECIMAL <QuotedInstruction>
<QuotedInstruction> ::= QUOTE <Program> QUOTE
```
 
 


## Parseträd för testfall 12

### Härledning
Programmet Startar på ```<Program>``` 
 
Programmet Startar på ```<Program>``` 
 
Program &rarr; Instruction Program &rarr; Pen Program &rarr; DOWN PERIOD Program &rarr; DOWN PERIOD Instruction &rarr; DOWN PERIOD Repeat &rarr; DOWN PERIOD REP DECIMAL Instruction &rarr; DOWN PERIOD REP DECIMAL Repeat &rarr; DOWN PERIOD REP DECIMAL REP DECIMAL QuotedInstruction &rarr; DOWN PERIOD REP DECIMAL REP DECIMAL QUOTE Program QUOTE &rarr; DOWN PERIOD REP DECIMAL REP DECIMAL QUOTE Instruction QUOTE &rarr; DOWN PERIOD REP DECIMAL REP DECIMAL QUOTE Repeat QUOTE &rarr; DOWN PERIOD REP DECIMAL REP DECIMAL QUOTE REP DECIMAL Instruction QUOTE &rarr; DOWN PERIOD REP DECIMAL REP DECIMAL QUOTE REP DECIMAL Repeat QUOTE &rarr; DOWN PERIOD REP DECIMAL REP DECIMAL QUOTE REP DECIMAL REP DECIMAL QuotedInstruction QUOTE &rarr; DOWN PERIOD REP DECIMAL REP DECIMAL QUOTE REP DECIMAL REP DECIMAL QUOTE Program QUOTE &rarr; DOWN PERIOD REP DECIMAL REP DECIMAL QUOTE REP DECIMAL REP DECIMAL QUOTE Instruction Program QUOTE &rarr; DOWN PERIOD REP DECIMAL REP DECIMAL QUOTE REP DECIMAL REP DECIMAL QUOTE Movement Program QUOTE &rarr; DOWN PERIOD REP DECIMAL REP DECIMAL QUOTE REP DECIMAL REP DECIMAL QUOTE FORW DECIMAL PERIOD Program QUOTE &rarr; DOWN PERIOD REP DECIMAL REP DECIMAL QUOTE REP DECIMAL REP DECIMAL QUOTE FORW DECIMAL PERIOD Instruction QUOTE &rarr; DOWN PERIOD REP DECIMAL REP DECIMAL QUOTE REP DECIMAL REP DECIMAL QUOTE FORW DECIMAL PERIOD Rotation QUOTE &rarr; DOWN PERIOD REP DECIMAL REP DECIMAL QUOTE REP DECIMAL REP DECIMAL QUOTE FORW DECIMAL PERIOD LEFT DECIMAL PERIOD QUOTE.


### Parseträd

![Parseträd](parseTrädS2.png)
