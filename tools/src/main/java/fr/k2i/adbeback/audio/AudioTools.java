package fr.k2i.adbeback.audio;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * User: dimitri
 * Date: 03/10/14
 * Time: 10:58
 * Goal:
 */
@Component
public class AudioTools {

    public void copySampleAudio(String sourceFileName, String destinationFileName) throws IOException, InterruptedException {
        String encodeCmd = "cutmp3 -i {0} -O {1} -a 0:00 -b 0:60";
        String cmd = MessageFormat.format(encodeCmd, sourceFileName ,destinationFileName);
        Process exec = Runtime.getRuntime().exec(cmd);
        exec.waitFor();
    }

}
