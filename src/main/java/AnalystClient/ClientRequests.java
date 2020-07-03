package AnalystClient;

import Beans.StatisticsList;
import com.sun.jersey.api.container.filter.LoggingFilter;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.message.internal.MessageBodyProviderNotFoundException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ClientRequests {

    private Client client;

    public Integer getNodes () {
        WebTarget webTarget = this.client.target("http://localhost:8080/simple_service_webapp_war/webapi/analyst/get_nodes_number");
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        Integer nodesNumber = response.readEntity(Integer.class);
        return nodesNumber;
    }

    public StatisticsList getStats (Integer n) {
        WebTarget webTarget = this.client.target("http://localhost:8080/simple_service_webapp_war/webapi/analyst/get_last_stats").queryParam("n",n.toString());
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        return response.readEntity(StatisticsList.class);
    }

    public Double getDevStandard (Integer n) throws MessageBodyProviderNotFoundException {
        WebTarget webTarget = this.client.target("http://localhost:8080/simple_service_webapp_war/webapi/analyst/get_dev").queryParam("n",n.toString());
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        return response.readEntity(Double.class);
    }

    public Double getMedia (Integer n) throws MessageBodyProviderNotFoundException {
        WebTarget webTarget = this.client.target("http://localhost:8080/simple_service_webapp_war/webapi/analyst/get_med").queryParam("n",n.toString());
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        return response.readEntity(Double.class);
    }

    public ClientRequests () {
        this.client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
    }

}
