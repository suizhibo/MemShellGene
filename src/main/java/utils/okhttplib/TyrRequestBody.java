package utils.okhttplib;

import okhttp3.internal.Util;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;


public class TyrRequestBody {

    public static RequestBody create(MediaType contentType, String content, int chunkCount) throws Exception {
        Charset charset = Util.UTF_8;
        if (contentType != null) {
            charset = contentType.charset();
            if (charset == null) {
                charset = Util.UTF_8;
                contentType = MediaType.parse(contentType + "; charset=utf-8");
            }
        }

        byte[] bytes = content.getBytes(charset);
        return create(contentType, bytes, chunkCount);
    }

    public static RequestBody create(final MediaType contentType, final byte[] content, final int chunkCount) {
        if (chunkCount < 0) {
            return RequestBody.create(contentType, content, 0, content.length);
        }
        else {
            return new RequestBody() {
                @Override
                public MediaType contentType() {
                    return contentType;
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    int startIndex = 0;
                    int endIndex = chunkCount;
                    while (content.length > endIndex)
                    {
                        byte[] chunkedData = Arrays.copyOfRange(content, startIndex, endIndex);
                        sink.write(chunkedData, 0, chunkedData.length);
                        sink.flush();
                        startIndex = endIndex;
                        endIndex += chunkCount;
                    }
                    byte[] chunkedData = Arrays.copyOfRange(content, startIndex, content.length);
                    sink.write(chunkedData, 0, chunkedData.length);
                    sink.flush();
                }
            };
        }
    }

    public static RequestBody create(final MediaType contentType, final File file, final int chunkCount) {
        if (chunkCount < 0) {
            return RequestBody.create(contentType, file);
        }
        else {
            return new RequestBody() {
                @Override
                public MediaType contentType() { return contentType; }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    BufferedSource source = null;
                    try {
                        source = Okio.buffer(Okio.source(file));
                        int endIndex = chunkCount;
                        while (file.length() > endIndex) {
                            byte[] data = source.readByteArray(chunkCount);
                            sink.write(data, 0, data.length);
                            sink.flush();
                            endIndex += chunkCount;
                        }
                        long lastCount = (long)chunkCount - ((long)endIndex - file.length());
                        byte[] data = source.readByteArray(lastCount);
                        sink.write(data, 0, data.length);
                        sink.flush();
                    }
                    finally {
                        Util.closeQuietly(source);
                    }
                }
            };
        }
    }
}

