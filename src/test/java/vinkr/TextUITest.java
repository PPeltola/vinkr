package vinkr;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.rules.Timeout;
import static org.mockito.Mockito.*;
import vinkr.vinkit.KirjaVinkki;
import vinkr.vinkit.Vinkki;

public class TextUITest {
    
    Vinkr vinkr;
    Validoija validoija;
    TextUI ui;
    PrintStream uiInput;
    ByteArrayOutputStream uiOutput;
    ArrayList<Vinkki> vinkit;
    
    @Before
    public void setUp() throws IOException {
        PipedOutputStream uiInputPiped = new PipedOutputStream();
        uiInput = new PrintStream(uiInputPiped, true);
        PipedInputStream input = new PipedInputStream(uiInputPiped);
        
        uiOutput = new ByteArrayOutputStream();
        
        vinkit = new ArrayList<>();
        vinkit.add(new KirjaVinkki("Formal Development of Programs and Proofs", "Dijkstra, Edsger", "978-0201172379"));
        vinkit.add(new KirjaVinkki("Refactoring", "Fowler, Martin", "0201485672"));
        vinkr = mock(Vinkr.class);
        when(vinkr.getVinkit()).thenReturn(vinkit);
        validoija = mock(Validoija.class);
        when(validoija.validoiOtsikko(anyString())).thenReturn(true);
        when(validoija.validoiTekija(anyString())).thenReturn(true);
        when(validoija.validoiIsbn(anyString())).thenReturn(true);
        ui = new TextUI(vinkr, input, uiOutput);
    }
    
    @Test
    public void lisaaKomentoLisaaKirjan() {
        uiInput.println("lisaa");
        uiInput.println("Dijkstra, Edsger");
        uiInput.println("A Discipline of Programming");
        uiInput.println("013215871X");
        uiInput.println("lopeta");
        ui.run();
        verify(vinkr).lisaaVinkki(any());
    }
    
    @Test
    public void listaaKomentoTulostaaKirjat() {
        uiInput.println("listaa");
        uiInput.println("lopeta");
        ui.run();
        
        String output = getOutput();
        for (Vinkki vinkki : vinkit) {
            assertTrue(output.contains(vinkki.tulosta()));
        }
    }
    
    private String getOutput() {
        return new String(uiOutput.toByteArray());
    }
}
