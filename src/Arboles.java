import javax.swing.JOptionPane;

public class Arboles {
    public static void main(String[] args) throws Exception {
        
        char[] cadena = obtenerCadena(); // aquí se captura la cadena base con la que nacerá el árbol

        Nodo raiz = new Nodo(cadena[0]); // el primer carácter siempre se toma como raíz inicial
        crearArbol(raiz, cadena); // los demás caracteres se insertan respetando el orden de un ABB
        
        int opcion = 0;
        do {
            opcion = Menu(); // se vuelve a pedir opción hasta que el usuario decida salir
            switch (opcion) {
                case 1:
                    // StringBuilder se usa para ir armando el recorrido sin crear muchos String intermedios
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
                    // ojo: eliminar puede cambiar la raíz, por eso aquí sí se reasigna
                    raiz = eliminarNodoDesdeMenu(raiz);
                    break;
                case 6:
                    int[] hojas = new int[1]; // convierto el int en un "contenedor" para poder compartirlo entre llamadas recursivas
                    contarHojas(raiz, hojas);
                    JOptionPane.showMessageDialog(null, "Cantidad de hojas:\n\n" + hojas[0]);
                    break;
                case 7:
                    // misma idea que en hojas: el arreglo permite modificar el valor desde varios niveles de recursión
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
                case 14:
                    // TODO
                    break;
                case 15:
                    mostrarSiArbolPerfecto(raiz);
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

        // se retorna directamente el parseo porque el menú solo necesita entregar la opción elegida
        return Integer.parseInt(JOptionPane.showInputDialog(
            "\n ===== Menu Principal :) ====="
                + "\n1. Mostrar recorrido inorden"
                + "\n2. Mostrar recorrido preorden"
                + "\n3. Mostrar recorrido posorden"

                + "\n\n4. Insertar nodo" // esta opción luego muestra el árbol actual para que el usuario no repita letras
                + "\n5. Eliminar nodo" // aquí también se muestra el árbol para que el usuario elimine un dato existente
                + "\n6. Contar las hojas"
                + "\n7. Contar los padres"
                + "\n8. Mostrar arbol" // aparece en el menú, pero su lógica todavía está pendiente más abajo

                + "\n\n9. Mostrar el hermano de un dato"
                + "\n10. Mostrar el nivel de un dato"
                + "\n11. Mostrar la altura de un dato"
                + "\n12. Mostrar los primos hermanos de un dato"
                + "\n13. Mostrar los ancestros de un dato"
                + "\n\n14. Balancear arbol (AVL)" // también figura como futura funcionalidad, pero aún no tiene case implementado
                
                + "\n\n15. Mostrar si el arbol es perfecto"
                + "\n\n0. Salir"
                + "\nSeleccione una opcion"
        ));
    }

    public static char[] obtenerCadena() {

        String vector = "";
        char[] cadena;

        do {
            vector = (JOptionPane.showInputDialog("Ingrese una cadena de letras para crear el arbol binario:")).toUpperCase();
            // paso todo a mayúsculas para que "a" y "A" no cuenten como dos datos distintos
            cadena = vector.toCharArray();
            
            // este doble for valida que no haya caracteres repetidos en la cadena inicial
            for (int i = 0; i < cadena.length; i++) {
                for (int j = 0; j < cadena.length; j++) {
                    if (i == j) {
                        j++; // para que no se compare consigo mismo y se detecte falso "duplicado"
                    }

                    if (j < cadena.length && cadena[i] == cadena[j]) {
                        // si encuentra repetido, se invalida toda la entrada y se vuelve a pedir completa
                        JOptionPane.showMessageDialog(null, "El carácter \"" + cadena[i] + "\" se repite en la cadena.\nIntente de nuevo.");
                        vector = "";

                        i = cadena.length; // salir del bucle externo forzando su condición de corte
                        j = cadena.length; // salir del bucle interno por la misma razón
                    }
                }
            }

        } while (vector == null || vector == "" || vector.isBlank()); // no se aceptan cadenas vacías ni solo espacios

        return cadena;
    }

    public static String obtenerCaracter(String mensaje) {

        String caracter = "";

        do {
            caracter = JOptionPane.showInputDialog(mensaje);

            if (caracter == null) {
                caracter = ""; // si el usuario cancela, se trata como entrada inválida y se vuelve a pedir
            } else {
                caracter = caracter.toUpperCase(); // misma normalización que en obtenerCadena()
            }

            if (!caracter.isBlank() && caracter.length() > 1) {
                // primero se revisa que no esté en blanco; así evitamos dar este error cuando el usuario solo deja vacío
                JOptionPane.showMessageDialog(null, "Error: Solo puede ingresar 1 carácter.");
                caracter = "";
            }

        } while (caracter.isBlank());

        return caracter;
    }

    public static void crearArbol(Nodo raiz, char[] cadena) {

        Nodo x = raiz; // referencia auxiliar; la raíz real sigue siendo la misma

        // se empieza desde 1 porque cadena[0] ya fue usada para construir la raíz en main()
        for (int i = 1; i < cadena.length; i++) {
            Nodo nuevoNodo = new Nodo(cadena[i]);

            insertarNodo(x, nuevoNodo); // cada carácter se acomoda según si es menor o mayor al dato actual
        }

    }

    public static void insertarNodo(Nodo raiz, Nodo nodo) {
        Nodo x = raiz;

        // recorrido dentro del árbol actual para saber si insertar a la izquierda o a la derecha

        // IMPORTANTE: insertarNodo asume que 'dato' de 'raiz' y 'nodo' son distintos;
        // si se intentara insertar un repetido, no entra a ningún bloque y simplemente no se insertaría
        if (nodo.getDato() > x.getDato()) {
            if (x.getLigaDer() == null) {

                x.setLigaDer(nodo); // si a la derecha hay espacio, ese será el lugar final del nuevo nodo
            } else {
                insertarNodo(x.getLigaDer(), nodo); // si no hay espacio, se sigue bajando por ese subárbol
            }
        } else if (nodo.getDato() < x.getDato()) {
            if (x.getLigaIzq() == null) {

                x.setLigaIzq(nodo); // mismo criterio, pero ahora hacia el lado izquierdo
            } else {
                insertarNodo(x.getLigaIzq(), nodo);
            }
        }
    }

    public static Nodo eliminarNodo(Nodo raiz, char dato) {
        Nodo x = raiz;
        Nodo nodoResultado = x;
        // valores para nodoResultado:
        /* null = la rama queda vacía en este punto
           nodo = la rama conserva un nodo válido
         */

        if (x == null) { 
            nodoResultado = null; // caso base: se llegó a una rama vacía y no hay nada por borrar
        } else {
            if (dato < x.getDato()) {
                // al reasignar la liga, se conserva cualquier cambio que ocurra más abajo en la recursión
                x.setLigaIzq(eliminarNodo(x.getLigaIzq(), dato));
            } else if (dato > x.getDato()) {
                x.setLigaDer(eliminarNodo(x.getLigaDer(), dato));
            } else {
                // si entró aquí, ya encontramos el nodo que sí coincide con el dato pedido
                if (x.getLigaIzq() == null && x.getLigaDer() == null) {
                    nodoResultado = null; // caso 1: era hoja, así que simplemente desaparece
                } else if (x.getLigaIzq() == null) {
                    nodoResultado = x.getLigaDer(); // caso 2: solo tiene hijo derecho, ese hijo sube a su lugar
                } else if (x.getLigaDer() == null) {
                    nodoResultado = x.getLigaIzq(); // caso 3: solo tiene hijo izquierdo
                } else {
                    // caso 4: tiene dos hijos.
                    // se toma el menor del subárbol derecho (sucesor inorden) para no romper la propiedad del ABB
                    Nodo sucesor = obtenerMenor(x.getLigaDer());
                    x.setDato(sucesor.getDato()); // se copia solo el dato; no se mueve el nodo completo
                    x.setLigaDer(eliminarNodo(x.getLigaDer(), sucesor.getDato())); // luego se elimina el duplicado que quedó en la derecha
                    nodoResultado = x;
                }
            }
        }

        return nodoResultado;
    }

    private static void recorrerInorden(Nodo raiz, StringBuilder inorden) {
        Nodo x = raiz;

        // inorden: izquierda -> raíz -> derecha
        // por eso en un ABB este recorrido entrega los datos ordenados de menor a mayor

        // búsqueda en izquierda: siempre se intenta llegar al dato más pequeño disponible primero
        if (x.getLigaIzq() != null) {
            recorrerInorden(x.getLigaIzq(), inorden);
        }
        
        inorden.append(x.getDato() + " ");
        // se guarda el dato del nodo actual ya que:
        /*   - es el ultimo a la izquierda en esa rama
             - es el nodo raiz del anterior nodo a la izquierda
             - es un nodo derecha sin hijo izquierdo
         */

        // búsqueda en derecha: después de procesar la raíz actual, se continúa con los mayores
        if (x.getLigaDer() != null) {
            recorrerInorden(x.getLigaDer(), inorden); // si hay a la derecha, se entra en esa rama
        }
    }

    private static void recorrerPreorden(Nodo raiz, StringBuilder preorden) {
        
        Nodo x = raiz;

        // preorden:   raíz -> izquierda -> derecha
        preorden.append(x.getDato() + " "); // aquí la raíz se guarda de primero, antes de bajar

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
        // este recorrido suele ser útil cuando se quiere "cerrar" un subárbol antes de procesar su raíz

        if (x.getLigaIzq() != null) {
            recorrerPosorden(x.getLigaIzq(), posorden);
        }
        if (x.getLigaDer() != null) {
            recorrerPosorden(x.getLigaDer(), posorden);
        }

        posorden.append(x.getDato() + " "); // la raíz queda de última dentro de su propio subárbol
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
            recorrerPreorden(raiz, cadena); // se usa preorden solo para mostrar el estado actual del árbol al usuario

            String nuevoNodo = (JOptionPane.showInputDialog("Arbol actual (preorden):\n"+cadena+"\nIngrese un carácter que no se encuentre en el árbol.\n\nNo ingrese espacios ni puntos.")).toUpperCase();

            if (nuevoNodo.length() > 1) {
                JOptionPane.showMessageDialog(null, "Error: Solo puede ingresar 1 carácter.");
                encontrado = 1;
            }

            if (encontrado == 0) {
                char caracter = nuevoNodo.charAt(0);
                
                // se revisa sobre la cadena mostrada porque allí ya están listados todos los nodos del árbol actual
                for (int i = 0; i < cadena.length() && encontrado == 0; i++) {
                    if (cadena.charAt(i) == caracter) {
                        JOptionPane.showMessageDialog(null, "Error: \""+caracter+"\" ya se encuentra en el árbol.");
                        encontrado = 1;
                    }
                }

                if (encontrado == 0) {
                    Nodo nodo = new Nodo(caracter);
                    insertarNodo(raiz, nodo); // ya validado que no existe, se puede insertar sin romper la suposición de insertarNodo()

                    StringBuilder nuevaCadena = new StringBuilder();
                    recorrerPreorden(raiz, nuevaCadena);
                    JOptionPane.showMessageDialog(null, nuevaCadena); // se muestra el árbol actualizado para confirmar el cambio
                    
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

            String entrada = obtenerCaracter(
                "Arbol actual (preorden):\n" + cadena
                + "\nIngrese un carácter que se encuentre en el árbol.\n\nNo ingrese espacios ni puntos."
            );

            if (encontrado == 1) {
                char caracter = entrada.charAt(0);
                if (buscarNodo(raiz, caracter) != null) {
                    raiz = eliminarNodo(raiz, caracter); // se reasigna por si el nodo borrado era la raíz actual

                    StringBuilder nuevaCadena = new StringBuilder();
                    if (raiz != null) {
                        recorrerPreorden(raiz, nuevaCadena);
                        JOptionPane.showMessageDialog(null, nuevaCadena);
                    } else {
                        // este mensaje aclara la duda típica de "¿se dañó?" cuando en realidad se borró el último nodo
                        JOptionPane.showMessageDialog(null, "El árbol quedó vacío.");
                    }
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Error: \"" + caracter + "\" no se encuentra en el árbol.");
                    encontrado = 0;
                }
            }

            

        } while (encontrado == 0 && raiz != null);

        return raiz;
    }


    public static void mostrarArbol(Nodo raiz) {
        // TODO: mostrar el árbol con una representación visual más clara que un simple recorrido lineal
    }

    private static void contarHojas(Nodo raiz, int[] hojas) {

        /*

        IMPORTANTE: contarHojas ASUME que ya se creó una variable int[0]
        para contar las hojas

         */
        Nodo x = raiz;
        // recorrido completo del árbol: una hoja es un nodo sin hijo izquierdo ni derecho
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

        /*

        IMPORTANTE: contarHojas ASUME que ya se creó una variable int[0]
        para contar las hojas

         */

        Nodo x = raiz;
        // recorrido completo: aquí "padre" significa cualquier nodo que tenga al menos un hijo
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
        Nodo menor = x;
        // valores para menor:
        /* nodo = menor actual mientras se recorre
           nodo mas a la izquierda = menor definitivo del subárbol
         */

        // en un BST, el menor de cualquier subárbol siempre está en la rama más a la izquierda
        while (menor.getLigaIzq() != null) {
            menor = menor.getLigaIzq();
        }

        return menor;
    }

    private static Nodo buscarNodo(Nodo raiz, char dato) {
        Nodo x = raiz;
        Nodo nodoEncontrado = null;
        // valores para nodoEncontrado:
        /* null = el dato no se encontró
           nodo = el dato sí se encontró
         */

        if (x == null) {
            nodoEncontrado = null; // no se encontró en esta ruta
        } else if (dato == x.getDato()) {
            nodoEncontrado = x; // caso encontrado
        } else if (dato < x.getDato()) {
            // si el dato buscado es menor, no tiene sentido revisar la derecha en un ABB
            nodoEncontrado = buscarNodo(x.getLigaIzq(), dato);
        } else {
            // y si es mayor, solo puede estar en la derecha
            nodoEncontrado = buscarNodo(x.getLigaDer(), dato);
        }

        return nodoEncontrado;
    }

    private static Nodo buscarPadre(Nodo raiz, char dato) {
        Nodo x = raiz;
        Nodo padreEncontrado = null;
        // valores para padreEncontrado:
        /* null = no se encontró padre para ese dato
           nodo = sí se encontró el padre del dato
         */

        if (x == null) {
            padreEncontrado = null;
        } else if ((x.getLigaIzq() != null && x.getLigaIzq().getDato() == dato)
            || (x.getLigaDer() != null && x.getLigaDer().getDato() == dato)) {
            padreEncontrado = x; // si uno de sus hijos coincide, entonces el actual es el padre buscado
        } else if (dato < x.getDato()) {
            padreEncontrado = buscarPadre(x.getLigaIzq(), dato);
        } else {
            padreEncontrado = buscarPadre(x.getLigaDer(), dato);
        }

        return padreEncontrado;
    }

    private static int buscarNivel(Nodo raiz, char dato, int nivelActual) {
        Nodo x = raiz;
        int nivelEncontrado = -1;
        // valores para nivelEncontrado:
        /* -1 = el dato no se encontró
           0 o mas = nivel del dato dentro del árbol
         */

        if (x == null) {
            nivelEncontrado = -1; // según Gemini: una rama inexistente mide -1
        } else if (x.getDato() == dato) {
            nivelEncontrado = nivelActual; // el nivel se va acumulando a medida que se baja desde la raíz
        } else if (dato < x.getDato()) {
            nivelEncontrado = buscarNivel(x.getLigaIzq(), dato, nivelActual + 1);
        } else {
            nivelEncontrado = buscarNivel(x.getLigaDer(), dato, nivelActual + 1);
        }

        return nivelEncontrado;
    }

    private static int calcularAltura(Nodo raiz) {
        Nodo x = raiz;
        int alturaCalculada = -1;
        // valores para alturaCalculada:
        /* -1 = rama inexistente
           0 o mas = altura calculada del nodo o subárbol
         */

        if (x == null) {
            alturaCalculada = -1; // según Gemini: una rama inexistente mide -1, así una hoja termina midiendo 0
        } else {
            int alturaIzq = calcularAltura(x.getLigaIzq());
            int alturaDer = calcularAltura(x.getLigaDer());

            if (alturaIzq > alturaDer) {
                alturaCalculada = alturaIzq + 1; // se suma 1 por el nodo actual
            } else {
                alturaCalculada = alturaDer + 1;
            }
        }

        return alturaCalculada;
    }

    private static void mostrarHermanoDeDato(Nodo raiz) {
        String entrada = obtenerCaracter("Ingrese el dato del que quiere ver el hermano:");
        char dato = entrada.charAt(0);
        int tienePadre = 1;
        // valores para tienePadre:
        /* 0 = el dato no tiene padre
           1 = el dato sí tiene padre
         */

        Nodo padre = buscarPadre(raiz, dato);
        if (padre == null) {
            // esto pasa si el dato es la raíz o si ese nodo simplemente no tiene un hermano al otro lado
            JOptionPane.showMessageDialog(null, "El dato \"" + dato + "\" no tiene hermano.");
            tienePadre = 0;
        }

        if (tienePadre == 1) {
            Nodo hermano = null;
            if (padre.getLigaIzq() != null && padre.getLigaIzq().getDato() == dato) {
                hermano = padre.getLigaDer(); // si el dato está a la izquierda, su hermano sería el hijo derecho del mismo padre
            } else if (padre.getLigaDer() != null && padre.getLigaDer().getDato() == dato) {
                hermano = padre.getLigaIzq();
            }

            if (hermano == null) {
                JOptionPane.showMessageDialog(null, "El dato \"" + dato + "\" no tiene hermano.");
            } else {
                JOptionPane.showMessageDialog(null, "El hermano de \"" + dato + "\" es \"" + hermano.getDato() + "\".");
            }
        }
    }

    private static void mostrarNivelDeDato(Nodo raiz) {
        String entrada = obtenerCaracter("Ingrese el dato del que quiere ver el nivel:");
        char dato = entrada.charAt(0);

        int nivel = buscarNivel(raiz, dato, 0); // la raíz arranca en nivel 0
        if (nivel == -1) {
            JOptionPane.showMessageDialog(null, "Error: \"" + dato + "\" no se encuentra en el árbol.");
        } else {
            JOptionPane.showMessageDialog(null, "El nivel de \"" + dato + "\" es " + nivel + ".");
        }
    }

    private static void mostrarAlturaDeDato(Nodo raiz) {
        String entrada = obtenerCaracter("Ingrese el dato del que quiere ver la altura:");
        char dato = entrada.charAt(0);

        Nodo nodo = buscarNodo(raiz, dato);
        if (nodo == null) {
            JOptionPane.showMessageDialog(null, "Error: \"" + dato + "\" no se encuentra en el árbol.");
        } else {
            // se calcula la altura tomando ese nodo como nueva raíz del subárbol correspondiente
            JOptionPane.showMessageDialog(null, "La altura de \"" + dato + "\" es " + calcularAltura(nodo) + ".");
        }
    }

    private static void mostrarPrimosHermanosDeDato(Nodo raiz) {
        String entrada = obtenerCaracter("Ingrese el dato del que quiere ver los primos hermanos:");
        char dato = entrada.charAt(0);
        
        int puedeBuscarPrimos = 1;
        // valores para puedeBuscarPrimos:
        /* 0 = no se puede continuar la búsqueda de primos hermanos
           1 = sí se puede continuar la búsqueda de primos hermanos
         */

        Nodo nodo = buscarNodo(raiz, dato);
        Nodo padre = buscarPadre(raiz, dato);
        Nodo abuelo = null;

        if (padre != null) {
            abuelo = buscarPadre(raiz, padre.getDato()); // si sé quién es el padre, busco quién es el padre de ese padre
        }

        if (nodo == null) {
            JOptionPane.showMessageDialog(null, "Error: \"" + dato + "\" no se encuentra en el árbol.");
            puedeBuscarPrimos = 0;
        }
        if (padre == null || abuelo == null) {
            JOptionPane.showMessageDialog(null, "El dato \"" + dato + "\" no tiene primos hermanos.");
            puedeBuscarPrimos = 0;
        }

        if (puedeBuscarPrimos == 1) {
            StringBuilder primos = new StringBuilder();
            if (abuelo.getLigaIzq() != null && abuelo.getLigaIzq() != padre) {
                // si el padre está al otro lado del abuelo, este subárbol sí puede contener primos hermanos
                if (abuelo.getLigaIzq().getLigaIzq() != null) {
                    primos.append(abuelo.getLigaIzq().getLigaIzq().getDato()).append(' ');
                }
                if (abuelo.getLigaIzq().getLigaDer() != null) {
                    primos.append(abuelo.getLigaIzq().getLigaDer().getDato()).append(' ');
                }
            }
            if (abuelo.getLigaDer() != null && abuelo.getLigaDer() != padre) {
                if (abuelo.getLigaDer().getLigaIzq() != null) {
                    primos.append(abuelo.getLigaDer().getLigaIzq().getDato()).append(' ');
                }
                if (abuelo.getLigaDer().getLigaDer() != null) {
                    primos.append(abuelo.getLigaDer().getLigaDer().getDato()).append(' ');
                }
            }

            if (primos.length() == 0) {
                JOptionPane.showMessageDialog(null, "El dato \"" + dato + "\" no tiene primos hermanos.");
            } else {
                JOptionPane.showMessageDialog(null, "Primos hermanos de \"" + dato + "\":\n" + primos);
            }
        }
    }

    private static void mostrarAncestrosDeDato(Nodo raiz) {
        String entrada = obtenerCaracter("Ingrese el dato del que quiere ver los ancestros:");
        char dato = entrada.charAt(0);
        int datoExiste = 1;
        // valores para datoExiste:
        /* 0 = el dato no existe en el árbol
           1 = el dato sí existe en el árbol
         */

        if (buscarNodo(raiz, dato) == null) {
            JOptionPane.showMessageDialog(null, "Error: \"" + dato + "\" no se encuentra en el árbol.");
            datoExiste = 0;
        }

        if (datoExiste == 1) {
            StringBuilder ancestros = new StringBuilder();
            construirAncestros(raiz, dato, ancestros); // este método llena el StringBuilder mientras la recursión "desenrolla"

            if (ancestros.length() == 0) {
                // si no se agregó nada, el dato existe pero era la raíz, así que no tiene ancestros
                JOptionPane.showMessageDialog(null, "El dato \"" + dato + "\" no tiene ancestros.");
            } else {
                JOptionPane.showMessageDialog(null, "Ancestros de \"" + dato + "\":\n" + ancestros);
            }
        }
    }

    private static boolean construirAncestros(Nodo raiz, char dato, StringBuilder ancestros) {
        Nodo x = raiz;
        boolean encontrado = false;
        // valores para encontrado:
        /* false = el dato no está en esta ruta
           true = el dato sí está en esta ruta
         */

        if (x == null) {
            encontrado = false;
        } else if (x.getDato() == dato) {
            encontrado = true; // al encontrar el dato, empieza el retorno hacia arriba marcando la ruta correcta
        } else {
            if (dato < x.getDato()) {
                encontrado = construirAncestros(x.getLigaIzq(), dato, ancestros);
            } else {
                encontrado = construirAncestros(x.getLigaDer(), dato, ancestros);
            }

            if (encontrado) {
                if (ancestros.length() == 0) {
                    ancestros.append(x.getDato()); // el primer ancestro agregado es el padre directo
                } else {
                    ancestros.insert(0, x.getDato() + " "); // los demás se ponen al inicio para conservar el orden desde la raíz
                }
            }
        }

        return encontrado;
    }


    private static void mostrarSiArbolPerfecto(Nodo raiz) {
        int verificar = 1;
        // valores para verificar:
        /* 0 = el arbol está vacío
           1 = el  arbol existe con por lo menos 1 nodo
        */

        if (raiz == null) {
            JOptionPane.showMessageDialog(null, "El árbol está vacío.");
            verificar = 0;
        }

        if (verificar == 1) { // si sí hay nodos en el arbol
            int altura = calcularAltura(raiz);
            int cantidadHojas = (int) Math.pow(2, altura); // segun la teoria de BST, un arbol es perfecto si 
                                                            // hojas = 2^altura

            int[] hojasContadas = new int[1]; // se usa un arreglo para poder pasarlo por referencia
            contarHojas(raiz, hojasContadas);

            if (hojasContadas[0] == cantidadHojas) {
                JOptionPane.showMessageDialog(null, "Arbol perfecto");
            } else {
                JOptionPane.showMessageDialog(null, "Arbol imperfecto");
            }
        }
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
