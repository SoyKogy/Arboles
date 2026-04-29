import javax.swing.JOptionPane;

public class Arboles {
    public static void main(String[] args) throws Exception {
        
        String vector = "";
        char[] cadena;

        do {
            vector = JOptionPane.showInputDialog("Ingrese una cadena de letras para crear el arbol binario:");

            cadena = vector.toCharArray();
            
            for (int i = 0; i < cadena.length; i++) {
                for (int j = 0; j < cadena.length; j++) {
                    if (i == j) {
                        j++; // paraq ue no se compare consigo mismo
                    }
                    
                    if (cadena[i] == cadena[j]) {
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
}
