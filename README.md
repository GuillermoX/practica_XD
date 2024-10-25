# A ESTRUCTURA JUEGO 3 EN RAYA USANDO CONEXIÓN UDP

La estructura cliente-servidor es la siguiente:
- Una máquina hace de servidor, la cual controlará el flujo del juego (turnos de jugadores
  estado del tablero, ganador, etc.)
- Cada jugador corresponde a un cliente, por lo que se necesitan dos maquinas para poder jugar al juego.
  Los clientes se encargarán de iniciar la conexión, hacer sus jugadas, recibir la información del servidor, etc.

Concretamente, el cliente será una clase llamada Client3ER (Cliente 3 En Raya) y el servidor será otra clase llamada Server3ER.
Cada clase tendrá su función main correspondiente, ya que cada una de estas clases generará un ejecutable para cada una de las máquinas
(maquinas cliente y máquina servidor). Esto significa que para jugar no hace falta tener el ejecutable del servidor ya que sólo se usará
el de cliente. Del mismo modo la máquina servidor sólo necesitará el ejecutable de la clase Server3ER.
Más adelante se  explicán unas clases adicionales que hará falta implementar.

La información que hay que transferirse entre cliente y servidor es la siguiente:
- Estado del tablero:
    El servidor debe enviar al cliente como se encuentra el tablero actualmente para que el jugador decida cómo actuar.
    De mismo modo el cliente debe comunicarle al servidor el estado del tablero luego de ejecutar la jugada.
- Estado del juego:
    El servidor debe comunicarle al cliente el estado de la partida para que el cliente pueda reaccionar según este.
    El estado significa si el jugador ha perdido o si le toca jugar.

Para codificar esta información se utilitzará un array de bytes de tamaño 10, donde cada byte symbolizará:
byteArray[0] -> Bite de información del estado del juego (A continuación se especifica la codificación de los estados del juego)
byteArray[1] hasta byteArray[9] -> Byte del estado del tablero (También se especifica a continuación la codificación)

La posición 0 del array podrá tener los siguientes valores:
    -1 -> Error de algun tipo (se expandirá en un futuro cuándo haga falta pero probablemente sea información del server hacia el cliente)
    0 -> Intento de conexión por parte del cliente hacia el servidor
         Conexión correcta si el servidor retorna este byte a 0
    1 -> Turno del jugador (información del servidor al jugador)
    2 -> Partida ganada (información del servidor al jugador)
    3 -> Partida perdida (información del servidor al jugador)

En cuanto a las posiciones 1 a 9 del array corresponde a la siguiente distribución en el tablero:
    posición 1, 2, 3 (primera fila)
    posición 4, 5, 6 (segunda fila)
    posición 7, 8, 9 (tercera fila)

    Cada posición del tablero podrá contener 3 valores que codifican el estado de la casilla:
        0 -> Casilla sin marcar (símbolo vacío)
        1 -> Casilla marcada por el jugador 1 (símbolo X)
        2 -> Casilla marcada por el jugador 2 (símbolo O)

Sabiendo esto, a continuación se especifica la estructura y lógica del cliente, servidor y clases adicionales, concretamente tendremos:
- Clase Client3ER
- Clase Server3ER
    - Clase Jugador (sólo se utiliza en el servidor)
- Clase Tablero


Es importante pensar durante el desarollo de cada clase únicamente en esta clase que estamos creando. Quién haga la clase Client3ER debe
saber cómo interactuar con la clase Server3ER, pero no hace falta conocer cómo esta funciona.
(Solo lo comento para que no os ralleis al principio intentando entender cómo funciona lo de los demás, primero cada uno hace lo suyo y después
se pone en común para el informe y la presentación)

-------------------------------------------------------------------------------------------------------------------------------------------------------------
    Clase Client3ER

La clase Client3ER será la que se encargará de contener la rutina main que se ejecutará en la máquina cliente.

Esta clase sólo contendrá la rutina public static void main(String[] args), aunque se pueden añadir rutinas 
adicionales de soporte. Lo importante es que no se deberá crear ninguna clase de apoyo a parte de la clase Tablero.

La lógica del cliente es la siguiente (la escribo en pseudocodigo).
Todo lo escrito como funciones no tiene por que ser funciones a parte en la versión real, sino que es una forma de escribir lo que se debe hacer en esa
parte del código.
```
    main(){
        
        //Se crea un bucle infinito ya que para parar la ejecución se hará un return en una de las opciones del menú
        while(1){
            do{

                mostrarMenuInicio();
                leerRespuestaJugador();

                switch{
                    caso CONECTAR_SERVER: enviarPaqueteInicioConnexion()
                                          esperarRespuesta() 
                    caso SALIR: detenerEjecucion() //Enchufale un return aqui que no estamos en progra
                }
                //Hasta que el servidor no especifique que toca jugar o la conexión es correcta no se iniciará el juego
                //Se tiene que recibir 1 (toca jugar) o 0 (conexión correcta pero le toca al otro) en el primer byte
                if(!conexionCorrecta()) mostrarCodigoError();
            }while(!conexionCorrecta());

            //En caso de que la conexión inicial sea correcta pero no toque jugar
            if(!turnoJugador()){
                imprimirTablero();
                //Esperar a que juegue primero el otro jugador
                esperarRespuestaServidor();
            }

            do{ 
                imprimirTablero();


                ejecutarJugada(); //Se tiene que modificar el tablero
                //Se imprime el tablero con tu jugada actual
                imprimirTablero();

                //Se espera a que el otro jugador realice su jugada
                esperarRespuestaServidor();
                info = procesarPaquete(); //Hay que ver si el byte de info indica 1 (seguir jugando), 2(ganado), 3(perdido)
            } while(info != ganado && info != perdido);

            //Se imprime el tablero en su estado final (todas las casillas llenas pq alguien se ha acabado la partida)
            imprimirTablero();
            if(ganado) {mostrarMensajeGanador();}
            else { mostrarMensajePerdedor();}
        } 
    }
```

Hay que remarcar que no se ha especificado nada de cómo se debe recibir, enviar y tratar la información de los paquetes.
La info de como hacer eso está en el moodle de XD, en un pdf del laboratorio 3.

Importante distinguir la diferencia entre el paquete (donde se incluye el byte de info junto a los bytes de tablero en un mismo array de bytes) y el tablero
(dónde solo está la info de este en forma de array de bytes también). Yo recomiendo cada vez que se reciba el paquete, desglosarlo en una variable simple byte y
el array byte[] que contiene el tablero. 

Relacionado con el tablero, quién haga el cliente también tendrá que saber que funcionalidades tiene la clase Tablero, que será desarollada por otra persona.
Por lo que a continuación está la información sobre la estructura del tablero.


----------------------------------------------------------------------------------------------------------------------------------------------------------------
    Clase Tablero


La clase tablero tiene que contener el siguiente atributo:
    private byte[] tablero;

Este atributo será la codificación del tablero antes mencionada (únicamente del tablero, por lo que tendrá 9 posiciones)

Los métodos que tendrá que contener esta clase són los siguientes:

```
    /**
    * Constructor de la clase Tablero. Inicializa el tablero todo a 0
    *
    public Tablero();


    /**
    * Devuelve el tablero en forma de array de bytes (devolver copia del atributo tablero de la clase)
    * @return tablero en forma de array de bytes
    *
    public byte[] rawTablero();
```
> Para desarrollar el método anterior recomiendo usar el método .clone() que viene incluido en los arrays de cualquier tipo

```
    /**
    * Genera una copia de la clase Tablero
    * @return Tablero copia del Tablero
    *
    public Tablero cpyTablero();
```
> Importante, la diferencia de cpyTablero() con el de rawTablero() es que este devuelve una copia de la clase Tablero, no un array de bytes.
> Recomiendo crear un nuevo tablero y inicializarlo con el constructor. Luego crear una copia del array atributo tablero y asignarla al nuevo tablero creado.

```
    /**
    * Modifica el tablero según el jugador y la posición escogida
    * @param jugador
    * @param posicion
    * @return boolean cierto -> se ha podido hacer la jugada // falso -> no se ha podidio hacer
    *
    public boolean jugadaTablero(int jugador, int posicion);
```
> Hay que acordarse de que puede que el jugador intente hacer una jugada imposible (colocar su ficha donde ya hay otra).

```
/**
* Informa del ganador del tablero
* @return int 0 -> no ha ganado nadie todavía / 1 -> ha ganado el jugador 1 / 2 -> ha ganado el jugador 2
*
public int ganador();



/**
* Imprime por pantalla el tablero actual
*
public void imprimirTablero();
```
> Para esta función de imprimirTablero se pueden usar prints en pantalla. Sobretodo intentar que quede bonito y facil de leer.



Esas son las funciones que s:w
ue deben crear para la clase Tablero en un principio. En caso de necesitarse más se avisará.




---------------------------------------------------------------------------------------------------------------------------------
    CLASE SERVER3ER

Esta clase se intentará que sea la primera en desarollar, por lo que directamente se proporcionará el codigo a continuación cuando
se acabe.
```
main(){
    do{
        cocinandoCodigo();
    } while(!morirDeSueño)
}
```