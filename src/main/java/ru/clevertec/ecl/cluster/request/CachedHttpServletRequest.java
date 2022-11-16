package ru.clevertec.ecl.cluster.request;

import java.io.IOException;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import ru.clevertec.ecl.util.constant.AppConstants;


public class CachedHttpServletRequest extends HttpServletRequestWrapper {

    @Getter
    @Setter
    private String body;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public CachedHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        body = readOriginalBody();
    }

    @Override
    public ServletInputStream getInputStream() {
        return new CachedServletInputStream(body.getBytes());
    }

    private String readOriginalBody() throws IOException {
        return IOUtils.toString(super.getInputStream(), AppConstants.ENCODING);
    }

    @RequiredArgsConstructor
    private static class CachedServletInputStream extends ServletInputStream {

        private final byte[] cachedBodyBytes;
        private int lastIndex = -1;

        @Override
        public boolean isFinished() {
            return (lastIndex == cachedBodyBytes.length - 1);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int read() {
            if (isFinished()) {
                return -1;
            }
            return cachedBodyBytes[++lastIndex];
        }
    }
}
