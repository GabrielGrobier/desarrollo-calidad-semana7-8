package cl.duoc.tienda.ejemplos;

/**
 * EJEMPLO DIDÁCTICO 2: lógica booleana con varias reglas combinadas.
 * Una clave válida requiere: largo mínimo de 8, al menos un dígito,
 * al menos una mayúscula y al menos una minúscula.
 * Sirve para practicar assertTrue/assertFalse y casos límite (boundary testing).
 */
public class ValidadorClave {

    public boolean esValida(String clave) {
        if (clave == null) {
            return false;
        }
        boolean tieneLongitudMinima = clave.length() >= 8;
        boolean tieneNumero = clave.chars().anyMatch(Character::isDigit);
        boolean tieneMayuscula = clave.chars().anyMatch(Character::isUpperCase);
        boolean tieneMinuscula = clave.chars().anyMatch(Character::isLowerCase);
        return tieneLongitudMinima && tieneNumero && tieneMayuscula && tieneMinuscula;
    }
}
