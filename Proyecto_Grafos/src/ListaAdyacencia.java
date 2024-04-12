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

    // Constructor para inicializar la lista de adyacencia con un número dado de vértices
    public ListaAdyacencia() {
        listaAdyacencia = new LinkedHashMap<>(); // Usa LinkedHashMap
    }

    // Método para agregar un vértice al grafo
    public void agregarVertice(String vertice) {
        listaAdyacencia.put(vertice, new ArrayList<>());
    }

    // Método para agregar una arista al grafo
    public void agregarArista(String origen, String destino, int peso) {
        listaAdyacencia.get(origen).add(new Arista(destino, peso));
        if (!origen.equals(destino)) {
            listaAdyacencia.get(destino).add(new Arista(origen, peso));
        }
    }

    // Método para imprimir la lista de adyacencia
    public void imprimirLista() {
        for (Map.Entry<String, ArrayList<Arista>> entry : listaAdyacencia.entrySet()) {
            System.out.print("Vértice " + entry.getKey() + " está conectado con: ");
            for (Arista arista : entry.getValue()) {
                System.out.print(arista.destino + "(peso: " + arista.peso + ") ");
            }
            System.out.println();
        }
    }

    // Método para ejecutar el algoritmo de Dijkstra y encontrar la ruta más corta entre dos vértices dados
    public void dijkstra(String origen, String destino) {
        Map<String, Integer> distancia = new HashMap<>(); // Distancia mínima desde el vértice origen
        Map<String, Boolean> visitado = new HashMap<>(); // Marcador para verificar si el vértice ha sido visitado
        Map<String, String> camino = new HashMap<>(); // Almacena el camino más corto desde el origen hasta cada vértice

        // Inicializar las distancias a infinito, los vértices como no visitados y el camino como desconocido (null)
        for (String vertice : listaAdyacencia.keySet()) {
            distancia.put(vertice, Integer.MAX_VALUE);
            visitado.put(vertice, false);
            camino.put(vertice, null);
        }

        distancia.put(origen, 0); // La distancia desde el origen hasta sí mismo es 0

        PriorityQueue<String> cola = new PriorityQueue<>((a, b) -> distancia.get(a) - distancia.get(b)); // Cola de prioridad para almacenar vértices según su distancia
        cola.add(origen); // Agregar el origen a la cola de prioridad

        while (!cola.isEmpty()) {
            String u = cola.poll(); // Tomar el vértice con la distancia mínima desde el origen
            visitado.put(u, true); // Marcar el vértice como visitado

            // Iterar sobre todas las aristas adyacentes al vértice actual
            for (Arista arista : listaAdyacencia.get(u)) {
                String v = arista.destino; // Destino de la arista
                int pesoUV = arista.peso; // Peso de la arista desde u hasta v

                // Actualizar la distancia si hay un camino más corto a través de u
                if (!visitado.get(v) && distancia.get(u) != Integer.MAX_VALUE && distancia.get(u) + pesoUV < distancia.get(v)) {
                    distancia.put(v, distancia.get(u) + pesoUV);
                    camino.put(v, u); // Actualizar el camino más corto hacia v
                    cola.add(v); // Agregar v a la cola de prioridad para explorar sus adyacentes
                }
            }
        }

        // Imprimir la distancia más corta desde el origen hasta el destino
        System.out.println("Distancia más corta desde " + origen + " hasta " + destino + ": " + distancia.get(destino));

        // Imprimir la ruta más corta
        System.out.print("Ruta más corta: ");
        imprimirRuta(origen, destino, camino);
    }

    // Método auxiliar para imprimir la ruta más corta
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

    // Método para ejecutar el algoritmo de Prim para encontrar el árbol de expansión mínima
    public void prim(String origen) {
        int numVertices = listaAdyacencia.size();

        // Estructuras para mantener el árbol de expansión mínima
        Map<String, Boolean> visitado = new HashMap<>();
        Map<String, String> padre = new HashMap<>();
        Map<String, Integer> pesoMinimo = new HashMap<>();

        // Inicialización de las estructuras
        for (String vertice : listaAdyacencia.keySet()) {
            visitado.put(vertice, false);
            padre.put(vertice, null);
            pesoMinimo.put(vertice, Integer.MAX_VALUE);
        }

        // Cola de prioridad para seleccionar la arista de menor peso
        PriorityQueue<Arista> cola = new PriorityQueue<>(numVertices, Comparator.comparingInt(a -> a.peso));

        // Iniciar desde el vértice origen
        pesoMinimo.put(origen, 0);
        cola.offer(new Arista(origen, 0));

        while (!cola.isEmpty()) {
            Arista arista = cola.poll();
            String actual = arista.destino;

            // Marcar el vértice actual como visitado
            visitado.put(actual, true);

            // Actualizar los pesos mínimos y padres de los vértices adyacentes
            for (Arista vecino : listaAdyacencia.get(actual)) {
                String destino = vecino.destino;
                int peso = vecino.peso;
                if (!visitado.get(destino) && peso < pesoMinimo.get(destino)) {
                    padre.put(destino, actual);
                    pesoMinimo.put(destino, peso);
                    // Si ya existe en la cola, se elimina y se añade con el nuevo peso
                    cola.removeIf(a -> a.destino.equals(destino));
                    cola.offer(new Arista(destino, peso));
                }
            }
        }

        // Imprimir el árbol de expansión mínima
        System.out.println("Árbol de expansión mínima desde el vértice " + origen + ":");
        for (Map.Entry<String, String> entry : padre.entrySet()) {
            String vertice = entry.getKey();
            String padreVertice = entry.getValue();
            if (padreVertice != null) {
                int peso = pesoMinimo.get(vertice);
                System.out.println("Arista: " + padreVertice + " - " + vertice + " (Peso: " + peso + ")");
            }
        }
    }

    // Método main para probar la implementación
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
