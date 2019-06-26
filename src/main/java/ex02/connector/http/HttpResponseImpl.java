package ex02.connector.http;

import ex02.connector.HttpResponseBase;

public class HttpResponseImpl extends HttpResponseBase{

	private boolean allowChunking;

	public void setConnector(HttpConnector httpConnector) {
	}

    void setAllowChunking(boolean allowChunking) {
        this.allowChunking = allowChunking;
    }

	public void finishResponse() {
	}

	public void recycle() {
		super.recycle();
		allowChunking = false;
	}



}
