package pe.edu.idat.tushkunaapp.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class XssRequestWrapper extends HttpServletRequestWrapper {

    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return sanitize(value);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) return null;
        String[] sanitized = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            sanitized[i] = sanitize(values[i]);
        }
        return sanitized;
    }

    private String sanitize(String value) {
        if (value == null) return null;
        // Elimina caracteres peligrosos para XSS y SQL Injection
        return value
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("'", "&#x27;")
                .replaceAll("\"", "&quot;")
                .replaceAll("\\(", "&#40;")
                .replaceAll("\\)", "&#41;")
                .replaceAll("/", "&#x2F;")
                .replaceAll(";", "&#59;")
                .replaceAll("--", "")
                .replaceAll("\\bunion\\b", "")
                .replaceAll("\\bselect\\b", "")
                .replaceAll("\\bdrop\\b", "")
                .replaceAll("\\binsert\\b", "")
                .replaceAll("\\bdelete\\b", "")
                .replaceAll("\\bscript\\b", "");
    }
}