import javax.swing.JOptionPane;

public class Arboles {
    public static void main(String[] args) throws Exception {
        
        char[] cadena = obtenerCadena();

        Nodo raiz = new Nodo(cadena[0]); 
        crearArbol(raiz, cadena);
        
        int opcion = 0;
        do {
            opcion = Menu();
            switch (opcion) {
                case 1:
                    StringBuilder inorden = new StringBuilder();
                    recorrerInorden(raiz, inorden);
                    JOptionPane.showMessageDialog(null, "Recorrido inorden:\n" + inorden);
                    break;
                case 2:
                    StringBuilder preorden = new StringBuilder();
                    recorrerPreorden(raiz, preorden);
                    JOptionPane.showMessageDialog(null, "Recorrido preorden:\n" + preorden);
                    break;
                case 3:
                    StringBuilder posorden = new StringBuilder();
                    recorrerPosorden(raiz, posorden);
                    JOptionPane.showMessageDialog(null, "Recorrido posorden:\n" + posorden);
                    break;
                case 4:
                    insertarNodoDesdeMenu(raiz);
                    break;
                case 5:
                    raiz = eliminarNodoDesdeMenu(raiz);
                    break;
                case 6:
                    int[] hojas = new int[1]; // convierto el int en un objeto para poder arrastrarlo entre recursiones
                    contarHojas(raiz, hojas);
                    JOptionPane.showMessageDialog(null, "Cantidad de hojas:\n\n" + hojas[0]);
                    break;
                case 7:
                    int[] padres = new int[1];
                    contarPadres(raiz, padres);
                    JOptionPane.showMessageDialog(null, "Cantidad de padres:\n\n" + padres[0]);
                    break;
                case 9:
                    mostrarHermanoDeDato(raiz);
                    break;
                case 10:
                    mostrarNivelDeDato(raiz);
                    break;
                case 11:
                    mostrarAlturaDeDato(raiz);
                    break;
                case 12:
                    mostrarPrimosHermanosDeDato(raiz);
                    break;
                case 13:
                    mostrarAncestrosDeDato(raiz);
                    break;
                case 0:
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opcion invalida.");
                    break;
            }
        } while (opcion != 0);
    }

    public static int Menu() {

        return Integer.parseInt(JOptionPane.showInputDialog(
            "\n ===== Menu Principal :) ====="
                + "\n1. Mostrar recorrido inorden"
                + "\n2. Mostrar recorrido preorden"
                + "\n3. Mostrar recorrido posorden"

                + "\n\n4. Insertar nodo" // para esto recordar hacer el input tipo "arbol actual: DFESMG. inserte un nodo distinto a los ya existentes"
                + "\n5. Eliminar nodo" // para esto recordar hacer el input tipo "arbol actual: DFESMG. inserte un nodo a eliminar que se encuentre en el arbol"
                + "\n6. Contar las hojas"
                + "\n7. Contar los padres"
                + "\n8. Mostrar arbol"

                + "\n\n9. Mostrar el hermano de un dato"
                + "\n10. Mostrar el nivel de un dato"
                + "\n11. Mostrar la altura de un dato"
                + "\n12. Mostrar los primos hermanos de un dato"
                + "\n13. Mostrar los ancestros de un dato"
                + "\n14. Balancear arbol"
                + "\n\n0. Salir"
                + "\nSeleccione una opcion"
        ));
    }

    public static char[] obtenerCadena() {

        String vector = "";
        char[] cadena;

        do {
            vector = (JOptionPane.showInputDialog("Ingrese una cadena de letras para crear el arbol binario:")).toUpperCase();
             // paso todo a mayusculas
            cadena = vector.toCharArray();
            
            for (int i = 0; i < cadena.length; i++) {
                for (int j = 0; j < cadena.length; j++) {
                    if (i == j) {
                        j++; // paraq ue no se compare consigo mismo
                    }

                    if (j < cadena.length && cadena[i] == cadena[j]) {
                        JOptionPane.showMessageDialog(null, "El carácter \"" + cadena[i] + "\" se repite en la cadena.\nIntente de nuevo.");
                        vector = "";

                        i = cadena.length; // Salir del bucle externo
                        j = cadena.length; // Salir del bucle interno
                    }
                }
            }

        } while (vector == null || vector == "" || vector.isBlank()); 

        return cadena;
    }

    public static void crearArbol(Nodo raiz, char[] cadena) {

        Nodo x = raiz;

        for (int i = 1; i < cadena.length; i++) {
            Nodo nuevoNodo = new Nodo(cadena[i]);

            insertarNodo(x, nuevoNodo);
        }

    }

    public static void insertarNodo(Nodo raiz, Nodo nodo) {
        Nodo x = raiz;

        //recorrido dentro del arbol actual, para saber si insertar a la izquierda o a la derecha

        // IMPORTANTE: insertarNodo ASUME que 'dato' de 'raiz' y 'nodo' son distintos
        if (nodo.getDato() > x.getDato()) {
            if (x.getLigaDer() == null) {

                x.setLigaDer(nodo); // si x apunta a null a la derecha, se guarda el nodo ahí
            } else {
                insertarNodo(x.getLigaDer(), nodo);
            }
        } else if (nodo.getDato() < x.getDato()) {
            if (x.getLigaIzq() == null) {

                x.setLigaIzq(nodo);
            } else {
                insertarNodo(x.getLigaIzq(), nodo);
            }
        }
    }

    public static Nodo eliminarNodo(Nodo raiz, char dato) {
        Nodo x = raiz;

        if (x == null) { 
            return null;
        }

        if (dato < x.getDato()) {
            x.setLigaIzq(eliminarNodo(x.getLigaIzq(), dato));
        } else if (dato > x.getDato()) {
            x.setLigaDer(eliminarNodo(x.getLigaDer(), dato));
        } else {
            if (x.getLigaIzq() == null && x.getLigaDer() == null) {
                return null;
            }
            if (x.getLigaIzq() == null) {
                return x.getLigaDer();
            }
            if (x.getLigaDer() == null) {
                return x.getLigaIzq();
            }

            Nodo sucesor = obtenerMenor(x.getLigaDer());
            x.setDato(sucesor.getDato());
            x.setLigaDer(eliminarNodo(x.getLigaDer(), sucesor.getDato()));
        }

        return x;
    }

    private static void recorrerInorden(Nodo raiz, StringBuilder inorden) {
        Nodo x = raiz;

        // inorden: izquierda -> raíz -> derecha
        // arbol de prueba: HCAPMFOLEN

        // busqueda en izquierda
        if (x.getLigaIzq() != null) {
            recorrerInorden(x.getLigaIzq(), inorden);
        }
        
        inorden.append(x.getDato() + " ");
        // se guarda el dato del nodo actual ya que:
        /*   - es el ultimo a la izquierda en esa rama
             - es el nodo raiz del anterior nodo a la izquierda
             - es un nodo derecha sin hijo izquierdo
         */

        // busqueda en derecha 
        if (x.getLigaDer() != null) {
            recorrerInorden(x.getLigaDer(), inorden); // si hay a la derecha, se entra en esa rama
        }
    }

    private static void recorrerPreorden(Nodo raiz, StringBuilder preorden) {
        
        Nodo x = raiz;

        // preorden:   raíz -> izquierda -> derecha
        preorden.append(x.getDato() + " ");

        if (x.getLigaIzq() != null) {
            recorrerPreorden(x.getLigaIzq(), preorden);
        }
        if (x.getLigaDer() != null) {
            recorrerPreorden(x.getLigaDer(), preorden);
        }

    }

    private static void recorrerPosorden(Nodo raiz, StringBuilder posorden) {
        Nodo x = raiz;

        // posorden:   izquierda -> derecha -> raiz

        if (x.getLigaIzq() != null) {
            recorrerPosorden(x.getLigaIzq(), posorden);
        }
        if (x.getLigaDer() != null) {
            recorrerPosorden(x.getLigaDer(), posorden);
        }

        posorden.append(x.getDato() + " ");
    }

    public static void insertarNodoDesdeMenu(Nodo raiz) {
        int encontrado;
        do {
            encontrado = 0;
            // valores para encontrado:
            /* 0 = melo para insertar
               1 = mas de 1 caracter/nodo ya existente
             */
            StringBuilder cadena = new StringBuilder();
            recorrerPreorden(raiz, cadena);

            String nuevoNodo = (JOptionPane.showInputDialog("Arbol actual (preorden):\n"+cadena+"\nIngrese un carácter que no se encuentre en el árbol.\n\nNo ingrese espacios ni puntos.")).toUpperCase();

            if (nuevoNodo.length() > 1) {
                JOptionPane.showMessageDialog(null, "Error: Solo puede ingresar 1 carácter.");
                encontrado = 1;
            }

            if (encontrado == 0) {
                char caracter = nuevoNodo.charAt(0);
                

                for (int i = 0; i < cadena.length() && encontrado == 0; i++) {
                    if (cadena.charAt(i) == caracter) {
                        JOptionPane.showMessageDialog(null, "Error: \""+caracter+"\" ya se encuentra en el árbol.");
                        encontrado = 1;
                    }
                }

                if (encontrado == 0) {
                    Nodo nodo = new Nodo(caracter);
                    insertarNodo(raiz, nodo);

                    StringBuilder nuevaCadena = new StringBuilder();
                    recorrerPreorden(raiz, nuevaCadena);
                    JOptionPane.showMessageDialog(null, nuevaCadena);
                    
                }
            }


        } while (encontrado == 1);
        
    }

    public static Nodo eliminarNodoDesdeMenu(Nodo raiz) {
        
        int encontrado;
        do {
            encontrado = 1;
            // valores para encontrado:
            /* 0 = caracter/nodo no encontrado / error en input
               1 = melo para eliminar
             */
            StringBuilder cadena = new StringBuilder();
            recorrerPreorden(raiz, cadena);

            char caracter = pedirCaracter(
                "Arbol actual (preorden):\n" + cadena
                + "\nIngrese un carácter que se encuentre en el árbol.\n\nNo ingrese espacios ni puntos."
            );

            if (caracter == '\0') {
                encontrado = 0;
            }

            if (encontrado == 1) {
                if (buscarNodo(raiz, caracter) != null) {
                    raiz = eliminarNodo(raiz, caracter);

                    StringBuilder nuevaCadena = new StringBuilder();
                    if (raiz != null) {
                        recorrerPreorden(raiz, nuevaCadena);
                        JOptionPane.showMessageDialog(null, nuevaCadena);
                    } else {
                        JOptionPane.showMessageDialog(null, "El árbol quedó vacío.");
                    }
                    
                } else {
                    mostrarDatoNoEncontrado(caracter);
                    encontrado = 0;
                }
            }

            

        } while (encontrado == 0 && raiz != null);

        return raiz;
    }


    public static void mostrarArbol(Nodo raiz) {
        // TODO: mostrar el arbol con el formato que decidas usar
    }

    private static void contarHojas(Nodo raiz, int[] hojas) {

        Nodo x = raiz;
        // recorrido 
        if (x.getLigaIzq() == null && x.getLigaDer() == null) {
            hojas[0]++; // si el nodo no tiene hijos, es una hoja, entonces se suma 1 a la cantidad de hojas
        } else {
            if (x.getLigaIzq() != null) {
                contarHojas(x.getLigaIzq(), hojas);
            }
            if (x.getLigaDer() != null) {
                contarHojas(x.getLigaDer(), hojas);
            }
        }
        
    }

    private static void contarPadres(Nodo raiz, int[] padres) {

        Nodo x = raiz;
        // recorrido  
        if (x.getLigaIzq() != null || x.getLigaDer() != null) {
            padres[0]++; // si el nodo tiene al menos un hijo, es un padre, entonces se suma 1 a la cantidad de padres
        }

        if (x.getLigaIzq() != null) {
            contarPadres(x.getLigaIzq(), padres);
        }
        if (x.getLigaDer() != null) {
            contarPadres(x.getLigaDer(), padres);
        }
    }

    private static Nodo obtenerMenor(Nodo raiz) {
        Nodo x = raiz;

        while (x.getLigaIzq() != null) {
            x = x.getLigaIzq();
        }

        return x;
    }

    private static Nodo buscarNodo(Nodo raiz, char dato) {
        Nodo x = raiz;

        if (x == null) {
            return null;
        }
        if (dato == x.getDato()) {
            return x;
        }
        if (dato < x.getDato()) {
            return buscarNodo(x.getLigaIzq(), dato);
        }

        return buscarNodo(x.getLigaDer(), dato);
    }

    private static Nodo buscarPadre(Nodo raiz, char dato) {
        Nodo x = raiz;

        if (x == null) {
            return null;
        }
        if ((x.getLigaIzq() != null && x.getLigaIzq().getDato() == dato)
            || (x.getLigaDer() != null && x.getLigaDer().getDato() == dato)) {
            return x;
        }
        if (dato < x.getDato()) {
            return buscarPadre(x.getLigaIzq(), dato);
        }

        return buscarPadre(x.getLigaDer(), dato);
    }

    private static int buscarNivel(Nodo raiz, char dato, int nivelActual) {
        Nodo x = raiz;

        if (x == null) {
            return -1;
        }
        if (x.getDato() == dato) {
            return nivelActual;
        }
        if (dato < x.getDato()) {
            return buscarNivel(x.getLigaIzq(), dato, nivelActual + 1);
        }

        return buscarNivel(x.getLigaDer(), dato, nivelActual + 1);
    }

    private static int calcularAltura(Nodo raiz) {
        Nodo x = raiz;

        if (x == null) {
            return -1;
        }

        int alturaIzq = calcularAltura(x.getLigaIzq());
        int alturaDer = calcularAltura(x.getLigaDer());

        if (alturaIzq > alturaDer) {
            return alturaIzq + 1;
        }

        return alturaDer + 1;
    }

    private static char pedirCaracter(String mensaje) {
        String entrada = JOptionPane.showInputDialog(mensaje);

        if (entrada == null) {
            JOptionPane.showMessageDialog(null, "Operación cancelada.");
            return '\0';
        }

        entrada = entrada.trim().toUpperCase();

        if (entrada.length() != 1) {
            JOptionPane.showMessageDialog(null, "Error: Solo puede ingresar 1 carácter.");
            return '\0';
        }

        return entrada.charAt(0);
    }

    private static void mostrarDatoNoEncontrado(char dato) {
        JOptionPane.showMessageDialog(null, "Error: \"" + dato + "\" no se encuentra en el árbol.");
    }

    private static void mostrarHermanoDeDato(Nodo raiz) {
        char dato = pedirCaracter("Ingrese el dato del que quiere ver el hermano:");

        if (dato == '\0') {
            return;
        }

        Nodo padre = buscarPadre(raiz, dato);
        if (padre == null) {
            JOptionPane.showMessageDialog(null, "El dato \"" + dato + "\" no tiene hermano.");
            return;
        }

        Nodo hermano = null;
        if (padre.getLigaIzq() != null && padre.getLigaIzq().getDato() == dato) {
            hermano = padre.getLigaDer();
        } else if (padre.getLigaDer() != null && padre.getLigaDer().getDato() == dato) {
            hermano = padre.getLigaIzq();
        }

        if (hermano == null) {
            JOptionPane.showMessageDialog(null, "El dato \"" + dato + "\" no tiene hermano.");
        } else {
            JOptionPane.showMessageDialog(null, "El hermano de \"" + dato + "\" es \"" + hermano.getDato() + "\".");
        }
    }

    private static void mostrarNivelDeDato(Nodo raiz) {
        char dato = pedirCaracter("Ingrese el dato del que quiere ver el nivel:");

        if (dato == '\0') {
            return;
        }

        int nivel = buscarNivel(raiz, dato, 0);
        if (nivel == -1) {
            mostrarDatoNoEncontrado(dato);
        } else {
            JOptionPane.showMessageDialog(null, "El nivel de \"" + dato + "\" es " + nivel + ".");
        }
    }

    private static void mostrarAlturaDeDato(Nodo raiz) {
        char dato = pedirCaracter("Ingrese el dato del que quiere ver la altura:");

        if (dato == '\0') {
            return;
        }

        Nodo nodo = buscarNodo(raiz, dato);
        if (nodo == null) {
            mostrarDatoNoEncontrado(dato);
        } else {
            JOptionPane.showMessageDialog(null, "La altura de \"" + dato + "\" es " + calcularAltura(nodo) + ".");
        }
    }

    private static void mostrarPrimosHermanosDeDato(Nodo raiz) {
        char dato = pedirCaracter("Ingrese el dato del que quiere ver los primos hermanos:");

        if (dato == '\0') {
            return;
        }

        Nodo nodo = buscarNodo(raiz, dato);
        Nodo padre = buscarPadre(raiz, dato);
        Nodo abuelo = padre == null ? null : buscarPadre(raiz, padre.getDato());

        if (nodo == null) {
            mostrarDatoNoEncontrado(dato);
            return;
        }
        if (padre == null || abuelo == null) {
            JOptionPane.showMessageDialog(null, "El dato \"" + dato + "\" no tiene primos hermanos.");
            return;
        }

        StringBuilder primos = new StringBuilder();
        recolectarPrimosHermanos(abuelo, padre, primos);

        if (primos.length() == 0) {
            JOptionPane.showMessageDialog(null, "El dato \"" + dato + "\" no tiene primos hermanos.");
        } else {
            JOptionPane.showMessageDialog(null, "Primos hermanos de \"" + dato + "\":\n" + primos);
        }
    }

    private static void recolectarPrimosHermanos(Nodo abuelo, Nodo padre, StringBuilder primos) {
        if (abuelo.getLigaIzq() != null && abuelo.getLigaIzq() != padre) {
            agregarHijos(abuelo.getLigaIzq(), primos);
        }
        if (abuelo.getLigaDer() != null && abuelo.getLigaDer() != padre) {
            agregarHijos(abuelo.getLigaDer(), primos);
        }
    }

    private static void agregarHijos(Nodo raiz, StringBuilder datos) {
        if (raiz.getLigaIzq() != null) {
            datos.append(raiz.getLigaIzq().getDato()).append(' ');
        }
        if (raiz.getLigaDer() != null) {
            datos.append(raiz.getLigaDer().getDato()).append(' ');
        }
    }

    private static void mostrarAncestrosDeDato(Nodo raiz) {
        char dato = pedirCaracter("Ingrese el dato del que quiere ver los ancestros:");

        if (dato == '\0') {
            return;
        }

        if (buscarNodo(raiz, dato) == null) {
            mostrarDatoNoEncontrado(dato);
            return;
        }

        StringBuilder ancestros = new StringBuilder();
        construirAncestros(raiz, dato, ancestros);

        if (ancestros.length() == 0) {
            JOptionPane.showMessageDialog(null, "El dato \"" + dato + "\" no tiene ancestros.");
        } else {
            JOptionPane.showMessageDialog(null, "Ancestros de \"" + dato + "\":\n" + ancestros);
        }
    }

    private static boolean construirAncestros(Nodo raiz, char dato, StringBuilder ancestros) {
        Nodo x = raiz;

        if (x == null) {
            return false;
        }
        if (x.getDato() == dato) {
            return true;
        }

        boolean encontrado;
        if (dato < x.getDato()) {
            encontrado = construirAncestros(x.getLigaIzq(), dato, ancestros);
        } else {
            encontrado = construirAncestros(x.getLigaDer(), dato, ancestros);
        }

        if (encontrado) {
            if (ancestros.length() == 0) {
                ancestros.append(x.getDato());
            } else {
                ancestros.insert(0, x.getDato() + " ");
            }
        }

        return encontrado;
    }





/*
    public void Construir(char Vc[]) {

        int i = 0;
        while (i < Vc.length) {

            if (true) {

            }

            AVL();

            i++;
        }
    }
    
    private void AVL() {
        
        // factor de balance
        FactorBalance();

        int R = ValidarRotacion();


        switch (R) {
            case 1:     // R.D
                
                break;
            case 2:     // R.I
                break;
            case 3:     // R.D.D
                break;
            case 4:     // R.D.I
                break;
        }
    }

 */
    }
