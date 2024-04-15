import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap; // Importa LinkedHashMap
import java.util.Map;
import java.util.PriorityQueue;

class Arista {
    String destino, origen;
    int peso;

    public Arista(String destino, int peso) {
        this.destino = destino;
        this.peso = peso;
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

    public void agregarArista(String origen, String destino, int peso) {
        listaAdyacencia.get(origen).add(new Arista(destino, peso));
        if (!origen.equals(destino)) {
            listaAdyacencia.get(destino).add(new Arista(origen, peso));
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
    
    private class Subset {
        String parent;
        int rank;
    }

    private String find(HashMap<String, Subset> subsets, String i) {
        if (!subsets.get(i).parent.equals(i))
            subsets.get(i).parent = find(subsets, subsets.get(i).parent);
        return subsets.get(i).parent;
    }

    private void union(HashMap<String, Subset> subsets, String x, String y) {
        String xroot = find(subsets, x);
        String yroot = find(subsets, y);

        if (subsets.get(xroot).rank < subsets.get(yroot).rank)
            subsets.get(xroot).parent = yroot;
        else if (subsets.get(xroot).rank > subsets.get(yroot).rank)
            subsets.get(yroot).parent = xroot;
        else {
            subsets.get(yroot).parent = xroot;
            subsets.get(xroot).rank++;
        }
    }

    public void kruskal() {
        ArrayList<Arista> result = new ArrayList<>();

        ArrayList<Arista> aristas = new ArrayList<>();
        for (Map.Entry<String, ArrayList<Arista>> entry : listaAdyacencia.entrySet()) {
            for (Arista arista : entry.getValue()) {
                aristas.add(arista);
            }
        }

        aristas.sort(Comparator.comparingInt(a -> a.peso));

        HashMap<String, Subset> subsets = new HashMap<>();
        for (String vertice : listaAdyacencia.keySet()) {
            subsets.put(vertice, new Subset());
            subsets.get(vertice).parent = vertice;
            subsets.get(vertice).rank = 0;
        }

        int i = 0;
        while (result.size() < listaAdyacencia.size() - 1) {
            Arista arista = aristas.get(i++);
            String origen = arista.origen;
            String destino = arista.destino;

            String x = find(subsets, origen);
            String y = find(subsets, destino);

            if (!x.equals(y)) {
                result.add(arista);
                union(subsets, x, y);
            }
        }

        System.out.println("Árbol de expansión mínima (Kruskal):");
        for (Arista arista : result) {
            System.out.println(arista.origen + " - " + arista.destino + " (Peso: " + arista.peso + ")");
        }
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
        int numVertices = listaAdyacencia.size();

        Map<String, Boolean> visitado = new HashMap<>();
        Map<String, String> padre = new HashMap<>();
        Map<String, Integer> pesoMinimo = new HashMap<>();

        for (String vertice : listaAdyacencia.keySet()) {
            visitado.put(vertice, false);
            padre.put(vertice, null);
            pesoMinimo.put(vertice, Integer.MAX_VALUE);
        }

        PriorityQueue<Arista> cola = new PriorityQueue<>(numVertices, Comparator.comparingInt(a -> a.peso));

        pesoMinimo.put(origen, 0);
        cola.offer(new Arista(origen, 0));

        while (!cola.isEmpty()) {
            Arista arista = cola.poll();
            String actual = arista.destino;

            visitado.put(actual, true);

            for (Arista vecino : listaAdyacencia.get(actual)) {
                String destino = vecino.destino;
                int peso = vecino.peso;
                if (!visitado.get(destino) && peso < pesoMinimo.get(destino)) {
                    padre.put(destino, actual);
                    pesoMinimo.put(destino, peso);
                    cola.removeIf(a -> a.destino.equals(destino));
                    cola.offer(new Arista(destino, peso));
                }
            }
        }

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

    public static void main(String[] args) {
        ListaAdyacencia grafo = new ListaAdyacencia();

        grafo.agregarVertice("A");
        grafo.agregarVertice("A");
        grafo.agregarVertice("C");
        grafo.agregarVertice("D");
        grafo.agregarVertice("E");
        grafo.agregarVertice("F");

        grafo.agregarArista("A", "A", 4);
        grafo.agregarArista("A", "C", 8);
        grafo.agregarArista("A", "C", 11);
        grafo.agregarArista("A", "D", 8);
        grafo.agregarArista("C", "E", 7);
        grafo.agregarArista("D", "E", 2);
        grafo.agregarArista("D", "F", 4);
        grafo.agregarArista("E", "F", 9);

        grafo.imprimirLista();
        System.out.println();

        grafo.dijkstra("A1", "F");
        System.out.println();

        grafo.prim("A1");
        System.out.println();
    }
}
