public class Nodo {
    
    // atributos

    private int dato;
    private Nodo ligaIzq, ligaDer;

    // constructor

    public Nodo (int dato) {
        this.dato = dato;

        this.ligaIzq = null;
        this.ligaDer = null;
    }
    
    // Getters y Setters

    public int getDato() {
        return dato;
    }

    public void setDato(int dato) {
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
