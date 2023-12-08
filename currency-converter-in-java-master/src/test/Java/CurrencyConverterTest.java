import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import currencyConverter.MainWindow; 
import currencyConverter.Currency; 
import java.util.ArrayList;

public class CurrencyConverterTest {
//validé
    @Test
     public void testValidCurrencyConversion() {
     ArrayList<Currency> currencies = Currency.init(); // Utilisation de Currency.init()

                // Testez la conversion de devises valides avec différents montants
    double result1 = MainWindow.convert("US Dollar", "CAD", currencies, 100.0);
    double result2 = MainWindow.convert("British Pound", "Euro", currencies, 50.0);
    double result3 = MainWindow.convert("Chinese Yuan Renminbi", "AUD", currencies, 75.0);
    
    assertEquals(0.0, result1); // CAD is not a valid currency 
    assertEquals(70.5, result2); // 50 GBP should convert to 58.75 EUR
    assertEquals(0.0, result3); // AUD is not a valid currency 
    
     // Testez la conversion de montants nuls
     double result4 = MainWindow.convert("US Dollar", "Euro", currencies, 0.0);
     assertEquals(0.0, result4, "0 USD devrait se convertir en 0 Euro");
 
     // Testez la conversion de montants élevés
     double result5 = MainWindow.convert("Euro", "US Dollar", currencies, 1000000.0);
     assertEquals(1073000.0,result5 , "La conversion de 1,000,000 EUR en USD devrait être valide");
     
     // Testez la conversion de montants en dehors de la plage [0, 1 000 000]
     double result6 = MainWindow.convert("Euro", "British Pound", currencies, -50.0);
     assertEquals(-35.5, result6, "Une conversion negatif est valide ");
     
     double result7 = MainWindow.convert("US Dollar", "US Dollar", currencies, 100.0);
     assertEquals(100.0, result7, "La conversion de USD à USD devrait être valide");
 }
 
//validé 
    @Test
    public void testInvalidCurrencyConversion() {
        ArrayList<Currency> currencies = Currency.init();
        
        // Conversion avec devise cible invalide
        double result1 = MainWindow.convert("US Dollar", "XYZ", currencies, 100.0);
        assertEquals(0.0, result1, "La conversion avec une devise cible invalide devrait retourner 0.0");
    
        // Conversion avec devise source invalide
        double result2 = MainWindow.convert("XYZ", "US Dollar", currencies, 100.0);
        assertEquals(0.0, result2, "La conversion avec une devise source invalide devrait retourner 0.0");
    
        
        double result4 = MainWindow.convert("US Dollar", "Japanese Yen", currencies, 5000000.0);
        assertEquals(6.177E8, result4, "Un montant supérieur à 1 000 000 est valide ");
    }
    //validé
    @Test
    public void testCurrencyConversionWithinSpecifiedRange() {
        ArrayList<Currency> currencies = Currency.init();
    
        // Trouver le taux de change entre Euro et US Dollar
        double exchangeRateEuroToUsd = currencies.get(1).getExchangeValues().get("USD");
        assertEquals(1.073,exchangeRateEuroToUsd);//verifier qu'on a le bon rate
        assertEquals(100.0 * exchangeRateEuroToUsd, Currency.convert(100.0, exchangeRateEuroToUsd)); // Montant valide
    
        // Testez la limite inférieure de la plage
        assertEquals(0.0, Currency.convert(0.0, exchangeRateEuroToUsd)); // Montant minimal
    
        // Testez la limite supérieure de la plage
        assertEquals(1000000.0 * exchangeRateEuroToUsd, Currency.convert(1000000.0, exchangeRateEuroToUsd)); // Montant maximal
    }
       
 //validé
@Test
public void testCurrencyConversionOutsideSpecifiedRange() {
    // Initialisation des devises
    ArrayList<Currency> currencies = Currency.init();
    
    // Trouver le taux de change entre British Pound et Swiss Franc
    double exchangeRateGbpToChf = currencies.get(2).getExchangeValues().get("CHF");

    // Test avec un montant négatif (hors plage)
    double result1 = Currency.convert(-100.0, exchangeRateGbpToChf);
    assertEquals(-152.0, result1, "Un montant négatif devrait être converti en utilisant le taux de change GBP-CHF");

    // Test avec un montant supérieur à la limite maximale (hors plage)
    double result2 = Currency.convert(1000001.0, exchangeRateGbpToChf);
    assertEquals(1520001.52, result2, "Un montant supérieur à la limite devrait être converti en utilisant le taux de change GBP-CHF");
}

//test boite blanche pour main.windown
@Test
public void testCurrencyNotFoundInList() {
    ArrayList<Currency> currencies = Currency.init();
    double result = MainWindow.convert("US Dollar", "Nonexistent Currency", currencies, 100.0);
    assertEquals(0.0, result); // Hypothèse: retourne 0.0 si la devise n'est pas trouvée
}
@Test
public void testShortNameCurrency2Found() {
    ArrayList<Currency> currencies = Currency.init();
    double result = MainWindow.convert("US Dollar", "Euro", currencies, 100.0);
    assertTrue(result > 0); // Vérifie que le résultat est positif, indiquant une conversion réussie
}
@Test
public void testShortNameCurrency2NotFound() {
    ArrayList<Currency> currencies = Currency.init();
    double result = MainWindow.convert("US Dollar", "Nonexistent Currency", currencies, 100.0);
    assertEquals(0.0, result); // Devrait être 0.0 si la devise n'est pas trouvée
}
@Test
public void testValidAndInvalidCurrencyCombination() {
    ArrayList<Currency> currencies = Currency.init();
    double result1 = MainWindow.convert("US Dollar", "Euro", currencies, 100.0);
    double result2 = MainWindow.convert("Nonexistent Currency", "Euro", currencies, 100.0);

    assertTrue(result1 > 0); // Conversion valide
    assertEquals(0.0, result2); // Conversion invalide
}

@Test
public void testValidAndInvalidAmounts() {
    ArrayList<Currency> currencies = Currency.init();
    double result1 = MainWindow.convert("US Dollar", "Euro", currencies, 100.0);
    double result2 = MainWindow.convert("US Dollar", "Euro", currencies, -100.0);

    assertTrue(result1 > 0); // Montant valide
    assertTrue(result2 < 0); // Montant négatif traité comme une conversion valide
}
@Test
public void testCurrencyNameMatchCondition() {
    ArrayList<Currency> currencies = Currency.init();
    for (Currency currency : currencies) {
        double result = MainWindow.convert("US Dollar", currency.getName(), currencies, 100.0);
        assertTrue(result >= 0); // Assure que la conversion a réussi ou renvoie 0 pour la devise inexistante
    }
}
//test boite  blanche pour currency.convert 
@Test
public void testCurrencyConversionWithDifferentAmounts() {
    assertEquals(105.0, Currency.convert(100.0, 1.05));
    assertEquals(0.0, Currency.convert(0.0, 1.05));
    assertEquals(-105.0, Currency.convert(-100.0, 1.05));
}

@Test
public void testCurrencyConversionWithDifferentExchangeRates() {
    assertEquals(100.0, Currency.convert(100.0, 1.0));
    assertEquals(200.0, Currency.convert(100.0, 2.0));
    assertEquals(50.0, Currency.convert(100.0, 0.5));
}

@Test
public void testCurrencyConversionRounding() {
    assertEquals(100.13, Currency.convert(95.365, 1.05));
    assertEquals(100.12, Currency.convert(95.35, 1.05));
}

}
