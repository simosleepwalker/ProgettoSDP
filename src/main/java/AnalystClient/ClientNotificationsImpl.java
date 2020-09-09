package AnalystClient;

import analyst.client.Analyst;
import analyst.client.AnalystClientGrpc;
import com.sun.jersey.api.container.filter.LoggingFilter;
import io.grpc.stub.StreamObserver;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;

public class ClientNotificationsImpl extends AnalystClientGrpc.AnalystClientImplBase {

    public Integer getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    private Integer id;
    private String ip;
    private Integer port;

    @Override
    public void notify(Analyst.notificationMessage request, StreamObserver<Analyst.okMessage> responseObserver) {
        System.out.println(request.getMessage());
        Analyst.okMessage response = Analyst.okMessage.newBuilder().setMessage("Ok").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public Beans.AnalystClient getBean () {
        return new Beans.AnalystClient(this.id,this.ip,this.port);
    }

    public void connectToServer () {
        Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
        WebTarget webTarget = client.target("http://localhost:8080/simple_service_webapp_war/webapi/analyst/insert_analyst");
        Beans.AnalystClient analystBean = this.getBean();
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        invocationBuilder.post(Entity.json(analystBean));
    }

    public void exit () {
        Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
        WebTarget webTarget = client.target("http://localhost:8080/simple_service_webapp_war/webapi/analyst/remove_analyst");
        Beans.AnalystClient analystBean = this.getBean();
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        invocationBuilder.post(Entity.json(analystBean));
    }

    public ClientNotificationsImpl(Integer id, String ip, Integer port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
    }

}
