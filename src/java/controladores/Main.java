/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package controladores;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import servicios.Util;

/**
 *
 * @author rulyone
 */
public class Main {
    
    public static void main(String[] args) {
        String pass = Util.hashPassword("mundo");
        System.out.println(pass);
    }
    
}
