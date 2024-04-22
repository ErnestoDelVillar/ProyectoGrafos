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

//Vertices---------------------------------------------------------------------------------------------------------------------------------------------
    public void agregarVertice(String vertice) {
        listaAdyacencia.put(vertice, new ArrayList<>());
    }
    
    public void eliminarVertice(String verticeEliminar) {
        if (listaAdyacencia.containsKey(verticeEliminar)) {
            // Eliminar el vértice de la lista de adyacencia
            listaAdyacencia.remove(verticeEliminar);

            // Eliminar todas las aristas que tengan el vértice como origen o destino
            for (ArrayList<Arista> aristas : listaAdyacencia.values()) {
                aristas.removeIf(arista -> arista.origen.equals(verticeEliminar) || arista.destino.equals(verticeEliminar));
            }

            System.out.println("Vértice " + verticeEliminar + " y todas sus aristas asociadas han sido eliminadas.");
        } else {
            System.out.println("El vértice especificado no existe en el grafo.");
        }
    }
    
    public void editarVertice(String verticeEditar, String nuevoNombre) {

        listaAdyacencia.put(nuevoNombre, listaAdyacencia.remove(verticeEditar));

        for (ArrayList<Arista> aristas : listaAdyacencia.values()) {
            for (Arista arista : aristas) {
                if (arista.destino.equals(verticeEditar)) {
                    arista.destino = nuevoNombre;
                }
                if (arista.origen.equals(verticeEditar)) {
                    arista.origen = nuevoNombre;
                }
            }
        }
}   
    
    public void VerticesDisponibles() {
   	 ArrayList<String> verticesImpresos = new ArrayList<>(); // Para evitar imprimir vértices repetidos
       
       System.out.println("Vertices disponibles: ");
       for (String vertice : listaAdyacencia.keySet()) {
           if (!verticesImpresos.contains(vertice)) {
               System.out.print("Vértice: " + vertice + " -- ");
               verticesImpresos.add(vertice);
           }
       }
   } 
    
// Aristas---------------------------------------------------------------------------------------------------------------------------------------------    
    
    public void agregarArista(String origen, String destino, int peso, int tiempo) {
        listaAdyacencia.get(origen).add(new Arista(origen, destino, peso, tiempo));
        if (!origen.equals(destino)) {
            listaAdyacencia.get(destino).add(new Arista(destino, origen, peso, tiempo));
        }
    }    
    
    public void modificarArista(String origen, String destino, int nuevoPeso, int nuevoTiempo) {
        if (listaAdyacencia.containsKey(origen) && listaAdyacencia.containsKey(destino)) {
            boolean aristaEncontrada = false;
            // Modificar la arista en la dirección origen -> destino
            ArrayList<Arista> aristasOrigen = listaAdyacencia.get(origen);
            for (Arista arista : aristasOrigen) {
                if (arista.destino.equals(destino)) {
                    arista.peso = nuevoPeso;
                    arista.tiempo = nuevoTiempo;
                    aristaEncontrada = true;
                    System.out.println("Arista modificada exitosamente.");
                    break;
                }
            }
            // Modificar la arista en la dirección destino -> origen
            ArrayList<Arista> aristasDestino = listaAdyacencia.get(destino);
            for (Arista arista : aristasDestino) {
                if (arista.destino.equals(origen)) {
                    arista.peso = nuevoPeso;
                    arista.tiempo = nuevoTiempo;
                    aristaEncontrada = true;
                    break;
                }
            }
            if (!aristaEncontrada) {
                System.out.println("La arista especificada no existe en el grafo.");
            }
        } else {
            System.out.println("No se han encontrado los vértices introducidos.");
        }
    }


    
    public boolean eliminarArista(String origen, String destino) {
    	
        if (!listaAdyacencia.containsKey(origen) || !listaAdyacencia.containsKey(destino)) {
            System.out.println("No se han encontrado los vértices introducidos.");
            return false;
        }
        
        if (existeArista(origen, destino)) {
            ArrayList<Arista> aristasOrigen = listaAdyacencia.get(origen);
            ArrayList<Arista> aristasDestino = listaAdyacencia.get(destino);
            
            aristasOrigen.removeIf(arista -> arista.destino.equals(destino));
            aristasDestino.removeIf(arista -> arista.destino.equals(origen));
            
            if (aristasOrigen.isEmpty()) {
                eliminarVertice(origen);
            }

            System.out.println("Arista eliminada exitosamente.");
            return true;
        } else {
            System.out.println("La arista especificada no existe en el grafo.");
            return false;
        }
    }    
    
    public boolean existeArista(String origen, String destino) {
        
        if (!listaAdyacencia.containsKey(origen) || !listaAdyacencia.containsKey(destino)) {
            return false;
        }
        
        // Recorre las aristas del vértice origen para verificar si existe una arista al destino
        for (Arista arista : listaAdyacencia.get(origen)) {
            if (arista.destino.equals(destino)) {
                return true; // Se encontró una arista al destino desde el origen
            }
        }
        
        
        // Si no se encontró una arista al destino desde el origen
        return false;
    }    
    
    public void AristasDisponibles() {
        HashSet<String> aristasImpresas = new HashSet<>(); // Usamos un conjunto para evitar aristas duplicadas
        
        System.out.println("Aristas Disponibles: ");
        for (Map.Entry<String, ArrayList<Arista>> entry : listaAdyacencia.entrySet()) {
            String origen = entry.getKey();
            for (Arista arista : entry.getValue()) {
                String destino = arista.destino;
                String aristaKey = origen.compareTo(destino) < 0 ? origen + "-" + destino : destino + "-" + origen;
                if (!aristasImpresas.contains(aristaKey)) {
                    System.out.println("Arista: " + origen + " - " + destino + " (Peso: " + arista.peso + ", Tiempo: " + arista.tiempo + ")");
                    aristasImpresas.add(aristaKey);
                }
            }
        }
    }
    
//Algoritmos de La ruta mas corta----------------------------------------------------------------------------------------------------------------------    
    
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
    

    public void rutaMasCorta(String origen, String destino) {
        Map<String, Integer> distanciaPeso = new HashMap<>();
        Map<String, Boolean> visitadoPeso = new HashMap<>();
        Map<String, String> caminoPeso = new HashMap<>();

        for (String vertice : listaAdyacencia.keySet()) {
            distanciaPeso.put(vertice, Integer.MAX_VALUE);
            visitadoPeso.put(vertice, false);
            caminoPeso.put(vertice, null);
        }

        distanciaPeso.put(origen, 0);

        PriorityQueue<String> colaPeso = new PriorityQueue<>((a, b) -> distanciaPeso.get(a) - distanciaPeso.get(b));
        colaPeso.add(origen);

        while (!colaPeso.isEmpty()) {
            String u = colaPeso.poll();
            visitadoPeso.put(u, true);

            for (Arista arista : listaAdyacencia.get(u)) {
                String v = arista.destino;
                int pesoUV = arista.peso;

                if (!visitadoPeso.get(v) && distanciaPeso.get(u) != Integer.MAX_VALUE && distanciaPeso.get(u) + pesoUV < distanciaPeso.get(v)) {
                    distanciaPeso.put(v, distanciaPeso.get(u) + pesoUV);
                    caminoPeso.put(v, u);
                    colaPeso.add(v);
                }
            }
        }

        Map<String, Integer> distanciaTiempo = new HashMap<>();
        Map<String, Boolean> visitadoTiempo = new HashMap<>();
        Map<String, String> caminoTiempo = new HashMap<>();

        for (String vertice : listaAdyacencia.keySet()) {
            distanciaTiempo.put(vertice, Integer.MAX_VALUE);
            visitadoTiempo.put(vertice, false);
            caminoTiempo.put(vertice, null);
        }

        distanciaTiempo.put(origen, 0);

        PriorityQueue<String> colaTiempo = new PriorityQueue<>((a, b) -> distanciaTiempo.get(a) - distanciaTiempo.get(b));
        colaTiempo.add(origen);

        while (!colaTiempo.isEmpty()) {
            String u = colaTiempo.poll();
            visitadoTiempo.put(u, true);

            for (Arista arista : listaAdyacencia.get(u)) {
                String v = arista.destino;
                int tiempoUV = arista.tiempo;

                if (!visitadoTiempo.get(v) && distanciaTiempo.get(u) != Integer.MAX_VALUE && distanciaTiempo.get(u) + tiempoUV < distanciaTiempo.get(v)) {
                    distanciaTiempo.put(v, distanciaTiempo.get(u) + tiempoUV);
                    caminoTiempo.put(v, u);
                    colaTiempo.add(v);
                }
            }
        }

        System.out.println("Ruta más corta en términos de peso:");
        System.out.print("Vértices: ");
        imprimirRuta(origen, destino, caminoPeso);
        System.out.println("Peso total: " + distanciaPeso.get(destino));
        System.out.println("Ruta más corta en términos de tiempo:");
        System.out.print("Vértices: ");
        imprimirRuta(origen, destino, caminoTiempo);
        System.out.println("Tiempo total: " + distanciaTiempo.get(destino));
    }
    
//Algoritmos de Arbol de expancion minima--------------------------------------------------------------------------------------------------------------    
    
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

//Metodos Auxiliares-----------------------------------------------------------------------------------------------------------------------------------    
    
    public void imprimirLista() {
        for (Map.Entry<String, ArrayList<Arista>> entry : listaAdyacencia.entrySet()) {
            System.out.print("Vértice " + entry.getKey() + " está conectado con: ");
            for (Arista arista : entry.getValue()) {
                System.out.print(arista.destino + "(peso: " + arista.peso + ") ");
            }
            System.out.println();
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
    
    public static void menu(ListaAdyacencia grafo) {
        int opcion, opcion2, peso, tiempo;
        String origenArista, destinoArista;
        Scanner scanner = new Scanner(System.in);
        
        do {
            grafo.imprimirLista();
            System.out.println();
            
            System.out.println("Menú de opciones:");
            System.out.println("1. Menu Vertice.");
            System.out.println("2. Menu Aristas.");
            System.out.println("3. dijkstra.");
            System.out.println("4. kruskal.");
            System.out.println("5. prim.");
            System.out.println("6. floyd-Warshall.");
            System.out.println("7. Distancia mas corta y tiempo mas corto.");
            System.out.println("8. Salir.");
            System.out.println();

            opcion = scanner.nextInt();

            switch (opcion) {
            
                case 1:
                	System.out.println();
                    System.out.println("1. Agregar vertice ");
                    System.out.println("2. Editor vertice  ");
                    System.out.println("3. Eliminar vertice");
                    System.out.println("4. Atras           ");
                    
                    opcion2 = scanner.nextInt();
                    switch (opcion2) {
                    
                        case 1:
                        	
                            System.out.println();
                            System.out.println("Agregar Vertice: ");
                            System.out.print("Ingrese el nombre del vértice: ");
                            String vertice = scanner.next();
                            grafo.agregarVertice(vertice);

                            String respuesta = "s";
                            
                            grafo.VerticesDisponibles();
                            while (respuesta.equalsIgnoreCase("s")) {
                            	System.out.println();
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
                            System.out.println();
                            break;
                        case 2:
                        	
                        	System.out.println();
                        	System.out.println("Editar Vertice:");
                            grafo.VerticesDisponibles();
                            System.out.println();
                            System.out.print("Ingrese el nombre del vértice que desea editar: ");
                            String verticeEditar = scanner.next();
                            if (grafo.listaAdyacencia.containsKey(verticeEditar)) {
                            	
                                System.out.print("Ingrese el nuevo nombre para el vértice: ");
                                String nuevoNombre = scanner.next();
                                grafo.editarVertice(verticeEditar, nuevoNombre);
                                
                            } else {
                                System.out.println("El vértice especificado no existe en el grafo.");
                            }
                            System.out.println();
                            break;
                        case 3:
                            
                            System.out.println();
                            System.out.println("Eliminar Vertice: ");
                            grafo.VerticesDisponibles();
                            System.out.println();
                            System.out.print("Ingrese el nombre del vértice que desea eliminar: ");
                            String verticeEliminar = scanner.next();
                            grafo.eliminarVertice(verticeEliminar);
                            System.out.println();
                            break;
                            
                        case 4:
                        	System.out.println();
                        	break;
                    }
                    break;
                    
                case 2:
                	
                    System.out.println();
                    System.out.println("1. Agregar Arista");
                    System.out.println("2. Editor Arista");
                    System.out.println("3. Eliminar Arista");
                    opcion2 = scanner.nextInt();
                    switch (opcion2) {
                    
                        case 1:
                        	
                        	System.out.println();
                        	System.out.println("Agregar Arista: ");
                        	grafo.VerticesDisponibles();
                        	System.out.println();
                            System.out.print("Ingrese el vertice origen de la arista: ");
                            origenArista = scanner.next();
                            System.out.print("Ingrese el vertice destino de la arista: ");
                            destinoArista = scanner.next();
                            
                            while( grafo.existeArista(origenArista, destinoArista) || !grafo.listaAdyacencia.containsKey(origenArista) || !grafo.listaAdyacencia.containsKey(destinoArista)) {
                            	
                            	System.out.println();
                            	
                            	if( !grafo.listaAdyacencia.containsKey(origenArista) ) {
                            		System.out.println("vertice origen no existe. Ingrese de nuevo: ");	
                            	}
                            	
                            	if( !grafo.listaAdyacencia.containsKey(destinoArista) ) {
                            		System.out.println("vertice destino no existe. Ingrese de nuevo: ");	
                            	}
                            	if( grafo.existeArista(origenArista, destinoArista) ) {
                            		System.out.println("La arista ya existe Ingrese Nuevos Valores: ");	
                            	}
                            
                            	
                                System.out.print("Ingrese el vertice origen de la arista: ");
                                origenArista = scanner.next();
                                System.out.print("Ingrese el destino de la arista: ");
                                destinoArista = scanner.next();
                                System.out.println();
                            }
                            
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
                            System.out.println();
                            break;
                            
                        case 2:
                        	
                        	System.out.println();
                        	System.out.println("Editar Arista: ");
                        	grafo.AristasDisponibles();                      	
                        	System.out.println();
                            System.out.println("Ingrese el origen de la arista: ");
                            String origenEditar = scanner.next();
                            System.out.print("Ingrese el destino de la arista: ");
                            String destinoEditar = scanner.next();
                            
                            while( !grafo.listaAdyacencia.containsKey(origenEditar) || !grafo.listaAdyacencia.containsKey(destinoEditar) ) {
                            	
                            	System.out.println();
                            	
                            	if( !grafo.listaAdyacencia.containsKey(origenEditar) ) {
                            		System.out.println("origen invalido. ingrese de nuevo: ");
                            	}
                            	
                            	if( !grafo.listaAdyacencia.containsKey(destinoEditar) ) {
                            		System.out.println("destino invalido. ingrese de nuevo: ");
                            	}
                            	
                                System.out.println("Ingrese el origen de la arista: ");
                                origenEditar = scanner.next();
                                System.out.print("Ingrese el destino de la arista: ");
                                destinoEditar = scanner.next();
                            	
                            }
                            if (grafo.listaAdyacencia.containsKey(origenEditar) && grafo.listaAdyacencia.containsKey(destinoEditar)) {
                                System.out.print("Nuevo peso de la arista: ");
                                int nuevoPeso = scanner.nextInt();
                                
                                System.out.print("Nuevo tiempo de la arista: ");
                                int nuevoTiempo = scanner.nextInt();
                                
                            	grafo.modificarArista(origenEditar, destinoEditar, nuevoPeso, nuevoTiempo);
                            } else {
                                System.out.println("No se han encontrado lo vertices introducidos.");
                            }
                            
                            System.out.println();
                            break;
                            
                        case 3:
                        	
                        	System.out.println();
                        	System.out.println("Eliminar Arista: ");
                        	grafo.AristasDisponibles();
                        	System.out.println();
                            System.out.println("Ingrese el origen de la arista: ");
                            String origenEliminar = scanner.next();
                            System.out.print("Ingrese el destino de la arista: ");
                            String destinoEliminar = scanner.next();

                            grafo.eliminarArista(origenEliminar, destinoEliminar);
                            
                            System.out.println();
                            break;
                    }
                    System.out.println();
                    break;
                    
                case 3:
                	
                	System.out.println();
                	System.out.println("Dijkstra: ");
                	grafo.VerticesDisponibles();
                	System.out.println();
                    System.out.print("Ingrese el vertice origen: ");
                    origenArista = scanner.next();
                    System.out.print("Ingrese el vertice destino: ");
                    destinoArista = scanner.next();
                    if (grafo.listaAdyacencia.containsKey(origenArista) && grafo.listaAdyacencia.containsKey(destinoArista)) {
                        grafo.dijkstra(origenArista, destinoArista);
                        System.out.println();
                    } else {
                        System.out.println("No se han encontrado lo vertices introducidos.");
                    }
                    System.out.println();
                    break;
                    
                case 4:
                	
                	System.out.println();
                	System.out.println("Kruskal: ");
                	System.out.println();
                    grafo.kruskal();
                    System.out.println();
                    break;
                    
                case 5:
                	
                	System.out.println();
                	System.out.println("Prim: ");
                	grafo.VerticesDisponibles();
                	System.out.println();
                    System.out.println("Ingrese el vertice origen: ");
                    String Primorigen = scanner.next();
                    if (grafo.listaAdyacencia.containsKey(Primorigen)) {
                        grafo.prim(Primorigen);
                        System.out.println();
                    } else {
                        System.out.println("No se han encontrado lo vertices introducidos.");
                    }
                    System.out.println();
                    break;
                    
                case 6:
                	
                	System.out.println();
                    grafo.floydWarshall();
                    System.out.println();
                    break;
                    
                case 7:
                	
                	System.out.println();
                	grafo.VerticesDisponibles();
                	System.out.println();
                	
                    System.out.println("Ingrese el vertice origen: ");
                    origenArista = scanner.next();
                    System.out.print("Ingrese el vertice destino: ");
                    destinoArista = scanner.next();
                    if (grafo.listaAdyacencia.containsKey(origenArista) && grafo.listaAdyacencia.containsKey(destinoArista)) {
                        grafo.rutaMasCorta(origenArista, destinoArista);
                        System.out.println();
                    } else {
                        System.out.println("No se han encontrado lo vertices introducidos.");
                    }
                    System.out.println();
                    break;
                    
                case 8:
                	
                    System.out.println("Adios");
                    break;
                    
                default:
                	
                    System.out.println("Error. Valor ingresado fuera de los limites.");
                    break;
            }

        } while (opcion != 9);


        scanner.close();
    }
    

//main-------------------------------------------------------------------------------------------------------------------------------------------------    
    public static void main(String[] args) {
    	ListaAdyacencia grafo = new ListaAdyacencia();
        
        grafo.agregarVertice("a");
        grafo.agregarVertice("b");
        grafo.agregarVertice("c");
        grafo.agregarVertice("d");

        grafo.agregarArista("a", "b", 4, 20);
        grafo.agregarArista("b", "c", 7, 15);
        grafo.agregarArista("b", "d", 12, 20);
        grafo.agregarArista("c", "d", 15, 14);
        
        menu(grafo);
    }
}
