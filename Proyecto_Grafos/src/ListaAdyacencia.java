import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
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


    
    public void kruskal() {
        int numVertices = listaAdyacencia.size();
        PriorityQueue<Arista> cola = new PriorityQueue<>(Comparator.comparingInt(a -> a.peso)); 
        Set<String> conjunto = new HashSet<>();
        ArrayList<Arista> arbolExpMin = new ArrayList<>(); 
        
        
        for (ArrayList<Arista> aristas : listaAdyacencia.values()) {
            for (Arista arista : aristas) {
                if (!arista.origen.equals(arista.destino)) { 
                    cola.add(arista);
                }
            }
        }

        while (!cola.isEmpty() && (arbolExpMin.size() < numVertices - 1)) {
            Arista arista = cola.poll();
            if (!formaCiclo(conjunto, arista.origen, arista.destino)) {
                arbolExpMin.add(arista);
                conjunto.add(arista.origen);
                conjunto.add(arista.destino);
            }
        }

        System.out.println("Árbol de expansión mínima (Kruskal):");
        for (Arista arista : arbolExpMin) {
            System.out.println("Arista: " + arista.origen + " - " + arista.destino + " (Peso: " + arista.peso + ")");
        }
    }

    private boolean formaCiclo(Set<String> conjunto, String origen, String destino) {
        return conjunto.contains(origen) && conjunto.contains(destino);
    }
    
    public void floydWarshall() {
        int n = listaAdyacencia.size();
        int[][] distancias = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distancias[i][j] = (i == j) ? 0 : Integer.MAX_VALUE;
            }
        }

        for (String vertice : listaAdyacencia.keySet()) {
            for (Arista arista : listaAdyacencia.get(vertice)) {
                int fila = getIndex(vertice);
                int columna = getIndex(arista.destino);
                distancias[fila][columna] = arista.peso;
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (distancias[i][k] != Integer.MAX_VALUE && distancias[k][j] != Integer.MAX_VALUE) {
                        distancias[i][j] = Math.min(distancias[i][j], distancias[i][k] + distancias[k][j]);
                    }
                }
            }
        }

        System.out.println("Matriz de distancias mínimas (Floyd-Warshall):");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print((distancias[i][j] == Integer.MAX_VALUE ? "INF" : distancias[i][j]) + "\t");
            }
            System.out.println();
        }
    }

    private int getIndex(String vertice) {
        int i = 0;
        for (String v : listaAdyacencia.keySet()) {
            if (v.equals(vertice)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static void main(String[] args) {
    	ListaAdyacencia grafo = new ListaAdyacencia();
    	Scanner scanner = new Scanner(System.in);
        int opcion, opcion2, peso, tiempo;
        
        grafo.agregarVertice("a");
        grafo.agregarVertice("b");
        grafo.agregarVertice("c");
        grafo.agregarVertice("d");

        grafo.agregarArista("a", "b", 4, 20);
        grafo.agregarArista("b", "c", 7, 15);
        grafo.agregarArista("b", "d", 12, 20);
        grafo.agregarArista("c", "d", 15, 14);

        do {
            System.out.println("Menú de opciones:");
            System.out.println("1. Editar Vector");
            System.out.println("2. Editar Aristas");
            System.out.println("3. Mostrar Lista");
            System.out.println("4. dijkstra");
            System.out.println("5. kruskal");
            System.out.println("6. prim");
            System.out.println("7. floyd-Warshall");
            System.out.println("8. Salir");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("1. Agregar vector");
                    System.out.println("2. Editor vector");
                    System.out.println("3. Eliminar vector");
                    opcion2 = scanner.nextInt();
                    switch (opcion2) {
                    case 1:
                        System.out.print("Ingrese el nombre del vértice: ");
                        String vertice = scanner.next();
                        grafo.agregarVertice(vertice);

                        System.out.println("¿Desea agregar aristas para este vértice? (s/n)");
                        String respuesta = scanner.next();
                        while (respuesta.equalsIgnoreCase("s")) {
                            System.out.print("Ingrese el destino de la arista: ");
                            String destino = scanner.next();
                            System.out.print("Ingrese el peso de la arista: ");
                            peso = scanner.nextInt();
                            System.out.print("Ingrese el tiempo de la arista: ");
                            tiempo = scanner.nextInt();
                            grafo.agregarArista(vertice, destino, peso, tiempo);

                            System.out.println("¿Desea agregar otra arista para este vértice? (s/n)");
                            respuesta = scanner.next();
                        }
                    break;
                    case 2:
                    	 System.out.print("Ingrese el nombre del vértice que desea editar: ");
                    	    String verticeEditar = scanner.next();
                    	    
                    	    if (grafo.listaAdyacencia.containsKey(verticeEditar)) {
                    	        System.out.print("Ingrese el nuevo nombre para el vértice: ");
                    	        String nuevoNombre = scanner.next();
                    	        
                    	        grafo.listaAdyacencia.put(nuevoNombre, grafo.listaAdyacencia.remove(verticeEditar));
                    	        
                    	        for (ArrayList<Arista> aristas : grafo.listaAdyacencia.values()) {
                    	            for (Arista arista : aristas) {
                    	                if (arista.destino.equals(verticeEditar)) {
                    	                    arista.destino = nuevoNombre;
                    	                }
                    	                if (arista.origen.equals(verticeEditar)) {
                    	                    arista.origen = nuevoNombre;
                    	                }
                    	            }
                    	        }
                    	        
                    	        System.out.println("Vértice " + verticeEditar + " ha sido modificado a " + nuevoNombre);
                    	    } else {
                    	        System.out.println("El vértice especificado no existe en el grafo.");
                    	    }
                    break;
                    case 3:
                        System.out.print("Ingrese el nombre del vértice que desea eliminar: ");
                        String verticeEliminar = scanner.next();
                        
                        if (grafo.listaAdyacencia.containsKey(verticeEliminar)) {
                            grafo.listaAdyacencia.remove(verticeEliminar);
                            
                            for (ArrayList<Arista> aristas : grafo.listaAdyacencia.values()) {
                                aristas.removeIf(arista -> arista.destino.equals(verticeEliminar));
                            }
                            
                            System.out.println("Vértice " + verticeEliminar + " y todas sus aristas asociadas han sido eliminadas.");
                        } else {
                            System.out.println("El vértice especificado no existe en el grafo.");
                        }
                    break;
                    }
                    break;
                case 2:
                	System.out.println("1. Agregar Arista");
                    System.out.println("2. Editor Arista");
                    System.out.println("3. Eliminar Arista");
                    opcion2 = scanner.nextInt();
                    switch (opcion2) {
                    case 1:
                        System.out.print("Ingrese el origen de la arista: ");
                        String origenArista = scanner.next();
                        System.out.print("Ingrese el destino de la arista: ");
                        String destinoArista = scanner.next();
                        System.out.print("Ingrese el peso de la arista: ");
                        int pesoArista = scanner.nextInt();
                        System.out.print("Ingrese el tiempo de la arista: ");
                        int tiempoArista = scanner.nextInt();
                        
                        if (grafo.listaAdyacencia.containsKey(origenArista) && grafo.listaAdyacencia.containsKey(destinoArista)) {
                            grafo.agregarArista(origenArista, destinoArista, pesoArista, tiempoArista);
                            System.out.println("Arista agregada exitosamente.");
                        } else {
                            System.out.println("Los vértices especificados no existen en el grafo.");
                        }
                        break;
                    case 2:
                    	System.out.print("Ingrese el origen de la arista: ");
                        String origenEditar = scanner.next();
                        System.out.print("Ingrese el destino de la arista: ");
                        String destinoEditar = scanner.next();
                        
                        if (grafo.listaAdyacencia.containsKey(origenEditar) && grafo.listaAdyacencia.containsKey(destinoEditar)) {
                            Arista aristaEditar = null;
                            for (Arista arista : grafo.listaAdyacencia.get(origenEditar)) {
                                if (arista.destino.equals(destinoEditar)) {
                                    aristaEditar = arista;
                                    break;
                                }
                            }
                            
                            if (aristaEditar != null) {
                                System.out.println("Arista encontrada. Ingrese los nuevos valores:");
                                System.out.print("Nuevo peso de la arista: ");
                                int nuevoPeso = scanner.nextInt();
                                System.out.print("Nuevo tiempo de la arista: ");
                                int nuevoTiempo = scanner.nextInt();
                                
                                aristaEditar.peso = nuevoPeso;
                                aristaEditar.tiempo = nuevoTiempo;
                                
                                System.out.println("Arista editada exitosamente.");
                            } else {
                                System.out.println("La arista especificada no existe en el grafo.");
                            }
                        } else {
                            System.out.println("Los vértices especificados no existen en el grafo.");
                        }
                        break;
                    case 3:
                    	System.out.print("Ingrese el origen de la arista: ");
                        String origenEliminar = scanner.next();
                        System.out.print("Ingrese el destino de la arista: ");
                        String destinoEliminar = scanner.next();
                        
                        if (grafo.listaAdyacencia.containsKey(origenEliminar) && grafo.listaAdyacencia.containsKey(destinoEliminar)) {

                            boolean aristaEncontrada = false;
                            ArrayList<Arista> aristasOrigen = grafo.listaAdyacencia.get(origenEliminar);
                            for (int i = 0; i < aristasOrigen.size(); i++) {
                                Arista arista = aristasOrigen.get(i);
                                if (arista.destino.equals(destinoEliminar)) {
                                    aristasOrigen.remove(i);
                                    aristaEncontrada = true;
                                    break;
                                }
                            }
                            
                            if (aristaEncontrada) {
                                System.out.println("Arista eliminada exitosamente.");
                            } else {
                                System.out.println("La arista especificada no existe en el grafo.");
                            }
                        } else {
                            System.out.println("Los vértices especificados no existen en el grafo.");
                        }
                        break;
                    }
                    break;
                case 3:
                	grafo.imprimirLista();
                    System.out.println();
                    break;
                case 4:
                	grafo.dijkstra("a", "d");
                    System.out.println();
                    break;
                case 5:
                	grafo.kruskal();
                    System.out.println();
                	break;
                case 6:
                	grafo.prim("a");
                    System.out.println();
                	break;
                case 7:
                	grafo.floydWarshall();
                	break;
                case 8:
                	System.out.println("Adios");
                	break;
                default:
                    System.out.println("Error. Valor ingresado fuera de los limetes.");
                    break;
            }

        } while (opcion != 8);

        scanner.close();

        /*grafo.agregarVertice("a");
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
        
        grafo.floydWarshall();*/
    }
}
