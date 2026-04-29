public class Nodo {
    
    // atributos

    private char dato;
    private Nodo ligaIzq, ligaDer;

    // constructor

    public Nodo (char dato) {
        this.dato = dato;

        this.ligaIzq = null;
        this.ligaDer = null;
    }

    // Getters y Setters

    public char getDato() {
        return dato;
    }

    public void setDato(char dato) {
        this.dato = dato;
    }

    public Nodo getLigaIzq() {
        return ligaIzq;
    }

    public void setLigaIzq(Nodo ligaIzq) {
        this.ligaIzq = ligaIzq;
    }

    public Nodo getLigaDer() {
        return ligaDer;
    }

    public void setLigaDer(Nodo ligaDer) {
        this.ligaDer = ligaDer;
    }
}
