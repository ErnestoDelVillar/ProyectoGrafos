import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap; // Importa LinkedHashMap
import java.util.Map;
import java.util.PriorityQueue;

class Arista {
    String destino;
    int peso;

    public Arista(String destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }
}

public class ListaAdyacencia {

    private Map<String, ArrayList<Arista>> listaAdyacencia;

    // Constructor para inicializar la lista de adyacencia con un n�mero dado de v�rtices
    public ListaAdyacencia() {
        listaAdyacencia = new LinkedHashMap<>(); // Usa LinkedHashMap
    }

    // M�todo para agregar un v�rtice al grafo
    public void agregarVertice(String vertice) {
        listaAdyacencia.put(vertice, new ArrayList<>());
    }

    // M�todo para agregar una arista al grafo
    public void agregarArista(String origen, String destino, int peso) {
        listaAdyacencia.get(origen).add(new Arista(destino, peso));
        if (!origen.equals(destino)) {
            listaAdyacencia.get(destino).add(new Arista(origen, peso));
        }
    }

    // M�todo para imprimir la lista de adyacencia
    public void imprimirLista() {
        for (Map.Entry<String, ArrayList<Arista>> entry : listaAdyacencia.entrySet()) {
            System.out.print("V�rtice " + entry.getKey() + " est� conectado con: ");
            for (Arista arista : entry.getValue()) {
                System.out.print(arista.destino + "(peso: " + arista.peso + ") ");
            }
            System.out.println();
        }
    }

    // M�todo para ejecutar el algoritmo de Dijkstra y encontrar la ruta m�s corta entre dos v�rtices dados
    public void dijkstra(String origen, String destino) {
        Map<String, Integer> distancia = new HashMap<>(); // Distancia m�nima desde el v�rtice origen
        Map<String, Boolean> visitado = new HashMap<>(); // Marcador para verificar si el v�rtice ha sido visitado
        Map<String, String> camino = new HashMap<>(); // Almacena el camino m�s corto desde el origen hasta cada v�rtice

        // Inicializar las distancias a infinito, los v�rtices como no visitados y el camino como desconocido (null)
        for (String vertice : listaAdyacencia.keySet()) {
            distancia.put(vertice, Integer.MAX_VALUE);
            visitado.put(vertice, false);
            camino.put(vertice, null);
        }

        distancia.put(origen, 0); // La distancia desde el origen hasta s� mismo es 0

        PriorityQueue<String> cola = new PriorityQueue<>((a, b) -> distancia.get(a) - distancia.get(b)); // Cola de prioridad para almacenar v�rtices seg�n su distancia
        cola.add(origen); // Agregar el origen a la cola de prioridad

        while (!cola.isEmpty()) {
            String u = cola.poll(); // Tomar el v�rtice con la distancia m�nima desde el origen
            visitado.put(u, true); // Marcar el v�rtice como visitado

            // Iterar sobre todas las aristas adyacentes al v�rtice actual
            for (Arista arista : listaAdyacencia.get(u)) {
                String v = arista.destino; // Destino de la arista
                int pesoUV = arista.peso; // Peso de la arista desde u hasta v

                // Actualizar la distancia si hay un camino m�s corto a trav�s de u
                if (!visitado.get(v) && distancia.get(u) != Integer.MAX_VALUE && distancia.get(u) + pesoUV < distancia.get(v)) {
                    distancia.put(v, distancia.get(u) + pesoUV);
                    camino.put(v, u); // Actualizar el camino m�s corto hacia v
                    cola.add(v); // Agregar v a la cola de prioridad para explorar sus adyacentes
                }
            }
        }

        // Imprimir la distancia m�s corta desde el origen hasta el destino
        System.out.println("Distancia m�s corta desde " + origen + " hasta " + destino + ": " + distancia.get(destino));

        // Imprimir la ruta m�s corta
        System.out.print("Ruta m�s corta: ");
        imprimirRuta(origen, destino, camino);
    }

    // M�todo auxiliar para imprimir la ruta m�s corta
    private void imprimirRuta(String origen, String destino, Map<String, String> camino) {
        ArrayList<String> ruta = new ArrayList<>();
        for (String v = destino; v != null; v = camino.get(v)) {
            ruta.add(v);
        }
        for (int i = ruta.size() - 1; i >= 0; i--) {
            System.out.print(ruta.get(i) + " ");
        }
        System.out.println();
    }

    // M�todo para ejecutar el algoritmo de Prim para encontrar el �rbol de expansi�n m�nima
    public void prim(String origen) {
        int numVertices = listaAdyacencia.size();

        // Estructuras para mantener el �rbol de expansi�n m�nima
        Map<String, Boolean> visitado = new HashMap<>();
        Map<String, String> padre = new HashMap<>();
        Map<String, Integer> pesoMinimo = new HashMap<>();

        // Inicializaci�n de las estructuras
        for (String vertice : listaAdyacencia.keySet()) {
            visitado.put(vertice, false);
            padre.put(vertice, null);
            pesoMinimo.put(vertice, Integer.MAX_VALUE);
        }

        // Cola de prioridad para seleccionar la arista de menor peso
        PriorityQueue<Arista> cola = new PriorityQueue<>(numVertices, Comparator.comparingInt(a -> a.peso));

        // Iniciar desde el v�rtice origen
        pesoMinimo.put(origen, 0);
        cola.offer(new Arista(origen, 0));

        while (!cola.isEmpty()) {
            Arista arista = cola.poll();
            String actual = arista.destino;

            // Marcar el v�rtice actual como visitado
            visitado.put(actual, true);

            // Actualizar los pesos m�nimos y padres de los v�rtices adyacentes
            for (Arista vecino : listaAdyacencia.get(actual)) {
                String destino = vecino.destino;
                int peso = vecino.peso;
                if (!visitado.get(destino) && peso < pesoMinimo.get(destino)) {
                    padre.put(destino, actual);
                    pesoMinimo.put(destino, peso);
                    // Si ya existe en la cola, se elimina y se a�ade con el nuevo peso
                    cola.removeIf(a -> a.destino.equals(destino));
                    cola.offer(new Arista(destino, peso));
                }
            }
        }

        // Imprimir el �rbol de expansi�n m�nima
        System.out.println("�rbol de expansi�n m�nima desde el v�rtice " + origen + ":");
        for (Map.Entry<String, String> entry : padre.entrySet()) {
            String vertice = entry.getKey();
            String padreVertice = entry.getValue();
            if (padreVertice != null) {
                int peso = pesoMinimo.get(vertice);
                System.out.println("Arista: " + padreVertice + " - " + vertice + " (Peso: " + peso + ")");
            }
        }
    }

    // M�todo main para probar la implementaci�n
    public static void main(String[] args) {
        ListaAdyacencia grafo = new ListaAdyacencia();

        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarVertice("D");
        grafo.agregarVertice("E");
        grafo.agregarVertice("F");

        grafo.agregarArista("A", "B", 4);
        grafo.agregarArista("A", "C", 8);
        grafo.agregarArista("B", "C", 11);
        grafo.agregarArista("B", "D", 8);
        grafo.agregarArista("C", "E", 7);
        grafo.agregarArista("D", "E", 2);
        grafo.agregarArista("D", "F", 4);
        grafo.agregarArista("E", "F", 9);

        grafo.imprimirLista();
        System.out.println();

        grafo.dijkstra("A", "F");
        System.out.println();

        grafo.prim("A");
        System.out.println();
    }
}
