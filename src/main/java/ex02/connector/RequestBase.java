package ex02.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class RequestBase implements ServletRequest{

	public Object getAttribute(String name) {
		
		return null;
		
	}

	public Enumeration<String> getAttributeNames() {
		
		return null;
		
	}

	public String getCharacterEncoding() {
		
		return null;
		
	}

	public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
	}

	public int getContentLength() {
		
		return 0;
		
	}

	public long getContentLengthLong() {
		
		return 0;
		
	}

	public String getContentType() {
		
		return null;
		
	}

	public ServletInputStream getInputStream() throws IOException {
		
		return null;
		
	}

	public String getParameter(String name) {
		
		return null;
		
	}

	public Enumeration<String> getParameterNames() {
		
		return null;
		
	}

	public String[] getParameterValues(String name) {
		
		return null;
		
	}

	public Map<String, String[]> getParameterMap() {
		
		return null;
		
	}

	public String getProtocol() {
		
		return null;
		
	}

	public String getScheme() {
		
		return null;
		
	}

	public String getServerName() {
		
		return null;
		
	}

	public int getServerPort() {
		
		return 0;
		
	}

	public BufferedReader getReader() throws IOException {
		
		return null;
		
	}

	public String getRemoteAddr() {
		
		return null;
		
	}

	public String getRemoteHost() {
		
		return null;
		
	}

	public void setAttribute(String name, Object o) {
	}

	public void removeAttribute(String name) {
	}

	public Locale getLocale() {
		
		return null;
		
	}

	public Enumeration<Locale> getLocales() {
		
		return null;
		
	}

	public boolean isSecure() {
		
		return false;
		
	}

	public RequestDispatcher getRequestDispatcher(String path) {
		
		return null;
		
	}

	public String getRealPath(String path) {
		
		return null;
		
	}

	public int getRemotePort() {
		
		return 0;
		
	}

	public String getLocalName() {
		
		return null;
		
	}

	public String getLocalAddr() {
		
		return null;
		
	}

	public int getLocalPort() {
		
		return 0;
		
	}

	public ServletContext getServletContext() {
		
		return null;
		
	}

	public AsyncContext startAsync() throws IllegalStateException {
		
		return null;
		
	}

	public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
			throws IllegalStateException {
		
		return null;
		
	}

	public boolean isAsyncStarted() {
		
		return false;
		
	}

	public boolean isAsyncSupported() {
		
		return false;
		
	}

	public AsyncContext getAsyncContext() {
		
		return null;
		
	}

	public DispatcherType getDispatcherType() {
		
		return null;
		
	}

}
