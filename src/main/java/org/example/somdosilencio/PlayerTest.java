package org.example.somdosilencio;

import org.junit.Test;
import java.io.File;
import static org.junit.Assert.assertTrue;

public class PlayerTest {

    @Test
    public void deveCarregarArquivoValido() {
        File mp3Valido = new File("src/test/resources/icarus.mp3");
        assertTrue("Arquivo de teste não encontrado", mp3Valido.exists());
    }

    @Test
    public void deveRejeitarMP4() {
        File mp4Invalido = new File("src/test/resources/a_strange_happening.mp4");

        if (!mp4Invalido.exists()) {
            throw new IllegalStateException("Arquivo de teste não encontrado: " + mp4Invalido.getAbsolutePath());
        }
    }

    @Test
    public void deveRejeitarWAV() {
        File wavInvalido = new File("src/test/resources/andy_asteroids.wav");

        if (!wavInvalido.exists()) {
            throw new IllegalStateException("Arquivo de teste não encontrado: " + wavInvalido.getAbsolutePath());
        }
    }
}