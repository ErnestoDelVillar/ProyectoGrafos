import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

class Arista {
    String origen;
    String destino;
    int peso;
    int tiempo;
    

    public Arista(String origen, String destino, int peso, int tiempo) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
        this.tiempo = tiempo;
    }
}

public class ListaAdyacencia {

    private Map<String, ArrayList<Arista>> listaAdyacencia;

    public ListaAdyacencia() {
        listaAdyacencia = new LinkedHashMap<>();
    }

    public void agregarVertice(String vertice) {
        listaAdyacencia.put(vertice, new ArrayList<>());
    }

    public void agregarArista(String origen, String destino, int peso, int tiempo) {
        listaAdyacencia.get(origen).add(new Arista(origen, destino, peso, tiempo));
        if (!origen.equals(destino)) {
            listaAdyacencia.get(destino).add(new Arista(destino, origen, peso, tiempo));
        }
    }


    public void imprimirLista() {
        for (Map.Entry<String, ArrayList<Arista>> entry : listaAdyacencia.entrySet()) {
            System.out.print("Vértice " + entry.getKey() + " está conectado con: ");
            for (Arista arista : entry.getValue()) {
                System.out.print(arista.destino + "(peso: " + arista.peso + ") ");
            }
            System.out.println();
        }
    }


    public void dijkstra(String origen, String destino) {
        Map<String, Integer> distancia = new HashMap<>(); 
        Map<String, Boolean> visitado = new HashMap<>(); 
        Map<String, String> camino = new HashMap<>(); 

        for (String vertice : listaAdyacencia.keySet()) {
            distancia.put(vertice, Integer.MAX_VALUE);
            visitado.put(vertice, false);
            camino.put(vertice, null);
        }

        distancia.put(origen, 0); 

        PriorityQueue<String> cola = new PriorityQueue<>((a, b) -> distancia.get(a) - distancia.get(b)); 
        cola.add(origen); 

        while (!cola.isEmpty()) {
            String u = cola.poll();
            visitado.put(u, true);

            for (Arista arista : listaAdyacencia.get(u)) {
                String v = arista.destino;
                int pesoUV = arista.peso;

                if (!visitado.get(v) && distancia.get(u) != Integer.MAX_VALUE && distancia.get(u) + pesoUV < distancia.get(v)) {
                    distancia.put(v, distancia.get(u) + pesoUV);
                    camino.put(v, u);
                    cola.add(v);
                }
            }
        }

        System.out.println("Distancia más corta desde " + origen + " hasta " + destino + ": " + distancia.get(destino));

        System.out.print("Ruta más corta: ");
        imprimirRuta(origen, destino, camino);
    }

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
    
    public void prim(String origen) {
        Map<String, Integer> pesoMinimo = new HashMap<>(); 
        Map<String, Arista> padre = new LinkedHashMap<>();
        Set<String> visitado = new HashSet<>();

        for (String vertice : listaAdyacencia.keySet()) {
            pesoMinimo.put(vertice, Integer.MAX_VALUE);
            padre.put(vertice, null); 
        }

        PriorityQueue<Arista> colaPrioridad = new PriorityQueue<>(Comparator.comparingInt(a -> a.peso));

        pesoMinimo.put(origen, 0); 
        colaPrioridad.offer(new Arista(origen, origen, 0, 0)); 

        while (!colaPrioridad.isEmpty()) {
            Arista arista = colaPrioridad.poll(); 
            String actual = arista.destino; 

            if (visitado.contains(actual)) continue; 

            visitado.add(actual); 

            for (Arista vecino : listaAdyacencia.get(actual)) {
                String destinoVecino = vecino.destino;
                int pesoVecino = vecino.peso;

                if (!visitado.contains(destinoVecino) && pesoVecino < pesoMinimo.get(destinoVecino)) {
                    padre.put(destinoVecino, vecino); 
                    pesoMinimo.put(destinoVecino, pesoVecino); 
                    colaPrioridad.offer(new Arista(actual, destinoVecino, pesoVecino, 0));  
                }
            }
        }
        
        System.out.println("Árbol de expansión mínima desde el vértice " + origen + ": (Prim)");
        for (Map.Entry<String, Arista> entry : padre.entrySet()) {
            String vertice = entry.getKey();
            Arista arista = entry.getValue();
            if (arista != null) {
                int peso = pesoMinimo.get(vertice);
                System.out.println("Arista: " + arista.origen + " - " + arista.destino + " (Peso: " + peso + ")");
            }
        }
    }


    

    // Método para ejecutar el algoritmo de Kruskal y encontrar el árbol de expansión mínima
    public void kruskal() {
        int numVertices = listaAdyacencia.size();
        PriorityQueue<Arista> cola = new PriorityQueue<>(Comparator.comparingInt(a -> a.peso)); // Cola de prioridad para ordenar las aristas por peso
        Set<String> conjunto = new HashSet<>(); // Conjunto para verificar la conectividad de los vértices
        ArrayList<Arista> arbolExpMin = new ArrayList<>(); // Lista para almacenar las aristas del árbol de expansión mínima
        
        
        // Iterar sobre los valores de la lista de adyacencia para obtener todas las aristas
        for (ArrayList<Arista> aristas : listaAdyacencia.values()) {
            for (Arista arista : aristas) {
                if (!arista.origen.equals(arista.destino)) { // Verificar que la arista no sea un bucle
                    cola.add(arista);
                }
            }
        }

        while (!cola.isEmpty() && (arbolExpMin.size() < numVertices - 1)) {
            Arista arista = cola.poll(); // Obtener la arista con el menor peso
            if (!formaCiclo(conjunto, arista.origen, arista.destino)) {
                arbolExpMin.add(arista);
                conjunto.add(arista.origen);
                conjunto.add(arista.destino);
            }
        }

        // Imprimir el árbol de expansión mínima
        System.out.println("Árbol de expansión mínima (Kruskal):");
        for (Arista arista : arbolExpMin) {
            System.out.println("Arista: " + arista.origen + " - " + arista.destino + " (Peso: " + arista.peso + ")");
        }
    }

    private boolean formaCiclo(Set<String> conjunto, String origen, String destino) {
        // Verificar si ambos vértices están en el mismo conjunto
        return conjunto.contains(origen) && conjunto.contains(destino);
    }

    public static void main(String[] args) {
    	ListaAdyacencia grafo = new ListaAdyacencia();
    	/*Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("Menú de opciones:");
            System.out.println("1. Opción 1");
            System.out.println("2. Imprimir lista");
            System.out.println("3. Dijkstra");
            System.out.println("4. kruskal");
            System.out.println("5. prim");
            System.out.println("6. salir");
            System.out.print("Elige una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("Has seleccionado la Opción 1");
                    // Aquí puedes poner el código correspondiente a la opción 1
                    break;
                case 2:
                    System.out.println("2. Imprimir lista");
                    // Código para la opción 2
                    break;
                case 3:
                    System.out.println("3. Dijkstra");
                    // Código para la opción 3
                    break;
                case 4:
                    System.out.println("4. kruskal");
                    break;
                case 5:
                	System.out.println("5. prim");
                	break;
                case 6:
                	System.out.println("6. salir");
                	break;
                default:
                    System.out.println("Error Valor ingresado fuera de los limetes.");
                    break;
            }

        } while (opcion != 4);

        scanner.close();*/

        grafo.agregarVertice("a");
        grafo.agregarVertice("b");
        grafo.agregarVertice("c");
        grafo.agregarVertice("d");

        grafo.agregarArista("a", "b", 4, 20);
        grafo.agregarArista("b", "c", 7, 15);
        grafo.agregarArista("b", "d", 12, 20);
        grafo.agregarArista("c", "d", 15, 14);
        

        grafo.imprimirLista();
        System.out.println();

        grafo.dijkstra("a", "d");
        System.out.println();
        
        grafo.kruskal();
        System.out.println();

        grafo.prim("a");
        System.out.println();
    }
}
