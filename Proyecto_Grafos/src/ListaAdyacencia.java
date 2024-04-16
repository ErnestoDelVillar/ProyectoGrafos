import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PriorityQueue;

class Arista {
    String destino, origen;
    int peso, tiempo;

    public Arista(String destino, int peso, int tiempo) {
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
        listaAdyacencia.get(origen).add(new Arista(destino, peso, tiempo));
        if (!origen.equals(destino)) {
            listaAdyacencia.get(destino).add(new Arista(origen, peso, tiempo));
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
        Map<String, Integer> pesoMinimo = new HashMap<>(); 
        Map<String, String> padre = new HashMap<>(); 
        Map<String, Boolean> visitado = new HashMap<>(); 

        for (String vertice : listaAdyacencia.keySet()) {
            pesoMinimo.put(vertice, Integer.MAX_VALUE);
            padre.put(vertice, null); 
            visitado.put(vertice, false); 
        }

        PriorityQueue<Arista> colaPrioridad = new PriorityQueue<>(Comparator.comparingInt(a -> a.peso));

        pesoMinimo.put(origen, 0); 
        colaPrioridad.offer(new Arista(origen, 0, 0)); 

        while (!colaPrioridad.isEmpty()) {
            Arista arista = colaPrioridad.poll(); 
            String actual = arista.destino; 

            visitado.put(actual, true); 

            for (Arista vecino : listaAdyacencia.get(actual)) {
                String destinoVecino = vecino.destino;
                int pesoVecino = vecino.peso;

                if (!visitado.get(destinoVecino) && pesoVecino < pesoMinimo.get(destinoVecino)) {
                    padre.put(destinoVecino, actual); 
                    pesoMinimo.put(destinoVecino, pesoVecino); 
                    colaPrioridad.offer(new Arista(destinoVecino, pesoVecino, 0));  
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
        grafo.agregarVertice("b");
        grafo.agregarVertice("C");
        grafo.agregarVertice("p");


        grafo.agregarArista("A", "b", 4, 20);
        grafo.agregarArista("b", "C", 7, 15);
        grafo.agregarArista("C", "p", 12, 14);
        grafo.agregarArista("b", "p", 12, 20);


        grafo.imprimirLista();
        System.out.println();

        grafo.dijkstra("A", "c");
        System.out.println();
        
        grafo.kruskal();
        System.out.println();

        grafo.prim("A1");
        System.out.println();
    }
}
