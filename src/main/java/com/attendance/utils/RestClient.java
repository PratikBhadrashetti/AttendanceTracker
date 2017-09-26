package com.attendance.utils;

import java.io.IOException;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RestClient 
{
	private static final Logger logger = LoggerFactory.getLogger(RestClient.class);
    
	public final String get(String uri, Map<String, String> map) throws Exception 
	{
		//logger.debug("RestClient.get.uri:" + uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try 
        {
            HttpGet httpget = new HttpGet(uri);
            for (Map.Entry<String, String> entry: map.entrySet()) 
            {
            	httpget.setHeader(entry.getKey(), entry.getValue());
            	//logger.debug("RestClient.get.header:" + entry.getKey() + ":" + entry.getValue());
            }
            ResponseHandler<String> responseHandler = new ResponseHandler<String>()
            {
                @Override
                public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException 
                {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300)
                    {
                        HttpEntity entity = response.getEntity();
                        if (entity == null) 
                        {
                        	logger.debug("RestClient.get.result:null");
                        	return null;
                        } 
                        else 
                        {
                        	String result = EntityUtils.toString(entity);
                        	//logger.debug("RestClient.get.result:" + result);
                        	return result;
                        }
                    } 
                    else 
                    {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            return httpclient.execute(httpget, responseHandler);
        } 
        finally
        {
            httpclient.close();
        }
    }

	public final String post(String uri, Map<String, String> map, String body) throws Exception
	{
		//logger.debug("RestClient.post.uri:" + uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try 
        {
        	HttpPost httpPost = new HttpPost(uri);
        	if (null != map)
        	{
	            for (Map.Entry<String, String> entry: map.entrySet()) 
	            {
	            	httpPost.setHeader(entry.getKey(), entry.getValue());
	            	//logger.debug("RestClient.post.header:" + entry.getKey() + ":" + entry.getValue());
	            	System.out.println("RestClient.post.header:" + entry.getKey() + ":" + entry.getValue());
	            }
        	}
            if (null != body)
            {
            	System.out.println("RestClient.post.body:" + body);
            	//logger.debug("RestClient.post.body:" + body);
	            StringEntity entity = new StringEntity(body);
	            httpPost.setEntity(entity);
            }
            ResponseHandler<String> responseHandler = new ResponseHandler<String>()
            {
                @Override
                public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException
                {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) 
                    {
                        HttpEntity entity = response.getEntity();
                        if (entity == null)
                        {
                        	logger.debug("RestClient.post.result:null");
                        	return null;
                        }
                        else 
                        {
                        	String result = EntityUtils.toString(entity);
                        	//logger.debug("RestClient.post.result:" + result);
                        	return result;
                        }
                    } 
                    else
                    {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            return httpclient.execute(httpPost, responseHandler);
        }
        finally 
        {
            httpclient.close();
        }
    }
	
	
	
	public final String put(String uri, Map<String, String> map) throws Exception 
	{
		//logger.debug("RestClient.put.uri:" + uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try 
        {
        	HttpPut httpPut = new HttpPut(uri);
        	if (null != map) 
        	{
	            for (Map.Entry<String, String> entry: map.entrySet())
	            {
	            	httpPut.setHeader(entry.getKey(), entry.getValue());
	            	//logger.debug("RestClient.put.header:" + entry.getKey() + ":" + entry.getValue());
	            }
        	}
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() 
            {
                @Override
                public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException
                {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) 
                    {
                        HttpEntity entity = response.getEntity();
                        if (entity == null)
                        {
                        	logger.debug("RestClient.put.result:null");
                        	return null;
                        } 
                        else
                        {
                        	String result = EntityUtils.toString(entity);
                        	//logger.debug("RestClient.put.result:" + result);
                        	return result;
                        }
                    } 
                    else 
                    {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            return httpclient.execute(httpPut, responseHandler);
        }
        finally
        {
            httpclient.close();
        }
    }
	
	public final String put(String uri, Map<String, String> map, byte[] bytes) throws Exception
	{
		//logger.debug("RestClient.put.uri:" + uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try
        {
        	HttpPut httpPut = new HttpPut(uri);
        	if (null != map)
        	{
	            for (Map.Entry<String, String> entry: map.entrySet()) 
	            {
	            	httpPut.setHeader(entry.getKey(), entry.getValue());
	            	//logger.debug("RestClient.put.header:" + entry.getKey() + ":" + entry.getValue());
	            }
        	}
            if (null != bytes) 
            {
            	HttpEntity entity = new ByteArrayEntity(bytes);
	            httpPut.setEntity(entity);
            }
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() 
            {
                @Override
                public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException
                {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300)
                    {
                        HttpEntity entity = response.getEntity();
                        if (entity == null)
                        {
                        	logger.debug("RestClient.put.result:null");
                        	return null;
                        } 
                        else 
                        {
                        	String result = EntityUtils.toString(entity);
                        	//logger.debug("RestClient.put.result:" + result);
                        	return result;
                        }
                    }
                    else 
                    {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            return httpclient.execute(httpPut, responseHandler);
        } 
        finally
        {
            httpclient.close();
        }
    }
	
	public final Header[] postHeader(String uri, Map<String, String> map, String body) throws Exception 
	{
		//logger.debug("RestClient.postHeader.uri:" + uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try 
        {
        	HttpPost httpPost = new HttpPost(uri);
        	if (null != map) 
        	{
	            for (Map.Entry<String, String> entry: map.entrySet())
	            {
	            	httpPost.setHeader(entry.getKey(), entry.getValue());
	            	//logger.debug("RestClient.postHeader.header:" + entry.getKey() + ":" + entry.getValue());
	            }
        	}
            if (null != body)
            {
            	//logger.debug("RestClient.postHeader.body:" + body);
	            StringEntity entity = new StringEntity(body);
	            httpPost.setEntity(entity);
            }
            ResponseHandler<Header[]> responseHandler = new ResponseHandler<Header[]>()
            {
                @Override
                public Header[] handleResponse(final HttpResponse response) throws ClientProtocolException, IOException 
                {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300)
                    {
                        return response.getAllHeaders();
                    }
                    else
                    {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            return httpclient.execute(httpPost, responseHandler);
        } 
        finally
        {
            httpclient.close();
        }
    }
	
		
	public final String delete(String uri, Map<String, String> map) throws Exception 
	{
		//logger.info("RestClient.delete.uri:" + uri);
		CloseableHttpClient httpclient = HttpClients.createDefault();  
		try
		{
			HttpDelete httpDelete = new HttpDelete(uri);
			httpDelete.setHeader("Accept", "application/json");
			httpDelete.setHeader("Content-type", "application/json");
			if (httpDelete!=null)
			{
				for (Map.Entry<String, String> entry: map.entrySet()) 
				{
					httpDelete.addHeader(entry.getKey(), entry.getValue());
				}
			}
			ResponseHandler<String> responseHandler = new ResponseHandler<String>()
			{
				@Override
				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException
				{
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						if (entity == null) 
						{
							logger.info("RestClient.delete.result:null");
							return null;
						}
						else
						{
							String result = EntityUtils.toString(entity);
							//logger.info("RestClient.delete.result:" + result);
							return result;
						}
					} 
					else 
					{
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}
			};
			return httpclient.execute(httpDelete, responseHandler);
		} 
		finally 
		{
			httpclient.close();
		}
	}
}

