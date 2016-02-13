package de.tmgdx.em.gui.screeens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpRequestHeader;
import com.badlogic.gdx.utils.Json;

public abstract class HttpRequestHelper extends HttpRequest {
	private static final int EXP_STATUS_CODE = 200;
	public static final String COMUNICATION_FORMAT = "application/json";
	public static final String SERVER_URL = "http://localhost:8080/SeminarEM_Tomcat/EMServer";

	public HttpRequestHelper() {
		super(HttpMethods.GET);
		this.setUrl(SERVER_URL);
	}

	public HttpRequestHelper(String httpMethod,HttpContentObject content) {
		super(httpMethod);
		this.setUrl(SERVER_URL);
		this.setHeader(HttpRequestHeader.ContentType, COMUNICATION_FORMAT);
		System.out.println(new Json().prettyPrint(content));//TODO
		this.setContent(new Json().toJson(content));
	}

	public void sendRequest() {
		Gdx.net.sendHttpRequest(this, new HttpResponseListener() {
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				if (isExpectedStatus(httpResponse)) {
					handleResponse(httpResponse);
				}
			}

			@Override
			public void failed(Throwable t) {
				handleFailed(t);
			}

			@Override
			public void cancelled() {
				handleCancelled();
				
			}
		});
	}

	protected void handleCancelled() {
		Gdx.app.log("Cancelled", "sendHttpRequest " + this.getMethod());
	}

	protected void handleFailed(Throwable t) {
		Gdx.app.error("HttpRequest", "something went wrong", t);
	}

	protected abstract void handleResponse(HttpResponse httpResponse);

	public static boolean isExpectedStatus(HttpResponse httpResponse) {
		if (httpResponse.getStatus().getStatusCode() != EXP_STATUS_CODE) {
			Gdx.app.log("unexpected Status code", ""
					+ httpResponse.getStatus().getStatusCode());
			Gdx.app.log("Result", httpResponse.getResultAsString());
			return false;
		} else
			return true;
	}
}
