import javax.swing.JOptionPane;

public class Arboles {
    public static void main(String[] args) throws Exception {
        
        String vector = "";
        char[] vectorChar;

        do {
            vector = JOptionPane.showInputDialog("Ingrese una cadena de letras para crear el arbol binario:");

            vectorChar = vector.toCharArray();
            
            for (int i = 0; i < vectorChar.length; i++) {
                for (int j = 0; j < vectorChar.length; j++) {
                    if (vectorChar[i] == vectorChar[j]) {
                        JOptionPane.showMessageDialog(null, "El carácter \"" + vectorChar[i] + "\" se repite en la cadena.\nIntente de nuevo.");
                        vector = "";

                        i = vectorChar.length; // Salir del bucle externo
                        j = vectorChar.length; // Salir del bucle interno
                    }
                }
            }

        } while (vector == null || vector == "" || vector.isBlank()); 
        
        

    }
}
