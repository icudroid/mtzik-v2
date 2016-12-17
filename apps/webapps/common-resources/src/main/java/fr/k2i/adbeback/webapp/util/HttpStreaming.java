package fr.k2i.adbeback.webapp.util;

import com.google.common.net.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * User: dimitri
 * Date: 15/05/14
 * Time: 17:23
 * Goal:
 */
public class HttpStreaming {

    public static ResponseEntity<byte[]> streaming(HttpServletRequest request, File file) throws IOException {
        byte[] bytes = null;

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();

        headers.add(HttpHeaders.CONTENT_TYPE, MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(file));

        bytes =  FileCopyUtils.copyToByteArray(file);

        String rang = request.getHeader(HttpHeaders.RANGE);
        if(rang==null){
            //send all file
            headers.add(HttpHeaders.CONTENT_LENGTH, bytes.length+"");
            return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.PARTIAL_CONTENT);
        }

        headers.add(HttpHeaders.ACCEPT_RANGES,"0-"+file.length());

        String rangeValue = rang.trim().substring("bytes=".length());

        long fileLength = file.length();
        long start, end;
        if (rangeValue.startsWith("-")) {
            end = fileLength - 1;
            start = fileLength - 1
                    - Long.parseLong(rangeValue.substring("-".length()));
        } else {
            String[] range = rangeValue.split("-");
            start = Long.parseLong(range[0]);
            end = range.length > 1 ? Long.parseLong(range[1])
                    : fileLength - 1;
        }
        if (end > fileLength - 1) {
            end = fileLength - 1;
        }
        if (start <= end) {
            long contentLength = end - start + 1;

            headers.add(HttpHeaders.CONTENT_LENGTH,contentLength+"");
            headers.add(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-"
                    + end + "/" + fileLength);

            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
            ByteArrayOutputStream out = new ByteArrayOutputStream(8192);
            //FileCopyUtils.copy(in,out);

            headers.add(HttpHeaders.CONTENT_LENGTH, contentLength+"");

            //bytes = new byte[(int) contentLength];
            out.write(bytes,(int)start,(int)contentLength);
            return new ResponseEntity<byte[]>(out.toByteArray(), headers, HttpStatus.PARTIAL_CONTENT);
        }


        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.PARTIAL_CONTENT);
    }

}
