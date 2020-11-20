AIAD - Grupo 13

António Dantas
João Macedo
Vítor Gonçalves

-----------------------------------------

Para correr o nosso projeto recomendamos o uso do IDE Eclipse.
Deverá adicionar ao class Path do projeto o jade.Jar e executar o programa através do ficheiro src/JADELauncher.java

Para alterar os parametros de entrada do SMA basta abrir o ficheiro JADELauncher.java. 
Dentro desse ficheiro irá encontrar a função launchBuilding() que contém a seguinte linha => String[] args = {"18", "3", "600.0", "2.5", "5.0", "1"}; 
Os valores significam, respetivamente, nmrFloors, nmrLifts, maxWeight per lift, lift maxSpeed, distance between floors, timeAtFloor(time the lift stops on floors for people to enter and exit)

Sempre que o sistema for abortado irão ser criados alguns ficheiros csv(no folder analysis) para propósitos de análise.
De realçar que o arquivo submetido inclui já alguns ficheiros csv, que foram gerados após correr instancias do SMA e foram usados para criar os resultados das experiencias que se encontram no relatório.
