Esto codigo se creo por la colaboracion de dos personas Erick De la Rosa y Ernesto del billar 

La funcion ListaAdyacencia Esto crea una nueva instancia de LinkedHashMap. En resumen, esta función simplemente crea una instancia vacía de la lista de adyacencia, que se utilizará para almacenar los vértices y sus aristas en el grafo.

La funcion public void agregarVertice simplemente crea un vertice para agregarlo al grafo por medio la lista de adyacencia. Esta solo necesita el nombre del vertice que va a crear para poder correr propiamente.

La funcion public void eliminarVertice Se encarga de buscar y eleminar un vertice del grafo con todas las aristas que esten conectadas a el. Esta funcion solo requiere del nombre del vertice que va a eliminar para poder correr. la funcion comienza verificando con un
if si el nombre enviado se encuentra en el grafo para despues proceder a eliminarlo y acontinuacion entra en un bucle for para comenzar a borrar todas las Aristas que estaban conectadas a este vertice y si el primer if no se cumplia se procedia a else el que te 
dice que no se encontro un vertice con el nombre enviado.

La funcion public void editarVertice sirve para poder editar el nomnbre de un vertice ya creado. A esta funcion se le debe enviar el nombre del vertice que se quiere editar y el nombre por el que se le quiere cambiar. se comienza borrando el vertice ya existente y
creando el nuevo para despues pasar a un for que recore toda la lista para cambiar todas las aristas con el nombre del vector borrado por el del vector nuevo.

La funcion public void VerticesDisponibles muestra todos los vertices disponibles En el momento. La funcion simplemente recore la lista completa mostrando los vertices que se encuentran disponibles para poder poner aristas.

La funcion public void agregarArista como el nombre de la funcion lo dice esta funcion se encarga de crear una arista nueva. Esta funcion toma los nombres de dos vertices que son el origen y el destino, el peso y el tiempo de la arista. se comienza creando la arista despues
se crea la misma arista pero en direccion opuesta y se le pone el mismo peso y tiempo a ambas.

La funcion public void modificarArista Esta arista se encarga de cambiar el peso y el tiempo de una arista ya creada. se le ingresa el origen y el destino de la arista para poder identificar cual es la que se quiere cambiar y el nuevo peso y tiempo por el que se le
quiere cambiar. se comienza con un for para recorer todo el codigo en busca de la arista para poder cambiar el peso y el tiempo cuando se ecuentra para despues entrar a otro for que hara exactamente lo mismo pero con la arista en la direccion opuesta.

La funcion public boolean eliminarArista esta funcion se encarga de eliminar la arista indicada. Esta funcion recibe el nombre del vertice de ortigen y el de destino para poder identificar cual es la arista que se quiere eliminar. Se comienza el verificando si las
vertices de origen y de destino se encuentran en la lista para despues buscar si existe la arista que se quiere eliminar si se encuentra se elimina la arista en los dos sentido y si no se manda un mensaje dicienendo que no se pudo encontrar.

La funcion public boolean existeArista esta funcion busca si existe una arista indicada. A esta funcion se le manda el vector de origen y el vector de destino para despues verificar si existen los dos vectores introducidos y acontinuacion se verifica so existe la arista
que se crea por esos dos vectores si existe se devuelve true y si no se devuelve false.

La funcion public void aristasDisponibles se encarga de imprimir las aristas disponibles en el programa en ese momento. A esta funcion no se le manda ninguna valor. Se crea un conjunto (HashSet) llamado aristasImpresas para almacenar las aristas que ya se han impreso para 
evitar duplicados. Cada vez que se encuentra una arista que no se ha impreso previamente, se imprime su información, incluyendo el vértice de origen, el vértice de destino, el peso y el tiempo de la arista.

La funcion public void dijkstra se encarga de implementear el algoritmo dijkstra para encontrar la ruta más corta desde un vértice de origen hasta un vértice de destino en un grafo. Se crea una cola de prioridad (PriorityQueue) que ordena los vértices según su distancia 
mínima conocida desde el vértice de origen. La comparación de prioridad se basa en las distancias almacenadas en el mapa distancia. Mientras la cola de prioridad no esté vacía. Se extrae el vértice u con la distancia mínima conocida desde la cola de prioridad.
Se marca el vértice u como visitado. Para cada arista que sale del vértice u. Se obtiene el vértice v al que se llega a través de la arista. Se calcula la nueva distancia desde el vértice de origen hasta v a través de u. Si la nueva distancia es menor que la 
distancia actualmente conocida hasta v. Se actualiza la distancia a v. Se actualiza el camino más corto hacia v, estableciendo u como el vértice precedente en el camino. Se agrega v a la cola de prioridad para explorar sus vecinos.

La funcion public void floydWarshall esta implementa el algoritmo de Floyd-Warshall para encontrar las distancias mínimas entre todos los pares de vértices en un grafo ponderado dirigido o no dirigido. Se recorre el mapa de adyacencia listaAdyacencia para obtener las aristas y sus pesos.
Para cada arista (origen, destino, peso) en el grafo, se encuentra el índice de fila y columna correspondiente en la matriz distancias y se asigna el peso de la arista a la entrada correspondiente en la matriz. para despues implementarse el algoritmo de floyd-warshall
Después de completar el algoritmo, se imprime la matriz de distancias mínimas.

La funcion private int buscarIndice tiene el objetivo de devolver el indice del vertice dado. se crea el indice i = 0 y se entra a for buscando del vector introducido si no se encunetra se incrementa 1 a i hasta que se encuentra el vector y se devuelve i. Si 
llega al final de la lista y nunca se encuentra se procede a devolver -1 como resultado de que nunca se encontro.

La funcion public void rutaMasCorta calcula la ruta más corta entre un origen y un destino en términos de dos criterios: peso y tiempo. Usa el algoritmo de disktra se usa dos veces este metodo una para el peso y otra para el peso para que al final que Se imprime 
la ruta más corta en términos de peso, mostrando los vértices en el camino y el peso total y tambien se imprime la ruta más corta en términos de tiempo, mostrando los vértices en el camino y el tiempo total.

La funcion public void prim implementa el algoritmo prim en la funcion. Se crea una cola de prioridad (colaPrioridad) para almacenar las aristas que conectan los vértices visitados con los no visitados, ordenadas por peso. Mientras la cola de prioridad no esté 
vacía, se extrae la arista con el peso mínimo (arista) de la cola. Si el vértice destino de la arista ya ha sido visitado, se ignora y se continúa con la siguiente arista. De lo contrario, el vértice destino se marca como visitado. Para cada arista adyacente 
al vértice actual, se actualiza el peso mínimo y se agrega a la cola de prioridad si el peso es menor que el mínimo conocido anteriormente.

La funcion public void kruskal que implementa el algoritmo de Kruskal para encontrar el árbol de expansión mínima. El algoritmo kruskal implementado en el codigo se encarga de crear un arbol de expancion minima Una vez que se ha completado la construcción del 
árbol, se imprime cada arista del árbol junto con su peso.

la funcion public void imprimirLista se encarga de imprimir la lista entera en forma de vectores y aristas. simplemente son dos for que recoren toda la lista imprimiendo de forma ordenada todos los vectores y la aristas que tienen estos.

La funcion private void imprimirRuta se encarga de imprimir la ruta más corta entre un origen y un destino dado, utilizando un mapa que representa el camino más corto desde cada vértice hasta el origen. Esta funcion Se inicia un bucle donde se comienza desde el 
destino y se sigue el camino inverso hasta llegar al origen. Esto se hace utilizando un bucle for que sigue las referencias de vértice a vértice, obtenidas del mapa camino. Después de completar el bucle, se recorre el ArrayList ruta en orden inverso, comenzando 
desde el último vértice (origen) hasta el primer vértice (destino). Después de imprimir todos los vértices de la ruta en orden inverso, se imprime una nueva línea para separar las impresiones.

La funcion public static void menu proporciona un menu de opciones el cual cuenta con 8 opciones de la cuales algunas cuentan con subopciones En la que el usuario puede eleguir cualquiera entre ellas. La funcion comienza con un bucle para presentar el primer meno,
este bucle se va a repetir hasta que el usuairo eliga la opcion de salir del programa. las opciones se imprimen cada vez que comienza el bucle y de ahi el usuaio puede saber cual opciones tomar. 
En cada iteración de este bucle, se imprime el vértice de la ruta.
En cada iteración del bucle, se agrega el vértice actual (v) al ArrayList ruta.
