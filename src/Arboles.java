import javax.swing.JOptionPane;

public class Arboles {
    public static void main(String[] args) throws Exception {
        
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
                    JOptionPane.showMessageDialog(null, "Recorrido inorden:\n" + preorden);
                    break;
                case 3:
                    StringBuilder posorden = new StringBuilder();
                    recorrerPosorden(raiz, posorden);
                    JOptionPane.showMessageDialog(null, "Recorrido inorden:\n" + posorden);
                    break;
                case 4:
                    insertarNodoDesdeMenu(raiz);
                    break;
                case 5:
                    mostrarArbol(raiz);
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
                + "\n4. Insertar nodo" // para esto recordar hacer el input tipo "arbol actual: DFESMG. inserte un nodo distinto a los ya existentes"
                + "\n5. Mostrar arbol"
                + "\n\n0. Salir"
                + "\nSeleccione una opcion"
        ));
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

    public static void recorrerInorden(Nodo raiz, StringBuilder inorden) {
        Nodo x = raiz;

        // inorden izquierda -> raíz -> derecha
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

    public static void recorrerPreorden(Nodo raiz, StringBuilder preorden) {
        
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

    public static void recorrerPosorden(Nodo raiz, StringBuilder posorden) {
        Nodo x = raiz;

        // preorden:   raíz -> izquierda -> derecha

        if (x.getLigaIzq() != null) {
            recorrerPreorden(x.getLigaIzq(), posorden);
        }
        if (x.getLigaDer() != null) {
            recorrerPreorden(x.getLigaDer(), posorden);
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

    public static void mostrarArbol(Nodo raiz) {
        // TODO: mostrar el arbol con el formato que decidas usar
    }
}
